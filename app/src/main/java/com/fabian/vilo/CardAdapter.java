package com.fabian.vilo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Fabian on 11/10/15.
 */
public class CardAdapter extends ArrayAdapter<Card> {
    private final Context context;
    private final ArrayList<Card> data;
    private final int layoutResourceId;

    public CardAdapter(Context context, int layoutResourceId, ArrayList<Card> data) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.data = data;
        this.layoutResourceId = layoutResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item_event for this position
        Card card = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());

            if (card.type == 0) {
                convertView = inflater.inflate(R.layout.item_quick, parent, false);
                viewHolder.postText = (TextView) convertView.findViewById(R.id.postText);
            } else if (card.type == 1) {
                convertView = inflater.inflate(R.layout.item_event, parent, false);
                viewHolder.cardLocation = (TextView) convertView.findViewById(R.id.cardLocation);
                viewHolder.cardHome = (TextView) convertView.findViewById(R.id.cardHome);
                viewHolder.cardCalendar = (TextView) convertView.findViewById(R.id.cardCalendar);
            }



            viewHolder.title = (TextView) convertView.findViewById(R.id.cardTitle);
            viewHolder.cardImage = (ImageView) convertView.findViewById(R.id.cardImage);
            viewHolder.userImage = (ImageView) convertView.findViewById(R.id.userImage);
            viewHolder.cardView = (RelativeLayout) convertView.findViewById(R.id.cardView);
            viewHolder.cardDistance = (TextView) convertView.findViewById(R.id.cardDistance);
            viewHolder.cardTime = (TextView) convertView.findViewById(R.id.cardTime);
            viewHolder.cardLikes = (TextView) convertView.findViewById(R.id.cardLikes);
            viewHolder.cardComments = (TextView) convertView.findViewById(R.id.cardComments);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object


        if(card != null) {

            if (card.type == 0) {
                viewHolder.postText.setText(card.text);
            } else if (card.type == 1) {
                viewHolder.cardCalendar.setText(card.event_date);
                viewHolder.cardLocation.setText(card.location.get(0).name);
                String adress = card.location.get(0).street+", "+card.location.get(0).city;
                viewHolder.cardHome.setText(adress);
            }


            viewHolder.title.setText(card.title);
            viewHolder.cardLikes.setText(""+card.interestCount);
            viewHolder.cardComments.setText(""+card.commentCount);
            viewHolder.cardDistance.setText(card.distance);
            viewHolder.cardTime.setText(card.timeAgo);

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

            new ImageDownloader(viewHolder.userImage).execute(card.photo);
            new ImageDownloader(viewHolder.cardImage).execute(card.attachment);
        }



        // Return the completed view to render on screen
        return convertView;
    }

    static class ViewHolder
    {
        TextView title;
        TextView postText;
        ImageView cardImage;
        ImageView userImage;
        RelativeLayout cardView;
        TextView cardDistance;
        TextView cardTime;
        TextView cardLikes;
        TextView cardComments;

        TextView cardLocation;
        TextView cardHome;
        TextView cardCalendar;
    }
}