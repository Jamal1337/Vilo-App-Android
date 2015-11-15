package com.fabian.vilo;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fabian.vilo.models.CDModels.CDUser;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class Settings extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().setTitle("Settings");

        View view = inflater.inflate(R.layout.settings, container, false);

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
}