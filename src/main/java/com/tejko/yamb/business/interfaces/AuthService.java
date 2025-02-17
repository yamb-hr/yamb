package com.tejko.yamb.business.interfaces;

import com.tejko.yamb.domain.models.PlayerWithToken;
import com.tejko.yamb.domain.models.Player;

public interface AuthService {

    PlayerWithToken getToken(String email, String username, String password);

    Player register(String email, String username, String password);

    PlayerWithToken registerGuest(String username);

    void verifyEmail(String emailVerificationToken);
    
    void sendPasswordResetEmail(String email);
    
    void sendVerificationEmail(String email);

    void resetPassword(String passwordResetToken, String oldPassword, String newPassword);
    
}
