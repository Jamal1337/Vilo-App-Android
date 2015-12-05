package com.fabian.vilo.me_screen;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import java.util.HashMap;
import java.util.Map;

import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.fabian.vilo.CustomViewPager;
import com.fabian.vilo.MainApplication;
import com.fabian.vilo.Settings;
import com.fabian.vilo.Tabbar;
import com.fabian.vilo.api.ViloApiAdapter;
import com.fabian.vilo.api.ViloApiEndpointInterface;
import com.fabian.vilo.custom_methods.Util;
import com.fabian.vilo.detail_views.QuickpostDetail;
import com.fabian.vilo.R;
import com.fabian.vilo.adapters.ListViewAdapter;
import com.fabian.vilo.models.CDModels.CDComment;
import com.fabian.vilo.models.CDModels.CDPost;
import com.fabian.vilo.models.CDModels.CDUser;
import com.fabian.vilo.models.CDModels.ModelManager;
import com.fabian.vilo.models.FbUserAuth;
import com.fabian.vilo.models.UpdatePosts;
import com.fabian.vilo.models.User;
import com.facebook.AccessToken;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class Me extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = Me.class.getSimpleName();

    private Tracker mTracker;

    View btnInterests;
    View btnPosts;
    FrameLayout frame;
    TextView txtInterests;
    TextView txtInterestsCount;
    TextView txtPosts;
    TextView txtPostsCount;
    View rootView;
    Realm realm;
    View header;

    private ArrayList<CDPost> listViewPosts = new ArrayList<CDPost>();
    private ListView myListView;
    private ListViewAdapter adapter;

    private Boolean firstViewLaunch = true;

    private SwipeRefreshLayout swipeRefreshLayout;

    private Context context;

    private CDUser user;

    SharedPreferences sharedpreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Obtain the shared Tracker instance.
        MainApplication application = (MainApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();

        rootView = inflater.inflate(R.layout.activity_me, container, false);

        sharedpreferences = getContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

        context = getActivity();

        //if(sharedpreferences.contains("loggedin")) {

            RelativeLayout coverImage = (RelativeLayout) rootView.findViewById(R.id.containerTop);

            //Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri);

            //ImageButton profileImage = (ImageButton) rootView.findViewById(R.id.profile);


            /*realm = Realm.getInstance(context);

            // Build the query looking at all users:
            RealmQuery<CDUser> query = realm.where(CDUser.class);

            // Execute the query:
            RealmResults<CDUser> result = query.findAll();

            user = result.first();*/
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

            header = getActivity().getLayoutInflater().inflate(R.layout.me_list_header, null);
            myListView.addHeaderView(header);

            adapter = new ListViewAdapter(getActivity(), R.layout.listview_quickpost, listViewPosts, this);
            myListView.setAdapter(adapter);

            myListView.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> arg0, View view,
                                                int position, long id) {

                            Log.d(TAG, "item at row " + position + " clicked!");
                            Log.d(TAG, "title of cell: " + listViewPosts.get(position - 1).getTitle());

                            ((Tabbar) getActivity()).findViewById(R.id.tab_layout).setVisibility(View.GONE);
                            ((Tabbar) getActivity()).findViewById(R.id.fab).setVisibility(View.GONE);

                            CustomViewPager pager = (CustomViewPager) ((Tabbar) getActivity()).findViewById(R.id.pager);
                            pager.setSwipeable(false);

                            QuickpostDetail quickpostDetail = new QuickpostDetail();
                            quickpostDetail.setTitle(getActivity().getTitle().toString());
                            quickpostDetail.setPost(listViewPosts.get(position - 1));
                            Fragment fragment = quickpostDetail;

                            FragmentManager manager = ((Tabbar) getActivity()).getSupportFragmentManager();
                            FragmentTransaction transaction = manager.beginTransaction(); //getParentFragment().getFragmentManager().beginTransaction();// manager.beginTransaction();
                            transaction.addToBackStack(null);
                            transaction.hide(Me.this);
                            transaction.replace(R.id.main_layout, fragment); // newInstance() is a static factory method.
                            transaction.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                            transaction.commit();

                        }
                    }
            );

            //ImageView profileImage = (ImageView) header.findViewById(R.id.profile);
            //Picasso.with(getContext()).load("https://graph.facebook.com/"+result.first().getFbid() + "/picture?width=500&height=500").into(profileImage);


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

            //displayInterests();

            setHasOptionsMenu(true);

        //}

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

            mTracker.setScreenName("AroundMe");
            mTracker.send(new HitBuilders.ScreenViewBuilder().build());

            if (firstViewLaunch) {
                realm = Realm.getInstance(context);

                // Build the query looking at all users:
                RealmQuery<CDUser> query = realm.where(CDUser.class);

                // Execute the query:
                RealmResults<CDUser> result = query.findAll();

                user = result.first();

                getActivity().setTitle(result.first().getFirst_name());

                ImageView profileImage = (ImageView) header.findViewById(R.id.profile);
                Glide.with(getContext()).load(user.getUserPhoto()).into(profileImage);

                firstViewLaunch = false;
            }

            /*realm = Realm.getInstance(context);

            // Build the query looking at all users:
            RealmQuery<CDUser> query = realm.where(CDUser.class);

            // Execute the query:
            RealmResults<CDUser> result = query.findAll();

            getActivity().setTitle(result.first().getFirst_name());*/

            final TextView textViewToChange = (TextView) header.findViewById(R.id.txtInterestsCount);
            Log.d(TAG, textViewToChange.toString());
            // TODO: crasht beim ersten mal auf den Me Screen gehen nachm Login
            textViewToChange.setText(String.valueOf(getNumberOfInterestPosts()));

            final TextView ownPosts = (TextView) header.findViewById(R.id.txtPostsCount);
            ownPosts.setText(String.valueOf(getNumberOfOwnPosts()));

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

        ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String,String>>();
        for (int l=0;l<result.size();l++) {
            HashMap<String, String> post = new HashMap<String, String>();
            post.put("id", String.valueOf(result.get(l).getId()));
            post.put("last_updated", result.get(l).getLast_updated());
            post.put("comment_count", String.valueOf(result.get(l).getCommentcount()));

            arrayList.add(post);
            listViewPosts.add(result.get(l));
        }

        Map<String, ArrayList> checkPosts = new HashMap<String, ArrayList>();
        checkPosts.put("data", arrayList);

        //myListView.invalidateViews();

        //adapter.clear();
        //adapter.addAll(listViewPosts);
        adapter.notifyDataSetChanged();

        //frame.addView(myListView);
        interestsSelected();

        updatePosts(checkPosts, 0);
    }


    void displayOwnPosts() {

        realm = Realm.getInstance(context);

        RealmQuery<CDPost> query = realm.where(CDPost.class);

        query.equalTo("isOwn", 1);

        // Execute the query:
        RealmResults<CDPost> result = query.findAll();

        result.sort("last_updated", RealmResults.SORT_ORDER_DESCENDING);

        listViewPosts.clear();

        ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String,String>>();
        for (int l=0;l<result.size();l++) {
            HashMap<String, String> post = new HashMap<String, String>();
            post.put("id", String.valueOf(result.get(l).getId()));
            post.put("last_updated", result.get(l).getLast_updated());
            post.put("comment_count", String.valueOf(result.get(l).getCommentcount()));

            arrayList.add(post);
            listViewPosts.add(result.get(l));
        }

        Map<String, ArrayList> checkPosts = new HashMap<String, ArrayList>();
        checkPosts.put("data", arrayList);

        //myListView.invalidateViews();
        //adapter.clear();
        //adapter.addAll(listViewPosts);
        adapter.notifyDataSetChanged();

        //View header = getActivity().getLayoutInflater().inflate(R.layout.me_header, null);
        //myListView.addHeaderView(header);

        ownPostsSelected();

        updatePosts(checkPosts, 1);
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
        MenuItem item = menu.add("Settings");

        item
                .setIcon(R.drawable.nav_settings)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        inflater.inflate(R.menu.menu_tabbar, menu);
    }

    public int getNumberOfOwnPosts() {
        //Realm realm = Realm.getInstance(context);

        // Build the query looking at all users:
        RealmQuery<CDPost> query = realm.where(CDPost.class);
        query.equalTo("isOwn", 1);

        // Execute the query:
        RealmResults<CDPost> result = query.findAll();

        return result.size();
    }

    public int getNumberOfInterestPosts() {
        //Realm realm = Realm.getInstance(context);

        // Build the query looking at all users:
        RealmQuery<CDPost> query = realm.where(CDPost.class);
        query.equalTo("isOwn", 0);

        // Execute the query:
        RealmResults<CDPost> result = query.findAll();

        Log.d(TAG, "array size: "+result.size());

        return result.size();
    }

    public void updateInterests() {
        final TextView textViewToChange = (TextView) rootView.findViewById(R.id.txtInterestsCount);
        textViewToChange.setText(String.valueOf(getNumberOfInterestPosts()));
    }

    public void updateOwn() {
        final TextView textViewToChange = (TextView) rootView.findViewById(R.id.txtPostsCount);
        textViewToChange.setText(String.valueOf(getNumberOfOwnPosts()));
    }

    public void refreshInterests() {
        swipeRefreshLayout.setRefreshing(true);

        // Do refresh from server


        swipeRefreshLayout.setRefreshing(false);
    }

    public void updatePosts(final Map<String, ArrayList> pushPosts, final Integer isOwn) {
        if (new Util().isNetworkAvailable(context)) {

            ViloApiAdapter viloAdapter = ViloApiAdapter.getInstance(context);

            ViloApiEndpointInterface apiService = viloAdapter.mApi;

            Call<UpdatePosts> call = apiService.updatePosts(pushPosts);

            call.enqueue(new Callback<UpdatePosts>() {
                @Override
                public void onResponse(Response<UpdatePosts> response, Retrofit retrofit) {

                    Log.d(TAG, "response code: "+response.code());
                    Log.d(TAG, "array: "+pushPosts);

                    if (response.code() == 200) {

                        Integer totalPosts = response.body().data.size();
                        for (int j = 0; j < totalPosts; j++) {

                            // check if some comments have been deleted, if so delete them in core data

                            Log.d(TAG, "postid"+response.body().data.get(j).id);
                            Log.d(TAG, "post deleted"+response.body().data.get(j).post_deleted);
                            Log.d(TAG, "comments deleted"+response.body().data.get(j).deleted_comments);
                            Log.d(TAG, "attendent"+response.body().data.get(j).attendentcount);
                            Log.d(TAG, "lastupdated"+response.body().data.get(j).last_updated);
                            Log.d(TAG, "followers"+response.body().data.get(j).followers);
                            Log.d(TAG, "main deleted"+response.body().data.get(j).maindeleted);
                            Log.d(TAG, "userid"+response.body().data.get(j).userid);
                            Log.d(TAG, "comment count: "+response.body().data.get(j).commentcount);

                            if (response.body().data.get(j).deleted_comments != null) {

                                for (int z=0; z < response.body().data.get(j).deleted_comments.size(); z++) {
                                    CDComment toEdit = realm.where(CDComment.class)
                                            .equalTo("commentid", response.body().data.get(j).deleted_comments.get(z)).findFirst();
                                    realm.beginTransaction();
                                    toEdit.removeFromRealm();
                                    realm.commitTransaction();
                                }

                            }

                            CDPost post = realm.where(CDPost.class).equalTo("id", response.body().data.get(j).id).findFirst();
                            realm.beginTransaction();

                            if (post != null) {
                                // check if main photo of photo album has been deleted
                                if (post.getType() == 3) {
                                    if (response.body().data.get(j).maindeleted == 1) {
                                        post.setAttachment(null);
                                        post.setImgURL("");
                                    }
                                }

                                // check if whole post has been deleted, if so remove user and image from main post
                                if (response.body().data.get(j).post_deleted == 1) {
                                    post.setUserid(1);
                                    post.setAttachment(null);
                                    post.setImgURL("");
                                }

                                // check if meet post attendents have changed
                                if (post.getType() == 4) {
                                    if (post.getAttendentcount() != response.body().data.get(j).attendentcount) {
                                        post.setAttendentcount(response.body().data.get(j).attendentcount);
                                    }
                                }

                                // check if likes differ from current one, if so => update
                                if (post.getInterestcount() != response.body().data.get(j).followers) {
                                    post.setInterestcount(response.body().data.get(j).followers);
                                }

                                // if there are new comments and the last comment has not been made by the user himself, set the post to new comments available

                                if (response.body().data.get(j).commentcount != null) {
                                    if (Integer.parseInt(response.body().data.get(j).commentcount) > post.getCommentcount()) {
                                        post.setCommentcount(Integer.parseInt(response.body().data.get(j).commentcount));
                                        if (response.body().data.get(j).userid != user.getUserid()) {
                                            post.setIsNew(1);
                                        } else {
                                            post.setIsNew(0);
                                        }
                                    } else if (Integer.parseInt(response.body().data.get(j).commentcount) < post.getCommentcount()) {
                                        post.setCommentcount(Integer.parseInt(response.body().data.get(j).commentcount));
                                    }
                                }
                                if (response.body().data.get(j).last_updated != null) {
                                    if (response.body().data.get(j).last_updated != post.getTimestamp()) {
                                        post.setLast_updated(response.body().data.get(j).last_updated);
                                    }
                                }

                            }

                            realm.commitTransaction();

                        }

                        // reload posts and table
                        RealmQuery<CDPost> query = realm.where(CDPost.class);

                        query.equalTo("isOwn", isOwn);

                        // Execute the query:
                        RealmResults<CDPost> result = query.findAll();

                        result.sort("last_updated", RealmResults.SORT_ORDER_DESCENDING);

                        listViewPosts.clear();

                        for (int l=0;l<result.size();l++) {
                            listViewPosts.add(result.get(l));
                        }

                        adapter.notifyDataSetChanged();

                        if (isOwn == 1) {
                            ownPostsSelected();
                        } else {
                            interestsSelected();
                        }

                    } else if (response.code() == 401) {
                        // reauth
                        ViloApiAdapter viloAdapter = ViloApiAdapter.getInstance(context);

                        ViloApiEndpointInterface apiService = viloAdapter.mApi;

                        String locale = context.getResources().getConfiguration().locale.getCountry();
                        FbUserAuth userAuth = new FbUserAuth();
                        userAuth.interests_push = 0;
                        userAuth.own_push = 1;
                        userAuth.origin = locale;
                        userAuth.token = AccessToken.getCurrentAccessToken().getToken();

                        Call<User> call = apiService.saveUser(userAuth);

                        call.enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Response<User> response, Retrofit retrofit) {
                                updatePosts(pushPosts, isOwn);
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                Log.d(TAG, "error: " + t.getMessage());
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.d(TAG, "error: " + t.getMessage());
                }
            });

        } else {
            // alert no internet
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.d(TAG, "menu item selected: "+item.getItemId());

        switch (item.getItemId()) {
            case 0:

                /*FragmentManager mFragmentManager = getFragmentManager();
                FragmentTransaction mFragmentTransaction = mFragmentManager
                        .beginTransaction();
                Settings mPrefsFragment = new Settings();
                mFragmentTransaction.replace(android.R.id.content, mPrefsFragment);
                mFragmentTransaction.commit();*/

                ((Tabbar) getActivity()).findViewById(R.id.tab_layout).setVisibility(View.GONE);
                ((Tabbar) getActivity()).findViewById(R.id.fab).setVisibility(View.GONE);

                FragmentManager manager = ((Tabbar)getActivity()).getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction(); //getParentFragment().getFragmentManager().beginTransaction();// manager.beginTransaction();
                transaction.addToBackStack(null);
                transaction.hide(Me.this);
                transaction.replace(R.id.main_layout, new Settings()); // newInstance() is a static factory method.
                transaction.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
