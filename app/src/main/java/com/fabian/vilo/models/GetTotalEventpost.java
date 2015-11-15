package com.fabian.vilo.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fabian on 07/11/15.
 */
public class GetTotalEventpost {
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
    public int commentcount;
    public String last_updated;
    public int followers;
    public List<Location> location = new ArrayList<Location>();
    public List<Comment> comments = new ArrayList<Comment>();

    public class Comment {
        public int commentid;
        public String attachment;
        public int lat;
        public int lng;
        public String timestamp;
        public String text;
        public String username;
        public int userid;
        public String photo;
    }

    public class Location {

        public int id;
        public String fbid;
        public String name;
        public String type;
        public String street;
        public String city;
        public String state;
        public String country;
        public String zip;
        public float lat;
        public float lng;
        public String image;

    }

}
