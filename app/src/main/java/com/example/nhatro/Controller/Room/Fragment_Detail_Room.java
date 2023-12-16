package com.example.nhatro.Controller.Room;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.nhatro.Controller.Tenants.Fragment_Tenants;
import com.example.nhatro.Controller.Bill_and_Search.Hoa_Don.Fragment_Bills;
import com.example.nhatro.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Fragment_Detail_Room extends Fragment {
    private View view;
    private TextView tv_tool_bar, tv_tinh_trang, tv_ngay_thue, tv_ngay_thu_tien, tv_gia_phong;
    private String idPhong, tenPhong, tinhTrang, ngayThue, ngayThuTien, giaPhong;
    private ImageView img_back, img_edit;
    private AppCompatButton btn_detail_bill, btn_detail_tenants;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_detail_room, container, false);
        anhxa();
        click_event();
        getPhong();


        return view;
    }
    private void anhxa(){
        tv_tool_bar = view.findViewById(R.id.tv_tool_bar);
        img_back = view.findViewById(R.id.img_back);
        tv_tinh_trang = view.findViewById(R.id.tv_tinh_trang);
        tv_ngay_thue = view.findViewById(R.id.tv_ngay_thue);
        tv_ngay_thu_tien = view.findViewById(R.id.tv_ngay_thu_tien);
        tv_gia_phong = view.findViewById(R.id.tv_gia_phong);
        btn_detail_bill = view.findViewById(R.id.btn_detail_bill);
        btn_detail_tenants = view.findViewById(R.id.btn_detail_tenants);
        img_edit = view.findViewById(R.id.img_edit);

    }

    private void click_event(){
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, new Fragment_Home()).commit();
            }
        });

        img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment_Edit_Room fragment_edit_room = new Fragment_Edit_Room();
                Bundle bundle = new Bundle();
                bundle.putString("idPhong", idPhong);
                fragment_edit_room.setArguments(bundle);

                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, fragment_edit_room)
                        .addToBackStack(null)
                        .commit();

            }
        });
        
    }

    private void getPhong(){
        Bundle args = getArguments();
        idPhong = args.getString("idPhong");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Rooms").child(idPhong);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tenPhong = snapshot.child("ten_Phong").getValue(String.class);
                tinhTrang = snapshot.child("tinh_Trang").getValue(String.class);
                ngayThue = snapshot.child("ngay_Thue").getValue(String.class);
                ngayThuTien = snapshot.child("ngay_thu_tien").getValue(String.class);
                giaPhong = snapshot.child("gia_Phong").getValue(String.class);

                tv_tool_bar.setText(tenPhong);
                tv_tinh_trang.setText(tinhTrang);
                tv_ngay_thue.setText(ngayThue);
                tv_ngay_thu_tien.setText(ngayThuTien);
                tv_gia_phong.setText(giaPhong);

                if (tinhTrang.equals("Chưa có người thuê")){
                    btn_detail_tenants.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                            builder.setTitle("Thông báo");
                            builder.setMessage("Chưa có người thuê");

                            // Thêm nút "Đóng" vào thông báo
                            builder.setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            // Hiển thị thông báo
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    });
                    btn_detail_bill.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                            builder.setTitle("Thông báo");
                            builder.setMessage("Chưa có người thuê");

                            // Thêm nút "Đóng" vào thông báo
                            builder.setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            // Hiển thị thông báo
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    });
                } else {
                    btn_detail_tenants.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Fragment_Tenants fragment__tenants = new Fragment_Tenants();
                            Bundle bundle = new Bundle();
                            bundle.putString("idPhong", idPhong);
                            fragment__tenants.setArguments(bundle);

                            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.frame_layout, fragment__tenants)
                                    .addToBackStack(null)
                                    .commit();

                        }
                    });
                    btn_detail_bill.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            Fragment_Bills fragment_bills = new Fragment_Bills();
                            Bundle bundle = new Bundle();
                            bundle.putString("idPhong", idPhong);
                            fragment_bills.setArguments(bundle);

                            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.frame_layout, fragment_bills)
                                    .addToBackStack(null)
                                    .commit();

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
