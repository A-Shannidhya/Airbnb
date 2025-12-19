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
 * Created on: 14-12-2025
 */

import com.airbnb.models.Hotel;
import lombok.Data;

import java.math.BigDecimal;

import jakarta.validation.constraints.*;

@Data
public class RoomDTO {

    @Positive(message = "Room ID must be positive")
    private Long id;

    private Hotel hotel;


    @NotNull(message = "Hotel ID is required")
    @Positive(message = "Hotel ID must be positive")
    private Long hotelId;

    @NotBlank(message = "Room type cannot be blank")
    @Size(min = 3, max = 50, message = "Room type must be between 3 and 50 characters")
    private String type;

    @NotNull(message = "Base price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Base price must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Invalid price format")
    private BigDecimal basePrice;

    @NotEmpty(message = "Room photos are required")
    @Size(max = 8, message = "Maximum 8 room photos allowed")
    private String[] photos;

    @NotEmpty(message = "Amenities cannot be empty")
    @Size(max = 15, message = "Maximum 15 amenities allowed")
    private String[] amenities;

    @NotNull(message = "Total room count is required")
    @Min(value = 1, message = "Total count must be at least 1")
    private Integer totalCount;

    @NotNull(message = "Room capacity is required")
    @Min(value = 1, message = "Capacity must be at least 1")
    @Max(value = 10, message = "Capacity cannot exceed 10")
    private Integer capacity;
}

