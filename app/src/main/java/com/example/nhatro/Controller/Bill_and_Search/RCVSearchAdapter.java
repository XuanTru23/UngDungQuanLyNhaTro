package com.example.nhatro.Controller.Bill_and_Search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhatro.Model.Tenants;
import com.example.nhatro.R;

import java.util.ArrayList;

public class RCVSearchAdapter extends RecyclerView.Adapter<RCVSearchAdapter.ViewHolder>{
    private ArrayList<Tenants> list;
    public RCVSearchAdapter(ArrayList<Tenants> list) {
        this.list = list;
    }

    private RCVSearchAdapter.OnItemClickListener listener;
    public interface OnItemClickListener {
        void onItemClick(String idNguoiThue);
    }

    public void setOnItemClickListener(RCVSearchAdapter.OnItemClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rcv_search, parent,false);
        return new RCVSearchAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tenants tenants = list.get(position);

        holder.tv_ten_nguoi_thue.setText(tenants.getTen_Nguoi_Thue());
        holder.tv_dia_chi.setText(tenants.getDia_chi());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(tenants.getId_Nguoi_Thue());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_dia_chi, tv_ten_nguoi_thue;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_dia_chi = itemView.findViewById(R.id.tv_dia_chi);
            tv_ten_nguoi_thue = itemView.findViewById(R.id.tv_ten_nguoi_thue);
        }
    }
}
