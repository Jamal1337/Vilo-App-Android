package com.fabian.vilo.models.CDModels;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import com.fabian.vilo.models.Card;
import com.fabian.vilo.models.Comment;
import com.fabian.vilo.models.GetTotalEventpost;
import com.fabian.vilo.models.GetTotalQuickpost;
import com.fabian.vilo.models.NewQuickComments;
import com.fabian.vilo.models.User;
import com.fabian.vilo.models.WebPostInterface;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
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
        user.setUserPhoto(response.body().photo);
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

        Uri photoUri = Uri.parse(post.photo);

        byte[] photoData = convertImageToByte(photoUri, context);

        Realm realm = Realm.getInstance(context);
        realm.beginTransaction();

        CDPost newPost = realm.createObject(CDPost.class);
        if (post.attachment.trim().length() > 0) {
            Uri myUri = Uri.parse(post.attachment);
            byte[] inputData = convertImageToByte(myUri, context);
            newPost.setAttachment(inputData);
        }
        newPost.setAttendentcount(0);
        newPost.setCategory(post.category);
        newPost.setComments(null);
        newPost.setCommentcount(post.commentCount);
        newPost.setEvent_date(post.event_date);
        newPost.setHasReported(0);
        newPost.setId(post.postid);
        newPost.setImgURL(post.attachment);
        newPost.setInterestcount(post.interestCount + 1);
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
        newPost.setUserPhoto(post.photo);

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

    public boolean storeQuickPostInRealm(GetTotalQuickpost post, int isOwn, CDUser user, Context context) {

        Uri photoUri = Uri.parse(post.photo);

        byte[] photoData = convertImageToByte(photoUri, context);

        Realm realm = Realm.getInstance(context);
        realm.beginTransaction();

        CDPost newPost = realm.createObject(CDPost.class);
        if (post.attachment.trim().length() > 0) {
            Uri myUri = Uri.parse(post.attachment);
            byte[] inputData = convertImageToByte(myUri, context);
            newPost.setAttachment(inputData);
        }
        newPost.setAttendentcount(0);
        newPost.setCategory(0);
        //newPost.setComment(null);
        newPost.setCommentcount(post.commentcount);
        //newPost.setEvent_date(post.event_date);
        newPost.setHasReported(0);
        newPost.setId(post.id);
        newPost.setImgURL(post.attachment);
        newPost.setInterestcount(post.followers);
        newPost.setIsNew(0);
        newPost.setIsOwn(isOwn);
        newPost.setLast_updated(post.last_updated);
        newPost.setLatitude(post.lat);
        newPost.setLongitude(post.lng);
        //newPost.setLocation_id(post.location_id);
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
        newPost.setUserPhoto(post.photo);

        if (post.commentcount > 0) {

            RealmList<CDComment> comments = new RealmList<CDComment>();

            for (int i = 0; i < post.comments.size(); i++) {
                CDComment comment = realm.createObject(CDComment.class);

                Uri photoCommentUri = Uri.parse(post.comments.get(i).photo);

                byte[] photoCommentData = convertImageToByte(photoCommentUri, context);

                if (post.comments.get(i).attachment.trim().length() > 0) {
                    Uri myCommentUri = Uri.parse(post.comments.get(i).attachment);
                    byte[] inputCommentData = convertImageToByte(myCommentUri, context);
                    comment.setAttachment(inputCommentData);
                }
                comment.setValue(post.comments.get(i).text);
                comment.setUsername(post.comments.get(i).username);
                comment.setUserPhoto(post.comments.get(i).photo);
                comment.setUserid(post.comments.get(i).userid);
                comment.setType(0);
                comment.setTimestamp(post.comments.get(i).timestamp);
                comment.setCommentid(post.comments.get(i).commentid);
                comment.setHasReported(0);
                comment.setImgURL(post.comments.get(i).attachment);
                comment.setLatitude(post.comments.get(i).lat);
                comment.setLongitude(post.comments.get(i).lng);
                comment.setPhoto(photoCommentData);
                comment.setPostid(post.id);
                comment.setPost(newPost);

                comments.add(comment);
            }

            newPost.setComments(comments);
        }

        /*if ((post.type == 4 && post.location != null) || (post.type == 1)) {
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

        }*/

        realm.commitTransaction();

        return true;
    }

    public boolean storeQuickPostCommentsInRealm(NewQuickComments commentsList, Integer postid, Context context) {

        if (commentsList.data.size() == 0) {
            return false;
        }

        Realm realm = Realm.getInstance(context);
        realm.beginTransaction();

        RealmList<CDComment> comments = new RealmList<CDComment>();

        RealmQuery<CDPost> query = realm.where(CDPost.class);

        query.equalTo("id", postid);

        // Execute the query:
        RealmResults<CDPost> result = query.findAll();

        for (int i = 0; i < commentsList.data.size(); i++) {
            CDComment comment = realm.createObject(CDComment.class);

            Uri photoCommentUri = Uri.parse(commentsList.data.get(i).photo);

            byte[] photoCommentData = convertImageToByte(photoCommentUri, context);

            if (commentsList.data.get(i).attachment.trim().length() > 0) {
                Uri myCommentUri = Uri.parse(commentsList.data.get(i).attachment);
                byte[] inputCommentData = convertImageToByte(myCommentUri, context);
                comment.setAttachment(inputCommentData);
            }
            comment.setValue(commentsList.data.get(i).text);
            comment.setUsername(commentsList.data.get(i).username);
            comment.setUserPhoto(commentsList.data.get(i).photo);
            comment.setUserid(commentsList.data.get(i).userid);
            comment.setType(0);
            comment.setTimestamp(commentsList.data.get(i).timestamp);
            comment.setCommentid(commentsList.data.get(i).commentid);
            comment.setHasReported(0);
            comment.setImgURL(commentsList.data.get(i).attachment);
            comment.setLatitude(commentsList.data.get(i).lat);
            comment.setLongitude(commentsList.data.get(i).lng);
            comment.setPhoto(photoCommentData);
            comment.setPostid(postid);
            comment.setPost(result.first());

            comments.add(comment);
        }

        result.first().setCommentcount(result.first().getCommentcount()+commentsList.data.size());

        realm.commitTransaction();

        return true;
    }

    public boolean storeEventPostCommentsInRealm(NewQuickComments commentsList, Integer postid, Context context) {

        if (commentsList.data.size() == 0) {
            return false;
        }

        Realm realm = Realm.getInstance(context);
        realm.beginTransaction();

        RealmList<CDComment> comments = new RealmList<CDComment>();

        RealmQuery<CDPost> query = realm.where(CDPost.class);

        query.equalTo("id", postid);

        // Execute the query:
        RealmResults<CDPost> result = query.findAll();

        for (int i = 0; i < commentsList.data.size(); i++) {
            CDComment comment = realm.createObject(CDComment.class);

            Uri photoCommentUri = Uri.parse(commentsList.data.get(i).photo);

            byte[] photoCommentData = convertImageToByte(photoCommentUri, context);

            if (commentsList.data.get(i).attachment.trim().length() > 0) {
                Uri myCommentUri = Uri.parse(commentsList.data.get(i).attachment);
                byte[] inputCommentData = convertImageToByte(myCommentUri, context);
                comment.setAttachment(inputCommentData);
            }
            comment.setValue(commentsList.data.get(i).text);
            comment.setUsername(commentsList.data.get(i).username);
            comment.setUserPhoto(commentsList.data.get(i).photo);
            comment.setUserid(commentsList.data.get(i).userid);
            comment.setType(0);
            comment.setTimestamp(commentsList.data.get(i).timestamp);
            comment.setCommentid(commentsList.data.get(i).commentid);
            comment.setHasReported(0);
            comment.setImgURL(commentsList.data.get(i).attachment);
            comment.setLatitude(commentsList.data.get(i).lat);
            comment.setLongitude(commentsList.data.get(i).lng);
            comment.setPhoto(photoCommentData);
            comment.setPostid(postid);
            comment.setPost(result.first());

            comments.add(comment);
        }

        result.first().setCommentcount(result.first().getCommentcount()+commentsList.data.size());

        realm.commitTransaction();

        return true;
    }

    public boolean storeEventPostInRealm(GetTotalEventpost post, int isOwn, CDUser user, Context context) {

        Uri photoUri = Uri.parse(post.photo);

        byte[] photoData = convertImageToByte(photoUri, context);

        Realm realm = Realm.getInstance(context);
        realm.beginTransaction();

        CDPost newPost = realm.createObject(CDPost.class);
        if (post.attachment.trim().length() > 0) {
            Uri myUri = Uri.parse(post.attachment);
            byte[] inputData = convertImageToByte(myUri, context);
            newPost.setAttachment(inputData);
        }

        Log.d("manager", "eventDate: " + post.event_date);

        /*SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        try {
            Date date = format.parse(post.event_date);
            System.out.println("Date ->" + date);
            newPost.setEvent_date(date.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        newPost.setAttendentcount(0);
        newPost.setCategory(0);
        newPost.setCommentcount(post.commentcount);
        newPost.setEvent_date(post.event_date);
        newPost.setHasReported(0);
        newPost.setId(post.id);
        newPost.setImgURL(post.attachment);
        newPost.setInterestcount(post.followers);
        newPost.setIsNew(0);
        newPost.setIsOwn(isOwn);
        newPost.setLast_updated(post.last_updated);
        newPost.setLatitude(post.lat);
        newPost.setLongitude(post.lng);
        newPost.setLocation_id(post.location.get(0).id);
        newPost.setPhoto(photoData);
        newPost.setRadius(post.radius);
        newPost.setUser(null);
        newPost.setUserid(post.userid);
        newPost.setUsername(post.username);
        newPost.setText(post.text);
        newPost.setTitle(post.title);
        newPost.setTimestamp(post.timestamp);
        newPost.setTopTip(post.topTip);
        newPost.setType(post.type);
        newPost.setUserPhoto(post.photo);

        if (post.commentcount > 0) {

            RealmList<CDComment> comments = new RealmList<CDComment>();

            for (int i = 0; i < post.comments.size(); i++) {
                CDComment comment = realm.createObject(CDComment.class);

                Uri photoCommentUri = Uri.parse(post.comments.get(i).photo);

                byte[] photoCommentData = convertImageToByte(photoCommentUri, context);

                if (post.comments.get(i).attachment.trim().length() > 0) {
                    Uri myCommentUri = Uri.parse(post.comments.get(i).attachment);
                    byte[] inputCommentData = convertImageToByte(myCommentUri, context);
                    comment.setAttachment(inputCommentData);
                }

                comment.setValue(post.comments.get(i).text);
                comment.setUsername(post.comments.get(i).username);
                comment.setUserPhoto(post.comments.get(i).photo);
                comment.setUserid(post.comments.get(i).userid);
                comment.setType(0);
                comment.setTimestamp(post.comments.get(i).timestamp);
                comment.setCommentid(post.comments.get(i).commentid);
                comment.setHasReported(0);
                comment.setImgURL(post.comments.get(i).attachment);
                comment.setLatitude(post.comments.get(i).lat);
                comment.setLongitude(post.comments.get(i).lng);
                comment.setPhoto(photoCommentData);
                comment.setPostid(post.id);
                comment.setPost(newPost);

                comments.add(comment);
            }

            newPost.setComments(comments);
        }

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
        newPost.setComments(null);
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
        newPost.setUserPhoto(post.photo);

        realm.commitTransaction();

        return true;
    }

    public CDComment saveNewQuickPostComment(Comment comment, Context context) {

        Realm realm = Realm.getInstance(context);

        // Fetch core Post
        RealmQuery<CDPost> queryPost = realm.where(CDPost.class);
        queryPost.equalTo("id", comment.postid);
        RealmResults<CDPost> resultPost = queryPost.findAll();

        // Fetch core User
        RealmQuery<CDUser> query = realm.where(CDUser.class);
        RealmResults<CDUser> result = query.findAll();

        realm.beginTransaction();

        resultPost.first().setCommentcount(resultPost.first().getCommentcount() + 1);

        CDComment newComment = realm.createObject(CDComment.class);

        newComment.setImgURL(comment.imgURL);
        newComment.setPost(resultPost.first());
        newComment.setLatitude(comment.latitude);
        if (comment.imgURL.trim().length() > 0) {
            Uri myUri = Uri.parse(comment.imgURL);

            byte[] inputData = convertImageToByte(myUri, context);
            newComment.setAttachment(inputData);
        }
        newComment.setCommentid(comment.commentid);
        newComment.setHasReported(0);
        newComment.setLongitude(comment.longitude);
        newComment.setPhoto(null);
        newComment.setPostid(comment.postid);
        newComment.setTimestamp(comment.timestamp);
        newComment.setType(comment.type);
        newComment.setUserid(comment.userid);
        newComment.setUsername(comment.username);
        newComment.setValue(comment.value);
        newComment.setUserPhoto(comment.photo);

        resultPost.first().setLast_updated(comment.timestamp);

        realm.commitTransaction();

        return newComment;
    }

    public List<Integer> getLastCommentID(Integer postid, Context context) {
        Realm realm = Realm.getInstance(context);

        // Fetch core Post
        RealmQuery<CDComment> query = realm.where(CDComment.class);
        query.equalTo("postid", postid);
        RealmResults<CDComment> result = query.findAll();
        result.sort("commentid", RealmResults.SORT_ORDER_DESCENDING);

        List<Integer> info = new ArrayList<Integer>();

        if (result.size() > 0) {
            CDComment resultLast = result.first();
            CDComment resultSecondLast = result.get(1);



            info.add(resultLast.getCommentid());
            info.add(resultSecondLast.getCommentid());
            info.add(resultLast.getUserid());
            return info;
        } else {
            info.add(0);
            return info;
        }
    }

    public Integer getCommentcount(Integer postid, Context context) {
        Realm realm = Realm.getInstance(context);

        // Fetch core Post
        RealmQuery<CDComment> query = realm.where(CDComment.class);
        query.equalTo("postid", postid);
        RealmResults<CDComment> result = query.findAll();

        return result.size();
    }

    public boolean setPostRead(Integer postid, Context context) {
        Realm realm = Realm.getInstance(context);

        // Fetch core Post
        RealmQuery<CDPost> query = realm.where(CDPost.class);
        query.equalTo("id", postid);
        RealmResults<CDPost> result = query.findAll();

        realm.beginTransaction();

        result.first().setIsNew(0);

        realm.commitTransaction();

        return true;

    }

    public boolean storeQuickpostAfterCommentInRealm(List<WebPostInterface> postArray, Context context) {

        Card post = (Card) postArray.get(0);

        Uri photoUri = Uri.parse(post.photo);

        byte[] photoData = convertImageToByte(photoUri, context);

        Realm realm = Realm.getInstance(context);
        realm.beginTransaction();

        CDPost newPost = realm.createObject(CDPost.class);
        if (post.attachment.trim().length() > 0) {
            Uri myUri = Uri.parse(post.attachment);
            byte[] inputData = convertImageToByte(myUri, context);
            newPost.setAttachment(inputData);
        }
        newPost.setAttendentcount(0);
        newPost.setCategory(0);
        //newPost.setComment(null);
        newPost.setCommentcount(post.commentCount);
        //newPost.setEvent_date(post.event_date);
        newPost.setHasReported(0);
        newPost.setId(post.postid);
        newPost.setImgURL(post.attachment);
        newPost.setInterestcount(post.interestCount);
        newPost.setIsNew(0);
        newPost.setIsOwn(0);
        newPost.setLast_updated(post.last_updated);
        newPost.setLatitude(post.latitude);
        newPost.setLongitude(post.longitude);
        //newPost.setLocation_id(post.location_id);
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
        newPost.setUserPhoto(post.photo);


        RealmList<CDComment> comments = new RealmList<CDComment>();

        for (int i = 1; i < postArray.size(); i++) {
            CDComment comment = realm.createObject(CDComment.class);

            Comment comm = (Comment) postArray.get(i);

            Uri photoCommentUri = Uri.parse(comm.photo);

            byte[] photoCommentData = convertImageToByte(photoCommentUri, context);

            if (comm.imgURL.trim().length() > 0) {
                Uri myCommentUri = Uri.parse(comm.imgURL);
                byte[] inputCommentData = convertImageToByte(myCommentUri, context);
                comment.setAttachment(inputCommentData);
            }
            comment.setValue(comm.value);
            comment.setUsername(comm.username);
            comment.setUserPhoto(comm.photo);
            comment.setUserid(comm.userid);
            comment.setType(0);
            comment.setTimestamp(comm.timestamp);
            comment.setCommentid(comm.commentid);
            comment.setHasReported(0);
            comment.setImgURL(comm.imgURL);
            comment.setLatitude(comm.latitude);
            comment.setLongitude(comm.longitude);
            comment.setPhoto(photoCommentData);
            comment.setPostid(comm.postid);
            comment.setPost(newPost);

            comments.add(comment);
        }

        newPost.setComments(comments);

        realm.commitTransaction();

        return true;
    }

    public boolean storeEventpostAfterCommentInRealm(List<WebPostInterface> postArray, Context context) {

        Card post = (Card) postArray.get(0);

        Uri photoUri = Uri.parse(post.photo);

        byte[] photoData = convertImageToByte(photoUri, context);

        Realm realm = Realm.getInstance(context);
        realm.beginTransaction();

        CDPost newPost = realm.createObject(CDPost.class);
        if (post.attachment.trim().length() > 0) {
            Uri myUri = Uri.parse(post.attachment);
            byte[] inputData = convertImageToByte(myUri, context);
            newPost.setAttachment(inputData);
        }
        newPost.setAttendentcount(0);
        newPost.setCategory(0);
        //newPost.setComment(null);
        newPost.setCommentcount(post.commentCount);
        newPost.setEvent_date(post.event_date);
        newPost.setHasReported(0);
        newPost.setId(post.postid);
        newPost.setImgURL(post.attachment);
        newPost.setInterestcount(post.interestCount);
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
        newPost.setUserPhoto(post.photo);

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


        RealmList<CDComment> comments = new RealmList<CDComment>();

        for (int i = 1; i < postArray.size(); i++) {
            CDComment comment = realm.createObject(CDComment.class);

            Comment comm = (Comment) postArray.get(i);

            Uri photoCommentUri = Uri.parse(comm.photo);

            byte[] photoCommentData = convertImageToByte(photoCommentUri, context);

            if (comm.imgURL.trim().length() > 0) {
                Uri myCommentUri = Uri.parse(comm.imgURL);
                byte[] inputCommentData = convertImageToByte(myCommentUri, context);
                comment.setAttachment(inputCommentData);
            }
            comment.setValue(comm.value);
            comment.setUsername(comm.username);
            comment.setUserPhoto(comm.photo);
            comment.setUserid(comm.userid);
            comment.setType(0);
            comment.setTimestamp(comm.timestamp);
            comment.setCommentid(comm.commentid);
            comment.setHasReported(0);
            comment.setImgURL(comm.imgURL);
            comment.setLatitude(comm.latitude);
            comment.setLongitude(comm.longitude);
            comment.setPhoto(photoCommentData);
            comment.setPostid(comm.postid);
            comment.setPost(newPost);

            comments.add(comment);
        }

        newPost.setComments(comments);

        realm.commitTransaction();

        return true;
    }

}
