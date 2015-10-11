package com.fabian.vilo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.view.animation.AnimationUtils;
import android.util.Log;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import java.util.ArrayList;
import android.widget.ListView;

public class Me extends Fragment {

    private static final String TAG = Me.class.getSimpleName();

    View btnInterests;
    View btnPosts;
    FrameLayout frame;
    TextView txtInterests;
    TextView txtInterestsCount;
    TextView txtPosts;
    TextView txtPostsCount;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        rootView = inflater.inflate(R.layout.activity_me, container, false);

        final TextView textViewToChange = (TextView) rootView.findViewById(R.id.txtInterestsCount);
        textViewToChange.setText("100");

        //((ImageButton)rootView.findViewById(R.id.profile)).setImageBitmap(Util.getRoundedCornerBitmap(((BitmapDrawable)((ImageButton) rootView.findViewById(R.id.profile)).getDrawable()).getBitmap(), 180));
        //((ImageButton)rootView.findViewById(R.id.profile)).setEnabled(false);
        //((ImageButton)rootView.findViewById(R.id.profile)).setImageBitmap(Util.getRoundedCornerBitmap(((BitmapDrawable) ((ImageButton) rootView.findViewById(R.id.profile)).getDrawable()).getBitmap()));
        ((ImageButton)rootView.findViewById(R.id.profile)).setImageBitmap(MLRoundedImageView.getCroppedBitmap(((BitmapDrawable) ((ImageButton) rootView.findViewById(R.id.profile)).getDrawable()).getBitmap(), 80));

        txtInterests = (TextView) rootView.findViewById(R.id.txtInterests);
        txtInterestsCount = (TextView) rootView.findViewById(R.id.txtInterestsCount);
        txtPosts = (TextView) rootView.findViewById(R.id.txtPosts);
        txtPostsCount = (TextView) rootView.findViewById(R.id.txtPostsCount);

        ListView myListView = (ListView) rootView.findViewById(R.id.listView);
        ArrayList<String> myStringArray1 = new ArrayList<String>();
        for (int l=0;l<5;l++) {
            myStringArray1.add("something");
        }

        ListViewAdapter adapter = new ListViewAdapter(getActivity(), R.layout.me_interests, myStringArray1);
        myListView.setAdapter(adapter);

        rootView.findViewById(R.id.showInterests).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Interests clicked!");
                displayInterests();
            }
        });

        rootView.findViewById(R.id.showPosts).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Own Posts clicked!");
                displayOwnPosts();
            }
        });

        frame = (FrameLayout) rootView.findViewById(R.id.containerMe);

        Log.d(TAG, "bla"+frame);

        //displayInterests();

        return rootView; //inflater.inflate(R.layout.activity_me, container, false);
    }

    void displayInterests() {
        frame.removeAllViews();

        View view = ((LayoutInflater) getActivity().getSystemService("layout_inflater")).inflate(R.layout.me_interests, frame, false);// .getSystemService("layout_inflater")).inflate(0x7f030011, frame, false);
        //view.startAnimation(AnimationUtils.loadAnimation(getActivity(), 0x7f040002));

        //rootView.inflate(R.layout.me_interests, frame, false);
        //rootView = rootView.inflate(App.context, R.layout.me_interests, frame);

        //View view = rootView.findViewById(R.id.interestList);
        //view.startAnimation(AnimationUtils.loadAnimation(getActivity()), );
        //View view = ((LayoutInflater)getActivity().getSystemService("layout_inflater")).inflate(0x7f030011, frame, false);
        //view.startAnimation(AnimationUtils.loadAnimation(getActivity(), 0x7f040002));


        frame.addView(view);
        interestsSelected();
    }

    void displayOwnPosts() {

        frame.removeAllViews();

        View view = ((LayoutInflater) getActivity().getSystemService("layout_inflater")).inflate(R.layout.me_posts, frame, false);
        View view2 = ((LayoutInflater) getActivity().getSystemService("layout_inflater")).inflate(R.layout.me_posts, frame, false);

        frame.addView(view);
        frame.addView(view2);
        ownPostsSelected();
    }

    void interestsSelected() {
        txtPosts.setTextColor(getResources().getColor(R.color.colorPrimary));
        txtPostsCount.setTextColor(getResources().getColor(R.color.colorPrimary));
        txtInterests.setTextColor(getResources().getColor(R.color.colorAccent));
        txtInterestsCount.setTextColor(getResources().getColor(R.color.colorAccent));
    }

    void ownPostsSelected() {
        txtPosts.setTextColor(getResources().getColor(R.color.colorAccent));
        txtPostsCount.setTextColor(getResources().getColor(R.color.colorAccent));
        txtInterests.setTextColor(getResources().getColor(R.color.colorPrimary));
        txtInterestsCount.setTextColor(getResources().getColor(R.color.colorPrimary));
    }

}
