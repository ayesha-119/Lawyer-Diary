package com.uptree.lawdiary.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.uptree.lawdiary.Adapters.AllCasesAdapter;
import com.uptree.lawdiary.Model.AddCasePojo;
import com.uptree.lawdiary.R;
import com.uptree.lawdiary.Retrofit.RetrofitClient;
import com.uptree.lawdiary.sharedPref.SharedPrefManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OpenCasesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SharedPrefManager sharedPrefManager;
    String userId;
    List<AddCasePojo> caseList;
    AllCasesAdapter adapter;
    public static final String TAG = "opencases";

    ImageView txt_empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_cases);
        txt_empty=findViewById(R.id.txt_empty);

        recyclerView=findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sharedPrefManager=new SharedPrefManager(getApplicationContext());
        userId =sharedPrefManager.getUser().getId();

        //retrofit
        fetchCases();
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
        fetchCases();
    }

    private void fetchCases() {
        Call<List<AddCasePojo>> call= RetrofitClient.getInstance().getApi().openCases(userId);
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
                            adapter=new AllCasesAdapter(OpenCasesActivity.this,caseList);
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