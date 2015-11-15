package com.fabian.vilo.models.CDModels;

import android.content.Context;

import com.fabian.vilo.models.CDModels.CDUser;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by Fabian on 12/10/15.
 */
public class CDPost extends RealmObject {

    private byte[] attachment;
    private int attendentcount;
    private int category;
    private int commentcount;
    private String event_date;
    private int hasReported;
    private int id;
    private String imgURL;
    private int interestcount;
    private int isAttended;
    private int isNew;
    private int isOwn;
    private String last_updated;
    private float latitude;
    private int location_id;
    private float longitude;
    private byte[] photo;
    private int radius;
    private String text;
    private String timestamp;
    private String title;
    private int type;
    private int userid;
    private String username;
    private int topTip;
    private CDUser user;
    private RealmList<CDComment> comments;
    private CDLocation location;
    private CDPoll poll;

    /**
     * Getter
     */

    public byte[] getAttachment() {
        return attachment;
    }

    public int getAttendentcount() {
        return attendentcount;
    }

    public int getCategory() {
        return category;
    }

    public int getCommentcount() {
        return commentcount;
    }

    public String getEvent_date() {
        return event_date;
    }

    public int getHasReported() {
        return hasReported;
    }

    public int getId() {
        return id;
    }

    public String getImgURL() {
        return imgURL;
    }

    public int getInterestcount() {
        return interestcount;
    }

    public int getIsAttended() {
        return isAttended;
    }

    public int getIsNew() {
        return isNew;
    }

    public int getIsOwn() {
        return isOwn;
    }

    public String getLast_updated() {
        return last_updated;
    }

    public float getLatitude() {
        return latitude;
    }

    public int getLocation_id() {
        return location_id;
    }

    public float getLongitude() {
        return longitude;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public int getRadius() {
        return radius;
    }

    public String getText() {
        return text;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getTitle() {
        return title;
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

    public int getTopTip() {
        return topTip;
    }

    public CDUser getUser() {
        return user;
    }

    public RealmList<CDComment> getComments() {
        return comments;
    }

    public CDLocation getLocation() {
        return location;
    }

    public CDPoll getPoll() {
        return poll;
    }

    /**
     * Setter
     */

    public void setAttachment(byte[] attachment) {
        this.attachment = attachment;
    }

    public void setAttendentcount(int attendentcount) {
        this.attendentcount = attendentcount;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public void setCommentcount(int commentcount) {
        this.commentcount = commentcount;
    }

    public void setEvent_date(String event_date) {
        this.event_date = event_date;
    }

    public void setHasReported(int hasReported) {
        this.hasReported = hasReported;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public void setInterestcount(int interestcount) {
        this.interestcount = interestcount;
    }

    public void setIsAttended(int isAttended) {
        this.isAttended = isAttended;
    }

    public void setIsNew(int isNew) {
        this.isNew = isNew;
    }

    public void setIsOwn(int isOwn) {
        this.isOwn = isOwn;
    }

    public void setLast_updated(String last_updated) {
        this.last_updated = last_updated;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public void setLocation_id(int location_id) {
        this.location_id = location_id;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public void setTopTip(int topTip) {
        this.topTip = topTip;
    }

    public void setUser(CDUser user) {
        this.user = user;
    }

    public void setComments(RealmList<CDComment> comments) {
        this.comments = comments;
    }

    public void setLocation(CDLocation location) {
        this.location = location;
    }

    public void setPoll(CDPoll poll) {
        this.poll = poll;
    }
}
