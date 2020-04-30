package edu.url.salle.amir.azzam.sallefy.model;

import android.media.MediaPlayer;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class TrackRealm extends RealmObject {

    @SerializedName("color")
    private String color;

    @SerializedName("duration")
    private Integer duration;

    @PrimaryKey
    @SerializedName("id")
    private Integer id;

    @SerializedName("name")
    private String name;

    @SerializedName("released")
    private String released;

    @SerializedName("thumbnail")
    private String thumbnail;

    @SerializedName("url")
    private String url;

    private String login;

    private boolean selected = false;

    private boolean liked;


    public TrackRealm() {
    }

    public TrackRealm(Track track) {
        this.color = track.getColor();
        this.duration = track.getDuration();
        this.id = track.getId();
        this.name = track.getName();
        this.released = track.getReleased();
        this.thumbnail = track.getThumbnail();
        this.url = track.getUrl();
        this.login = track.getUserLogin();
        this.liked = track.isLiked();
    }


    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getUserLogin() {
        return login;
    }
}
