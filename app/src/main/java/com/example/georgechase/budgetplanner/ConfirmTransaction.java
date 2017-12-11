package com.example.georgechase.budgetplanner;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.georgechase.budgetplanner.models.Transaction;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ConfirmTransaction extends AppCompatActivity {
    private String itemName;
    private String date;
    private String amount;
    private int count;
    private static final String TAG = "Confirm Transaction";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_transaction);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            itemName = extras.getString("itemName");
            date = extras.getString("date");
            amount = extras.getString("amount");

            setTextViewStrings();
            queryForTransactionCount();
        }
    }

    private void setTextViewStrings() {
        TextView itemNameView = findViewById(R.id.item_placeholder_confirm_transaction);
        itemNameView.setText(itemName);

        TextView dateView = findViewById(R.id.date_placeholder_confirm_transaction);
        dateView.setText(date);

        TextView amountView = findViewById(R.id.amount_placeholder_confirm_transaction);
        amountView.setText(amount);
    }


    public void submitConfirmedTransaction(View view) {
        Transaction trans = new Transaction();
        trans.setDate(date);
        trans.setAmount(amount);
        trans.setItemName(itemName);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("transactions");

        DatabaseReference transRef = ref.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        DatabaseReference pushedTransRef = transRef.push();
        String transID = pushedTransRef.getKey();

        Toast.makeText(getApplicationContext(), "Transaction has successfully been added.",  Toast.LENGTH_SHORT).show();
        ManualTransaction.getInstance().finish();
        goToAllTransactions();
    }

    private void goToAllTransactions() {
        Intent intent = new Intent(ConfirmTransaction.this, AllTransactions.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void queryForTransactionCount() {
        //Gets the count for number of total transactions that the user has
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("transactions")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int numOfTransactions = (int) dataSnapshot.getChildrenCount();
                        Log.d(TAG, "onCT: (QUERY FOR TRANS COUNT) = " + numOfTransactions);
                        setNumTransactionCount(numOfTransactions);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    private void setNumTransactionCount(int numTransactions) {
        this.count = numTransactions;
        count++;
        Log.d(TAG, "onCT: AFTER COUNT = " + count);
    }

    public void goBack(View view) {
        this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
        this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK));
    }
}
