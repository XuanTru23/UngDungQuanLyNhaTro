package com.example.nhatro.Controller.Tenants;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.nhatro.Controller.Bill_and_Search.Progress_Dialog;
import com.example.nhatro.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Fragment_Detail_Tenants extends Fragment {
    private View view;
    private TextView tv_ten_nguoi_thue, tv_cccd, tv_sdt, tv_dia_chi;
    private AppCompatButton btn_edit_tenants, btn_delete_tenants;
    private ImageView img_back;
    private String idPhong, idNguoiThue, tenNguoiThue, CCCD, SDT, diaChi;
    private Progress_Dialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_detail_tenants, container, false);
        anhxa();
        click_event();
        getTenants();

        return view;

    }
    private void anhxa(){
        tv_ten_nguoi_thue = view.findViewById(R.id.tv_ten_nguoi_thue);
        img_back = view.findViewById(R.id.img_back);
        tv_cccd = view.findViewById(R.id.tv_cccd);
        tv_sdt = view.findViewById(R.id.tv_sdt);
        tv_dia_chi = view.findViewById(R.id.tv_dia_chi);
        btn_edit_tenants = view.findViewById(R.id.btn_edit_tenants);
        btn_delete_tenants = view.findViewById(R.id.btn_delete_tenants);
        dialog = new Progress_Dialog(view.getContext());
    }

    private void click_event(){
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment_Tenants fragment_tenants = new Fragment_Tenants();
                Bundle bundle = new Bundle();
                bundle.putString("idPhong", idPhong);
                fragment_tenants.setArguments(bundle);

                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, fragment_tenants)
                        .addToBackStack(null)
                        .commit();
            }
        });
        btn_delete_tenants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xoaTenants();
            }
        });
        btn_edit_tenants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment_Edit_Tenants fragment_edit_tenants = new Fragment_Edit_Tenants();
                Bundle bundle = new Bundle();
                bundle.putString("idPhong", idPhong);
                bundle.putString("idNguoiThue", idNguoiThue);
                fragment_edit_tenants.setArguments(bundle);

                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, fragment_edit_tenants)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    private void getTenants(){
        Bundle args = getArguments();
        idNguoiThue = args.getString("idNguoiThue");
        idPhong = args.getString("idPhong");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Tenants").child(idNguoiThue);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tenNguoiThue = snapshot.child("ten_Nguoi_Thue").getValue(String.class);
                CCCD = snapshot.child("cccd").getValue(String.class);
                SDT = snapshot.child("sdt").getValue(String.class);
                diaChi = snapshot.child("dia_chi").getValue(String.class);

                tv_ten_nguoi_thue.setText(tenNguoiThue);
                tv_cccd.setText(CCCD);
                tv_sdt.setText(SDT);
                tv_dia_chi.setText(diaChi);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void xoaTenants(){
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Xóa");
        builder.setMessage("Bạn có xóa ngươi thuê này không!");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseReference referenceXoa = FirebaseDatabase.getInstance().getReference("Tenants").child(idNguoiThue);
                dialog.ShowDilag("Đang xóa...");
                referenceXoa.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        dialog.HideDialog();
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
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.HideDialog();
                        Toast.makeText(view.getContext(), "Xóa phòng thất bại!", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.create();
        builder.show();
    }
}
