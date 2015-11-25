package com.fabian.vilo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.fabian.vilo.api.ViloApiAdapter;
import com.fabian.vilo.api.ViloApiEndpointInterface;
import com.fabian.vilo.around_me_screen.AroundMe;
import com.fabian.vilo.models.CDModels.CDUser;
import com.fabian.vilo.models.CDModels.ModelManager;
import com.fabian.vilo.models.FbUserAuth;
import com.fabian.vilo.models.GetPosts;
import com.fabian.vilo.models.GetTotalEventpost;
import com.fabian.vilo.models.GetTotalQuickpost;
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

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
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
    private int downloadedPostsCounter = 0;
    private ModelManager modelManager = new ModelManager();
    private Boolean agbCheck = false;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        TextView tosText = (TextView) findViewById(R.id.TOSLink);
        TextView ppText = (TextView) findViewById(R.id.PPLink);

        tosText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                WebViewController webView = new WebViewController();
                webView.setUrl(getApplicationContext().getResources().getText(R.string.termsofserviceurl).toString());

                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.addToBackStack(null);
                transaction.replace(R.id.loginFrame, webView);
                transaction.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.commit();

            }
        });

        ppText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebViewController webView = new WebViewController();
                webView.setUrl(getApplicationContext().getResources().getText(R.string.privacypolicyurl).toString());

                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.addToBackStack(null);
                transaction.replace(R.id.loginFrame, webView);
                transaction.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.commit();
            }
        });

        final LoginButton loginButton = (LoginButton) findViewById(R.id.fb_login_button);
        loginButton.setEnabled(false);
        agbCheck = false;

        CheckBox agbCheckbox = (CheckBox) findViewById(R.id.agbCheckbox);

        agbCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    agbCheck = true;
                    loginButton.setEnabled(true);
                } else {
                    agbCheck = false;
                    loginButton.setEnabled(false);
                }
            }
        });

        progress = new ProgressDialog(Login.this);


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

                                    progress.setTitle(R.string.loginLoadingTitle);
                                    progress.setMessage(Login.this.getResources().getText(R.string.loginLoadingMessage));
                                    progress.show();

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
                                    userAuth.interests_push = 1;
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
                    // check if user already has posts stored in db => download and save in realm
                    getUserPosts();
                    //finish();
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

    public void getUserPosts() {
        ViloApiAdapter viloAdapter = ViloApiAdapter.getInstance(getApplicationContext());

        final ViloApiEndpointInterface apiService = viloAdapter.mApi;

        Call<GetPosts> call = apiService.getPosts(85503);

        call.enqueue(new Callback<GetPosts>() {
            @Override
            public void onResponse(Response<GetPosts> response, Retrofit retrofit) {
                Log.d(TAG, "first id: "+response.body().interests.size());
                Log.d(TAG, "first id: "+response.body().own.size());

                final int totalDownloadPosts = response.body().interests.size() + response.body().own.size();
                Realm realm = Realm.getInstance(getApplicationContext());
                // Build the query looking at all users:
                RealmQuery<CDUser> query = realm.where(CDUser.class);

                // Execute the query:
                RealmResults<CDUser> result = query.findAll();
                final CDUser user = result.first();

                if (response.body().interests.size() > 0) {
                    int totalInterests = response.body().interests.size();
                    for (int i = 0; i < totalInterests; i++) {

                        Log.d(TAG, "interestid: "+response.body().interests.get(i).id);
                        Log.d(TAG, "interesttype: "+response.body().interests.get(i).type);

                        switch (response.body().interests.get(i).type) {
                            case 0:

                                Call<GetTotalQuickpost> call = apiService.getTotalQuickPost(response.body().interests.get(i).id);

                                call.enqueue(new Callback<GetTotalQuickpost>() {
                                    @Override
                                    public void onResponse(Response<GetTotalQuickpost> response, Retrofit retrofit) {

                                        if (modelManager.storeQuickPostInRealm(response.body(), 0, user, getApplicationContext())) {
                                            downloadedPostsCounter++;

                                            if (downloadedPostsCounter == totalDownloadPosts) {
                                                finish();
                                                progress.dismiss();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Throwable t) {
                                        Log.d("", "error: " + t.getMessage());
                                    }
                                });

                                break;
                            case 1:
                                Call<GetTotalEventpost> callEvent = apiService.getTotalEventPost(response.body().interests.get(i).id);

                                callEvent.enqueue(new Callback<GetTotalEventpost>() {
                                    @Override
                                    public void onResponse(Response<GetTotalEventpost> response, Retrofit retrofit) {

                                        if (modelManager.storeEventPostInRealm(response.body(), 0, user, getApplicationContext())) {
                                            downloadedPostsCounter++;

                                            if (downloadedPostsCounter == totalDownloadPosts) {
                                                finish();
                                                progress.dismiss();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Throwable t) {
                                        Log.d("", "error: " + t.getMessage());
                                    }
                                });

                                break;
                            case 2:
                                downloadedPostsCounter++;
                                if (downloadedPostsCounter == totalDownloadPosts) {
                                    Log.d("bla", "all cards fetched in poll");
                                    finish();
                                    progress.dismiss();
                                }
                                break;
                            case 3:
                                downloadedPostsCounter++;
                                if (downloadedPostsCounter == totalDownloadPosts) {
                                    Log.d("bla", "all cards fetched in album");
                                    finish();
                                    progress.dismiss();
                                }
                                break;
                            case 4:
                                downloadedPostsCounter++;
                                if (downloadedPostsCounter == totalDownloadPosts) {
                                    Log.d("bla", "all cards fetched in meetup");
                                    finish();
                                    progress.dismiss();
                                }
                                break;
                            default:
                                break;
                        }

                    }
                }

                if (response.body().own.size() > 0) {
                    int totalOwn = response.body().own.size();
                    for (int j = 0; j < totalOwn; j++) {
                        Log.d(TAG, "ownid: "+response.body().own.get(j).id);
                        Log.d(TAG, "owntype: "+response.body().own.get(j).type);
                        switch (response.body().own.get(j).type) {
                            case 0:

                                Call<GetTotalQuickpost> call = apiService.getTotalQuickPost(response.body().own.get(j).id);

                                call.enqueue(new Callback<GetTotalQuickpost>() {
                                    @Override
                                    public void onResponse(Response<GetTotalQuickpost> response, Retrofit retrofit) {

                                        if (modelManager.storeQuickPostInRealm(response.body(), 1, user, getApplicationContext())) {
                                            downloadedPostsCounter++;

                                            if (downloadedPostsCounter == totalDownloadPosts) {
                                                finish();
                                                progress.dismiss();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Throwable t) {
                                        Log.d("", "error: " + t.getMessage());
                                    }
                                });

                                break;
                            case 1:
                                Call<GetTotalEventpost> callEvent = apiService.getTotalEventPost(response.body().own.get(j).id);

                                callEvent.enqueue(new Callback<GetTotalEventpost>() {
                                    @Override
                                    public void onResponse(Response<GetTotalEventpost> response, Retrofit retrofit) {

                                        if (modelManager.storeEventPostInRealm(response.body(), 1, user, getApplicationContext())) {
                                            downloadedPostsCounter++;

                                            if (downloadedPostsCounter == totalDownloadPosts) {
                                                finish();
                                                progress.dismiss();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Throwable t) {
                                        Log.d("", "error: " + t.getMessage());
                                    }
                                });

                                break;
                            case 2:
                                downloadedPostsCounter++;
                                if (downloadedPostsCounter == totalDownloadPosts) {
                                    Log.d("bla", "all cards fetched in poll");
                                    finish();
                                    progress.dismiss();
                                }
                                break;
                            case 3:
                                downloadedPostsCounter++;
                                if (downloadedPostsCounter == totalDownloadPosts) {
                                    Log.d("bla", "all cards fetched in album");
                                    finish();
                                    progress.dismiss();
                                }
                                break;
                            case 4:
                                downloadedPostsCounter++;
                                if (downloadedPostsCounter == totalDownloadPosts) {
                                    Log.d("bla", "all cards fetched in meetup");
                                    finish();
                                    progress.dismiss();
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }


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
