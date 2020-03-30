package edu.url.salle.amir.azzam.sallefy.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Search {

    @SerializedName("playlists")
    private List<Playlist> foundPlaylists;
    @SerializedName("users")
    private List<User> foundUsers;
    @SerializedName("tracks")
    private List<Track> foundSongs;


    public Search(List<Playlist> foundPlaylists, List<User> foundUsers, List<Track> foundSongs) {
        this.foundPlaylists = foundPlaylists;
        this.foundUsers = foundUsers;
        this.foundSongs = foundSongs;
    }

    public List<Playlist> getFoundPlaylists() {
        return foundPlaylists;
    }

    public void setFoundPlaylists(List<Playlist> foundPlaylists) {
        this.foundPlaylists = foundPlaylists;
    }

    public List getFoundUsers() {
        return foundUsers;
    }

    public void setFoundUsers(List<User> foundUsers) {
        this.foundUsers = foundUsers;
    }

    public List<Track> getFoundSongs() {
        return foundSongs;
    }

    public void setFoundSongs(List<Track> foundSongs) {
        this.foundSongs = foundSongs;
    }
}
