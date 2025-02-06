package com.ll.hotel.domain.hotel.hotel.service;

import com.ll.hotel.domain.hotel.hotel.dto.GetHotelDetailResponse;
import com.ll.hotel.domain.hotel.hotel.dto.GetHotelResponse;
import com.ll.hotel.domain.hotel.hotel.dto.HotelDetailDto;
import com.ll.hotel.domain.hotel.hotel.dto.PostHotelRequest;
import com.ll.hotel.domain.hotel.hotel.dto.PostHotelResponse;
import com.ll.hotel.domain.hotel.hotel.dto.PutHotelRequest;
import com.ll.hotel.domain.hotel.hotel.dto.PutHotelResponse;
import com.ll.hotel.domain.hotel.hotel.entity.Hotel;
import com.ll.hotel.domain.hotel.hotel.repository.HotelRepository;
import com.ll.hotel.domain.hotel.hotel.type.HotelStatus;
import com.ll.hotel.domain.hotel.option.hotelOption.entity.HotelOption;
import com.ll.hotel.domain.hotel.option.hotelOption.repository.HotelOptionRepository;
import com.ll.hotel.domain.hotel.room.dto.RoomWithImageDto;
import com.ll.hotel.domain.hotel.room.repository.RoomRepository;
import com.ll.hotel.domain.image.entity.Image;
import com.ll.hotel.domain.image.service.ImageService;
import com.ll.hotel.domain.image.type.ImageType;
import com.ll.hotel.domain.member.member.entity.Business;
import com.ll.hotel.domain.member.member.entity.Member;
import com.ll.hotel.domain.member.member.repository.BusinessRepository;
import com.ll.hotel.domain.review.review.dto.PresignedUrlsResponse;
import com.ll.hotel.global.aws.s3.S3Service;
import com.ll.hotel.global.exceptions.ServiceException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class HotelService {
    private final ImageService imageService;
    private final S3Service s3Service;
    private final HotelRepository hotelRepository;
    private final HotelOptionRepository hotelOptionRepository;
    private final RoomRepository roomRepository;
    private final BusinessRepository businessRepository;

    @Transactional
    public PostHotelResponse create(Member actor, PostHotelRequest postHotelRequest) {
        Business business = this.businessRepository.findByMember(actor)
                .orElseThrow(() -> new ServiceException("404-1", "사업자가 존재하지 않습니다."));

        if (business.getHotel() != null) {
            throw new ServiceException("409-1", "한 사업자는 하나의 호텔만 등록할 수 있습니다.");
        }

        Set<HotelOption> hotelOptions = this.hotelOptionRepository.findByNameIn(postHotelRequest.hotelOptions());

        if (hotelOptions.size() != postHotelRequest.hotelOptions().size()) {
            throw new ServiceException("404-2", "사용할 수 없는 호텔 옵션이 존재합니다.");
        }

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
                .hotelOptions(hotelOptions)
                .build();

        try {
            return new PostHotelResponse(this.hotelRepository.save(hotel),
                    this.saveHotelImages(hotel.getId(), postHotelRequest.imageExtensions()));
        } catch (DataIntegrityViolationException e) {
            throw new ServiceException("409-2", "동일한 이메일의 호텔이 이미 존재합니다.");
        }
    }

    @Transactional
    public void saveImages(ImageType imageType, long hotelId, List<String> urls) {
        this.imageService.saveImages(imageType, hotelId, urls);
    }

    @Transactional
    public Page<GetHotelResponse> findAll(int page, int pageSize, String filterName, String filterDirection) {
        Map<String, String> sortFieldMapping = Map.of(
                "latest", "createdAt",
                "averageRating", "averageRating",
                "reviewCount", "totalReviewCount"
        );

        String sortField = sortFieldMapping.getOrDefault(filterName, "createdAt");

        Sort.Direction direction;
        if (filterDirection == null) {
            direction = Direction.DESC;
        } else {
            try {
                direction = Sort.Direction.valueOf(filterDirection.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new ServiceException("400-1", "정렬 방향은 ASC 또는 DESC만 가능합니다.");
            }
        }

        Sort sort = Sort.by(direction, sortField);
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, sort);

        return this.hotelRepository.findAllHotels(ImageType.HOTEL, pageRequest)
                .map(GetHotelResponse::new);
    }

    @Transactional
    public GetHotelDetailResponse findHotelDetail(long hotelId) {
        Hotel hotel = this.hotelRepository.findHotelDetail(hotelId)
                .orElseThrow(() -> new ServiceException("404-1", "호텔 정보를 찾을 수 없습니다."));

        List<String> imageUrls = this.imageService.findImagesById(ImageType.HOTEL, hotelId).stream()
                .map(Image::getImageUrl)
                .toList();

        List<RoomWithImageDto> roomDtos = this.roomRepository.findAllRooms(hotelId, ImageType.ROOM);

        return new GetHotelDetailResponse(new HotelDetailDto(hotel, roomDtos), imageUrls);
    }

    @Transactional
    public PutHotelResponse modify(long hotelId, Member actor, PutHotelRequest request) {
        Hotel hotel = this.getHotel(hotelId);

        if (!hotel.isOwnedBy(actor)) {
            throw new ServiceException("403-2", "해당 호텔의 사업자가 아닙니다.");
        }

        if (this.hotelRepository.existsByHotelEmailAndIdNot(request.hotelEmail(), hotelId)) {
            throw new ServiceException("409-1", "동일한 이름의 방이 이미 호텔에 존재합니다.");
        }

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

        modifyOptions(hotel, request.hotelOptions());

        List<String> deleteImageUrls = request.deleteImageUrls();

        this.imageService.deleteImagesByIdAndUrls(ImageType.HOTEL, hotelId, deleteImageUrls);
        this.s3Service.deleteObjectsByUrls(deleteImageUrls);

        return new PutHotelResponse(hotel, this.saveHotelImages(hotelId, request.imageExtensions()));
    }

    private <T> void modifyIfPresent(T newValue, Supplier<T> getter, Consumer<T> setter) {
        if (newValue != null && !newValue.equals(getter.get())) {
            setter.accept(newValue);
        }
    }

    private void modifyOptions(Hotel hotel, Set<String> optionNames) {
        if (optionNames == null || optionNames.isEmpty()) {
            hotel.setHotelOptions(new HashSet<>());
            return;
        }

        Set<HotelOption> options = this.hotelOptionRepository.findByNameIn(optionNames);

        if (options.size() != optionNames.size()) {
            throw new ServiceException("404-3", "사용할 수 없는 호텔 옵션이 존재합니다.");
        }

        hotel.setHotelOptions(options);
    }

    @Transactional
    public void delete(Long hotelId, Member actor) {
        Hotel hotel = this.getHotel(hotelId);

        if (!hotel.isOwnedBy(actor)) {
            throw new ServiceException("403-2", "해당 호텔의 사업자가 아닙니다.");
        }

        hotel.setHotelStatus(HotelStatus.UNAVAILABLE);

        if (this.imageService.deleteImages(ImageType.HOTEL, hotelId) > 0) {
            this.s3Service.deleteAllObjectsById(ImageType.HOTEL, hotelId);
        }
    }

    // 호텔 이미지 저장
    private PresignedUrlsResponse saveHotelImages(long hotelId, List<String> extensions) {
        List<URL> urls = this.s3Service.generatePresignedUrls(ImageType.HOTEL, hotelId, extensions);

        return new PresignedUrlsResponse(hotelId, urls);
    }

    public Hotel getHotel(Long hotelId) {
        return this.hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ServiceException("404-1", "호텔 정보를 찾을 수 없습니다."));
    }
}
