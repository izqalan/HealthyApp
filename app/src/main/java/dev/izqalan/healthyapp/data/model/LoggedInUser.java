package dev.izqalan.healthyapp.data.model;

import com.google.firebase.auth.FirebaseUser;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private String userId;
    private String displayName;

    public LoggedInUser(String userId, String displayName) {
        this.userId = userId;
        this.displayName = displayName;
    }

    public LoggedInUser(FirebaseUser mUser) {
        this.userId = mUser.getUid();
        this.displayName = mUser.getEmail();
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }
}