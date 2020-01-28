package com.tech4use.tinydatesample;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import harsh.barnwal.tinydate.Dialogs.TinyDateDialog;
import harsh.barnwal.tinydate.Interfaces.DateSelectedListener;

public class TinyDateSample extends AppCompatActivity implements View.OnClickListener,
        DateSelectedListener,
        AdapterView.OnItemSelectedListener {

    Button button;
    TextView date_textView;
    Spinner dateFormat_spinner;
    TinyDateDialog dateDialog;
    TextView selectedDate;
    TextView selectedDay;
    TextView selectedMonth;
    TextView selectedMonthNumber;
    TextView selectedYear;

    String[] spinnerItems = {"dd/MM/yyyy", "MM/dd/yyyy",
            "yyyy/MM/dd", "MM/yyyy", "yyyy/MM",
            "day/date/monthName/year"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findIds();
        bindSpinnerData();
        button.setOnClickListener(this);
        dateDialog = new TinyDateDialog(this, new DateSelectedListener() {
            @Override
            public void onDateSelected(String date) {
                // do you stuff here
            }
        });


    }

    @Override
    public void onClick(View view) {

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.img);    // for image from drawable to bitmap

        dateDialog
                .setCalendarImage(bitmap)    // setting the background image of the calendar
                .setImageAlpha(0.7f)    // for visibility % of image
                .setImageScaleType(ImageView.ScaleType.FIT_XY)    // for scaling image to fit calendar background
//                .setWeekTextColor(getResources().getColor(R.color.white)) // for changing week text color
//                .setOkButtonColor(getResources().getColor(R.color.white)); // for changing button text color
                .setTabColor(getResources().getColor(R.color.orange))
                .setWeekTextColor(getResources().getColor(R.color.black)) // for changing week text color
                .setTextColor(getResources().getColor(R.color.black));    // for changing text color
        dateDialog.show();
    }

    @Override
    public void onDateSelected(String date) {
        date_textView.setText(date);

        // fetching date details, this is optional
        selectedDate.setText(String.valueOf(dateDialog.getSelectedDate()));
        selectedDay.setText(dateDialog.getSelectedDay());
        selectedMonth.setText(dateDialog.getSelectedMonthName());
        selectedMonthNumber.setText(String.valueOf(dateDialog.getSelectedMonthNumber()));
        selectedYear.setText(String.valueOf(dateDialog.getSelectedYear()));
    }

    private void bindSpinnerData(){
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, spinnerItems);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateFormat_spinner.setAdapter(spinnerAdapter);
        dateFormat_spinner.setOnItemSelectedListener(this);
    }

    private void findIds(){
        button = findViewById(R.id.button);
        date_textView = findViewById(R.id.date_textView);
        dateFormat_spinner = findViewById(R.id.dateFormat_spinner);
        selectedDate = findViewById(R.id.selectedDate);
        selectedDay = findViewById(R.id.selectedDay);
        selectedMonth = findViewById(R.id.selectedMonth);
        selectedMonthNumber = findViewById(R.id.selectedMonthNumber);
        selectedYear = findViewById(R.id.selectedYear);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i){
            case 0:
                dateDialog.setDateFormat(TinyDateDialog.MonthFormats.dd_MM_yyyy);
                break;
            case 1:
                dateDialog.setDateFormat(TinyDateDialog.MonthFormats.MM_dd_yyyy);
                break;
            case 2:
                dateDialog.setDateFormat(TinyDateDialog.MonthFormats.yyyy_MM_dd);
                break;
            case 3:
                dateDialog.setDateFormat(TinyDateDialog.MonthFormats.MM_yyyy);
                break;
            case 4:
                dateDialog.setDateFormat(TinyDateDialog.MonthFormats.yyyy_MM);
                break;
            case 5:
                dateDialog.setDateFormat(TinyDateDialog.MonthFormats.day_date_monthName_year);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // do nothing
    }
}
