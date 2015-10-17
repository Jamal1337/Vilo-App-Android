package com.fabian.vilo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.media.Image;
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

        // Get the data item for this position
        Card card = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_quick, parent, false);

            Log.d("test", "value: "+card.title);

            viewHolder.title = (TextView) convertView.findViewById(R.id.cardTitle);
            //viewHolder.text = (TextView) convertView.findViewById(R.id.cardLocation);
            viewHolder.cardImage = (ImageView) convertView.findViewById(R.id.cardImage);
            viewHolder.cardView = (RelativeLayout) convertView.findViewById(R.id.cardView);
            viewHolder.postText = (TextView) convertView.findViewById(R.id.postText);
            viewHolder.cardDistance = (TextView) convertView.findViewById(R.id.cardDistance);
            viewHolder.cardTime = (TextView) convertView.findViewById(R.id.cardTime);
            viewHolder.cardLikes = (TextView) convertView.findViewById(R.id.cardLikes);
            viewHolder.cardComments = (TextView) convertView.findViewById(R.id.cardComments);

            viewHolder.title.setText(card.title);
            viewHolder.postText.setText(card.text);
            //viewHolder.cardComments.setText(card.commentCount);
            //viewHolder.cardLikes.setText(card.interestCount);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object

        Picasso.with(context)
                .load(R.drawable.rehab)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .centerCrop()
                .fit()
                .into(viewHolder.cardImage);
        // Return the completed view to render on screen
        return convertView;
    }

    static class ViewHolder
    {
        TextView title;
        //TextView text;
        TextView postText;
        ImageView cardImage;
        RelativeLayout cardView;
        TextView cardDistance;
        TextView cardTime;
        TextView cardLikes;
        TextView cardComments;
    }
}