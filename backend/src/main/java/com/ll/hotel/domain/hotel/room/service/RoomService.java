package com.ll.hotel.domain.hotel.room.service;

import com.ll.hotel.domain.hotel.hotel.entity.Hotel;
import com.ll.hotel.domain.hotel.hotel.repository.HotelRepository;
import com.ll.hotel.domain.hotel.option.entity.RoomOption;
import com.ll.hotel.domain.hotel.option.repository.RoomOptionRepository;
import com.ll.hotel.domain.hotel.room.dto.*;
import com.ll.hotel.domain.hotel.room.entity.Room;
import com.ll.hotel.domain.hotel.room.repository.RoomRepository;
import com.ll.hotel.domain.hotel.room.type.BedTypeNumber;
import com.ll.hotel.domain.hotel.room.type.RoomStatus;
import com.ll.hotel.domain.image.entity.Image;
import com.ll.hotel.domain.image.service.ImageService;
import com.ll.hotel.domain.image.type.ImageType;
import com.ll.hotel.domain.member.member.entity.Member;
import com.ll.hotel.domain.review.review.dto.response.PresignedUrlsResponse;
import com.ll.hotel.global.annotation.BusinessOnly;
import com.ll.hotel.global.aws.s3.S3Service;
import com.ll.hotel.global.exceptions.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RoomService {
    private final ImageService imageService;
    private final S3Service s3Service;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final RoomOptionRepository roomOptionRepository;

    // 객실 등록
    @BusinessOnly
    @Transactional
    public PostRoomResponse createRoom(long hotelId, Member actor, PostRoomRequest postRoomRequest) {
        Hotel hotel = this.getHotelById(hotelId);

        // 호텔 소유자가 아닌 경우
        if (!hotel.isOwnedBy(actor)) {
            ErrorCode.INVALID_BUSINESS.throwServiceException();
        }

        BedTypeNumber bedTypeNumber = BedTypeNumber.fromJson(postRoomRequest.bedTypeNumber());

        Set<RoomOption> roomOptions = this.roomOptionRepository.findByNameIn(postRoomRequest.roomOptions());

        // 요청한 옵션 중 존재하지 않는 옵션이 있을 경우
        if (roomOptions.size() != postRoomRequest.roomOptions().size()) {
            ErrorCode.ROOM_OPTION_NOT_FOUND.throwServiceException();
        }

        Room room = Room.from(hotel, postRoomRequest, bedTypeNumber, roomOptions);

        try {
            return new PostRoomResponse(this.roomRepository.save(room),
                    this.saveRoomImages(room.getId(), postRoomRequest.imageExtensions()));
        } catch (DataIntegrityViolationException e) {   // 중복된 객실명인 경우
            throw ErrorCode.ROOM_NAME_ALREADY_EXISTS.throwServiceException();
        }
    }

    // 객실 사진 URL 리스트 저장
    @BusinessOnly
    @Transactional
    public void saveImages(Member actor, long roomId, List<String> urls) {
        Hotel hotel = this.getRoomById(roomId).getHotel();

        // 호텔 소유자가 아닌 경우
        if (!hotel.isOwnedBy(actor)) {
            ErrorCode.INVALID_BUSINESS.throwServiceException();
        }

        this.imageService.saveImages(ImageType.ROOM, roomId, urls);
    }

    // 객실 삭제
    @BusinessOnly
    @Transactional
    public void deleteRoom(long hotelId, long roomId, Member actor) {
        Hotel hotel = this.getHotelById(hotelId);

        // 호텔 소유자가 아닌 경우
        if (!hotel.isOwnedBy(actor)) {
            ErrorCode.INVALID_BUSINESS.throwServiceException();
        }

        Room room = this.getRoomById(roomId);

        room.setRoomStatus(RoomStatus.UNAVAILABLE);

        // 삭제한 이미지가 있다면 S3에서 삭제
        if (this.imageService.deleteImages(ImageType.ROOM, roomId) > 0) {
            this.s3Service.deleteAllObjectsById(ImageType.ROOM, roomId);
        }
    }

    // 호텔의 모든 객실 조회
    @Transactional(readOnly = true)
    public List<GetRoomResponse> findAllRooms(long hotelId) {
        return this.roomRepository.findAllRooms(hotelId, ImageType.ROOM).stream()
                .map(GetRoomResponse::new)
                .collect(Collectors.toList());
    }

    // 객실 상세 조회
    @Transactional(readOnly = true)
    public GetRoomDetailResponse findRoomDetail(long hotelId, long roomId) {
        // 호텔 존재 여부 확인
        this.checkHotelExists(hotelId);

        Room room = this.getRoomDetail(hotelId, roomId);

        List<String> imageUrls = this.imageService.findImagesById(ImageType.ROOM, roomId).stream()
                .map(Image::getImageUrl)
                .toList();

        return new GetRoomDetailResponse(new RoomDto(room), imageUrls);
    }

    // 객실 수정
    @BusinessOnly
    @Transactional
    public PutRoomResponse modifyRoom(long hotelId, long roomId, Member actor, PutRoomRequest request) {
        Hotel hotel = this.getHotelById(hotelId);

        // 호텔 소유자가 아닌 경우
        if (!hotel.isOwnedBy(actor)) {
            ErrorCode.INVALID_BUSINESS.throwServiceException();
        }

        Room room = this.getRoomDetail(hotelId, roomId);

        // 이미 존재하는 객실명인 경우
        if (this.roomRepository.existsByHotelIdAndRoomNameAndIdNot(hotelId, request.roomName(), roomId)) {
            ErrorCode.ROOM_NAME_ALREADY_EXISTS.throwServiceException();
        }

        // 변경된 필드만 수정
        this.modifyIfPresent(request.roomName(), room::getRoomName, room::setRoomName);
        this.modifyIfPresent(request.roomNumber(), room::getRoomNumber, room::setRoomNumber);
        this.modifyIfPresent(request.basePrice(), room::getBasePrice, room::setBasePrice);
        this.modifyIfPresent(request.standardNumber(), room::getStandardNumber, room::setStandardNumber);
        this.modifyIfPresent(request.maxNumber(), room::getMaxNumber, room::setMaxNumber);
        this.modifyIfPresent(request.bedTypeNumber(), room::getBedTypeNumber, room::setBedTypeNumber);

        if (request.roomStatus() != null) {
            try {
                room.setRoomStatus(RoomStatus.valueOf(request.roomStatus().toUpperCase()));
            } catch (Exception e) {
                ErrorCode.ROOM_STATUS_NOT_FOUND.throwServiceException();
            }
        }

        this.modifyOptions(room, request.roomOptions());

        List<String> deleteImageUrls = request.deleteImageUrls();

        this.imageService.deleteImagesByIdAndUrls(ImageType.ROOM, roomId, deleteImageUrls);
        this.s3Service.deleteObjectsByUrls(deleteImageUrls);

        return new PutRoomResponse(room, this.saveRoomImages(roomId, request.imageExtensions()));
    }

    // null이 아니고, 기존 값과 같지 않은 필드 수정
    private <T> void modifyIfPresent(T newValue, Supplier<T> getter, Consumer<T> setter) {
        if (newValue != null && !newValue.equals(getter.get())) {
            setter.accept(newValue);
        }
    }

    // 객실 옵션 수정
    @Transactional
    public void modifyOptions(Room room, Set<String> optionNames) {
        // 요청한 옵션이 없을 경우
        if (optionNames == null || optionNames.isEmpty()) {
            room.setRoomOptions(new HashSet<>());
            return;
        }

        Set<RoomOption> options = this.roomOptionRepository.findByNameIn(optionNames);

        // 요청한 옵션 중 존재하지 않는 옵션이 있을 경우
        if (options.size() != optionNames.size()) {
            ErrorCode.ROOM_OPTION_NOT_FOUND.throwServiceException();
        }

        room.setRoomOptions(options);
    }

    // 호텔 ID로 호텔 조회
    private Hotel getHotelById(long hotelId) {
        return this.hotelRepository.findById(hotelId)
                .orElseThrow(ErrorCode.HOTEL_NOT_FOUND::throwServiceException);
    }

    // 객실 ID로 객실 조회
    private Room getRoomById(long roomId) {
        return this.roomRepository.findById(roomId)
                .orElseThrow(ErrorCode.ROOM_NOT_FOUND::throwServiceException);
    }

    // 호텔 ID와 객실 ID로 객실 상세 조회
    private Room getRoomDetail(long hotelId, long roomId) {
        return this.roomRepository.findRoomDetail(hotelId, roomId)
                .orElseThrow(ErrorCode.ROOM_NOT_FOUND::throwServiceException);
    }

    // 호텔 존재 여부 확인
    private void checkHotelExists(long hotelId) {
        if (!this.hotelRepository.existsById(hotelId)) {
            ErrorCode.HOTEL_NOT_FOUND.throwServiceException();
        }
    }

    // 객실 이미지 저장
    private PresignedUrlsResponse saveRoomImages(long roomId, List<String> extensions) {
        List<URL> urls = this.s3Service.generatePresignedUrls(ImageType.ROOM, roomId, extensions);

        return new PresignedUrlsResponse(roomId, urls);
    }

    // 모든 객실 옵션 조회
    @BusinessOnly
    @Transactional(readOnly = true)
    public GetAllRoomOptionsResponse findAllRoomOptions(Member actor) { // AOP로 BusinessOnly 처리
        return new GetAllRoomOptionsResponse(this.roomOptionRepository.findAll());
    }
}
