package com.airbnb.strategyImpl;

import com.airbnb.models.Inventory;
import com.airbnb.strategy.PricingStrategy;
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
 * Created on: 20-12-2025
 */
@Service
public class PricingService {

    /*
        base price is inside surge,
        surge is inside occupancy,
        occupancy is inside urgency,
        urgency is inside holiday

        which means basePrice is the core of this circle and Holiday is the outermost layer
     */

    //This is Decorated Design Pattern
    public BigDecimal calculateDynamicPricing(Inventory inventory) {
        PricingStrategy basePricingStrategy = new BasePricingStrategy();

        //apply the additional strategies
        basePricingStrategy = new SurgePricingStrategy(basePricingStrategy);
        basePricingStrategy = new OccupancyPricingStrategy(basePricingStrategy);
        basePricingStrategy = new UrgencyPricingStrategy(basePricingStrategy);
        basePricingStrategy = new HolidayPricingStrategy(basePricingStrategy);

        return basePricingStrategy.calculatePrice(inventory);
    }
}
