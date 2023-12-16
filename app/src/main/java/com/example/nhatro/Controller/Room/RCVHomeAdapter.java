package com.example.nhatro.Controller.Room;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhatro.Model.Rooms;
import com.example.nhatro.R;

import java.util.ArrayList;

public class RCVHomeAdapter extends RecyclerView.Adapter<RCVHomeAdapter.ViewHolder>{
    private ArrayList<Rooms> list;

    public RCVHomeAdapter(ArrayList<Rooms> list) {
        this.list = list;
    }

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(String idPhong);
    }

    public void setOnItemClickListener(RCVHomeAdapter.OnItemClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rcv_room, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Rooms rooms = list.get(position);

        holder.tv_ten_phong.setText(rooms.getTen_Phong());
        holder.tv_trang_thai.setText(rooms.getTinh_Trang());
        String tt = rooms.getTinh_Trang();
        if (tt.equals("Chưa có người thuê")){
            holder.CV.setCardBackgroundColor(Color.parseColor("#FF535353"));
        } else {
            holder.CV.setCardBackgroundColor(Color.parseColor("#FF03A9F4"));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(rooms.getId_Phong());
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_ten_phong, tv_trang_thai;
        private CardView CV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            CV = itemView.findViewById(R.id.CV);
            tv_ten_phong = itemView.findViewById(R.id.tv_ten_phong);
            tv_trang_thai = itemView.findViewById(R.id.tv_trang_thai);

        }
    }
}
