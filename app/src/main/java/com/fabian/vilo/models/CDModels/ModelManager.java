package com.fabian.vilo.models.CDModels;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.fabian.vilo.around_me_screen.Card;
import com.fabian.vilo.models.User;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import retrofit.Response;

/**
 * Created by Fabian on 12/10/15.
 */
public class ModelManager {

    public ModelManager()
    {

    }

    public boolean storeUserToDB(Response<User> response, Context context) {

        Uri myUri = Uri.parse("https://vilostorage.s3.amazonaws.com/profilepics/user_31437672110.jpg");

        byte[] inputData = convertImageToByte(myUri, context);

        // Obtain a Realm instance
        Realm realm = Realm.getInstance(context);
        realm.beginTransaction();
        CDUser user = realm.createObject(CDUser.class); // Create a new object
        user.setUserid(response.body().id);
        user.setBirthday(response.body().birthday);
        user.setDevicetoken(response.body().devicetoken);
        user.setEmail(response.body().email);
        user.setFbid(response.body().facebookid);
        user.setFirst_name(response.body().first_name);
        user.setGender(response.body().sex);
        user.setInterests_push(response.body().interests_push);
        user.setLast_name(response.body().last_name);
        user.setOwn_push(response.body().own_push);
        user.setPassword("");
        user.setProfilePicture(inputData);
        user.setTstamp(response.body().signup_date);
        realm.commitTransaction();

        return true;
    }

    public byte[] convertImageToByte(Uri uri, Context context){
        byte[] data = null;
        try {
            ContentResolver cr = context.getContentResolver();
            InputStream inputStream = cr.openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            data = baos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return data;
    }

    public Bitmap getBitmapFromURL(String src)
    {
        try
        {
            URL url = new URL(src);
            HttpURLConnection connection;
            connection=(HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public boolean savePost(Card post, Context context) {

        Uri myUri = Uri.parse(post.attachment);
        Uri photoUri = Uri.parse(post.photo);

        byte[] inputData = convertImageToByte(myUri, context);
        byte[] photoData = convertImageToByte(photoUri, context);

        Realm realm = Realm.getInstance(context);
        realm.beginTransaction();

        CDPost newPost = realm.createObject(CDPost.class);
        newPost.setAttachment(inputData);
        newPost.setAttendentcount(0);
        newPost.setCategory(post.category);
        newPost.setComment(null);
        newPost.setCommentcount(post.commentCount);
        newPost.setEvent_date(post.event_date);
        newPost.setHasReported(0);
        newPost.setId(post.postid);
        newPost.setImgURL(post.attachment);
        newPost.setInterestcount(post.interestCount+1);
        newPost.setIsNew(0);
        newPost.setIsOwn(0);
        newPost.setLast_updated(post.last_updated);
        newPost.setLatitude(post.latitude);
        newPost.setLongitude(post.longitude);
        newPost.setLocation_id(post.location_id);
        newPost.setPhoto(photoData);
        newPost.setRadius(post.radius);
        //newPost.setPoll();
        newPost.setUser(null);
        newPost.setUserid(post.userid);
        newPost.setUsername(post.username);
        newPost.setText(post.text);
        newPost.setTitle(post.title);
        newPost.setTimestamp(post.timestamp);
        newPost.setTopTip(post.topTip);
        newPost.setType(post.type);

        if ((post.type == 4 && post.location != null) || (post.type == 1)) {
            CDLocation location = realm.createObject(CDLocation.class);

            location.setFbid(post.location.get(0).fbid);
            location.setCity(post.location.get(0).city);
            location.setCountry(post.location.get(0).country);
            location.setImage(post.location.get(0).image);
            location.setLatitude(post.location.get(0).lat);
            location.setLongitude(post.location.get(0).lng);
            location.setName(post.location.get(0).name);
            location.setLocation_id(post.location.get(0).id);
            location.setState(post.location.get(0).state);
            location.setStreet(post.location.get(0).street);
            location.setZip(post.location.get(0).zip);
            location.setType(post.location.get(0).type);

            location.setPost(newPost);
            newPost.setLocation(location);

        }

        realm.commitTransaction();

        return true;
    }

    public boolean saveNewQuickPost(Card post, Context context) {

        Uri myUri = Uri.parse(post.attachment);

        byte[] inputData = convertImageToByte(myUri, context);

        Realm realm = Realm.getInstance(context);

        RealmQuery<CDUser> query = realm.where(CDUser.class);

        // Execute the query:
        RealmResults<CDUser> result = query.findAll();

        realm.beginTransaction();

        CDPost newPost = realm.createObject(CDPost.class);
        newPost.setAttachment(inputData);
        newPost.setAttendentcount(0);
        newPost.setCategory(post.category);
        newPost.setComment(null);
        newPost.setCommentcount(post.commentCount);
        newPost.setEvent_date(post.event_date);
        newPost.setHasReported(0);
        newPost.setId(post.postid);
        newPost.setImgURL(post.attachment);
        newPost.setInterestcount(post.interestCount);
        newPost.setIsNew(0);
        newPost.setIsOwn(1);
        newPost.setLast_updated(post.last_updated);
        newPost.setLatitude(post.latitude);
        newPost.setLongitude(post.longitude);
        newPost.setLocation_id(post.location_id);
        newPost.setPhoto(null);
        newPost.setRadius(post.radius);
        //newPost.setPoll();
        newPost.setUser(result.first());
        newPost.setUserid(post.userid);
        newPost.setUsername(post.username);
        newPost.setText(post.text);
        newPost.setTitle(post.title);
        newPost.setTimestamp(post.timestamp);
        newPost.setTopTip(post.topTip);
        newPost.setType(post.type);

        realm.commitTransaction();

        return true;
    }

}
