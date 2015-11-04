package com.fabian.vilo.around_me_screen;

import com.fabian.vilo.cards.EventpostCard;
import com.fabian.vilo.cards.PostLocation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fabian on 11/10/15.
 */
public class Card {
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
    public List<PostLocation> location;

    public Card(String title, String text, int postid, int type, int category, int commentCount, int interestCount, String timeAgo, String distance, String username, String attachment, String timestamp,
    int userid, String photo, float latitude, float longitude, int radius, String event_date, int location_id, String last_updated, int isAttended, int topTip, List<PostLocation> location) {
        this.title = title;
        this.text = text;
        this.postid = postid;
        this.type = type;
        this.category = category;
        this.commentCount = commentCount;
        this.interestCount = interestCount;
        this.timeAgo = timeAgo;
        this.distance = distance;
        this.username = username;
        this.attachment = attachment;
        this.timestamp = timestamp;
        this.userid = userid;
        this.photo = photo;
        this.longitude = longitude;
        this.latitude = latitude;
        this.radius = radius;
        this.event_date = event_date;
        this.location_id = location_id;
        this.last_updated = last_updated;
        this.isAttended = isAttended;
        this.topTip = topTip;
        this.location = location;

    }

    // Constructor to convert JSON object into a Java class instance
    /*public Card(JSONObject object){
        try {
            this.title = object.getString("title");
            this.text = object.getString("text");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Factory method to convert an array of JSON objects into a list of objects
    // User.fromJson(jsonArray);
    public static ArrayList<Card> fromJson(JSONArray jsonObjects) {
        ArrayList<Card> cards = new ArrayList<Card>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                cards.add(new Card(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return cards;
    }*/
}
