package dev.izqalan.healthyapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import dev.izqalan.healthyapp.R;
import dev.izqalan.healthyapp.models.BmiModel;

public class BmiAdapter extends RecyclerView.Adapter<BmiAdapter.ViewHolder> {

    Context context;
    ArrayList<BmiModel> historyList;



    public BmiAdapter(Context context, ArrayList<BmiModel> historyList) {
        this.context = context;
        this.historyList = historyList;
    }

    public Context getContext() {
        return context;
    }

    public ArrayList<BmiModel> getHistoryList() {
        return historyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.recycler_view_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BmiModel bmi = historyList.get(position);
        holder.weight.setText(bmi.getWeight());
        holder.height.setText(bmi.getHeight());
        holder.calculated.setText(bmi.getCalculated());
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView weight, height, calculated;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            weight = itemView.findViewById(R.id.tv_stored_weight);
            height = itemView.findViewById(R.id.tv_stored_height);
            calculated = itemView.findViewById(R.id.tv_stored_bmi);


        }
    }

}
