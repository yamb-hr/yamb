package com.tejko.yamb.interfaces.services;

import com.tejko.yamb.api.payload.requests.AuthRequest;
import com.tejko.yamb.api.payload.responses.AuthResponse;
import com.tejko.yamb.domain.models.RegisteredPlayer;

public interface AuthService {

    public AuthResponse createAnonymousPlayer(AuthRequest tempPlayerCredentials);

    public AuthResponse login(AuthRequest playerCredentials);

    public RegisteredPlayer register(AuthRequest playerCredentials);
    
}
