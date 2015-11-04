package com.fabian.vilo;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Fabian on 29/10/15.
 */
public class CustomViewPager extends ViewPager {
    private boolean swipeable = true;

    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // Call this method in your motion events when you want to disable or enable
    // It should work as desired.
    public void setSwipeable(boolean swipeable) {
        this.swipeable = swipeable;
    }

    // TODO: evtl. wieder einkommentieren und so setzen dass nur bei detail der swipe deaktiviert ist

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        return false;//(this.swipeable) ? super.onInterceptTouchEvent(arg0) : false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Never allow swiping to switch between pages
        return false;
    }

}