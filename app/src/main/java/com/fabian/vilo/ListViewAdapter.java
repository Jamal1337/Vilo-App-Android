package com.fabian.vilo;

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

import com.fabian.vilo.models.CDModels.CDPost;
import com.squareup.picasso.Picasso;

import io.realm.Realm;

/**
 * Created by Fabian on 10/10/15.
 */

public class ListViewAdapter extends ArrayAdapter<CDPost> {
    private final Context context;
    private final int resourceID;
    private final ArrayList<CDPost> data;

    private CDPost post;
    private Me mAdapterCallback;

    SharedPreferences sharedpreferences;

    public ListViewAdapter(Context context, int resource, ArrayList<CDPost> data, Me parent) {
        super(context, resource, data);

        this.context = context;
        this.resourceID = resource;
        this.data = data;
        this.mAdapterCallback = parent;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // Get the data item_event for this position
        post = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());

            switch (post.getType()) {
                case 0:
                    if (post.getImgURL() != "") {
                        convertView = inflater.inflate(R.layout.listview_quickpost, null);
                        viewHolder.swipeImage = (ImageView) convertView.findViewById(R.id.swipeImage);
                    } else {
                        convertView = inflater.inflate(R.layout.listview_quickpost_noimage, null);
                    }
                    break;
                case 1:
                    convertView = inflater.inflate(R.layout.listview_eventpost, null);
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
                                    Realm realm = Realm.getInstance(context);
                                    CDPost toEdit = realm.where(CDPost.class)
                                            .equalTo("id", data.get(position).getId()).findFirst();
                                    realm.beginTransaction();
                                    toEdit.removeFromRealm();
                                    realm.commitTransaction();

                                    data.remove(position);

                                    notifyDataSetChanged();

                                    mAdapterCallback.updateInterests();

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

            if (post.getType() == 0) {
                if (post.getImgURL() != "") {
                    //new ImageDownloader(viewHolder.swipeImage).execute(post.getImgURL());
                    Picasso.with(context)
                            .load(post.getImgURL())
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            //.centerCrop()
                            //.fit()
                            .into(viewHolder.swipeImage);
                }
            } else if (post.getType() == 1) {
                //new ImageDownloader(viewHolder.swipeImage).execute(post.getImgURL());
                Picasso.with(context)
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