package com.phamminhtri.appchatfirebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class SignUpACT extends AppCompatActivity {
    private ImageView imgLogo;
    private TextView tvTitle;
    private EditText edtFirstname;
    private EditText edtLastname;
    private EditText edtUsername;
    private EditText edtPassword;
    private Button btnRegister;
    private ImageView imgBglogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_act);
        intitsg();
        glideLoad();
        buttonclick();
    }

    private void buttonclick() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get all value in edit text
                final String firstName = edtFirstname.getText().toString().trim();
                final String lastName = edtLastname.getText().toString().trim();
                final String username = edtUsername.getText().toString().trim();
                final String password = edtPassword.getText().toString().trim();
                // dont forget validating value
                if (firstName.equals("")||lastName.equals("")||password.equals("")) {
                    Toast.makeText(SignUpACT.this, "yêu cầu nhập đủ các trường", Toast.LENGTH_SHORT).show();
                }else if(password.length()<=5){
                    Toast.makeText(SignUpACT.this, "mật khẩu trên 6 kí tự", Toast.LENGTH_SHORT).show();
                }else{

                // Firstly, we check username is exits or not
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference users = database.getReference("Users").child(username);

                users.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        // chua co user voi username duoc nhap
                        if (dataSnapshot.getValue() == null) {
                            // hoc vien tu khoi tao model User
                            User user = new User();
                            user.setFirstName(firstName);
                            user.setLastName(lastName);
                            user.setPassword(password);

                            // them user vao nhanh Users
                            users.setValue(user, new DatabaseReference.CompletionListener() {

                                @Override
                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                                    // hoc vien tu viet va kiem tra su kien loi va thanh cong
                                    if (databaseError != null) {
                                        Toast.makeText(SignUpACT.this, "lỗi đăng kí :" + databaseError, Toast.LENGTH_SHORT).show();
                                    } else if (databaseReference != null) {
                                        Toast.makeText(SignUpACT.this, "đăng kí thành công", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(SignUpACT.this, LoginACT.class));
                                        finish();
                                    }
                                }
                            });

                            // username da ton tai, thong bao chon username khac
                        } else {
                            Toast.makeText(SignUpACT.this,
                                    "Username đã tồn tại, vui lòng chọn username mới", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }}
        });
    }

    private void intitsg() {

        imgLogo = (ImageView) findViewById(R.id.img_logo);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        edtFirstname = (EditText) findViewById(R.id.edt_firstname);
        edtLastname = (EditText) findViewById(R.id.edt_lastname);
        edtUsername = (EditText) findViewById(R.id.edt_username);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        btnRegister = (Button) findViewById(R.id.btn_register);
        imgBglogin = (ImageView) findViewById(R.id.img_bglogin);

    }

    private void glideLoad() {
        Glide.with(this).load(R.drawable.logo).into(imgLogo);
        Glide.with(this).load(R.drawable.bglogin).into(imgBglogin);
    }
}
