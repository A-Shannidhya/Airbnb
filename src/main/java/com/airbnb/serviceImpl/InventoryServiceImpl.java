package com.airbnb.serviceImpl;

import com.airbnb.dto.HotelDTO;
import com.airbnb.dto.HotelSearchRequest;
import com.airbnb.exception.ResourceNotFoundException;
import com.airbnb.models.Hotel;
import com.airbnb.models.Inventory;
import com.airbnb.models.Room;
import com.airbnb.repository.HotelRepo;
import com.airbnb.repository.InventoryRepo;
import com.airbnb.service.InventoryService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

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
@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImpl implements InventoryService {

    private final ModelMapper modelMapper;
    private final InventoryRepo inventoryRepo;

    @Override
    public void initializeRoomForAYear(Room room) {
        log.info("Initializing inventory for roomId={} hotelId={} for one year",
                room.getId(), room.getHotel().getId());

        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusYears(1);

        for (; !today.isAfter(endDate); today = today.plusDays(1)) {
            Inventory inventory = Inventory.builder()
                    .hotel(room.getHotel())
                    .room(room)
                    .date(today)
                    .bookedCount(0)
                    .reservedCount(0)
                    .totalCount(room.getTotalCount())
                    .surgeFactor(BigDecimal.ONE)
                    .price(room.getBasePrice())
                    .city(room.getHotel().getCity())
                    .closed(false)
                    .build();
            inventoryRepo.save(inventory);
        }

        log.info("Completed inventory initialization for roomId={}", room.getId());
    }

    @Override
    public void deleteAllInventories(Room room) {
        log.info("Deleting all inventories for roomId={}", room.getId());
        inventoryRepo.deleteByRoom(room);
        log.info("Deleted all inventories for roomId={}", room.getId());
    }

    @Override
    public Page<HotelDTO> searchHotels(@NonNull HotelSearchRequest hotelSearchRequest) {
        log.info("Searching hotels for city={}, startDate={}, endDate={}, roomsCount={}, page={}, size={}",
                hotelSearchRequest.getCity(),
                hotelSearchRequest.getStartDate(),
                hotelSearchRequest.getEndDate(),
                hotelSearchRequest.getRoomsCount(),
                hotelSearchRequest.getPage(),
                hotelSearchRequest.getSize());

        Pageable pageable = PageRequest.of(hotelSearchRequest.getPage(), hotelSearchRequest.getSize());

        long dateCount =
                ChronoUnit.DAYS.between(hotelSearchRequest.getStartDate(), hotelSearchRequest.getEndDate()) + 1;

        Page<Hotel> hotelPage = inventoryRepo.findHotelsWithAvailableInventory(
                hotelSearchRequest.getCity(),
                hotelSearchRequest.getStartDate(),
                hotelSearchRequest.getEndDate(),
                hotelSearchRequest.getRoomsCount(),
                dateCount,
                pageable
        );

        log.info("Found {} hotels matching search criteria", hotelPage.getTotalElements());

        //hotelPage is converted into HotelDTO
        return hotelPage.map((HotelElement) -> modelMapper.map(HotelElement, HotelDTO.class));
    }
}
