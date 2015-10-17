package com.fabian.vilo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Button;
import android.view.animation.AnimationUtils;
import android.util.Log;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import java.util.ArrayList;
import android.widget.ListView;

import com.fabian.vilo.models.CDModels.CDUser;
import com.fabian.vilo.models.CDModels.ModelManager;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

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

    private Context context;

    SharedPreferences sharedpreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        rootView = inflater.inflate(R.layout.activity_me, container, false);

        sharedpreferences = getContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

        context = getActivity();

        if(sharedpreferences.contains("loggedin")) {

            final TextView textViewToChange = (TextView) rootView.findViewById(R.id.txtInterestsCount);
            textViewToChange.setText("100");

            RelativeLayout coverImage = (RelativeLayout) rootView.findViewById(R.id.containerTop);

            Uri imageUri = Uri.parse("https://vilostorage.s3.amazonaws.com/profilepics/user_41444676563.png");
            //Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri);

            ImageButton profileImage = (ImageButton) rootView.findViewById(R.id.profile);

            //Picasso.with(getContext()).load("https://vilostorage.s3.amazonaws.com/profilepics/user_41444676563.png").into(coverImage);

            //((ImageButton)rootView.findViewById(R.id.profile)).setImageBitmap(Util.getRoundedCornerBitmap(((BitmapDrawable)((ImageButton) rootView.findViewById(R.id.profile)).getDrawable()).getBitmap(), 180));
            //((ImageButton)rootView.findViewById(R.id.profile)).setEnabled(false);
            //((ImageButton)rootView.findViewById(R.id.profile)).setImageBitmap(Util.getRoundedCornerBitmap(((BitmapDrawable) ((ImageButton) rootView.findViewById(R.id.profile)).getDrawable()).getBitmap()));
            //((ImageButton) rootView.findViewById(R.id.profile)).setImageBitmap(MLRoundedImageView.getCroppedBitmap(((BitmapDrawable) ((ImageButton) rootView.findViewById(R.id.profile)).getDrawable()).getBitmap(), 80));


            txtInterests = (TextView) rootView.findViewById(R.id.txtInterests);
            txtInterestsCount = (TextView) rootView.findViewById(R.id.txtInterestsCount);
            txtPosts = (TextView) rootView.findViewById(R.id.txtPosts);
            txtPostsCount = (TextView) rootView.findViewById(R.id.txtPostsCount);

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

            ModelManager modelManager = new ModelManager();
            Uri myUri = Uri.parse("https://vilostorage.s3.amazonaws.com/profilepics/user_31437672110.jpg");
            byte[] myBytes = modelManager.convertImageToByte(myUri, getContext());

            Log.d(TAG, "bytes: " + myBytes);

            Log.d(TAG, "bla" + frame);

            displayInterests();

            setHasOptionsMenu(true);

        }

        return rootView; //inflater.inflate(R.layout.activity_me, container, false);
    }

    /*@Override
    public void onResume() {
        super.onResume();

        Log.d(TAG, "VIEW RESUMED");

        if(sharedpreferences.contains("loggedin")){

            Realm realm = Realm.getInstance(context);
            // Build the query looking at all users:
            RealmQuery<CDUser> query = realm.where(CDUser.class);

            // Execute the query:
            RealmResults<CDUser> result = query.findAll();

            getActivity().setTitle(result.first().getFirst_name());


        } else {
            Log.d(TAG, "logged in pref is not set");
            Intent intent = new Intent(getActivity(), Login.class);
            startActivity(intent);
        }
    }*/

    /*@Override
    public void onResume() {
        super.onResume();
        Handler handler = getActivity().getWindow().getDecorView().getHandler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                // initialize the loader here!
                getLoaderManager().initLoader(0, null, context);
                getLoaderManager().initLoader(0, null, Tabbar.this);
            }
        });
    }*/

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Log.d(TAG, "ME SCREEN APPEARED");
        } else {
            Log.d(TAG, "ME SCREEN DISAPPEARED");
        }
    }


    void displayInterests() {
        //frame.removeAllViews();

        //View view = ((LayoutInflater) getActivity().getSystemService("layout_inflater")).inflate(R.layout.me_interests, frame, false);// .getSystemService("layout_inflater")).inflate(0x7f030011, frame, false);

        ListView myListView = (ListView) rootView.findViewById(R.id.listView);
        ListViewAdapter emptyAdapter = new ListViewAdapter(getActivity(), R.layout.me_interests, new ArrayList<String>());
        myListView.setAdapter(emptyAdapter);
        ArrayList<String> myStringArray1 = new ArrayList<String>();
        for (int l=0;l<5;l++) {
            myStringArray1.add("something");
        }

        ListViewAdapter adapter = new ListViewAdapter(getActivity(), R.layout.me_interests, myStringArray1);
        myListView.setAdapter(adapter);


        myListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View view,
                                            int position, long id) {

                        /*FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction()
                                                        .replace(R.id.meLayout, new ItemListFragment())
                                                        .commit();*/
                        Log.d(TAG, "item clicked!");

                        //Take action here.
                    }
                }
        );
        //view.startAnimation(AnimationUtils.loadAnimation(getActivity(), 0x7f040002));

        //rootView.inflate(R.layout.me_interests, frame, false);
        //rootView = rootView.inflate(App.context, R.layout.me_interests, frame);

        //View view = rootView.findViewById(R.id.interestList);
        //view.startAnimation(AnimationUtils.loadAnimation(getActivity()), );
        //View view = ((LayoutInflater)getActivity().getSystemService("layout_inflater")).inflate(0x7f030011, frame, false);
        //view.startAnimation(AnimationUtils.loadAnimation(getActivity(), 0x7f040002));


        //frame.addView(myListView);
        interestsSelected();
    }


    void displayOwnPosts() {

        /*frame.removeAllViews();

        View view = ((LayoutInflater) getActivity().getSystemService("layout_inflater")).inflate(R.layout.me_posts, frame, false);
        View view2 = ((LayoutInflater) getActivity().getSystemService("layout_inflater")).inflate(R.layout.me_posts, frame, false);

        frame.addView(view);
        frame.addView(view2);*/

        ListView myListView = (ListView) rootView.findViewById(R.id.listView);
        ListViewAdapter emptyAdapter = new ListViewAdapter(getActivity(), R.layout.me_interests, new ArrayList<String>());
        myListView.setAdapter(emptyAdapter);
        ArrayList<String> myStringArray1 = new ArrayList<String>();
        for (int l=0;l<3;l++) {
            myStringArray1.add("something");
        }

        ListViewAdapter adapter = new ListViewAdapter(getActivity(), R.layout.me_interests, myStringArray1);
        myListView.setAdapter(adapter);

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.add("Radius")
                .setIcon(R.drawable.nav_settings)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        inflater.inflate(R.menu.menu_tabbar, menu);
    }

}
