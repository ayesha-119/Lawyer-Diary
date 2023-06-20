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

import com.uptree.lawdiary.Activities.BookmarkDetailActivity;
import com.uptree.lawdiary.Model.AddCasePojo;
import com.uptree.lawdiary.R;
import com.uptree.lawdiary.Retrofit.RetrofitClient;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClosedCasesAdap extends RecyclerView.Adapter<ClosedCasesAdap.ViewHolder> {

    Context context;
    List<AddCasePojo> caseList;
    public static final String TAG = "dellcases";
    String case_id,today_date;


    public ClosedCasesAdap(Context context, List<AddCasePojo> caseList) {
        this.context = context;
        this.caseList = caseList;
    }

    @NonNull
    @NotNull
    @Override
    public ClosedCasesAdap.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.all_cases,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ClosedCasesAdap.ViewHolder holder, int position) {

        final AddCasePojo list = caseList.get(position);

        holder.title.setText(caseList.get(position).getCase_title());
        holder.txtcategory.setText(caseList.get(position).getCase_category());

        case_id=caseList.get(position).getId();


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String start_date=caseList.get(position).getCase_starting_date();
                String uid=caseList.get(position).getUser_id();
                String case_category=caseList.get(position).getCase_category();
                String detail=caseList.get(position).getCase_details();
                String Title=caseList.get(position).getCase_title();
                String culp=caseList.get(position).getName_of_culprit();
                String judge=caseList.get(position).getName_of_judge();
                case_id=caseList.get(position).getId();
                String complaint=caseList.get(position).getName_of_complain();
                String service=caseList.get(position).getOur_services_for();


                Intent intent=new Intent(context, BookmarkDetailActivity.class);

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
                v.getContext().startActivity(intent);
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

    }

    @Override
    public int getItemCount() {
        return caseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title,txtcategory;
        CardView cardView;
        Button dellBtn;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            title=itemView.findViewById(R.id.title);
            txtcategory=itemView.findViewById(R.id.cName);
            cardView=itemView.findViewById(R.id.card_case_detail);
            dellBtn = itemView.findViewById(R.id.favBtn);
        }
    }
}
