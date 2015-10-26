package com.fabian.vilo;

/**
 * Created by Fabian on 10/10/15.
 */

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuffXfermode;
import android.graphics.PorterDuff.Mode;
import android.util.Log;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.util.AttributeSet;
import android.graphics.drawable.BitmapDrawable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {

    public String calculateElapsedTime(String startTime) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String elapsedTime = "";

        try {
            Date endDate = new Date();
            Date startDate = dateFormat.parse(startTime);

            //milliseconds
            long different = endDate.getTime() - startDate.getTime();

            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

            long elapsedDays = different / daysInMilli;
            different = different % daysInMilli;

            long elapsedHours = different / hoursInMilli;
            different = different % hoursInMilli;

            long elapsedMinutes = different / minutesInMilli;
            different = different % minutesInMilli;

            long elapsedSeconds = different / secondsInMilli;

            if (elapsedDays != 0) {
                elapsedTime = elapsedDays+"d";
            } else if (elapsedHours != 0) {
                elapsedTime = elapsedHours+"h";
            } else if (elapsedMinutes != 0) {
                elapsedTime = elapsedMinutes+"m";
            } else if (elapsedSeconds != 0) {
                elapsedTime = elapsedSeconds+"s";
            }

        } catch (ParseException e) {
            Log.d("Error", "error: "+e.getMessage());
        }



        return elapsedTime;
    }

    public String distance2String(Location location_a, Location location_b, String unit) {

        String distance = "";

        String distance_array[][] =
                {
                        { "0","50","< 50m" },
                        { "51","100","< 100m" },
                        { "101","200","< 200m" },
                        { "201","300","< 300m" },
                        { "301","400","< 400m" },
                        { "401","500","< 500m" },
                        { "501","600","< 600m" },
                        { "601","700","< 700m" },
                        { "701","800","< 800m" },
                        { "801","900","< 900m" },
                        { "901","1000","< 1km" },
                        { "1001","1500","< 1,5km" },
                        { "1501","2000","< 2km" },
                        { "2001","2500","< 2,5km" },
                        { "2501","3000","< 3km" },
                        { "3001","4000","< 4km" },
                        { "4001","5000","< 5km" },
                        { "5001","10000","< 10km" },
                        { "10001","20000","< 20km" },
                        { "20001","50000","< 50km" },
                        { "50001","100000","< 100km" },
                        { "100001","200000","< 200km" },
                        { "200001","500000","< 500km" },
                        { "500001","1000000","< 1000km" }
                };

        String distance_array_miles[][] =
                {
                        { "0","160","< 0.1mi" },
                        { "161","320","< 0.2mi" },
                        { "321","482","< 0.3mi" },
                        { "483","643","< 0.4mi" },
                        { "644","804","< 0.5mi" },
                        { "805","965","< 0.6mi" },
                        { "966","1126","< 0.7mi" },
                        { "1127","1287","< 0.8mi" },
                        { "1288","1448","< 0.9mi" },
                        { "1449","1609","< 1mi" },
                        { "1610","3218","< 2mi" },
                        { "3219","4828","< 3mi" },
                        { "4829","6437","< 4mi" },
                        { "6438","8046","< 5mi" },
                        { "8047","9656","< 6mi" },
                        { "9657","11265","< 7mi" },
                        { "11266","12874","< 8mi" },
                        { "12875","14484","< 9mi" },
                        { "14485","16093","< 10mi" },
                        { "16094","32186","< 20mi" },
                        { "32187","80467","< 50mi" },
                        { "80468","160934","< 100mi" },
                        { "160935","321869","< 200mi" },
                        { "321870","482803","< 300mi" },
                        { "482804","804672","< 500mi" },
                        { "804673","1609344","< 1000mi" }
                };


        // calculate distance in meters
        Float calcDistance = location_a.distanceTo(location_b) ;

        if (unit == "km") {
            String distanceString = "";
            Boolean inRange = false;
            for (int j = 0; j < distance_array.length; j++) {
                Integer min = Integer.parseInt(distance_array[j][0]);
                Integer max = Integer.parseInt(distance_array[j][1]);
                if (calcDistance >= min && calcDistance <= max) {
                    distanceString = distance_array[j][2];
                    inRange = true;
                }
            }
            if (inRange == true) {
                return distanceString;
            } else {
                return "far away";
            }
        } else {
            String distanceString = "";
            Boolean inRange = false;
            for (int j = 0; j < distance_array_miles.length; j++) {
                Integer min = Integer.parseInt(distance_array[j][0]);
                Integer max = Integer.parseInt(distance_array[j][1]);
                if (calcDistance >= min && calcDistance <= max) {
                    distanceString = distance_array[j][2];
                    inRange = true;
                }
            }
            if (inRange == true) {
                return distanceString;
            } else {
                return "far away";
            }
        }
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /*private static final String TAG = Util.class.getSimpleName();

    public Util()
    {
    }

    public static float convertDpToPixel(float dp){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }

    /*public static float convertDpToPixel(float f, Context context)
    {
        Log.d(TAG, "bla");
        Log.d(TAG, "context: "+context);
        //return f * ((float)context.getResources().getDisplayMetrics().densityDpi / 160F);
        return 10;
    }*/

    /*public static float convertPixelsToDp(float f, Context context)
    {
        return f / ((float)context.getResources().getDisplayMetrics().densityDpi / 160F);
    }

    public static Bitmap roundImage(Bitmap bitmap)
    {
        Bitmap bitmap1 = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), android.graphics.Bitmap.Config.ARGB_8888);
        Object obj = new BitmapShader(bitmap, android.graphics.Shader.TileMode.CLAMP, android.graphics.Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setShader(((android.graphics.Shader) (obj)));
        obj = new Paint();
        ((Paint) (obj)).setColor(-1);
        paint.setStyle(android.graphics.Paint.Style.FILL);
        paint.setAntiAlias(true);
        Canvas canvas = new Canvas(bitmap1);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, ((Paint) (obj)));
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2 - (int) convertDpToPixel(1.0F), paint);
        return bitmap1;
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 12;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }*/

}
