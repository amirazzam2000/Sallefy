package edu.url.salle.amir.azzam.sallefy.restapi.service;

import java.util.List;

import edu.url.salle.amir.azzam.sallefy.model.*;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TrackService {
    @GET("tracks")
    Call<List<Track>> getAllTracks(@Query("liked") boolean liked, @Query("played") boolean played, @Query("recent") boolean recent, @Query("size") int size,  @Header("Authorization") String token);

    @GET("tracks")
    Call<List<Track>> getAllTracks(@Header("Authorization") String token);

    @POST("tracks")
    Call<Track> createTrack(@Header("Authorization") String token, @Body Track track);

    //@PUT("tracks")
    //Call<Track> addTrack(@Header("Authorization") String token, @Path("track") Track track);

    @GET("tracks/{id}")
    Call<Track> getTrackId(@Header("Authorization") String token, @Path("id") int id);

    @DELETE("tracks/{id}")
    Call<Track> deleteTrack(@Header("Authorization") String token, @Path("track") int id);

    @GET("tracks/{id}/like")
    Call<Like> isLiked(@Header("Authorization") String token, @Path("id") int id);

    //TODO
    @PUT("tracks/{id}/like")
    Call<Like> likeTrackById(@Header("Authorization") String token, @Path("id") int id);

    @GET("users/{login}/tracks")
    Call<List<Track>> getUserTracks(@Path("login") String login, @Header("Authorization") String token);

    //doesnt work in the API but here is is
    //@PUT("tracks/{id}/like")
    //Call<Track> playTrackById(@Header("Authorization") String token, @Path("id") int id, LatLong latLong);


}
