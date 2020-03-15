package edu.url.salle.amir.azzam.sallefy.model;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;

public class UserToken {

    @SerializedName("id_Token")
    private String idToken;

    public UserToken(String idToken) {
        this.idToken = idToken;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

}
