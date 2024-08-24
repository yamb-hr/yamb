package com.tejko.yamb.domain.models;

import java.util.Set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("REGISTERED")
public class RegisteredPlayer extends Player {

    private RegisteredPlayer() {}

    private RegisteredPlayer(String username, String password, Set<Role> roles) {
        super(username, password, roles);
    }

    public static RegisteredPlayer getInstance(String username, String password, Set<Role> roles) {
        return new RegisteredPlayer(username, password, roles);
    }


}