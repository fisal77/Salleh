package com.seniorproject.sallemapp.Activities;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.seniorproject.sallemapp.Activities.listsadpaters.FriendsListAdapter;
import com.seniorproject.sallemapp.R;
import com.seniorproject.sallemapp.entities.DomainPost;
import com.seniorproject.sallemapp.entities.DomainUser;
import com.seniorproject.sallemapp.entities.User;
import com.seniorproject.sallemapp.helpers.ListAsyncResult;
import com.seniorproject.sallemapp.helpers.LoadFriendsAsync;
import com.seniorproject.sallemapp.helpers.MyApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class FriendsFragment extends ListFragment implements ListAsyncResult<DomainUser> {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String _title;
    private int _page;
    private FriendsListAdapter mAdpater= null;
    private View mCurrentView;
    ArrayList<DomainUser> mFriendsList = new ArrayList<DomainUser>();
    ProgressBar mProgress;
    private OnFragmentInteractionListener mListener;
    Context mContext;
    MyApplication mMyApplication;
    public FriendsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param page Parameter 1.
     * @param title Parameter 2.
     * @return A new instance of fragment FriendsFragment.
     */
    public static FriendsFragment newInstance(int page, String title) {
        FriendsFragment fragment = new FriendsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, page);
        args.putString(ARG_PARAM2, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            _page = getArguments().getInt(ARG_PARAM1);
            _title = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mCurrentView = inflater.inflate(R.layout.fragment_friends, container, false);
        mProgress = (ProgressBar)mCurrentView.findViewById(R.id.friends_progressBar);
        mProgress.setVisibility(View.GONE);
        mContext =getContext();
        mMyApplication = (MyApplication)getActivity().getApplication();
        attachSearchButton();
        loadFriends();
        return mCurrentView;

    }

    private void loadFriends() {
        if(mMyApplication.Friends_Cach != null && mMyApplication.Friends_Cach.size() > 0){
            processFinish(mMyApplication.Friends_Cach);
            mProgress.setVisibility(View.GONE);
        }
        else{
            mProgress.setVisibility(View.VISIBLE);
        }
            LoadFriendsAsync loadFrinds = new LoadFriendsAsync
                    (mContext, DomainUser.CURRENT_USER.getId());
            loadFrinds.delegat = this;
            loadFrinds.execute();
    }

    private void attachSearchButton() {
        Button b = (Button) mCurrentView.findViewById(R.id.friends_btnSearch);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText searchText = (EditText)mCurrentView.findViewById(R.id.friends_txtSearch);
                String searchTerm = searchText.getText().toString();
                searchFriends(searchTerm);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        loadFriends();
    }

    private void searchFriends(final String term){
    ArrayList<DomainUser> s = new ArrayList<>();
        for(int i = 0; i < mFriendsList.size(); i++){
        boolean b = mFriendsList.get(i).getLasttName().toLowerCase().contains(term);
        boolean n = mFriendsList.get(i).getFirstName().toLowerCase().contains(term);
            if(b || n){
                s.add(mFriendsList.get(i));
            }
    }
        mAdpater = new FriendsListAdapter(this.getContext(), s);
        setListAdapter(mAdpater);
    }



    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void processFinish(List<DomainUser> result) {
        mProgress.setVisibility(View.GONE);
        if(result != null){
            mMyApplication.Friends_Cach = result;
            mFriendsList = (ArrayList<DomainUser>)result;
            mAdpater = new FriendsListAdapter(mContext, mFriendsList);
            setListAdapter(mAdpater);
        }

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
