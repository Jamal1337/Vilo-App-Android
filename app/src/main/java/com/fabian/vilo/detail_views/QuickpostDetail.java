package com.fabian.vilo.detail_views;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.fabian.vilo.R;
import com.fabian.vilo.Tabbar;
import com.fabian.vilo.adapters.DetailCorePostAdapter;
import com.fabian.vilo.adapters.DetailPostAdapter;
import com.fabian.vilo.api.ViloApiAdapter;
import com.fabian.vilo.api.ViloApiEndpointInterface;
import com.fabian.vilo.around_me_screen.AroundMe;
import com.fabian.vilo.models.CDModels.CDComment;
import com.fabian.vilo.models.Card;
import com.fabian.vilo.custom_methods.GPSTracker;
import com.fabian.vilo.custom_methods.Util;
import com.fabian.vilo.models.CDModels.CDPost;
import com.fabian.vilo.models.CDModels.CDUser;
import com.fabian.vilo.models.CDModels.ModelManager;
import com.fabian.vilo.models.Comment;
import com.fabian.vilo.models.NewQuickComments;
import com.fabian.vilo.models.QuickComment;
import com.fabian.vilo.models.ViloResponse;
import com.fabian.vilo.models.ViloUploadResponse;
import com.fabian.vilo.models.WebPostInterface;
import com.fabian.vilo.upload_views.Quickpost;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Fabian on 25/10/15.
 */
