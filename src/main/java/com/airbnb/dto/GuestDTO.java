package com.airbnb.dto;

import com.airbnb.enumeration.Gender;
import com.airbnb.models.User;
import lombok.Data;

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
public class GuestDTO {
    private Long id;
    private User user;
    private String name;
    private Gender gender;
    private Integer age;
}
