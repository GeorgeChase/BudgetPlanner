package com.example.georgechase.budgetplanner;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;


public class NewGoal extends AppCompatActivity {

    private Spinner categorySpinner;
    private String chosenCategory;
    private EditText dateET;
    private EditText amtReqET;
    final private Calendar myCalendar = Calendar.getInstance();
    private FirebaseDatabase database;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_goal);

        dateET =  findViewById(R.id.dateET);
        amtReqET = findViewById(R.id.amtRequiredET);

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
                EditText category = findViewById(R.id.newCategoryET);
                if (((CheckBox) v).isChecked()) {
                    category.setVisibility(View.VISIBLE);
                    categorySpinner.setAlpha(0.5F);
                } else {
                    category.setVisibility(View.GONE);
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
    }


    public void submitNewGoal(View view) {
        CheckBox checkbox = findViewById(R.id.otherChkBx);
        if (chosenCategory.equals("Select a Category")  && !checkbox.isChecked()) {
            Toast.makeText(getApplicationContext(), "Please select or enter another category",  Toast.LENGTH_LONG).show();
        }
        else {

            user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                String uid = user.getUid();
                database = FirebaseDatabase.getInstance();
                DatabaseReference usersRef = database.getReference("users");

                //HashMap<String, Object> result = new HashMap<>();
                //result.put(uid + "/goals/", "Zero");
                //usersRef.updateChildren(result);
            }
         /*   setContentView(R.layout.fragment_goals);
            TableLayout goalTable = findViewById(R.id.goalsTable);
            Integer count = goalTable.getChildCount();

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
            theDate.setText(dateET.getText().toString());
            theDate.setPadding(5, 0,35, 0);

            TextView category = new TextView(this);
            category.setId(200 + count);
            category.setText(chosenCategory);
            category.setPadding(70, 0,35, 0);

            TextView amtRequired = new TextView(this);
            String amount = getString(R.string.money_sign) +
                    amtReqET.getText().toString();
            amtRequired.setId(300 + count);
            amtRequired.setText(amount);
            amtRequired.setPadding(70, 0,35, 0);

            row.addView(theDate);
            row.addView(category);
            row.addView(amtRequired);

            goalTable.addView(row);*/
        }
    }

    private void updateLabel() {
        String dateFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        dateET.setText(sdf.format(myCalendar.getTime()));
    }
}
