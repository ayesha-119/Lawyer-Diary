package com.uptree.lawdiary.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.uptree.lawdiary.Adapters.ImpCasesAdapter;
import com.uptree.lawdiary.R;
import com.uptree.lawdiary.RoomDB.BookmarkList;
import com.uptree.lawdiary.sharedPref.SharedPrefManager;

import java.util.List;

public class ImpCasesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SharedPrefManager sharedPrefManager;
    String userId;
    List<BookmarkList> bookmarkLists;
    ImpCasesAdapter adapter;
    public static final String TAG = "allcases";
    ImageView txt_empty;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imp_cases);


        txt_empty=findViewById(R.id.txt_empty);

        recyclerView=findViewById(R.id.recycler_imp_cases);

        getBookmarkData();
    }

    private void getBookmarkData() {
        bookmarkLists = Dashboard.bookmarkDatabase.bookmarkDao().getBookmarkData();
        if (bookmarkLists != null){
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new ImpCasesAdapter(bookmarkLists,ImpCasesActivity.this);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            Log.e("impCases:",""+bookmarkLists.size());
            txt_empty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        else {
            txt_empty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);

        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getBookmarkData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getBookmarkData();
    }

}