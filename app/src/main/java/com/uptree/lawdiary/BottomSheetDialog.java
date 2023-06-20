package com.uptree.lawdiary;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.uptree.lawdiary.Activities.AddNewCase;
import com.uptree.lawdiary.Model.AddCasePojo;
import com.uptree.lawdiary.Retrofit.RetrofitClient;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BottomSheetDialog extends BottomSheetDialogFragment {
    EditText winner,judgment;
    String str_winner,str_judgement;
    public static final String TAG = "judgement";
    View v;
    Bundle bundle;
    String caseid,userid,value;
    RelativeLayout layout_view_judgement, layout_close_case;


    TextView txt_date , txt_judgement , txt_winner;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {
         v = inflater.inflate(R.layout.bottom_sheet,
                container, false);

         layout_close_case = v.findViewById(R.id.layout_case_close);
         layout_view_judgement = v.findViewById(R.id.layout_view_judgement);

         txt_date = v.findViewById(R.id.txt_case_date);
         txt_winner = v.findViewById(R.id.txt_case_winner);
         txt_judgement = v.findViewById(R.id.txt_case_judgement);


        bundle = getArguments();
        caseid = bundle.getString("caseid","");
        userid = bundle.getString("userid","");
        value = bundle.getString("value", "");

        if (value.equals("view")){

        layout_view_judgement.setVisibility(View.VISIBLE);
        layout_close_case.setVisibility(View.GONE);
            Call<AddCasePojo> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .fetchJudgement(userid, caseid);

            call.enqueue(new Callback<AddCasePojo>() {
                @Override
                public void onResponse(Call<AddCasePojo> call, Response<AddCasePojo> response) {
                    Log.d("lawDiary", "check01 ");

                    AddCasePojo user=response.body();
                    String result = user.getResult();
                    Log.d("lawDiary", "Result : "+result);

                    if (response.isSuccessful()){
                        Log.d("lawDiary", "isSuccessful :  "+response);

                        if (result.equals("Fetch Successfully") )
                        {
                            Log.d("lawDiary", "check3 ");

                            txt_date.setText(user.getNew_date());
                            txt_winner.setText(user.getWin());
                            txt_judgement.setText(user.getJudgement());
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
        else if ( value.equals("close"))
        {

            layout_view_judgement.setVisibility(View.GONE);
            layout_close_case.setVisibility(View.VISIBLE);

        }




        winner=v.findViewById(R.id.txt_winner);
        judgment=v.findViewById(R.id.detail);
        v.findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_winner = winner.getText().toString();
                str_judgement = judgment.getText().toString();
                String date = bundle.getString("date","");

                Call<AddCasePojo> call = RetrofitClient
                        .getInstance()
                        .getApi()
                        .addJudgement(userid,caseid,date,str_winner,str_judgement);

                Log.d("data", "onClick: "+"userid"+ userid + "\t"+"case id" + caseid + "\t"+"dateid"  + date + "\t"  +str_winner + "\t"  + str_judgement);


                call.enqueue(new Callback<AddCasePojo>() {
                    @Override
                    public void onResponse(Call<AddCasePojo> call, Response<AddCasePojo> response) {

                        Log.d("check", "check1 ");

                        AddCasePojo user=response.body();
                        String result = user.getResult();

                        if (response.isSuccessful()){
                            Log.d("check", "check2 ");
                            Log.d("check", "result: "+ result);

                            if (result.equals("Add successfully") ){
                                Log.d("check", "check3 ");


                                ProgressDialog pDialog = new ProgressDialog(getContext()); //Your Activity.this
                                pDialog.setMessage("Uploading...!");
                                pDialog.setCancelable(false);
                                pDialog.show();
                                pDialog.cancel();
                                Toast.makeText(getContext(), "Add successfully", Toast.LENGTH_SHORT).show();
                                BottomSheetDialog.this.dismiss();//to hide it


                                Log.d(TAG, "onResponse: isSuccessful"+response);

                            }

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
        });





        return v;
    }
}
