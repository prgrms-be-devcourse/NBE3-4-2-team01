package com.ll.hotel.domain.booking.booking.service;

import com.ll.hotel.domain.booking.booking.dto.BookingRequest;
import com.ll.hotel.domain.booking.booking.entity.Booking;
import com.ll.hotel.domain.booking.booking.repository.BookingRepository;
import com.ll.hotel.domain.booking.booking.type.BookingStatus;
import com.ll.hotel.domain.booking.payment.entity.Payment;
import com.ll.hotel.domain.booking.payment.repository.PaymentRepository;
import com.ll.hotel.domain.hotel.hotel.entity.Hotel;
import com.ll.hotel.domain.hotel.hotel.repository.HotelRepository;
import com.ll.hotel.domain.hotel.room.entity.Room;
import com.ll.hotel.domain.hotel.room.repository.RoomRepository;
import com.ll.hotel.domain.member.member.entity.Member;
import com.ll.hotel.domain.member.member.repository.MemberRepository;
import com.ll.hotel.global.exceptions.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final PaymentRepository paymentRepository;

    // 테스트용
    private final MemberRepository memberRepository;

    @Transactional
    public Booking create(Member member, BookingRequest bookingRequest) {
        Optional<Room> room = roomRepository.findById(bookingRequest.roomId());
        Optional<Hotel> hotel = hotelRepository.findById(bookingRequest.hotelId());
        Optional<Payment> payment = paymentRepository.findById(bookingRequest.paymentId());

        // 테스트용
        member = memberRepository.findById(1L).get();

        Booking booking = Booking.builder()
                .room(room.get())
                .hotel(hotel.get())
                .member(member)
                .payment(payment.get())
                .checkInDate(bookingRequest.checkInDate())
                .checkOutDate(bookingRequest.checkOutDate())
                .build();

        booking = bookingRepository.save(booking);
        booking.setBookingNumber(String.format("B%08d", booking.getId()));
        return bookingRepository.save(booking);
    }

    public void cancel(Booking booking) {
        if (booking.getBookingStatus() == BookingStatus.CANCELLED) {
            throw new ServiceException("400", "이미 취소된 예약입니다.");
        }

        booking.setBookingStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }

    public Booking findById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ServiceException("404", "예약 정보를 찾을 수 없습니다."));
    }

    public Page<Booking> findByMember(Member member, int page, int pageSize) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Order.desc("id")));
        return bookingRepository.findByMember(member, pageRequest);
    }

    public Page<Booking> findByHotelId(Long hotelId, int page, int pageSize) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Order.desc("id")));
        return bookingRepository.findByHotelId(hotelId, pageRequest);
    }
}
