package com.example.nhatro.Controller.Tenants;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
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

public class Fragment_Edit_Tenants extends Fragment {
    private View view;

    private String idPhong, idNguoiThue, tenNguoiThue, CCCD, SDT, diaChi;

    private EditText edt_ten_nguoi_thue, edt_cccd, edt_sdt, edt_dia_chi;
    private AppCompatButton btn_edit_room;
    private ImageView img_back;
    private Progress_Dialog dialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_tenants, container, false);
        anhxa();
        getTenants();
        click_event();
        return view;
    }

    private void anhxa(){
        img_back = view.findViewById(R.id.img_back);
        edt_ten_nguoi_thue = view.findViewById(R.id.edt_ten_nguoi_thue);
        edt_cccd = view.findViewById(R.id.edt_cccd);
        edt_sdt = view.findViewById(R.id.edt_sdt);
        edt_dia_chi = view.findViewById(R.id.edt_dia_chi);
        btn_edit_room = view.findViewById(R.id.btn_edit_room);
        dialog = new Progress_Dialog(view.getContext());
    }
    private void click_event(){
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment_Detail_Tenants fragment_detail_tenants = new Fragment_Detail_Tenants();
                Bundle bundle = new Bundle();
                bundle.putString("idPhong", idPhong);
                bundle.putString("idNguoiThue", idNguoiThue);
                fragment_detail_tenants.setArguments(bundle);

                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, fragment_detail_tenants)
                        .addToBackStack(null)
                        .commit();

            }
        });
        btn_edit_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTenants();
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

                edt_ten_nguoi_thue.setText(tenNguoiThue);
                edt_cccd.setText(CCCD);
                edt_sdt.setText(SDT);
                edt_dia_chi.setText(diaChi);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void editTenants(){
        String tenNguoiThue = edt_ten_nguoi_thue.getText().toString();
        String cccd = edt_cccd.getText().toString();
        String sdt = edt_sdt.getText().toString();
        String diachi = edt_dia_chi.getText().toString();

        if (tenNguoiThue.isEmpty()){

        } else if (cccd.isEmpty()) {

        } else if (sdt.isEmpty()) {

        } else if (diachi.isEmpty()) {

        } else {
            dialog.ShowDilag("Đang sửa...");
            DatabaseReference referenceSua = FirebaseDatabase.getInstance().getReference("Tenants").child(idNguoiThue);
            referenceSua.child("ten_Nguoi_Thue").setValue(tenNguoiThue);
            referenceSua.child("cccd").setValue(cccd);
            referenceSua.child("sdt").setValue(sdt);
            referenceSua.child("dia_chi").setValue(diachi).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    dialog.HideDialog();

                    Fragment_Detail_Tenants fragment_detail_tenants = new Fragment_Detail_Tenants();
                    Bundle bundle = new Bundle();
                    bundle.putString("idPhong", idPhong);
                    bundle.putString("idNguoiThue", idNguoiThue);
                    fragment_detail_tenants.setArguments(bundle);

                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_layout, fragment_detail_tenants)
                            .addToBackStack(null)
                            .commit();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.HideDialog();
                    Toast.makeText(view.getContext(), "Sửa người thuê thất bại!", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

}
