package com.example.georgechase.budgetplanner.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.georgechase.budgetplanner.R;
import com.example.georgechase.budgetplanner.models.Goal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Goals extends Fragment {
    private static final String TAG = "Goals";
    private View rootView;
    private int numGoals;
    private DatabaseReference reference;
    private boolean firstInit  = false;
    private int count;

    public Goals(){
        // Empty Constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        reference = FirebaseDatabase.getInstance().getReference();
        rootView = inflater.inflate(R.layout.fragment_goals, container, false);
        return rootView;
    }

    private void initUserGoalData() {
        //Loops through goals and adds them to the current goals table
        for (int i = 1; i <= numGoals; i++) {
            Query getGoals = reference.child("users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("goals")
                    .orderByKey()
                    .equalTo("goal " + i);
            getGoals.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                        Goal goal = singleSnapshot.getValue(Goal.class);
                        Log.d(TAG, "onDataChange: (QUERY FOR GOALS) found goal: " + goal.toString());

                        insertGoalToTable(goal.getDate(), goal.getCategory(), goal.getRequired_amount());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
    private void insertGoalToTable(String date, String categoryData, String amtReq){

        TableLayout goalTable = rootView.findViewById(R.id.goalsTable);
         count = goalTable.getChildCount();

        TableRow row = new TableRow(getActivity());
        if (count % 2 != 1) {
            row.setBackgroundColor(getResources().getColor(R.color.colorAccentLight));
        }
        row.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));

        //ID convention: Date = 10X, Category = 20X, Amount Required = 30X; where X = count.
        TextView theDate = new TextView(getActivity());
        theDate.setId(100 + count);
        theDate.setText(date);
        theDate.setPadding(5, 0, 35, 0);

        TextView category = new TextView(getActivity());
        category.setId(200 + count);
        category.setText(categoryData);
        category.setPadding(75, 0, 35, 0);

        TextView amtRequired = new TextView(getActivity());
        String amount = getString(R.string.money_sign) + amtReq;
        amtRequired.setId(300 + count);
        amtRequired.setText(amount);
        amtRequired.setPadding(75, 0, 35, 0);

        row.addView(theDate);
        row.addView(category);
        row.addView(amtRequired);

        goalTable.addView(row);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateNumGoalsCount();
        Log.d(TAG, "onDataChange: AFTER - Current Goals Count = " + count + " | NumGoals Counter = " + numGoals);
        if (numGoals != count) {
            addNewGoal();
        }
    }

    private void addNewGoal() {
        Query getGoals = reference.child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("goals")
                .orderByKey()
                .equalTo("goal " + numGoals);
        Log.d(TAG, "onDataChange: INSIDE ADD NEW GOAL - Current Goals Count = " + count + " | NumGoals Counter = " + numGoals);
        getGoals.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Goal goal = singleSnapshot.getValue(Goal.class);
                    Log.d(TAG, "onDataChange: (ADDING GOAL) found goal: " + goal.toString());

                    insertGoalToTable(goal.getDate(), goal.getCategory(), goal.getRequired_amount());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateNumGoalsCount() {
        //Gets the count for number of goals
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("goals")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int numOfGoals = (int) dataSnapshot.getChildrenCount();
                        setNumGoalsCount(numOfGoals);

                        if (!firstInit) {
                            initUserGoalData();
                            firstInit = true;
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

    }
    private void setNumGoalsCount(int numOfGoals) {
        this.numGoals = numOfGoals;
        Log.d(TAG, "onDataChange: UPDATER COUNT = " + numGoals);
    }
}
