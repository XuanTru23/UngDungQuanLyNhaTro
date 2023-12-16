package com.example.nhatro.Controller.Giao_Dien_Open_App.Tai_Khoan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.nhatro.Model.User;
import com.example.nhatro.Controller.Bill_and_Search.Progress_Dialog;
import com.example.nhatro.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Fragment_Information_Room extends Fragment {
    private View view;
    private ImageView img_back;
    private EditText edt_gia_dien, edt_gia_nuoc;
    private AppCompatButton btn_cap_nhat;
    private Progress_Dialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_information_room, container, false);
        anhxa();
        set_data();
        click_event();
        return view;
    }
    private void anhxa(){
        img_back = view.findViewById(R.id.img_back);
        edt_gia_dien = view.findViewById(R.id.edt_gia_dien);
        edt_gia_nuoc = view.findViewById(R.id.edt_gia_nuoc);
        btn_cap_nhat = view.findViewById(R.id.btn_cap_nhat);
        dialog = new Progress_Dialog(view.getContext());

    }

    private void set_data(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User users = snapshot.getValue(User.class);
                if (users != null){
                    edt_gia_dien.setText(users.getGia_Dien());
                    edt_gia_nuoc.setText(users.getGia_Nuoc());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void click_event(){
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, new Fragment_Setting()).commit();
            }
        });
        btn_cap_nhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update_information_room();
            }
        });
    }

    private void update_information_room(){
        String giaDien = edt_gia_dien.getText().toString();
        String giaNuoc = edt_gia_nuoc.getText().toString();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String idUser = user.getUid();
        dialog.ShowDilag("Đang cập nhất giá điện, giá nước mới...");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("User").child(idUser);
        reference.child("gia_Dien").setValue(giaDien);
        reference.child("gia_Nuoc").setValue(giaNuoc).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                dialog.HideDialog();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, new Fragment_Information_Room()).commit();
            }
        });

    }

}
