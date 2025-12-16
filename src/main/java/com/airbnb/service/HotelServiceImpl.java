package com.airbnb.service;

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
import com.airbnb.exception.ResourceNotFoundException;
import com.airbnb.models.Hotel;
import com.airbnb.repository.HotelRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class HotelServiceImpl implements HotelService {

    private final HotelRepo hotelRepo;
    private final ModelMapper modelMapper;

    @Override
    public HotelDTO createNewHotel(HotelDTO hotelDTO) {
        log.info("Creating a new hotel with name: {}", hotelDTO.getName());
        Hotel hotel = modelMapper.map(hotelDTO, Hotel.class);// convert hotelDTO to Hotel
        hotel.setActive(false);
        hotel = hotelRepo.save(hotel);
        log.info("Created a new hotel with ID: {}", hotelDTO.getId());
        return modelMapper.map(hotel, HotelDTO.class);
    }

    @Override
    public HotelDTO getHotelById(Long id) {
        log.info("Getting a new hotel with ID: {}", id);
        Hotel hotel = hotelRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: " + id));

        return modelMapper.map(hotel, HotelDTO.class);
    }

    @Override
    public HotelDTO updateHotelById(Long id, HotelDTO hotelDTO) {
        log.info("Updating a new hotel with ID: {}", id);
        Hotel hotel = hotelRepo
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: " + id));

        modelMapper.map(hotelDTO, hotel);//All the fields from hotelDTO will transfer to hotel, like source to destination
        hotel.setId(id);
        hotel = hotelRepo.save(hotel);
        return modelMapper.map(hotel, HotelDTO.class);
    }

    @Override
    public void deleteHotelById(Long id) {
        boolean exist = hotelRepo.existsById(id);
        if (!exist) throw new ResourceNotFoundException("Hotel not found with ID: " + id);
        else
            hotelRepo.deleteById(id);
//        TODO: delete the future inventories for this hotel
    }

    @Override
    public void activateHotel(long id) {
        log.info("Activating a new hotel with ID: {}", id);
        Hotel hotel = hotelRepo
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: " + id));
        hotel.setActive(true);
        //TODO: Create inventory of all rooms for this hotel

    }


}
