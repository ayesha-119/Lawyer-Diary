package com.uptree.lawdiary.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.TaskExecutors;
import com.uptree.lawdiary.Model.LoginPojo;
import com.uptree.lawdiary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.uptree.lawdiary.Retrofit.RetrofitClient;
import com.uptree.lawdiary.sharedPref.SharedPrefManager;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageOTP extends AppCompatActivity {
    Button btn;
    String Phone_no,email;
    String otpid;
    FirebaseAuth mAuth;
    public static String TAG="otp";
    TextView txt_phone;
    SharedPreferences sharedPreferences;
    PinView pinView;
    Toolbar toolbar;

    private PhoneAuthProvider.ForceResendingToken mResendToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_otp);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

         pinView = findViewById(R.id.firstPinView);

        sharedPreferences = getSharedPreferences("pref", Context.MODE_PRIVATE);
        email=sharedPreferences.getString("Email",null);
        Phone_no=getIntent().getStringExtra("mobile").toString();

        mAuth=FirebaseAuth.getInstance();
        btn=findViewById(R.id.b1);
        txt_phone=findViewById(R.id.phone_no);
        txt_phone.setText(Phone_no);

        TextView textView = findViewById(R.id.resend_verification);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ManageOTP.this, "Code resend on your Phone Number", Toast.LENGTH_SHORT).show();

                resendVerificationCode(Phone_no, mResendToken);
            }
        });


        sendVerificationCode(Phone_no);

        initiateotp(Phone_no);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = pinView.getText().toString().trim();
                pinView.clearFocus();
                if (code.equals("") || code.isEmpty()){
                    Toast.makeText(ManageOTP.this, "Blank field cannot be processed ",Toast.LENGTH_SHORT).show();

                }
                else if (pinView.getText().toString().length()!=6)
                    Toast.makeText(ManageOTP.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(ManageOTP.this, pinView.getText().toString(), Toast.LENGTH_SHORT).show();
                    PhoneAuthCredential credential=PhoneAuthProvider.getCredential(otpid,pinView.getText().toString());
                    signInWithPhoneAuthCredential(credential);
                }

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
            Toast.makeText(ManageOTP.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            otpid = s;
            mResendToken = forceResendingToken;
        }
    };

    private void initiateotp(String s) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                s,
                60,
                TimeUnit.SECONDS,
                this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onCodeSent( String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        otpid=s;
                    }

                    @Override
                    public void onVerificationCompleted( PhoneAuthCredential phoneAuthCredential) {
                        signInWithPhoneAuthCredential(phoneAuthCredential);

                        String code = phoneAuthCredential.getSmsCode();
                        if (code != null) {
                            pinView.setText(code);
                            verifyCode(code);
                        }

                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        Log.d(TAG, "onVerificationFailed: "+e.getMessage());
                        Toast.makeText(ManageOTP.this, "verification failed"+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

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
                            Log.d(TAG, "signInWithCredential:success");
                            Toast.makeText(ManageOTP.this, "verification success", Toast.LENGTH_SHORT).show();


                            startActivity(new Intent(ManageOTP.this,ChangePassword.class));
                            finish();

                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(ManageOTP.this, "Some issue has occoured while verifying otp, try again after some time", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
}