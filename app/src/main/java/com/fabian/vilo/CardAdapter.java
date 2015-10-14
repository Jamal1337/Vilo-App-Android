package com.fabian.vilo;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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
            convertView = inflater.inflate(R.layout.item, parent, false);
            viewHolder.title = (TextView) convertView.findViewById(R.id.cardTitle);
            viewHolder.text = (TextView) convertView.findViewById(R.id.cardLocation);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        viewHolder.title.setText(card.title);
        viewHolder.text.setText(card.text);
        // Return the completed view to render on screen
        return convertView;
    }

    static class ViewHolder
    {
        TextView title;
        TextView text;
        TextView textView3;
    }
}