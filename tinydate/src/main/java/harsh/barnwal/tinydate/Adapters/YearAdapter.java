package harsh.barnwal.tinydate.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import harsh.barnwal.tinydate.Interfaces.YearSelectListener;
import harsh.barnwal.tinydate.R;

public class YearAdapter extends RecyclerView.Adapter<YearAdapter.YearViewHolder> {

    private Context context;
    private YearSelectListener yearSelectListener;
    private int currentYear;
    private int textColor;

    public YearAdapter(Context context,
                       YearSelectListener yearSelectListener,
                       int currentYear, int textColor) {
        this.context = context;
        this.yearSelectListener = yearSelectListener;
        this.currentYear = currentYear;
        this.textColor = textColor;
    }

    @NonNull
    @Override
    public YearAdapter.YearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View year_view = inflater.inflate(R.layout.tiny_year_child, parent, false);
        return new YearViewHolder(year_view);
    }

    @Override
    public void onBindViewHolder(@NonNull YearAdapter.YearViewHolder holder, int position) {

        // setting year from 1900 to 2100
        final int year = 1900 + position;



        // setting the year text
        holder.yearText.setText(String.valueOf(year));

        // changing the textColor of current year
        if (year == currentYear){
            holder.yearText.setTextColor(textColor);
            holder.yearText.setTypeface(Typeface.DEFAULT_BOLD);
        }

        // returning the clicked year using Listener
        holder.yearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yearSelectListener.yearClicked(year);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 201;
    }

    class YearViewHolder extends RecyclerView.ViewHolder {

        TextView yearText;

        YearViewHolder(@NonNull View itemView) {
            super(itemView);
            yearText = itemView.findViewById(R.id.yearText_child);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
