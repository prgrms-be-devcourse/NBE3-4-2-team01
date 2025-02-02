package com.ll.hotel.domain.booking.booking.service;

import com.ll.hotel.domain.booking.booking.dto.BookingRequest;
import com.ll.hotel.domain.booking.booking.entity.Booking;
import com.ll.hotel.domain.booking.booking.repository.BookingRepository;
import com.ll.hotel.domain.booking.payment.entity.Payment;
import com.ll.hotel.domain.booking.payment.repository.PaymentRepository;
import com.ll.hotel.domain.hotel.hotel.entity.Hotel;
import com.ll.hotel.domain.hotel.hotel.repository.HotelRepository;
import com.ll.hotel.domain.hotel.room.entity.Room;
import com.ll.hotel.domain.hotel.room.repository.RoomRepository;
import com.ll.hotel.domain.member.member.entity.Member;
import com.ll.hotel.standard.util.Ut;
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

    @Transactional
    public Booking create(Member member, BookingRequest bookingRequest) {
        Optional<Room> room = roomRepository.findById(bookingRequest.roomId());
        Optional<Hotel> hotel = hotelRepository.findById(bookingRequest.hotelId());
        Optional<Payment> payment = paymentRepository.findById(bookingRequest.paymentId());

        Booking booking = Booking.builder()
                .room(room.get())
                .hotel(hotel.get())
                .member(member)
                .payment(payment.get())
                .build();

        booking = bookingRepository.save(booking);
        booking.setBookingNumber(String.format("B%08d", booking.getId()));
        return bookingRepository.save(booking);
    }

    public void delete(Booking booking) {
        bookingRepository.delete(booking);
    }

    public Optional<Booking> findById(Long id) {
        return bookingRepository.findById(id);
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
