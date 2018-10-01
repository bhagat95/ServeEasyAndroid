package com.example.bhagat.finalyear;

import android.support.v4.app.Fragment;

/**
 * Created by Shashank on 07-02-2017.
 */

public class PagerItem {
    private String mTitle;
    private Fragment mFragment;
    public PagerItem(String title, Fragment fragment) {
        mTitle = title;
        mFragment = fragment;
    }
    public String getTitle() {
        return mtitle;
    }
    public Fragment getFragment() {
        return mFragment;
    }
    public void setTitle(String title) {
        mTitle = title;
    }
    public void setFragment(Fragment fragment) {
        mFragment = fragment;
    }
}
