package com.uptree.lawdiary.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.uptree.lawdiary.Model.LoginPojo;
import com.uptree.lawdiary.R;
import com.uptree.lawdiary.Retrofit.RetrofitClient;
import com.uptree.lawdiary.sharedPref.SharedPrefManager;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTPActivity extends AppCompatActivity {
    PinView pinView;
    Button btn;
    TextView txt_phone;
    String id,phone,email;
    SharedPrefManager sharedPrefManager;
    SharedPreferences sharedPreferences;
    private static final String TAG ="phone";
    String otpid;
    FirebaseAuth mAuth;

    Toolbar toolbar;

    private PhoneAuthProvider.ForceResendingToken mResendToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPrefManager=new SharedPrefManager(getApplicationContext());
        id=sharedPrefManager.getUser().getId();
        email=sharedPrefManager.getUser().getEmail();
        setContentView(R.layout.activity_otpactivity);


        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView textView = findViewById(R.id.resend_verification);


        sharedPreferences = getSharedPreferences("pref", Context.MODE_PRIVATE);

        mAuth=FirebaseAuth.getInstance();
        pinView = findViewById(R.id.firstPinView);
        btn=findViewById(R.id.b1);
        txt_phone=findViewById(R.id.phone_no);
        openDialog();

        //initiateotp();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pinView.getText().toString().isEmpty())
                    Toast.makeText(OTPActivity.this, "Blank field cannot be processed ",Toast.LENGTH_SHORT).show();
                else if (pinView.getText().toString().length()!=6)
                    Toast.makeText(OTPActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                else {
                    PhoneAuthCredential credential=PhoneAuthProvider.getCredential(otpid,pinView.getText().toString());
                    signInWithPhoneAuthCredential(credential);
                }

            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(OTPActivity.this, "Code resend on your Phone Number", Toast.LENGTH_SHORT).show();

                resendVerificationCode(phone, mResendToken);


            }
        });
    }

    private void resendVerificationCode(String phone_no, PhoneAuthProvider.ForceResendingToken token) {


        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone_no,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);

    }

    private void openDialog() {

                AlertDialog alertDialog = new AlertDialog.Builder(this)

                .setTitle("Reset password")
                .setMessage("Do you want to send OTP to your number?")
                        .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what would happen when positive button is clicked
                        fetchPhoneno();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what should happen when negative button is clicked
                        finish();
                    }
                })
                .show();

    }



    private void fetchPhoneno() {
        Call<LoginPojo> call= RetrofitClient.getInstance().getApi().fetchPhone(id);
        call.enqueue(new Callback<LoginPojo>() {
            @Override
            public void onResponse(Call<LoginPojo> call, Response<LoginPojo> response) {
                LoginPojo user=response.body();
                if (response.isSuccessful()){
                    Log.d(TAG, "onResponsesuccessful: "+user.getPhone());
                    phone=user.getPhone();
                    txt_phone.setText(phone);
                    sendVerificationCode(phone);
                }else {
                    Log.d(TAG, "onResponsenotsuccessful: "+response);
                }
            }

            @Override
            public void onFailure(Call<LoginPojo> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t);

            }
        });

    }

    private void sendVerificationCode(String no) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                no,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks);


    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {


            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                pinView.setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(OTPActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            otpid = s;
            mResendToken = forceResendingToken;
        }
    };

    private void verifyCode(String code) {

        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpid,code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("Email", email);
                            editor.commit();
                            startActivity(new Intent(OTPActivity.this,ChangePassword.class));
                            finish();

                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(OTPActivity.this, "Otp code error", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

}