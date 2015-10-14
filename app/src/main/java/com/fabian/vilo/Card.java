package com.fabian.vilo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Fabian on 11/10/15.
 */
public class Card {
    public String title;
    public String text;

    public Card(String title, String text) {
        this.title = title;
        this.text = text;
    }

    // Constructor to convert JSON object into a Java class instance
    public Card(JSONObject object){
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
    }
}
