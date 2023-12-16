package com.example.nhatro.Controller.Giao_Dien_Open_App.Tai_Khoan;

import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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

import com.bumptech.glide.Glide;
import com.example.nhatro.Controller.Giao_Dien_Open_App.DangKy_DangNhap.Activity_Login;
import com.example.nhatro.Model.User;
import com.example.nhatro.Controller.Bill_and_Search.Progress_Dialog;
import com.example.nhatro.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class Fragment_My_Account extends Fragment {
    private View view;

    private ImageView img_back, img_profile;
    private TextView tv_change_img_logo, tv_change_name, tv_change_email, tv_change_pass, tv_name, tv_email;
    private FirebaseUser user;
    private String imageUrl;
    public static final int REQUEST_CODE_GALLERY = 1;
    private Progress_Dialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_account, container, false);
        anhxa();
        set_profile();
        click_event();
        return view;

    }

    private void anhxa(){
        img_back = view.findViewById(R.id.img_back);
        img_profile = view.findViewById(R.id.img_profile);
        tv_change_img_logo = view.findViewById(R.id.tv_change_img_logo);
        tv_change_name = view.findViewById(R.id.tv_change_name);
        tv_change_email = view.findViewById(R.id.tv_change_email);
        tv_change_pass = view.findViewById(R.id.tv_change_pass);
        tv_name = view.findViewById(R.id.tv_name);
        tv_email = view.findViewById(R.id.tv_email);
        dialog = new Progress_Dialog(view.getContext());

    }

    private void set_profile(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User").child(user.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                imageUrl = snapshot.child("image_Url").getValue(String.class);
                Glide.with(view.getContext())
                        .load(imageUrl)
                        .circleCrop()
                        .into(img_profile);
                tv_name.setText(user.getName_User());
                tv_email.setText(user.getEmail_User());
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

        tv_change_img_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }
        });
        tv_change_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogNewName();
            }
        });
        tv_change_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogNewEmail();
            }
        });
        tv_change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogNewPass();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            saveImageToFirebase(imageUri);
        }
    }

    private void saveImageToFirebase(Uri imageUri) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("Images/" + UUID.randomUUID().toString());
        dialog.ShowDilag("Đang thay đổi ảnh đại diện...");
        storageRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        dialog.HideDialog();
                        // Lưu URL của ảnh vào Firebase Realtime Database
                        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("User").child(user.getUid());
                        databaseRef.child("image_Url").setValue(uri.toString());
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Xử lý khi upload ảnh thất bại
                dialog.HideDialog();
                Toast.makeText(view.getContext(), "Xử lý ảnh thất bại!" + e, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openDialogNewName(){
        final Dialog dialog_name = new Dialog(view.getContext());
        dialog_name.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_name.setContentView(R.layout.dialog_change_name);
        dialog_name.setCancelable(false);
        Window window = dialog_name.getWindow();
        if (window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);

        EditText edt_new = dialog_name.findViewById(R.id.edt_new);
        AppCompatButton btn_huy = dialog_name.findViewById(R.id.btn_huy);
        AppCompatButton btn_gui = dialog_name.findViewById(R.id.btn_gui);

        btn_huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_name.dismiss();
            }
        });

        btn_gui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newName = edt_new.getText().toString();
                if (newName.isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Lỗi!");
                    builder.setMessage("Họ và tên bỏ trống");

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
                    dialog.ShowDilag("Đang cập nhật lại họ và tên.");

                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("User").child(user.getUid());
                    reference1.child("name_User").setValue(newName).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            dialog.HideDialog();
                            dialog_name.dismiss();
                        }
                    });
                }

            }
        });

        dialog_name.show();
    }

    private void openDialogNewEmail(){
        final Dialog dialog_email = new Dialog(view.getContext());
        dialog_email.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_email.setContentView(R.layout.dialog_change_email);
        dialog_email.setCancelable(false);
        Window window = dialog_email.getWindow();
        if (window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);

        TextView tv_tb = dialog_email.findViewById(R.id.tv_tb);
        EditText edt_pass_old = dialog_email.findViewById(R.id.edt_pass_old);
        EditText edt_new = dialog_email.findViewById(R.id.edt_new);
        AppCompatButton btn_huy = dialog_email.findViewById(R.id.btn_huy);
        AppCompatButton btn_gui = dialog_email.findViewById(R.id.btn_gui);

        btn_huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_email.dismiss();
            }
        });

        btn_gui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email_new = edt_new.getText().toString();
                String pass_old = edt_pass_old.getText().toString();
                String email_old = user.getEmail();

                dialog.ShowDilag("Đang cập nhật lại Email mới.");
                if (TextUtils.isEmpty(pass_old)) {
                    dialog.HideDialog();

                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Lỗi!");
                    builder.setMessage("Mật khẩu không được để trống!");

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

                } else if (pass_old.length() < 6) {
                    dialog.HideDialog();

                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Lỗi!");
                    builder.setMessage("Mật khẩu phải từ 6 ký tự trở lên!");

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

                } else if (TextUtils.isEmpty(email_new)) {
                    dialog.HideDialog();

                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Lỗi!");
                    builder.setMessage("Email không được để trống!");

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


                } else if (!email_new.contains("@gmail.com")) {
                    dialog.HideDialog();

                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Lỗi!");
                    builder.setMessage("Email không đúng định dạng!");

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

                } else if (email_new.equalsIgnoreCase(email_old)){
                    dialog.HideDialog();
                    tv_tb.setText("Tài khoản Email mới trùng với tài khoản Email cũ..");
                } else {
                    AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), pass_old);
                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){
                                user.updateEmail(email_new).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()){
                                            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("User").child(user.getUid());
                                            databaseRef.child("email_User").setValue(email_new);
                                            thong_bao1();
                                            dialog_email.dismiss();
                                            dialog.HideDialog();
                                        } else {
                                            dialog.HideDialog();
                                            tv_tb.setText("Thay đổi Email mới thất bại. Vui lòng kiểm tra lại thông tin muốn thay đổi.");
                                        }
                                    }
                                });

                            } else {
                                dialog.HideDialog();
                                tv_tb.setText("Quá trình xác thực mật khẩu cũ với tài khoản đang đăng nhập thất bại. Vui lòng nhập đúng mật khẩu hiện tại để được phép cập nhật Email mới.");
                            }
                        }
                    });

                }

            }
        });
        dialog_email.show();
    }

    private void openDialogNewPass(){
        final Dialog dialog_pass = new Dialog(view.getContext());
        dialog_pass.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_pass.setContentView(R.layout.dialog_change_pass);
        dialog_pass.setCancelable(false);
        Window window = dialog_pass.getWindow();
        if (window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);

        TextView tv_tb = dialog_pass.findViewById(R.id.tv_tb);
        EditText edt_pass_old = dialog_pass.findViewById(R.id.edt_pass_old);
        EditText edt_new = dialog_pass.findViewById(R.id.edt_new);
        AppCompatButton btn_huy = dialog_pass.findViewById(R.id.btn_huy);
        AppCompatButton btn_gui = dialog_pass.findViewById(R.id.btn_gui);

        btn_huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_pass.dismiss();
            }
        });

        btn_gui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass_new = edt_new.getText().toString();
                String pass_old = edt_pass_old.getText().toString();

                dialog.ShowDilag("Đang cập nhật lại Email mới.");
                if (TextUtils.isEmpty(pass_old) || TextUtils.isEmpty(pass_new)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Lỗi!");
                    builder.setMessage("Mật khẩu hiện tại không được để trống!");

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
                    dialog.HideDialog();

                } else if (pass_old.length() < 6 || pass_new.length() < 6) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Lỗi!");
                    builder.setMessage("Mật khẩu phải từ 6 ký tự trở lên.");

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
                    dialog.HideDialog();
                } else {
                    AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), pass_old);
                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){
                                user.updatePassword(pass_new).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            dialog_pass.dismiss();
                                            dialog.HideDialog();
                                        } else {
                                            tv_tb.setText("Thay đổi mật khẩu mới thất bại. Vui lòng kiểm tra lại thông tin muốn thay đổi.");
                                            dialog.HideDialog();
                                        }
                                    }
                                });

                            } else {
                                tv_tb.setText("Quá trình xác thực mật khẩu cũ với tài khoản đang đăng nhập thất bại. Vui lòng nhập đúng mật khẩu hiện tại để được phép cập nhật mật khẩu mới.");
                                dialog.HideDialog();
                            }

                        }
                    });

                }
            }
        });

        dialog_pass.show();
    }

    private void thong_bao1(){
        final Dialog dialog1 = new Dialog(view.getContext());
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.dialog_thong_bao);
        dialog1.setCancelable(false);
        Window window = dialog1.getWindow();
        if (window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);

        AppCompatButton btn_click = dialog1.findViewById(R.id.btn_click);
        TextView tv_thong_bao_dialog = dialog1.findViewById(R.id.tv_thong_bao_dialog);

        user.getEmail();
        tv_thong_bao_dialog.setText("Chú ý khi ấn vào nút đồng ý hệ thống sẽ đăng xuất tài khoản bạn. Bạn lên đăng nhập lại bằng Email mới và xác thực tài khoản.");
        btn_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(view.getContext(), Activity_Login.class);
                startActivity(intent);
                requireActivity().finishAffinity();
                dialog1.dismiss();
            }
        });
        dialog1.show();
    }


}
