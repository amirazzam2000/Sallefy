package edu.url.salle.amir.azzam.sallefy.restapi.manager;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import edu.url.salle.amir.azzam.sallefy.model.Track;
import edu.url.salle.amir.azzam.sallefy.model.UserLogin;
import edu.url.salle.amir.azzam.sallefy.model.UserToken;
import edu.url.salle.amir.azzam.sallefy.restapi.callback.TrackCallback;
import edu.url.salle.amir.azzam.sallefy.restapi.callback.UserCallback;
import edu.url.salle.amir.azzam.sallefy.restapi.service.TrackService;
import edu.url.salle.amir.azzam.sallefy.restapi.service.UserService;
import edu.url.salle.amir.azzam.sallefy.restapi.service.UserTokenService;
import edu.url.salle.amir.azzam.sallefy.utils.Constants;
import edu.url.salle.amir.azzam.sallefy.utils.Session;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TrackManager {

    private static final String TAG = "UserManager";

    private static TrackManager sTrackManager;
    private Retrofit mRetrofit;
    private Context mContext;

    private TrackService mService;


    public static TrackManager getInstance(Context context) {
        if (sTrackManager == null) {
            sTrackManager = new TrackManager(context);
        }
        return sTrackManager;
    }

    private TrackManager(Context cntxt) {
        mContext = cntxt;
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constants.NETWORK.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mService = mRetrofit.create(TrackService.class);
    }

    public synchronized void getAllTracks (final TrackCallback trackCallback) {

        UserToken userToken = Session.getInstance(mContext).getUserToken();

        Call<List<Track>>  call = mService.getAllTracks( "Bearer " + userToken.getIdToken() );

        call.enqueue(new Callback<List<Track>>() {
            @Override
            public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {

                int code = response.code();
                List<Track> tracks = response.body();

                if (response.isSuccessful()) {
                    trackCallback.onTracksReceived(tracks);
                } else {
                    Log.d(TAG, "Error: " + code);
                    try {
                        trackCallback.onNoTracks(new Throwable("ERROR " + code + ", " + response.errorBody().string()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Track>> call, Throwable t) {
                Log.d(TAG, "Error: " + t.getMessage());
                trackCallback.onFailure(t);
            }
        });
    }


    public synchronized void getTrackId(final TrackCallback trackCallback, int id) {

        UserToken userToken = Session.getInstance(mContext).getUserToken();

        Call<Track>  call = mService.getTrackId( "Bearer " + userToken.getIdToken(), id);

        new Callback<Track>() {
            @Override
            public void onResponse(Call<Track> call, Response<Track> response) {
                int code = response.code();
                Track track = response.body();

                if (response.isSuccessful()) {
                    trackCallback.onTrackReceived(track);
                } else {
                    Log.d(TAG, "Error: " + code);
                    try {
                        trackCallback.onNoTracks(new Throwable("ERROR " + code + ", " + response.errorBody().string()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Track> call, Throwable t) {
                Log.d(TAG, "Error: " + t.getMessage());
                trackCallback.onFailure(t);
            }
        };
    }

}
