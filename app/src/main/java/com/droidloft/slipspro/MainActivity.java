package com.droidloft.slipspro;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private String version = "0.1", buildDate = "4-22-2017";
    private TextView dateTextView;
    private EditText descriptionEditText, amountEditText;
    private Spinner mySpinner;
    private ArrayAdapter<String> spinnerAdapter;
    private String mDay, mMonth, changedDate;
    private Button saveButton;
    private String description, amount, date, type;

    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference entryRef = rootRef.child("entryRef");
    private DatabaseReference checkNumberRef = rootRef.child("checknumref");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        idViews();
        getDate();
        setupSpinner();


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get Data from Views
                date = dateTextView.getText().toString();
                description = descriptionEditText.getText().toString();
                amount = amountEditText.getText().toString();
                type = mySpinner.getSelectedItem().toString();

                if(description.equals("") || amount.equals("")){
                    Toast.makeText(MainActivity.this, "forget something?", Toast.LENGTH_SHORT).show();
                } else {

                    //Cleanup and Reset UI
                    descriptionEditText.setText("");
                    amountEditText.setText("");
                    getDate();
                    descriptionEditText.requestFocus();


                    //Push Data to Firebase
                    entryRef.push().setValue(date);
                    entryRef.push().setValue(description);
                    entryRef.push().setValue(amount);
                    entryRef.push().setValue(type);
                }


            }
        });
        
        
        
        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCalendarDialog();
            }
        });


    }

    private void showCalendarDialog() {

        final Dialog calendarDialog = new Dialog(this);
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.calendar_layout, (ViewGroup)findViewById(R.id.calendarView));
        calendarDialog.setContentView(layout);
        calendarDialog.show();

        CalendarView calendarView = (CalendarView)layout.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {

                month = month + 1;

                if(dayOfMonth < 10) {
                    mDay = ("0" + dayOfMonth);
                } else {
                    mDay = Integer.toString(dayOfMonth);
                }

                if(month < 10) {
                    mMonth = ("0" + month);
                } else {
                    mMonth = Integer.toString(month);
                }

                changedDate = String.valueOf(mMonth + "-" + mDay);
                dateTextView.setText(changedDate);
                calendarDialog.cancel();
            }
        });
    }

    private void setupSpinner() {
        ArrayList<String> spinnerArrayList = new ArrayList<>();
        spinnerArrayList.add("Debit");
        spinnerArrayList.add("Credit");
        spinnerArrayList.add("Check");
        spinnerArrayList.add("Cash");

        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerArrayList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
        mySpinner.setAdapter(spinnerAdapter);


    }

    private void idViews() {
        dateTextView = (TextView)findViewById(R.id.dateTextView);
        descriptionEditText = (EditText)findViewById(R.id.descriptionEditText);
        amountEditText = (EditText)findViewById(R.id.amountEditText);
        mySpinner = (Spinner)findViewById(R.id.spinner);
        saveButton = (Button)findViewById(R.id.saveButton);
    }


    private void getDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        String strDate = sdf.format(c.getTime());
        dateTextView.setText(strDate);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.about){
            AlertDialog.Builder aboutAlert = new AlertDialog.Builder(this);
            aboutAlert.setTitle("Slips v" + version);
            aboutAlert.setMessage("Build Date: " + buildDate + "\n" + "by Michael May" + "\n" + "DroidLoft");
            aboutAlert.setIcon(R.mipmap.ic_launcher);
            aboutAlert.setCancelable(true);
            aboutAlert.show();
        }

        if(item.getItemId() == R.id.list_screen){
            startActivity(new Intent(MainActivity.this, ListScreen.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
