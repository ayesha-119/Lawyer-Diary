package com.uptree.lawdiary.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.uptree.lawdiary.Model.LoginPojo;
import com.uptree.lawdiary.R;
import com.uptree.lawdiary.Retrofit.RetrofitClient;
import com.uptree.lawdiary.sharedPref.SharedPrefManager;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    TextInputEditText pass,email;
    Button login;
    float v=0;
    String userEmail,userPass;
    String strEmail,strPass;
    public static final String TAG = "login";
    SharedPrefManager sharedPrefManager;
    SharedPreferences sharedPreferences;

    private static final int MY_CAMERA_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        takePermissions();

        pass=findViewById(R.id.password);
        email=findViewById(R.id.email);
        login=findViewById(R.id.btn_Login);

        Intent intent=getIntent();
       strEmail = intent.getStringExtra("userEmail");
       strPass =  intent.getStringExtra("userPass");
        if (intent!= null){
            email.setText(strEmail);
            pass.setText(strPass);

        }

        //creating sharedprefmanager object
        sharedPrefManager=new SharedPrefManager(this);
        sharedPreferences = getSharedPreferences("pref", Context.MODE_PRIVATE);

        findViewById(R.id.register).setOnClickListener(this);
        findViewById(R.id.btn_Login).setOnClickListener(this);
        findViewById(R.id.forget_pass).setOnClickListener(this);
    }

    private void takePermissions() {
        if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register:
                startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));
                finish();
                break;
            case R.id.btn_Login:
                String mail = email.getText().toString();
                final String password = pass.getText().toString();

                if (mail.isEmpty()) {
                    email.setError("Please Enter your Email");
                    Toast.makeText(LoginActivity.this, "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.isEmpty() || password.length()<6) {
                    Toast.makeText(LoginActivity.this, "Enter correct password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                loginUser();

                break;
            case R.id.forget_pass:
                startActivity(new Intent(LoginActivity.this,FetchEmailScreen.class));
                finish();

        }
    }

    private void loginUser() {
        userEmail=email.getText().toString();
        userPass=pass.getText().toString();
        Call<LoginPojo> call= RetrofitClient
                .getInstance()
                .getApi()
                .register(userEmail,userPass);
        call.enqueue(new Callback<LoginPojo>() {
            @Override
            public void onResponse(Call<LoginPojo> call, Response<LoginPojo> response) {
                LoginPojo user=response.body();
                String res=user.getResult();
                if (response.isSuccessful()){

                    if (res.equals("Login Successfully")){
                        ProgressDialog progressDialog= new ProgressDialog(LoginActivity.this);
                        progressDialog.setMessage("Please wait...");
                        progressDialog.show();
                        Toast.makeText(LoginActivity.this, res, Toast.LENGTH_SHORT).show();

                        sharedPrefManager.saveUser(user);
                        Intent intent=new Intent(LoginActivity.this, Dashboard.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();

                        Log.d(TAG, "onResponse: isSuccessful"+response);
                    }
                    else
                        {
                            Toast.makeText(LoginActivity.this, res, Toast.LENGTH_SHORT).show();

                        }
                }
                else {
                    Toast.makeText(LoginActivity.this, res, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: isNotSuccessful"+response);
                }
            }

            @Override
            public void onFailure(Call<LoginPojo> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t);

            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        if (sharedPrefManager.isLoggedIn()){
            Intent intent=new Intent(LoginActivity.this, Dashboard.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }
}