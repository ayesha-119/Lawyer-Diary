package com.uptree.lawdiary.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.uptree.lawdiary.Model.HearingModel;
import com.uptree.lawdiary.R;
import com.uptree.lawdiary.Retrofit.RetrofitClient;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HearingAdap extends RecyclerView.Adapter<HearingAdap.ViewHolder>{
    Context context;
    List<HearingModel> caseList;
    public static final String TAG = "deletehearing";
    String case_id,uid,date_id;

    public HearingAdap(Context context, List<HearingModel> caseList) {
        this.context = context;
        this.caseList = caseList;


    }

    @NonNull
    @NotNull
    @Override
    public HearingAdap.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.list_items,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull HearingAdap.ViewHolder holder, int position) {
        HearingModel list = caseList.get(position);
        holder.nextDate.setText(list.getNew_date());

        case_id =list.getCase_id();
        uid = list.getUser_id();

        for (HearingModel model: caseList )
        {
            Log.d("abcde", ""+model.getNew_date());
        }

        holder.nextDate.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                 new AlertDialog.Builder(
                        context)
                        .setTitle("Delete Hearing Date")
                        .setMessage("Do you really want to delete this hearing date")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {

                                date_id = list.getId();

                                Log.d("checkdata", "onClick: " + "userid"+ "\t" +uid + "caseid"+ "\t" +case_id+ "dateid"+ "\t" +date_id);

                                Call<HearingModel> call = RetrofitClient.getInstance().getApi().deleteHearingDate(uid,case_id,date_id);

                                call.enqueue(new Callback<HearingModel>() {
                                    @Override
                                    public void onResponse(Call<HearingModel> call, Response<HearingModel> response) {
                                        HearingModel hearingResponse = response.body();

                                        if (response.isSuccessful())
                                        {
                                            Log.d(TAG, "onResponse:successful "+ response);
                                            String result = hearingResponse.getResult();
                                            Log.d(TAG, "result "+result);
                                            if (result.equals("Delete Successfully"))
                                            {
                                                Log.d(TAG, "onResponse: result"+ result);
                                                Toast.makeText(context, "case deleted! ", Toast.LENGTH_SHORT).show();
                                                notifyItemRemoved(position);
                                                notifyDataSetChanged();
                                                caseList.remove(position);
                                            }
                                        }
                                        else
                                        {
                                            Log.d(TAG, "onResponse: isNotSuccessful "+response);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<HearingModel> call, Throwable t) {
                                        Log.d(TAG, "onFailure: "+t);

                                    }
                                });




                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();


                return false;
            }
        });



    }

    @Override
    public int getItemCount() {
        return caseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nextDate;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            nextDate=itemView.findViewById(R.id.label);
        }
    }
}
