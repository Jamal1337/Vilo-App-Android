package com.fabian.vilo.cards;

import android.location.Location;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Fabian on 19/10/15.
 */
public class EventpostCard {

    public int id;
    public String title;
    public String text;
    public String username;
    public int userid;
    public String photo;
    public String timestamp;
    public String event_date;
    public float lng;
    public float lat;
    public int radius;
    public int type;
    public String attachment;
    public int topTip;
    public String last_updated;
    public int commentcount;
    public int followers;
    //public EVLocation location;
    //public ArrayList<Location> location;// = new ArrayList<EVLocation>();
    public List<PostLocation> location = new ArrayList<PostLocation>();
    //public T location;

}
