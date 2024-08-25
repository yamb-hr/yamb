package com.tejko.yamb.domain.services.interfaces;

import com.tejko.yamb.api.dto.requests.AuthRequest;
import com.tejko.yamb.api.dto.responses.AuthResponse;
import com.tejko.yamb.domain.models.RegisteredPlayer;

public interface AuthService {

    public AuthResponse createAnonymousPlayer(AuthRequest tempPlayerCredentials);

    public AuthResponse login(AuthRequest playerCredentials);

    public RegisteredPlayer register(AuthRequest playerCredentials);
    
}
