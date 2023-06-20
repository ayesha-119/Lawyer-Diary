package com.uptree.lawdiary.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.uptree.lawdiary.Model.LoginPojo;
import com.uptree.lawdiary.R;
import com.uptree.lawdiary.Retrofit.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    String email;
    EditText pass,confirm_pass;
    Button btn_reset;
    String str_pass,str_confirm;
    public static final String TAG = "reset";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
        sharedPreferences = getSharedPreferences("pref", Context.MODE_PRIVATE);
        email=sharedPreferences.getString("Email", "");
        pass=findViewById(R.id.pass);
        confirm_pass=findViewById(R.id.confirm_pass);
        btn_reset=findViewById(R.id.btn_reset);
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_pass=pass.getText().toString();
                str_confirm=confirm_pass.getText().toString();
                if (str_pass.equals(""))
                    pass.setError("Please Enter Password");
                if (str_pass.length()<6){
                    pass.setError("Password should be atleast 6 digits long");
                }
                if (!str_pass.equals(str_confirm)){
                    Toast.makeText(ResetActivity.this, "Password and confirm password don't match", Toast.LENGTH_SHORT).show();
                }
                retroFit();
            }
        });
    }

    private void retroFit() {
        Call<LoginPojo> call= RetrofitClient
                .getInstance()
                .getApi()
                .resetPassword(email);
        call.enqueue(new Callback<LoginPojo>() {
            @Override
            public void onResponse(Call<LoginPojo> call, Response<LoginPojo> response) {
                LoginPojo user=response.body();
                if (response.isSuccessful()){
                    Intent intent=new Intent(ResetActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();

                    Log.d(TAG, "onResponse: isSuccessful"+response);
                }
                else {
                    Log.d(TAG, "onResponse: isNotSuccessful"+response);
                }
            }

            @Override
            public void onFailure(Call<LoginPojo> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t);

            }
        });
    }
}