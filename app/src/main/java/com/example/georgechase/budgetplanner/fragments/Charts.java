package com.example.georgechase.budgetplanner.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.georgechase.budgetplanner.R;
import com.example.georgechase.budgetplanner.models.Transaction;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Charts extends Fragment {
    private View view;
    private ArrayList<Transaction> transactionList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_charts, container, false);

        DatabaseReference getTransactions = FirebaseDatabase.getInstance().getReference()
                .child("transactions")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        getTransactions.addListenerForSingleValueEvent(new ValueEventListener() {
            ArrayList<Transaction> tempList = new ArrayList<>();
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot transactionSnap : dataSnapshot.getChildren()){
                    tempList.add(transactionSnap.getValue(Transaction.class));
                }
                transactionList = tempList;
                initGraph(tempList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });



        return view;
    }

    private void initGraph(ArrayList<Transaction> list) {
        GraphView graph = view.findViewById(R.id.graph);

        DataPoint[] data = new DataPoint[list.size()];
        for (int i = 0; i < list.size(); i++) {
            Transaction trans = list.get(i);
            String date = trans.getDate();
            String amt = trans.getAmount();

            amt = amt.substring(1);
            double amount = Double.parseDouble(amt);

            String[] parts = date.split("/");
            double dateNum = Double.parseDouble(parts[1]);

            DataPoint dp = new DataPoint(dateNum, amount);
            data[i] = dp;
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(data);
        series.setColor(getResources().getColor(R.color.colorPrimary));
        graph.addSeries(series);

        //graph.getViewport().setScalable(true);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(200);
        graph.getGridLabelRenderer().setHumanRounding(true);

       /* PointsGraphSeries<DataPoint> series2 = new PointsGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 3 ),
                new DataPoint(2, 7)
        });
        graph.addSeries(series2); */

        graph.setTitle("Transactions");
    }
}
