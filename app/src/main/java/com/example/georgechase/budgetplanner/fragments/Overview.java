package com.example.georgechase.budgetplanner.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.georgechase.budgetplanner.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Overview extends Fragment {

    ListView transList;
    ListView budgetList;
    FirebaseDatabase database;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        database = FirebaseDatabase.getInstance();
        //TODO: Revamp overview to implement database items into Listview
        View view = inflater.inflate(R.layout.fragment_overview, container, false);

        //List stubs for recent transactions, upcoming bills, and budgets
        String transactionList[] = {"Transaction 1", "Transaction 2", "Transaction 3"};
        transList = view.findViewById(R.id.recentTransList);

        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(
                getActivity().getBaseContext(),
                android.R.layout.simple_list_item_1,
                transactionList
        );

        transList.setAdapter(arrayAdapter1);

        String listOfBudgets[] = {"Entertainment", "Food", "Clothing"};
        budgetList = view.findViewById(R.id.budgetList);

        ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<>(
                getActivity().getBaseContext(),
                android.R.layout.simple_list_item_1,
                listOfBudgets
        );

        budgetList.setAdapter(arrayAdapter3);




        return view;
    }
}