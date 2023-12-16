package com.example.nhatro.Controller.Room;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
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

import java.util.Calendar;

public class Fragment_Edit_Room extends Fragment {
    private View view;
    String[] item = {"Chưa có người thuê", "Đã có ngươi thuê"};
    ArrayAdapter<String> adapterItem;

    private String idPhong, tenPhong, tinhTrang, ngayThue, ngayThuTien, giaPhong, tinh_trang;
    private TextView tv_tool_bar, tv_ngay_thue;
    private EditText edt_ten_phong, edt_gia_phong, edt_thu_tien;
    private AutoCompleteTextView auto_complete_tv;
    private AppCompatButton btn_edit_room, btn_delete_room;
    private ImageView img_back;
    private Progress_Dialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_room, container, false);
        anhxa();
        getPhong();
        click_event();
        autoCompleteTextView();

        return view;
    }
    private void anhxa(){
        img_back = view.findViewById(R.id.img_back);
        tv_tool_bar = view.findViewById(R.id.tv_tool_bar);
        tv_ngay_thue = view.findViewById(R.id.tv_ngay_thue);
        edt_ten_phong = view.findViewById(R.id.edt_ten_phong);
        edt_thu_tien = view.findViewById(R.id.edt_thu_tien);
        edt_gia_phong = view.findViewById(R.id.edt_gia_phong);
        auto_complete_tv = view.findViewById(R.id.auto_complete_tv);
        btn_edit_room = view.findViewById(R.id.btn_edit_room);
        btn_delete_room = view.findViewById(R.id.btn_delete_room);
        dialog = new Progress_Dialog(view.getContext());

    }

    private void autoCompleteTextView(){
        auto_complete_tv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    adapterItem = new ArrayAdapter<String>(view.getContext(), R.layout.list_item, item);
                    auto_complete_tv.setAdapter(adapterItem);
                    auto_complete_tv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            tinh_trang = adapterView.getItemAtPosition(i).toString();
                            if (tinh_trang.equals("Chưa có người thuê")) {
                                edt_gia_phong.setText("");
                                edt_thu_tien.setText("");
                                tv_ngay_thue.setText("");
                                tv_ngay_thue.setEnabled(false);
                                edt_gia_phong.setEnabled(false);
                                edt_thu_tien.setEnabled(false);
                            } else {
                                edt_thu_tien.setEnabled(true);
                                edt_gia_phong.setEnabled(true);
                                tv_ngay_thue.setEnabled(true);
                            }
                        }
                    });
                }
            }
        });

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

        btn_delete_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xoaPhong();
            }
        });
        btn_edit_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                suaPhong();
            }
        });
        tv_ngay_thue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_calendar();
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

                String text = "Chi tiết " + tenPhong;
                tv_tool_bar.setText(text);
                edt_ten_phong.setText(tenPhong);
                auto_complete_tv.setText(tinhTrang);
                tv_ngay_thue.setText(ngayThue);
                edt_thu_tien.setText(ngayThuTien);
                edt_gia_phong.setText(giaPhong);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void suaPhong(){

        String ten_phong = edt_ten_phong.getText().toString();
        String ngay_Thue = tv_ngay_thue.getText().toString();
        String ngay_Thu_Tien = edt_thu_tien.getText().toString();
        String gia_phong = edt_gia_phong.getText().toString();
        String TinhTrang = auto_complete_tv.getText().toString();

        if (ten_phong.isEmpty()) {
            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(view.getContext());
            builder.setTitle("Lỗi!");
            builder.setMessage("Bỏ trống tên phòng");

            // Thêm nút "Đóng" vào thông báo
            builder.setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            // Hiển thị thông báo
            androidx.appcompat.app.AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else {
            dialog.ShowDilag("Đang sửa...");

            DatabaseReference referenceSua = FirebaseDatabase.getInstance().getReference("Rooms").child(idPhong);

            referenceSua.child("ten_Phong").setValue(ten_phong);
            referenceSua.child("ngay_Thue").setValue(ngay_Thue);
            referenceSua.child("tinh_Trang").setValue(TinhTrang);
            referenceSua.child("ngay_thu_tien").setValue(ngay_Thu_Tien);
            referenceSua.child("gia_Phong").setValue(gia_phong).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    dialog.HideDialog();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_layout, new Fragment_Home()).commit();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.HideDialog();
                    Toast.makeText(view.getContext(), "Sửa phòng thất bại!", Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    private void xoaPhong(){
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Xóa");
        builder.setMessage("Bạn có xóa phòng này không!");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseReference referenceXoa = FirebaseDatabase.getInstance().getReference("Rooms").child(idPhong);
                dialog.ShowDilag("Đang xóa...");
                referenceXoa.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        dialog.HideDialog();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.frame_layout, new Fragment_Home()).commit();
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

    private void open_calendar(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // Xử lý ngày được chọn
                String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                tv_ngay_thue.setText(selectedDate);
            }
        }, year, month, day);

        datePickerDialog.show();
    }
}
