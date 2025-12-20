package com.airbnb.strategyImpl;

import com.airbnb.models.Inventory;
import com.airbnb.strategy.PricingStrategy;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

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


@RequiredArgsConstructor
public class BasePricingStrategy implements PricingStrategy {

    @Override
    public BigDecimal calculatePrice(@NonNull Inventory inventory) {
        return inventory.getRoom().getBasePrice();
    }
}
