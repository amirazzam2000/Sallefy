package edu.url.salle.amir.azzam.sallefy.restapi.manager;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

import edu.url.salle.amir.azzam.sallefy.model.Search;
import edu.url.salle.amir.azzam.sallefy.model.UserToken;
import edu.url.salle.amir.azzam.sallefy.restapi.callback.SearchCallback;
import edu.url.salle.amir.azzam.sallefy.restapi.service.SearchService;
import edu.url.salle.amir.azzam.sallefy.utils.Constants;
import edu.url.salle.amir.azzam.sallefy.utils.Session;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchManager {

    private static final String TAG = "SearchManager";

    private static SearchManager sSearchManager;
    private Retrofit mRetrofit;
    private Context mContext;

    private SearchService mService;

    public static SearchManager getInstance(Context context) {
        if (sSearchManager == null) {
            sSearchManager = new SearchManager(context);
        }
        return sSearchManager;
    }

    private SearchManager(Context cntxt) {
        mContext = cntxt;
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constants.NETWORK.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mService = mRetrofit.create(SearchService.class);
    }

    public synchronized void search (String keyword, final SearchCallback searchCallback) {

        UserToken userToken = Session.getInstance(mContext).getUserToken();

        Call<Search> call = mService.SearchGlobally(keyword, "Bearer " + userToken.getIdToken() );

        call.enqueue(new Callback<Search>() {
            @Override
            public void onResponse(Call<Search> call, Response<Search> response) {

                int code = response.code();
                Search search_result = response.body();

                if (response.isSuccessful()) {
                    searchCallback.onSearchResultsReceive(search_result);
                } else {
                    Log.d(TAG, "Error: " + code);
                    try {
                        searchCallback.onFailureReceive(new Throwable("ERROR " + code + ", " + response.errorBody().string()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Search> call, Throwable t) {
                Log.d(TAG, "Error: " + t.getMessage());
                searchCallback.onFailure(t);
            }
        });
    }



}
