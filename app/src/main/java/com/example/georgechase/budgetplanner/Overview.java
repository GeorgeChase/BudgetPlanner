package com.example.georgechase.budgetplanner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Overview extends Fragment {

    ListView transList;
    ListView billsList;
    ListView budgetList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_overview, container, false);

        String transactionList[] = {"Transaction 1", "Transaction 2", "Transaction 3"};
        transList = view.findViewById(R.id.recentTransList);

        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(
                getActivity().getBaseContext(),
                android.R.layout.simple_list_item_1,
                transactionList
        );

        transList.setAdapter(arrayAdapter1);

        String listOfBills[] = {"Bill 1", "Bill 2", "Bill 3"};
        billsList = view.findViewById(R.id.billsList);

        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(
                getActivity().getBaseContext(),
                android.R.layout.simple_list_item_1,
                listOfBills
        );

        billsList.setAdapter(arrayAdapter2);

        String listOfBudgets[] = {"Budget 1", "Budget 2", "Budget 3"};
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