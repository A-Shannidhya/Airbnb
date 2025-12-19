package com.airbnb.dto;

import com.airbnb.enumeration.BookingStatus;
import com.airbnb.models.Hotel;
import com.airbnb.models.User;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

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
@Data
public class BookingDTO {
    private Long id;
    //    private Hotel hotel;
//    private User user;
    private Integer roomsCount;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private BookingStatus bookingStatus;
    private Set<GuestDTO> guest;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
