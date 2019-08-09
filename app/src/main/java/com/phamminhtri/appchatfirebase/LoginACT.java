package com.phamminhtri.appchatfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginACT extends AppCompatActivity implements View.OnClickListener {
    private ImageView imgLogo;
    private ImageView imgBglogin;
    private EditText edtUsername;
    private EditText edtPassword;
    private Button btnSignin;
    private TextView tvFogotpass;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_act);
        init();
        glideLoad();
    }

    private void glideLoad() {
        Glide.with(this).load(R.drawable.logo).into(imgLogo);
        Glide.with(this).load(R.drawable.bglogin).into(imgBglogin);
    }

    private void init() {
        imgLogo = (ImageView) findViewById(R.id.img_logo);
        imgBglogin = (ImageView) findViewById(R.id.img_bglogin);
        edtUsername = (EditText) findViewById(R.id.edt_username);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        btnSignin = (Button) findViewById(R.id.btn_signin);
        btnRegister = (Button) findViewById(R.id.btn_register);
        tvFogotpass = (TextView) findViewById(R.id.tv_fogotpass);

        btnSignin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_signin:
                signIn(view);
                break;
            case R.id.btn_register:
                startActivity(new Intent(LoginACT.this, SignUpACT.class));
                finish();
                break;
        }

    }

    public void signIn(View view) {

        final String username = edtUsername.getText().toString().trim();
        final String password = edtPassword.getText().toString().trim();


// câu lệnh kiểm tra dữ liệu, cắt khoảng trắng 2 đầu dữ liệu học viên // tự luyện tập

        if (username.equals("") || password.equals("")) {
            Toast.makeText(LoginACT.this, "yêu cầu nhập đủ các trường", Toast.LENGTH_SHORT).show();
        } else if (password.length() <= 5) {
            Toast.makeText(LoginACT.this, "mật khẩu trên 6 kí tự", Toast.LENGTH_SHORT).show();
        }else{
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        // truy vấn vào nhánh username mà người dùng nhập
        DatabaseReference users = firebaseDatabase.getReference("Users").child(username);

        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {

                    Toast.makeText(LoginACT.this,
                            "Người dùng không tồn tại", Toast.LENGTH_SHORT).show();
                } else {

                    // lấy dữ liệu từ dataSnapshot gán vào model User,
                    // lưu ý : biến ở User cần trùng khớp với tên các giá trị trên firebase
                    User user = dataSnapshot.getValue(User.class);

// so sánh mật khẩu người dùng nhập và dữ liệu trên firebase
                    if (user.getPassword().equals(password)) {

                        startActivity(new Intent(LoginACT.this, MainActivity.class));

                    } else {
// thông báo sai mật khẩu
                        Toast.makeText(LoginACT.this, "Sai mật khẩu", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LoginACT.this, "lỗi" + databaseError, Toast.LENGTH_SHORT).show();
            }
              });
    }
}}
