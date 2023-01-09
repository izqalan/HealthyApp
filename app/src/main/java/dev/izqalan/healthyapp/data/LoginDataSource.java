package dev.izqalan.healthyapp.data;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import dev.izqalan.healthyapp.R;
import dev.izqalan.healthyapp.api.Auth;
import dev.izqalan.healthyapp.data.model.LoggedInUser;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {

        try {
            // TODO: handle loggedInUser authentication
            FirebaseUser mUser = Auth.signIn(username, password);
            LoggedInUser localUser = new LoggedInUser(mUser);
            return new Result.Success<>(localUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
        Auth.logout();
    }
}