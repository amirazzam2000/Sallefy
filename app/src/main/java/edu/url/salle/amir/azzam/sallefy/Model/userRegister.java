package edu.url.salle.amir.azzam.sallefy.model;

import android.os.Build;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.RequiresApi;

public class UserRegister {
    @SerializedName("activated")
    private boolean activated;
    @SerializedName("authorities")
    private ArrayList<String> authorities;
    @SerializedName("createdBy")
    private String createdBy;
    @SerializedName("createdDate")
    private String createdDate;
    @SerializedName("email")
    private String email;
    @SerializedName("firstName")
    private String firstName;
    @SerializedName("followers")
    private int followers;
    @SerializedName("following")
    private int following;
    @SerializedName("id")
    private int id;
    @SerializedName("imageUrl")
    private String imageUrl;
    @SerializedName("langKey")
    private String langKey;
    @SerializedName("lastModifiedBy")
    private String lastModifiedBy;
    @SerializedName("lastModifiedDate")
    private String lastModifiedDate;
    @SerializedName("lastName")
    private String lastName;
    @SerializedName("login")
    private String login;
    @SerializedName("playlists")
    private int playlists;
    @SerializedName("tracks")
    private int tracks;
    @SerializedName("password")
    private String password;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public UserRegister(String createdBy, String email, String firstName, String lastModifiedBy, String lastName, String login, String password) {
        this.createdBy = createdBy;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.langKey = "en";
        this.lastModifiedBy = lastModifiedBy;
        this.lastName = lastName;
        this.login = login;
        this.activated = true;
        this.authorities = new ArrayList<String>();
        this.authorities.add("ROLE_USER");

        this.createdDate =  LocalDateTime.now() + "Z";
        this.followers = 0;
        this.following = 0;
        this.id = 0;
        this.imageUrl = null;
        this.lastModifiedBy = createdBy;
        this.lastModifiedDate = LocalDateTime.now() + "Z";
        this.playlists = 0;
        this.tracks = 0;

    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(ArrayList<String> authorities) {
        this.authorities = authorities;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getPlaylists() {
        return playlists;
    }

    public void setPlaylists(int playlists) {
        this.playlists = playlists;
    }

    public int getTracks() {
        return tracks;
    }

    public void setTracks(int tracks) {
        this.tracks = tracks;
    }
}
