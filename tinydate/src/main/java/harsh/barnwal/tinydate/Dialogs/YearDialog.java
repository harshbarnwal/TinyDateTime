package harsh.barnwal.tinydate.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import harsh.barnwal.tinydate.Adapters.YearAdapter;
import harsh.barnwal.tinydate.Interfaces.YearDialogListener;
import harsh.barnwal.tinydate.Interfaces.YearSelectListener;
import harsh.barnwal.tinydate.R;

public class YearDialog extends Dialog implements YearSelectListener {

    private Context context;
    private YearDialogListener yearDialogListener;
    private int selectedYear;
    private int textColor;

    YearDialog(@NonNull Context context,
               YearDialogListener yearDialogListener,
               int selectedYear, int textColor) {
        super(context);
        this.context = context;
        this.yearDialogListener = yearDialogListener;
        this.selectedYear = selectedYear;
        this.textColor = textColor;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tiny_year_dialog);
        RecyclerView yearRecyclerView = findViewById(R.id.year_recyclerView);

        // setting text color
        TextView select_year_text = findViewById(R.id.select_year_text);
        select_year_text.setTextColor(textColor);

        // setting the dialog width 200dp and height 200dp
        if (getWindow() != null) {
            getWindow()
                    .getAttributes().windowAnimations = R.style.YearDialogAnimation;

            getWindow().setLayout(
                    (int) (200 * Resources.getSystem().getDisplayMetrics().density),
                    (int) (200 * Resources.getSystem().getDisplayMetrics().density));

            // for full screen transparent cardView rounded area
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                getWindow().getDecorView().getBackground().setAlpha(0);
            } else {
                getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
            }
        }

        YearAdapter yearAdapter = new YearAdapter(context, this,
                selectedYear, textColor);
        yearRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        yearRecyclerView.setAdapter(yearAdapter);
        // scrolling down to current year
        yearRecyclerView.scrollToPosition((selectedYear - 1900 - 1));
    }

    @Override
    public void yearClicked(int selectedYear) {
        // sending data to Date Dialog via listener
        yearDialogListener.yearSelected(selectedYear);
        cancel();
    }
}
