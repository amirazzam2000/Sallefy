package edu.url.salle.amir.azzam.sallefy.controller.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
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
import edu.url.salle.amir.azzam.sallefy.model.User;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    public static final String TAG = UserAdapter.class.getName();

    private User mUsers;
    private Context mContext;

    public UserAdapter(User user, Context context) {
        mUsers = user;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        ViewHolder vh = new UserAdapter.ViewHolder(v);
        Log.d(TAG, "onCreateViewHolder: called. viewHolder hashCode: " + vh.hashCode());
        return vh;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(mUsers!= null){
            holder.tvUsername.setText(mUsers.getLogin());
            if (mUsers.getImageUrl() != null) {
                Glide.with(mContext)
                        .asBitmap()
                        .placeholder(R.drawable.ic_person_black_24dp)
                        .load(mUsers.getImageUrl())
                        .into(holder.ivPhoto);
            }else{
                Glide.with(mContext)
                        .asBitmap()
                        .placeholder(R.drawable.ic_person_black_24dp)
                        .load(R.drawable.ic_logo)
                        .into(holder.ivPhoto);
            }

        }else{
            holder.tvUsername.setText("User_Name");
            Glide.with(mContext)
                .asBitmap()
                .placeholder(R.drawable.ic_person_black_24dp)
                .load(R.drawable.ic_person_black_24dp)
                .into(holder.ivPhoto);
        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvUsername;
        ImageView ivPhoto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = (TextView) itemView.findViewById(R.id.item_user_name);
            ivPhoto = (ImageView) itemView.findViewById(R.id.user_img);

        }
    }



}
