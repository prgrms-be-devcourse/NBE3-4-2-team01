package com.ll.hotel.domain.booking.booking.service;

import com.ll.hotel.domain.booking.booking.dto.BookingRequest;
import com.ll.hotel.domain.booking.booking.entity.Booking;
import com.ll.hotel.domain.booking.booking.repository.BookingRepository;
import com.ll.hotel.domain.booking.booking.type.BookingStatus;
import com.ll.hotel.domain.booking.payment.entity.Payment;
import com.ll.hotel.domain.booking.payment.service.PaymentService;
import com.ll.hotel.domain.hotel.hotel.entity.Hotel;
import com.ll.hotel.domain.hotel.hotel.repository.HotelRepository;
import com.ll.hotel.domain.hotel.room.entity.Room;
import com.ll.hotel.domain.hotel.room.repository.RoomRepository;
import com.ll.hotel.domain.member.member.entity.Member;
import com.ll.hotel.global.exceptions.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.IllegalFormatException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final String CREATE_ERROR_MESSAGE = "예약 정보 저장에 실패했습니다. 관리자에게 문의하세요.";
    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final PaymentService paymentService;

    /*
     * 예약, 결제 정보 저장
     * 결제 -> 예약 저장 순으로 진행
     * 결제 오류 코드는 500-1, 500-2, 500-3 (PaymentService 코드 참고)
     * 예약 오류 코드는 500-4, 500-5, 500-6
     * 코드 500-4: room, hotel 검색 에러
     * 코드 500-5: 예약 번호 생성 에러
     * 코드 500-6: 예약 생성 및 저장 중 에러
     */
    @Transactional
    public Booking create(Member member, BookingRequest bookingRequest) {
        // 결제 정보 저장
        Payment payment = paymentService.create(bookingRequest);

        // 예약 정보 저장
        try {
            Optional<Room> room = roomRepository.findById(bookingRequest.roomId());
            Optional<Hotel> hotel = hotelRepository.findById(bookingRequest.hotelId());
            Booking booking = Booking.builder()
                    .room(room.get())
                    .hotel(hotel.get())
                    .member(member)
                    .payment(payment)
                    .checkInDate(bookingRequest.checkInDate())
                    .checkOutDate(bookingRequest.checkOutDate())
                    .build();

            booking = bookingRepository.save(booking);
            booking.setBookingNumber(String.format("B%08d", booking.getId())); // ID 기반으로 예약 번호 생성
            return bookingRepository.save(booking);
        } catch (ServiceException e) {
            throw new ServiceException("500-4", CREATE_ERROR_MESSAGE);
        } catch (IllegalFormatException e) {
            throw new ServiceException("500-5", CREATE_ERROR_MESSAGE);
        } catch (Exception e) {
            throw new ServiceException("500-6", CREATE_ERROR_MESSAGE);
        }
    }

    /*
     * 예약, 결제 취소
     * 결제 -> 예약 취소 순으로 진행
     */
    public Booking cancel(Long bookingId) {
        return cancel(findById(bookingId));
    }

    @Transactional
    public Booking cancel(Booking booking) {
        // 이미 예약 및 결제가 취소되었을 경우
        if (booking.getBookingStatus() == BookingStatus.CANCELLED) {
            throw new ServiceException("400", "이미 취소된 예약입니다.");
        }

        // 결제 취소 시도
        Payment payment = booking.getPayment();
        paymentService.softDelete(payment);

        // 예약 취소 시도
        try {
            booking.setBookingStatus(BookingStatus.CANCELLED);
            return bookingRepository.save(booking);
        } catch (Exception e) {
            throw new ServiceException("500-3", "예약이 정상적으로 취소되지 않았습니다. 관리자에게 문의하세요.");
        }
    }

    public Booking findById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ServiceException("404", "예약 정보를 찾을 수 없습니다."));
    }

    public Page<Booking> findByMember(Member member, int page, int pageSize) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Order.desc("id")));
        return bookingRepository.findByMember(member, pageRequest);
    }

    public Page<Booking> findByHotel(Hotel hotel, int page, int pageSize) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Order.desc("id")));
        return bookingRepository.findByHotel(hotel, pageRequest);
    }

    // 미사용
    public Page<Booking> findByHotelId(Long hotelId, int page, int pageSize) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Order.desc("id")));
        return bookingRepository.findByHotelId(hotelId, pageRequest);
    }

    public Page<Booking> findAll(int page, int pageSize) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Order.desc("id")));
        return bookingRepository.findAll(pageRequest);
    }
}
