package com.airbnb.repository;

import com.airbnb.models.Hotel;
import com.airbnb.models.Inventory;
import com.airbnb.models.Room;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

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
@Repository
public interface InventoryRepo extends JpaRepository<Inventory, Long> {

    void deleteByRoom(Room room);

    @Query("""
             SELECT DISTINCT i.hotel
             FROM Inventory i
             WHERE i.city = :city
                AND i.date between :startDate and :endDate
                AND i.closed = false 
                AND (i.totalCount - i.bookedCount - i.reservedCount) >= :roomsCount 
             group by i.hotel, i.room
             having count(i.date) = :dateCount
            """)
    Page<Hotel> findHotelsWithAvailableInventory(
            @Param("city") String city,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("roomsCount") Integer roomsCount,
            @Param("dateCount") Long dateCount,
            Pageable pageable
    );

    @Query("""
            select i
            from Inventory i
            where i.room.id = :roomId
                AND i.date between :startDate and :endDate
                AND i.closed = false 
                AND (i.totalCount - i.bookedCount - i.reservedCount) >= :roomsCount           
            """)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Inventory> findAndLockAvailableInventory(
            @Param("roomId") Long roomId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("roomsCount") Integer roomsCount
    );

    List<Inventory> findByHotelAndDateBetween(Hotel hotel, LocalDate startDate, LocalDate endDate);
}
