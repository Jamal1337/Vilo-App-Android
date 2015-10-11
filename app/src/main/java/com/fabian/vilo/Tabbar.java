package com.fabian.vilo;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;

import android.widget.TableLayout;
import android.widget.TextView;

import android.app.FragmentTransaction;

import android.app.AlertDialog;
import android.view.WindowManager;

import android.view.animation.AnimationUtils;
import android.view.animation.Animation;

public class Tabbar extends AppCompatActivity {

    private static final String TAG = Tabbar.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbar);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        FloatingActionButton uploadBtn = (FloatingActionButton) findViewById(R.id.fab);

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "btn pressed");


                Intent i = new Intent(Tabbar.this, Quickpost.class);
                startActivity(i);

                /*FragmentTransaction fragmenttransaction = getFragmentManager().beginTransaction();
                //fragmenttransaction.setCustomAnimations(0x7f040000, 0, 0, 0x7f040001);
                fragmenttransaction.replace(R.layout.activity_around_me, Quickpost.newInstance(), "postFragment");
                fragmenttransaction.addToBackStack(null);
                fragmenttransaction.commit();*/


                /*final AlertDialog dialog = new AlertDialog
                        .Builder(Tabbar.this)
                        .setTitle("GEILE APP DIGGA")
                        .setPositiveButton("Close", null)
                        .create();
                dialog.getWindow().getAttributes().dimAmount = 0.5f;
                dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

                dialog.show();*/

            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Around Me"));
        tabLayout.addTab(tabLayout.newTab().setText("Me"));
        //tabLayout.addTab(tabLayout.newTab().setText("Tab 3"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabTextColors(getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorAccent));
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorAccent));

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d(TAG, "value = " + tab.getPosition());

                if (tab.getPosition() == 0) {
                    getSupportActionBar().setTitle("Around Me");
                } else {
                    getSupportActionBar().setTitle("Me");
                }


                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tabbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
