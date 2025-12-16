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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private boolean success;
    private T data;
    private ApiError error;
    private Instant timestamp;

    /**
     * Success response constructor
     */
    public ApiResponse(T data) {
        this.success = true;
        this.data = data;
        this.error = null;
        this.timestamp = Instant.now();
    }

    /**
     * Error response constructor
     */
    public ApiResponse(ApiError error) {
        this.success = false;
        this.data = null;
        this.error = error;
        this.timestamp = Instant.now();
    }
}

