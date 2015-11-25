package com.fabian.vilo.api;

import com.fabian.vilo.cards.EventpostCard;
import com.fabian.vilo.cards.Posts;
import com.fabian.vilo.cards.QuickpostCard;
import com.fabian.vilo.models.FbUserAuth;
import com.fabian.vilo.models.GetPosts;
import com.fabian.vilo.models.GetTotalEventpost;
import com.fabian.vilo.models.GetTotalQuickpost;
import com.fabian.vilo.models.NewQuickComments;
import com.fabian.vilo.models.QuickComment;
import com.fabian.vilo.models.QuickUpload;
import com.fabian.vilo.models.UpdatePosts;
import com.fabian.vilo.models.User;
import com.fabian.vilo.models.ViloResponse;
import com.fabian.vilo.models.ViloUploadResponse;
import com.squareup.okhttp.RequestBody;

import java.util.ArrayList;
import java.util.Map;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Part;
import retrofit.http.Path;


/**
 * Created by Fabian on 11/10/15.
 */
public interface ViloApiEndpointInterface {

    // Request method and URL specified in the annotation
    // Callback for the parsed response is the last parameter

    // ski9OA6rkDlCxhctbNMiyFjn0s45rb1LmYDd7Jz1NqE3D


    @GET("/api/v2/post/around/me/lat/{lat}/lng/{lng}/radius/{radius}/metric/{metric}")
    Call<Posts> getPosts(@Path("lat") String lat, @Path("lng") String lng, @Path("radius") int radius, @Path("metric") String metric);

    @POST("/api/v1/user/fbauth")
    Call<User> saveUser(@Body FbUserAuth userAuth);

    @POST("/api/v1/user/fbauth")
    Call<User> reAuth(@Body FbUserAuth userAuth);

    @GET("api/v1/user/{userid}/getposts/")
    Call<GetPosts> getPosts(@Path("userid") int userid);

    @GET("/api/v1/post/quick/{postid}")
    Call<QuickpostCard> getQuickPost(@Path("postid") int postid);

    @GET("/api/v1/post/quick/{postid}/total")
    Call<GetTotalQuickpost> getTotalQuickPost(@Path("postid") int postid);

    @GET("/api/v1/post/event/{postid}")
    Call<EventpostCard> getEventPost(@Path("postid") int postid);

    @GET("/api/v1/post/event/{postid}/total")
    Call<GetTotalEventpost> getTotalEventPost(@Path("postid") int postid);

    @PUT("/api/v1/post/swipe")
    Call<ViloResponse> pushSwipedPosts(@Body Map<String,ArrayList> swipedPosts);

    /**
     * Quickpost
     * @param witPhoto
     * @param title
     * @param text
     * @param lat
     * @param lng
     * @param file
     * @return
     */

    @Multipart
    @POST("/api/v2/post/quick/create")
    Call<ViloUploadResponse> uploadQuickWithImage(
            @Part("withPhoto") int witPhoto,
            @Part("title") String title,
            @Part("text") String text,
            @Part("lat") double lat,
            @Part("lng") double lng,
            @Part("file\"; filename=\"file.png\" ") RequestBody file);

    @POST("/api/v2/post/quick/create")
    Call<ViloUploadResponse> uploadQuickWithoutImage(@Body QuickUpload quickUpload);

    @Multipart
    @POST("/api/v2/post/quick/{postid}/comment/create")
    Call<ViloUploadResponse> commentQuickWithImage(
            @Path("postid") int postid,
            @Part("withPhoto") int witPhoto,
            @Part("value") String value,
            @Part("lat") double lat,
            @Part("lng") double lng,
            @Part("file\"; filename=\"file.png\" ") RequestBody file);

    @POST("/api/v2/post/quick/{postid}/comment/create")
    Call<ViloUploadResponse> commentQuickWithoutImage(@Path("postid") int postid, @Body QuickComment quickComment);

    @DELETE("/api/v1/post/delete/own/{postid}/type/{type}")
    Call<ViloResponse> deletePost(@Path("postid") int postid, @Path("type") int type);

    @DELETE("/api/v1/post/delete/interest/{postid}/type/{type}")
    Call<ViloResponse> deleteInterest(@Path("postid") int postid, @Path("type") int type);

    @PUT("/api/v1/post/check")
    Call<UpdatePosts> updatePosts(@Body Map<String,ArrayList> checkPosts);

    @GET("/api/v1/post/quick/{postid}/comments/after/{secondLastCommentID}/and/{lastCommentID}")
    Call<NewQuickComments> fetchNewQuickpostComments(@Path("postid") Integer postid, @Path("secondLastCommentID") Integer secondLastCommentID, @Path("lastCommentID") Integer lastCommentID);

    @GET("/api/v1/post/event/{postid}/comments/after/{secondLastCommentID}/and/{lastCommentID}")
    Call<NewQuickComments> fetchNewEventpostComments(@Path("postid") Integer postid, @Path("secondLastCommentID") Integer secondLastCommentID, @Path("lastCommentID") Integer lastCommentID);

    @POST("/api/v1/user/{userid}/updateuserlocation")
    Call<ViloResponse> updateUserLocation(@Path("userid") Integer userid, @Body Map<String, String> parameters);

    @GET("/api/v1/post/{type}/{postid}/comments")
    Call<NewQuickComments> loadCommentsOfPost(@Path("type") String type, @Path("postid") Integer postid);

    @PUT("/api/v1/user/{userid}/updatepushsettings")
    Call<ViloResponse> updatePushSettings(@Path("userid") Integer userid, @Body Map<String,String> parameters);

}