public class QuickpostDetail extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private AroundMe parent;

    private String title;
    private Card card;
    private CDPost post;
    private ListView myListView;
    private DetailCorePostAdapter adapter;
    private DetailPostAdapter plainAdapter;

    private Uri outputFileUri;
    private static final int YOUR_SELECT_PICTURE_REQUEST_CODE = 232;

    private Button sendBtn;
    private RelativeLayout showPhoto;
    private ImageView chosenPhoto;
    private EditText commentText;
    private Boolean photoSelected = false;
    private ModelManager modelManager = new ModelManager();
    private GPSTracker gps;
    private double current_lattitude;
    private double current_longitude;
    SharedPreferences sharedpreferences;
    Uri selectedImageUri;

    private SwipeRefreshLayout swipeRefreshLayout;

    ArrayList<RealmObject> coreListViewPosts = new ArrayList<RealmObject>();
    ArrayList<WebPostInterface> listViewPosts = new ArrayList<WebPostInterface>();

    String username;
    String date;
    int userid;
    private int postid;

    private boolean showImagePicker = false;

    private boolean postIsSaved = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.quickpost_detail, container,
                false);

        sendBtn = (Button) view.findViewById(R.id.send_btn);
        final ImageButton choosePhoto = (ImageButton) view.findViewById(R.id.choosePhoto);
        showPhoto = (RelativeLayout) view.findViewById(R.id.showPhoto);
        chosenPhoto = (ImageView) view.findViewById(R.id.chosenPhoto);
        commentText = (EditText) view.findViewById(R.id.quickCommentTextField);
        sendBtn.setEnabled(false);

        sharedpreferences = getContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        gps = new GPSTracker(getContext());

        if (card != null) {

            // post infos from around me screen => Web Data

            getActivity().setTitle(card.title);

            postid = card.postid;
            myListView = (ListView) view.findViewById(R.id.detailistView);

            swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
            swipeRefreshLayout.setEnabled(false);

            // download comments and add to array if commentcount > 0

            String postType = card.type == 0 ? "quick" : "event";
            loadComments(postType);
        } else {

            // post infos from Me screen => Realm Data

            getActivity().setTitle(post.getTitle());

            postid = post.getId();
            myListView = (ListView) view.findViewById(R.id.detailistView);

            swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
            swipeRefreshLayout.setOnRefreshListener(this);

            myListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    int topRowVerticalPosition =
                            (myListView == null || myListView.getChildCount() == 0) ?
                                    0 : myListView.getChildAt(0).getTop();
                    swipeRefreshLayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
                }
            });

            coreListViewPosts = loadCommentsFromRealm();

            adapter = new DetailCorePostAdapter(getContext(), R.layout.quickpost_detail, coreListViewPosts, QuickpostDetail.this);
            myListView.setAdapter(adapter);

            scrollMyListViewToBottom();

            if (post.getIsNew() == 1 || coreListViewPosts.size()-1 < post.getCommentcount()) {
                if (post.getType() == 0) {
                    fetchNewQuickpostComments();
                } else if (post.getType() == 1) {
                    fetchNewEventpostComments();
                }
            }

        }

        commentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0) {
                    sendBtn.setEnabled(true);
                } else {
                    sendBtn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        choosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePicker = true;
                openImageIntent();
            }
        });

        chosenPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoSelected = false;
                showPhoto.setVisibility(View.GONE);
                chosenPhoto.setImageURI(null);
                sendBtn.setEnabled(false);
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gps.canGetLocation()) {
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    current_lattitude = gps.getLatitude();
                    current_longitude = gps.getLongitude();

                    editor.putString("lat", "" + current_lattitude);
                    editor.putString("lng", "" + current_longitude);
                    editor.commit();
                    ViloApiAdapter viloAdapter = ViloApiAdapter.getInstance(getContext());

                    ViloApiEndpointInterface apiService = viloAdapter.mApi;

                    final QuickComment quickComment = new QuickComment();
                    quickComment.withPhoto = (photoSelected == true) ? 1 : 0;
                    quickComment.value = commentText.getText().toString();
                    quickComment.lat = current_lattitude;
                    quickComment.lng = current_longitude;

                    Realm realm = Realm.getInstance(getContext());

                    // Build the query looking at all users:
                    RealmQuery<CDUser> query = realm.where(CDUser.class);

                    // Execute the query:
                    RealmResults<CDUser> result = query.findAll();

                    username = result.first().getFirst_name();
                    userid = result.first().getUserid();
                    date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

                    if (photoSelected == true) {

                        //File file = new File(selectedImageUri);
                        File file = new File(new Util().getRealPathFromURI(getContext(), selectedImageUri));

                        File reducedFile = new Util().saveBitmapToFile(file, getContext());

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("multipart/form-data"), reducedFile);

                        Call<ViloUploadResponse> call = apiService.commentQuickWithImage(postid, quickComment.withPhoto, quickComment.value, quickComment.lat, quickComment.lng, requestBody);

                        call.enqueue(new Callback<ViloUploadResponse>() {
                            @Override
                            public void onResponse(Response<ViloUploadResponse> response, Retrofit retrofit) {

                                photoSelected = false;
                                showPhoto.setVisibility(View.GONE);
                                chosenPhoto.setImageURI(null);
                                commentText.setText("");
                                sendBtn.setEnabled(false);
                                Comment newComment = new Comment(response.body().result.id, response.body().result.attachment, (float) quickComment.lat, (float) quickComment.lng, "", postid, date, 0, userid, username, quickComment.value, 0);

                                if (card != null) {
                                    listViewPosts.add(newComment);
                                    // wenn nicht in realm

                                    if (new Util().isNetworkAvailable(getContext()) && !postIsSaved) {

                                        ViloApiAdapter viloAdapter = ViloApiAdapter.getInstance(getContext());

                                        ViloApiEndpointInterface apiService = viloAdapter.mApi;

                                        Map<String, ArrayList> pushPosts = new HashMap<String, ArrayList>();
                                        ArrayList swipedPosts = new ArrayList();
                                        Map<String, Integer> postArray = new HashMap<String, Integer>();
                                        postArray.put("postid", postid);
                                        postArray.put("status", 1);
                                        swipedPosts.add(postArray);
                                        pushPosts.put("data", swipedPosts);

                                        Call<ViloResponse> call = apiService.pushSwipedPosts(pushPosts);

                                        call.enqueue(new Callback<ViloResponse>() {
                                            @Override
                                            public void onResponse(Response<ViloResponse> response, Retrofit retrofit) {

                                                if (response.code() == 200) {
                                                    // save post & comments in realm
                                                    if (card.type == 0) {
                                                        if (modelManager.storeQuickpostAfterCommentInRealm(listViewPosts, getContext())) {
                                                            parent.setShouldLoadCards(true);
                                                            postIsSaved = true;
                                                        }
                                                    } else if (card.type == 1) {
                                                        if (modelManager.storeEventpostAfterCommentInRealm(listViewPosts, getContext())) {
                                                            parent.setShouldLoadCards(true);
                                                            postIsSaved = true;
                                                        }
                                                    }

                                                } else {
                                                    // error handling
                                                }
                                            }

                                            @Override
                                            public void onFailure(Throwable t) {
                                                Log.d("bla", "error: " + t.getMessage());
                                            }
                                        });

                                    } else if (postIsSaved) {
                                        CDComment savedComment = modelManager.saveNewQuickPostComment(newComment, getContext());
                                        coreListViewPosts.add(savedComment);
                                    } else {
                                        // alert no internet
                                    }

                                    // refresh listview
                                    plainAdapter.notifyDataSetChanged();
                                } else {
                                    CDComment savedComment = modelManager.saveNewQuickPostComment(newComment, getContext());
                                    coreListViewPosts.add(savedComment);
                                    // refresh listview
                                    adapter.notifyDataSetChanged();
                                }
                                scrollMyListViewToBottom();
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                Log.d("upload", "error: " + t.getMessage());
                            }
                        });
                    } else {
                        Call<ViloUploadResponse> call = apiService.commentQuickWithoutImage(postid, quickComment);

                        call.enqueue(new Callback<ViloUploadResponse>() {
                            @Override
                            public void onResponse(Response<ViloUploadResponse> response, Retrofit retrofit) {
                                Log.d("upload", "posted without image!");
                                Log.d("upload", "msg: " + response.body());
                                commentText.setText("");
                                sendBtn.setEnabled(false);
                                Comment newComment = new Comment(response.body().result.id, "", (float) quickComment.lat, (float) quickComment.lng, "", postid, date, 0, userid, username, quickComment.value, 0);

                                if (card != null) {
                                    listViewPosts.add(newComment);
                                    // wenn nicht in realm

                                    if (new Util().isNetworkAvailable(getContext()) && !postIsSaved) {

                                        ViloApiAdapter viloAdapter = ViloApiAdapter.getInstance(getContext());

                                        ViloApiEndpointInterface apiService = viloAdapter.mApi;

                                        Map<String, ArrayList> pushPosts = new HashMap<String, ArrayList>();
                                        ArrayList swipedPosts = new ArrayList();
                                        Map<String, Integer> postArray = new HashMap<String, Integer>();
                                        postArray.put("postid", postid);
                                        postArray.put("status", 1);
                                        swipedPosts.add(postArray);
                                        pushPosts.put("data", swipedPosts);

                                        Call<ViloResponse> call = apiService.pushSwipedPosts(pushPosts);

                                        call.enqueue(new Callback<ViloResponse>() {
                                            @Override
                                            public void onResponse(Response<ViloResponse> response, Retrofit retrofit) {

                                                if (response.code() == 200) {
                                                    // save post & comments in realm
                                                    if (card.type == 0) {
                                                        if (modelManager.storeQuickpostAfterCommentInRealm(listViewPosts, getContext())) {
                                                            parent.setShouldLoadCards(true);
                                                            postIsSaved = true;
                                                        }
                                                    } else if (card.type == 1) {
                                                        if (modelManager.storeEventpostAfterCommentInRealm(listViewPosts, getContext())) {
                                                            parent.setShouldLoadCards(true);
                                                            postIsSaved = true;
                                                        }
                                                    }

                                                } else {
                                                    // error handling
                                                }
                                            }

                                            @Override
                                            public void onFailure(Throwable t) {
                                                Log.d("bla", "error: " + t.getMessage());
                                            }
                                        });

                                    } else if (postIsSaved) {
                                        CDComment savedComment = modelManager.saveNewQuickPostComment(newComment, getContext());
                                        coreListViewPosts.add(savedComment);
                                    } else {
                                        // alert no internet
                                    }

                                    // refresh listview
                                    plainAdapter.notifyDataSetChanged();
                                } else {
                                    CDComment savedComment = modelManager.saveNewQuickPostComment(newComment, getContext());
                                    coreListViewPosts.add(savedComment);
                                    // refresh listview
                                    adapter.notifyDataSetChanged();
                                }
                                scrollMyListViewToBottom();
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                Log.d("upload", "error: " + t.getMessage());
                            }
                        });
                    }
                }
            }
        });

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (showImagePicker == false) {

            getActivity().setTitle(title);

            ((Tabbar) getActivity()).findViewById(R.id.tab_layout).setVisibility(View.VISIBLE);
            ((Tabbar) getActivity()).findViewById(R.id.fab).setVisibility(View.VISIBLE);

            FragmentManager manager = ((Tabbar) getActivity()).getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.remove(this).commit();
        }

    }

    /**
     * Getter
     */

    public String getTitle() {
        return title;
    }

    public Card getCard() {
        return card;
    }

    public CDPost getPost() {
        return post;
    }

    public AroundMe getParent() {
        return parent;
    }

    /**
     * Setter
     */

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public void setPost(CDPost post) {
        this.post = post;
    }

    public void setParent(AroundMe parent) {
        this.parent = parent;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        //menu.findItem(0).setVisible(false).setEnabled(false);
    }

    private void openImageIntent() {

        // Determine Uri of camera image to save.
        final File root = new File(Environment.getExternalStorageDirectory() + File.separator + "MyDir" + File.separator);
        root.mkdirs();
        final String fname = "file";
        final File sdImageMainDirectory = new File(root, fname);
        outputFileUri = Uri.fromFile(sdImageMainDirectory);

        // Camera.
        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getActivity().getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for(ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            cameraIntents.add(intent);
        }

        // Filesystem.
        final Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

        // Chooser of filesystem options.
        final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Source");

        // Add the camera options.
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size()]));

        startActivityForResult(chooserIntent, YOUR_SELECT_PICTURE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == YOUR_SELECT_PICTURE_REQUEST_CODE) {
                final boolean isCamera;
                if (data == null) {
                    isCamera = true;
                } else {
                    final String action = data.getAction();
                    if (action == null) {
                        isCamera = false;
                    } else {
                        isCamera = action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    }
                }

                if (isCamera) {
                    selectedImageUri = outputFileUri;
                } else {
                    selectedImageUri = data == null ? null : data.getData();
                }

                photoSelected = true;
                showPhoto.setVisibility(View.VISIBLE);
                sendBtn.setEnabled(true);
                Picasso.with(getContext())
                        .load(selectedImageUri)
                        .centerCrop()
                        .fit()
                        .into(chosenPhoto);

                showImagePicker = false;
                chosenPhoto.setClickable(true);
            }
        }
    }

    public void showFullscreenImage(String imgURL) {
        ImageFullscreen imageFullscreen = new ImageFullscreen();
        imageFullscreen.setImgURL(imgURL);
        Fragment fragment = imageFullscreen;

        ((Tabbar)getActivity()).getSupportActionBar().hide();
        FragmentManager manager = ((Tabbar)getActivity()).getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction(); //getParentFragment().getFragmentManager().beginTransaction();// manager.beginTransaction();
        transaction.addToBackStack(null);
        //transaction.hide(this);
        transaction.replace(R.id.main_layout, fragment); // newInstance() is a static factory method.
        transaction.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();
    }

    public void fetchNewQuickpostComments() {

        Integer lastCommentID = 0;
        Integer secondLastCommentID = 0;

        List<Integer> lastComment = modelManager.getLastCommentID(postid, getContext());

        if (modelManager.getCommentcount(postid, getContext()) > 0) {
            lastCommentID = lastComment.get(0);
            if (lastComment.get(2) == userid) {
                secondLastCommentID = lastComment.get(1);
            }
        }

        ViloApiAdapter viloAdapter = ViloApiAdapter.getInstance(getContext());

        final ViloApiEndpointInterface apiService = viloAdapter.mApi;

        Call<NewQuickComments> call = apiService.fetchNewQuickpostComments(postid, secondLastCommentID, lastCommentID);

        call.enqueue(new Callback<NewQuickComments>() {
            @Override
            public void onResponse(Response<NewQuickComments> response, Retrofit retrofit) {
                if (response.code() == 200) {
                    if (modelManager.storeQuickPostCommentsInRealm(response.body(), postid, getContext())) {
                        if (modelManager.setPostRead(postid, getContext())) {
                            loadCommentsFromRealm();
                            swipeRefreshLayout.setRefreshing(false);
                            adapter.notifyDataSetChanged();
                            scrollMyListViewToBottom();
                        }
                    } else {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
        }

        public void fetchNewEventpostComments() {

        Integer lastCommentID = 0;
        Integer secondLastCommentID = 0;

        List<Integer> lastComment = modelManager.getLastCommentID(postid, getContext());

            if (modelManager.getCommentcount(postid, getContext()) > 0) {
                lastCommentID = lastComment.get(0);
                if (lastComment.get(2) == userid) {
                    secondLastCommentID = lastComment.get(1);
                }
            }

            ViloApiAdapter viloAdapter = ViloApiAdapter.getInstance(getContext());

            final ViloApiEndpointInterface apiService = viloAdapter.mApi;

            Call<NewQuickComments> call = apiService.fetchNewEventpostComments(postid, secondLastCommentID, lastCommentID);

            call.enqueue(new Callback<NewQuickComments>() {
                @Override
                public void onResponse(Response<NewQuickComments> response, Retrofit retrofit) {
                if (response.code() == 200) {
                    if (modelManager.storeEventPostCommentsInRealm(response.body(), postid, getContext())) {
                        if (modelManager.setPostRead(postid, getContext())) {
                            loadCommentsFromRealm();
                            swipeRefreshLayout.setRefreshing(false);
                            adapter.notifyDataSetChanged();
                            scrollMyListViewToBottom();
                        }
                    } else {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    public ArrayList<RealmObject> loadCommentsFromRealm() {
        coreListViewPosts.clear();
        coreListViewPosts.add(post);

        // fetch core comments
        Realm realm = Realm.getInstance(getContext());

        // Fetch core Post
        RealmQuery<CDComment> query = realm.where(CDComment.class);
        query.equalTo("postid", postid);
        RealmResults<CDComment> result = query.findAll();
        result.sort("timestamp");

        for (int i = 0; i < result.size(); i++) {
            coreListViewPosts.add(result.get(i));
            Log.d("bla", result.get(i).getValue());
        }

        Log.d("bla", "number of comments: " + coreListViewPosts.size());

        return coreListViewPosts;
    }

    public void loadComments(String type) {
        listViewPosts.clear();
        listViewPosts.add(card);

        if (card.commentCount > 0) {

            ViloApiAdapter viloAdapter = ViloApiAdapter.getInstance(getContext());

            final ViloApiEndpointInterface apiService = viloAdapter.mApi;

            Call<NewQuickComments> call = apiService.loadCommentsOfPost(type, postid);

            call.enqueue(new Callback<NewQuickComments>() {
                @Override
                public void onResponse(Response<NewQuickComments> response, Retrofit retrofit) {
                    if (response.code() == 200) {
                        for (int i = 0; i < response.body().data.size(); i++) {
                            Comment newComment = new Comment(response.body().data.get(i).commentid, response.body().data.get(i).attachment, (float) response.body().data.get(i).lat, (float) response.body().data.get(i).lng, response.body().data.get(i).photo, postid, response.body().data.get(i).timestamp, 0, response.body().data.get(i).userid, response.body().data.get(i).username, response.body().data.get(i).text, 0);
                            listViewPosts.add(newComment);
                        }

                        plainAdapter = new DetailPostAdapter(getContext(), R.layout.quickpost_detail, listViewPosts, QuickpostDetail.this);
                        myListView.setAdapter(plainAdapter);
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.d("bla", "error: "+t.getMessage());
                }
            });

        } else {
            plainAdapter = new DetailPostAdapter(getContext(), R.layout.quickpost_detail, listViewPosts, QuickpostDetail.this);
            myListView.setAdapter(plainAdapter);
        }

    }

    /**
     * This method is called when swipe refresh is pulled down
     */
    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);

        // Do refresh from server
        if (post.getType() == 0) {
            fetchNewQuickpostComments();
        } else if (post.getType() == 1) {
            fetchNewEventpostComments();
        }
    }

    private void scrollMyListViewToBottom() {
        myListView.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                if (card != null) {
                    myListView.setSelection(plainAdapter.getCount() - 1);
                } else {
                    myListView.setSelection(adapter.getCount() - 1);
                }
            }
        });
    }

    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.add("Menu 2").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        inflater.inflate(R.menu.menu_tabbar, menu);
    }*/
}