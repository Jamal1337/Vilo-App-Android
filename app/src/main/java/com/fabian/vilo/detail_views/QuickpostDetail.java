package com.fabian.vilo.detail_views;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.app.ListFragment;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fabian.vilo.R;
import com.fabian.vilo.Tabbar;
import com.fabian.vilo.adapters.DetailCorePostAdapter;
import com.fabian.vilo.adapters.DetailPostAdapter;
import com.fabian.vilo.around_me_screen.Card;
import com.fabian.vilo.me_screen.Me;
import com.fabian.vilo.models.CDModels.CDPost;
import com.fabian.vilo.models.CDModels.CDUser;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by Fabian on 25/10/15.
 */
public class QuickpostDetail extends Fragment {

    private String title;
    private Card card;
    private CDPost post;
    private ListView myListView;
    private DetailCorePostAdapter adapter;
    private DetailPostAdapter plainAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.quickpost_detail, container,
                false);


        // TODO: kommentare laden und übergeben an Adapter, dort abfragen ob runde 0, dann main cell nehmen sonst comment cell!

        if (card != null) {
            getActivity().setTitle(card.title);

            myListView = (ListView) view.findViewById(R.id.detailistView);
            ArrayList<Card> listViewPosts = new ArrayList<Card>();
            listViewPosts.add(card);
            plainAdapter = new DetailPostAdapter(getContext(), R.layout.quickpost_detail, listViewPosts);
            myListView.setAdapter(plainAdapter);
        } else {
            getActivity().setTitle(post.getTitle());

            myListView = (ListView) view.findViewById(R.id.detailistView);
            ArrayList<CDPost> listViewPosts = new ArrayList<CDPost>();
            listViewPosts.add(post);
            adapter = new DetailCorePostAdapter(getContext(), R.layout.quickpost_detail, listViewPosts);
            myListView.setAdapter(adapter);
        }

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        getActivity().setTitle(title);

        ((Tabbar) getActivity()).findViewById(R.id.tab_layout).setVisibility(View.VISIBLE);
        ((Tabbar) getActivity()).findViewById(R.id.fab).setVisibility(View.VISIBLE);

        FragmentManager manager = ((Tabbar)getActivity()).getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.remove(this).commit();

    }

    /**
     * Getter
     */

    public String getTitle() {
        return title;
    }

    public Card getCard() {
        return card;
    }

    public CDPost getPost() {
        return post;
    }

    /**
     * Setter
     */

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public void setPost(CDPost post) {
        this.post = post;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        //menu.findItem(0).setVisible(false).setEnabled(false);
    }

    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.add("Menu 2").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        inflater.inflate(R.menu.menu_tabbar, menu);
    }*/
}