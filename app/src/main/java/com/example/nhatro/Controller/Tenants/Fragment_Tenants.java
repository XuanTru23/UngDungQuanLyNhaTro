package com.example.nhatro.Controller.Tenants;

import android.annotation.SuppressLint;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhatro.Controller.Room.Fragment_Detail_Room;
import com.example.nhatro.Model.Tenants;
import com.example.nhatro.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Fragment_Tenants extends Fragment {
    private View view;
    private String idPhong, tenPhong;
    private TextView tv_tool_bar;
    private ImageView img_add_tenants, img_back;
    private RecyclerView rcv_tenants;
    private RCVTenantsAdapter rcvTenantsAdapter;
    private ArrayList<Tenants> list;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tenants, container, false);
        anhxa();
        click_event();
        getRoom();
        getTenants();

        list = new ArrayList<>();
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcv_tenants.setLayoutManager(linearLayoutManager);
        rcvTenantsAdapter = new RCVTenantsAdapter(list);
        rcv_tenants.setAdapter(rcvTenantsAdapter);

        rcvTenantsAdapter.setOnItemClickListener(new RCVTenantsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String idNguoiThue) {
                go_To_Fragment_Detail_Tenants(idNguoiThue);
            }
        });

        return view;
    }

    private void go_To_Fragment_Detail_Tenants(String idNguoiThue) {
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

    private void anhxa(){
        tv_tool_bar = view.findViewById(R.id.tv_tool_bar);
        img_add_tenants = view.findViewById(R.id.img_add_tenants);
        img_back = view.findViewById(R.id.img_back);
        rcv_tenants = view.findViewById(R.id.rcv_tenants);

    }
    private void click_event(){
        img_add_tenants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment_Add_Tenants fragment_add_tenants = new Fragment_Add_Tenants();
                Bundle bundle = new Bundle();
                bundle.putString("idPhong", idPhong);
                fragment_add_tenants.setArguments(bundle);

                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, fragment_add_tenants)
                        .addToBackStack(null)
                        .commit();
            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment_Detail_Room fragment_detail_room = new Fragment_Detail_Room();
                Bundle bundle = new Bundle();
                bundle.putString("idPhong", idPhong);
                fragment_detail_room.setArguments(bundle);

                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, fragment_detail_room)
                        .addToBackStack(null)
                        .commit();

            }
        });
    }

    private void getRoom(){
        Bundle args = getArguments();
        idPhong = args.getString("idPhong");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Rooms").child(idPhong);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tenPhong = snapshot.child("ten_Phong").getValue(String.class);

                String text = "Quản lý người thuê " + tenPhong;
                int maxLength = 25; // Đặt độ dài tối đa cho văn bản

                if (text.length() > maxLength) {
                    String trimmedText = text.substring(0, maxLength - 3) + "...";
                    tv_tool_bar.setText(trimmedText);
                } else {
                    tv_tool_bar.setText(text);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getTenants(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Tenants");
        reference.orderByChild("id_Phong").equalTo(idPhong).addChildEventListener(new ChildEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Tenants tenants = snapshot.getValue(Tenants.class);
                if (tenants != null){
                    list.add(tenants);
                    rcvTenantsAdapter.notifyDataSetChanged();
                }
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Tenants tenants = snapshot.getValue(Tenants.class);
                if (list == null || list.isEmpty()){
                    return;
                }

                for (int i = 0; i < list.size(); i++){
                    if (tenants.getId_Nguoi_Thue() == list.get(i).getId_Nguoi_Thue()){
                        list.set(i, tenants);
                        break;
                    }
                }
                rcvTenantsAdapter.notifyDataSetChanged();
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Tenants tenants = snapshot.getValue(Tenants.class);
                if (tenants == null || list == null || list.isEmpty()){
                    return;
                }
                for (int i = 0; i < list.size(); i++){
                    if (tenants.getId_Nguoi_Thue() == list.get(i).getId_Nguoi_Thue()){
                        list.remove(list.get(i));
                        break;
                    }
                }
                rcvTenantsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
