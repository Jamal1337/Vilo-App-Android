package com.fabian.vilo.models.CDModels;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.fabian.vilo.models.User;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import io.realm.Realm;
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

}
