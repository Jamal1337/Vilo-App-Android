package com.fabian.vilo.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.util.Log;

import com.fabian.vilo.api.ViloApiAdapter;
import com.fabian.vilo.api.ViloApiEndpointInterface;
import com.fabian.vilo.around_me_screen.Card;
import com.fabian.vilo.custom_methods.Util;
import com.fabian.vilo.cards.EventpostCard;
import com.fabian.vilo.cards.PostLocation;
import com.fabian.vilo.cards.QuickpostCard;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Fabian on 16/10/15.
 */
public class CardManager {

    private Context context;
    private Card card;
    SharedPreferences sharedpreferences;

    public CardManager(Context context) {
        this.context = context;
        sharedpreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
    }

    public Card getQuickPost(int postID) {
        ViloApiAdapter viloAdapter = ViloApiAdapter.getInstance(context);

        ViloApiEndpointInterface apiService = viloAdapter.mApi;

        Call<QuickpostCard> call = apiService.getQuickPost(postID);

        call.enqueue(new Callback<QuickpostCard>() {
            @Override
            public void onResponse(Response<QuickpostCard> response, Retrofit retrofit) {
                Log.d("", "quickpost response: " + response.body().lng);
                QuickpostCard qpCard = response.body();

                Location location_a = new Location("locA");
                Location location_b = new Location("locB");

                location_a.setLatitude(qpCard.lat);
                location_a.setLongitude(qpCard.lng);
                location_b.setLatitude(Integer.parseInt(sharedpreferences.getString("lat", "0")));
                location_b.setLongitude(Integer.parseInt(sharedpreferences.getString("lng", "0")));

                card = new Card(qpCard.title, qpCard.text, qpCard.id, qpCard.type, 0, qpCard.commentcount, qpCard.followers, new Util().calculateElapsedTime(qpCard.timestamp), new Util().distance2String(location_a, location_b, "km"), qpCard.username, qpCard.attachment, qpCard.timestamp,
                        qpCard.userid, qpCard.photo, qpCard.lat, qpCard.lng, qpCard.radius, "", 0, qpCard.last_updated, 0, qpCard.topTip, new ArrayList<PostLocation>());
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("", "error: " + t.getMessage());
            }
        });

        return card;
    }

    public Card getEventPost(int postID) {
        ViloApiAdapter viloAdapter = ViloApiAdapter.getInstance(context);

        ViloApiEndpointInterface apiService = viloAdapter.mApi;

        Call<EventpostCard> call = apiService.getEventPost(postID);

        call.enqueue(new Callback<EventpostCard>() {
            @Override
            public void onResponse(Response<EventpostCard> response, Retrofit retrofit) {
                /*Log.d("", "eventpost response: " + response.body()._long);
                EventpostCard qpCard = response.body();

                Location location_a = new Location("locA");
                Location location_b = new Location("locB");

                location_a.setLatitude(qpCard.lat);
                location_a.setLongitude(qpCard._long);
                location_b.setLatitude(Integer.parseInt(sharedpreferences.getString("lat", "0")));
                location_b.setLongitude(Integer.parseInt(sharedpreferences.getString("lng", "0")));

                card = new Card(qpCard.title, qpCard.text, qpCard.id, qpCard.type, 0, qpCard.commentcount, qpCard.followers, new Util().calculateElapsedTime(qpCard.timestamp), new Util().distance2String(location_a, location_b, "km"), qpCard.username, qpCard.attachment, qpCard.timestamp,
                        qpCard.userid, qpCard.photo, qpCard.lat, qpCard._long, qpCard.radius, qpCard.event_date, 0, qpCard.last_updated, 0, qpCard.topTip, new PostLocation());*/
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("", "error: " + t.getMessage());
            }
        });

        return card;
    }
}
