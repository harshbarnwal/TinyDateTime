package harsh.barnwal.tinydate.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.Date;

import harsh.barnwal.tinydate.Adapters.GridAdapter;
import harsh.barnwal.tinydate.Adapters.MonthsAdapter;
import harsh.barnwal.tinydate.Interfaces.DateSelectedListener;
import harsh.barnwal.tinydate.Interfaces.TinyDateListener;
import harsh.barnwal.tinydate.Interfaces.YearDialogListener;
import harsh.barnwal.tinydate.R;

public class TinyDateDialog extends Dialog implements TinyDateListener,
        View.OnClickListener, YearDialogListener {

    private TextView selectedDate;
    private Context context;

    private enum monthsEnum {
        January, February, March, April, Mays,
        June, July, August, September, October,
        November, December
    }
    private enum weekDaysEnum {
        Sun, Mon , Tue, Wed,
        Thu, Fri, Sat
    }
    public enum MonthFormats{
        dd_MM_yyyy,
        MM_dd_yyyy,
        yyyy_MM_dd,
        MM_yyyy,
        yyyy_MM,
        day_date_monthName_year
    }
    private int monthInt = 0;
    private int extraDays;
    private Calendar calendar;
    private int daysNumber;
    private RecyclerView monthsRecyclerView;
    private String month;
    private int dialogWidth;
    private RecyclerView tinyDate_recyclerView;
    private TextView yearText;
    private int dateYear = Calendar.getInstance().get(Calendar.YEAR);
    private String tinySelectedDate;
    private RelativeLayout tiny_tab_layout;
    private int currentMonth;
    private String selectedDay;
    private String selectedMonth;
    private int selectedDateNumber;
    private String monthDetail;
    private TextView sun, mon, tue, wed, thu, fri, sat;
    private int weekTextColor = 0;

    // for calendar properties
    private ImageView calendar_img;
    private float alphaValue = 0f;
    private ImageView.ScaleType scaleType;
    private Bitmap bitmap;
    private int colorId = 0;
    private int textColor = 0;
    private int buttonColor = 0;
    private Button tinyDate_okButton;
    private DateSelectedListener dateSelectedListener;
    private MonthFormats dateFormat = MonthFormats.dd_MM_yyyy;

    public TinyDateDialog(@NonNull Context context,
                          DateSelectedListener dateSelectedListener) {
        super(context);
        this.context = context;
        this.dateSelectedListener = dateSelectedListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tiny_date);

        // setting the dialog size
        if (getWindow() != null) {
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                    500);
            getWindow()
                    .getAttributes().windowAnimations = R.style.DateDialogAnimation;

            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                    (int) (450 * Resources.getSystem().getDisplayMetrics().density));

            // for full screen transparent cardView rounded area
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                getWindow().getDecorView().getBackground().setAlpha(0);
            } else {
                getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
            }
        }

        // finding all views
        findIds();

        calendar_img.post(new Runnable() {
            @Override
            public void run() {
                dialogWidth = calendar_img.getWidth();
                Log.v("harsh", "dialog width = " + dialogWidth);
                bindDataToRecyclerView();
            }
        });

        // binding data to calendar
        yearText.setText(String.valueOf(dateYear));
        calendar = Calendar.getInstance();
        String currentCalendar = calendar.getTime().toString();


        // extracting the current month
        for (int i = 0; i < 12; i++){
            if (currentCalendar.substring(4, 7).equalsIgnoreCase(
                    monthsEnum.values()[i].toString().substring(0, 3))){
                currentMonth = i;
                break;
            }
        }

        // extracting the current date
        String currentDate = currentCalendar.substring(0, 3) + ", " // current day
                + currentCalendar.substring(8, 10) + " "// extracting date
                + monthsEnum.values()[currentMonth].toString(); // extracting current month name
        selectedDate.setText(currentDate);

        // getting the first date of the month
        monthDetail = getFirstDateOfCurrentMonth().toString();
        bindDataToGridView();

        addProperties();
        yearText.setOnClickListener(this);
        tinyDate_okButton.setOnClickListener(this);
    }

    private void bindDataToGridView(){
        String firstDay = monthDetail.substring(0, 3);
        // extracting the extra previous day
        for (int i = 0; i < TinyDateDialog.weekDaysEnum.values().length; i++){
            if (firstDay.equalsIgnoreCase(TinyDateDialog.weekDaysEnum.values()[i].toString())){
                extraDays = i;
                break;
            }
        }
        // extracting month and year
        month = monthDetail.substring(4, 7);
        for (int i = 0; i < TinyDateDialog.monthsEnum.values().length; i++){
            String monthCode = TinyDateDialog.monthsEnum.values()[i].toString().substring(0, 3);
            if (month.equalsIgnoreCase(monthCode)){
                monthInt = i;
                break;
            }
        }

        // calling the adapter with data
        GridAdapter gridAdapter = new GridAdapter(context, extraDays,
                daysNumber, this, textColor);
        tinyDate_recyclerView.setLayoutManager(new GridLayoutManager(
                context, 7
        ));
        tinyDate_recyclerView.setAdapter(gridAdapter);
    }

    private void findIds(){
        selectedDate = findViewById(R.id.selectedDate);
        yearText = findViewById(R.id.yearText);
        monthsRecyclerView = findViewById(R.id.monthsRecyclerView);
        tinyDate_recyclerView = findViewById(R.id.tinyDate_recyclerView);
        calendar_img = findViewById(R.id.calendar_img);
        tinyDate_okButton = findViewById(R.id.tinyDate_okButton);
        tiny_tab_layout = findViewById(R.id.tiny_tab_layout);
        sun = findViewById(R.id.week_sun);
        mon = findViewById(R.id.week_mon);
        tue = findViewById(R.id.week_tue);
        wed = findViewById(R.id.week_wed);
        thu = findViewById(R.id.week_thu);
        fri = findViewById(R.id.week_fri);
        sat = findViewById(R.id.week_sat);
    }

    private Date getFirstDateOfCurrentMonth() {
        // setting month to get 1st day of that month
        calendar.set((Calendar.DAY_OF_MONTH), calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        daysNumber = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        return calendar.getTime();
    }

    private void bindDataToRecyclerView(){
        MonthsAdapter monthsAdapter = new MonthsAdapter(context, month,
                this, dialogWidth, textColor);
        monthsRecyclerView.setLayoutManager(new LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false));
        monthsRecyclerView.smoothScrollToPosition(monthInt);
        monthsRecyclerView.setAdapter(monthsAdapter);
    }


    //==========================================onClick events==========================================//
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.yearText){
            YearDialog yearDialog = new YearDialog(context, this,
                    dateYear, textColor);
            yearDialog.show();
        } else if (view.getId() == R.id.tinyDate_okButton){
            dateSelectedListener.onDateSelected(tinySelectedDate);
            dismiss();
        }
    }


    //==========================================all interface calls==========================================//
    //==========================================all interface calls==========================================//

    // android month recyclerView click interface
    @Override
    public void monthClick(int monthPosition) {
        // setting month to get 1st day of that month

        Calendar cal = Calendar.getInstance();
        cal.set(dateYear, monthPosition, 1);
        cal.set((Calendar.DAY_OF_MONTH), cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        daysNumber = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        Date date = cal.getTime();

        // getting the first dat of the month
        monthDetail = date.toString();
        String firstDay = monthDetail.substring(0, 3);

        // extracting the extra previous day
        for (int i = 0; i < TinyDateDialog.weekDaysEnum.values().length; i++){
            if (firstDay.equalsIgnoreCase(TinyDateDialog.weekDaysEnum.values()[i].toString())){
                extraDays = i;
                break;
            }
        }

        // extracting month and year
        month = monthDetail.substring(4, 7);

        for (int i = 0; i < TinyDateDialog.monthsEnum.values().length; i++){
            String monthCode = TinyDateDialog.monthsEnum.values()[i].toString().substring(0, 3);
            if (month.equalsIgnoreCase(monthCode)){
                monthInt = i;
                break;
            }
        }

        // calling the adapter with data
        GridAdapter gridAdapter = new GridAdapter(context, extraDays,
                daysNumber, this, textColor);
        tinyDate_recyclerView.setAdapter(gridAdapter);
        bindDataToRecyclerView();
    }

    // on click of year of yearDialog
    @Override
    public void yearSelected(int selectedYear) {
        yearText.setText(String.valueOf(selectedYear));

        // saving the selected year to pass it
        dateYear = selectedYear;

        Calendar newCalendar = Calendar.getInstance();
        newCalendar.set(selectedYear, newCalendar.get(Calendar.MONTH), 1);
        newCalendar.set((Calendar.DAY_OF_MONTH), newCalendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        daysNumber = newCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        Date newDate = newCalendar.getTime();

        // getting the first dat of the month
        String monthDetail = newDate.toString();
        String firstDay = monthDetail.substring(0, 3);

        // extracting the extra previous day
        for (int i = 0; i < TinyDateDialog.weekDaysEnum.values().length; i++){
            if (firstDay.equalsIgnoreCase(TinyDateDialog.weekDaysEnum.values()[i].toString())){
                extraDays = i;
                break;
            }
        }

        // extracting month and year
        month = monthDetail.substring(4, 7);

        for (int i = 0; i < TinyDateDialog.monthsEnum.values().length; i++){
            String monthCode = TinyDateDialog.monthsEnum.values()[i].toString().substring(0, 3);
            if (month.equalsIgnoreCase(monthCode)){
                monthInt = i;
                break;
            }
        }

        // calling the adapter with data
        GridAdapter gridAdapter = new GridAdapter(context, extraDays,
                daysNumber, this, textColor);
        tinyDate_recyclerView.setAdapter(gridAdapter);
        bindDataToRecyclerView();
    }

    // android date recyclerView click interface
    @Override
    public void dateClick(String clickedDate, int weekOfDay) {

        selectedDay = weekDaysEnum.values()[weekOfDay].toString(); // extracting clicked day
        selectedMonth = monthsEnum.values()[monthInt].toString(); // month of clicked date
        selectedDateNumber = Integer.parseInt(clickedDate);

        // saving date to show
        String dateDetail = selectedDay + ", " + clickedDate + " " + selectedMonth;

        switch (dateFormat){
            case dd_MM_yyyy:
                tinySelectedDate = clickedDate + "/" + (monthInt + 1) + "/" + dateYear;
                break;
            case MM_dd_yyyy:
                tinySelectedDate = (monthInt + 1) + "/" + clickedDate + "/" + dateYear;
                break;
            case MM_yyyy:
                tinySelectedDate = (monthInt + 1) + "/" + dateYear;
                break;
            case yyyy_MM:
                tinySelectedDate = dateYear + "/" + (monthInt + 1);
                break;
            case yyyy_MM_dd:
                tinySelectedDate = dateYear + "/" + (monthInt + 1) + "/" + clickedDate;
                break;
            case day_date_monthName_year:
                tinySelectedDate = dateDetail + " " + dateYear;
                break;
        }

        // setting the selected date to text
        selectedDate.setText(dateDetail);
    }


    //==========================================adding properties==========================================//
    //==========================================adding properties==========================================//
    // adding properties to calendar
    private void addProperties(){
        if (scaleType != null)
            calendar_img.setScaleType(scaleType);
        if (alphaValue != 0)
            calendar_img.setAlpha(alphaValue);
        if (bitmap != null)
            calendar_img.setImageBitmap(bitmap);
        if (colorId != 0) {
            tiny_tab_layout.setBackgroundColor(colorId);
            tinyDate_okButton.setTextColor(colorId);
        }
        if (weekTextColor != 0){
            sun.setTextColor(weekTextColor);
            mon.setTextColor(weekTextColor);
            tue.setTextColor(weekTextColor);
            wed.setTextColor(weekTextColor);
            thu.setTextColor(weekTextColor);
            fri.setTextColor(weekTextColor);
            sat.setTextColor(weekTextColor);
        }
        if (buttonColor != 0){
            tinyDate_okButton.setTextColor(buttonColor);
        }
    }

    public TinyDateDialog setImageAlpha(float alphaValue){
        this.alphaValue = alphaValue;
        return this;
    }

    public TinyDateDialog setImageScaleType(ImageView.ScaleType scaleType){
        this.scaleType = scaleType;
        return this;
    }

    public TinyDateDialog setCalendarImage(Bitmap bitmap){
        this.bitmap = bitmap;
        return this;
    }

    public TinyDateDialog setTabColor(@ColorInt int colorId){
        this.colorId = colorId;
        return this;
    }

    public TinyDateDialog setTextColor(@ColorInt int textColor){
        this.textColor = textColor;
        return this;
    }

    public TinyDateDialog setDateFormat(MonthFormats dateFormat){
        this.dateFormat = dateFormat;
        return this;
    }

    public TinyDateDialog setWeekTextColor(@ColorInt int weekTextColor) {
        this.weekTextColor = weekTextColor;
        return this;
    }

    public TinyDateDialog setOkButtonColor(@ColorInt int buttonColor){
        this.buttonColor = buttonColor;
        return this;
    }

    public String getSelectedDay(){
        return selectedDay;
    }

    public String getSelectedMonthName(){
        return selectedMonth;
    }

    public int getSelectedMonthNumber(){
        return monthInt + 1;
    }

    public int getSelectedDate(){
        return selectedDateNumber;
    }

    public int getSelectedYear(){
        return dateYear;
    }
}
