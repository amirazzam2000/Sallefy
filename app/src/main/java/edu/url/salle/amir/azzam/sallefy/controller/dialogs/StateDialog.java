package edu.url.salle.amir.azzam.sallefy.controller.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import edu.url.salle.amir.azzam.sallefy.R;
import edu.url.salle.amir.azzam.sallefy.controller.UploadActivity;

public class StateDialog {

    private static StateDialog sManager;
    private static Object mutex = new Object();

    private Context mContext;
    private Dialog mDialog;

    private TextView tvTitle;
    private TextView tvSubtitle;
    private ImageView ivIcon;
    private ImageView ivLike;
    private ImageView ivFollow;
    private Button btnAccept;
    private ProgressBar bar;

    public static StateDialog getInstance(Context context) {

        sManager = new StateDialog(context);

        return sManager;
    }

    private StateDialog(Context context) {
        mContext = context;
        mDialog = new Dialog(mContext);
    }

    public void showStateDialog(boolean completed) {
        mDialog.setContentView(R.layout.dialog_state);
        mDialog.setCanceledOnTouchOutside(false);

        tvTitle = (TextView) mDialog.findViewById(R.id.dialog_state_title);
        tvSubtitle = (TextView) mDialog.findViewById(R.id.dialog_state_subtitle);
        btnAccept = (Button) mDialog.findViewById(R.id.dialog_state_button);
        bar = (ProgressBar) mDialog.findViewById(R.id.loadingPanel);
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.cancel();
            }
        });

        if ((completed)) {
            completedTask();
        } else {
            inProgressTask();
        }
        mDialog.show();
    }


    private void inProgressTask() {
        tvTitle.setText(R.string.state_wait);
        tvSubtitle.setText(R.string.state_task_in_progress);
        bar.setVisibility(View.VISIBLE);
        btnAccept.setVisibility(View.GONE);
    }

    private void completedTask() {
        tvTitle.setText(R.string.state_success_title);
        tvSubtitle.setText(R.string.state_task_completed);
        bar.setVisibility(View.GONE);
        btnAccept.setVisibility(View.VISIBLE);
    }

    public boolean isDialogShown() {
        return mDialog.isShowing();
    }


}
