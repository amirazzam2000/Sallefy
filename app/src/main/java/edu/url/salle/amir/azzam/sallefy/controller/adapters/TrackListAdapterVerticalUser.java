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
import edu.url.salle.amir.azzam.sallefy.controller.callbacks.TrackListCallback;
import edu.url.salle.amir.azzam.sallefy.model.Track;
import edu.url.salle.amir.azzam.sallefy.model.User;
import edu.url.salle.amir.azzam.sallefy.restapi.callback.UserCallback;

public class TrackListAdapterVerticalUser extends RecyclerView.Adapter<TrackListAdapterVerticalUser.ViewHolder>{

    private static final String TAG = "TrackListAdapter";
    private ArrayList<User> mTracks;
    private Context mContext;
    private UserCallback mCallback;
    private int NUM_VIEWHOLDERS = 0;


    public TrackListAdapterVerticalUser(UserCallback callback, Context context, ArrayList<User> tracks ) {
        mTracks = tracks;
        mContext = context;
        mCallback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: called. Num viewHolders: " + NUM_VIEWHOLDERS++);


        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_track, parent, false);
        ViewHolder vh = new TrackListAdapterVerticalUser.ViewHolder(itemView);
        Log.d(TAG, "onCreateViewHolder: called. viewHolder hashCode: " + vh.hashCode());
        return vh;
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called. viewHolder hashcode: " + holder.hashCode());


        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mCallback.onUserSelected(mTracks.get(position));
            }
        });
        holder.tvTitle.setText((String)mTracks.get(position).getFirstName());
        holder.tvAuthor.setText(mTracks.get(position).getLogin());

        holder.tvTitle.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        holder.tvTitle.setSingleLine(true);
        holder.tvTitle.setMarqueeRepeatLimit(-1);
        holder.tvTitle.setSelected(true);

        holder.tvAuthor.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        holder.tvAuthor.setSingleLine(true);
        holder.tvAuthor.setMarqueeRepeatLimit(-1);
        holder.tvAuthor.setSelected(true);

        if (mTracks.get(position).getImageUrl() != null) {
            Glide.with(mContext)
                    .asBitmap()
                    .placeholder(R.drawable.ic_audiotrack)
                    .load(mTracks.get(position).getImageUrl())
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
        return mTracks != null ? mTracks.size():0;
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
