package com.tejko.yamb.api.payload.responses;

import java.util.List;

public class PlayerResponse extends BaseResponse {

    public String name;
    public List<RoleResponse> roles;
    public boolean tempUser;
    
}