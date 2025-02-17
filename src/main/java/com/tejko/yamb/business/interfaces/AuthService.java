package com.tejko.yamb.business.interfaces;

import com.tejko.yamb.domain.models.PlayerWithTokens;

import com.tejko.yamb.domain.models.Player;

public interface AuthService {

    PlayerWithTokens getToken(String email, String username, String password);

    PlayerWithTokens migrateToken(String token);

    PlayerWithTokens refreshTokens(String refreshToken);

    Player register(String email, String username, String password);

    PlayerWithTokens registerGuest(String username);

    void verifyEmail(String emailVerificationToken);
    
    void sendPasswordResetEmail(String email);
    
    void sendVerificationEmail(String email);

    void resetPassword(String passwordResetToken, String oldPassword, String newPassword);
    
}
