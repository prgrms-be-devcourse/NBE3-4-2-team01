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
import com.ll.hotel.domain.image.ImageType;
import com.ll.hotel.global.exceptions.ServiceException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
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

    @Transactional
    public PostRoomResponse create(long hotelId, PostRoomRequest postRoomRequest) {
        Hotel hotel = this.hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ServiceException("404-1", "호텔의 정보가 존재하지 않습니다."));

        BedTypeNumber bedTypeNumber = BedTypeNumber.fromJson(postRoomRequest.bedTypeNumber());
        Set<RoomOption> roomOptions = new HashSet<>();

        for (String name : postRoomRequest.roomOptions()) {
            RoomOption roomOption = RoomOption.builder().name(name).build();
            roomOptions.add(roomOption);
            this.roomOptionRepository.save(roomOption);
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

        return new PostRoomResponse(this.roomRepository.save(room));
    }

    @Transactional
    public void delete(long hotelId, long roomId) {
        Hotel hotel = this.hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ServiceException("404-1", "호텔 정보를 찾을 수 없습니다."));

        Room room = this.roomRepository.findById(roomId)
                .orElseThrow(() -> new ServiceException("404-2", "객실 정보를 찾을 수 없습니다."));

        room.setRoomStatus(RoomStatus.UNAVAILABLE);
    }

    @Transactional
    public List<GetAllRoomResponse> findAllRooms(long hotelId) {
        List<Room> rooms = this.roomRepository.findAllRooms(hotelId, ImageType.ROOM);
        List<GetAllRoomResponse> responses = new ArrayList<>();

        for (Room room : rooms) {
            responses.add(new GetAllRoomResponse(room));
        }

        return responses;
    }

    @Transactional
    public GetRoomResponse findRoomDetail(long hotelId, long roomId) {
        Room room = this.roomRepository.findRoomDetail(hotelId, roomId, ImageType.ROOM)
                .orElseThrow(() -> new ServiceException("404-1", "호텔, 또는 객실 정보가 존재하지 않습니다."));

        return new GetRoomResponse(room);
    }

    @Transactional
    public GetRoomOptionResponse findRoomOptions(long hotelId, long roomId) {
        Hotel hotel = this.hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ServiceException("404-1", "호텔 정보가 존재하지 않습니다."));

        Room room = this.roomRepository.findRoomOptionsById(roomId)
                .orElseThrow(() -> new ServiceException("404-2", "객실, 혹은 객실 옵션 정보가 존재하지 않습니다."));

        return new GetRoomOptionResponse(room);
    }

    @Transactional
    public PutRoomResponse modify(long hotelId, long roomId, PutRoomRequest request) {
        Room room = this.roomRepository.findRoomDetail(hotelId, roomId, ImageType.ROOM)
                .orElseThrow(() -> new ServiceException("404-1", "객실 정보를 조회할 수 없습니다."));

        modifyIfPresent(request.roomName(), room::setRoomName);
        modifyIfPresent(request.roomNumber(), room::setRoomNumber);
        modifyIfPresent(request.basePrice(), room::setBasePrice);
        modifyIfPresent(request.standardNumber(), room::setStandardNumber);
        modifyIfPresent(request.maxNumber(), room::setMaxNumber);
        modifyIfPresent(request.bedTypeNumber(), room::setBedTypeNumber);

        if (request.roomStatus() != null) {
            try {
                room.setRoomStatus(RoomStatus.valueOf(request.roomStatus().toUpperCase()));
            } catch (Exception e) {
                throw new ServiceException("404-2", "객실 상태 정보를 정확히 입력해주세요.");
            }
        }

        // 이미지 수정 처리 필요

        modifyOptions(room, request.roomOptions());

        return new PutRoomResponse(room);
    }

    private <T> void modifyIfPresent(T newValue, Consumer<T> setter) {
        if (newValue != null) {
            setter.accept(newValue);
        }
    }

    @Transactional
    public void modifyOptions(Room room, Set<String> optionNames) {
        if (optionNames == null || optionNames.isEmpty()) {
            room.getRoomOptions().clear();
            return;
        }

        List<RoomOption> options = this.roomOptionRepository.findByNames(room.getHotel().getId(), room.getId(),
                optionNames);

        Set<String> validNames = options.stream()
                .map(RoomOption::getName)
                .collect(Collectors.toSet());

        Set<String> inValidNames = optionNames.stream()
                .filter(name -> !validNames.contains(name))
                .collect(Collectors.toSet());

        if (!inValidNames.isEmpty()) {
            throw new ServiceException("404-3", "객실 옵션 정보가 정확하지 않습니다.");
        }

        Set<RoomOption> curOptions = room.getRoomOptions();
        Set<RoomOption> newOptions = new HashSet<>(options);

        Set<RoomOption> toAdd = newOptions.stream()
                .filter(option -> !curOptions.contains(option))
                .collect(Collectors.toSet());

        Set<RoomOption> toRemove = curOptions.stream()
                .filter(option -> !newOptions.contains(option))
                .collect(Collectors.toSet());

        curOptions.addAll(toAdd);
        curOptions.removeAll(toRemove);
    }
}
