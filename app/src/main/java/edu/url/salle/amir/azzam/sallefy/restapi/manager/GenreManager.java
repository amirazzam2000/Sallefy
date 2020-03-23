package edu.url.salle.amir.azzam.sallefy.restapi.manager;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import edu.url.salle.amir.azzam.sallefy.model.Genre;
import edu.url.salle.amir.azzam.sallefy.model.Track;
import edu.url.salle.amir.azzam.sallefy.model.UserToken;
import edu.url.salle.amir.azzam.sallefy.restapi.callback.GenreCallback;
import edu.url.salle.amir.azzam.sallefy.restapi.service.GenreService;
import edu.url.salle.amir.azzam.sallefy.utils.Constants;
import edu.url.salle.amir.azzam.sallefy.utils.Session;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GenreManager {

    private static final String TAG = "genreManager";
    private static GenreManager staticGenreManager;
    private Retrofit mRetrofit;
    private Context mContext;

    private GenreService mService;

    //gets an instance of genre manager
    public static GenreManager getInstance(Context context) {
        if (staticGenreManager == null) {
            staticGenreManager = new GenreManager(context);
        }
        return staticGenreManager;
    }

    //builds the context from retrofit
    private GenreManager(Context context) {
        mContext = context;
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constants.NETWORK.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mService = mRetrofit.create(GenreService.class);
    }

    //get all genres
    public synchronized void getAllGenres(final GenreCallback genreCallback) {
        //make sure there's an active session
        UserToken userToken = Session.getInstance(mContext).getUserToken();

        //
        Call<List<Genre>> call = mService.getAllGenres("Bearer " + userToken.getIdToken());
        //allow for multiple calls at once -> add calls to the queue
        call.enqueue(new Callback<List<Genre>>() {


            @Override
            public void onResponse(Call<List<Genre>> call, Response<List<Genre>> response) {
                int code = response.code();
                ArrayList<Genre> data = (ArrayList<Genre>) response.body();

                if (response.isSuccessful()) {
                    genreCallback.onGenresReceive(data);

                } else {
                    Log.d(TAG, "Error: " + code);
                    genreCallback.onFailure(new Throwable("ERROR " + code + ", " + response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<List<Genre>> call, Throwable t) {
                Log.d(TAG, "Error: " + t);
                genreCallback.onFailure(new Throwable("ERROR " + t.getMessage() ));
            }
        });
    }

    //Get all tracks
    public synchronized void getTracksByGenre(int genreId, final GenreCallback genreCallback) {
        UserToken userToken = Session.getInstance(mContext).getUserToken();

        Call<List<Track>> call = mService.getTracksByGenre( genreId, "Bearer " + userToken.getIdToken());
        call.enqueue(new Callback<List<Track>>() {
            @Override
            public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {
                int code = response.code();
                ArrayList<Track> data = (ArrayList<Track>) response.body();

                if (response.isSuccessful()) {
                    genreCallback.onTracksByGenre(data);

                } else {
                    Log.d(TAG, "Error: " + code);
                    genreCallback.onFailure(new Throwable("ERROR " + code + ", " + response.raw().message()));
                }
            }

            @Override
            public void onFailure(Call<List<Track>> call, Throwable t) {
                Log.d(TAG, "Error: " + t);
                genreCallback.onFailure(new Throwable("ERROR " + t.getMessage() ));
            }
        });

    }



}
