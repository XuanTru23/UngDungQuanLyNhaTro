package com.example.nhatro.Controller.Room;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhatro.Controller.Bill_and_Search.Fragment_Search;
import com.example.nhatro.R;
import com.example.nhatro.Model.Rooms;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Fragment_Home extends Fragment {
    private View view;
    private ImageView img_add_room, img_search;
    private RCVHomeAdapter rcvHomeAdapter;
    private RecyclerView rcv_home;
    private ArrayList<Rooms> list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        anhxa();
        click_event();

        list = new ArrayList<>();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), 3);
        rcv_home.setLayoutManager(gridLayoutManager);
        rcvHomeAdapter = new RCVHomeAdapter(list);
        rcv_home.setAdapter(rcvHomeAdapter);

        rcvHomeAdapter.setOnItemClickListener(new RCVHomeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String idPhong) {
                go_To_Fragment_Detail_Room(idPhong);
            }
        });
        getRooms();

        return view;
    }

    private void go_To_Fragment_Detail_Room(String idPhong) {
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

    private void anhxa(){
        rcv_home = view.findViewById(R.id.rcv_home);
        img_add_room = view.findViewById(R.id.img_add_room);
        img_search = view.findViewById(R.id.img_search);

    }
    private void click_event(){
        img_add_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, new Fragment_Add_Room()).commit();
            }
        });
        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, new Fragment_Search()).commit();
            }
        });
    }

    private void getRooms(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String idUser = user.getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Rooms");
        reference.orderByChild("id_User").equalTo(idUser).addChildEventListener(new ChildEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Rooms rooms = snapshot.getValue(Rooms.class);
                if (rooms != null){
                    list.add(rooms);
                    rcvHomeAdapter.notifyDataSetChanged();
                }
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Rooms rooms = snapshot.getValue(Rooms.class);
                if (list == null || list.isEmpty()){
                    return;
                }

                for (int i = 0; i < list.size(); i++){
                    if (rooms.getId_Phong() == list.get(i).getId_Phong()){
                        list.set(i, rooms);
                        break;
                    }
                }
                rcvHomeAdapter.notifyDataSetChanged();
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Rooms rooms = snapshot.getValue(Rooms.class);
                if (rooms == null || list == null || list.isEmpty()){
                    return;
                }
                for (int i = 0; i < list.size(); i++){
                    if (rooms.getId_Phong() == list.get(i).getId_Phong()){
                        list.remove(list.get(i));
                        break;
                    }
                }
                rcvHomeAdapter.notifyDataSetChanged();
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
