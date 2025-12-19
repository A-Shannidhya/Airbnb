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

import com.airbnb.models.HotelContactInfo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class HotelDTO {

    @Positive(message = "Hotel ID must be positive")
    private Long id;

    @NotBlank(message = "Hotel name cannot be blank")
    @Size(min = 3, max = 100, message = "Hotel name must be between 3 and 100 characters")
    private String name;

    @NotBlank(message = "City cannot be blank")
    @Size(min = 2, max = 50, message = "City name must be between 2 and 50 characters")
    private String city;

    @NotEmpty(message = "At least one photo is required")
    @Size(max = 10, message = "Maximum 10 photos allowed")
    private String[] photos;

    @NotEmpty(message = "Amenities cannot be empty")
    @Size(max = 20, message = "Maximum 20 amenities allowed")
    private String[] amenities;

    @NotNull(message = "Active status must be specified")
    private Boolean active;

    @NotNull(message = "Hotel contact info is required")
    @Valid
    private HotelContactInfo hotelContactInfo;
}

