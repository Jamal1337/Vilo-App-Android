package com.fabian.vilo;

import android.content.Context;
import android.util.Log;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.List;

import okio.Buffer;
import retrofit.Callback;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

//import java.lang.String;

/**
 * Created by Fabian on 11/10/15.
 */
public class ViloApiAdapter {

    protected final String TAG = getClass().getSimpleName();

    private static ViloApiAdapter mInstance = null;

    protected Retrofit mRestAdapter;
    protected ViloApiEndpointInterface mApi;
    static final String BASE_URL = "https://api.viloapp.com";

    private ViloApiAdapter(Context context) {


        /*httpClient.setCookieHandler(new CookieManager(
                new PersistentCookieStore(context),
                CookiePolicy.ACCEPT_ALL));*/
        //CookieManager cookieManager = new CookieManager();
        //cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        //httpClient.setCookieHandler(cookieManager);


        //CookieManager cookieManager = new CookieManager(new PersistentCookieStore(context), CookiePolicy.ACCEPT_ALL);
        OkHttpClient httpClient = new OkHttpClient();

        //httpClient.setCookieHandler(cookieManager);

        httpClient.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request request = chain.request().newBuilder().addHeader("apiKey", "ski9OA6rkDlCxhctbNMiyFjn0s45rb1LmYDd7Jz1NqE3D").build();
                return chain.proceed(request);
            }
        });

        httpClient.interceptors().add(new Interceptor() {
            @Override
            public com.squareup.okhttp.Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Buffer buffer = new Buffer();
                //request.body().writeTo(buffer);

                Log.i(TAG,"Request to " + request.urlString() + "\n" + buffer.readUtf8());
                long t1 = System.nanoTime();
                com.squareup.okhttp.Response response = chain.proceed(request);
                long t2 = System.nanoTime();
                String msg = response.body().string();
                Log.i(TAG, String.format("Response from %s in %.1fms%n\n%s",
                        response.request().urlString(), (t2 - t1) / 1e6d, msg));
                return response.newBuilder()
                        .body(ResponseBody.create(response.body().contentType(), msg))
                        .build();
            }
        });

        httpClient.setCookieHandler(new CookieManager(
                new PersistentCookieStore(context),
                CookiePolicy.ACCEPT_ALL));

        mRestAdapter = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();

        mApi = mRestAdapter.create(ViloApiEndpointInterface.class);

    }

    public static ViloApiAdapter getInstance(Context context){
        if(mInstance == null)
        {
            mInstance = new ViloApiAdapter(context);
        }
        return mInstance;
    }

    /*public ViloApiEndpointInterface getViloRestAdapter(Context context) {





        OkHttpClient httpClient = new OkHttpClient();
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        httpClient.setCookieHandler(cookieManager);
        httpClient.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request request = chain.request().newBuilder().addHeader("apiKey", "ski9OA6rkDlCxhctbNMiyFjn0s45rb1LmYDd7Jz1NqE3D").build();
                return chain.proceed(request);
            }
        });

        mRestAdapter = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();

        mApi = mRestAdapter.create(ViloApiEndpointInterface.class);

        return mApi;
    }*/

}
