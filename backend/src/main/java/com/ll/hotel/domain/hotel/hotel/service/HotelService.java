package com.ll.hotel.domain.hotel.hotel.service;

import com.ll.hotel.domain.booking.booking.entity.Booking;
import com.ll.hotel.domain.booking.booking.type.BookingStatus;
import com.ll.hotel.domain.booking.payment.entity.Payment;
import com.ll.hotel.domain.hotel.hotel.dto.*;
import com.ll.hotel.domain.hotel.hotel.entity.Hotel;
import com.ll.hotel.domain.hotel.hotel.repository.HotelRepository;
import com.ll.hotel.domain.hotel.hotel.type.HotelStatus;
import com.ll.hotel.domain.hotel.option.hotelOption.entity.HotelOption;
import com.ll.hotel.domain.hotel.option.hotelOption.repository.HotelOptionRepository;
import com.ll.hotel.domain.hotel.room.dto.GetRoomRevenueResponse;
import com.ll.hotel.domain.hotel.room.dto.RoomWithImageDto;
import com.ll.hotel.domain.hotel.room.entity.Room;
import com.ll.hotel.domain.hotel.room.repository.RoomRepository;
import com.ll.hotel.domain.image.entity.Image;
import com.ll.hotel.domain.image.service.ImageService;
import com.ll.hotel.domain.image.type.ImageType;
import com.ll.hotel.domain.member.member.entity.Business;
import com.ll.hotel.domain.member.member.entity.Member;
import com.ll.hotel.domain.member.member.repository.BusinessRepository;
import com.ll.hotel.domain.review.review.dto.response.PresignedUrlsResponse;
import com.ll.hotel.global.annotation.BusinessOnly;
import com.ll.hotel.global.aws.s3.S3Service;
import com.ll.hotel.global.exceptions.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

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

    @BusinessOnly
    @Transactional
    public PostHotelResponse createHotel(Member actor, PostHotelRequest postHotelRequest) {
        Business business = this.businessRepository.findByMember(actor)
                .orElseThrow(() -> new ServiceException("404-1", "사업자가 존재하지 않습니다."));

        if (business.getHotel() != null) {
            throw new ServiceException("409-1", "한 사업자는 하나의 호텔만 등록할 수 있습니다.");
        }

        Set<HotelOption> hotelOptions = this.hotelOptionRepository.findByNameIn(postHotelRequest.hotelOptions());

        if (hotelOptions.size() != postHotelRequest.hotelOptions().size()) {
            throw new ServiceException("404-2", "사용할 수 없는 호텔 옵션이 존재합니다.");
        }

        Hotel hotel = Hotel.hotelBuild(postHotelRequest, business, hotelOptions);

        try {
            return new PostHotelResponse(this.hotelRepository.save(hotel),
                    this.saveHotelImages(hotel.getId(), postHotelRequest.imageExtensions()));
        } catch (DataIntegrityViolationException e) {
            throw new ServiceException("409-2", "동일한 이메일의 호텔이 이미 존재합니다.");
        }
    }

    @BusinessOnly
    @Transactional
    public void saveImages(Member actor, ImageType imageType, long hotelId, List<String> urls) {
        this.imageService.saveImages(imageType, hotelId, urls);
    }

    @Transactional(readOnly = true)
    public Page<GetHotelResponse> findAllHotels(int page, int pageSize, String filterName, String filterDirection,
                                                String streetAddress, LocalDate checkInDate, LocalDate checkOutDate) {
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

        Page<HotelWithImageDto> hotels = this.hotelRepository.findAllHotels(ImageType.HOTEL, streetAddress,
                pageRequest);

        List<GetHotelResponse> availableHotels = this.getAvailableHotels(checkInDate, checkOutDate, hotels);

        return new PageImpl<>(availableHotels, pageRequest, availableHotels.size());
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

    @BusinessOnly
    @Transactional
    public PutHotelResponse modifyHotel(long hotelId, Member actor, PutHotelRequest request) {
        Hotel hotel = this.getHotelById(hotelId);

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

    @BusinessOnly
    @Transactional
    public void deleteHotel(Long hotelId, Member actor) {
        Hotel hotel = this.getHotelById(hotelId);

        if (!hotel.isOwnedBy(actor)) {
            throw new ServiceException("403-2", "해당 호텔의 사업자가 아닙니다.");
        }

        hotel.setHotelStatus(HotelStatus.UNAVAILABLE);

        if (this.imageService.deleteImages(ImageType.HOTEL, hotelId) > 0) {
            this.s3Service.deleteAllObjectsById(ImageType.HOTEL, hotelId);
        }
    }

    @Transactional
    public GetHotelRevenueResponse findRevenue(long hotelId, Member actor) {
        Hotel hotel = getHotelById(hotelId);

        if (!hotel.isOwnedBy(actor)) {
            throw new ServiceException("403-2", "해당 호텔의 사업자가 아닙니다.");
        }

        long hotelRevenue = 0;
        List<GetRoomRevenueResponse> roomRevenueResponses = new ArrayList<>();
        for (Room room : hotel.getRooms()) {
            long roomRevenue = room.getBookings().stream()
                    .filter(booking -> booking.getBookingStatus().equals(BookingStatus.COMPLETED))
                    .map(Booking::getPayment)
                    .mapToLong(Payment::getAmount)
                    .sum();

            hotelRevenue += roomRevenue;

            roomRevenueResponses.add(
                    new GetRoomRevenueResponse(room.getId(), room.getRoomName(), room.getBasePrice(), roomRevenue));
        }

        return new GetHotelRevenueResponse(roomRevenueResponses, hotelRevenue);
    }

    // 호텔 이미지 저장
    private PresignedUrlsResponse saveHotelImages(long hotelId, List<String> extensions) {
        List<URL> urls = this.s3Service.generatePresignedUrls(ImageType.HOTEL, hotelId, extensions);

        return new PresignedUrlsResponse(hotelId, urls);
    }

    public Hotel getHotelById(Long hotelId) {
        return this.hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ServiceException("404-1", "호텔 정보를 찾을 수 없습니다."));
    }

    /**
     * 예약 가능한 호텔 리스트
     * 예약 가능한 객실이 없으면 해당 호텔은 보여주지 않음
     * room 이 변경되므로, 다른 곳에서는 사용하지 말 것
     */
    // 배포 시 .filter 부분 주석 해제 (예약 가능한 Room이 없으면 호텔을 보여주지 않는 부분)
    private List<GetHotelResponse> getAvailableHotels(LocalDate checkInDate, LocalDate checkOutDate,
                                                      Page<HotelWithImageDto> hotels) {
        return hotels.stream()
                .map(dto -> {
                    Hotel hotel = dto.hotel();
                    List<Room> availableRooms = hotel.getRooms().stream()
                            .filter(room -> this.roomIsAvailable(room, checkInDate, checkOutDate))
                            .map(room -> {
                                room.setRoomNumber(this.countAvailableRoomNumber(room, checkInDate, checkOutDate));
                                return room;
                            })
                            .filter(room -> room.getRoomNumber() > 0)
                            .toList();
                    hotel.setRooms(availableRooms);
                    return dto;
                })
//                .filter(dto -> !dto.hotel().getRooms().isEmpty())
                .map(GetHotelResponse::new)
                .toList();
    }

    // 예약 가능한 객실 여부
    private boolean roomIsAvailable(Room room, LocalDate checkInDate, LocalDate checkOutDate) {
        return room.getBookings().stream()
                .noneMatch(booking -> (booking.getCheckInDate().isBefore(checkOutDate)
                                       && booking.getCheckOutDate().isAfter(checkInDate)));
    }

    // 호텔의 예약 가능한 객실 수 Count
    private int countAvailableRoomNumber(Room room, LocalDate checkInDate, LocalDate checkOutDate) {
        long resolvedCount = room.getBookings().stream()
                .filter(booking -> booking.getCheckInDate().isBefore(checkOutDate)
                                   && booking.getCheckOutDate().isAfter(checkInDate))
                .count();

        return room.getRoomNumber() - (int) resolvedCount;
    }
}
