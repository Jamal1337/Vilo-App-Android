package com.fabian.vilo.adapters;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fabian.vilo.api.ViloApiAdapter;
import com.fabian.vilo.api.ViloApiEndpointInterface;
import com.fabian.vilo.me_screen.Me;
import com.fabian.vilo.R;
import com.fabian.vilo.custom_methods.Util;
import com.fabian.vilo.models.CDModels.CDPost;
import com.fabian.vilo.models.ViloResponse;

import io.realm.Realm;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Fabian on 10/10/15.
 */

public class ListViewAdapter extends ArrayAdapter<CDPost> {
    private final Context context;
    private final int resourceID;
    private final ArrayList<CDPost> data;

    private CDPost post;
    private Me mAdapterCallback;

    private Realm realm;
    private ViloApiAdapter viloAdapter;
    private ViloApiEndpointInterface apiService;

    SharedPreferences sharedpreferences;

    public ListViewAdapter(Context context, int resource, ArrayList<CDPost> data, Me parent) {
        super(context, resource, data);

        this.context = context;
        this.resourceID = resource;
        this.data = data;
        this.mAdapterCallback = parent;

        this.realm = Realm.getInstance(context);

        this.viloAdapter = ViloApiAdapter.getInstance(context);

        this.apiService = viloAdapter.mApi;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // Get the data item_event for this position
        post = getItem(position);

        convertView = null;
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());

            switch (post.getType()) {
                case 0:
                    Log.d("bla", "title is quick: "+post.getTitle());
                    if (post.getImgURL().trim().length() > 0) {
                        convertView = inflater.inflate(R.layout.listview_quickpost, parent, false);
                        viewHolder.swipeImage = (ImageView) convertView.findViewById(R.id.swipeImage);
                    } else {
                        convertView = inflater.inflate(R.layout.listview_quickpost_noimage, null);
                    }
                    break;
                case 1:
                    Log.d("bla", "title is event: "+post.getTitle());
                    convertView = inflater.inflate(R.layout.listview_eventpost, parent, false);
                    viewHolder.swipeImage = (ImageView) convertView.findViewById(R.id.swipeImage);
                    break;
            }

            convertView.findViewById(R.id.trash).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(context)
                            .setTitle("Delete post")
                            .setMessage("Are you sure you want to delete this post?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    //Log.d("bla", "delete post id: "+data.get(position).getId());

                                    if (data.get(position).getIsOwn() == 0) {

                                        Call<ViloResponse> call = apiService.deleteInterest(data.get(position).getId(), data.get(position).getType());

                                        call.enqueue(new Callback<ViloResponse>() {
                                            @Override
                                            public void onResponse(Response<ViloResponse> response, Retrofit retrofit) {
                                                CDPost toEdit = realm.where(CDPost.class)
                                                        .equalTo("id", data.get(position).getId()).findFirst();
                                                realm.beginTransaction();
                                                toEdit.removeFromRealm();
                                                realm.commitTransaction();
                                                data.remove(position);

                                                // interests post delete
                                                mAdapterCallback.updateInterests();

                                                notifyDataSetChanged();
                                            }

                                            @Override
                                            public void onFailure(Throwable t) {
                                                Log.d("error", "error: " + t.getMessage());
                                            }
                                        });


                                    } else {

                                        Call<ViloResponse> call = apiService.deletePost(data.get(position).getId(), data.get(position).getType());

                                        call.enqueue(new Callback<ViloResponse>() {
                                            @Override
                                            public void onResponse(Response<ViloResponse> response, Retrofit retrofit) {
                                                CDPost toEdit = realm.where(CDPost.class)
                                                        .equalTo("id", data.get(position).getId()).findFirst();
                                                realm.beginTransaction();
                                                toEdit.removeFromRealm();
                                                realm.commitTransaction();
                                                data.remove(position);

                                                // own posts delete
                                                mAdapterCallback.updateOwn();

                                                notifyDataSetChanged();
                                            }

                                            @Override
                                            public void onFailure(Throwable t) {
                                                Log.d("error", "error: " + t.getMessage());
                                            }
                                        });
                                    }

                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            });

            viewHolder.swipeTitle = (TextView) convertView.findViewById(R.id.swipeTitle);
            viewHolder.swipeText = (TextView) convertView.findViewById(R.id.swipeText);
            viewHolder.swipeDistanceText = (TextView) convertView.findViewById(R.id.swipeDistanceText);
            viewHolder.swipeClockText = (TextView) convertView.findViewById(R.id.swipeClockText);
            viewHolder.swipeLikes = (TextView) convertView.findViewById(R.id.swipeLikes);
            viewHolder.swipeComments = (TextView) convertView.findViewById(R.id.swipeComments);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object


        if(post != null) {

            Log.d("bla", "fetched image: "+post.getImgURL());
            Log.d("bla", "fetched title: "+post.getTitle());
            Log.d("bla", "fetched type: "+post.getType());

            if (post.getType() == 0) {
                if (post.getImgURL().trim().length() > 0) {
                    //new ImageDownloader(viewHolder.swipeImage).execute(post.getImgURL());
                    Glide.with(context)
                            .load(post.getImgURL())
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            //.centerCrop()
                            //.fit()
                            .into(viewHolder.swipeImage);
                }
            } else if (post.getType() == 1) {
                //new ImageDownloader(viewHolder.swipeImage).execute(post.getImgURL());
                Glide.with(context)
                        .load(post.getImgURL())
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        //.centerCrop()
                        //.fit()
                        .into(viewHolder.swipeImage);
            }

            sharedpreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

            Location location_a = new Location("locA");
            Location location_b = new Location("locB");

            location_a.setLatitude(post.getLatitude());
            location_a.setLongitude(post.getLongitude());
            location_b.setLatitude(Double.parseDouble(sharedpreferences.getString("lat", "0")));
            location_b.setLongitude(Double.parseDouble(sharedpreferences.getString("lng", "0")));

            viewHolder.swipeText.setText(post.getText());
            viewHolder.swipeTitle.setText(post.getTitle());
            viewHolder.swipeLikes.setText(""+post.getInterestcount());
            viewHolder.swipeComments.setText(""+post.getCommentcount());
            viewHolder.swipeDistanceText.setText(new Util().distance2String(location_a, location_b, "km"));
            viewHolder.swipeClockText.setText(new Util().calculateElapsedTime(post.getTimestamp()));

            /*Picasso.with(context)
                    .load(card.photo)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    //.centerCrop()
                    .fit()
                    .into(viewHolder.userImage);

            Picasso.with(context)
                    .load(card.attachment)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    //.centerCrop()
                    .fit()
                    .into(viewHolder.cardImage);*/
        }



        // Return the completed view to render on screen
        return convertView;
    }

    static class ViewHolder
    {
        TextView swipeTitle;
        TextView swipeText;
        ImageView swipeImage;
        TextView swipeDistanceText;
        TextView swipeClockText;
        TextView swipeLikes;
        TextView swipeComments;
    }

}