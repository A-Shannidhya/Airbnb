package com.airbnb.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

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
 * Created on: 18-12-2025
 */
@Data
@AllArgsConstructor
public class HotelInfoDTO {

    private HotelDTO hotelDTO;
    private List<RoomDTO> roomDTO;


}
