package com.fabian.vilo.custom_methods;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by Fabian on 22/10/15.
 */
public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;
    byte[] bytes;

    /*public ImageDownloader(ImageView bmImage) {
        this.bmImage = bmImage;
    }*/

    public ImageDownloader(byte[] bytes) {
        this.bytes = bytes;
    }

    protected Bitmap doInBackground(String... urls) {
        String url = urls[0];
        Bitmap mIcon = null;
        try {
            InputStream in = new java.net.URL(url).openStream();
            mIcon = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
        return mIcon;
    }

    protected void onPostExecute(Bitmap result) {
        //bmImage.setImageBitmap(result);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        result.compress(Bitmap.CompressFormat.PNG, 100, stream);
        bytes = stream.toByteArray();
    }
}