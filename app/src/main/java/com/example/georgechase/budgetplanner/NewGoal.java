package com.example.georgechase.budgetplanner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


public class NewGoal extends AppCompatActivity {

    private Spinner categorySpinner;
    private String chosenCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_goal);

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
    }


    public void submitNewGoal(View view) {
        CheckBox checkbox = findViewById(R.id.otherChkBx);
        if (chosenCategory.equals("Select a Category")  && !checkbox.isChecked()) {
            Toast.makeText(getApplicationContext(), "Please select or enter another category",  Toast.LENGTH_LONG).show();
        }
        else {
            EditText dateET = findViewById(R.id.dateET);
            EditText amtReqET = findViewById(R.id.amtRequiredET);

            setContentView(R.layout.fragment_goals);
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
            TextView date = new TextView(this);
            date.setId(100 + count);
            date.setText(dateET.getText().toString());
            date.setPadding(5, 0,35, 0);

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

            row.addView(date);
            row.addView(category);
            row.addView(amtRequired);

            goalTable.addView(row);
        }
    }

    public void addNewGoal(View view) {
    }
}
