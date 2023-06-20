package com.uptree.lawdiary.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.uptree.lawdiary.Adapters.HearingAdap;
import com.uptree.lawdiary.BottomSheetDialog;
import com.uptree.lawdiary.Model.AddCasePojo;
import com.uptree.lawdiary.Model.HearingModel;
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

public class BookmarkDetailActivity extends AppCompatActivity implements View.OnClickListener {
    TextView title,judgeName,culpName,detail,date,category,txtComplaint,txtService;
    Button save;
    String caseTitle,judge_name,culprit_name,caseCategory,caseDetail,caseStartingDate,next_date,case_id,complaint,servvice,uid;
    public static final String TAG = "bookmarkdetail";
    String userId;
    SharedPrefManager sharedPrefManager;

    //added later...do check if you have created refrences accurately
    EditText nextdate;
    Button btn_close , btn_view;
    private int mYear, mMonth, mDay;
    List<HearingModel> caseList;
    HearingAdap adap;
    RecyclerView recyclerView;
    TextView start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark_detail);
        title = findViewById(R.id.title);
        nextdate = findViewById(R.id.next_date);
        save = findViewById(R.id.btn_save);
        recyclerView=findViewById(R.id.hearing_recycler1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        judgeName=findViewById(R.id.judgename);
        culpName=findViewById(R.id.culpname);
        category=findViewById(R.id.txt_Category);
        detail=findViewById(R.id.txt_detail);
        date=findViewById(R.id.txt_date);
        txtComplaint=findViewById(R.id.txt_complaintName);
        txtService=findViewById(R.id.txt_Service);
        start=findViewById(R.id.start);
        sharedPrefManager=new SharedPrefManager(getApplicationContext());
        getData();
        check();
        userId= sharedPrefManager.getUser().getId();


        //code added later!!!
        // beep beep error may happen cuz you are sleepy now
        // check later from here in case of problem
        // you might miss something be careful

        btn_close = findViewById(R.id.btn_close_case);
        btn_view = findViewById(R.id.btn_view_judgement);

        findViewById(R.id.btn_close_case).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentDate=todayDate();
                BottomSheetDialog bottomSheet = new BottomSheetDialog();
                Bundle bundle = new Bundle();
                bundle.putString("value","close");
                bundle.putString("caseid",case_id);
                bundle.putString("userid",userId);
                bundle.putString("date",currentDate);
                bottomSheet.setArguments(bundle);
                bottomSheet.show(getSupportFragmentManager(),
                        "ModalBottomSheet");

            }
        });


        findViewById(R.id.btn_view_judgement).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheet = new BottomSheetDialog();
                Bundle bundle = new Bundle();
                bundle.putString("value","view");
                bundle.putString("caseid",case_id);
                bundle.putString("userid",userId);
                bottomSheet.setArguments(bundle);
                bottomSheet.show(getSupportFragmentManager(),
                        "ModalBottomSheet");

            }
        });

        nextdate.setOnClickListener(this);
        // getData();
        save.setOnClickListener(this);

    }
    private void getData() {
        Intent intent=getIntent();
        case_id=intent.getStringExtra("caseId");
        caseTitle=intent.getStringExtra("title");
        caseStartingDate=intent.getStringExtra("startdate");
        //userId=intent.getStringExtra("uid");
        caseCategory=intent.getStringExtra("case_category");
        caseDetail=intent.getStringExtra("detail");
        judge_name=intent.getStringExtra("judge");
        culprit_name=intent.getStringExtra("culp");
        complaint=intent.getStringExtra("complaint");
        servvice=intent.getStringExtra("service");
        setData();

    }

    private void setData() {
        title.setText(caseTitle);
        judgeName.setText(judge_name);
        culpName.setText(culprit_name);
        category.setText(caseCategory);
        detail.setText(caseDetail);
        date.setText(caseStartingDate);
        txtComplaint.setText(complaint);
        txtService.setText(servvice);
        start.setText(caseStartingDate);


    }



    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.next_date:
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                next_date=dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                nextdate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                break;
            case R.id.btn_save:
                String date=nextdate.getText().toString();
                if (date.isEmpty()){
                    nextdate.setError("date is compulsory");
                    Toast.makeText(this, "please Select a Date to update next hearing", Toast.LENGTH_SHORT).show();
                }else {
                    saveHearingDate(date);
                }

                break;
        }
    }

    private void saveHearingDate(String nextDate) {
        Toast.makeText(this, "save"+userId+"\t"+case_id, Toast.LENGTH_SHORT).show();
        Log.d("save11", "saveHearingDate: "+userId+"\n"+case_id);



        Call<AddCasePojo> call= RetrofitClient.getInstance().getApi().newDate(userId,case_id,nextDate);
        call.enqueue(new Callback<AddCasePojo>() {
            @Override
            public void onResponse(Call<AddCasePojo> call, Response<AddCasePojo> response) {
                if (response.isSuccessful()){
                    Log.d(TAG,"onResponse: isSuccessful"+response);
                    nextdate.clearFocus();

                    fetchNextDates(userId,case_id);
                }
                else {
                    Log.d(TAG, "onResponse: isNotSuccessful "+response);
                }
            }
            @Override
            public void onFailure(Call<AddCasePojo> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t);

            }
        });
    }

    private void fetchNextDates(String str_userId, String str_case_id) {
        Log.d("fetch11", "fetchNextDates: "+ str_userId + str_case_id);

        Call<List<HearingModel>> call=RetrofitClient.getInstance().getApi().fetchDates(str_userId, str_case_id);

        Log.d(TAG, "fetchNextDates: "+"userid "+ this.userId + "\t" +"case_id "+ this.case_id);
        call.enqueue(new Callback<List<HearingModel>>() {
            @Override
            public void onResponse(Call<List<HearingModel>> call, Response<List<HearingModel>> response) {
                caseList=response.body();
                if (response.isSuccessful())
                {


                    adap = new HearingAdap(BookmarkDetailActivity.this,caseList);
                    recyclerView.setAdapter(adap);

                        Log.d("book", "onResponse: isSuccessful : "+caseList.size());
                }
                else {
                    Log.d("book", "onResponse: isNotSuccessful : "+response);
                }
            }

            @Override
            public void onFailure(Call<List<HearingModel>> call, Throwable t) {
                Log.d("book", "onFailure: "+t);
            }
        });
    }

    void check()
    {

        Call<AddCasePojo> call = RetrofitClient
                .getInstance()
                .getApi()
                .fetchJudgement(userId, case_id);

        Log.d("caseid", "check1 "+"case id: "+case_id);

        call.enqueue(new Callback<AddCasePojo>() {
            @Override
            public void onResponse(Call<AddCasePojo> call, Response<AddCasePojo> response) {
                Log.d("caseid", "check2 "+"case id: "+case_id);

                AddCasePojo user=response.body();
                String result = user.getResult();

                if (response.isSuccessful()){

                    if (result.equals("Fetch Successfully") )
                    {
                        Log.d("caseid", "check3 "+"case id: "+case_id);
                        //Todo: hide btn
                        btn_close.setVisibility(View.GONE);
                        Log.d("lawDiary", "check3 ");

                    }
                    else if (result.equals("No Result found")){
                        Log.d("caseid", "check4 "+"case id: "+case_id);

                        btn_view.setVisibility(View.GONE);
                    }



                }
                else {
                    Log.d("lawDiary", "onResponse: isNotSuccessful : "+response);
                }

            }

            @Override
            public void onFailure(Call<AddCasePojo> call, Throwable t) {
                Log.d("lawDiary", "onFailure: "+t);

            }
        });

    }

    public  String todayDate(){
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);

        return formattedDate;


    }

    @Override
    protected void onStart()
    {
        super.onStart();
//        fetchNextDates(userId, case_id);
//
//        check();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        fetchNextDates(userId, case_id);
        check();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchNextDates(userId, case_id);

        check();
    }
}