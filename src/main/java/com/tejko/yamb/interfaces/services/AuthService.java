package com.tejko.yamb.interfaces.services;

import com.tejko.yamb.api.payload.requests.AuthRequest;
import com.tejko.yamb.api.payload.responses.AuthResponse;
import com.tejko.yamb.domain.models.Player;

public interface AuthService {

    public AuthResponse login(AuthRequest playerCredentials);

    public Player register(AuthRequest playerCredentials);

    public AuthResponse createTempPlayer(AuthRequest tempPlayerCredentials);
    
}
