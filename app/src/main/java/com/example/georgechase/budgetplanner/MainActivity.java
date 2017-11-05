package com.example.georgechase.budgetplanner;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.overviewViewPager);
        tabLayout = findViewById(R.id.tabLayoutOverview);

        TabLayout.Tab tabOne = tabLayout.newTab();
        tabOne.setText("Overview");
        tabLayout.addTab(tabOne);

        TabLayout.Tab tabTwo = tabLayout.newTab();
        tabTwo.setText("Goals");
        tabLayout.addTab(tabTwo);

        TabLayout.Tab tabThree = tabLayout.newTab();
        tabThree.setText("Charts");
        tabLayout.addTab(tabThree);

        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //tabLayout.setupWithViewPager(viewPager, true);
    }

    public void addNewGoal(View view) {
    }
}
