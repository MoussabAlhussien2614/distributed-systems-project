package com.example.user_service.dtos.response;

import com.example.user_service.models.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {

    private Integer id;
    private String firstName;
    private String lastName;
    private String username;
    private Role role;


}
