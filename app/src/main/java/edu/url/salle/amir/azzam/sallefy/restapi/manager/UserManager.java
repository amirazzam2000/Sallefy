package edu.url.salle.amir.azzam.sallefy.restapi.manager;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

import edu.url.salle.amir.azzam.sallefy.model.UserLogin;
import edu.url.salle.amir.azzam.sallefy.model.UserToken;
import edu.url.salle.amir.azzam.sallefy.restapi.callback.UserCallback;
import edu.url.salle.amir.azzam.sallefy.restapi.service.UserService;
import edu.url.salle.amir.azzam.sallefy.restapi.service.UserTokenService;
import edu.url.salle.amir.azzam.sallefy.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserManager {

    private static final String TAG = "UserManager";

    private static UserManager sUserManger;
    private Retrofit mRetrofit;
    private Context mContext;

    private UserService mService;
    private UserTokenService mTokenService;

    public static UserManager getInstance(Context context){
        if(sUserManger == null){
            sUserManger = new UserManager(context);
        }
        return sUserManger;
    }

    private UserManager(Context context){
        mContext = context;
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constants.NETWORK.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mService = mRetrofit.create(UserService.class);
        mTokenService = mRetrofit.create(UserTokenService.class);
    }

    public synchronized void loginAttempt (String username, String password, final UserCallback userCallback){
        Call<UserToken> call = mTokenService.loginUer(new UserLogin(username, password, true));

        call.enqueue(new Callback<UserToken>() {
            @Override
            public void onResponse(Call<UserToken> call, Response<UserToken> response) {
                int code = response.code();
                UserToken userToken = response.body();

                if(response.isSuccessful()){
                    userCallback.onLoginSuccess(userToken);
                }
                else{
                    Log.d(TAG, "Error: " +code);
                    try {
                        userCallback.onLoginFailure(new Throwable("Error " + code + ", " + response.errorBody().string()));
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserToken> call, Throwable t) {
                Log.d(TAG, "Error: " + t.getMessage());
                userCallback.onFailure(t);
            }
        });
    }

}
