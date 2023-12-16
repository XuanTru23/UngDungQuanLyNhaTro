package com.example.nhatro.Controller.Bill_and_Search.Hoa_Don;

import android.app.AlertDialog;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.nhatro.Model.Bills;
import com.example.nhatro.Model.User;
import com.example.nhatro.Controller.Bill_and_Search.Progress_Dialog;
import com.example.nhatro.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class Fragment_Detail_Bill extends Fragment {
    private View view;
    String[] item = {"Chưa nộp tiền", "Đã thu tiền"};
    ArrayAdapter<String> adapterItem;
    private TextView tv_tool_bar, tv_ngay_thu, tv_tong_tien;
    private EditText edt_so_nuoc, edt_so_dien;
    private AutoCompleteTextView auto_complete_tv;
    private float tien_dien, tien_nuoc, tong_tien;
    private AppCompatButton btn_edit_bill, btn_delete_bill;

    private String id_Bills, idPhong, tinh_trang;
    private ImageView img_back;
    private Progress_Dialog dialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_detail_bill, container, false);
        anhxa();
        Bundle args = getArguments();
        id_Bills = args.getString("id_Bills");
        idPhong = args.getString("idPhong");
        get_data();
        click_event();
        autoCompleteTextView();
        edit_bill();
        return view;
    }
    private void anhxa(){
        tv_tool_bar = view.findViewById(R.id.tv_tool_bar);
        img_back = view.findViewById(R.id.img_back);
        tv_ngay_thu = view.findViewById(R.id.tv_ngay_thu);
        tv_tong_tien = view.findViewById(R.id.tv_tong_tien);
        edt_so_nuoc = view.findViewById(R.id.edt_so_nuoc);
        edt_so_dien = view.findViewById(R.id.edt_so_dien);
        btn_edit_bill = view.findViewById(R.id.btn_edit_bill);
        auto_complete_tv = view.findViewById(R.id.auto_complete_tv);
        btn_delete_bill = view.findViewById(R.id.btn_delete_bill);
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
        btn_delete_bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xoa_Bill();
            }
        });
    }

    private void get_data(){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Bills").child(id_Bills);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Bills bills = snapshot.getValue(Bills.class);
                if (bills != null){
                    String tv_Tool_Bar = bills.FormattedDate();
                    String so_Nuoc = bills.getSo_Nuoc();
                    String so_Dien = bills.getSo_Dien();
                    String ngay_Thu = bills.getNgay_Thu();
                    String tong_Tien = bills.getTong_Tien();
                    String tinh_Trang = bills.getTinh_Trang();

                    int maxLength = 25; // Đặt độ dài tối đa cho văn bản
                    if (tv_Tool_Bar.length() > maxLength) {
                        String trimmedText = tv_Tool_Bar.substring(0, maxLength - 3) + "...";
                        tv_tool_bar.setText(trimmedText);
                    } else {
                        tv_tool_bar.setText(tv_Tool_Bar);
                    }

                    edt_so_nuoc.setText(so_Nuoc);
                    edt_so_dien.setText(so_Dien);
                    tv_ngay_thu.setText(ngay_Thu);
                    tv_tong_tien.setText(tong_Tien);
                    auto_complete_tv.setText(tinh_Trang);


                }

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

    private void edit_bill(){
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

                            btn_edit_bill.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.ShowDilag("Đang sửa hóa đơn");
                                    String s_dien = edt_so_dien.getText().toString();
                                    String s_nuoc = edt_so_nuoc.getText().toString();
                                    String ngay_thu = tv_ngay_thu.getText().toString();
                                    String t_tien = tv_tong_tien.getText().toString();
                                    String tinh_trang = auto_complete_tv.getText().toString();

                                    DatabaseReference referenceSua = FirebaseDatabase.getInstance().getReference("Bills").child(id_Bills);
                                    referenceSua.child("so_Nuoc").setValue(s_nuoc);
                                    referenceSua.child("so_Dien").setValue(s_dien);
                                    referenceSua.child("tinh_Trang").setValue(tinh_trang);
                                    referenceSua.child("ngay_Thu").setValue(ngay_thu);
                                    referenceSua.child("Tong_Tien").setValue(t_tien).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    private void del_bill(){

        btn_delete_bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Bills").child(id_Bills);
                dialog.ShowDilag("Đang xóa...");
                reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
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
        });


    }

    private void xoa_Bill(){
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Xóa");
        builder.setMessage("Bạn có xóa hóa đơn này không!");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Bills").child(id_Bills);
                dialog.ShowDilag("Đang xóa...");
                reference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
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
                        dialog.HideDialog();
                        Toast.makeText(view.getContext(), "Xóa hóa đơn thất bại!", Toast.LENGTH_SHORT).show();

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
                tv_ngay_thu.setText(selectedDate);
            }
        }, year, month, day);

        datePickerDialog.show();
    }
}
