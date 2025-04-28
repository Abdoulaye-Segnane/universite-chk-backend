package com.universite.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String username;
    private String password;

    public String getEmail() {
        return null;
    }

    //@Getter @Setter Lombok
}
