package com.tejko.yamb.domain.models;

import java.util.Set;
import java.util.UUID;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("ANONYMOUS")
public class AnonymousPlayer extends Player {

    private AnonymousPlayer() {}

    private AnonymousPlayer(String username, String password, Set<Role> roles) {
        super(username, password, roles);
    }

    public static AnonymousPlayer getInstance(String username, Set<Role> roles) {
        String password = UUID.randomUUID().toString();
        return new AnonymousPlayer(username, password, roles);
    }
   
}
