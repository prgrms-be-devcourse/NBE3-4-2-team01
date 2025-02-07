package com.ll.hotel.global.initData;

import com.ll.hotel.domain.booking.booking.dto.BookingRequest;
import com.ll.hotel.domain.booking.booking.entity.Booking;
import com.ll.hotel.domain.booking.booking.repository.BookingRepository;
import com.ll.hotel.domain.booking.payment.dto.PaymentRequest;
import com.ll.hotel.domain.booking.payment.entity.Payment;
import com.ll.hotel.domain.booking.payment.repository.PaymentRepository;
import com.ll.hotel.domain.booking.payment.service.PaymentService;
import com.ll.hotel.domain.hotel.hotel.dto.PostHotelRequest;
import com.ll.hotel.domain.hotel.hotel.dto.PostHotelResponse;
import com.ll.hotel.domain.hotel.hotel.entity.Hotel;
import com.ll.hotel.domain.hotel.hotel.repository.HotelRepository;
import com.ll.hotel.domain.hotel.hotel.service.HotelService;
import com.ll.hotel.domain.hotel.option.hotelOption.entity.HotelOption;
import com.ll.hotel.domain.hotel.option.hotelOption.repository.HotelOptionRepository;
import com.ll.hotel.domain.hotel.option.roomOption.entity.RoomOption;
import com.ll.hotel.domain.hotel.option.roomOption.repository.RoomOptionRepository;
import com.ll.hotel.domain.hotel.room.dto.PostRoomRequest;
import com.ll.hotel.domain.hotel.room.dto.PostRoomResponse;
import com.ll.hotel.domain.hotel.room.entity.Room;
import com.ll.hotel.domain.hotel.room.repository.RoomRepository;
import com.ll.hotel.domain.hotel.room.service.RoomService;
import com.ll.hotel.domain.member.member.entity.Business;
import com.ll.hotel.domain.member.member.entity.Member;
import com.ll.hotel.domain.member.member.entity.Role;
import com.ll.hotel.domain.member.member.repository.BusinessRepository;
import com.ll.hotel.domain.member.member.repository.MemberRepository;
import com.ll.hotel.domain.member.member.type.BusinessApprovalStatus;
import com.ll.hotel.domain.member.member.type.MemberStatus;
import com.ll.hotel.domain.review.comment.entity.ReviewComment;
import com.ll.hotel.domain.review.comment.repository.ReviewCommentRepository;
import com.ll.hotel.domain.review.comment.type.ReviewCommentStatus;
import com.ll.hotel.domain.review.review.entity.Review;
import com.ll.hotel.domain.review.review.repository.ReviewRepository;
import com.ll.hotel.domain.review.review.type.ReviewStatus;
import com.ll.hotel.global.exceptions.ServiceException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.time.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class BaseInit {
    private final PaymentService paymentService;
    private final HotelService hotelService;
    private final RoomService roomService;
    private final MemberRepository memberRepository;
    private final BusinessRepository businessRepository;
    private final HotelOptionRepository hotelOptionRepository;
    private final RoomOptionRepository roomOptionRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewCommentRepository reviewCommentRepository;

    @Autowired
    @Lazy
    private BaseInit self;

    @Bean
    public ApplicationRunner baseInitApplicationRunner() {
        return args -> {
            self.createMembers();
        };
    }

    @Transactional
    public void createMembers() {
        if (memberRepository.count() > 0) return;

        // 손님 3명, 사업자 2명, 관리자 1명 추가

        Member bMember1 = Member
                .builder()
                .birthDate(LocalDate.now())
                .memberEmail("business1@gmail.com")
                .memberName("business1")
                .memberPhoneNumber("01011111111")
                .memberStatus(MemberStatus.ACTIVE)
                .role(Role.BUSINESS)
                .build();

        Member bMember2 = Member
                .builder()
                .birthDate(LocalDate.now())
                .memberEmail("business2@gmail.com")
                .memberName("business2")
                .memberPhoneNumber("01022222222")
                .memberStatus(MemberStatus.ACTIVE)
                .role(Role.BUSINESS)
                .build();

        // 손님
        Member customer1 = Member
                .builder()
                .birthDate(LocalDate.now())
                .memberEmail("customer1@gmail.com")
                .memberName("customer1")
                .memberPhoneNumber("01011111111")
                .memberStatus(MemberStatus.ACTIVE)
                .role(Role.USER)
                .build();

        Member customer2 = Member
                .builder()
                .birthDate(LocalDate.now())
                .memberEmail("customer2@gmail.com")
                .memberName("customer2")
                .memberPhoneNumber("01022222222")
                .memberStatus(MemberStatus.ACTIVE)
                .role(Role.USER)
                .build();

        Member customer3 = Member
                .builder()
                .birthDate(LocalDate.now())
                .memberEmail("customer3@gmail.com")
                .memberName("customer3")
                .memberPhoneNumber("01033333333")
                .memberStatus(MemberStatus.ACTIVE)
                .role(Role.USER)
                .build();

        // 관리자
        Member admin1 = Member
                .builder()
                .birthDate(LocalDate.now())
                .memberEmail("admin1@gmail.com")
                .memberName("admin1")
                .memberPhoneNumber("01012345678")
                .memberStatus(MemberStatus.ACTIVE)
                .role(Role.ADMIN)
                .build();

        memberRepository.save(bMember1);
        memberRepository.save(bMember2);
        memberRepository.save(customer1);
        memberRepository.save(customer2);
        memberRepository.save(customer3);
        memberRepository.save(admin1);

        // 사업자 추가
        // ------------------------------------------------------------------

        Business business1 = Business
                .builder()
                .businessRegistrationNumber("1111111111")
                .startDate(LocalDate.now())
                .ownerName("사장일")
                .approvalStatus(BusinessApprovalStatus.PENDING)
                .member(bMember1)
                .hotel(null)
                .build();

        Business business2 = Business
                .builder()
                .businessRegistrationNumber("2222222222")
                .startDate(LocalDate.now())
                .ownerName("사장이")
                .approvalStatus(BusinessApprovalStatus.PENDING)
                .member(bMember2)
                .hotel(null)
                .build();

        businessRepository.save(business1);
        businessRepository.save(business2);

        // 호텔옵션
        // ------------------------------------------------------------------

        HotelOption hotelOption1 = hotelOptionRepository.save(HotelOption
                .builder()
                .name("호텔옵션1")
                .build()
        );

        HotelOption hotelOption2 = hotelOptionRepository.save(HotelOption
                .builder()
                .name("호텔옵션2")
                .build()
        );

        HotelOption hotelOption3 = hotelOptionRepository.save(HotelOption
                .builder()
                .name("호텔옵션3")
                .build()
        );

        Set<String> hotelOptions = new HashSet<>();
        hotelOptions.add(hotelOption1.getName());
        hotelOptions.add(hotelOption2.getName());
        hotelOptions.add(hotelOption3.getName());

        // 객실옵션
        // ------------------------------------------------------------------

        RoomOption roomOption1 = roomOptionRepository.save(RoomOption
                .builder()
                .name("객실옵션1")
                .build()
        );

        RoomOption roomOption2 = roomOptionRepository.save(RoomOption
                .builder()
                .name("객실옵션2")
                .build()
        );

        RoomOption roomOption3 = roomOptionRepository.save(RoomOption
                .builder()
                .name("객실옵션3")
                .build()
        );

        Set<String> roomOptions = new HashSet<>();
        roomOptions.add(roomOption1.getName());
        roomOptions.add(roomOption2.getName());
        roomOptions.add(roomOption3.getName());

        // 호텔 추가
        // ------------------------------------------------------------------

        PostHotelRequest postHotelRequest1 = new PostHotelRequest(business1.getId(), "호텔1", "hotel1@naver.com",
                "010-1111-1111", "서울시", 11111,
                3, LocalTime.of(12, 0), LocalTime.of(14, 0), "호텔1입니다.", null, hotelOptions);

        PostHotelRequest postHotelRequest2 = new PostHotelRequest(business2.getId(), "호텔2", "hotel2@naver.com",
                "010-2222-2222", "서울시", 22222,
                4, LocalTime.of(12, 0), LocalTime.of(14, 0), "호텔2입니다.", null, hotelOptions);

        PostHotelResponse postHotelResponse1 = hotelService.createHotel(bMember1, postHotelRequest1);
        PostHotelResponse postHotelResponse2 = hotelService.createHotel(bMember2, postHotelRequest2);

        Hotel hotel1 = hotelRepository.findById(postHotelResponse1.hotelId())
                .orElseThrow(() -> new ServiceException("400-5", "호텔 X"));
        Hotel hotel2 = hotelRepository.findById(postHotelResponse2.hotelId())
                .orElseThrow(() -> new ServiceException("400-6", "호텔 X"));

        // 객실 추가
        // ------------------------------------------------------------------

        Map<String, Integer> bedTypeNumber = Map.of("SINGLE", 4, "DOUBLE", 2, "KING", 1);

        PostRoomRequest hotel1Req1 = new PostRoomRequest("호텔1_객실1", 1, 300000, 2, 4, bedTypeNumber, null, roomOptions);
        PostRoomRequest hotel1Req2 = new PostRoomRequest("호텔1_객실2", 2, 400000, 2, 4, bedTypeNumber, null, roomOptions);


        PostRoomRequest hotel2Req1 = new PostRoomRequest("호텔2_객실1", 1, 500000, 2, 4, bedTypeNumber, null, roomOptions);
        PostRoomRequest hotel2Req2 = new PostRoomRequest("호텔2_객실2", 2, 600000, 2, 4, bedTypeNumber, null, roomOptions);

        PostRoomResponse postRoomResponse1 = roomService.createRoom(hotel1.getId(), bMember1, hotel1Req1);
        PostRoomResponse postRoomResponse2 = roomService.createRoom(hotel1.getId(), bMember1, hotel1Req2);
        PostRoomResponse postRoomResponse3 = roomService.createRoom(hotel2.getId(), bMember2, hotel2Req1);
        PostRoomResponse postRoomResponse4 = roomService.createRoom(hotel2.getId(), bMember2, hotel2Req2);

        Room room1_1 = roomRepository.findById(postRoomResponse1.roomId())
                .orElseThrow(() -> new ServiceException("400-1", "객실 X"));
        Room room1_2 = roomRepository.findById(postRoomResponse2.roomId())
                .orElseThrow(() -> new ServiceException("400-2", "객실 X"));
        Room room2_1 = roomRepository.findById(postRoomResponse3.roomId())
                .orElseThrow(() -> new ServiceException("400-3", "객실 X"));
        Room room2_2 = roomRepository.findById(postRoomResponse4.roomId())
                .orElseThrow(() -> new ServiceException("400-4", "객실 X"));

        // 예약 추가
        // -------------------------------------------------------------------

        BookingRequest bookingRequest1 = new BookingRequest(
                room1_1.getId(), hotel1.getId(),
                LocalDate.now().minusDays(3), LocalDate.now().minusDays(1),
                "임시uid1", 300000, Instant.now().getEpochSecond());

        BookingRequest bookingRequest2 = new BookingRequest(
                room1_2.getId(), hotel1.getId(),
                LocalDate.now().minusDays(7), LocalDate.now().minusDays(5),
                "임시uid2", 400000, Instant.now().getEpochSecond());

        BookingRequest bookingRequest3 = new BookingRequest(
                room2_1.getId(), hotel2.getId(),
                LocalDate.now().minusDays(4), LocalDate.now(),
                "임시uid3", 500000, Instant.now().getEpochSecond());

        BookingRequest bookingRequest4 = new BookingRequest(
                room2_2.getId(), hotel2.getId(),
                LocalDate.now().minusDays(5), LocalDate.now().minusDays(1),
                "임시uid4", 600000, Instant.now().getEpochSecond());

        PaymentRequest paymentRequest1 = PaymentRequest.from(bookingRequest1);
        PaymentRequest paymentRequest2 = PaymentRequest.from(bookingRequest2);
        PaymentRequest paymentRequest3 = PaymentRequest.from(bookingRequest3);
        PaymentRequest paymentRequest4 = PaymentRequest.from(bookingRequest4);

        Payment payment1 = paymentRepository.save(Payment.builder()
                .merchantUid(paymentRequest1.merchantUid())
                .amount(paymentRequest1.amount())
                .paidAt(LocalDateTime.ofInstant(Instant.ofEpochSecond(paymentRequest1.paidAtTimestamp()), ZoneId.systemDefault()))
                .build());

        Payment payment2 = paymentRepository.save(Payment.builder()
                .merchantUid(paymentRequest2.merchantUid())
                .amount(paymentRequest2.amount())
                .paidAt(LocalDateTime.ofInstant(Instant.ofEpochSecond(paymentRequest2.paidAtTimestamp()), ZoneId.systemDefault()))
                .build());

        Payment payment3 = paymentRepository.save(Payment.builder()
                .merchantUid(paymentRequest3.merchantUid())
                .amount(paymentRequest3.amount())
                .paidAt(LocalDateTime.ofInstant(Instant.ofEpochSecond(paymentRequest3.paidAtTimestamp()), ZoneId.systemDefault()))
                .build());

        Payment payment4 = paymentRepository.save(Payment.builder()
                .merchantUid(paymentRequest4.merchantUid())
                .amount(paymentRequest4.amount())
                .paidAt(LocalDateTime.ofInstant(Instant.ofEpochSecond(paymentRequest4.paidAtTimestamp()), ZoneId.systemDefault()))
                .build());

        Booking booking1 = Booking.builder()
                .room(room1_1)
                .hotel(hotel1)
                .member(customer1)
                .payment(payment1)
                .checkInDate(bookingRequest1.checkInDate())
                .checkOutDate(bookingRequest1.checkOutDate())
                .build();

        Booking booking2 = Booking.builder()
                .room(room1_2)
                .hotel(hotel1)
                .member(customer1)
                .payment(payment2)
                .checkInDate(bookingRequest2.checkInDate())
                .checkOutDate(bookingRequest2.checkOutDate())
                .build();

        Booking booking3 = Booking.builder()
                .room(room2_1)
                .hotel(hotel2)
                .member(customer1)
                .payment(payment3)
                .checkInDate(bookingRequest3.checkInDate())
                .checkOutDate(bookingRequest3.checkOutDate())
                .build();

        Booking booking4 = Booking.builder()
                .room(room2_2)
                .hotel(hotel2)
                .member(customer1)
                .payment(payment4)
                .checkInDate(bookingRequest4.checkInDate())
                .checkOutDate(bookingRequest4.checkOutDate())
                .build();

        bookingRepository.save(booking1);
        bookingRepository.save(booking2);
        bookingRepository.save(booking3);
        bookingRepository.save(booking4);


        // 리뷰 추가
        // ------------------------------------------------------------------

        Review review1 = Review.builder()
                .hotel(hotel1)
                .room(room1_1)
                .member(customer1)
                .booking(booking1)
                .content("객실1-1 리뷰")
                .rating(5)
                .reviewStatus(ReviewStatus.CREATED)
                .build();

        Review review2 = Review.builder()
                .hotel(hotel1)
                .room(room1_2)
                .member(customer1)
                .booking(booking2)
                .content("객실1-2 리뷰")
                .rating(4)
                .reviewStatus(ReviewStatus.CREATED)
                .build();

        Review review3 = Review.builder()
                .hotel(hotel2)
                .room(room2_1)
                .member(customer1)
                .booking(booking3)
                .content("객실2-1 리뷰")
                .rating(5)
                .reviewStatus(ReviewStatus.CREATED)
                .build();

        Review review4 = Review.builder()
                .hotel(hotel2)
                .room(room2_2)
                .member(customer1)
                .booking(booking4)
                .content("객실2-2 리뷰")
                .rating(3)
                .reviewStatus(ReviewStatus.CREATED)
                .build();

        reviewRepository.save(review1);
        reviewRepository.save(review2);
        reviewRepository.save(review3);
        reviewRepository.save(review4);

        // 리뷰 답변 생성
        // -----------------------------------------------

        ReviewComment reviewComment1 = ReviewComment.builder()
                .review(review1)
                .content("객실1-1 답변")
                .reviewCommentStatus(ReviewCommentStatus.CREATED)
                .build();

        ReviewComment reviewComment2 = ReviewComment.builder()
                .review(review2)
                .content("객실1-2 답변")
                .reviewCommentStatus(ReviewCommentStatus.CREATED)
                .build();

        ReviewComment reviewComment3 = ReviewComment.builder()
                .review(review3)
                .content("객실2-1 답변")
                .reviewCommentStatus(ReviewCommentStatus.CREATED)
                .build();

        ReviewComment reviewComment4 = ReviewComment.builder()
                .review(review4)
                .content("객실2-2 답변")
                .reviewCommentStatus(ReviewCommentStatus.CREATED)
                .build();

        reviewCommentRepository.save(reviewComment1);
        reviewCommentRepository.save(reviewComment2);
        reviewCommentRepository.save(reviewComment3);
        reviewCommentRepository.save(reviewComment4);
    }
}
