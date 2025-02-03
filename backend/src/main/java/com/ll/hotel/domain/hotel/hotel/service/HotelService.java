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
import com.ll.hotel.domain.image.type.ImageType;
import com.ll.hotel.domain.member.member.entity.Business;
import com.ll.hotel.domain.member.member.repository.BusinessRepository;
import com.ll.hotel.global.exceptions.ServiceException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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
public class HotelService {
    private final HotelRepository hotelRepository;
    private final BusinessRepository businessRepository;

    /**
     * HotelImage 등록 추가 필요
     */
    @Transactional
    public PostHotelResponse create(PostHotelRequest postHotelRequest) {
        Business business = this.businessRepository.findById(postHotelRequest.businessId())
                .orElseThrow(() -> new ServiceException("404-1", "사업자가 존재하지 않습니다."));

        Set<HotelOption> hotelOptions = new HashSet<>();

        for (String name : postHotelRequest.hotelOptions()) {
            Optional<HotelOption> opHotelOption = this.hotelOptionRepository.findByName(name);

            if (opHotelOption.isEmpty()) {
                HotelOption hotelOption = HotelOption.builder().name(name).build();
                hotelOptions.add(hotelOption);
            } else {
                hotelOptions.add(opHotelOption.get());
            }
        }

        this.hotelOptionRepository.saveAll(hotelOptions);

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
                .hotelOptions(hotelOptions)
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

        modifyIfPresent(request.hotelName(), hotel::getHotelName, hotel::setHotelName);
        modifyIfPresent(request.hotelEmail(), hotel::getHotelEmail, hotel::setHotelEmail);
        modifyIfPresent(request.hotelPhoneNumber(), hotel::getHotelPhoneNumber, hotel::setHotelPhoneNumber);
        modifyIfPresent(request.streetAddress(), hotel::getStreetAddress, hotel::setStreetAddress);
        modifyIfPresent(request.zipCode(), hotel::getZipCode, hotel::setZipCode);
        modifyIfPresent(request.hotelGrade(), hotel::getHotelGrade, hotel::setHotelGrade);
        modifyIfPresent(request.checkInTime(), hotel::getCheckInTime, hotel::setCheckInTime);
        modifyIfPresent(request.checkOutTime(), hotel::getCheckOutTime, hotel::setCheckOutTime);
        modifyIfPresent(request.hotelExplainContent(), hotel::getHotelExplainContent, hotel::setHotelExplainContent);

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

    private <T> void modifyIfPresent(T newValue, Supplier<T> getter, Consumer<T> setter) {
        if (newValue != null && !newValue.equals(getter.get())) {
            setter.accept(newValue);
        }
    }

    @Transactional
    public void modifyOptions(Hotel hotel, Set<String> optionNames) {
        if (optionNames == null || optionNames.isEmpty()) {
            hotel.setHotelOptions(new HashSet<>());
            return;
        }

        List<HotelOption> options = this.hotelOptionRepository.findByNameIn(optionNames);

        Set<String> curNames = options.stream()
                .map(HotelOption::getName)
                .collect(Collectors.toSet());

        Set<HotelOption> newHotelOptions = optionNames.stream()
                .filter(name -> !curNames.contains(name))
                .filter(name -> !this.hotelOptionRepository.existsByName(name))
                .map(name -> HotelOption.builder().name(name).build())
                .collect(Collectors.toSet());

        if (!newHotelOptions.isEmpty()) {
            newHotelOptions = new HashSet<>(this.hotelOptionRepository.saveAll(newHotelOptions));
        }

        Set<HotelOption> resultHotelOptions = new HashSet<>(options);
        resultHotelOptions.addAll(newHotelOptions);
        hotel.setHotelOptions(resultHotelOptions);
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
