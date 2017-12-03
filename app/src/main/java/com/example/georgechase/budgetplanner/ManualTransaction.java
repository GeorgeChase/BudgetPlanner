package com.example.georgechase.budgetplanner;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ManualTransaction extends AppCompatActivity{

    private static ManualTransaction manualTransaction;
    private EditText itemName;
    private EditText dateET;
    private EditText amountET;
    private Calendar cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_transaction);
        manualTransaction = this;

        itemName = findViewById(R.id.itemET_manual_transaction);
        dateET= findViewById(R.id.dateET_manual_transaction);
        amountET = findViewById(R.id.amountET_manual_transaction);

        //Formats numbers into currency as the user inputs
        amountET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            private String current = "";
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals(current)){
                    amountET.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[$,.]", "");

                    double parsed = Double.parseDouble(cleanString);
                    String formatted = NumberFormat.getCurrencyInstance().format((parsed/100));

                    current = formatted;
                    amountET.setText(formatted);
                    amountET.setSelection(formatted.length());

                    amountET.addTextChangedListener(this);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        cal = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, monthOfYear);
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        dateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ManualTransaction.this, date, cal
                        .get(Calendar.YEAR), cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //So if you tap outside of the edit text field, it hides the keyboard
        itemName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        amountET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

    }
    public void hideKeyboard(View view) {
        // Remember to set Parent views of the EditTexts to:
        // android:clickable="true" android:focusableInTouchMode="true"
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void updateLabel() {
        String dateFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        dateET.setText(sdf.format(cal.getTime()));
    }

    public void confirmTransaction(View view) {
        if (validInputCheck()) {
            Intent i = new Intent(ManualTransaction.this, ConfirmTransaction.class);
            i.putExtra("itemName", itemName.getText().toString());
            i.putExtra("date", dateET.getText().toString());
            i.putExtra("amount", amountET.getText().toString());
            startActivity(i);
        }
    }

    private boolean validInputCheck() {
        if (TextUtils.isEmpty(itemName.getText())) {
            itemName.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

            Toast.makeText(getApplicationContext(), "Please input the item name",  Toast.LENGTH_LONG).show();
            return false;
        }
        else if (TextUtils.isEmpty(dateET.getText())){
            dateET.requestFocus();
            Toast.makeText(getApplicationContext(), "Please input a date",  Toast.LENGTH_LONG).show();
            return false;
        }
        else if (TextUtils.isEmpty(amountET.getText())){
            amountET.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

            Toast.makeText(getApplicationContext(), "Please input the amount",  Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public static ManualTransaction getInstance(){
        return   manualTransaction;
    }

    public void clearFields(View view) {
        itemName.setText("");
        dateET.setText("");
        amountET.setText("0");
    }
}
