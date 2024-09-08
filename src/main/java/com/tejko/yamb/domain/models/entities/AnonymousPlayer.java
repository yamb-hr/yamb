package com.tejko.yamb.domain.models.entities;

import java.util.Set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("ANONYMOUS")
public class AnonymousPlayer extends Player {

    protected AnonymousPlayer() {}

    protected AnonymousPlayer(String username, Set<Role> roles) {
        super(username, roles);
    }

    public static AnonymousPlayer getInstance(String username, Set<Role> roles) {
        return new AnonymousPlayer(username, roles);
    }
    
    @Override
    public String getPassword() {
        return null;
    }
   
}
