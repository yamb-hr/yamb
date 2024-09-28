package com.tejko.yamb.domain.models;

import java.util.Set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("REGISTERED")
public class RegisteredPlayer extends Player {

    protected RegisteredPlayer() {}

    protected RegisteredPlayer(String username, String password, Set<Role> roles) {
        super(username, roles);
        setPassword(password);
    }

    public static RegisteredPlayer getInstance(String username, String password, Set<Role> roles) {
        return new RegisteredPlayer(username, password, roles);
    }

}