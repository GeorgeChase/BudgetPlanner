package com.example.georgechase.budgetplanner.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.georgechase.budgetplanner.Camera;
import com.example.georgechase.budgetplanner.ConfirmTransaction;
import com.example.georgechase.budgetplanner.R;
import com.example.georgechase.budgetplanner.models.Transaction;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class Overview extends Fragment {

    private DatabaseReference reference;
    private ListView transList;
    private ListView budgetList;
    private ArrayList<Transaction> transactionList;
    private String[] list;
    private View view;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        reference = FirebaseDatabase.getInstance().getReference();
        view = inflater.inflate(R.layout.fragment_overview, container, false);
        checkFirstRun();
        queryForTransactions();
        return view;
    }

    public void checkFirstRun() {
        boolean isFirstRun = this.getActivity().getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstRun", true);
        if (isFirstRun){
            // Place your dialog code here to display the dialog

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Enter Budget Amount");

            final EditText input = new EditText(getActivity());
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
            builder.setView(input);

            input.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                private String current = "";
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(!s.toString().equals(current)){
                        input.removeTextChangedListener(this);

                        String cleanString = s.toString().replaceAll("[$,.]", "");

                        double parsed = Double.parseDouble(cleanString);
                        String formatted = NumberFormat.getCurrencyInstance().format((parsed/100));

                        current = formatted;
                        input.setText(formatted);
                        input.setSelection(formatted.length());

                        input.addTextChangedListener(this);
                    }
                }
                @Override
                public void afterTextChanged(Editable editable) {
                }
            });


            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String temp = input.getText().toString();
                    reference.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("balance").setValue(temp);

                }
            });

            builder.show();

            this.getActivity().getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .edit()
                    .putBoolean("isFirstRun", false)
                    .apply();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void queryForTransactions() {
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
                list = new String[transactionList.size()];
                setupList();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void setupList() {
        for (int i = 0; i < transactionList.size(); i++) {
            Transaction tran = transactionList.get(i);
            String name = tran.getItemName();
            String amount = tran.getAmount();

            list[i] = name + "          " + amount;
        }

        transList = view.findViewById(R.id.recentTransList);

        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(
                getActivity().getBaseContext(),
                android.R.layout.simple_list_item_1,
                list
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
    }
}