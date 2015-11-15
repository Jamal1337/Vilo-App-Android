package com.fabian.vilo.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fabian on 13/11/15.
 */
public class NewQuickComments {
    public List<Data> data = new ArrayList<Data>();

    public class Data {

        public String timestamp;
        public String text;
        public String attachment;
        public float lng;
        public float lat;
        public String username;
        public String photo;
        public int userid;
        public int commentid;

    }
}
