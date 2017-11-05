package com.example.georgechase.budgetplanner;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

//Used for managing tabs in the Main Activity

public class PagerAdapter extends FragmentStatePagerAdapter {
    private int numTabs;

    PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.numTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new Overview();
            case 1:
                return new Goals();
            case 2:
                return new Charts();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numTabs;
    }
}
