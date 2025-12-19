package com.airbnb.serviceImpl;

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

import com.airbnb.dto.HotelDTO;
import com.airbnb.dto.HotelInfoDTO;
import com.airbnb.dto.RoomDTO;
import com.airbnb.exception.ResourceNotFoundException;
import com.airbnb.models.Hotel;
import com.airbnb.models.Room;
import com.airbnb.repository.HotelRepo;
import com.airbnb.repository.RoomRepo;
import com.airbnb.service.HotelService;
import com.airbnb.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class HotelServiceImpl implements HotelService {

    private final HotelRepo hotelRepo;
    private final ModelMapper modelMapper;
    private final InventoryService inventoryService;
    private final RoomRepo roomRepo;

    @Override
    public HotelDTO createNewHotel(HotelDTO hotelDTO) {
        log.info("Creating a new hotel with name: {}", hotelDTO.getName());

        Hotel hotel = modelMapper.map(hotelDTO, Hotel.class);// convert hotelDTO to Hotel
        hotel.setActive(false);
        hotel = hotelRepo.save(hotel);

        log.info("Created a new hotel with ID: {}", hotel.getId());
        return modelMapper.map(hotel, HotelDTO.class);
    }

    @Override
    public HotelDTO getHotelById(Long id) {
        log.info("Fetching hotel with ID: {}", id);

        Hotel hotel = hotelRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: " + id));

        return modelMapper.map(hotel, HotelDTO.class);
    }

    @Override
    public HotelDTO updateHotelById(Long id, HotelDTO hotelDTO) {
        log.info("Updating hotel with ID: {}", id);

        Hotel hotel = hotelRepo
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: " + id));

        modelMapper.map(hotelDTO, hotel);//All the fields from hotelDTO will transfer to hotel, like source to destination
        hotel.setId(id);
        hotel = hotelRepo.save(hotel);

        log.info("Updated hotel with ID: {}", hotel.getId());
        return modelMapper.map(hotel, HotelDTO.class);
    }

    @Override
    @Transactional
    public void deleteHotelById(Long id) {
        log.info("Deleting hotel with ID: {}", id);

        Hotel hotel = hotelRepo
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: " + id));

        for (Room room : hotel.getRooms()) {
            log.info("Deleting inventories and room for roomId={}", room.getId());
            inventoryService.deleteAllInventories(room);
            roomRepo.deleteById(room.getId());
        }

        hotelRepo.deleteById(id);
        log.info("Deleted hotel with ID: {}", id);
    }

    @Override
    public void activateHotel(long id) {
        log.info("Activating hotel with ID: {}", id);

        Hotel hotel = hotelRepo
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: " + id));

        hotel.setActive(true);
        hotelRepo.save(hotel);

        //assuming only do it once
        for (Room room : hotel.getRooms()) {
            log.info("Initializing inventory for roomId={} under hotelId={}", room.getId(), id);
            inventoryService.initializeRoomForAYear(room);
        }

        log.info("Hotel activated successfully with ID: {}", id);
    }

    @Override
    public HotelInfoDTO getHotelInfoById(Long hotelId) {
        log.info("Fetching hotel info for hotelId={}", hotelId);

        Hotel hotel = hotelRepo
                .findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel id not found: {}" + hotelId));

        List<RoomDTO> roomDTOS = hotel
                .getRooms()
                .stream()
                .map((element) -> modelMapper.map(element, RoomDTO.class))
                .toList();

        log.info("Fetched hotel info for hotelId={}, roomsCount={}", hotelId, roomDTOS.size());
        return new HotelInfoDTO(modelMapper.map(hotel, HotelDTO.class), roomDTOS);
    }
}
