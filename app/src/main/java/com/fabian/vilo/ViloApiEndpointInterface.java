package com.fabian.vilo;

import com.fabian.vilo.cards.Posts;
import com.fabian.vilo.cards.QuickpostCard;
import com.fabian.vilo.models.FbUserAuth;
import com.fabian.vilo.models.User;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;


/**
 * Created by Fabian on 11/10/15.
 */
public interface ViloApiEndpointInterface {

    // Request method and URL specified in the annotation
    // Callback for the parsed response is the last parameter

    /*@GET("/users/{username}")
    Call<Posts> getUser(@Path("username") String username);

    @GET("/group/{id}/users")
    Call<List<Posts>> groupList(@Path("id") int groupId, @Query("sort") String sort);

    @POST("/users/new")
    Call<Posts> createUser(@Body Posts post);*/

    // ski9OA6rkDlCxhctbNMiyFjn0s45rb1LmYDd7Jz1NqE3D

    //@Headers("apiKey: ski9OA6rkDlCxhctbNMiyFjn0s45rb1LmYDd7Jz1NqE3D")
    @GET("/api/v2/post/around/me/lat/{lat}/lng/{lng}/radius/{radius}/metric/{metric}")
    Call<Posts> getPosts(@Path("lat") String lat, @Path("lng") String lng, @Path("radius") int radius, @Path("metric") String metric);

    @POST("/api/v1/user/fbauth")
    Call<User> saveUser(@Body FbUserAuth userAuth);

    @POST("/api/v1/user/fbauth")
    Call<User> reAuth(@Body FbUserAuth userAuth);

    @GET("/api/v1/post/quick/{postid}")
    Call<QuickpostCard> getQuickPost(@Path("postid") int postid);

    /*@GET("/v2/post/around/me")
    void  getPosts(
            Callback<Posts> callback
    );*/
    //Call<Posts> getPosts(Callback<Posts> callback);


}
