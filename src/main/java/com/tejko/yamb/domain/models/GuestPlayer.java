package com.tejko.yamb.domain.models;

import java.util.Set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("GUEST")
public class GuestPlayer extends Player {

    protected GuestPlayer() {}

    protected GuestPlayer(String username, Set<Role> roles) {
        super(username, roles);
    }

    public static GuestPlayer getInstance(String username, Set<Role> roles) {
        return new GuestPlayer(username, roles);
    }
    
    @Override
    public String getPassword() {
        return null;
    }
   
}
