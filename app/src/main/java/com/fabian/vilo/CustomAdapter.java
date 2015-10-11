package com.fabian.vilo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter
{

    private final Context ctx;
    private final ArrayList list;

    public CustomAdapter(Context context, ArrayList arraylist)
    {
        ctx = context;
        list = arraylist;
    }

    public int getCount()
    {
        return list.size();
    }

    public Object getItem(int i)
    {
        return list.get(i);
    }

    public long getItemId(int i)
    {
        return (long)i;
    }

    public View getView(int i, View view, ViewGroup viewgroup)
    {
        view = ((LayoutInflater)ctx.getSystemService("layout_inflater")).inflate(0x7f03000e, viewgroup, false);
        ((TextView)view.findViewById(0x7f090047)).setText("2");
        return view;
    }
}
