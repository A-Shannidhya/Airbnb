package com.airbnb.dto;

import com.airbnb.models.Hotel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/*
 * Copyright (c) 2025 Ayshi Shannidhya Panda. All rights reserved.
 *
 * This source code is confidential and intended solely for internal use.
 * Unauthorized copying, modification, distribution, or disclosure of this
 * file, via any medium, is strictly prohibited.
 *
 * Project: Airbnb
 * Author: Ayshi Shannidhya Panda
 * Created on: 20-12-2025
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelPriceDTO {
    private Hotel hotel;
    private BigDecimal price;

    /**
     * JPQL AVG(...) can be returned as different numeric types depending on dialect/provider.
     * This constructor keeps the projection stable.
     */
    public HotelPriceDTO(Hotel hotel, Number price) {
        this.hotel = hotel;
        this.price = (price == null) ? null : new BigDecimal(price.toString());
    }
}
