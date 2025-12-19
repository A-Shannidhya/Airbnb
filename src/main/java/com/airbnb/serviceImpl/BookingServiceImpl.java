package com.airbnb.serviceImpl;

import com.airbnb.dto.BookingDTO;
import com.airbnb.dto.BookingRequest;
import com.airbnb.dto.GuestDTO;
import com.airbnb.enumeration.BookingStatus;
import com.airbnb.exception.ResourceNotFoundException;
import com.airbnb.models.*;
import com.airbnb.repository.*;
import com.airbnb.service.BookingService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/*
 * Copyright (c) 2025 Ayshi Shannidhya Panda. All rights reserved.
 *
 * This source code is confidential and intended solely for internal use.
 * Unauthorized copying, modification, distribution, or disclosure of this
 * file, via any medium, is strictly prohibited.
 *
 * Project: Airbnb
 * Author: Ayshi Shannidhya Panda
 * Created on: 18-12-2025
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final GuestRepo guestRepo;

    private final BookingRepo bookingRepo;
    private final HotelRepo hotelRepo;
    private final RoomRepo roomRepo;
    private final InventoryRepo inventoryRepo;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public BookingDTO initialiseBooking(@NonNull BookingRequest bookingRequest) {

        log.info("Initialising booking for hotel : {}, room : {}, date : {}-{}",
                bookingRequest.getHotelId(),
                bookingRequest.getRoomId(),
                bookingRequest.getCheckInDate(),
                bookingRequest.getCheckOutDate());

        Hotel hotel = hotelRepo.findById(bookingRequest.getHotelId()).orElseThrow(
                () -> new ResourceNotFoundException("Hotel not found with id: {}" + bookingRequest.getHotelId()));

        Room room = roomRepo.findById(bookingRequest.getRoomId()).orElseThrow(
                () -> new ResourceNotFoundException("Room not found with id: {}" + bookingRequest.getRoomId()));

        List<Inventory> inventoryList = inventoryRepo.findAndLockAvailableInventory(room.getId(),
                bookingRequest.getCheckInDate(), bookingRequest.getCheckOutDate(), bookingRequest.getRoomsCount());

        long daysCount = ChronoUnit.DAYS.between(bookingRequest.getCheckInDate(), bookingRequest.getCheckOutDate()) + 1;

        if (inventoryList.size() != daysCount) {
            throw new IllegalStateException("Room is not available");
        }

        //Reserve the room, update the booked count in inventories

        for (Inventory inventory : inventoryList) {
            inventory.setReservedCount(inventory.getReservedCount() + bookingRequest.getRoomsCount());
        }
        inventoryRepo.saveAll(inventoryList);

        //Create the booking


        //TODO : Calculate dynamic pricing

        Booking booking = Booking.builder()
                .hotel(hotel)
                .room(room)
                .user(getCurrentUser())
                .amount(BigDecimal.TEN)
                .roomsCount(bookingRequest.getRoomsCount())
                .checkInDate(bookingRequest.getCheckInDate())
                .checkOutDate(bookingRequest.getCheckOutDate())
                .bookingStatus(BookingStatus.RESERVED)
                .build();

        booking = bookingRepo.save(booking);

        return modelMapper.map(booking, BookingDTO.class);
    }

    @Override
    @Transactional
    public BookingDTO addGuests(Long bookingId, List<GuestDTO> guestDTOList) {

        log.info("Adding guest for booking with id: {}", bookingId);

        Booking booking = bookingRepo.findById(bookingId).orElseThrow(
                () -> new ResourceNotFoundException("Hotel not found with id: {}" + bookingId));

        if (hasBookingExpired(booking)) {
            throw new IllegalStateException("Booking is already expired.");
        }

        if (booking.getBookingStatus() != BookingStatus.RESERVED) {
            throw new IllegalStateException("Booking status is not reserved, To proceed kindly reserve it.");
        }

        for (GuestDTO guestDTO : guestDTOList) {
            Guest guest = modelMapper.map(guestDTO, Guest.class);
            guest.setUser(getCurrentUser());
            guest = guestRepo.save(guest);
            booking.getGuest().add(guest);
        }

        booking.setBookingStatus(BookingStatus.GUESTS_ADDED);
        booking = bookingRepo.save(booking);

        return modelMapper.map(booking, BookingDTO.class);
    }

    public boolean hasBookingExpired(@NonNull Booking booking) {
        return booking.getCreatedAt().plusMinutes(10).isBefore(LocalDateTime.now());
    }

    public User getCurrentUser() {
        User user = new User();
        user.setId(1L); //TODO : Remove dummy user
        return user;
    }
}
