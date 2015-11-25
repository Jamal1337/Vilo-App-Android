package com.fabian.vilo;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.fabian.vilo.api.ViloApiAdapter;
import com.fabian.vilo.api.ViloApiEndpointInterface;
import com.fabian.vilo.custom_methods.Util;
import com.fabian.vilo.models.CDModels.CDComment;
import com.fabian.vilo.models.CDModels.CDLocation;
import com.fabian.vilo.models.CDModels.CDPoll;
import com.fabian.vilo.models.CDModels.CDPost;
import com.fabian.vilo.models.CDModels.CDUser;
import com.fabian.vilo.models.UpdatePosts;
import com.fabian.vilo.models.ViloResponse;
import com.facebook.login.LoginManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class Settings extends Fragment {

    private Switch interestsPush;
    private Switch ownPush;
    private CDUser user;
    private Boolean interestsPushStatus;
    private Boolean ownPushStatus;
    private Realm realm;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().setTitle("Settings");

        View view = inflater.inflate(R.layout.settings, container, false);

        realm = Realm.getInstance(getContext());

        // Build the query looking at all users:
        RealmQuery<CDUser> query = realm.where(CDUser.class);

        // Execute the query:
        RealmResults<CDUser> result = query.findAll();

        user = result.first();

        interestsPushStatus = (user.getInterests_push() != 0);
        ownPushStatus = (user.getOwn_push() != 0);

        interestsPush = (Switch) view.findViewById(R.id.interests_push_switch);
        ownPush = (Switch) view.findViewById(R.id.own_push_switch);

        interestsPush.setChecked(interestsPushStatus);
        ownPush.setChecked(ownPushStatus);

        interestsPush.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                interestsPushStatus = !interestsPushStatus;
                interestsPush.setChecked(interestsPushStatus);

                if (new Util().isNetworkAvailable(getContext()) == true) {

                    ViloApiAdapter viloAdapter = ViloApiAdapter.getInstance(getContext());

                    ViloApiEndpointInterface apiService = viloAdapter.mApi;

                    final Integer pushStatus = (interestsPushStatus) ? 1 : 0;
                    Map<String, String> pushArray = new HashMap<String, String>();
                    pushArray.put("push_type", "interests_push");
                    pushArray.put("push_value", ""+pushStatus);


                    Call<ViloResponse> call = apiService.updatePushSettings(user.getUserid(), pushArray);

                    call.enqueue(new Callback<ViloResponse>() {
                        @Override
                        public void onResponse(Response<ViloResponse> response, Retrofit retrofit) {

                            if (response.code() == 200) {
                                realm.beginTransaction();

                                user.setInterests_push(pushStatus);

                                realm.commitTransaction();
                            } else {
                                interestsPushStatus = !interestsPushStatus;
                                interestsPush.setChecked(interestsPushStatus);
                            }
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            interestsPushStatus = !interestsPushStatus;
                            interestsPush.setChecked(interestsPushStatus);
                        }
                    });

                }
            }
        });

        ownPush.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ownPushStatus = !ownPushStatus;
                ownPush.setChecked(ownPushStatus);

                if (new Util().isNetworkAvailable(getContext()) == true) {

                    ViloApiAdapter viloAdapter = ViloApiAdapter.getInstance(getContext());

                    ViloApiEndpointInterface apiService = viloAdapter.mApi;

                    final Integer pushStatus = (ownPushStatus) ? 1 : 0;
                    Map<String, String> pushArray = new HashMap<String, String>();
                    pushArray.put("push_type", "own_push");
                    pushArray.put("push_value", ""+pushStatus);


                    Call<ViloResponse> call = apiService.updatePushSettings(user.getUserid(), pushArray);

                    call.enqueue(new Callback<ViloResponse>() {
                        @Override
                        public void onResponse(Response<ViloResponse> response, Retrofit retrofit) {

                            if (response.code() == 200) {
                                realm.beginTransaction();

                                user.setOwn_push(pushStatus);

                                realm.commitTransaction();
                            } else {
                                ownPushStatus = !ownPushStatus;
                                ownPush.setChecked(ownPushStatus);
                            }
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            ownPushStatus = !ownPushStatus;
                            ownPush.setChecked(ownPushStatus);
                        }
                    });

                }

            }
        });

        // 2nd part Web Views

        Button ppButton = (Button) view.findViewById(R.id.privacypolicyBtn);
        Button tosButton = (Button) view.findViewById(R.id.termsofserviceBtn);
        Button impButton = (Button) view.findViewById(R.id.imprintBtn);

        ppButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Tabbar) getActivity()).findViewById(R.id.tab_layout).setVisibility(View.GONE);
                ((Tabbar) getActivity()).findViewById(R.id.fab).setVisibility(View.GONE);

                WebViewController webView = new WebViewController();
                webView.setTitle(getContext().getResources().getText(R.string.privacypolicy).toString());
                webView.setOldTitle(getContext().getResources().getText(R.string.settings).toString());
                webView.setUrl(getContext().getResources().getText(R.string.privacypolicyurl).toString());

                FragmentManager manager = ((Tabbar)getActivity()).getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.addToBackStack(null);
                transaction.hide(Settings.this);
                transaction.replace(R.id.main_layout, webView);
                transaction.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.commit();
            }
        });

        tosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Tabbar) getActivity()).findViewById(R.id.tab_layout).setVisibility(View.GONE);
                ((Tabbar) getActivity()).findViewById(R.id.fab).setVisibility(View.GONE);

                WebViewController webView = new WebViewController();
                webView.setTitle(getContext().getResources().getText(R.string.termsofservice).toString());
                webView.setOldTitle(getContext().getResources().getText(R.string.settings).toString());
                webView.setUrl(getContext().getResources().getText(R.string.termsofserviceurl).toString());

                FragmentManager manager = ((Tabbar)getActivity()).getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.addToBackStack(null);
                transaction.hide(Settings.this);
                transaction.replace(R.id.main_layout, webView);
                transaction.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.commit();
            }
        });

        impButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Tabbar) getActivity()).findViewById(R.id.tab_layout).setVisibility(View.GONE);
                ((Tabbar) getActivity()).findViewById(R.id.fab).setVisibility(View.GONE);

                WebViewController webView = new WebViewController();
                webView.setTitle(getContext().getResources().getText(R.string.imprint).toString());
                webView.setOldTitle(getContext().getResources().getText(R.string.settings).toString());
                webView.setUrl(getContext().getResources().getText(R.string.imprinturl).toString());

                FragmentManager manager = ((Tabbar)getActivity()).getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.addToBackStack(null);
                transaction.hide(Settings.this);
                transaction.replace(R.id.main_layout, webView);
                transaction.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.commit();
            }
        });

        Button logoutBtn = (Button) view.findViewById(R.id.logoutBtn);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.logoutTitle)
                        .setMessage(R.string.logoutText)
                        .setNegativeButton(android.R.string.cancel, null) // dismisses by default
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override public void onClick(DialogInterface dialog, int which) {
                                // do the acknowledged action, beware, this is run on UI thread
                                if (clearDB()) {

                                    LoginManager.getInstance().logOut();

                                    SharedPreferences sharedpreferences = getContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.remove("loggedin");
                                    editor.apply();

                                    Intent intent = new Intent(getActivity(), Login.class);
                                    startActivity(intent);
                                }
                            }
                        })
                        .create()
                        .show();
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        Realm realm = Realm.getInstance(getContext());

        // Build the query looking at all users:
        RealmQuery<CDUser> query = realm.where(CDUser.class);

        // Execute the query:
        RealmResults<CDUser> result = query.findAll();

        getActivity().setTitle(result.first().getFirst_name());

        ((Tabbar) getActivity()).findViewById(R.id.tab_layout).setVisibility(View.VISIBLE);
        ((Tabbar) getActivity()).findViewById(R.id.fab).setVisibility(View.VISIBLE);

        FragmentManager manager = ((Tabbar) getActivity()).getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.remove(this).commit();

    }

    public boolean clearDB() {
        realm.beginTransaction();
        realm.clear(CDUser.class);
        realm.clear(CDComment.class);
        realm.clear(CDLocation.class);
        realm.clear(CDPoll.class);
        realm.clear(CDPost.class);
        realm.commitTransaction();

        return true;
    }
}