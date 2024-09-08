package com.tejko.yamb.business.interfaces;

import com.tejko.yamb.domain.models.PlayerWithToken;
import com.tejko.yamb.domain.models.entities.RegisteredPlayer;

public interface AuthService {

    public PlayerWithToken login(String username, String password);

    public RegisteredPlayer register(String username, String password);

    public PlayerWithToken createAnonymousPlayer(String username);

    public void changePassword(String oldPassword, String newPassword);
    
}
