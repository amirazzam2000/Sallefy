package edu.url.salle.amir.azzam.sallefy.restapi.service;

import edu.url.salle.amir.azzam.sallefy.model.UserLogin;
import edu.url.salle.amir.azzam.sallefy.model.UserToken;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserTokenService {
    @POST("authenticate")
    Call<UserToken> loginUser(@Body UserLogin login);
}
