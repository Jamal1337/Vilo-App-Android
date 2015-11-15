package com.fabian.vilo.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fabian on 07/11/15.
 */
public class GetTotalQuickpost {
    public int id;
    public String title;
    public String text;
    public String username;
    public int userid;
    public String photo;
    public String timestamp;
    public float lng;
    public float lat;
    public int radius;
    public int type;
    public String attachment;
    public int topTip;
    public int commentcount;
    public String last_updated;
    public int followers;
    public List<Comment> comments = new ArrayList<Comment>();

    public class Comment {

        public int commentid;
        public String attachment;
        public float lat;
        public float lng;
        public String timestamp;
        public String text;
        public String username;
        public int userid;
        public String photo;

    }

}
