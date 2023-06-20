package com.uptree.lawdiary.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.uptree.lawdiary.Model.LoginPojo;
import com.uptree.lawdiary.R;
import com.uptree.lawdiary.Retrofit.RetrofitClient;
import com.uptree.lawdiary.sharedPref.SharedPrefManager;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfile extends AppCompatActivity {
    CircleImageView uImage;
    EditText name,phone,chamberno,address;
    String e,p,ph,loc,chNo,n;
    Button btn_update;
    SharedPrefManager sharedPrefManager;
    String uid,u_name,u_phone, u_address,u_chamberno,u_imgurl, imageString;
    public static final String TAG = "updateprofile";
    public static final int REQUEST_IMAGE_GALLERY = 1;
    private static final int REQUEST_IMAGE_CAMERA = 11;
    String encodedImage;
    String encImage;
    Uri imageUri;

    private static final int MY_CAMERA_REQUEST_CODE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        uImage=findViewById(R.id.uImage);
        name=findViewById(R.id.Name);

        phone=findViewById(R.id.PhoneNo);
        btn_update=findViewById(R.id.btn_update);
        chamberno=findViewById(R.id.chamberNo);
        address=findViewById(R.id.chamberAddress);
        sharedPrefManager=new SharedPrefManager(this);

        //getting data from sharedprefrences manager
        uid=sharedPrefManager.getUser().getId();
        u_name=sharedPrefManager.getUser().getUsername();
        u_phone=sharedPrefManager.getUser().getPhone();
        u_address=sharedPrefManager.getUser().getChamber_address();
        u_chamberno=sharedPrefManager.getUser().getChamber_no();
        u_imgurl=sharedPrefManager.getUser().getImage();

        //setting values in editText
        name.setText(u_name);
        phone.setText(u_phone);
        address.setText(u_address);
        chamberno.setText(u_chamberno);
        String imageStr = "https://lawdiary.uptreedevelopers.com/"+u_imgurl;
        Glide.with(this).load(imageStr).into(uImage);
        Log.d(TAG, "onCreate: "+ u_imgurl);


        uImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();


            }
        });


        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (isValid()){
                    updateUser();
                }*/
                updateUser();
            }
        });
    }


    boolean isValid() {
        String ph,loc,chNo,n;
        n=name.getText().toString();
        ph=phone.getText().toString();
        chNo=chamberno.getText().toString();
        loc=address.getText().toString();
        if (TextUtils.isEmpty(n)){
            name.setError("Name is required");
            return false;
        }

        if (ph.isEmpty()|| ph.length()!=13){
            phone.setError("Please Enter Valid phone no");
            return false;
        }

        if (chNo.isEmpty()){
            chamberno.setError("Please Enter Chamber no");
            return false;

        }
        if (loc.isEmpty()){
            address.setError("Chamber Address is required");
            return false;

        }
        return true;
    }
    private void updateUser() {
        e=sharedPrefManager.getUser().getEmail();
        n=name.getText().toString();
        p=sharedPrefManager.getUser().getPassword();
        ph=phone.getText().toString();
        chNo=chamberno.getText().toString();
        loc=address.getText().toString();
        Log.d("data", "updateUser: "+n
        +e+p+ph+chNo+loc);

        Call<LoginPojo> call=RetrofitClient.getInstance().getApi().editProfile(uid,n,e,ph,chNo,loc,imageString);
        call.enqueue(new Callback<LoginPojo>() {
            @Override
            public void onResponse(Call<LoginPojo> call, Response<LoginPojo> response) {
                LoginPojo user=response.body();
                if (response.isSuccessful()){

                    //for storing user data into sharedprefrence
                    sharedPrefManager.saveUser(user);
                    Intent intent=new Intent(UpdateProfile.this, Dashboard.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    Log.d(TAG, "onResponse: isSuccessful"+response);

                    ProgressDialog pDialog = new ProgressDialog(UpdateProfile.this); //Your Activity.this
                    pDialog.setMessage("Updating...!");
                    pDialog.setCancelable(false);
                    pDialog.show();
                    Toast.makeText(UpdateProfile.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();

                }
                else {
                    Toast.makeText(UpdateProfile.this, "resonse unsuccesful", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginPojo> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t);
                Toast.makeText(UpdateProfile.this, "Updation Failed", Toast.LENGTH_SHORT).show();

            }
        });

    }
    private void selectImage() {

        final CharSequence[] options = {"Choose from Gallery","Choose from Camera","Cancel"};
        AlertDialog.Builder builder =  new AlertDialog.Builder(this);
        builder.setTitle("Add photo");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Choose from Gallery"))
                {
                    Intent gallery = new Intent();
                    gallery.setType("image/*");
                    gallery.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(gallery, "Select Picture"), REQUEST_IMAGE_GALLERY);

                }
                if (options[item].equals("Choose from Camera")) {
                    if (ContextCompat.checkSelfPermission(UpdateProfile.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
                    }
                    else{
                        dispatchTakePictureIntent();

                    }


                }
                else if (options[item].equals("Cancel"))
                {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAMERA);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_IMAGE_GALLERY) {
            imageUri = data.getData();
            try {
                Bitmap photo = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
                uImage.setImageBitmap(photo);
                encodedImage = encodeImage(photo);
                imageString =  getImageString(photo);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == REQUEST_IMAGE_CAMERA && resultCode == RESULT_OK){


            try{
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                uImage.setImageBitmap(imageBitmap);
                encodedImage = encodeImage(imageBitmap);
                imageString =  getImageString(imageBitmap);
            }catch (Exception e){
                e.printStackTrace();

            }



        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String temp=null;
        try{
            System.gc();
            temp= Base64.encodeToString(b, Base64.DEFAULT);
        }catch(Exception e){
            e.printStackTrace();
        }
        catch(OutOfMemoryError e){
            baos=new  ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG,50, baos);
            b=baos.toByteArray();
            temp=Base64.encodeToString(b, Base64.DEFAULT);
            Log.e("EWN", "Out of memory error catched");
        }
        encImage = Base64.encodeToString(b, Base64.DEFAULT);
        //System.gc();
        //return encImage;
        return temp;
    }
    private String getImageString(Bitmap bitmap) {
        //encode image to base64 string
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String s = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return s;
    }



}