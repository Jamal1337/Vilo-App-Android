package com.fabian.vilo;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.util.Log;

import com.fabian.vilo.adapters.PagerAdapter;
import com.fabian.vilo.upload_views.NewQuickpost;
import com.fabian.vilo.upload_views.Quickpost;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import java.net.CookieHandler;
import java.net.CookiePolicy;

public class Tabbar extends AppCompatActivity {

    private static final String TAG = Tabbar.class.getSimpleName();
    private TabLayout tabLayout;
    Boolean isVisible = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbar);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        FacebookSdk.sdkInitialize(this);

        // enable cookies
        java.net.CookieManager cookieManager = new java.net.CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookieManager);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Around Me"));
        tabLayout.addTab(tabLayout.newTab().setText("Me"));
        //tabLayout.addTab(tabLayout.newTab().setText("Tab 3"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabTextColors(getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorAccent));
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorAccent));

        final ViewPager viewPager = (CustomViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        FloatingActionButton uploadBtn = (FloatingActionButton) findViewById(R.id.fab);

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // load quickpost upload

                NewQuickpost newQuickpost = new NewQuickpost();
                Fragment fragment = newQuickpost;

                getSupportActionBar().hide();

                isVisible = false;

                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction(); //getParentFragment().getFragmentManager().beginTransaction();// manager.beginTransaction();
                transaction.addToBackStack(null);
                transaction.replace(R.id.main_layout, fragment); // newInstance() is a static factory method.
                transaction.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.commit();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        /*menu.add("Share")
                .setIcon(android.R.drawable.ic_menu_share)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);*/

        getMenuInflater().inflate(R.menu.menu_tabbar, menu);
        return true;
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    public Boolean getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(Boolean isVisible) {
        this.isVisible = isVisible;
    }
}
