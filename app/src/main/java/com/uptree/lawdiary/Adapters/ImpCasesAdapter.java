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
import com.uptree.lawdiary.Activities.Dashboard;
import com.uptree.lawdiary.R;
import com.uptree.lawdiary.RoomDB.BookmarkList;

import java.util.List;

public class ImpCasesAdapter extends RecyclerView.Adapter<ImpCasesAdapter.ViewHolder> {
    private List<BookmarkList> bookmarkLists;
    Context context;

    public ImpCasesAdapter(List<BookmarkList> bookmarkLists, Context context) {
        this.bookmarkLists = bookmarkLists;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bookmarks_list,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        BookmarkList list = bookmarkLists.get(position);

        BookmarkList bookmarkList = new BookmarkList();

        String case_category=list.getCategory();
        String title = list.getTitle();
        viewHolder.title.setText(title);
        viewHolder.category.setText(case_category);




        viewHolder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(context)



                        .setTitle("Delete Case")

                        .setMessage("This will permanently delete the data")

                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //set what would happen when positive button is clicked
                                int id = list.getId();
                                if (Dashboard.bookmarkDatabase.bookmarkDao().isBookmark(id) != 1) {
                                    Dashboard.bookmarkDatabase.bookmarkDao().addBookmarkData(bookmarkList);
                                    //Log.e(TAG,"addBookmarkData: "+list.get(id).getVerseKey());

                                } else {
                                    Dashboard.bookmarkDatabase.bookmarkDao().deleteById(id);
                                    Toast.makeText(context, "Bookmark Delete!", Toast.LENGTH_SHORT).show();
                                    bookmarkLists.remove(position);
                                    notifyItemRemoved(position);
                                    notifyDataSetChanged();
                                }

                            }
                        })
//set negative button
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //set what should happen when negative button is clicked
                                Toast.makeText(context,"Nothing Happened",Toast.LENGTH_LONG).show();
                            }
                        })
                        .show();





            }
        });
        viewHolder.cardview1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String start_date=list.getStartingdate();

                String detail=list.getDetail();
                String Title=list.getTitle();
                String culp=list.getCulprit();
                String judge=list.getJudge();
                String case_id=list.getCaseId();
                String complaint=list.getComplaint();
                String service=list.getServices();





                Intent intent = new Intent(context, BookmarkDetailActivity.class);
                intent.putExtra("startdate",start_date);
                //intent.putExtra("uid",uid);
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
    }

    @Override
    public int getItemCount() {
        return bookmarkLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title,category;
        CardView cardview1;
        Button cancel;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            category = itemView.findViewById(R.id.Category);
            cardview1=itemView.findViewById(R.id.cardview1);
            cancel=itemView.findViewById(R.id.cancelBtn);

        }
    }
}
