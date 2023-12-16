package com.example.nhatro.Controller.Bill_and_Search.Hoa_Don;

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
import com.example.nhatro.Model.Bills;
import com.example.nhatro.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Fragment_Bills extends Fragment {
    private View view;

    private ImageView img_add_room, img_back;
    private String idPhong, tenPhong;
    private TextView tv_tool_bar;
    private RecyclerView rcv_bill;
    private RCVBillsAdapter rcvBillsAdapter;
    private ArrayList<Bills> list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bills, container, false);
        anhxa();
        click_event();
        getRoom();
        getBills();

        list = new ArrayList<>();
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcv_bill.setLayoutManager(linearLayoutManager);
        rcvBillsAdapter = new RCVBillsAdapter(list);
        rcv_bill.setAdapter(rcvBillsAdapter);

        rcvBillsAdapter.setOnItemClickListener(new RCVBillsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String id_Bills) {
                go_To_Fragment_Detail_Bill(id_Bills);

            }
        });


        return view;
    }

    private void anhxa(){
        img_back = view.findViewById(R.id.img_back);
        img_add_room = view.findViewById(R.id.img_add_room);
        tv_tool_bar = view.findViewById(R.id.tv_tool_bar);
        rcv_bill = view.findViewById(R.id.rcv_bill);

    }

    private void click_event(){
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
        img_add_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment_Add_Bill fragment_add_bill = new Fragment_Add_Bill();
                Bundle bundle = new Bundle();
                bundle.putString("idPhong", idPhong);
                fragment_add_bill.setArguments(bundle);

                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, fragment_add_bill)
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

                String text = "Quản lý hóa đơn " + tenPhong;
                tv_tool_bar.setText(text);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getBills(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Bills");
        reference.orderByChild("id_Phong").equalTo(idPhong).addChildEventListener(new ChildEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Bills bills = snapshot.getValue(Bills.class);
                if (bills != null){
                    list.add(bills);
                    rcvBillsAdapter.notifyDataSetChanged();
                }
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Bills bills = snapshot.getValue(Bills.class);
                if (list == null || list.isEmpty()){
                    return;
                }

                for (int i = 0; i < list.size(); i++){
                    if (bills.getId_Bill() == list.get(i).getId_Bill()){
                        list.set(i, bills);
                        break;
                    }
                }
                rcvBillsAdapter.notifyDataSetChanged();
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Bills bills = snapshot.getValue(Bills.class);
                if (bills == null || list == null || list.isEmpty()){
                    return;
                }
                for (int i = 0; i < list.size(); i++){
                    if (bills.getId_Bill() == list.get(i).getId_Bill()){
                        list.remove(list.get(i));
                        break;
                    }
                }
                rcvBillsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void go_To_Fragment_Detail_Bill(String id_Bills) {
        Fragment_Detail_Bill fragment_detail_bill = new Fragment_Detail_Bill();
        Bundle bundle = new Bundle();
        bundle.putString("id_Bills", id_Bills);
        bundle.putString("idPhong", idPhong);
        fragment_detail_bill.setArguments(bundle);

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_layout, fragment_detail_bill)
                .addToBackStack(null)
                .commit();
    }




}
