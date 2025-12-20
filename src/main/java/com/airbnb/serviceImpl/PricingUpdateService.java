package com.airbnb.serviceImpl;

import com.airbnb.models.Hotel;
import com.airbnb.models.HotelMinPrice;
import com.airbnb.models.Inventory;
import com.airbnb.repository.HotelMinPriceRepo;
import com.airbnb.repository.HotelRepo;
import com.airbnb.repository.InventoryRepo;
import com.airbnb.strategyImpl.PricingService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PricingUpdateService {

    //Scheduler to update the inventory and hotelMinPrice tables every hour

    private final HotelRepo hotelRepo;
    private final InventoryRepo inventoryRepo;
    private final HotelMinPriceRepo hotelMinPriceRepo;

    private final PricingService pricingService;
    
    @Scheduled(cron = "0 */5 * * * * ")
    public void updatePrices() {
        int pageNumber = 0;
        int pageSize = 100;

        while (true) {
            Page<Hotel> hotelPage = hotelRepo.findAll(PageRequest.of(pageNumber, pageSize));
            if (hotelPage.isEmpty()) {
                break;
            }
            hotelPage.getContent().forEach(this::updateHotelPrices);

            pageNumber++;
        }
    }

    private void updateHotelPrices(@NonNull Hotel hotel) {

        log.info("Updating hotel prices for hotel ID: {}", hotel.getId());

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusYears(1);

        List<Inventory> inventoryList = inventoryRepo.findByHotelAndDateBetween(hotel, startDate, endDate);

        updateInventoryPrices(inventoryList);

        updateHotelMinPrice(hotel, inventoryList, startDate, endDate);
    }

    private void updateInventoryPrices(@NonNull List<Inventory> inventoryList) {
        inventoryList.forEach(inventory -> {
            BigDecimal dynamicPricing = pricingService.calculateDynamicPricing(inventory);
            inventory.setPrice(dynamicPricing);
        });
        inventoryRepo.saveAll(inventoryList);
    }

    private void updateHotelMinPrice(Hotel hotel,
                                     @NonNull List<Inventory> inventoryList,
                                     LocalDate startDate,
                                     LocalDate endDate) {

        // Group inventory by date and find minimum price for each date
        Map<LocalDate, BigDecimal> dailyMinPrices = inventoryList
                .stream()
                .filter(inv -> !inv.getDate().isBefore(startDate) && !inv.getDate().isAfter(endDate))
                .collect(Collectors.groupingBy(
                        Inventory::getDate,
                        Collectors.mapping(
                                Inventory::getPrice,
                                Collectors.minBy(Comparator.naturalOrder())
                        )
                ))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().orElse(BigDecimal.ZERO)
                ));

        // Prepare entities in bulk
        List<HotelMinPrice> hotelMinPrices = new ArrayList<>();

        dailyMinPrices.forEach((date, price) -> {
            HotelMinPrice hotelMinPrice = hotelMinPriceRepo
                    .findByHotelAndDate(hotel, date)
                    .orElseGet(() -> new HotelMinPrice(hotel, date));

            hotelMinPrice.setPrice(price);
            hotelMinPrices.add(hotelMinPrice);
        });

        // Bulk save (IMPORTANT for performance)
        hotelMinPriceRepo.saveAll(hotelMinPrices);
    }
}
