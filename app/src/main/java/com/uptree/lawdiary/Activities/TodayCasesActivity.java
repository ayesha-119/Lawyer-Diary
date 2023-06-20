package com.uptree.lawdiary.Activities;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.uptree.lawdiary.Adapters.AllCasesAdapter;
import com.uptree.lawdiary.Model.AddCasePojo;
import com.uptree.lawdiary.R;
import com.uptree.lawdiary.Retrofit.RetrofitClient;
import com.uptree.lawdiary.sharedPref.SharedPrefManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TodayCasesActivity extends AppCompatActivity {
    //Toolbar toolbar;
    RecyclerView recyclerView;
    SharedPrefManager sharedPrefManager;
    String userId;
    List<AddCasePojo> caseList;
    AllCasesAdapter adapter;
    public static final String TAG = "todaycases";
    String current_date;
    TextView txtsize;
    String listSize;
    ImageView txt_empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_cases);


        txt_empty=findViewById(R.id.txt_empty);



        recyclerView=findViewById(R.id.todaycase_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sharedPrefManager=new SharedPrefManager(getApplicationContext());
        userId =sharedPrefManager.getUser().getId();

        current_date=getCurrentDate();

        fetchTodayCases();
    }



    private void fetchTodayCases() {
        Call<List<AddCasePojo>> call=RetrofitClient.getInstance().getApi().fetchTodayCases(userId,current_date);
        call.enqueue(new Callback<List<AddCasePojo>>() {
            @Override
            public void onResponse(Call<List<AddCasePojo>> call, Response<List<AddCasePojo>> response) {
                caseList=response.body();
                if (response.isSuccessful()){
                    Log.d(TAG, "onResponse: successful"+response);
                    try {
                        //txtsize.setText(caseList.size()+"");
                        if (caseList.size()==0){
                            txt_empty.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }
                        else {
                           // txtsize.setText(caseList.size()+"");
                            listSize=String.valueOf(caseList.size());
                            adapter=new AllCasesAdapter(TodayCasesActivity.this,caseList);
                            recyclerView.setAdapter(adapter);
                            Log.d(TAG, "onResponse: isSuccessful"+response);
                            txt_empty.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);

                        }

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

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        return formattedDate;

    }

}