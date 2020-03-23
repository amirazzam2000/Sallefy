package edu.url.salle.amir.azzam.sallefy.restapi.service;

import java.util.List;

import edu.url.salle.amir.azzam.sallefy.model.Track;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface TrackService {
    @GET("tracks")
//    Call<List<Track>> getAllTracks(@Query("liked") boolean liked, @Query("played") boolean played, @Query("recent") boolean recent, @Query("size") int size,  @Header("Authorization") String token);
    Call<List<Track>> getAllTracks(@Header("Authorization") String token);

}
