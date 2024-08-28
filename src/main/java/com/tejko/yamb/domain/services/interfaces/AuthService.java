package com.tejko.yamb.domain.services.interfaces;

import com.tejko.yamb.api.dto.requests.AnonymousPlayerRequest;
import com.tejko.yamb.api.dto.requests.AuthRequest;
import com.tejko.yamb.api.dto.responses.AuthResponse;
import com.tejko.yamb.domain.models.RegisteredPlayer;

public interface AuthService {

    public AuthResponse login(AuthRequest authRequest);

    public RegisteredPlayer register(AuthRequest authRequest);

    public AuthResponse createAnonymousPlayer(AnonymousPlayerRequest authRequest);
    
}
