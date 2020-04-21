package edu.url.salle.amir.azzam.sallefy.controller.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.url.salle.amir.azzam.sallefy.R;
import edu.url.salle.amir.azzam.sallefy.model.Playlist;
import edu.url.salle.amir.azzam.sallefy.restapi.callback.PlaylistCallback;

public class PlayListAdapterHorizantal extends RecyclerView.Adapter<PlayListAdapterHorizantal.ViewHolder>{

    private static final String TAG = "TrackListAdapter";
    private ArrayList<Playlist> playlists;
    private Context mContext;
    private PlaylistCallback mCallback;
    private int NUM_VIEWHOLDERS = 0;


    public PlayListAdapterHorizantal(PlaylistCallback callback, Context context, ArrayList<Playlist> tracks ) {
        playlists = tracks;
        mContext = context;
        mCallback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: called. Num viewHolders: " + NUM_VIEWHOLDERS++);


        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_track_h, parent, false);
        ViewHolder vh = new PlayListAdapterHorizantal.ViewHolder(itemView);
        Log.d(TAG, "onCreateViewHolder: called. viewHolder hashCode: " + vh.hashCode());
        return vh;
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called. viewHolder hashcode: " + holder.hashCode());


        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onPlaylistSelected(playlists.get(position));
            }
        });
        holder.tvTitle.setText(playlists.get(position).getName());
        holder.tvTitle.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        holder.tvTitle.setSingleLine(true);
        holder.tvTitle.setMarqueeRepeatLimit(-1);
        holder.tvTitle.setSelected(true);

        holder.tvAuthor.setText(playlists.get(position).getUserLogin());
        holder.tvAuthor.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        holder.tvAuthor.setSingleLine(true);
        holder.tvAuthor.setMarqueeRepeatLimit(-1);
        holder.tvAuthor.setSelected(true);

        if (playlists.get(position).getThumbnail() != null) {
            Glide.with(mContext)
                    .asBitmap()
                    .placeholder(R.drawable.ic_audiotrack)
                    .load(playlists.get(position).getThumbnail())
                    .into(holder.ivPicture);
        }
        else{
            Glide.with(mContext)
                    .asBitmap()
                    .placeholder(R.drawable.ic_audiotrack)
                    .load(R.drawable.ic_logo)
                    .into(holder.ivPicture);
        }
    }

    @Override
    public int getItemCount() {
        return playlists != null ? playlists.size():0;
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout mLayout;
        TextView tvTitle;
        TextView tvAuthor;
        ImageView ivPicture;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mLayout = itemView.findViewById(R.id.track_item_layout);
            tvTitle = (TextView) itemView.findViewById(R.id.track_title);
            tvAuthor = (TextView) itemView.findViewById(R.id.track_author);
            ivPicture = (ImageView) itemView.findViewById(R.id.track_img);
        }
    }
}
