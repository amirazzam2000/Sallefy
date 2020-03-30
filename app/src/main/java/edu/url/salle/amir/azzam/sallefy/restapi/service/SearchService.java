package edu.url.salle.amir.azzam.sallefy.restapi.service;

import edu.url.salle.amir.azzam.sallefy.model.Search;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface SearchService {

    @GET("search")
    Call<Search> SearchGlobally(@Query("keyword") String keyword, @Header("Authorization") String token);
}
