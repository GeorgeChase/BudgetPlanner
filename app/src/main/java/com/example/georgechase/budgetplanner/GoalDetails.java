package com.example.georgechase.budgetplanner;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class GoalDetails extends AppCompatActivity{

    private Calendar cal;
    private TextView dateView;
    private TextView categoryView;
    private TextView amountRequiredView;
    private EditText amountET;
    private String d;
    private String c;
    private String ar;

    @Override
    public void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.activity_goal_details);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
           String date = extras.getString("date");
           String category = extras.getString("category");
           String amountRequired = extras.getString("amountRequired");

           setTextViewStrings(date, category, amountRequired);

            cal = Calendar.getInstance();
            final DatePickerDialog.OnDateSetListener calDate = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    cal.set(Calendar.YEAR, year);
                    cal.set(Calendar.MONTH, monthOfYear);
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateLabel();
                }
            };
            dateView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new DatePickerDialog(GoalDetails.this, calDate,
                            cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                            cal.get(Calendar.DAY_OF_MONTH)).show();
                }
            });

            //Formats numbers into currency as the user inputs
            amountET = findViewById(R.id.amountET_goal_details);
            amountET.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
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
                public void afterTextChanged(Editable editable) {}
            });
        }
    }

    private void setTextViewStrings(String date, String category, String amountRequired) {

        d = "Date : " ;
        c = "Category : ";
        ar = "Amount Required : ";
        final int one = 1;
        final int two = 2;
        final int three = 3;

        String dv = d + date;
        dateView = findViewById(R.id.date_placeholder_goal_details);
        dateView.setText(dv);
        dateView.setId(one);

        String cv = c + category;
        categoryView = findViewById(R.id.category_placeholder_goal_details);
        categoryView.setText(cv);
        categoryView.setId(two);

        String arv = ar + amountRequired;
        amountRequiredView = findViewById(R.id.amount_placeholder_goal_details);
        amountRequiredView.setText(arv);
        amountRequiredView.setId(three);
    }

    private void updateLabel() {
        String dateFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        String formattedText = "New " + d + sdf.format(cal.getTime());
        dateView.setText(formattedText);
    }

    public void deleteGoal(View view) {
        //toggleProgressBar();
        //Remove goal from DB, and update the goal "#"?


    }

    public void saveChanges(View view) {
        //toggleProgressBar();
        //Deletes Goal and re-adds the new one maybe?
    }

    public void editDate(View view) {
        dateView.setVisibility(View.GONE);
        int fieldId = view.getId();
        editField(fieldId);
    }

    public void editCategory(View view) {
        categoryView.setText(c);
        int fieldId = view.getId();
        editField(fieldId);
    }

    public void editAmountRequired(View view) {
        amountRequiredView.setText(ar);
        int fieldId = view.getId();
        editField(fieldId);
    }

    private void editField(int fieldId) {

        EditText field = null;
        TextView placeholder = null;
        Boolean dateSelected = false;

        switch (fieldId) {
            case 1:
                field = findViewById(R.id.dateET_goal_details);
                placeholder = dateView;
                dateSelected = true;
                break;
            case 2:
                field = findViewById(R.id.categoryET_goal_details);
                break;
            case 3:
                field = amountET;
                break;
        }
        assert field != null;
        field.setVisibility(View.VISIBLE);
        field.requestFocus();

        if (dateSelected) {
            field.setVisibility(View.GONE);
            placeholder.setVisibility(View.VISIBLE);
        }
        else {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }

    public void goBack(View view) {
        this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
        this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK));
    }

    private void toggleProgressBar() {
        ProgressBar pb = findViewById(R.id.progress_bar_goal_details);

        if (pb.getVisibility() == View.VISIBLE) {
            pb.setVisibility(View.GONE);
        }
        else {
            pb.setVisibility(View.VISIBLE);
        }
    }
}
