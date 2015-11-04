package com.fabian.vilo.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fabian.vilo.R;
import com.fabian.vilo.custom_methods.Util;
import com.fabian.vilo.models.CDModels.CDPost;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.realm.Realm;

/**
 * Created by Fabian on 31/10/15.
 */

public class DetailCorePostAdapter extends ArrayAdapter<CDPost> {

    private CDPost post;
    private Context context;
    private final int resourceID;

    public DetailCorePostAdapter(Context context, int textViewResourceId, List<CDPost> objects) {
        super(context, textViewResourceId, objects);

        this.context = context;
        this.resourceID = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);

        // Get the data item_event for this position
        post = getItem(position);
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());

            
            if (post.getType() == 1) {
                convertView = inflater.inflate(R.layout.event_main_detail_cell, null);

                viewHolder.eventPostInfoTitle = (TextView) convertView.findViewById(R.id.eventPostInfoTitle);
                viewHolder.eventPostLocationName = (TextView) convertView.findViewById(R.id.eventPostLocationName);
                viewHolder.eventPostAdress = (TextView) convertView.findViewById(R.id.eventPostAdress);
                viewHolder.eventPostDate = (TextView) convertView.findViewById(R.id.eventPostDate);
            } else {
                convertView = inflater.inflate(R.layout.quick_main_detail_cell, null);
            }

            viewHolder.quickMainText = (TextView) convertView.findViewById(R.id.quickMainText);
            viewHolder.quickMainTitle = (TextView) convertView.findViewById(R.id.quickMainTitle);
            viewHolder.quickMainImage = (ImageView) convertView.findViewById(R.id.quickMainImage);
            viewHolder.quickMainUserImage = (ImageView) convertView.findViewById(R.id.quickMainUserImage);
            viewHolder.quickMainUsername = (TextView) convertView.findViewById(R.id.quickMainUsername);
            viewHolder.quickMainTime = (TextView) convertView.findViewById(R.id.quickMainTime);

            viewHolder.quickMainImage.getLayoutParams().height = viewHolder.quickMainImage.getLayoutParams().width;

            /*switch (post.getType()) {
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

            viewHolder.swipeTitle = (TextView) convertView.findViewById(R.id.swipeTitle);
            viewHolder.swipeText = (TextView) convertView.findViewById(R.id.swipeText);
            viewHolder.swipeDistanceText = (TextView) convertView.findViewById(R.id.swipeDistanceText);
            viewHolder.swipeClockText = (TextView) convertView.findViewById(R.id.swipeClockText);
            viewHolder.swipeLikes = (TextView) convertView.findViewById(R.id.swipeLikes);
            viewHolder.swipeComments = (TextView) convertView.findViewById(R.id.swipeComments);*/

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object


        if(post != null) {

            if (post.getType() == 1) {
                viewHolder.eventPostInfoTitle.setText(post.getTitle());
                viewHolder.eventPostLocationName.setText(post.getLocation().getName());
                viewHolder.eventPostAdress.setText(post.getLocation().getCity()+", "+post.getLocation().getStreet());
                viewHolder.eventPostDate.setText(post.getEvent_date());
            }

            viewHolder.quickMainText.setText(post.getText());
            viewHolder.quickMainTitle.setText(post.getTitle());
            viewHolder.quickMainUsername.setText(post.getUsername());
            viewHolder.quickMainTime.setText(new Util().calculateElapsedTime(post.getTimestamp()));

            if (post.getImgURL().trim().length() > 0) {

                Picasso.with(context)
                        .load(post.getImgURL())
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .centerCrop()
                        .fit()
                        .into(viewHolder.quickMainImage);
            } else {
                viewHolder.quickMainImage.setVisibility(View.GONE);
            }

            /*if (post.getType() == 0) {
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
            viewHolder.swipeClockText.setText(new Util().calculateElapsedTime(post.getTimestamp()));*/

        }



        // Return the completed view to render on screen
        return convertView;

    }

    static class ViewHolder
    {
        // quickpost main
        TextView quickMainTitle;
        TextView quickMainText;
        ImageView quickMainImage;
        ImageView quickMainUserImage;
        TextView quickMainUsername;
        TextView quickMainTime;

        // event main
        TextView eventPostInfoTitle;
        TextView eventPostLocationName;
        TextView eventPostAdress;
        TextView eventPostDate;
    }

}
