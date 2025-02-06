package com.ll.hotel.domain.hotel.room.service;

import com.ll.hotel.domain.hotel.hotel.entity.Hotel;
import com.ll.hotel.domain.hotel.hotel.repository.HotelRepository;
import com.ll.hotel.domain.hotel.option.roomOption.entity.RoomOption;
import com.ll.hotel.domain.hotel.option.roomOption.repository.RoomOptionRepository;
import com.ll.hotel.domain.hotel.room.dto.GetRoomDetailResponse;
import com.ll.hotel.domain.hotel.room.dto.GetRoomResponse;
import com.ll.hotel.domain.hotel.room.dto.GetRoomOptionResponse;
import com.ll.hotel.domain.hotel.room.dto.RoomDto;
import com.ll.hotel.domain.hotel.room.dto.PostRoomRequest;
import com.ll.hotel.domain.hotel.room.dto.PostRoomResponse;
import com.ll.hotel.domain.hotel.room.dto.PutRoomRequest;
import com.ll.hotel.domain.hotel.room.dto.PutRoomResponse;
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
import com.ll.hotel.global.exceptions.ServiceException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RoomService {
    private final ImageService imageService;
    private final S3Service s3Service;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final RoomOptionRepository roomOptionRepository;

    @BusinessOnly
    @Transactional
    public PostRoomResponse createRoom(long hotelId, Member actor, PostRoomRequest postRoomRequest) {
        Hotel hotel = this.getHotelById(hotelId);

        if (!hotel.isOwnedBy(actor)) {
            throw new ServiceException("403-2", "해당 호텔의 사업가가 아닙니다.");
        }

        BedTypeNumber bedTypeNumber = BedTypeNumber.fromJson(postRoomRequest.bedTypeNumber());

        Set<RoomOption> roomOptions = this.roomOptionRepository.findByNameIn(postRoomRequest.roomOptions());

        if (roomOptions.size() != postRoomRequest.roomOptions().size()) {
            throw new ServiceException("404-2", "사용할 수 없는 객실 옵션이 존재합니다.");
        }

        Room room = Room.builder()
                .hotel(hotel)
                .roomName(postRoomRequest.roomName())
                .roomNumber(postRoomRequest.roomNumber())
                .basePrice(postRoomRequest.basePrice())
                .standardNumber(postRoomRequest.standardNumber())
                .maxNumber(postRoomRequest.maxNumber())
                .bedTypeNumber(bedTypeNumber)
                .roomOptions(roomOptions)
                .build();

        try {
            return new PostRoomResponse(this.roomRepository.save(room),
                    this.saveRoomImages(room.getId(), postRoomRequest.imageExtensions()));
        } catch (DataIntegrityViolationException e) {
            throw new ServiceException("409-1", "동일한 이름의 방이 이미 호텔에 존재합니다.");
        }
    }

    @BusinessOnly
    @Transactional
    public void saveImages(Member actor, ImageType imageType, long roomId, List<String> urls) {
        this.imageService.saveImages(imageType, roomId, urls);
    }

    @BusinessOnly
    @Transactional
    public void deleteRoom(long hotelId, long roomId, Member actor) {
        Hotel hotel = this.getHotelById(hotelId);

        if (!hotel.isOwnedBy(actor)) {
            throw new ServiceException("403-2", "해당 호텔의 사업가가 아닙니다.");
        }

        Room room = this.getRoomById(roomId);

        room.setRoomStatus(RoomStatus.UNAVAILABLE);

        if (this.imageService.deleteImages(ImageType.ROOM, roomId) > 0) {
            this.s3Service.deleteAllObjectsById(ImageType.ROOM, roomId);
        }
    }

    @Transactional
    public List<GetRoomResponse> findAllRooms(long hotelId) {
        return this.roomRepository.findAllRooms(hotelId, ImageType.ROOM).stream()
                .map(GetRoomResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public GetRoomDetailResponse findRoomDetail(long hotelId, long roomId) {
        checkHotelExists(hotelId);

        Room room = this.getRoomDetail(hotelId, roomId);

        List<String> imageUrls = this.imageService.findImagesById(ImageType.ROOM, roomId).stream()
                .map(Image::getImageUrl)
                .toList();

        return new GetRoomDetailResponse(new RoomDto(room), imageUrls);
    }

    @Transactional
    public GetRoomOptionResponse findRoomOptions(long hotelId, long roomId) {
        this.checkHotelExists(hotelId);

        Room room = this.getRoomById(roomId);

        return new GetRoomOptionResponse(room);
    }

    @BusinessOnly
    @Transactional
    public PutRoomResponse modifyRoom(long hotelId, long roomId, Member actor, PutRoomRequest request) {
        Hotel hotel = this.getHotelById(hotelId);

        if (!hotel.isOwnedBy(actor)) {
            throw new ServiceException("403-2", "해당 호텔의 사업가가 아닙니다.");
        }

        Room room = this.getRoomDetail(hotelId, roomId);

        if (this.roomRepository.existsByHotelIdAndRoomNameAndIdNot(hotelId, request.roomName(), roomId)) {
            throw new ServiceException("409-1", "동일한 이름의 방이 이미 호텔에 존재합니다.");
        }

        modifyIfPresent(request.roomName(), room::getRoomName, room::setRoomName);
        modifyIfPresent(request.roomNumber(), room::getRoomNumber, room::setRoomNumber);
        modifyIfPresent(request.basePrice(), room::getBasePrice, room::setBasePrice);
        modifyIfPresent(request.standardNumber(), room::getStandardNumber, room::setStandardNumber);
        modifyIfPresent(request.maxNumber(), room::getMaxNumber, room::setMaxNumber);
        modifyIfPresent(request.bedTypeNumber(), room::getBedTypeNumber, room::setBedTypeNumber);

        if (request.roomStatus() != null) {
            try {
                room.setRoomStatus(RoomStatus.valueOf(request.roomStatus().toUpperCase()));
            } catch (Exception e) {
                throw new ServiceException("404-3", "객실 상태 정보를 정확히 입력해주세요.");
            }
        }

        modifyOptions(room, request.roomOptions());

        List<String> deleteImageUrls = request.deleteImageUrls();

        this.imageService.deleteImagesByIdAndUrls(ImageType.ROOM, roomId, deleteImageUrls);
        this.s3Service.deleteObjectsByUrls(deleteImageUrls);

        return new PutRoomResponse(room, this.saveRoomImages(roomId, request.imageExtensions()));
    }

    private <T> void modifyIfPresent(T newValue, Supplier<T> getter, Consumer<T> setter) {
        if (newValue != null && !newValue.equals(getter.get())) {
            setter.accept(newValue);
        }
    }

    @Transactional
    public void modifyOptions(Room room, Set<String> optionNames) {
        if (optionNames == null || optionNames.isEmpty()) {
            room.setRoomOptions(new HashSet<>());
            return;
        }

        Set<RoomOption> options = this.roomOptionRepository.findByNameIn(optionNames);

        if (options.size() != optionNames.size()) {
            throw new ServiceException("404-4", "사용할 수 없는 객실 옵션이 존재합니다.");
        }

        room.setRoomOptions(options);
    }

    private Hotel getHotelById(long hotelId) {
        return this.hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ServiceException("404-1", "호텔 정보가 존재하지 않습니다."));
    }

    private Room getRoomById(long roomId) {
        return this.roomRepository.findById(roomId)
                .orElseThrow(() -> new ServiceException("404-2", "객실 정보가 존재하지 않습니다."));
    }

    private Room getRoomDetail(long hotelId, long roomId) {
        return this.roomRepository.findRoomDetail(hotelId, roomId)
                .orElseThrow(() -> new ServiceException("404-2", "객실 정보를 조회할 수 없습니다."));
    }

    private void checkHotelExists(long hotelId) {
        if (!this.hotelRepository.existsById(hotelId)) {
            throw new ServiceException("404-1", "호텔 정보가 존재하지 않습니다.");
        }
    }

    // 객실 이미지 저장
    private PresignedUrlsResponse saveRoomImages(long roomId, List<String> extensions) {
        List<URL> urls = this.s3Service.generatePresignedUrls(ImageType.ROOM, roomId, extensions);

        return new PresignedUrlsResponse(roomId, urls);
    }
}
