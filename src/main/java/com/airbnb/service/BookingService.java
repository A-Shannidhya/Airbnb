package com.airbnb.service;

import com.airbnb.dto.BookingDTO;
import com.airbnb.dto.BookingRequest;
import com.airbnb.dto.GuestDTO;
import org.springframework.stereotype.Service;

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
public interface BookingService {
    BookingDTO initialiseBooking(BookingRequest bookingRequest);

    BookingDTO addGuests(Long bookingId, List<GuestDTO> guestDTO);
}
