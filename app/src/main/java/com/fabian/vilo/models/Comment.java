package com.fabian.vilo.models;

/**
 * Created by Fabian on 06/11/15.
 */
public class Comment implements WebPostInterface {
    public int commentid;
    public String imgURL;
    public float latitude;
    public float longitude;
    public String photo;
    public int postid;
    public String timestamp;
    public int type;
    public int userid;
    public String username;
    public String value;
    public int hasReported;

    public Comment(int commentid, String imgURL, float latitude, float longitude, String photo, int postid, String timestamp, int type, int userid, String username, String value, int hasReported) {
        this.commentid = commentid;
        this.imgURL = imgURL;
        this.latitude = latitude;
        this.longitude = longitude;
        this.photo = photo;
        this.postid = postid;
        this.timestamp = timestamp;
        this.type = type;
        this.userid = userid;
        this.username = username;
        this.value = value;
        this.hasReported = hasReported;
    }

}
