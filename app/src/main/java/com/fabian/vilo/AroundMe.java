package com.fabian.vilo;

import android.app.ProgressDialog;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.skyfishjy.library.RippleBackground;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import java.util.ArrayList;
import android.widget.ArrayAdapter;
import android.util.Log;
import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.widget.Toast;

public class AroundMe extends Fragment {

    private static final String TAG = AroundMe.class.getSimpleName();

    private Context context;

    private ArrayList<String> al;
    private ArrayAdapter<String> arrayAdapter;
    private int i;

    //private ArrayList<Productp> al;
    //private ArrayAdapter<Productp> arrayAdapter;

    SwipeFlingAdapterView flingContainer;
    View rootView;

    JSONArray products = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        rootView = inflater.inflate(R.layout.activity_around_me, container, false);

        ((ImageButton)rootView.findViewById(R.id.centerImage)).setImageBitmap(MLRoundedImageView.getCroppedBitmap(((BitmapDrawable) ((ImageButton) rootView.findViewById(R.id.centerImage)).getDrawable()).getBitmap(), 200));

        final RippleBackground rippleBackground=(RippleBackground) rootView.findViewById(R.id.content);
        ImageView imageView=(ImageView) rootView.findViewById(R.id.centerImage);
        rippleBackground.startRippleAnimation();

        context = getActivity();

        /*imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rippleBackground.startRippleAnimation();
            }
        });*/

        initUI(rootView);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Around Me");
    }

    private void initUI(View view) {

        flingContainer = (SwipeFlingAdapterView) view.findViewById(R.id.swipeCards);


        al = new ArrayList<String>();
        al.add("php");
        al.add("c");
        al.add("python");
        al.add("java");

        //choose your favorite adapter
        arrayAdapter = new ArrayAdapter<String>(context,R.layout.item,R.id.helloText,al);

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
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Log.d(TAG, "Right!");
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here
                al.add("XML ".concat(String.valueOf(i)));
                arrayAdapter.notifyDataSetChanged();
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

        flingContainer.bringToFront();
    }
        //new Loaditems().execute();




    /*class Loaditems extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Loading products. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("range", "1"));

            JSONObject json = jParser.makeHttpRequest(url_all_products, "GET",
                    params);

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    products = json.getJSONArray(TAG_PRODUCTS);

                    al = new ArrayList<Productp>();

                    for (int i = 0; i < products.length(); i++) {

                        JSONObject c = products.getJSONObject(i);

                        Productp pp = new Productp();

                        pp.portal = c.getString(TAG_IMAGE);

                        al.add(pp);
                    }
                } else {

                }

            } catch (JSONException e) {
                e.printStackTrace();

            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();

            arrayAdapter = new ProductAdpater(context, al);
            flingContainer.setAdapter(arrayAdapter);
            arrayAdapter.notifyDataSetChanged();
        }
    }*/
}
