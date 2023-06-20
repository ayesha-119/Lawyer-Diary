package com.uptree.lawdiary.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.uptree.lawdiary.Model.AddCasePojo;
import com.uptree.lawdiary.R;
import com.uptree.lawdiary.Retrofit.RetrofitClient;
import com.uptree.lawdiary.sharedPref.SharedPrefManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNewCase extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    EditText title,judgeName,culpName,detail,date,complaint;
    Spinner category,services_spinner;
    Button AddCase;
    String caseTitle,judge_name,culprit_name,caseCategory,caseDetail,caseStartingDate,currentDate,complaint_name,service;
    public static final String TAG = "newcase";
    private int mYear, mMonth, mDay;
    String userId;
    String caseServices;

    SharedPrefManager sharedPrefManager;
    ConstraintLayout spinnerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_case);
        title=findViewById(R.id.title);
        judgeName=findViewById(R.id.judgeName);
        culpName=findViewById(R.id.culp_name);
        category=findViewById(R.id.category);
        detail=findViewById(R.id.detail);
        date=findViewById(R.id.date);
        complaint=findViewById(R.id.complaint_name);
        AddCase=findViewById(R.id.addCase);
        spinnerLayout=findViewById(R.id.spinnerLayout);
        sharedPrefManager=new SharedPrefManager(getApplicationContext());
        userId= sharedPrefManager.getUser().getId();




        //spinner refrence
        services_spinner=findViewById(R.id.services_spinner);
        final List<String> str = new ArrayList<String>();
        str.add("Select");
        str.add("for complaint");
        str.add("for culprit");
        ArrayAdapter<String> adp1 = new ArrayAdapter<String>(AddNewCase.this,
                android.R.layout.simple_list_item_1, str);
        services_spinner.setAdapter(adp1);
        services_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    caseServices=parent.getItemAtPosition(position).toString();
                    
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        String d=getCurrentDate();
        //initialize arraylist
        ArrayList<String> list=new ArrayList<>();
        list.add("Select Case Category");
        list.add("Criminal");
        list.add("Civil");
        list.add("Family");
        list.add("Services");
        list.add("Anti Corruption");
        list.add("Anti Terrorism");
        list.add("Banking");
        list.add("Consumer");
        list.add("Labour");

        //setting adapter for spinner
        category.setAdapter(new ArrayAdapter<>(AddNewCase.this, android.R.layout.simple_spinner_dropdown_item,list));
        category.setOnItemSelectedListener(this);

        date.setOnClickListener(this);

        AddCase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewCase();
            }
        });
    }

    public void addNewCase() {


        if (caseServices.equals("for complaint")){
            service=culpName.getText().toString();

        }
       if (caseServices.equals("for culprit")) {
            service=complaint.getText().toString();

        }
       if (validate())
       {
           Call<AddCasePojo> call= RetrofitClient
                   .getInstance()
                   .getApi()
                   .addCase(caseTitle,judge_name,culprit_name,caseCategory,caseDetail,caseStartingDate,currentDate,userId,complaint_name,service);
           //Log.d("casedata", "onResponse: "+caseTitle+judge_name+culprit_name+caseCategory+caseDetail+caseStartingDate+currentDate+userId+complaint_name+service);

           call.enqueue(new Callback<AddCasePojo>() {
               @Override
               public void onResponse(Call<AddCasePojo> call, Response<AddCasePojo> response) {

                   AddCasePojo user=response.body();
                   if (response.isSuccessful()){
                       ProgressDialog pDialog = new ProgressDialog(AddNewCase.this); //Your Activity.this
                       pDialog.setMessage("Uploading...!");
                       pDialog.setCancelable(false);
                       pDialog.show();
                       pDialog.cancel();

                       Toast.makeText(AddNewCase.this, user.getResult(), Toast.LENGTH_SHORT).show();


                       //Todo:

                       title.getText().clear();
                       judgeName.getText().clear();
                       culpName.getText().clear();
                       detail.getText().clear();
                       date.getText().clear();
                       complaint.getText().clear();

                       Log.d(TAG, "onResponse: isSuccessful"+response);

                   }
                   else {
                       Log.d(TAG, "onResponse: isNotSuccessful"+response);
                   }
               }

               @Override
               public void onFailure(Call<AddCasePojo> call, Throwable t) {
                   Log.d(TAG, "onFailure: "+t);

               }
           });
       }




    }

    private boolean validate() {
        caseTitle=title.getText().toString();
        judge_name=judgeName.getText().toString();
        culprit_name=culpName.getText().toString();
        caseDetail=detail.getText().toString();
        caseStartingDate=date.getText().toString();
        currentDate=getCurrentDate();
        complaint_name=complaint.getText().toString();

        if (caseTitle.length()==0||caseTitle.isEmpty()||caseTitle==""){
            title.requestFocus();
            title.setError("Enter title");
            return false;

        }
        if (judge_name.isEmpty()){
            judgeName.requestFocus();

            judgeName.setError("Enter judge name");
            return false;

        }
        if (culprit_name.isEmpty()){
            culpName.requestFocus();
            culpName.setError("Enter culprit name");
            return false;
        }
        if (complaint_name.isEmpty()){
            complaint.requestFocus();
            complaint.setError("Enter Complaint name");
            return false;
        }
        if (caseStartingDate.isEmpty()){
            date.setError("Starting date is compulsory");
            return false;
        }

        if(caseServices.equals("Select")){
            Toast.makeText(this, "Select your services", Toast.LENGTH_SHORT).show();
            return false;
        }
       if (caseDetail.isEmpty()){
           detail.requestFocus();

           detail.setError("Enter case detail");
           return false;
        }



    return true;


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position==0){

        }
        else {
            caseCategory=parent.getItemAtPosition(position).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.date:
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                int month = monthOfYear + 1;
                                String formattedMonth = "" + month;
                                String formattedDayOfMonth = "" + dayOfMonth;

                                if(month < 10){

                                    formattedMonth = "0" + month;
                                }
                                if(dayOfMonth < 10){

                                    formattedDayOfMonth = "0" + dayOfMonth;
                                }


                                date.setText(formattedDayOfMonth + "-" +formattedMonth + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                break;

        }
    }

    public String getCurrentDate(){
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        return formattedDate;

    }
}