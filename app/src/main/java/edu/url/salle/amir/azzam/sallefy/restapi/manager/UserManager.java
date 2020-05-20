package edu.url.salle.amir.azzam.sallefy.restapi.manager;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.RequiresApi;
import edu.url.salle.amir.azzam.sallefy.model.Playlist;
import edu.url.salle.amir.azzam.sallefy.model.UserToken;
import edu.url.salle.amir.azzam.sallefy.restapi.service.UserService;
import edu.url.salle.amir.azzam.sallefy.restapi.service.UserTokenService;
import edu.url.salle.amir.azzam.sallefy.utils.Constants;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import edu.url.salle.amir.azzam.sallefy.model.User;
import edu.url.salle.amir.azzam.sallefy.model.UserLogin;
import edu.url.salle.amir.azzam.sallefy.model.UserRegister;
import edu.url.salle.amir.azzam.sallefy.restapi.callback.*;
import edu.url.salle.amir.azzam.sallefy.utils.Session;
import edu.url.salle.amir.azzam.sallefy.restapi.callback.UserCallback;

public class UserManager {

    private static final String TAG = "UserManager";

    private static UserManager sUserManager;
    private Retrofit mRetrofit;
    private Context mContext;

    private UserService mService;
    private UserTokenService mTokenService;
    UserToken userToken;

    public static UserManager getInstance(Context context) {
        if (sUserManager == null) {
            sUserManager = new UserManager(context);
        }
        return sUserManager;
    }

    private UserManager(Context cntxt) {
        mContext = cntxt;
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constants.NETWORK.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mService = mRetrofit.create(UserService.class);
        mTokenService = mRetrofit.create(UserTokenService.class);
    }

    public synchronized void loginAttempt (String username, String password, final UserCallback userCallback) {

        Call<UserToken> call = mTokenService.loginUser(new UserLogin(username, password, true));

        call.enqueue(new Callback<UserToken>() {
            @Override
            public void onResponse(Call<UserToken> call, Response<UserToken> response) {

                int code = response.code();
                UserToken userToken = response.body();
                Session.getInstance(mContext).setUserToken(userToken);

                if (response.isSuccessful()) {
                    //userToken = Session.getInstance(mContext).getUserToken();
                    userCallback.onLoginSuccess(userToken);

                } else {
                    Log.d(TAG, "Error: " + code);
                    userCallback.onLoginFailure(new Throwable("ERROR " + code + ", " + response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<UserToken> call, Throwable t) {
                Log.d(TAG, "Error: " + t.getMessage());
                userCallback.onFailure(t);
            }
        });
    }

    public synchronized void getUserData (String login, final UserCallback userCallback) {
        userToken = Session.getInstance(mContext).getUserToken();
        Call<User> call = mService.getUserById(login, "Bearer " + userToken.getIdToken());
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                int code = response.code();
                if (response.isSuccessful()) {
                    userCallback.onUserInfoReceived(response.body());
                } else {
                    Log.d(TAG, "Error NOT SUCCESSFUL: " + response.toString());
                    userCallback.onFailure(new Throwable("ERROR " + code + ", " + response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(TAG, "Error: " + t.getMessage());
                userCallback.onFailure(new Throwable("ERROR " + t.getStackTrace()));
            }
        });
    }

    public synchronized void registerAttempt (String email, String username, String password, String lastName, String firstName, final UserCallback userCallback) {

        Call<ResponseBody> call = mService.registerUser(new UserRegister(email, username, password, lastName , firstName));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                int code = response.code();
                if (response.isSuccessful()) {
                    userCallback.onRegisterSuccess();
                } else {
                    try {
                        userCallback.onRegisterFailure(new Throwable("ERROR " + code + ", " + response.errorBody().string()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                userCallback.onFailure(t);
            }
        });
    }

    public synchronized void isFollowing (String login,final UserCallback playlistCallback) {
        userToken = Session.getInstance(mContext).getUserToken();
        Call<User> call = mService.isFollowing("Bearer " + userToken.getIdToken(), login);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                int code = response.code();
                if (response.isSuccessful()) {
                    boolean value =  response.body().isFollowed();
                    playlistCallback.onUserFollowed(value);

                } else {
                    Log.d(TAG, "Error Not Successful: " + code);
                    playlistCallback.onFailure(new Throwable("ERROR " + code + ", " + response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(TAG, "Error Failure: " + t.getStackTrace());
                playlistCallback.onFailure(new Throwable("ERROR " + t.getStackTrace()));
            }
        });
    }

    public synchronized void follow (String login, final UserCallback playlistCallback) {
        userToken = Session.getInstance(mContext).getUserToken();
        Call<User> call = mService.follow("Bearer " + userToken.getIdToken(),  login);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                int code = response.code();
                if (response.isSuccessful()) {
                    playlistCallback.onUserFollowed(response.body().isFollowed());

                } else {
                    Log.d(TAG, "Error Not Successful: " + code);
                    playlistCallback.onFailure(new Throwable("ERROR " + code + ", " + response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(TAG, "Error Failure: " + t.getStackTrace());
                playlistCallback.onFailure(new Throwable("ERROR " + t.getStackTrace()));
            }
        });
    }


    public synchronized void getFollowers (final UserCallback userCallback) {
        userToken = Session.getInstance(mContext).getUserToken();
        Call<List<User>> call = mService.getFollowers("Bearer " + userToken.getIdToken());
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                int code = response.code();
                if (response.isSuccessful()) {
                    userCallback.onFollowersUserReceived((ArrayList<User>) response.body());

                } else {
                    Log.d(TAG, "Error Not Successful: " + code);
                    userCallback.onFailure(new Throwable("ERROR " + code + ", " + response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.d(TAG, "Error Failure: " + t.getStackTrace());
                userCallback.onFailure(new Throwable("ERROR " + t.getStackTrace()));
            }
        });
    }

    public synchronized void getFollowing (final UserCallback userCallback) {
        userToken = Session.getInstance(mContext).getUserToken();
        Call<List<User>> call = mService.getFollowing("Bearer " + userToken.getIdToken());
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                int code = response.code();
                if (response.isSuccessful()) {
                    userCallback.onFollowingUsersReceived((ArrayList<User>) response.body());

                } else {
                    Log.d(TAG, "Error Not Successful: " + code);
                    userCallback.onFailure(new Throwable("ERROR " + code + ", " + response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.d(TAG, "Error Failure: " + t.getStackTrace());
                userCallback.onFailure(new Throwable("ERROR " + t.getStackTrace()));
            }
        });
    }

}
