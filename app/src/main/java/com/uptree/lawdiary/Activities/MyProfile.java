package com.uptree.lawdiary.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.uptree.lawdiary.R;
import com.uptree.lawdiary.sharedPref.SharedPrefManager;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfile extends AppCompatActivity {
    CircleImageView uImage;
    TextView unamme,uemail,uphone,uaddress,uchamberno;
    SharedPrefManager sharedPrefManager;
    String u_name,u_email,u_phone,u_address,u_chamberno,u_imgurl;
    Button btn_edit;
    Toolbar toolbar;
    String imageString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        //Profile Refrences

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        uImage=findViewById(R.id.uImage);
        unamme=findViewById(R.id.uname);
        uemail=findViewById(R.id.uEmail);
        uphone=findViewById(R.id.uphone);
        uaddress=findViewById(R.id.uAddress);
        uchamberno=findViewById(R.id.uChamberno);
        btn_edit=findViewById(R.id.edit_profile);

        sharedPrefManager=new SharedPrefManager(getApplicationContext());
        getValues();
        setValues();
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MyProfile.this,UpdateProfile.class));


            }
        });
        uImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadPhoto();
            }
        });

    }

    private void loadPhoto() {
        Log.d("FAILS", "1");
        AlertDialog.Builder imageDialog = new AlertDialog.Builder(
                MyProfile.this);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        Log.d("FAILS", "2");
        View layout = inflater.inflate(R.layout.custom_fullimage_dialog,
                (ViewGroup)findViewById(R.id.layout_root));
        ImageView image = (ImageView) layout.findViewById(R.id.fullimage);
        Log.d("FAILS", "3");
        Glide.with(this).load(imageString).into(image);
        Log.d("FAILS", imageString);

        imageDialog.setView(layout);
        imageDialog.show();
    }

    private void getValues() {
        u_name=sharedPrefManager.getUser().getUsername();
        u_phone=sharedPrefManager.getUser().getPhone();
        u_email=sharedPrefManager.getUser().getEmail();
        u_address=sharedPrefManager.getUser().getChamber_address();
        u_chamberno=sharedPrefManager.getUser().getChamber_no();
        u_imgurl=sharedPrefManager.getUser().getImage();


    }

    private void setValues() {
        Log.d("profile", "getValues: "+u_imgurl);
        unamme.setText(""+u_name);
        uemail.setText(""+u_email);
        uphone.setText(""+u_phone);
        uaddress.setText(""+u_address);
        uchamberno.setText(""+u_chamberno);

        imageString = "https://lawdiary.uptreedevelopers.com/"+u_imgurl;
        Glide.with(this).load(imageString).into(uImage);


    }
}