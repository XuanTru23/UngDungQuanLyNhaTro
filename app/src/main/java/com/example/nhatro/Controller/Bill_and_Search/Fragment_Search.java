package com.example.nhatro.Controller.Bill_and_Search;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhatro.Controller.Room.Fragment_Home;
import com.example.nhatro.Model.Tenants;
import com.example.nhatro.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Fragment_Search extends Fragment {
    private View view;
    private EditText edt_Search;
    private ImageView img_back;
    private RecyclerView rcv_search;
    private RCVSearchAdapter rcvSearchAdapter;
    private ArrayList<Tenants> list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);
        anhxa();
        click_event();
        getTenants();
        search();

        list = new ArrayList<>();
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcv_search.setLayoutManager(linearLayoutManager);
        rcvSearchAdapter = new RCVSearchAdapter(list);
        rcv_search.setAdapter(rcvSearchAdapter);

        rcvSearchAdapter.setOnItemClickListener(new RCVSearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String idNguoiThue) {
                go_To_Fragment_Detail_Search(idNguoiThue);
            }
        });


        return view;
    }
    private void anhxa(){
        edt_Search = view.findViewById(R.id.edt_Search);
        img_back = view.findViewById(R.id.img_back);
        rcv_search = view.findViewById(R.id.rcv_search);
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
    }
    private void go_To_Fragment_Detail_Search(String idNguoiThue) {
        Fragment_Detail_Search fragment_detail_search = new Fragment_Detail_Search();
        Bundle bundle = new Bundle();
        bundle.putString("idNguoiThue", idNguoiThue);
        fragment_detail_search.setArguments(bundle);

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_layout, fragment_detail_search)
                .addToBackStack(null)
                .commit();
    }
    private void getTenants(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Tenants");
        reference.addChildEventListener(new ChildEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Tenants tenants = snapshot.getValue(Tenants.class);
                if (tenants != null){
                    list.add(tenants);
                    rcvSearchAdapter.notifyDataSetChanged();
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
                rcvSearchAdapter.notifyDataSetChanged();
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
                rcvSearchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void search(){
        edt_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tu_Khoa = edt_Search.getText().toString().trim().toLowerCase();
                if (tu_Khoa == null || tu_Khoa.isEmpty()){
                    getTenants();
                } else {
                    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Tenants");
                    databaseRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ArrayList<Tenants> list1 = new ArrayList<>();
                            for (DataSnapshot ds : snapshot.getChildren()){
                                Tenants tenants = ds.getValue(Tenants.class);
                                String ten = tenants.getTen_Nguoi_Thue().toLowerCase();
                                int viTri = ten.indexOf(tu_Khoa);
                                if (viTri != -1) {
                                    // Tìm thấy chuỗi con trong chuỗi lớn
                                    ten.substring(0, viTri + tu_Khoa.length());
                                    list1.add(tenants);
                                }

//                                if (tenants.getTen_Nguoi_Thue().equals(tu_Khoa)){
//                                    list1.add(tenants);
//                                }

                            }
                            LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(view.getContext());
                            linearLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
                            rcv_search.setLayoutManager(linearLayoutManager1);
                            rcvSearchAdapter = new RCVSearchAdapter(list1);
                            rcv_search.setAdapter(rcvSearchAdapter);

                            rcvSearchAdapter.setOnItemClickListener(new RCVSearchAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(String idNguoiThue) {
                                    go_To_Fragment_Detail_Search(idNguoiThue);
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });

    }

}
