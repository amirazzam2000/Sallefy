package edu.url.salle.amir.azzam.sallefy.restapi.manager;

import android.content.Context;
import android.net.Uri;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import androidx.appcompat.app.AppCompatActivity;
import edu.url.salle.amir.azzam.sallefy.controller.UploadActivity;
import edu.url.salle.amir.azzam.sallefy.model.Genre;
import edu.url.salle.amir.azzam.sallefy.model.Track;
import edu.url.salle.amir.azzam.sallefy.restapi.callback.TrackCallback;
import edu.url.salle.amir.azzam.sallefy.utils.CloudinaryConfigs;
import edu.url.salle.amir.azzam.sallefy.utils.Session;


public class CloudinaryManager extends AppCompatActivity {

    private static CloudinaryManager sManager;
    private Context mContext;
    private String mFileName;
    private Genre mGenre;
    private TrackCallback mCallback;
    private ConcurrentLinkedQueue<Map> requests;
    private int requestNum;

    public static CloudinaryManager getInstance(Context context, TrackCallback callback) {
        if (sManager == null) {
            sManager = new CloudinaryManager(context, callback);
        }
        return sManager;
    }

    public CloudinaryManager(Context context, TrackCallback callback) {
        mContext = context;
        mCallback = callback;
        requests = new ConcurrentLinkedQueue<>();
        MediaManager.init(mContext, CloudinaryConfigs.getConfigurations());
    }

    public synchronized void uploadAudioFile(Uri fileUri, String fileName, Genre genre) {
        mGenre = genre;
        mFileName = fileName;
        Map<String, Object> options = new HashMap<>();
        options.put("public_id", fileName);
        options.put("folder", "sallefy/songs/mobile");
        options.put("resource_type", "video");

        MediaManager.get().upload(fileUri)
                .unsigned(fileName)
                .options(options)
                .callback(new CloudinaryCallback())
                .dispatch();
    }

    public synchronized void uploadImageFile(Uri fileUri, String fileName) {


        Map<String, Object> options = new HashMap<>();
        options.put("public_id", fileName);
        options.put("folder", "sallefy/images/mobile");
        options.put("resource_type", "image");

        MediaManager.get().upload(fileUri)
                .unsigned(fileName)
                .options(options)
                .callback(new CloudinaryCallback())
                .dispatch();
    }

    private class CloudinaryCallback implements UploadCallback {

        @Override
        public void onStart(String requestId) {
        }
        @Override
        public void onProgress(String requestId, long bytes, long totalBytes) {
            Double progress = (double) bytes/totalBytes;
        }
        @Override
        public void onSuccess(String requestId, Map resultData) {
            requestNum++;
            requests.add(resultData);
            if(requestNum == 2) {
                Track track = new Track();
                track.setId(null);
                track.setName(mFileName);
                track.setUser(Session.getInstance(mContext).getUser());
                track.setUserLogin(Session.getInstance(mContext).getUser().getLogin());
                track.setThumbnail((String) requests.poll().get("url") );
                track.setUrl((String) requests.poll().get("url"));
                ArrayList<Genre> genres = new ArrayList<>();
                genres.add(mGenre);
                track.setGenres(genres);
                TrackManager.getInstance(mContext).createTrack(track, mCallback);
            }
        }
        @Override
        public void onError(String requestId, ErrorInfo error) {

        }
        @Override
        public void onReschedule(String requestId, ErrorInfo error) {
        }
    }
}

