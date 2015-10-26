package com.fabian.vilo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.fabian.vilo.cards.EventpostCard;
import com.fabian.vilo.cards.PostLocation;
import com.fabian.vilo.cards.Posts;
import com.fabian.vilo.cards.QuickpostCard;
import com.fabian.vilo.models.CDModels.CDUser;
import com.fabian.vilo.models.CDModels.ModelManager;
import com.fabian.vilo.models.FbUserAuth;
import com.fabian.vilo.models.User;
import com.fabian.vilo.models.ViloResponse;
import com.facebook.AccessToken;
import com.google.gson.Gson;
import com.skyfishjy.library.RippleBackground;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.squareup.picasso.Picasso;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.util.Log;
import android.content.Context;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.w3c.dom.Text;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class AroundMe extends Fragment {

    private static final String TAG = AroundMe.class.getSimpleName();

    public static final String BASE_URL = "https://api.viloapp.com/v1/";

    private Context context;
    private ViloApiAdapter viloAdapter;
    private ViloApiEndpointInterface apiService;

    private ArrayList<Card> al;
    private CardAdapter arrayAdapter;
    private int cardCounter = 0;
    private int i;
    private RippleBackground rippleBackground;

    private TextView searchLabel;

    private GPSTracker gps;

    private double current_lattitude;
    private double current_longitude;

    private int cardCount;
    private int totalPosts = 0;

    //private ArrayList<Map<String, Integer>> swipedPosts;
    private ArrayList swipedPosts = new ArrayList();

    ModelManager modelManager;

    SharedPreferences sharedpreferences;

    //private ArrayList<Productp> al;
    //private ArrayAdapter<Productp> arrayAdapter;

    SwipeFlingAdapterView flingContainer;
    View rootView;

    JSONArray products = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        //boolean cbValue = sp.getBoolean("CHECKBOX", false);



        //SharedPreferences preferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        //SharedPreferences.Editor editor = preferences.edit();
        //editor.putInt("var1", myvar);
        //editor.commit();

        //Log.d(TAG, "loggin status: "+preferences.getInt("var1", 0));

        rootView = inflater.inflate(R.layout.activity_around_me, container, false);

        //locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,AroundMe.this);
        //locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 1, this);

        //((ImageButton)rootView.findViewById(R.id.centerImage)).setImageBitmap(MLRoundedImageView.getCroppedBitmap(((BitmapDrawable) ((ImageButton) rootView.findViewById(R.id.centerImage)).getDrawable()).getBitmap(), 200));

        context = getActivity();

        modelManager = new ModelManager();

        viloAdapter = ViloApiAdapter.getInstance(context);

        apiService = viloAdapter.mApi;

        ImageView profileImage = (ImageView) rootView.findViewById(R.id.centerImage);
        Picasso.with(getContext()).load("https://vilostorage.s3.amazonaws.com/profilepics/user_41444676563.png").into(profileImage);

        profileImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // do stuff
                clearCards();
            }

        });

        rippleBackground=(RippleBackground) rootView.findViewById(R.id.content);
        ImageView imageView = (ImageView) rootView.findViewById(R.id.centerImage);
        searchLabel = (TextView) rootView.findViewById(R.id.searchingText);
        rippleBackground.startRippleAnimation();



        flingContainer = (SwipeFlingAdapterView) rootView.findViewById(R.id.swipeCards);

        /*imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rippleBackground.startRippleAnimation();
            }
        });*/

        /*AccessToken token = AccessToken.getCurrentAccessToken();
        Log.d(TAG, "current access token: "+token.getToken());*/

        sharedpreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

        gps = new GPSTracker(context);

        /*if(gps.canGetLocation())

        {
            /*status = GooglePlayServicesUtil
                    .isGooglePlayServicesAvailable(context);

            if (status == ConnectionResult.SUCCESS) {*/
            /*current_lattitude = gps.getLatitude();
            current_longitude = gps.getLongitude();
            Log.d("dashlatlongon", "" + current_lattitude + "-"
                        + current_longitude);

            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("lat", ""+current_lattitude);
            editor.putString("lng", "" + current_longitude);
            editor.commit();


            /*} else {
                current_lattitude = 22.22;
                current_longitude = 22.22;
            }*/

        /*} else {
            gps.showSettingsAlert();
        }*/

        setHasOptionsMenu(true);

        /*Realm realm = Realm.getInstance(context);
        // Build the query looking at all users:
        RealmQuery<CDUser> query = realm.where(CDUser.class);

        // Execute the query:
        RealmResults<CDUser> result = query.findAll();

        Log.d(TAG, "userinfo: "+result.first().getFirst_name());*/


        //SharedPreferences.Editor ed;
        /*if(sharedpreferences.contains("loggedin")){
            Log.d(TAG, "logged in pref is set");
            Log.d(TAG, "loggin status: "+sharedpreferences.getBoolean("loggedin", true));

            Realm realm = Realm.getInstance(context);
            // Build the query looking at all users:
            RealmQuery<CDUser> query = realm.where(CDUser.class);

            // Execute the query:
            RealmResults<CDUser> result = query.findAll();

            Log.d(TAG, "userinfo: "+result.first().getFirst_name());

            if (fetchPosts(rootView) == true) {
                //rippleBackground.stopRippleAnimation();
            }

        } else {
            Log.d(TAG, "logged in pref is not set");
            Intent intent = new Intent(getActivity(), Login.class);
            startActivity(intent);
        }*/

        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isResumed()) {
            if (isVisibleToUser && sharedpreferences.contains("loggedin") && isResumed()) {
                Log.d(TAG, "AROUND ME SCREEN APPEARED");
                clearCards();
            } else {
                Log.d(TAG, "AROUND ME SCREEN DISAPPEARED");
                if (al.size() > 0) {
                    al.clear();
                    arrayAdapter.notifyDataSetChanged();
                    pushSwipedPosts();
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Around Me");
        if(sharedpreferences.contains("loggedin")){
            Log.d(TAG, "logged in pref is set");
            Log.d(TAG, "loggin status: " + sharedpreferences.getBoolean("loggedin", true));

            Realm realm = Realm.getInstance(context);
            // Build the query looking at all users:
            RealmQuery<CDUser> query = realm.where(CDUser.class);

            // Execute the query:
            RealmResults<CDUser> result = query.findAll();

            Log.d(TAG, "userinfo: " + result.first().getFirst_name());

            fetchPosts(rootView);


        } else {
            Log.d(TAG, "logged in pref is not set");
            Intent intent = new Intent(getActivity(), Login.class);
            startActivity(intent);
        }
    }

    private void fetchPosts(View view) {

        // TODO: bei häufigem tab switchen merkt der sich dass er die posts laden muss und zeigt dann alle doppelt und dreifach an
        searchLabel.setText(R.string.searchLabel);
        searchLabel.setVisibility(View.VISIBLE);

        if(gps.canGetLocation())

        {

            SharedPreferences.Editor editor = sharedpreferences.edit();
            current_lattitude = gps.getLatitude();
            current_longitude = gps.getLongitude();

            editor.putString("lat", "" + current_lattitude);
            editor.putString("lng", "" + current_longitude);
            editor.commit();

            Call<Posts> call = apiService.getPosts(sharedpreferences.getString("lat", "0"), sharedpreferences.getString("lng", "0"), sharedpreferences.getInt("radius", 10000), "mi");

            al = new ArrayList<Card>();

            Log.d(TAG, "result: " + call);
            call.enqueue(new Callback<Posts>() {
                @Override
                public void onResponse(Response<Posts> response, Retrofit retrofit) {
                    int statusCode = response.code();

                    if (statusCode == 200) {

                        if (response.body().data.size() > 0) {
                            totalPosts = response.body().data.size();
                            for (int j = 0; j < totalPosts; j++) {

                                CardManager cardManager = new CardManager(context);

                                // TODO: Reihenfolge bleibt beim Karten erstellen nicht erhalten, da die Results unterschiedlich zurück kommen
                                // TODO: Anzeigen das nix gefunden wurde wenn nur PhotoAlbum/Poll/Meetup zurückkommt

                                switch (response.body().data.get(j).type) {
                                    case 0:

                                        Call<QuickpostCard> call = apiService.getQuickPost(response.body().data.get(j).id);

                                        call.enqueue(new Callback<QuickpostCard>() {
                                            @Override
                                            public void onResponse(Response<QuickpostCard> response, Retrofit retrofit) {

                                                cardCounter++;
                                                cardCount++;

                                                QuickpostCard qpCard = response.body();

                                                Location location_a = new Location("locA");
                                                Location location_b = new Location("locB");

                                                location_a.setLatitude(qpCard.lat);
                                                location_a.setLongitude(qpCard.lng);
                                                location_b.setLatitude(current_lattitude);
                                                location_b.setLongitude(current_longitude);

                                                al.add(new Card(qpCard.title, qpCard.text, qpCard.id, qpCard.type, 0, qpCard.commentcount, qpCard.followers, new Util().calculateElapsedTime(qpCard.timestamp), new Util().distance2String(location_a, location_b, "km"), qpCard.username, qpCard.attachment, qpCard.timestamp,
                                                        qpCard.userid, qpCard.photo, qpCard.lat, qpCard.lng, qpCard.radius, null, 0, qpCard.last_updated, 0, qpCard.topTip, new ArrayList<PostLocation>()));

                                                if (cardCounter == totalPosts) {
                                                    Log.d("bla", "all cards fetched in quick at id: " + qpCard.id);
                                                    createCards();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Throwable t) {
                                                Log.d("", "error: " + t.getMessage());
                                            }
                                        });

                                        break;
                                    case 1:
                                        Call<EventpostCard> callEvent = apiService.getEventPost(response.body().data.get(j).id);

                                        callEvent.enqueue(new Callback<EventpostCard>() {
                                            @Override
                                            public void onResponse(Response<EventpostCard> response, Retrofit retrofit) {
                                                cardCounter++;
                                                cardCount++;

                                                EventpostCard qpCard = response.body();

                                                Location location_a = new Location("locA");
                                                Location location_b = new Location("locB");

                                                location_a.setLatitude(qpCard.lat);
                                                location_a.setLongitude(qpCard.lng);
                                                location_b.setLatitude(current_lattitude);
                                                location_b.setLongitude(current_longitude);

                                                al.add(new Card(qpCard.title, qpCard.text, qpCard.id, qpCard.type, 0, qpCard.commentcount, qpCard.followers, new Util().calculateElapsedTime(qpCard.timestamp), new Util().distance2String(location_a, location_b, "km"), qpCard.username, qpCard.attachment, qpCard.timestamp,
                                                        qpCard.userid, qpCard.photo, qpCard.lat, qpCard.lng, qpCard.radius, qpCard.event_date, 0, qpCard.last_updated, 0, qpCard.topTip, qpCard.location));

                                                if (cardCounter == totalPosts) {
                                                    Log.d("bla", "all cards fetched in quick at id: " + qpCard.id);
                                                    createCards();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Throwable t) {
                                                Log.d("", "error: " + t.getMessage());
                                            }
                                        });

                                        break;
                                    case 2:
                                        cardCounter++;
                                        if (cardCounter == totalPosts) {
                                            Log.d("bla", "all cards fetched in poll");
                                            createCards();
                                        }
                                        break;
                                    case 3:
                                        cardCounter++;
                                        if (cardCounter == totalPosts) {
                                            Log.d("bla", "all cards fetched in album");
                                            createCards();
                                        }
                                        break;
                                    case 4:
                                        cardCounter++;
                                        if (cardCounter == totalPosts) {
                                            Log.d("bla", "all cards fetched in meetup");
                                            createCards();
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            }
                        } else {
                            rippleBackground.stopRippleAnimation();
                            searchLabel.setText(R.string.no_posts_found);
                        }
                    } else {
                        String locale = context.getResources().getConfiguration().locale.getCountry();
                        FbUserAuth userAuth = new FbUserAuth();
                        userAuth.interests_push = 0;
                        userAuth.own_push = 1;
                        userAuth.origin = locale;
                        userAuth.token = AccessToken.getCurrentAccessToken().getToken();

                        reauthUser(userAuth);
                    }


                }

                @Override
                public void onFailure(Throwable t) {
                    Log.d(TAG, "error: " + t.getMessage());
                }
            });

        } else {
            gps.showSettingsAlert();
        }
    }


    public void createCards() {

        /*Set<Card> hs = new HashSet<Card>();
        hs.addAll(al);
        al.clear();
        arrayAdapter.notifyDataSetChanged();
        al.addAll(hs);*/

        Log.d(TAG, "number of cards:" + al.size());

        searchLabel.setVisibility(View.GONE);
        cardCount = al.size();
        getActivity().setTitle("Around Me (" + cardCount + " Posts)");

        arrayAdapter = new CardAdapter(context, R.layout.item_quick, al) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                // make a nice stack of cards
                v.setTranslationY(position*10);
                return v;
            }
        };

        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                al.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                Log.d(TAG, "Left!");
                cardCount = cardCount - 1;

                Card currentCard = (Card) dataObject;

                Map<String, Integer> postArray = new HashMap<String, Integer>();
                postArray.put("postid", currentCard.postid);
                postArray.put("status", 0);
                swipedPosts.add(postArray);

                if (cardCount == 0) {
                    clearCards();
                } else {
                    getActivity().setTitle("Around Me (" + cardCount + " Posts)");
                }
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Log.d(TAG, "Right!");

                cardCount = cardCount - 1;

                Card currentCard = (Card) dataObject;

                Map<String, Integer> postArray = new HashMap<String, Integer>();
                postArray.put("postid", currentCard.postid);
                postArray.put("status", 1);
                swipedPosts.add(postArray);

                if (modelManager.savePost(currentCard, context) == true) {
                    Log.d(TAG, "post saved!");
                }

                if (cardCount == 0) {
                    clearCards();
                } else {
                    getActivity().setTitle("Around Me (" + cardCount + " Posts)");
                }
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here
                //al.add("XML ".concat(String.valueOf(i)));
                Log.d(TAG, "items left: " + al.size());
                arrayAdapter.notifyDataSetChanged();

                if (al.size() == 0) {
                    rippleBackground.startRippleAnimation();
                    //getPosts(rootView);
                }

                Log.d("LIST", "notified");
                i++;
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        });

        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Log.d("LIST", "Clicked!");
            }
        });

        rippleBackground.stopRippleAnimation();

        arrayAdapter.notifyDataSetChanged();

        flingContainer.bringToFront();


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.add("Radius")
                .setIcon(R.drawable.nav_radius)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        inflater.inflate(R.menu.menu_tabbar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // handle item_event selection
        switch (item.getItemId()) {
            case 0:

                final Dialog dialog = new Dialog(context);

                //setting custom layout to dialog
                dialog.setContentView(R.layout.distance_picker);
                dialog.setTitle("Set radius");


                final SeekBar seek = (SeekBar) dialog.findViewById(R.id.seekbar);
                final TextView text = (TextView) dialog.findViewById(R.id.txtRadius);
                final Button cancelBtn = (Button) dialog.findViewById(R.id.cancel_btn);
                final Button okBtn = (Button) dialog.findViewById(R.id.ok_btn);

                int currentRadius = sharedpreferences.getInt("radius", 10000);

                DecimalFormat df = new DecimalFormat("#.#");
                df.setRoundingMode(RoundingMode.CEILING);

                String metric = "km";
                if (UnitLocale.getDefault() == UnitLocale.Imperial) {
                    metric = "mi";
                }

                if (currentRadius >= 1000) {
                    Double newValue = (double)currentRadius/1000;
                    Double d = newValue.doubleValue();
                    text.setText(df.format(d)+metric);
                } else {
                    if (metric == "km") {
                        String newValue = "" + currentRadius;
                        newValue.substring(0, 1);
                        text.setText(newValue.substring(0, 1) + "00m");
                    } else {
                        Double newValue = (double)currentRadius/1000;
                        Double d = newValue.doubleValue();
                        text.setText(df.format(d)+metric);
                    }
                }


                seek.setMax(9500);
                seek.setProgress(sharedpreferences.getInt("radius", 10000));

                seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        //Do something here with new value
                        progress += 500;

                        DecimalFormat df = new DecimalFormat("#.#");
                        df.setRoundingMode(RoundingMode.CEILING);

                        String metric = "km";
                        if (UnitLocale.getDefault() == UnitLocale.Imperial) {
                            metric = "mi";
                        }

                        if (progress >= 1000) {
                            Double newValue = (double)progress/1000;
                            Double d = newValue.doubleValue();
                            text.setText(df.format(d)+metric);
                        } else {
                            if (metric == "km") {
                                String newValue = "" + progress;
                                newValue.substring(0, 1);
                                text.setText(newValue.substring(0, 1) + "00m");
                            } else {
                                Double newValue = (double)progress/1000;
                                Double d = newValue.doubleValue();
                                text.setText(df.format(d)+metric);
                            }
                        }

                    }

                    public void onStartTrackingTouch(SeekBar arg0) {
                        // TODO Auto-generated method stub

                    }

                    public void onStopTrackingTouch(SeekBar seekBar) {
                        // TODO Auto-generated method stub

                    }
                });


                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //dialog.dismiss();
                        Log.d(TAG, "cancel pressed");
                        dialog.dismiss();
                    }
                });

                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putInt("radius", seek.getProgress() + 500);
                        editor.commit();
                        dialog.dismiss();

                        clearCards();
                    }
                });

                dialog.show();


                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void clearCards() {

        if (al.size() > 0) {
            al.clear();
            arrayAdapter.notifyDataSetChanged();
        }

        getActivity().setTitle("Around Me");

        rippleBackground.startRippleAnimation();

        cardCounter = 0;
        cardCount = 0;
        totalPosts = 0;

        pushSwipedPosts();
        //fetchPosts(rootView);
    }

    public void reauthUser(FbUserAuth userAuth) {
        ViloApiAdapter viloAdapter = ViloApiAdapter.getInstance(context);

        ViloApiEndpointInterface apiService = viloAdapter.mApi;

        Call<User> call = apiService.saveUser(userAuth);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Response<User> response, Retrofit retrofit) {
                fetchPosts(rootView);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d(TAG, "error: " + t.getMessage());
            }
        });
    }

    public void pushSwipedPosts() {
        if (swipedPosts.size() > 0) {

            if (new Util().isNetworkAvailable(context) == true) {

                ViloApiAdapter viloAdapter = ViloApiAdapter.getInstance(context);

                ViloApiEndpointInterface apiService = viloAdapter.mApi;

                Map<String, ArrayList> pushPosts = new HashMap<String, ArrayList>();
                pushPosts.put("data", swipedPosts);

                Call<ViloResponse> call = apiService.pushSwipedPosts(pushPosts);

                call.enqueue(new Callback<ViloResponse>() {
                    @Override
                    public void onResponse(Response<ViloResponse> response, Retrofit retrofit) {

                        Log.d(TAG, "statusCode: " + response.code());

                        if (response.code() == 200) {
                            swipedPosts.clear();
                            fetchPosts(rootView);
                        } else {
                            pushSwipedPosts();
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
    }

}
