package harsh.barnwal.tinydate.Adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import harsh.barnwal.tinydate.Interfaces.TinyDateListener;
import harsh.barnwal.tinydate.R;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.GridViewHolder> {

    private Context context;
    private int noDate;
    private int daysCount;
    private int emptyDays;
    private int pos = 0;
    private View previousView;
    private TinyDateListener tinyDateListener;
    private ColorStateList colorStateList;
    private int textColor;

    public GridAdapter(Context context, int emptyDays, int daysNumber,
                       TinyDateListener tinyDateListener, int textColor) {
        this.context = context;
        this.noDate = emptyDays;
        this.tinyDateListener = tinyDateListener;
        this.textColor = textColor;

        daysCount = daysNumber + noDate;
        this.emptyDays = emptyDays;
    }

    @NonNull
    @Override
    public GridAdapter.GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.grid_item, parent, false);
        return new GridViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GridAdapter.GridViewHolder holder, final int position) {
        if (noDate != 0) {

            // setting the blank text
            holder.dateText.setText("");
            noDate -= 1;
            holder.dateText.setTextColor(textColor);

        } else {

            // setting the date here
            int date = position - emptyDays;
            holder.dateText.setText(String.valueOf(date + 1));
            holder.dateText.setTextColor(textColor);

        }
        if (position > (emptyDays - 1)) {
            holder.dateText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (pos == 0) {
//                            view.setBackground(context.getResources().getDrawable(
//                                    R.drawable.round_background));
                            TextView textView = (TextView) view;
                            colorStateList = textView.getTextColors();

                            textView.setTypeface(Typeface.DEFAULT_BOLD);
                            textView.setTextColor(textColor);
                            previousView = view;
                            pos += 1;

                            int weekOFDay = position % 7;

                            // passing the data using listener
                            tinyDateListener.dateClick(textView.getText().toString(), weekOFDay);
                        } else {
                            if (previousView != view) {
//                                view.setBackground(context.getResources().getDrawable(
//                                        R.drawable.round_background));

                                TextView textView = (TextView) view;
                                textView.setTextColor(textColor);
                                textView.setTypeface(Typeface.DEFAULT_BOLD);

                                previousView.setBackgroundResource(0);
                                TextView previousTextView = (TextView) previousView;
                                previousTextView.setTextColor(colorStateList);
                                previousTextView.setTypeface(Typeface.DEFAULT);
                                previousView = view;

                                // passing the data using listener
                                int weekOFDay = position % 7;
                                tinyDateListener.dateClick(textView.getText().toString(), weekOFDay);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return daysCount;
    }

    class GridViewHolder extends RecyclerView.ViewHolder {

        TextView dateText;

        GridViewHolder(@NonNull View itemView) {
            super(itemView);
            dateText = itemView.findViewById(R.id.gridChild_text);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

}
