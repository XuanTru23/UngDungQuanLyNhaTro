package com.example.nhatro.Controller.Bill_and_Search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.nhatro.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Fragment_Detail_Search extends Fragment {
    private View view;
    private String idNguoiThue, idPhong, tenNguoiThue, CCCD, diaChi, SDT;
    private TextView tv_ten_nguoi_thue, tv_cccd, tv_sdt, tv_dia_chi, tv_phong;
    private ImageView img_back;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_detail_search, container, false);
        anhxa();
        click_event();
        getData();
        return view;
    }
    private void anhxa(){
        tv_ten_nguoi_thue = view.findViewById(R.id.tv_ten_nguoi_thue);
        img_back = view.findViewById(R.id.img_back);
        tv_cccd = view.findViewById(R.id.tv_cccd);
        tv_sdt = view.findViewById(R.id.tv_sdt);
        tv_dia_chi = view.findViewById(R.id.tv_dia_chi);
        tv_phong = view.findViewById(R.id.tv_phong);
    }
    private void click_event(){
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, new Fragment_Search()).commit();
            }
        });
    }

    private void getData(){
        Bundle args = getArguments();
        idNguoiThue = args.getString("idNguoiThue");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Tenants").child(idNguoiThue);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tenNguoiThue = snapshot.child("ten_Nguoi_Thue").getValue(String.class);
                CCCD = snapshot.child("cccd").getValue(String.class);
                SDT = snapshot.child("sdt").getValue(String.class);
                diaChi = snapshot.child("dia_chi").getValue(String.class);
                idPhong = snapshot.child("id_Phong").getValue(String.class);

                tv_ten_nguoi_thue.setText(tenNguoiThue);
                tv_cccd.setText(CCCD);
                tv_sdt.setText(SDT);
                tv_dia_chi.setText(diaChi);
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Rooms").child(idPhong);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String tenPhong = snapshot.child("ten_Phong").getValue(String.class);
                        tv_phong.setText(tenPhong);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
