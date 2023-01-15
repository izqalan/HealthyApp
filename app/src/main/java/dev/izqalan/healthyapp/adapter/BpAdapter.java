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
import dev.izqalan.healthyapp.models.BPModel;
import dev.izqalan.healthyapp.models.BmiModel;


public class BpAdapter  extends RecyclerView.Adapter<BpAdapter.ViewHolder> {
    Context context_bp;

    ArrayList <BPModel> historyList_bp;


    public BpAdapter(Context context, ArrayList<BPModel> historyList) {
        this.context_bp = context;
        this.historyList_bp = historyList;


    }
    public Context getContext() {
        return context_bp;
    }

    public ArrayList<BPModel> getHistoryList() {
        return historyList_bp;
    }

    @NonNull
    @Override
    public BpAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context_bp).inflate(R.layout.recycler_view_bp, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BpAdapter.ViewHolder holder, int position) {
        BPModel bp = historyList_bp.get(position);
        holder.sys.setText(bp.getSys());
        holder.dia.setText(bp.getDia());
        holder.bp.setText(bp.getBp());
    }

    @Override
    public int getItemCount() {
        return historyList_bp.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView sys, dia, bp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            dia = itemView.findViewById(R.id.tv_stored_dia);
            sys = itemView.findViewById(R.id.tv_stored_sys);
            bp = itemView.findViewById(R.id.tv_stored_bp);


        }
    }

}
