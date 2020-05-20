package edu.url.salle.amir.azzam.sallefy.controller.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import edu.url.salle.amir.azzam.sallefy.R;
import edu.url.salle.amir.azzam.sallefy.controller.adapters.TrackListAdapterVertical;
import edu.url.salle.amir.azzam.sallefy.controller.adapters.TrackListAdapterVerticalPlaylist;
import edu.url.salle.amir.azzam.sallefy.controller.adapters.TrackListAdapterVerticalUser;
import edu.url.salle.amir.azzam.sallefy.model.User;
import edu.url.salle.amir.azzam.sallefy.model.UserToken;
import edu.url.salle.amir.azzam.sallefy.restapi.callback.UserCallback;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FollowingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FollowingFragment extends Fragment implements UserCallback {

    private final ArrayList<User> users;
    private RecyclerView mRecyclerView;

    public FollowingFragment(ArrayList<User> users) {
        // Required empty public constructor
        this.users = users;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FollowingFragment.
     */

    public static FollowingFragment newInstance(ArrayList<User> users) {
        FollowingFragment fragment = new FollowingFragment(users);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_following, container, false);
        initView(root);
        return root;
    }
    private void initView(View v) {
        mRecyclerView = (RecyclerView) v.findViewById(R.id.userList);
        LinearLayoutManager managerP2 = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(managerP2);

        if (users.size() >= 1){
            TrackListAdapterVerticalUser adapter = new TrackListAdapterVerticalUser(this, getActivity(), users);
            mRecyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onLoginSuccess(UserToken userToken) {

    }

    @Override
    public void onLoginFailure(Throwable throwable) {

    }

    @Override
    public void onRegisterSuccess() {

    }

    @Override
    public void onRegisterFailure(Throwable throwable) {

    }

    @Override
    public void onUserInfoReceived(User userData) {

    }

    @Override
    public void onUserFollowed(boolean value) {

    }

    @Override
    public void onUserSelected(User user) {
        OtherProfileViewFragment fragment = new OtherProfileViewFragment(user);
        FragmentTransaction t = this.getFragmentManager().beginTransaction();
        t.replace(R.id.nav_host_fragment, fragment);
        t.commit();
    }

    @Override
    public void onFollowersUserReceived(List<User> followers) {

    }

    @Override
    public void onFollowingUsersReceived(List<User> following) {

    }

    @Override
    public void onFailure(Throwable throwable) {

    }
}
