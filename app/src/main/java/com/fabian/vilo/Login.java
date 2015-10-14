package com.fabian.vilo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.fabian.vilo.models.CDModels.ModelManager;
import com.fabian.vilo.models.FbUserAuth;
import com.fabian.vilo.models.User;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;


import java.util.Arrays;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Fabian on 12/10/15.
 */
public class Login extends FragmentActivity {

    SharedPreferences sharedpreferences;
    private CallbackManager callbackManager;
    private static final String TAG = AroundMe.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        LoginButton loginButton = (LoginButton) findViewById(R.id.fb_login_button);
        loginButton.setReadPermissions(Arrays.asList("user_friends", "email"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            //final ProgressDialog dialog = ProgressDialog.show(getApplicationContext(), "", "loading...");

            @Override
            public void onSuccess(final LoginResult loginResult) {
                Log.i("Login : ", "Success");
                GraphRequest.newMeRequest(
                        loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject me, GraphResponse response) {
                                if (response.getError() != null) {
                                    // handle error
                                } else {
                                    sharedpreferences = getApplicationContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.putBoolean("loggedin", true);
                                    editor.putInt("radius", 10000);
                                    editor.commit();

                                    String email = me.optString("email");
                                    String id = me.optString("id");
                                    Log.d(TAG, "user id: " + id);

                                    String locale = getApplicationContext().getResources().getConfiguration().locale.getCountry();
                                    FbUserAuth userAuth = new FbUserAuth();
                                    userAuth.interests_push = 0;
                                    userAuth.own_push = 1;
                                    userAuth.origin = locale;
                                    userAuth.token = loginResult.getAccessToken().getToken();

                                    Log.d(TAG, "userauth: " + userAuth);
                                    Log.d("bla", "push " + userAuth.interests_push);
                                    Log.d("bla", "push " + userAuth.own_push);
                                    Log.d("bla", userAuth.origin);
                                    Log.d("bla", loginResult.getAccessToken().getToken());

                                    // handle login data, call fbauth function via api
                                    saveUserData(userAuth);
                                        //dialog.dismiss();

                                }
                            }
                        }).executeAsync();

            }

            @Override
            public void onCancel() {
                Log.i("Login : ", "Cancel");
                //dialog.dismiss();
            }

            @Override
            public void onError(FacebookException e) {
                Log.e("Login Error : ", e.getMessage() + "");
                //dialog.dismiss();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void saveUserData(FbUserAuth userAuth) {

        ViloApiAdapter viloAdapter = ViloApiAdapter.getInstance(getApplicationContext());

        ViloApiEndpointInterface apiService = viloAdapter.mApi;

        Call<User> call = apiService.saveUser(userAuth);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Response<User> response, Retrofit retrofit) {

                //String bodyString = response.body().string();	log(bodyString); response = response.newBuilder().body(ResponseBody.create(response.body().contentType(), bodyString)).build();
                int statusCode = response.code();
                Log.d(TAG, "response raw: " + response.raw());
                Log.d(TAG, "response statusCode: " + statusCode);
                Log.d(TAG, "response: " + response.body().toString());
                Log.d(TAG, "response headers: " + response.headers());

                /*for (int i = 0; i < response.body().data.size(); i++) {
                    Log.d(TAG, "value: "+ response.body().data.get(i).id);
                }*/
                Log.d(TAG, "username: " + response.body().first_name);

                ModelManager modelManager = new ModelManager();
                if (modelManager.storeUserToDB(response, getApplicationContext()) == true) {
                    finish();
                }

                /*Uri myUri = Uri.parse(response.body().photo);

                byte[] inputData = convertImageToByte(myUri);

                // Obtain a Realm instance
                Realm realm = Realm.getInstance(getApplicationContext());
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
                realm.commitTransaction();*/

            }

            @Override
            public void onFailure(Throwable t) {
                Log.d(TAG, "error: " + t.getMessage());
            }
        });

    }

    /*public Login() {

    }

    public static Login newInstance()
    {
        Login login = new Login();
        //quickpost.setArguments(new Bundle());
        return login;
    }

    public void onActivityResult(int i, int j, Intent intent)
    {

    }*/

}
