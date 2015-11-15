package com.fabian.vilo.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fabian on 13/11/15.
 */
public class UpdatePosts {
    public List<Data> data = new ArrayList<Data>();

    public class Data {

        public int id;
        public int followers;
        public int attendentcount;
        public String commentcount;
        public int post_deleted;
        public List<Integer> deleted_comments = new ArrayList<Integer>();
        public String last_updated;
        public int maindeleted;
        public int userid;

    }
}
