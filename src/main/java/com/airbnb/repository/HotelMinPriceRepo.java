package com.airbnb.repository;

import com.airbnb.dto.HotelPriceDTO;
import com.airbnb.models.Hotel;
import com.airbnb.models.HotelMinPrice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

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
@Repository
public interface HotelMinPriceRepo extends JpaRepository<HotelMinPrice, Long> {

    @Query("""
             SELECT new com.airbnb.dto.HotelPriceDTO(hmp.hotel, AVG(hmp.price))
             FROM HotelMinPrice hmp
             WHERE hmp.hotel.city = :city
                AND hmp.date BETWEEN :startDate AND :endDate
                AND hmp.hotel.active = true
                AND EXISTS (
                    SELECT 1
                    FROM Inventory inv
                    WHERE inv.hotel = hmp.hotel
                      AND inv.date = hmp.date
                      AND inv.closed = false
                      AND (inv.totalCount - inv.bookedCount - inv.reservedCount) >= :roomsCount
                )
             GROUP BY hmp.hotel
             HAVING COUNT(DISTINCT hmp.date) = :dateCount
            """)
    Page<HotelPriceDTO> findHotelsWithAvailableInventory(
            @Param("city") String city,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("roomsCount") Integer roomsCount,
            @Param("dateCount") Long dateCount,
            Pageable pageable
    );

    Optional<HotelMinPrice> findByHotelAndDate(Hotel hotel, LocalDate date);
}
