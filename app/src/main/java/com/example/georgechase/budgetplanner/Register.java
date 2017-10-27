package com.example.georgechase.budgetplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by GeorgeChase on 2017-10-23.
 */

public class Register extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        setTitle("Registration");
    }

    public void initializeProfile(View view) {
        Intent i = new Intent(this, FirstTime.class);
        //handle name data, add extras to bundle/intent
        startActivity(i);
    }
}
