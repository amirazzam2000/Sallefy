package edu.url.salle.amir.azzam.sallefy.restapi.callback;

import edu.url.salle.amir.azzam.sallefy.model.Search;

public interface SearchCallback extends FailureCallback {

    void onSearchResultsReceive(Search search_result);
    void onFailureReceive(Throwable throwable);
}
