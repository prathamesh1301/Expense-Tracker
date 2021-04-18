package com.example.expensetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="newDatabase";
    private static final String TABLE_NAME="newTable";
    private static final String COL_1="ID";
    private static final String COL_2="TITLE";
    private static final String COL_3="AMOUNT";
    private static final String COL_4="DATE";
    private static final String COL_5="TIME";

    private SQLiteDatabase db;
    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_NAME+ " (ID INTEGER PRIMARY KEY AUTOINCREMENT,TITLE TEXT,AMOUNT TEXT,DATE TEXT,TIME TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);

    }

    public boolean addData(String title,String amount,String date,String time){
        db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(COL_2,title);
        values.put(COL_3,amount);
        values.put(COL_4,date);
        values.put(COL_5,time);
        long result=db.insert(TABLE_NAME,null,values);
        if(result==-1)
            return false;
        else
            return true;
    }

    public Cursor getAllData()
    {
        db=this.getWritableDatabase();
        Cursor res=db.rawQuery("SELECT * FROM "+TABLE_NAME,null);
        return res;
    }

    public void deleteItem(int id){
        db=this.getWritableDatabase();
        db.delete(TABLE_NAME,"ID=?",new String[] {String.valueOf(id)});
    }
}

