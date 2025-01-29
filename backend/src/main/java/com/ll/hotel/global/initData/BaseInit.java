package com.ll.hotel.global.initData;

import com.ll.hotel.domain.hotel.amenity.hotelAmenity.dto.request.HotelAmenityRequest;
import com.ll.hotel.domain.hotel.amenity.hotelAmenity.repository.HotelAmenityRepository;
import com.ll.hotel.domain.hotel.amenity.hotelAmenity.service.HotelAmenityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@RequiredArgsConstructor
public class BaseInit {
    private final HotelAmenityService hotelAmenityService;
    private final HotelAmenityRepository hotelAmenityRepository;

    @Autowired
    @Lazy
    private BaseInit self;

    @Bean
    public ApplicationRunner baseInitDataApplicationRunner() {
        return args -> {
            self.hotelAmenities();
        };
    }

    @Transactional
    public void hotelAmenities() {
        if (hotelAmenityRepository.count() > 0) return;

        for (int i = 0; i < 10; i++) {
            String description = String.format("테스트[%02d]", i);

            HotelAmenityRequest.Details details =
                    new HotelAmenityRequest.Details(description);

            hotelAmenityService.add(details);
        }
    }
}
