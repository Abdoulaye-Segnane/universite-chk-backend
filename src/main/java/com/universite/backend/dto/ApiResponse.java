package com.universite.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApiResponse {
    // Getters & Setters
    private String message;
    private String status;
    private int code;

    public ApiResponse() {
    }

    public ApiResponse(String message, String status, int code) {
        this.message = message;
        this.status = status;
        this.code = code;
    }

}
