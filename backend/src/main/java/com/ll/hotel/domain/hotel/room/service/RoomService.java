package com.ll.hotel.domain.hotel.room.service;

import com.ll.hotel.domain.hotel.hotel.entity.Hotel;
import com.ll.hotel.domain.hotel.hotel.repository.HotelRepository;
import com.ll.hotel.domain.hotel.option.roomOption.entity.RoomOption;
import com.ll.hotel.domain.hotel.option.roomOption.repository.RoomOptionRepository;
import com.ll.hotel.domain.hotel.room.dto.GetAllRoomResponse;
import com.ll.hotel.domain.hotel.room.dto.GetRoomOptionResponse;
import com.ll.hotel.domain.hotel.room.dto.GetRoomResponse;
import com.ll.hotel.domain.hotel.room.dto.PostRoomRequest;
import com.ll.hotel.domain.hotel.room.dto.PostRoomResponse;
import com.ll.hotel.domain.hotel.room.dto.PutRoomRequest;
import com.ll.hotel.domain.hotel.room.dto.PutRoomResponse;
import com.ll.hotel.domain.hotel.room.entity.Room;
import com.ll.hotel.domain.hotel.room.repository.RoomRepository;
import com.ll.hotel.domain.hotel.room.type.BedTypeNumber;
import com.ll.hotel.domain.hotel.room.type.RoomStatus;
import com.ll.hotel.domain.image.type.ImageType;
import com.ll.hotel.domain.member.member.entity.Member;
import com.ll.hotel.global.exceptions.ServiceException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RoomService {
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final RoomOptionRepository roomOptionRepository;

    /**
     * RoomImage 등록 추가 필요
     */
    @Transactional
    public PostRoomResponse create(long hotelId, Member actor, PostRoomRequest postRoomRequest) {
        Hotel hotel = this.getHotel(hotelId);

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
                // 객실 이미지 등록 추가 필요
                .roomOptions(roomOptions)
                .build();

        return new PostRoomResponse(this.roomRepository.save(room));
    }

    @Transactional
    public void delete(long hotelId, long roomId, Member actor) {
        Hotel hotel = this.getHotel(hotelId);

        if (!hotel.isOwnedBy(actor)) {
            throw new ServiceException("403-2", "해당 호텔의 사업가가 아닙니다.");
        }

        Room room = this.getRoom(roomId);

        room.setRoomStatus(RoomStatus.UNAVAILABLE);
    }

    @Transactional
    public List<GetAllRoomResponse> findAllRooms(long hotelId) {
        List<Room> rooms = this.roomRepository.findAllRooms(hotelId, ImageType.ROOM);

        return rooms.stream()
                .map(GetAllRoomResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public GetRoomResponse findRoomDetail(long hotelId, long roomId) {
        checkHotelExists(hotelId);

        Room room = this.getRoomDetail(hotelId, roomId);

        return new GetRoomResponse(room);
    }

    @Transactional
    public GetRoomOptionResponse findRoomOptions(long hotelId, long roomId) {
        this.checkHotelExists(hotelId);

        Room room = this.getRoom(roomId);

        return new GetRoomOptionResponse(room);
    }

    @Transactional
    public PutRoomResponse modify(long hotelId, long roomId, Member actor, PutRoomRequest request) {
        Hotel hotel = this.getHotel(hotelId);

        if (!hotel.isOwnedBy(actor)) {
            throw new ServiceException("403-2", "해당 호텔의 사업가가 아닙니다.");
        }

        Room room = this.getRoomDetail(hotelId, roomId);

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

        // 이미지 수정 처리 필요

        modifyOptions(room, request.roomOptions());

        return new PutRoomResponse(room);
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

    private Hotel getHotel(long hotelId) {
        return this.hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ServiceException("404-1", "호텔 정보가 존재하지 않습니다."));
    }

    private Room getRoom(long roomId) {
        return this.roomRepository.findById(roomId)
                .orElseThrow(() -> new ServiceException("404-2", "객실 정보가 존재하지 않습니다."));
    }

    private Room getRoomDetail(long hotelId, long roomId) {
        return this.roomRepository.findRoomDetail(hotelId, roomId, ImageType.ROOM)
                .orElseThrow(() -> new ServiceException("404-2", "객실 정보를 조회할 수 없습니다."));
    }

    private void checkHotelExists(long hotelId) {
        if (!this.hotelRepository.existsById(hotelId)) {
            throw new ServiceException("404-1", "호텔 정보가 존재하지 않습니다.");
        }
    }
}
