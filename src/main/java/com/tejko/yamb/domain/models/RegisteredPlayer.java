package com.tejko.yamb.domain.models;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("REGISTERED")
public class RegisteredPlayer extends Player {

    @Column(name = "password", nullable = true)
    private String password;

    protected RegisteredPlayer() {}

    protected RegisteredPlayer(String username, String password, Set<Role> roles) {
        super(username, roles);
        this.password = password;
    }

    public static RegisteredPlayer getInstance(String username, String password, Set<Role> roles) {
        return new RegisteredPlayer(username, password, roles);
    }

    // public upgradeFromGuest()...

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}