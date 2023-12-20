package com.tejko.yamb.models.payload;

public class PrincipalResponse {

    private String principal;

    public PrincipalResponse(String principal) {
        this.principal = principal;
    }

    public String getPrincipal() {
        return principal;
    }
    
}
