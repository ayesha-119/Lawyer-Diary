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
import com.uptree.lawdiary.sharedPref.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePassword extends AppCompatActivity {
    String email;
    EditText pass,confirm_pass;
    Button btn_reset;
    String str_pass,str_confirm;
    public static final String TAG = "ChangePassword";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("pref", Context.MODE_PRIVATE);
        email = sharedPreferences.getString("Email",null);


        setContentView(R.layout.activity_change_password);


        pass=findViewById(R.id.pass);
        confirm_pass=findViewById(R.id.confirm_pass);
        btn_reset=findViewById(R.id.btn_reset);
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if(isValid())
                {
                    str_pass=pass.getText().toString();
                    retroFit(email,str_pass);
                }

            }
        });

    }

    private boolean isValid()
    {
        str_pass = pass.getText().toString();
        str_confirm=confirm_pass.getText().toString();

        if (str_pass.isEmpty() || str_pass.length() < 6)
        {
            pass.setError("Password should be atleast 6 digits long");
            return false;
        }

        if (!str_pass.equals(str_confirm))
        {
            confirm_pass.setError("confirm password is not correct");
            return false;
        }
        return true;
    }

    private void retroFit(String email,String pass)
    {
        Toast.makeText(this, "email : "+email +" \n" +"pass : "+pass, Toast.LENGTH_SHORT).show();

        Call<LoginPojo> call= RetrofitClient
                .getInstance()
                .getApi()
                .updatePassword(email,pass);
        call.enqueue(new Callback<LoginPojo>() {
            @Override
            public void onResponse(Call<LoginPojo> call, Response<LoginPojo> response) {
                LoginPojo user=response.body();

                if (response.isSuccessful()){
                    String res=user.getResult();

                    Log.d(TAG, "onResponse: isSuccessful "+user.getResult());

                    if (res.equals("Update successfully"))
                    {
                        Log.d("value", "onResponse: "+email+str_pass);
                        Intent intent=new Intent(ChangePassword.this, LoginActivity.class);
                       // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                        Log.d(TAG, "onResponse: isSuccessful"+res);
                        Toast.makeText(ChangePassword.this, "password updated! ", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Log.d(TAG, "onResponse: isNotSuccessful"+user.getResult());
                }
            }

            @Override
            public void onFailure(Call<LoginPojo> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t);

            }
        });
    }
}