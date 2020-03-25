package edu.url.salle.amir.azzam.sallefy.restapi.service;

import java.util.List;

import edu.url.salle.amir.azzam.sallefy.model.Like;
import edu.url.salle.amir.azzam.sallefy.model.Playlist;
import edu.url.salle.amir.azzam.sallefy.model.Track;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PlaylistService {

    @GET("playlists")
    Call<List<Playlist>> showPlaylists(@Query("popular") boolean popular, @Query("recent") boolean recent, @Query("size") int size, @Header("Authorization") String token);

    @POST("playlists")
    Call<Playlist> createPlaylist(@Header("Authorization") String token, @Body Playlist playlist);

    @PUT("playlists")
    Call<Playlist> updatePlaylist(@Header("Authorization") String token, @Body Playlist playlist);

    @GET("playlists/{id}")
    Call<Playlist> getPlaylistId(@Header("Authorization") String token, @Path("id") int id);

    @DELETE("playlists/{id}")
    Call<Playlist> deletePlaylist(@Header("Authorization") String token, @Path("id") int id);

    @GET("playlists/{id}/follow")
    Call<Playlist> checkisFollowed(@Header("Authorization") String token, @Path("id") int id);


    @PUT("playlists/{id}/follow")
    Call<Playlist> isFollowed(@Header("Authorization") String token, @Path("id") int id);

}
