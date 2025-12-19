package com.airbnb.advice;

/*
 * Copyright (c) 2025 Ayshi Shannidhya Panda. All rights reserved.
 *
 * This source code is confidential and intended solely for internal use.
 * Unauthorized copying, modification, distribution, or disclosure of this
 * file, via any medium, is strictly prohibited.
 *
 * Project: Airbnb
 * Author: Ayshi Shannidhya Panda
 * Created on: 15-12-2025
 */


import lombok.Data;

import java.time.LocalDate;

@Data
public class ApiResponse<T> {

    private LocalDate timestamp;
    private T data;
    private ApiError error;

    public ApiResponse() {
        this.timestamp = LocalDate.now();
    }

    /**
     * Success response constructor
     */
    public ApiResponse(T data) {
        this();
        this.data = data;
    }

    /**
     * Error response constructor
     */
    public ApiResponse(ApiError error) {
        this();
        this.error = error;
    }
}

