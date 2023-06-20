package com.uptree.lawdiary.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.uptree.lawdiary.Adapters.AllCasesAdapter;
import com.uptree.lawdiary.Adapters.ClosedCasesAdap;
import com.uptree.lawdiary.Model.AddCasePojo;
import com.uptree.lawdiary.R;
import com.uptree.lawdiary.Retrofit.RetrofitClient;
import com.uptree.lawdiary.sharedPref.SharedPrefManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CloseCases extends AppCompatActivity {

    RecyclerView recyclerView;
    SharedPrefManager sharedPrefManager;
    String userId;
    List<AddCasePojo> caseList;
    //AllCasesAdapter adapter;
    ClosedCasesAdap adapter;
    public static final String TAG = "allcases";
    ImageView txt_empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_close_cases);

        txt_empty=findViewById(R.id.txt_empty);

        recyclerView=findViewById(R.id.closecase_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sharedPrefManager=new SharedPrefManager(getApplicationContext());
        userId =sharedPrefManager.getUser().getId();

        fetchCases();
    }

    private void fetchCases() {

        Call<List<AddCasePojo>> call= RetrofitClient
                .getInstance()
                .getApi()
                .fetchCloseCases(userId);

        call.enqueue(new Callback<List<AddCasePojo>>() {
            @Override
            public void onResponse(Call<List<AddCasePojo>> call, Response<List<AddCasePojo>> response) {

                caseList=response.body();
                if (response.isSuccessful()){


                    try {
                        if (caseList.size()==0)
                        {
                            txt_empty.setVisibility(View.VISIBLE);
                        }else
                        {
                            adapter=new ClosedCasesAdap(CloseCases.this,caseList);
                            recyclerView.setAdapter(adapter);
                            Log.d(TAG, "onResponse: isSuccessful"+caseList.size());
                            txt_empty.setVisibility(View.GONE);

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
}