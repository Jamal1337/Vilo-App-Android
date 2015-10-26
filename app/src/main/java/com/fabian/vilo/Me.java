package com.fabian.vilo;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import android.widget.ListView;

import com.fabian.vilo.models.CDModels.CDPost;
import com.fabian.vilo.models.CDModels.CDUser;
import com.fabian.vilo.models.CDModels.ModelManager;
import com.squareup.picasso.Picasso;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class Me extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = Me.class.getSimpleName();

    View btnInterests;
    View btnPosts;
    FrameLayout frame;
    TextView txtInterests;
    TextView txtInterestsCount;
    TextView txtPosts;
    TextView txtPostsCount;
    View rootView;
    Realm realm;

    private ArrayList<CDPost> listViewPosts = new ArrayList<CDPost>();
    private ListView myListView;
    private ListViewAdapter adapter;

    private SwipeRefreshLayout swipeRefreshLayout;

    private Context context;

    SharedPreferences sharedpreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        rootView = inflater.inflate(R.layout.activity_me_2, container, false);

        sharedpreferences = getContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

        context = getActivity();

        if(sharedpreferences.contains("loggedin")) {

            RelativeLayout coverImage = (RelativeLayout) rootView.findViewById(R.id.containerTop);

            //Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri);

            //ImageButton profileImage = (ImageButton) rootView.findViewById(R.id.profile);


            realm = Realm.getInstance(context);

            // Build the query looking at all users:
            RealmQuery<CDUser> query = realm.where(CDUser.class);

            // Execute the query:
            RealmResults<CDUser> result = query.findAll();

            //Picasso.with(getContext()).load("https://graph.facebook.com/"+result.first().getFbid() + "/picture?width=500&height=500").into(profileImage);

            //((ImageButton)rootView.findViewById(R.id.profile)).setImageBitmap(Util.getRoundedCornerBitmap(((BitmapDrawable)((ImageButton) rootView.findViewById(R.id.profile)).getDrawable()).getBitmap(), 180));
            //((ImageButton)rootView.findViewById(R.id.profile)).setEnabled(false);
            //((ImageButton)rootView.findViewById(R.id.profile)).setImageBitmap(Util.getRoundedCornerBitmap(((BitmapDrawable) ((ImageButton) rootView.findViewById(R.id.profile)).getDrawable()).getBitmap()));
            //((ImageButton) rootView.findViewById(R.id.profile)).setImageBitmap(MLRoundedImageView.getCroppedBitmap(((BitmapDrawable) ((ImageButton) rootView.findViewById(R.id.profile)).getDrawable()).getBitmap(), 80));

            myListView = (ListView) rootView.findViewById(R.id.listView);
            /*swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
            swipeRefreshLayout.setOnRefreshListener(this);

            /**
             * Showing Swipe Refresh animation on activity create
             * As animation won't start on onCreate, post runnable is used
             */
            /*swipeRefreshLayout.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            swipeRefreshLayout.setRefreshing(true);

                                            refreshInterests();
                                        }
                                    }
            );*/

            View header = getActivity().getLayoutInflater().inflate(R.layout.me_list_header, null);
            myListView.addHeaderView(header);

            adapter = new ListViewAdapter(getActivity(), R.layout.listview_quickpost, listViewPosts, this);
            myListView.setAdapter(adapter);

            myListView.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> arg0, View view,
                                                int position, long id) {

                        /*FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction()
                                                        .replace(R.id.meLayout, new ItemListFragment())
                                                        .commit();*/
                            Log.d(TAG, "item at row "+position+" clicked!");
                            Log.d(TAG, "title of cell: "+listViewPosts.get(position-1).getTitle());

                            // Go to Detail Activity
                        /*Intent i = new Intent(MainActivity.this, DetailActivity.class);
                        // Send the position number to Detail Activity too.
                        i.putExtra("position", position);
                        // Run the process
                        startActivity(i);*/

                            /*Intent detailIntent = new Intent(context, QuickpostDetail.class);
                            detailIntent.putExtra(QuickpostDetail.ARG_ITEM_ID, id);
                            startActivity(detailIntent);*/

                            //Take action here.

                            android.support.v4.app.Fragment detail = new QuickpostDetail();
                            android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.containerMe, detail).addToBackStack("back").commit();
                            //fragmentManager.beginTransaction().add(R.id.listView, detail).addToBackStack("back").commit();
                        }
                    }
            );

            ImageView profileImage = (ImageView) header.findViewById(R.id.profile);
            Picasso.with(getContext()).load("https://graph.facebook.com/"+result.first().getFbid() + "/picture?width=500&height=500").into(profileImage);


            txtInterests = (TextView) header.findViewById(R.id.txtInterests);
            txtInterestsCount = (TextView) header.findViewById(R.id.txtInterestsCount);
            txtPosts = (TextView) header.findViewById(R.id.txtPosts);
            txtPostsCount = (TextView) header.findViewById(R.id.txtPostsCount);

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

    /**
     * This method is called when swipe refresh is pulled down
     */
    @Override
    public void onRefresh() {
        // TODO: geht irgendwie nicht im zusammenhang mit dem cell swipe...deswegen erstmal auskommentiert
        refreshInterests();
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
        if (isVisibleToUser && sharedpreferences.contains("loggedin")) {
            Log.d(TAG, "ME SCREEN APPEARED");

            realm = Realm.getInstance(context);

            // Build the query looking at all users:
            RealmQuery<CDUser> query = realm.where(CDUser.class);

            // Execute the query:
            RealmResults<CDUser> result = query.findAll();

            getActivity().setTitle(result.first().getFirst_name());

            final TextView textViewToChange = (TextView) rootView.findViewById(R.id.txtInterestsCount);
            textViewToChange.setText(""+getNumberOfInterestPosts());

            final TextView ownPosts = (TextView) rootView.findViewById(R.id.txtPostsCount);
            ownPosts.setText(""+getNumberOfOwnPosts());

            displayInterests();

            //getActivity().setTitle("Me");
        } else {
            Log.d(TAG, "ME SCREEN DISAPPEARED");
        }
    }


    void displayInterests() {
        //frame.removeAllViews();

        //View view = ((LayoutInflater) getActivity().getSystemService("layout_inflater")).inflate(R.layout.listview_quickpost, frame, false);// .getSystemService("layout_inflater")).inflate(0x7f030011, frame, false);

        /*ListView myListView = (ListView) rootView.findViewById(R.id.listView);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        /*swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);

                                        refreshInterests();
                                    }
                                }
        );*/

        realm = Realm.getInstance(context);

        RealmQuery<CDPost> query = realm.where(CDPost.class);

        query.equalTo("isOwn", 0);

        // Execute the query:
        RealmResults<CDPost> result = query.findAll();

        result.sort("last_updated", RealmResults.SORT_ORDER_DESCENDING);

        listViewPosts.clear();

        for (int l=0;l<result.size();l++) {
            listViewPosts.add(result.get(l));
        }

        //myListView.invalidateViews();

        //adapter.clear();
        //adapter.addAll(listViewPosts);
        adapter.notifyDataSetChanged();

        //frame.addView(myListView);
        interestsSelected();
    }


    void displayOwnPosts() {

        realm = Realm.getInstance(context);

        RealmQuery<CDPost> query = realm.where(CDPost.class);

        query.equalTo("isOwn", 1);

        // Execute the query:
        RealmResults<CDPost> result = query.findAll();

        result.sort("last_updated", RealmResults.SORT_ORDER_DESCENDING);

        listViewPosts.clear();
        for (int l=0;l<result.size();l++) {
            listViewPosts.add(result.get(l));
        }

        //myListView.invalidateViews();
        //adapter.clear();
        //adapter.addAll(listViewPosts);
        adapter.notifyDataSetChanged();

        //View header = getActivity().getLayoutInflater().inflate(R.layout.me_header, null);
        //myListView.addHeaderView(header);

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

    public int getNumberOfOwnPosts() {
        Realm realm = Realm.getInstance(context);

        // Build the query looking at all users:
        RealmQuery<CDPost> query = realm.where(CDPost.class);
        query.equalTo("isOwn", 1);

        // Execute the query:
        RealmResults<CDPost> result = query.findAll();

        return result.size();
    }

    public int getNumberOfInterestPosts() {
        Realm realm = Realm.getInstance(context);

        // Build the query looking at all users:
        RealmQuery<CDPost> query = realm.where(CDPost.class);
        query.equalTo("isOwn", 0);

        // Execute the query:
        RealmResults<CDPost> result = query.findAll();

        return result.size();
    }

    public void updateInterests() {
        final TextView textViewToChange = (TextView) rootView.findViewById(R.id.txtInterestsCount);
        textViewToChange.setText(""+getNumberOfInterestPosts());
    }

    public void refreshInterests() {
        swipeRefreshLayout.setRefreshing(true);

        // Do refresh from server


        swipeRefreshLayout.setRefreshing(false);
    }

}
