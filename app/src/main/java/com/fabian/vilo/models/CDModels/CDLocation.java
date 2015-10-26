package com.fabian.vilo.models.CDModels;

import io.realm.RealmObject;

/**
 * Created by Fabian on 12/10/15.
 */
public class CDLocation extends RealmObject {

    private int location_id;
    private String fbid;
    private String name;
    private String type;
    private String street;
    private String city;
    private String state;
    private String country;
    private String zip;
    private float latitude;
    private float longitude;
    private String image;
    private CDPost post;

    /**
     * Getter
     */
    public int getLocation_id() {
        return location_id;
    }

    public String getFbid() {
        return fbid;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public String getZip() {
        return zip;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public String getImage() {
        return image;
    }

    public CDPost getPost() {
        return post;
    }

    /**
     * Setter
     */

    public void setLocation_id(int location_id) {
        this.location_id = location_id;
    }

    public void setFbid(String fbid) {
        this.fbid = fbid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setPost(CDPost post) {
        this.post = post;
    }
}
