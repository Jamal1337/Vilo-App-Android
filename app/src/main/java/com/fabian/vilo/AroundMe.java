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

import com.fabian.vilo.cards.Posts;
import com.fabian.vilo.models.CDModels.CDUser;
import com.skyfishjy.library.RippleBackground;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

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

    private ArrayList<Card> al;
    private CardAdapter arrayAdapter;
    private int i;
    private RippleBackground rippleBackground;

    private double current_lattitude;
    private double current_longitude;

    private int cardCount;

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

        ((ImageButton)rootView.findViewById(R.id.centerImage)).setImageBitmap(MLRoundedImageView.getCroppedBitmap(((BitmapDrawable) ((ImageButton) rootView.findViewById(R.id.centerImage)).getDrawable()).getBitmap(), 200));

        rippleBackground=(RippleBackground) rootView.findViewById(R.id.content);
        ImageView imageView=(ImageView) rootView.findViewById(R.id.centerImage);
        rippleBackground.startRippleAnimation();

        context = getActivity();

        flingContainer = (SwipeFlingAdapterView) rootView.findViewById(R.id.swipeCards);

        /*imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rippleBackground.startRippleAnimation();
            }
        });*/

        sharedpreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

        GPSTracker gps = new GPSTracker(context);
        int status = 0;
        if(gps.canGetLocation())

        {
            /*status = GooglePlayServicesUtil
                    .isGooglePlayServicesAvailable(context);

            if (status == ConnectionResult.SUCCESS) {*/
            current_lattitude = gps.getLatitude();
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

        } else {
            gps.showSettingsAlert();
        }

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
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Around Me");
        if(sharedpreferences.contains("loggedin")){
            Log.d(TAG, "logged in pref is set");
            Log.d(TAG, "loggin status: "+sharedpreferences.getBoolean("loggedin", true));

            Realm realm = Realm.getInstance(context);
            // Build the query looking at all users:
            RealmQuery<CDUser> query = realm.where(CDUser.class);

            // Execute the query:
            RealmResults<CDUser> result = query.findAll();

            Log.d(TAG, "userinfo: "+result.first().getFirst_name());

            fetchPosts(rootView);


        } else {
            Log.d(TAG, "logged in pref is not set");
            Intent intent = new Intent(getActivity(), Login.class);
            startActivity(intent);
        }
    }

    private boolean fetchPosts(View view) {

        /*Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();*/

        ViloApiAdapter viloAdapter = ViloApiAdapter.getInstance(context);

        ViloApiEndpointInterface apiService = viloAdapter.mApi;

        Call<Posts> call = apiService.getPosts(sharedpreferences.getString("lat", "0"), sharedpreferences.getString("lng", "0"), sharedpreferences.getInt("radius", 10000), "mi");

        al = new ArrayList<Card>();

        Log.d(TAG, "result: " + call);
        call.enqueue(new Callback<Posts>() {
            @Override
            public void onResponse(Response<Posts> response, Retrofit retrofit) {
                int statusCode = response.code();
                Log.d(TAG, "response raw: " + response.raw());
                Log.d(TAG, "response statusCode: " + statusCode);
                Log.d(TAG, "response: " + response.body().data);

                for (int i = 0; i < response.body().data.size(); i++) {
                    Log.d(TAG, "value: " + response.body().data.get(i).id);
                    al.add(new Card("bla", "blub"));

                    cardCount = response.body().data.size();

                    if (i == response.body().data.size() - 1) {

                        getActivity().setTitle("Around Me ("+cardCount+" Posts)");
                        createCards();
                    }
                }
                //createCards();

                Log.d(TAG, "first loop");

            }

            @Override
            public void onFailure(Throwable t) {
                Log.d(TAG, "error: " + t.getMessage());
            }
        });

        Log.d(TAG, "second loop");

        //al.add(new Card("bla", "blub"));
        //createCards();

        /*al = new ArrayList<String>();
        al.add("php");
        al.add("c");
        al.add("python");
        al.add("java");*/

        //choose your favorite adapter
       // arrayAdapter = new ArrayAdapter<String>(context,R.layout.item,R.id.helloText,al);

        //ArrayList<Card> ab = new ArrayList<Card>();
        //ab.add(new Card("bla", "blub"));

        // Construct the data source


// Create the adapter to convert the array to views
        //UsersAdapter adapter = new UsersAdapter(this, arrayOfUsers);
// Attach the adapter to a ListView
        //ListView listView = (ListView) findViewById(R.id.lvItems);
        //listView.setAdapter(adapter);

        //arrayAdapter = new CardAdapter(context, R.layout.item, arrayOfUsers);



        return true;
    }
        //new Loaditems().execute();

    public void createCards() {
        arrayAdapter = new CardAdapter(context, R.layout.item, al) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                // make a nice stack of cards
                v.setTranslationY(position*10);
                return v;
            }
        };

        //setListAdapter(new PersonAdapter(MyActivity.this, R.layout.list_text, personList));

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

                if (cardCount == 0) {
                    getActivity().setTitle("Around Me");
                } else {
                    getActivity().setTitle("Around Me (" + cardCount + " Posts)");
                }
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Log.d(TAG, "Right!");

                cardCount = cardCount - 1;

                if (cardCount == 0) {
                    getActivity().setTitle("Around Me");
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

        // handle item selection
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

                        flingContainer.removeAllViewsInLayout();
                        rippleBackground.startRippleAnimation();

                        fetchPosts(rootView);
                    }
                });

                dialog.show();


                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
