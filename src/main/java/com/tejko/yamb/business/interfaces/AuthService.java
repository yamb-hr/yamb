package com.tejko.yamb.business.interfaces;

import com.tejko.yamb.domain.models.PlayerWithToken;
import com.tejko.yamb.domain.models.RegisteredPlayer;

public interface AuthService {

    public PlayerWithToken getToken(String username, String password);

    public RegisteredPlayer register(String username, String password);

    public PlayerWithToken registerGuest(String username);

    public void changePassword(String oldPassword, String newPassword);
    
}
