package com.tejko.yamb.websocket;

import java.security.Principal;

import java.util.Objects;

public class StompPrincipal implements Principal {
    
    private final Long id;

    public StompPrincipal(Long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return String.valueOf(id);
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StompPrincipal that = (StompPrincipal) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
