package edu.url.salle.amir.azzam.sallefy.restapi.manager;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.url.salle.amir.azzam.sallefy.model.Like;
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

    public synchronized void getMostPlayedTracks (final TrackCallback trackCallback) {

        UserToken userToken = Session.getInstance(mContext).getUserToken();

        Call<List<Track>>  call = mService.getMostPlayedTracks( true,"Bearer " + userToken.getIdToken() );

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

    public synchronized void getMostLikedTracks(final TrackCallback trackCallback) {

        UserToken userToken = Session.getInstance(mContext).getUserToken();

        Call<List<Track>>  call = mService.getMostLikedTracks( true, "Bearer " + userToken.getIdToken() );

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

    public synchronized void getMostRecentTracks (final TrackCallback trackCallback) {

        UserToken userToken = Session.getInstance(mContext).getUserToken();

        Call<List<Track>>  call = mService.getMostRecentTracks( true,"Bearer " + userToken.getIdToken() );

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
    public synchronized void getAllTracks (boolean liked , boolean played , boolean recent , int size ,final TrackCallback trackCallback) {

        UserToken userToken = Session.getInstance(mContext).getUserToken();

        Call<List<Track>>  call = mService.getAllTracks( liked , played ,recent, size,"Bearer " + userToken.getIdToken() );

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

    public synchronized void createTrack(Track track, final TrackCallback trackCallback) {
        UserToken userToken = Session.getInstance(mContext).getUserToken();

        Call<Track> call = mService.createTrack("Bearer " + userToken.getIdToken(), track);
        call.enqueue(new Callback<Track>() {
            @Override
            public void onResponse(Call<Track> call, Response<Track> response) {
                int code = response.code();
                if (response.isSuccessful()) {
                    trackCallback.onCreateTrack();
                } else {
                    Log.d(TAG, "Error Not Successful: " + code);
                    trackCallback.onFailure(new Throwable("ERROR " + code + ", " + response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<Track> call, Throwable t) {
                Log.d(TAG, "Error Failure: " + t.getStackTrace());
                trackCallback.onFailure(new Throwable("ERROR " + t.getStackTrace()));
            }

        });
    }


    public synchronized void getTrackId(final TrackCallback trackCallback, int id) {

        UserToken userToken = Session.getInstance(mContext).getUserToken();

        Call<Track>  call = mService.getTrackId( "Bearer " + userToken.getIdToken(), id);

        call.enqueue(new Callback<Track>() {
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
        });
    }


    public synchronized void deleteTrack(final TrackCallback trackCallback, int id) {

        UserToken userToken = Session.getInstance(mContext).getUserToken();

        Call<Track>  call = mService.deleteTrack( "Bearer " + userToken.getIdToken(), id);

        call.enqueue(new Callback<Track>() {
            @Override
            //TODO
            // there really isnt a response unless it didnt work, where you get a class (type Error) that has a message that tells you what went wrong
            public void onResponse(Call<Track> call, Response<Track> response) {
                int code = response.code();
                //Track track = response.body();

                if (response.isSuccessful()) {
                    trackCallback.onTrackDeleted();
                } else {
                    Log.d(TAG, "Error: " + code);
                    try {
                        //TODO
                        // I think this should be changed to something like onTrackNotFound, but this also should work since if the response is not successful we will show a message
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
        });
    }


    public synchronized void isLiked(final TrackCallback trackCallback, int id) {

        UserToken userToken = Session.getInstance(mContext).getUserToken();

        Call<Like>  call = mService.isLiked( "Bearer " + userToken.getIdToken(), id);

        call.enqueue(new Callback<Like>() {
            @Override
            public void onResponse(Call<Like> call, Response<Like> response) {
                int code = response.code();
                Like like = response.body();

                if (response.isSuccessful()) {
                    trackCallback.onLikeReceived(like);
                } else {
                    Log.d(TAG, "Error: " + code);
                    try {
                        trackCallback.onNoLike(new Throwable("ERROR " + code + ", " + response.errorBody().string()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Like> call, Throwable t) {
                Log.d(TAG, "Error: " + t.getMessage());
                trackCallback.onFailure(t);
            }
        });
    }


    public synchronized void like(final TrackCallback trackCallback, int id) {

        UserToken userToken = Session.getInstance(mContext).getUserToken();

        Call<Like>  call = mService.likeTrackById( "Bearer " + userToken.getIdToken(), id);

        call.enqueue(new Callback<Like>() {
            @Override
            public void onResponse(Call<Like> call, Response<Like> response) {
                int code = response.code();
                Like like = response.body();

                if (response.isSuccessful()) {
                    trackCallback.onLikeReceived(like);
                } else {
                    Log.d(TAG, "Error: " + code);
                    try {
                        trackCallback.onNoLike(new Throwable("ERROR " + code + ", " + response.errorBody().string()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Like> call, Throwable t) {
                Log.d(TAG, "Error: " + t.getMessage());
                trackCallback.onFailure(t);
            }
        });
    }

    public synchronized void getUserTracks(String login, final TrackCallback trackCallback) {
        UserToken userToken = Session.getInstance(mContext).getUserToken();

        Call<List<Track>> call = mService.getUserTracks(login, "Bearer " + userToken.getIdToken());
        call.enqueue(new Callback<List<Track>>() {
            @Override
            public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {
                int code = response.code();

                if (response.isSuccessful()) {
                    trackCallback.onUserTracksReceived(response.body());
                } else {
                    Log.d(TAG, "Error Not Successful: " + code);
                    trackCallback.onNoTracks(new Throwable("ERROR " + code + ", " + response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<List<Track>> call, Throwable t) {
                Log.d(TAG, "Error Failure: " + t.getStackTrace());
                trackCallback.onFailure(new Throwable("ERROR " + t.getStackTrace()));
            }
        });
    }

    public synchronized void getOwnTracks(final TrackCallback trackCallback) {
        UserToken userToken = Session.getInstance(mContext).getUserToken();
        Call<List<Track>> call = mService.getOwnTracks("Bearer " + userToken.getIdToken());
        call.enqueue(new Callback<List<Track>>() {
            @Override
            public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {

                int code = response.code();
                if (response.isSuccessful()) {
                    trackCallback.onPersonalTracksReceived((ArrayList<Track>) response.body());
                } else {
                    Log.d(TAG, "Error Not Successful: " + code);
                    trackCallback.onNoTracks(new Throwable("ERROR " + code + ", " + response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<List<Track>> call, Throwable t) {
                Log.d(TAG, "Error Failure: " + t.getStackTrace());
                trackCallback.onFailure(new Throwable("ERROR " + t.getStackTrace()));
            }
        });
    }

    public synchronized void getOwnLikedTracks(final TrackCallback trackCallback) {
        UserToken userToken = Session.getInstance(mContext).getUserToken();
        Call<List<Track>> call = mService.getOwnLikedTracks("Bearer " + userToken.getIdToken());

        call.enqueue(new Callback<List<Track>>() {
            @Override
            public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {

                int code = response.code();
                if (response.isSuccessful()) {
                    trackCallback.onUserLikedTracksReceived((ArrayList<Track>) response.body());
                } else {
                    Log.d(TAG, "Error Not Successful: " + code);
                    trackCallback.onNoTracks(new Throwable("ERROR " + code + ", " + response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<List<Track>> call, Throwable t) {
                Log.d(TAG, "Error Failure: " + t.getStackTrace());
                trackCallback.onFailure(new Throwable("ERROR " + t.getStackTrace()));
            }
        });

    }

        //TODO: Figure out how to do a PUT
    /*
    public synchronized void likeTrackById(final TrackCallback trackCallback, int id) {

        UserToken userToken = Session.getInstance(mContext).getUserToken();

        Call<Like>  call = mService.likeTrackById( "Bearer " + userToken.getIdToken(), id);

        new Callback<Like>() {
            @Override
            public void onResponse(Call<Like> call, Response<Like> response) {
                int code = response.code();
                Like like = response.body();

                if (response.isSuccessful()) {
                    trackCallback.onLikeReceived(like);
                } else {
                    Log.d(TAG, "Error: " + code);
                    try {
                        trackCallback.onNoLike(new Throwable("ERROR " + code + ", " + response.errorBody().string()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Like> call, Throwable t) {
                Log.d(TAG, "Error: " + t.getMessage());
                trackCallback.onFailure(t);
            }
        };
    }

     */



}
