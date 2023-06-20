package com.uptree.lawdiary.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.uptree.lawdiary.Model.LoginPojo;
import com.uptree.lawdiary.R;
import com.uptree.lawdiary.Retrofit.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FetchEmailScreen extends AppCompatActivity {
    Toolbar toolbar;
    EditText Email;
    String str_mail,Phone_no;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_email_screen);


        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Email=findViewById(R.id.Email);
        sharedPreferences = getSharedPreferences("pref", Context.MODE_PRIVATE);



        findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_mail=Email.getText().toString().trim();


                if (TextUtils.isEmpty(str_mail)) {
                    Email.setError("Please Enter your Email");
                    Toast.makeText(FetchEmailScreen.this, "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Email", str_mail);
                editor.commit();
                fetchPhoneNo();

            }
        });


        findViewById(R.id.txt_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FetchEmailScreen.this,LoginActivity.class));
                finish();
            }
        });





    }
    private void fetchPhoneNo(){
        Call<LoginPojo> call= RetrofitClient
                .getInstance()
                .getApi()
                .resetPassword(str_mail);
        call.enqueue(new Callback<LoginPojo>() {
            @Override
            public void onResponse(Call<LoginPojo> call, Response<LoginPojo> response) {
                LoginPojo user=response.body();
                if (response.isSuccessful()){

                    String res=user.getResult();
                    if (res.equals("found")){
                        Log.d("TAG", "onResponse: isSuccessful"+response+user.getPhone());
                        Phone_no=user.getPhone();
                        startActivity(new Intent(FetchEmailScreen.this,ManageOTP.class)
                        .putExtra("mobile",Phone_no));
                    }
                    else {
                        Email.setError("Enter valid email");
                        Toast.makeText(FetchEmailScreen.this, "This Email has not been registered yet, please input a valid email", Toast.LENGTH_SHORT).show();

                    }
                }
                else {
                    Log.d("TAG", "onResponse: isNotSuccessful"+response);
                    Toast.makeText(FetchEmailScreen.this, "This is not a registered email,please input a valid email", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginPojo> call, Throwable t) {
                Log.d("TAG", "onFailure: "+t);
                Toast.makeText(FetchEmailScreen.this, "This is not a registered email,please input a valid email", Toast.LENGTH_SHORT).show();


            }
        });
    }
}