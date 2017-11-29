package com.example.georgechase.budgetplanner;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.georgechase.budgetplanner.fragments.Charts;
import com.example.georgechase.budgetplanner.fragments.Goals;
import com.example.georgechase.budgetplanner.fragments.Overview;

//Used for managing tabs in the Main Activity

public class PagerAdapter extends FragmentStatePagerAdapter {
    private String[] tabTitles = new String[]{"Overview", "Goals", "Charts"};

    PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
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
        return tabTitles.length;
    }
}
