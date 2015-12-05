package com.fabian.vilo.models.CDModels;

import io.realm.RealmObject;

/**
 * Created by Fabian on 12/10/15.
 */
public class CDComment extends RealmObject {

    private byte[] attachment;
    private int commentid;
    private String imgURL;
    private float latitude;
    private float longitude;
    private byte[] photo;
    private String userPhoto;
    private int postid;
    private String timestamp;
    private int type;
    private int userid;
    private String username;
    private String value;
    private int hasReported;
    private CDPost post;

    /**
     * Getter
     */

    public byte[] getAttachment() {
        return attachment;
    }

    public int getCommentid() {
        return commentid;
    }

    public String getImgURL() {
        return imgURL;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public int getPostid() {
        return postid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public int getType() {
        return type;
    }

    public int getUserid() {
        return userid;
    }

    public String getUsername() {
        return username;
    }

    public String getValue() {
        return value;
    }

    public int getHasReported() {
        return hasReported;
    }

    public CDPost getPost() {
        return post;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    /**
     * Setter
     */

    public void setAttachment(byte[] attachment) {
        this.attachment = attachment;
    }

    public void setCommentid(int commentid) {
        this.commentid = commentid;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public void setPostid(int postid) {
        this.postid = postid;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setHasReported(int hasReported) {
        this.hasReported = hasReported;
    }

    public void setPost(CDPost post) {
        this.post = post;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }
}
