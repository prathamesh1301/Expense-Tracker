package com.example.expensetracker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    List<String> titleList;
    List<String> amountList;
    List<String> dateList;
    List<String> timeList;
    DatabaseHelper db;
    Context context;

    public RecyclerAdapter(List<String> titleList, List<String> amountList,List<String> dateList,List<String> timeList, Context context) {
        this.titleList = titleList;
        this.amountList = amountList;
        this.dateList = dateList;
        this.timeList = timeList;
        this.context = context;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_layout,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String s=titleList.get(position);
        String t=amountList.get(position);
        holder.title.setText(s);
        holder.amount.setText("Rs "+t);
        holder.time.setText(timeList.get(position));
        holder.date.setText(dateList.get(position));
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder alertDialog=new AlertDialog.Builder(context);
                alertDialog.setTitle("Delete");
                alertDialog.setMessage("Are you sure?");
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db=new DatabaseHelper(context);
                        db.deleteItem(position);
                        titleList.remove(position);
                        amountList.remove(position);
                        notifyDataSetChanged();
                    }
                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.show();
                return true;
            }
        });


    }

    @Override
    public int getItemCount() {
        return titleList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
    TextView title,amount,time,date;
    CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title);
            amount=itemView.findViewById(R.id.amount);
            cardView=itemView.findViewById(R.id.cardView);
            date=itemView.findViewById(R.id.date);
            time=itemView.findViewById(R.id.time);


        }
    }
}
