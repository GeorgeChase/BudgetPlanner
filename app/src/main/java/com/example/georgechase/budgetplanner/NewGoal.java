package com.example.georgechase.budgetplanner;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.georgechase.budgetplanner.models.Goal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class NewGoal extends AppCompatActivity {

    private Spinner categorySpinner;
    private String chosenCategory;
    private EditText dateET;
    private EditText amtReqET;
    private EditText otherCategory;
    private Calendar myCalendar;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_goal);

        dateET =  findViewById(R.id.dateET);
        amtReqET = findViewById(R.id.amtRequiredET);
        myCalendar = Calendar.getInstance();

        //Formats numbers into currency as the user inputs
        amtReqET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            private String current = "";
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals(current)){
                    amtReqET.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[$,.]", "");

                    double parsed = Double.parseDouble(cleanString);
                    String formatted = NumberFormat.getCurrencyInstance().format((parsed/100));

                    current = formatted;
                    amtReqET.setText(formatted);
                    amtReqET.setSelection(formatted.length());

                    amtReqET.addTextChangedListener(this);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        //Populates the category spinner from string-array
        categorySpinner = findViewById(R.id.categorySpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.category_names, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                chosenCategory = adapterView.getItemAtPosition(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //"Other" checkbox toggle functions
        CheckBox checkbox = findViewById(R.id.otherChkBx);
        checkbox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                otherCategory = findViewById(R.id.newCategoryET);
                if (((CheckBox) v).isChecked()) {
                    otherCategory.setVisibility(View.VISIBLE);
                    categorySpinner.setAlpha(0.5F);
                } else {
                    otherCategory.setVisibility(View.GONE);
                    categorySpinner.setAlpha(1.0F);
                }
            }
        });

        //Pick a date calendar popup
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        dateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(NewGoal.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //Gets the count for number of goals
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("goals")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int numOfGoals = (int) dataSnapshot.getChildrenCount();
                        initNumGoalsCount(numOfGoals);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    public void submitNewGoal(View view) {
        CheckBox checkbox = findViewById(R.id.otherChkBx);
        if (chosenCategory.equals("Select a Category")  && !checkbox.isChecked()) {
            Toast.makeText(getApplicationContext(), "Please select or enter another category",  Toast.LENGTH_LONG).show();
        }
        else if (TextUtils.isEmpty(dateET.getText())){
            Toast.makeText(getApplicationContext(), "Please input a date",  Toast.LENGTH_LONG).show();
        }
        else if (TextUtils.isEmpty(amtReqET.getText())){
            Toast.makeText(getApplicationContext(), "Please input the required amount",  Toast.LENGTH_LONG).show();
        }
        else if (checkbox.isChecked() && TextUtils.isEmpty(otherCategory.getText())) {
            Toast.makeText(getApplicationContext(), "Please enter a new category",  Toast.LENGTH_LONG).show();
        }
        else if (checkbox.isChecked() && !TextUtils.isEmpty(otherCategory.getText())) {
            chosenCategory = otherCategory.getText().toString();
            addGoalToDB();
        }
        else {
            addGoalToDB();
        }
    }

    private void addGoalToDB() {
        Goal goal = new Goal ();
        goal.setDate(dateET.getText().toString());
        goal.setCategory(chosenCategory);
        goal.setRequired_amount(amtReqET.getText().toString());

        //Updates current user's goals
        String goalCounter = Integer.toString(count);
        FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("goals")
                .child("goal " + goalCounter)
                .setValue(goal);

        Toast.makeText(getApplicationContext(), "Goal has successfully been added.",  Toast.LENGTH_SHORT).show();
        this.finish();
    }

    private void updateLabel() {
        String dateFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        dateET.setText(sdf.format(myCalendar.getTime()));
    }

    private void initNumGoalsCount(int numGoals) {
        this.count = numGoals;
        count++; //Just to offset goals to start at 1 and not 0.
    }
}
