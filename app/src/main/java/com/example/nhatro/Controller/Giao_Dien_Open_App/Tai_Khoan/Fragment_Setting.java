package com.example.nhatro.Controller.Giao_Dien_Open_App.Tai_Khoan;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.example.nhatro.Controller.Giao_Dien_Open_App.DangKy_DangNhap.Activity_Login;
import com.example.nhatro.Model.User;
import com.example.nhatro.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Fragment_Setting extends Fragment {
    private View view;
    private ImageView img_profile;
    private TextView tv_name_profile;
    private ConstraintLayout log_out, my_account, information_room, Help_Support ,Privacy_Policy;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting, container, false);
        anhxa();
        click_event();
        set_profile();
        return view;
    }
    private void anhxa(){
        log_out = view.findViewById(R.id.log_out);
        img_profile = view.findViewById(R.id.img_profile);
        tv_name_profile = view.findViewById(R.id.tv_name_profile);
        my_account = view.findViewById(R.id.my_account);
        information_room = view.findViewById(R.id.information_room);
        Help_Support = view.findViewById(R.id.Help_Support);
        Privacy_Policy = view.findViewById(R.id.Privacy_Policy);
    }

    private void click_event(){
        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(), Activity_Login.class);
                startActivity(intent);
                getActivity().finishAffinity();
            }
        });
        my_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, new Fragment_My_Account()).commit();
            }
        });
        information_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, new Fragment_Information_Room()).commit();
            }
        });
    }

    private void set_profile(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User users = snapshot.getValue(User.class);
                String imageUrl = snapshot.child("image_Url").getValue(String.class);

                // Sử dụng thư viện Glide để tải và hiển thị ảnh từ URL
                Glide.with(view.getContext().getApplicationContext())
                        .load(imageUrl)
                        .circleCrop()
                        .into(img_profile);

                if (users != null){
                    tv_name_profile.setText(users.getName_User());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
