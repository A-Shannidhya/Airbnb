package com.airbnb.dto;

/*
 * Copyright (c) 2025 Ayshi Shannidhya Panda. All rights reserved.
 *
 * This source code is confidential and intended solely for internal use.
 * Unauthorized copying, modification, distribution, or disclosure of this
 * file, via any medium, is strictly prohibited.
 *
 * Project: Airbnb
 * Author: Ayshi Shannidhya Panda
 * Created on: 16-12-2025
 */

import lombok.Data;

import java.time.LocalDate;

import jakarta.validation.constraints.*;

@Data
public class HotelSearchRequest {

    @NotBlank(message = "City is required")
    @Size(min = 2, max = 50, message = "City name must be between 2 and 50 characters")
    private String city;

    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date cannot be in the past")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @Future(message = "End date must be in the future")
    private LocalDate endDate;

    @NotNull(message = "Rooms count is required")
    @Min(value = 1, message = "At least one room must be requested")
    @Max(value = 10, message = "Maximum 10 rooms can be requested at once")
    private Integer roomsCount;

    @Min(value = 0, message = "Page index cannot be negative")
    private Integer page = 0;

    @Min(value = 1, message = "Page size must be at least 1")
    @Max(value = 50, message = "Page size cannot exceed 50")
    private Integer size = 10;
}

