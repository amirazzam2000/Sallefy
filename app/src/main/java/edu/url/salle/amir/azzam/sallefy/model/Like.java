package edu.url.salle.amir.azzam.sallefy.model;

import com.google.gson.annotations.SerializedName;

public class Like {

    @SerializedName("liked")
    private boolean liked;

    public void setLiked(boolean liked){
        this.liked = liked;
    }
    public boolean getLiked(){
        return this.liked;
    }
}
