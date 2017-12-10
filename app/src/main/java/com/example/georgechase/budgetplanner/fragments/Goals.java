package com.example.georgechase.budgetplanner.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.georgechase.budgetplanner.GoalDetails;
import com.example.georgechase.budgetplanner.R;
import com.example.georgechase.budgetplanner.models.Goal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Goals extends Fragment {
    private static final String TAG = "Goals";
    private View rootView;
    private DatabaseReference reference;
    private ArrayList<Goal> goalList;
    private boolean firstInit;

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

    @Override
    public void onResume(){
        super.onResume();
        queryForAllGoals();
    }

    private void insertGoalToTable(String date, String categoryData, String amtReq){

        final TableLayout goalTable = rootView.findViewById(R.id.goalsTable);
        int count = goalTable.getChildCount();

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
        theDate.setTextSize(18);
        theDate.setPadding(10,10,35,10);

        TextView category = new TextView(getActivity());
        category.setId(200 + count);
        category.setText(categoryData);
        category.setTextSize(18);
        category.setPadding(125,10,35,10);

        TextView amtRequired = new TextView(getActivity());
        amtRequired.setId(300 + count);
        amtRequired.setText(amtReq);
        amtRequired.setTextSize(18);
        amtRequired.setPadding(35,10,35,10);
        amtRequired.setGravity(Gravity.END);

        row.setId(count);
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goalDetails(v, goalTable);
            }
        });
        row.addView(theDate);
        row.addView(category);
        row.addView(amtRequired);

        goalTable.addView(row);
    }

    private void goalDetails(View v, TableLayout table) {

        String date = "";
        String category = "";
        String amountRequired = "";

        int goalRowId = v.getId();
        View goalRow = table.getChildAt(goalRowId);
        if (goalRow instanceof TableRow) {
            View d = ((TableRow) goalRow).getChildAt(0);
            TextView d2 = (TextView)d;
            date = d2.getText().toString();

            View c = ((TableRow) goalRow).getChildAt(1);
            TextView c2 = (TextView)c;
            category = c2.getText().toString();

            View a = ((TableRow) goalRow).getChildAt(2);
            TextView a2 = (TextView)a;
            amountRequired = a2.getText().toString();
        }

        Intent i = new Intent(getActivity(), GoalDetails.class);
        i.putExtra("date", date);
        i.putExtra("category", category);
        i.putExtra("amountRequired", amountRequired);
        startActivity(i);


        //Toast.makeText(getActivity(), "OnClick works: " + goalId,  Toast.LENGTH_SHORT).show();
    }

    private void addNewGoal() {
        int lastIndex = goalList.size() - 1;
        Goal goal = goalList.get(lastIndex);
        insertGoalToTable(goal.getDate(), goal.getCategory(), goal.getRequired_amount());
    }

    private void queryForAllGoals() {
        //Queries the list of goals for current user.
        Query getGoals = reference.child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("goals");
        getGoals.addListenerForSingleValueEvent(new ValueEventListener() {
            ArrayList<Goal> tempGoalList = new ArrayList<>();
            int prevCount;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot goalSnap : dataSnapshot.getChildren()) {
                    tempGoalList.add(goalSnap.getValue(Goal.class));
                }

                if (firstInit) {
                    prevCount = goalList.size();
                }

                goalList = tempGoalList;

                Log.d(TAG, "onDataChange: prevCount= " + prevCount + " | tempGoalSize= " + tempGoalList.size());
                if (!firstInit) {
                    initGoalTable();
                    firstInit = true;
                }
                else if (prevCount < tempGoalList.size()) {
                    addNewGoal();
                }
                //Might need to add another else if depending on how deleting/editing a goal works

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onDataChange: (onCancelled Method)");
            }
        });
    }

    private void initGoalTable() {
        for (int i = 0; i < goalList.size(); i++) {
            Goal goal = goalList.get(i);
            Log.d(TAG, "onDataChange: (ADDING GOAL) found goal: " + goal.toString());
            insertGoalToTable(goal.getDate(), goal.getCategory(), goal.getRequired_amount());
        }
    }

    public void resetTable() {
        firstInit = false;
    }
}
