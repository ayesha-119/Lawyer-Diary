package com.uptree.lawdiary.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;
import com.uptree.lawdiary.Model.LoginPojo;
import com.uptree.lawdiary.R;
import com.uptree.lawdiary.Retrofit.RetrofitClient;
import com.uptree.lawdiary.sharedPref.SharedPrefManager;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {
    EditText name, emailEt,phone,pass,chamberno,address;
    CountryCodePicker cpp;
    Button signup;
    ImageView img;
    Uri imageUri;
    public static final int REQUEST_IMAGE_GALLERY = 1;
    private static final int REQUEST_IMAGE_CAMERA = 11;
    public static final String TAG = "signup";
    String e,p,ph,loc,chNo,n,user_phone;
    String encodedImage,imageString = "";
    String encImage;
    SharedPrefManager sharedPrefManager;

    private static final int MY_CAMERA_REQUEST_CODE = 100;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);



        //creating sharedprefmanager object
        sharedPrefManager=new SharedPrefManager(this);

        name=findViewById(R.id.Name);
        emailEt =findViewById(R.id.Email);
        pass=findViewById(R.id.userPassword);
        phone=findViewById(R.id.PhoneNo);
        signup=findViewById(R.id.btn_register);
        chamberno=findViewById(R.id.chamberNo);
        address=findViewById(R.id.chamberAddress);
        img=findViewById(R.id.profile);
        cpp=findViewById(R.id.cpp);
        cpp.registerCarrierNumberEditText(phone);

        img.setOnClickListener(this);
        signup.setOnClickListener(this);
        findViewById(R.id.txt_login).setOnClickListener(this);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.profile:
                selectImage();
                break;
            case R.id.btn_register:
                if (isValid())
                {

                    createUser();
                }
                break;

            case R.id.txt_login:
                startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
                break;
        }
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
                    if (ContextCompat.checkSelfPermission(RegistrationActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
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

            if (imageUri != null)
            {
                try {
                    Bitmap photo = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
                    img.setImageBitmap(photo);
                    encodedImage = encodeImage(photo);
                    imageString =  getImageString(photo);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }
        if (requestCode == REQUEST_IMAGE_CAMERA && resultCode == RESULT_OK){


                try{
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    img.setImageBitmap(imageBitmap);
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
            } else {
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
            temp=Base64.encodeToString(b, Base64.DEFAULT);
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

    boolean isValid()
    {
        String e,p,ph,loc,chNo,n;
        e = emailEt.getText().toString();
        n = name.getText().toString();
        p = pass.getText().toString();
        ph = phone.getText().toString();
        chNo = chamberno.getText().toString();
        loc = address.getText().toString();


        if(imageString.isEmpty() || imageString ==null)
        {
            Toast.makeText(this, "Select valid image", Toast.LENGTH_LONG).show();
            return false;
        }

        if (n.isEmpty() || n.length()<3 ){
            name.setError("Enter valid name");
            return false;
        }

        if (e.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(e).matches())
        {
            emailEt.setError("Please enter valid email");
            return false;
        }

        if (p.isEmpty() || p.length()<6)
            {
                pass.setError("Password must be atleast 6 digits long");
                return false;
            }

        if (ph.length() != 10)
            {
                phone.setError("Please enter valid phone no");
                return false;
            }

        if(ph.startsWith("0"))
            {
                phone.setError("Phone no must start with 3");
                return false;
            }

        if (loc.isEmpty())
            {
                address.setError("Chamber Address is required");
                return false;
            }

        if (chNo.isEmpty())
            {
                chamberno.setError("Please Enter Chamber no");
                return false;
            }

        return true;
    }

    private void createUser()
    {
        e= emailEt.getText().toString();
        n=name.getText().toString();
        p=pass.getText().toString();
        ph=phone.getText().toString();
        chNo=chamberno.getText().toString();
        loc=address.getText().toString();
        user_phone = cpp.getFullNumberWithPlus().replace(" ","");
        Log.d("encodedimg", "encodedimg: "+imageString);
        Call<LoginPojo> call= RetrofitClient
                .getInstance()
                .getApi()
                .register(n,e,p,user_phone,chNo,loc,imageString);
        call.enqueue(new Callback<LoginPojo>() {
            @Override
            public void onResponse(Call<LoginPojo> call, Response<LoginPojo> response) {
                LoginPojo registerResponse = response.body();
                if (response.isSuccessful()){
                    Log.d(TAG, "onResponse: isSuccessful "+registerResponse.getResult());
                    String res=registerResponse.getResult();
                    String imgurl=registerResponse.getImage();
                    Log.d("imgurl", "onResponse: "+imgurl);

                    if (res.equals("Add successfully"))
                    {
                        progressDialog = new ProgressDialog(RegistrationActivity.this);
                        progressDialog.setMessage("please wait...");
                        progressDialog.show();
                        String id=registerResponse.getId();
                        Log.d("uid", "onResponse: "+id);
                        Log.d(TAG, "onResponse:result "+registerResponse.getResult());
                        //sharedPrefManager.saveUser(registerResponse);
                        Intent intent=new Intent(RegistrationActivity.this, LoginActivity.class);
                        intent.putExtra("userEmail" , e);
                        intent.putExtra("userPass" , p);
                        startActivity(intent);
                        finish();
                        Log.d(TAG, "onResponse: isSuccessful "+registerResponse.getResult());
                    }
                }
                else {
                    Log.d(TAG, "onResponse: isNotSuccessful "+registerResponse.getResult());
                }
            }

            @Override
            public void onFailure(Call<LoginPojo> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t);
            }
        });

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