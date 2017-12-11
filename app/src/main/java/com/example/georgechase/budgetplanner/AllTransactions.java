package com.example.georgechase.budgetplanner;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.georgechase.budgetplanner.models.Goal;
import com.example.georgechase.budgetplanner.models.Transaction;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllTransactions extends AppCompatActivity {

    private static final String TAG = "Transactions";
    private DatabaseReference reference;
    private ArrayList<Transaction> transList;
    private boolean firstInit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reference = FirebaseDatabase.getInstance().getReference();
        setContentView(R.layout.activity_all_transactions);
    }

    @Override
    public void onResume() {
        super.onResume();
        queryForAllTransactions();
    }

    private void insertTransactionToTable(String date, String categoryData, String amtReq) {

        final TableLayout transTable = findViewById(R.id.allTransTable);
        int count = transTable.getChildCount();

        TableRow row = new TableRow(this);
        if (count % 2 != 1) {
            row.setBackgroundColor(getResources().getColor(R.color.colorAccentLight));
        }
        row.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));

        //ID convention: Date = 10X, Category = 20X, Amount Required = 30X; where X = count.
        TextView theDate = new TextView(this);
        theDate.setId(100 + count);
        theDate.setText(date);
        theDate.setTextSize(18);
        theDate.setPadding(10, 10, 35, 10);

        TextView category = new TextView(this);
        category.setId(200 + count);
        category.setText(categoryData);
        category.setTextSize(18);
        category.setPadding(125, 10, 35, 10);

        TextView amtRequired = new TextView(this);
        amtRequired.setId(300 + count);
        amtRequired.setText(amtReq);
        amtRequired.setTextSize(18);
        amtRequired.setPadding(35, 10, 35, 10);
        amtRequired.setGravity(Gravity.END);

        row.addView(theDate);
        row.addView(category);
        row.addView(amtRequired);

        transTable.addView(row);
    }

    private void queryForAllTransactions() {
        //Queries the list of goals for current user.
        Query getTrans = reference.child("transactions")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        getTrans.addListenerForSingleValueEvent(new ValueEventListener() {
            ArrayList<Transaction> tempTransList = new ArrayList<>();
            int prevCount;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot goalSnap : dataSnapshot.getChildren()) {
                    tempTransList.add(goalSnap.getValue(Transaction.class));
                }

                if (firstInit) {
                    prevCount = transList.size();
                }

                transList = tempTransList;

                Log.d(TAG, "onDataChange: prevCount= " + prevCount + " | tempTransSize= " + tempTransList.size());
                if (!firstInit) {
                    initTransactionTable();
                    firstInit = true;
                } else if (prevCount < tempTransList.size()) {
                    addNewTransaction();
                }
                //Might need to add another else if depending on how deleting/editing a goal works

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onDataChange: (onCancelled Method)");
            }

            private void addNewTransaction() {
                int lastIndex = transList.size() - 1;
                Transaction trans = transList.get(lastIndex);
                insertTransactionToTable(trans.getDate(), trans.getItemName(), trans.getAmount());
            }

            private void initTransactionTable() {
                for (int i = 0; i < transList.size(); i++) {
                    Transaction trans = transList.get(i);
                    Log.d(TAG, "onDataChange: (ADDING GOAL) found goal: " + trans.toString());
                    insertTransactionToTable(trans.getDate(), trans.getItemName(), trans.getAmount());
                }
            }
        });

    }
}