 package com.uptree.lawdiary.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.room.Room;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.NotificationReceiver;
import com.bumptech.glide.Glide;
import com.uptree.lawdiary.Adapters.AllCasesAdapter;
import com.uptree.lawdiary.Model.AddCasePojo;
import com.uptree.lawdiary.R;
import com.uptree.lawdiary.Retrofit.RetrofitClient;
import com.uptree.lawdiary.RoomDB.BookmarkDatabase;
import com.uptree.lawdiary.RoomDB.BookmarkList;
import com.uptree.lawdiary.sharedPref.SharedPrefManager;
import com.google.android.material.navigation.NavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Dashboard extends AppCompatActivity{
    Toolbar toolbar;
    TextView uName;
    SharedPrefManager sharedPrefManager;
    String profileUrl;
    TextView add_cases_no,open_cases_no,today_cases,date , close_cases_no;
    String current_date;
    List<AddCasePojo> caseList;
    String userId;
    public static BookmarkDatabase bookmarkDatabase;
    public static final String TAG = "caseslistsize";
    TextView txt_no_of_allcases,no_of_today_cases,txt_no_of_imp_cases , txt_no_of_closecases, txt_no_of_opencases;
    List<BookmarkList> bookmarkLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);


        sharedPrefManager=new SharedPrefManager(getApplicationContext());
        userId =sharedPrefManager.getUser().getId();

        uName=findViewById(R.id.uName);
        date = findViewById(R.id.current_date);




        date.setText( getCurrentDay() + ", " + getCurrentDate());
        String userName="Hey "+ sharedPrefManager.getUser().getUsername();



        profileUrl=sharedPrefManager.getUser().getImage();
        uName.setText(userName);

        add_cases_no=findViewById(R.id.addcaseno);
        today_cases=findViewById(R.id.todaycaseNo);
        open_cases_no=findViewById(R.id.opencaseNo);
        close_cases_no=findViewById(R.id.closedcasesno);

        //creating refrences of no of cases textviews
        txt_no_of_allcases=findViewById(R.id.txt_no_of_allcases);
        no_of_today_cases=findViewById(R.id.no_of_todaycases);
        txt_no_of_imp_cases=findViewById(R.id.no_of_imp_cases);
        txt_no_of_closecases=findViewById(R.id.txt_no_of_closecases);
        txt_no_of_opencases=findViewById(R.id.no_of_open_cases);

        //fetching all list sizes
        retrofit();
        fetchCloseCases();
        fetchTodayCases();
        fetchOpenCases();

        findViewById(R.id.b).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this,AddNewCase.class));

            }
        });
        findViewById(R.id.b1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ;
                startActivity(new Intent(Dashboard.this,AllCasesActivity.class));

            }
        });
        findViewById(R.id.b2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this,TodayCasesActivity.class));

            }
        });
        findViewById(R.id.impCasesCard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this,ImpCasesActivity.class));
            }
        });

        findViewById(R.id.openCasesCard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this,OpenCasesActivity.class));
            }
        });


        findViewById(R.id.resolveCasesCard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Dashboard.this,CloseCases.class));
            }
        });

        // set Room Database
        bookmarkDatabase = Room.databaseBuilder(Dashboard.this,BookmarkDatabase.class,
                "lawDB").allowMainThreadQueries().build();

        getBookmarkData();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_one:
                startActivity(new Intent(Dashboard.this,SearchActivity.class));
                break;

            case R.id.userProfile:
                startActivity(new Intent(Dashboard.this,MyProfile.class));
                break;
            case R.id.aboutus:
                startActivity(new Intent(Dashboard.this,AboutUsActivity.class));

                break;
            case R.id.rateus:
                rateUsOnGooglePlay();

                break;
            case R.id.resetpassword:
                startActivity(new Intent(Dashboard.this,OTPActivity.class));
                break;
            case R.id.privacypoicy:
                startActivity(new Intent(Dashboard.this,PrivacyPolicy.class));
                break;
            case R.id.logout:
                AlertDialog alertDialog = new AlertDialog.Builder(Dashboard.this)
                        .setTitle("Logout")
                        .setMessage("Do you really want to logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                sharedPrefManager.logout();
                                Intent i=new Intent(Dashboard.this,LoginActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                finish();

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //set what should happen when negative button is clicked

                            }
                        })
                        .show();


                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void retrofit() {
        current_date=todayDate();
        Call<List<AddCasePojo>> call= RetrofitClient.getInstance().getApi().fetchAllCases(userId);
        call.enqueue(new Callback<List<AddCasePojo>>() {
            @Override
            public void onResponse(Call<List<AddCasePojo>> call, Response<List<AddCasePojo>> response) {
                caseList=response.body();
                if (response.isSuccessful()){
                    try {
                        add_cases_no.setText(caseList.size()+ "");
                        txt_no_of_allcases.setText(caseList.size()+" available");


                    }
                    catch (Exception e){
                        e.printStackTrace();

                    }

                }
                else {
                    Log.d(TAG, "onResponse: isNotSuccessful"+response);
                }

            }

            @Override
            public void onFailure(Call<List<AddCasePojo>> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t);

            }
        });

    }

    private void fetchTodayCases()
    {

        current_date=todayDate();
        Log.d("currentdate", "fetchTodayCases: "+current_date);
        Call<List<AddCasePojo>> call= RetrofitClient.getInstance().getApi().fetchTodayCases(userId,current_date);
        call.enqueue(new Callback<List<AddCasePojo>>() {
            @Override
            public void onResponse(Call<List<AddCasePojo>> call, Response<List<AddCasePojo>> response) {
                Log.d("today", "onResponse: ");
                caseList = response.body();
                if (response.isSuccessful())
                {
                    Log.d("today", "onResponse: successful");

                    today_cases.setText(caseList.size()+"");
                    no_of_today_cases.setText(caseList.size()+" available");

                }
                else {
                    Log.d(TAG, "onResponse: isNotSuccessful"+response);
                }

            }

            @Override
            public void onFailure(Call<List<AddCasePojo>> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t);

            }
        });

    }

    private void getBookmarkData() {
        bookmarkLists = Dashboard.bookmarkDatabase.bookmarkDao().getBookmarkData();
        if (bookmarkLists != null){
            txt_no_of_imp_cases.setText(String.valueOf(bookmarkLists.size())+" available");

        }
        else {
            txt_no_of_imp_cases.setText(String.valueOf(0)+" available");
        }


    }


    private void fetchCloseCases()
    {
        Call<List<AddCasePojo>> call= RetrofitClient.getInstance().getApi().fetchCloseCases(userId);
        call.enqueue(new Callback<List<AddCasePojo>>() {
            @Override
            public void onResponse(Call<List<AddCasePojo>> call, Response<List<AddCasePojo>> response) {
                caseList=response.body();
                if (response.isSuccessful()){
                    try {
                        close_cases_no.setText(caseList.size()+ "");
                        txt_no_of_closecases.setText(caseList.size()+" available");


                    }
                    catch (Exception e){
                        e.printStackTrace();

                    }

                }
                else {
                    Log.d(TAG, "onResponse: isNotSuccessful"+response);
                }

            }

            @Override
            public void onFailure(Call<List<AddCasePojo>> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t);

            }
        });
    }


    private void fetchOpenCases() {
        Call<List<AddCasePojo>> call= RetrofitClient.getInstance().getApi().openCases(userId);

        call.enqueue(new Callback<List<AddCasePojo>>() {
            @Override
            public void onResponse(Call<List<AddCasePojo>> call, Response<List<AddCasePojo>> response) {

                caseList=response.body();
                if (response.isSuccessful()){
                    try {

                        open_cases_no.setText(caseList.size()+"");
                        txt_no_of_opencases.setText(caseList.size()+" available");



                    }
                    catch (Exception e){
                        e.printStackTrace();

                    }

                }
                else {
                    Log.d(TAG, "onResponse: isNotSuccessful"+response);
                }

            }

            @Override
            public void onFailure(Call<List<AddCasePojo>> call, Throwable t) {

                Log.d(TAG, "onFailure: "+t);

            }
        });

    }

    public String getCurrentDate(){
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd MMM", Locale.getDefault());
        String formattedDate = df.format(c);

        return formattedDate;

    }
   public  String todayDate(){
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);

        return formattedDate;


    }
    public String getCurrentDay(){
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String day=new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime());
        return day;
    }


    public void rateUsOnGooglePlay() {
        try{
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+getPackageName())));
        }
        catch (ActivityNotFoundException e){
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName())));
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        retrofit();
        getBookmarkData();
        fetchCloseCases();
        fetchTodayCases();
        fetchOpenCases();
    }

    @Override
    protected void onStart() {
        super.onStart();
        retrofit();
        getBookmarkData();
        fetchCloseCases();
        fetchTodayCases();
        fetchOpenCases();

    }

    @Override
    protected void onResume() {
        super.onResume();
        retrofit();
        getBookmarkData();
        fetchCloseCases();
        fetchTodayCases();
        fetchOpenCases();
    }

}