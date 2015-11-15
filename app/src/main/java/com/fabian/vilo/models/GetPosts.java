package com.fabian.vilo.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fabian on 07/11/15.
 */
public class GetPosts {
    public List<Interest> interests = new ArrayList<Interest>();
    public List<Own> own = new ArrayList<Own>();

    public class Interest {
        public int id;
        public int type;
    }

    public class Own {
        public int id;
        public int type;
    }
}
