package com.example.nhatro.Controller.Bill_and_Search.Hoa_Don;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.nhatro.Controller.Bill_and_Search.Progress_Dialog;
import com.example.nhatro.Model.Bills;
import com.example.nhatro.Model.User;
import com.example.nhatro.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class Fragment_Add_Bill extends Fragment {
    private View view;
    String[] item = {"Chưa nộp tiền", "Đã thu tiền"};
    ArrayAdapter<String> adapterItem;

    private String idPhong, tinh_trang;
    private TextView tv_tool_bar, tv_ngay_thu, tv_tong_tien;
    private EditText edt_so_nuoc, edt_so_dien;
    private AutoCompleteTextView auto_complete_tv;
    private ImageView img_back;
    private float tien_dien, tien_nuoc, tong_tien;

    private AppCompatButton btn_add_bill;
    private Progress_Dialog dialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_bill, container, false);
        anhxa();
        getRoom();
        autoCompleteTextView();
        click_event();
        add_bill();

        return view;
    }
    private void anhxa(){
        tv_tool_bar = view.findViewById(R.id.tv_tool_bar);
        tv_ngay_thu = view.findViewById(R.id.tv_ngay_thu);
        tv_tong_tien = view.findViewById(R.id.tv_tong_tien);
        edt_so_nuoc = view.findViewById(R.id.edt_so_nuoc);
        edt_so_dien = view.findViewById(R.id.edt_so_dien);
        auto_complete_tv = view.findViewById(R.id.auto_complete_tv);
        img_back = view.findViewById(R.id.img_back);
        btn_add_bill = view.findViewById(R.id.btn_add_bill);
        dialog = new Progress_Dialog(view.getContext());
    }
    private void click_event(){
        img_back.setOnClickListener(new View.OnClickListener() {
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
        tv_ngay_thu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_calendar();
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
                String tenPhong = snapshot.child("ten_Phong").getValue(String.class);

                String text = "Thêm hóa đơn " + tenPhong;
                tv_tool_bar.setText(text);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
                        }
                    });
                }
            }
        });

    }

    private void add_bill(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User").child(user.getUid());
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("Rooms").child(idPhong);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String giaNuoc, giaDien;
                User users = snapshot.getValue(User.class);
                if (users != null){
                    giaNuoc = users.getGia_Nuoc();
                    giaDien = users.getGia_Dien();

                    reference1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            String giaPhong = snapshot.child("gia_Phong").getValue(String.class);
                            float gia_nuoc = Float.parseFloat(giaNuoc);
                            float gia_dien= Float.parseFloat(giaDien);
                            float gia_phong= Float.parseFloat(giaPhong);

                            edt_so_dien.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                }

                                @Override
                                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                    String so_dien = edt_so_dien.getText().toString();

                                    if (!so_dien.isEmpty()){
                                        float sd = Float.parseFloat(so_dien);
                                        tien_dien = sd * gia_dien;
                                        tong_tien = gia_phong + tien_dien + tien_nuoc;
                                        String tt = String.valueOf(tong_tien);
                                        tv_tong_tien.setText(tt);
                                    } else {
                                        tien_dien = 0;
                                        tong_tien = gia_phong + tien_dien + tien_nuoc;
                                        String tt = String.valueOf(tong_tien);
                                        tv_tong_tien.setText(tt);
                                    }


                                }

                                @Override
                                public void afterTextChanged(Editable editable) {

                                }
                            });

                            edt_so_nuoc.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                }

                                @Override
                                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                    String so_nuoc = edt_so_nuoc.getText().toString();

                                    if (!so_nuoc.isEmpty()){
                                        float sn = Float.parseFloat(so_nuoc);
                                        tien_nuoc = sn * gia_nuoc;
                                        tong_tien = gia_phong + tien_dien + tien_nuoc;
                                        String tt = String.valueOf(tong_tien);
                                        tv_tong_tien.setText(tt);

                                    } else {
                                        tien_nuoc = 0;
                                        tong_tien = gia_phong + tien_dien + tien_nuoc;
                                        String tt = String.valueOf(tong_tien);
                                        tv_tong_tien.setText(tt);
                                    }
                                }

                                @Override
                                public void afterTextChanged(Editable editable) {

                                }
                            });

                            btn_add_bill.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    String s_dien = edt_so_dien.getText().toString();
                                    String s_nuoc = edt_so_nuoc.getText().toString();
                                    String ngay_thu = tv_ngay_thu.getText().toString();
                                    String t_tien = tv_tong_tien.getText().toString();
                                    String tinh_trang = auto_complete_tv.getText().toString();
                                    if (s_nuoc.isEmpty()){
                                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                                        builder.setTitle("Lỗi!");
                                        builder.setMessage("Không được bỏ trống số nước");

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
                                    } else if (s_dien.isEmpty()) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                                        builder.setTitle("Lỗi!");
                                        builder.setMessage("Không được bỏ trống số điện");

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
                                    } else if (ngay_thu.isEmpty()) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                                        builder.setTitle("Lỗi!");
                                        builder.setMessage("Không được bỏ trống ngày thu");

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
                                    } else if (tinh_trang.isEmpty()) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                                        builder.setTitle("Lỗi!");
                                        builder.setMessage("Không được bỏ trống tình trạng");

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
                                    } else {
                                        // Lấy ID người dùng
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        String idUser = user.getUid();

                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Bills");
                                        String id_Bilss = reference.push().getKey();
                                        Bills bills = new Bills();
                                        bills.setId_Bill(id_Bilss);
                                        bills.setId_Phong(idPhong);
                                        bills.setId_User(idUser);
                                        bills.setSo_Dien(s_dien);
                                        bills.setSo_Nuoc(s_nuoc);
                                        bills.setNgay_Thu(ngay_thu);
                                        bills.setTong_Tien(t_tien);
                                        bills.setTinh_Trang(tinh_trang);
                                        dialog.ShowDilag("Đang tạo hóa đơn");
                                        reference.child(id_Bilss).setValue(bills).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                dialog.HideDialog();
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
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });
                                    }




                                }
                            });


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
                tv_ngay_thu.setText(selectedDate);
            }
        }, year, month, day);

        datePickerDialog.show();
    }


}
