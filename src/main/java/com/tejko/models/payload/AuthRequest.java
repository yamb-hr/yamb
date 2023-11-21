package com.tejko.models.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class AuthRequest {

    @NotBlank
    @Size(min = 5, max = 15)
    public String username;

    @NotBlank
    @Size(min = 10)
    public String password;

}
