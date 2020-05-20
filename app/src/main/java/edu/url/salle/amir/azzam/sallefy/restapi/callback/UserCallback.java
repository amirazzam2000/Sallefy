package edu.url.salle.amir.azzam.sallefy.restapi.callback;

import java.util.List;

import edu.url.salle.amir.azzam.sallefy.model.User;
import edu.url.salle.amir.azzam.sallefy.model.UserToken;
import edu.url.salle.amir.azzam.sallefy.restapi.callback.FailureCallback;
import edu.url.salle.amir.azzam.sallefy.model.User;
import edu.url.salle.amir.azzam.sallefy.model.UserToken;

public interface UserCallback extends FailureCallback {
    void onLoginSuccess(UserToken userToken);
    void onLoginFailure(Throwable throwable);
    void onRegisterSuccess();
    void onRegisterFailure(Throwable throwable);
    void onUserInfoReceived(User userData);
    void onUserFollowed(boolean value);
    void onUserSelected(User user);
    void onFollowersUserReceived(List<User> followers);
    void onFollowingUsersReceived(List<User> following);
}
