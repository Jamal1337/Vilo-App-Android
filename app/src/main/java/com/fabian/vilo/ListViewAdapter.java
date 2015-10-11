package com.fabian.vilo;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * Created by Fabian on 10/10/15.
 */

public class ListViewAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final int resourceID;

    public ListViewAdapter(Context context, int resource, ArrayList<String> bah) {
        super(context, resource, bah);

        this.context = context;
        this.resourceID = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(resourceID, parent, false);

        return rowView;
    }
}