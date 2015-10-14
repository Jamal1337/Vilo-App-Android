package com.fabian.vilo.cards;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fabian on 11/10/15.
 */

public class Posts {

    public List<Data> data = new ArrayList<Data>();

    public class Data {

        public int id;
        public int type;

    }

}

/*public class Posts {

    public String title;
    public String text;
    public int postid;
    public int type;
    public int category;
    public int commentCount;
    public int interestCount;
    public String timeAgo;
    public String distance;
    public String username;
    public String attachment;
    public String timestamp;
    public int userid;
    public String photo;
    public float longitude;
    public float latitude;
    public int radius;
    public String event_date;
    public int location_id;
    public String last_updated;
    public int isAttended;
    public int topTip;

}*/