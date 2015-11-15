package com.fabian.vilo.models.CDModels;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.fabian.vilo.models.CDModels.CDPost;
import com.fabian.vilo.models.User;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import retrofit.Response;

/**

 * Created by Fabian on 12/10/15.
 */
public class CDUser extends RealmObject {

    private String devicetoken;
    private String email;
    private String password;
    private String fbid;
    private String first_name;
    private int interests_push;
    private String last_name;
    private int own_push;
    private byte[] profilePicture;
    private String tstamp;
    private int userid;
    private String gender;
    private String birthday;

    /**
     * Setter
     */

    public void setDevicetoken(String devicetoken) {
        this.devicetoken = devicetoken;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFbid(String fbid) {
        this.fbid = fbid;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setInterests_push(int interests_push) {
        this.interests_push = interests_push;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setOwn_push(int own_push) {
        this.own_push = own_push;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void setTstamp(String tstamp) {
        this.tstamp = tstamp;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }


    /**
     * Getter
     */

    public String getDevicetoken() {
        return devicetoken;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFbid() {
        return fbid;
    }

    public String getFirst_name() {
        return first_name;
    }

    public int getInterests_push() {
        return interests_push;
    }

    public String getLast_name() {
        return last_name;
    }

    public int getOwn_push() {
        return own_push;
    }

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public String getTstamp() {
        return tstamp;
    }

    public int getUserid() {
        return userid;
    }

    public String getGender() {
        return gender;
    }

    public String getBirthday() {
        return birthday;
    }

}
