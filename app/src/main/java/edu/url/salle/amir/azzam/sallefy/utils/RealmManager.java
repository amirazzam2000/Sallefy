package edu.url.salle.amir.azzam.sallefy.utils;

import android.content.Context;

import edu.url.salle.amir.azzam.sallefy.model.Track;
import edu.url.salle.amir.azzam.sallefy.model.TrackRealm;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class RealmManager {
    private Realm realm;
    private static RealmManager realmManager;

    public static RealmManager getInstance(Context context){
        if(realmManager == null)
            realmManager = new RealmManager(context);
        return realmManager;
    }

    public RealmManager(Context context){
        Realm.init(context);
        realm = Realm.getDefaultInstance();
    }

    public void add(Track track){
        realm.executeTransaction(r -> {
            realm.insertOrUpdate(new TrackRealm(track));
        });
    }

    public TrackRealm[] getSongs(){
        RealmResults<TrackRealm> result = realm.where(TrackRealm.class).findAll();
        TrackRealm[] t = new TrackRealm[result.size()];
        for (int i = 0; i < result.size(); i++) {
            t[i] = (TrackRealm) result.get(i);
        }
        return t;
    }
}
