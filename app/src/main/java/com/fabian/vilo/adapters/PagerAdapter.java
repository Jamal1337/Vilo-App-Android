package com.fabian.vilo.adapters;

/**
 * Created by Fabian on 10/10/15.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.fabian.vilo.around_me_screen.AroundMe;
import com.fabian.vilo.me_screen.Me;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                AroundMe tab1 = new AroundMe();
                return tab1;
            case 1:
                Me tab2 = new Me();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
