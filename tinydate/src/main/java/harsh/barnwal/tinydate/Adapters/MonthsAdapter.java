package harsh.barnwal.tinydate.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import harsh.barnwal.tinydate.Interfaces.TinyDateListener;
import harsh.barnwal.tinydate.R;

public class MonthsAdapter extends RecyclerView.Adapter<MonthsAdapter.MonthsViewHolder> {

    private Context context;
    private enum monthsEnum {
        Jan, Feb, Mar, Apr, May,
        Jun, Jul, Aug, Sep, Oct,
        Nov, Dec
    }
    private String selectedMonth;
    private int selectedMonthPosition;
    private TinyDateListener tinyDateListener;
    private int dialogWidth;
    private int textColor;

    public MonthsAdapter(Context context, String selectedMonth,
                  TinyDateListener tinyDateListener,
                  int dialogWidth, int textColor) {
        this.context = context;
        this.selectedMonth = selectedMonth;
        this.tinyDateListener = tinyDateListener;
        this.dialogWidth = dialogWidth;
        this.textColor = textColor;

        if (selectedMonth.equalsIgnoreCase(monthsEnum.values()[0].toString())){
            this.selectedMonth = "January";
            selectedMonthPosition = 0;
        } else if (selectedMonth.equalsIgnoreCase(monthsEnum.values()[1].toString())){
            this.selectedMonth = "February";
            selectedMonthPosition = 1;
        } else if (selectedMonth.equalsIgnoreCase(monthsEnum.values()[2].toString())){
            this.selectedMonth = "March";
            selectedMonthPosition = 2;
        } else if (selectedMonth.equalsIgnoreCase(monthsEnum.values()[3].toString())){
            this.selectedMonth = "April";
            selectedMonthPosition = 3;
        } else if (selectedMonth.equalsIgnoreCase(monthsEnum.values()[4].toString())){
            this.selectedMonth = "May";
            selectedMonthPosition = 4;
        } else if (selectedMonth.equalsIgnoreCase(monthsEnum.values()[5].toString())){
            this.selectedMonth = "June";
            selectedMonthPosition = 5;
        } else if (selectedMonth.equalsIgnoreCase(monthsEnum.values()[6].toString())){
            this.selectedMonth = "July";
            selectedMonthPosition = 6;
        } else if (selectedMonth.equalsIgnoreCase(monthsEnum.values()[7].toString())){
            this.selectedMonth = "August";
            selectedMonthPosition = 7;
        } else if (selectedMonth.equalsIgnoreCase(monthsEnum.values()[8].toString())){
            this.selectedMonth = "September";
            selectedMonthPosition = 8;
        } else if (selectedMonth.equalsIgnoreCase(monthsEnum.values()[9].toString())){
            this.selectedMonth = "October";
            selectedMonthPosition = 9;
        } else if (selectedMonth.equalsIgnoreCase(monthsEnum.values()[10].toString())){
            this.selectedMonth = "November";
            selectedMonthPosition = 10;
        } else if (selectedMonth.equalsIgnoreCase(monthsEnum.values()[11].toString())){
            this.selectedMonth = "December";
            selectedMonthPosition = 11;
        }
    }

    @NonNull
    @Override
    public MonthsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(
                dialogWidth / 3,
                70
        );
        View monthsView = inflater.inflate(R.layout.month_item, parent, false);
        monthsView.setLayoutParams(layoutParams);
        return new MonthsViewHolder(monthsView, tinyDateListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MonthsViewHolder holder, int position) {

        // setting all the months
        holder.gridChild_text.setText(monthsEnum.values()[position].toString());

        // changing the look of the selected month
        if (position == selectedMonthPosition){
            holder.gridChild_text.setText(selectedMonth);
            holder.gridChild_text.setTypeface(Typeface.DEFAULT_BOLD);
            holder.gridChild_text.setTextColor(textColor);
        }

    }

    @Override
    public int getItemCount() {
        return monthsEnum.values().length;
    }

    class MonthsViewHolder extends RecyclerView.ViewHolder {

        TextView gridChild_text;

        MonthsViewHolder(@NonNull View itemView, final TinyDateListener tinyDateListener) {
            super(itemView);
            gridChild_text = itemView.findViewById(R.id.month_text);

            // returning clicked month position using listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    tinyDateListener.monthClick(pos);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
