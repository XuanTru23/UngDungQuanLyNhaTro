package com.example.nhatro.Controller.Bill_and_Search.Hoa_Don;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhatro.Model.Bills;
import com.example.nhatro.R;

import java.util.ArrayList;

public class RCVBillsAdapter extends RecyclerView.Adapter<RCVBillsAdapter.ViewHolder>{
    private ArrayList<Bills> list;

    public RCVBillsAdapter(ArrayList<Bills> list) {
        this.list = list;
    }
    private RCVBillsAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(String id_Bills);
    }

    public void setOnItemClickListener(RCVBillsAdapter.OnItemClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rcv_bills, parent,false);
        return new RCVBillsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Bills bills = list.get(position);

        String ngay_thu = bills.FormattedDate();
        holder.tv_hoa_don.setText(ngay_thu);

        String tt = bills.getTinh_Trang();
        if (tt.equals("Chưa nộp tiền")){
            holder.CV.setCardBackgroundColor(Color.parseColor("#FF535353"));
        } else {
            holder.CV.setCardBackgroundColor(Color.parseColor("#FF03A9F4"));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(bills.getId_Bill());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_hoa_don;
        private CardView CV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_hoa_don = itemView.findViewById(R.id.tv_hoa_don);
            CV = itemView.findViewById(R.id.CV);

        }
    }
}
