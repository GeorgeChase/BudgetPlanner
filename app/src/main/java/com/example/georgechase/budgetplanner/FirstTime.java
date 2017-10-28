package com.example.georgechase.budgetplanner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by GeorgeChase on 2017-10-23.
 */

public class FirstTime extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firsttime);
        setTitle("First Time Registration - Profile Settings");
    }

    public void finishRegistration(View view) {
    }
}
