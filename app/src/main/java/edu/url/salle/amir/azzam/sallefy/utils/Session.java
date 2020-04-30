package edu.url.salle.amir.azzam.sallefy.utils;


import android.annotation.SuppressLint;
import android.content.Context;

import edu.url.salle.amir.azzam.sallefy.model.User;
import edu.url.salle.amir.azzam.sallefy.model.UserRegister;
import edu.url.salle.amir.azzam.sallefy.model.UserToken;

public class Session {

    @SuppressLint("StaticFieldLeak")
    private static Session sSession;
    private static Object mutex = new Object();

    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    private UserRegister mUserRegister;
    private User mUser;
    private UserToken mUserToken;

    public static Session getInstance(Context context) {
        Session result = sSession;
        mContext = context;
        if (result == null) {
            synchronized (mutex) {
                result = sSession;
                if (result == null)
                    sSession = result = new Session();
            }
        }
        return result;
    }

    private Session() {}

    public Session(Context context) {
        Session.mContext = context;
        this.mUserRegister = null;
        this.mUserToken = null;
    }

    public void resetValues() {
        mUserRegister = null;
        mUserToken = null;
    }

    public UserRegister getUserRegister() {
        return mUserRegister;
    }

    public void setUserRegister(UserRegister userRegister) {
        mUserRegister = userRegister;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public UserToken getUserToken() {
        return mUserToken;
    }

    public void setUserToken(UserToken userToken) {
        if (PreferenceUtils.getToken(mContext) == null || !PreferenceUtils.getToken(mContext).equals(userToken.getIdToken()))
            PreferenceUtils.saveToken(mContext, userToken.getIdToken());
        this.mUserToken = userToken;
    }

    public void setAudioEnabled(boolean b) {
    }
}
