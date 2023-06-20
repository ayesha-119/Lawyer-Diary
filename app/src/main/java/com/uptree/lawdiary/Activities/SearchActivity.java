package com.uptree.lawdiary.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.uptree.lawdiary.Adapters.AllCasesAdapter;
import com.uptree.lawdiary.Model.AddCasePojo;
import com.uptree.lawdiary.Model.LoginPojo;
import com.uptree.lawdiary.R;
import com.uptree.lawdiary.Retrofit.RetrofitClient;
import com.uptree.lawdiary.sharedPref.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    SearchView searchView;
    SharedPrefManager sharedPrefManager;
    String uid;
    List<AddCasePojo> caseList;
    AllCasesAdapter adapter;

    public static final String TAG = "searchcases";
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        sharedPrefManager=new SharedPrefManager(getApplicationContext());
        uid=sharedPrefManager.getUser().getId();
        caseList = new ArrayList<>();
        searchView=(SearchView) findViewById(R.id.searchview);
        searchView.setQueryHint("Search");
        searchView.setOnSearchClickListener(this);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                search(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                search(query);
                return false;
            }
        });


        recyclerView=findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));




    }
    private void search(String str) {
       Call<List<AddCasePojo>> call=RetrofitClient.getInstance().getApi().searchCase(uid,str);
        Log.d(TAG, "search: "+ uid+ "\t" + str);
       call.enqueue(new Callback<List<AddCasePojo>>() {
           @Override
           public void onResponse(Call<List<AddCasePojo>> call, Response<List<AddCasePojo>> response) {

               caseList = response.body();

               if (response.isSuccessful()){
                   try {
                       adapter=new AllCasesAdapter(SearchActivity.this,caseList);
                       recyclerView.setAdapter(adapter);
                       Log.d(TAG, "onResponse: isSuccessful"+response);
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

    @Override
    public void onClick(View v) {

    }
}