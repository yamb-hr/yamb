package com.tejko.yamb.domain.services.interfaces;

import com.tejko.yamb.domain.models.RegisteredPlayer;

public interface AuthService {

    public String login(String username, String password);

    public RegisteredPlayer register(String username, String password);

    public String createAnonymousPlayer(String username);
    
}
