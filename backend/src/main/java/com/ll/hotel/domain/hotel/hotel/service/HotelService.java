package com.ll.hotel.domain.hotel.hotel.service;

import com.ll.hotel.domain.hotel.hotel.dto.GetAllHotelResponse;
import com.ll.hotel.domain.hotel.hotel.dto.HotelDto;
import com.ll.hotel.domain.hotel.hotel.dto.PostHotelRequest;
import com.ll.hotel.domain.hotel.hotel.dto.PostHotelResponse;
import com.ll.hotel.domain.hotel.hotel.dto.PutHotelRequest;
import com.ll.hotel.domain.hotel.hotel.dto.PutHotelResponse;
import com.ll.hotel.domain.hotel.hotel.entity.Hotel;
import com.ll.hotel.domain.hotel.hotel.repository.HotelRepository;
import com.ll.hotel.domain.hotel.hotel.type.HotelStatus;
import com.ll.hotel.domain.hotel.option.hotelOption.entity.HotelOption;
import com.ll.hotel.domain.hotel.option.hotelOption.repository.HotelOptionRepository;
import com.ll.hotel.domain.image.ImageType;
import com.ll.hotel.domain.member.member.entity.Business;
import com.ll.hotel.domain.member.member.repository.BusinessRepository;
import com.ll.hotel.global.exceptions.ServiceException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class HotelService {
    private final HotelRepository hotelRepository;
    private final BusinessRepository businessRepository;

    /**
     * HotelImage, hotelOption 등록 추가 필요
     */
    @Transactional
    public PostHotelResponse create(PostHotelRequest postHotelRequest) {
        Business business = this.businessRepository.findById(postHotelRequest.businessId())
                .orElseThrow(() -> new ServiceException("404-1", "사업자가 존재하지 않습니다."));

        Hotel hotel = Hotel.builder()
                .hotelName(postHotelRequest.hotelName())
                .hotelEmail(postHotelRequest.hotelEmail())
                .hotelPhoneNumber(postHotelRequest.hotelPhoneNumber())
                .streetAddress(postHotelRequest.streetAddress())
                .zipCode(postHotelRequest.zipCode())
                .hotelGrade(postHotelRequest.hotelGrade())
                .checkInTime(postHotelRequest.checkInTime())
                .checkOutTime(postHotelRequest.checkOutTime())
                .hotelExplainContent(postHotelRequest.hotelExplainContent())
                .business(business)
                // 호텔 이미지 등록 추가 필요
                // 호텔 옵션 등록 추가 필요
                .build();

        return new PostHotelResponse(this.hotelRepository.save(hotel));
    }

    @Transactional
    public List<GetAllHotelResponse> findAll() {
        List<Hotel> hotels = this.hotelRepository.findAllHotels(ImageType.HOTEL);
        List<GetAllHotelResponse> responses = new ArrayList<>();

        for (Hotel hotel : hotels) {
            responses.add(new GetAllHotelResponse(hotel));
        }

        return responses;
    }

    @Transactional
    public HotelDto findHotelDetail(long hotelId) {
        Hotel hotel = this.hotelRepository.findHotelDetail(hotelId, ImageType.HOTEL)
                .orElseThrow(() -> new ServiceException("404-1", "호텔 정보를 찾을 수 없습니다."));

        return new HotelDto(hotel);
    }

    private final HotelOptionRepository hotelOptionRepository;

    @Transactional
    public PutHotelResponse modify(long hotelId, PutHotelRequest request) {
        Hotel hotel = this.hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ServiceException("404-1", "호텔 정보를 찾을 수 없습니다."));

        modifyIfPresent(request.hotelName(), hotel::setHotelName);
        modifyIfPresent(request.hotelEmail(), hotel::setHotelEmail);
        modifyIfPresent(request.hotelPhoneNumber(), hotel::setHotelPhoneNumber);
        modifyIfPresent(request.streetAddress(), hotel::setStreetAddress);
        modifyIfPresent(request.zipCode(), hotel::setZipCode);
        modifyIfPresent(request.hotelGrade(), hotel::setHotelGrade);
        modifyIfPresent(request.checkInTime(), hotel::setCheckInTime);
        modifyIfPresent(request.checkOutTime(), hotel::setCheckOutTime);
        modifyIfPresent(request.hotelExplainContent(), hotel::setHotelExplainContent);

        if (request.hotelStatus() != null) {
            try {
                hotel.setHotelStatus(HotelStatus.valueOf(request.hotelStatus().toUpperCase()));
            } catch (Exception e) {
                throw new ServiceException("404-2", "호텔 상태 정보를 정확히 입력해주세요.");
            }
        }

        // 이미지 수정 처리 필요

        modifyOptions(hotel, request.hotelOptions());

        return new PutHotelResponse(hotel);
    }

    private <T> void modifyIfPresent(T newValue, Consumer<T> setter) {
        if (newValue != null) {
            setter.accept(newValue);
        }
    }

    @Transactional
    public void modifyOptions(Hotel hotel, Set<String> optionNames) {
        if (optionNames == null || optionNames.isEmpty()) {
            hotel.getHotelOptions().clear();
            return;
        }

        List<HotelOption> options = this.hotelOptionRepository.findByNames(hotel.getId(), optionNames);

        Set<String> validNames = options.stream()
                .map(HotelOption::getName)
                .collect(Collectors.toSet());

        Set<String> inValidNames = optionNames.stream()
                .filter(name -> !validNames.contains(name))
                .collect(Collectors.toSet());

        if (!inValidNames.isEmpty()) {
            throw new ServiceException("404-3", "호텔 옵션 정보가 정확하지 않습니다.");
        }

        Set<HotelOption> curOptions = hotel.getHotelOptions();
        Set<HotelOption> newOptions = new HashSet<>(options);

        Set<HotelOption> toAdd = newOptions.stream()
                .filter(option -> !curOptions.contains(option))
                .collect(Collectors.toSet());

        Set<HotelOption> toRemove = curOptions.stream()
                .filter(option -> !newOptions.contains(option))
                .collect(Collectors.toSet());

        curOptions.addAll(toAdd);
        curOptions.removeAll(toRemove);
    }

    @Transactional
    public void delete(Long hotelId) {
        Optional<Hotel> opHotel = this.hotelRepository.findById(hotelId);

        if (opHotel.isEmpty()) {
            throw new ServiceException("404-1", "호텔 정보를 찾을 수 없습니다.");
        }

        opHotel.get().setHotelStatus(HotelStatus.UNAVAILABLE);
    }
}
