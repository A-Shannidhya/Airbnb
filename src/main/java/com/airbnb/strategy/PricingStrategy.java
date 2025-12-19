package com.airbnb.strategy;

import com.airbnb.models.Inventory;
import org.springframework.stereotype.Service;

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
 * Created on: 19-12-2025
 */

@Service
public interface PricingStrategy {

    BigDecimal calculatePrice(Inventory inventory);
}
