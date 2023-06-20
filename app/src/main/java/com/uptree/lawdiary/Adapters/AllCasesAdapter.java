package com.uptree.lawdiary.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.uptree.lawdiary.Activities.CaseDetailActivity;
import com.uptree.lawdiary.Model.AddCasePojo;
import com.uptree.lawdiary.R;
import com.uptree.lawdiary.Retrofit.RetrofitClient;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllCasesAdapter extends RecyclerView.Adapter<AllCasesAdapter.ViewHolder> {
    Context context;
    List<AddCasePojo> caseList;

    public static final String TAG = "dellcases";
    String case_id,today_date;
    String start_date,uid,case_category,detail,Title,culp,judge,complaint,service;

    public AllCasesAdapter(Context context, List<AddCasePojo> caseList) {
        this.context = context;
        this.caseList = caseList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //create table on first
        View view= LayoutInflater.from(context).inflate(R.layout.all_cases,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final AddCasePojo list = caseList.get(position);

        holder.title.setText(list.getCase_title());
        holder.txtcategory.setText(list.getCase_category());
        case_id=list.getId();



        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                start_date=list.getCase_starting_date();
                uid = list.getUser_id();
                case_category=list.getCase_category();
                detail=list.getCase_details();
                Title=list.getCase_title();
                culp=list.getName_of_culprit();
                judge=list.getName_of_judge();
                case_id=list.getId();
                complaint=list.getName_of_complain();
                service=list.getOur_services_for();

                Intent intent=new Intent(context, CaseDetailActivity.class);
                intent.putExtra("startdate",start_date);
                intent.putExtra("uid",uid);
                intent.putExtra("case_category",case_category);
                intent.putExtra("detail",detail);
                intent.putExtra("title",Title);
                intent.putExtra("judge",judge);
                intent.putExtra("culp",culp);
                intent.putExtra("caseId",case_id);
                intent.putExtra("complaint",complaint);
                intent.putExtra("service",service);
                context.startActivity(intent);
            }
        });

        holder.dellBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        AlertDialog alertDialog = new AlertDialog.Builder(context)
                        .setTitle("Delete Case")
                        .setMessage("This will permanently delete the data")

                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //set what would happen when positive button is clicked
                                Call<AddCasePojo> call= RetrofitClient.getInstance().getApi().deleteCase(case_id);
                                call.enqueue(new Callback<AddCasePojo>() {
                                    @Override
                                    public void onResponse(Call<AddCasePojo> call, Response<AddCasePojo> response) {
                                        if (response.isSuccessful()){
                                            Log.d(TAG, "onResponse: isSuccessful"+response);
                                            notifyItemRemoved(position);
                                            notifyDataSetChanged();
                                            caseList.remove(position);
                                            Toast.makeText(context, "case deleted! ", Toast.LENGTH_SHORT).show();
                                        }else {
                                            Log.d(TAG, "onResponse: isNotSuccessful"+response);
                                        }

                                    }

                                    @Override
                                    public void onFailure(Call<AddCasePojo> call, Throwable t) {
                                        Log.d(TAG, "onFailure: "+t);
                                    }
                                });

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //set what should happen when negative button is clicked
                            }
                        })
                        .show();

            }
        });


        today_date=getCurrentDate();
        Log.d("checkDate", "Check 1 : "+ start_date +" \t thisDate : "+today_date);
        /*if (start_date.equals(today_date))
        {
            Log.d("checkDate", "condition: running");

            String listSize = String.valueOf(caseList.size());
            if (caseList.size() != 0){

                Log.d("checkDate", "check 3 : running");
                    Intent intent= new Intent(context, NotificationReceiver.class);
                    context.sendBroadcast(intent);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast
                            (context,0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    intent.setData((Uri.parse("custom://"+System.currentTimeMillis())));


                    AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

                    Calendar calendar = Calendar.getInstance();
                    Calendar now = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    calendar.set(Calendar.HOUR_OF_DAY, 11);
                    calendar.set(Calendar.MINUTE, 33);
                    calendar.set(Calendar.SECOND, 1);

                if (now.after(calendar)) {
                    Log.d("Hey","Added a day");
                    calendar.add(Calendar.DATE, 1);
                }

                    manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                            AlarmManager.INTERVAL_DAY, pendingIntent);
                Log.d("Alarm","Alarms set for everyday 8 am.");


            }
        }*/



    }




    public String getCurrentDate(){
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-M-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        return formattedDate;

    }


    @Override
    public int getItemCount() {
        return caseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,txtcategory;
        CardView cardView;
        Button dellBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title);
            txtcategory=itemView.findViewById(R.id.cName);
            cardView=itemView.findViewById(R.id.card_case_detail);
            dellBtn = itemView.findViewById(R.id.favBtn);


        }

    }


}
