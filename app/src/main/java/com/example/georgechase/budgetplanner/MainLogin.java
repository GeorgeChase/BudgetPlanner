package com.example.georgechase.budgetplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by GeorgeChase on 2017-10-23.
 */


public class MainLogin extends AppCompatActivity {

     @Override
     protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.main_login);
          setTitle("Welcome!");
     }

     public void login(View view) {
          Intent i = new Intent(MainLogin.this, Overview.class);

          //verify if user and pass match the pair saved on device

          startActivity(i);
     }

     public void signUp(View view) {
          Intent i = new Intent(MainLogin.this, Register.class);
          startActivity(i);
     }
}
