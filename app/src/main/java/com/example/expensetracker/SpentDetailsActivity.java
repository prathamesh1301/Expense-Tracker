package com.example.expensetracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpentDetailsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseHelper db;
    List<String> titles;
    List<String> amounts;
    List<String> date;
    List<String> time;
    RecyclerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spent_details);
        recyclerView=findViewById(R.id.recyclerView);
        db=new DatabaseHelper(SpentDetailsActivity.this);
        titles=new ArrayList<>();
        amounts=new ArrayList<>();
        date=new ArrayList<>();
        time=new ArrayList<>();
        storeInArray();

        adapter=new RecyclerAdapter(titles,amounts,date,time,SpentDetailsActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(SpentDetailsActivity.this));
        recyclerView.scrollToPosition(titles.size());
        recyclerView.setAdapter(adapter);


    }

    private void storeInArray(){
        Cursor res=db.getAllData();
        if(res.getCount()==0){
            return;
        }else{
            res.moveToFirst();
            titles.add(res.getString(1));
            amounts.add(res.getString(2));
            date.add(res.getString(3));
            time.add(res.getString(4));

            while (res.moveToNext()){
                titles.add(res.getString(1));
                amounts.add(res.getString(2));
                date.add(res.getString(3));
                time.add(res.getString(4));

            }
        }


    }
}