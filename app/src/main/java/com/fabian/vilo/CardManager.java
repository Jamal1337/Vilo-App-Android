package com.fabian.vilo;

import android.content.Context;
import android.util.Log;

import com.fabian.vilo.cards.Posts;
import com.fabian.vilo.cards.QuickpostCard;

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

    public CardManager(Context context) {
        this.context = context;
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
                card = new Card(qpCard.title, qpCard.text, qpCard.id, qpCard.type, 0, qpCard.commentcount, qpCard.followers, "", "", qpCard.username, qpCard.attachment, qpCard.timestamp,
                        qpCard.userid, qpCard.photo, qpCard.lat, qpCard.lng, qpCard.radius, "", 0, qpCard.last_updated, 0, qpCard.topTip);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("", "error: " + t.getMessage());
            }
        });

        return card;
    }

    public void getEventPost(int postID) {

    }
}
