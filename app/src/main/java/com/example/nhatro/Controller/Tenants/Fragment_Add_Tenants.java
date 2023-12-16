package com.example.nhatro.Controller.Tenants;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.nhatro.Model.Tenants;
import com.example.nhatro.Controller.Bill_and_Search.Progress_Dialog;
import com.example.nhatro.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Fragment_Add_Tenants extends Fragment {
    private View view;
    private String idPhong, tenPhong;
    private EditText edt_ten_nguoi_thue, edt_cccd, edt_sdt, edt_dia_chi;
    private AppCompatButton btn_add_tenants;
    private TextView tv_tool_bar;
    private ImageView img_back;
    private Progress_Dialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_tenants, container, false);
        anhxa();
        click_event();
        getTenants();
        return view;
    }
    private void anhxa(){
        tv_tool_bar = view.findViewById(R.id.tv_tool_bar);
        img_back = view.findViewById(R.id.img_back);
        edt_ten_nguoi_thue = view.findViewById(R.id.edt_ten_nguoi_thue);
        edt_cccd = view.findViewById(R.id.edt_cccd);
        edt_sdt = view.findViewById(R.id.edt_sdt);
        edt_dia_chi = view.findViewById(R.id.edt_dia_chi);
        btn_add_tenants = view.findViewById(R.id.btn_add_tenants);
        dialog = new Progress_Dialog(view.getContext());
    }
    private void click_event(){
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment_Tenants fragment__tenants = new Fragment_Tenants();
                Bundle bundle = new Bundle();
                bundle.putString("idPhong", idPhong);
                fragment__tenants.setArguments(bundle);

                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, fragment__tenants)
                        .addToBackStack(null)
                        .commit();

            }
        });
        btn_add_tenants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themTenants();
            }
        });
    }
    private void getTenants(){
        Bundle args = getArguments();
        idPhong = args.getString("idPhong");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Rooms").child(idPhong);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tenPhong = snapshot.child("ten_Phong").getValue(String.class);

                String text = "Thêm người thuê " + tenPhong;
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

    private void themTenants(){
        String ten_Nguoi_Thue = edt_ten_nguoi_thue.getText().toString();
        String CCCD = edt_cccd.getText().toString();
        String SDT = edt_sdt.getText().toString();
        String dia_Chi = edt_dia_chi.getText().toString();

        if (ten_Nguoi_Thue.isEmpty()){
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("Lỗi!");
            builder.setMessage("Không được bỏ trống tên người thuê");

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
        } else if (CCCD.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("Lỗi!");
            builder.setMessage("Không được bỏ trống CCCD");

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
        } else if (CCCD.length() != 12) {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("Lỗi!");
            builder.setMessage("Căn cước công dân phải đủ 12 số");

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
        } else if (SDT.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("Lỗi!");
            builder.setMessage("Không được bỏ trống số điện thoại");

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
        } else if (SDT.length() != 10) {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("Lỗi!");
            builder.setMessage("Số điện thoại không xác định.");

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
        } else if (dia_Chi.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("Lỗi!");
            builder.setMessage("Không được bỏ trống địa chỉ");

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
            dialog.ShowDilag("Đang thêm người thuê...");
            // Tạo node Rooms
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tenants");
            // Tạo khóa tự động và lấy giá trị của từng room con
            String id_Tenants = reference.push().getKey();

            Tenants tenants = new Tenants();

            tenants.setId_Nguoi_Thue(id_Tenants);
            tenants.setId_Phong(idPhong);
            tenants.setTen_Nguoi_Thue(ten_Nguoi_Thue);
            tenants.setCCCD(CCCD);
            tenants.setSDT(SDT);
            tenants.setDia_chi(dia_Chi);
            reference.child(id_Tenants).setValue(tenants).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    dialog.HideDialog();

                    Fragment_Tenants fragment__tenants = new Fragment_Tenants();
                    Bundle bundle = new Bundle();
                    bundle.putString("idPhong", idPhong);
                    fragment__tenants.setArguments(bundle);

                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_layout, fragment__tenants)
                            .addToBackStack(null)
                            .commit();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.HideDialog();
                    Toast.makeText(view.getContext(), "Thêm phòng thất bại.", Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

}
