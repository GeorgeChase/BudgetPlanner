package com.example.georgechase.budgetplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Toolbar mTopToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTopToolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(mTopToolbar);

        viewPager = findViewById(R.id.overviewViewPager);
        tabLayout = findViewById(R.id.overviewTabLayout);

        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setupWithViewPager(viewPager, true);
        tabLayout.bringToFront();
    }

    public void addNewGoal(View view) {
        Intent i = new Intent(MainActivity.this, NewGoal.class);
        startActivity(i);
    }

    @Override
    protected void onResume () {
        super.onResume();
        if (viewPager != null && viewPager.getAdapter() != null) {
            viewPager.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        if (id == R.id.action_camera) {
            Intent i = new Intent(MainActivity.this, Camera.class);
            startActivity(i);
            return true;
        }
        else if (id ==R.id.action_more_options) {
            View moreOptions = findViewById(R.id.action_more_options);
            PopupMenu popup = new PopupMenu(this, moreOptions);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.overflow_more_options, popup.getMenu());
            popup.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void goToManualTransaction(MenuItem item) {
        Intent i = new Intent(MainActivity.this, ManualTransaction.class);
        startActivity(i);
    }

    public void goToViewAllTransactions(MenuItem item) {
        //TODO: Implement View All Transactions Activity (See all transactions for user)
    }

    public void goToPreferences(MenuItem item) {
        //TODO: Implement Preferences (Change/update user's name, e-mail, etc.)
    }

    public void signOut(MenuItem item) {
        FirebaseAuth.getInstance().signOut();
        finish();
    }
}
