package com.example.expensetracker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    Button addMoneyBtn,spendMoneyBtn,spentDetailsBtn;
    TextView currentBalancetv;
    SharedPreferences sharedPreferences;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentBalancetv=findViewById(R.id.currentBalancetv);
        addMoneyBtn=findViewById(R.id.addMoneyBtn);
        spendMoneyBtn=findViewById(R.id.spendMoneyBtn);
        spentDetailsBtn=findViewById(R.id.spentDetailsBtn);
        sharedPreferences=getSharedPreferences("Prefs",MODE_PRIVATE);
        String balance=sharedPreferences.getString("balance","0");
        currentBalancetv.setText("Current Balance: "+balance);
        db=new DatabaseHelper(MainActivity.this);

        addMoneyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog=new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle("Add Money");
                alertDialog.setMessage("Enter amount to be added");
                final EditText edittext = new EditText(MainActivity.this);
                alertDialog.setView(edittext);
                alertDialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String initial=sharedPreferences.getString("balance","0");
                        Integer initialBal=Integer.parseInt(initial);
                        String balance=edittext.getText().toString().trim();
                        if(balance.length()==0){
                            Toast.makeText(MainActivity.this, "Amount was not added", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Integer finalBal=initialBal+Integer.parseInt(balance);
                        String TotalBal=String.valueOf(finalBal);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("balance",TotalBal);
                        editor.commit();
                        currentBalancetv.setText("Current Balance: "+TotalBal);

                    }
                });

                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                alertDialog.show();


            }
        });

        spendMoneyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog=new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle("Spend Money");
                alertDialog.setMessage("Spending Details:");
                Context context=MainActivity.this;
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);


                final EditText titleBox = new EditText(context);
                titleBox.setHint("Title");
                layout.addView(titleBox); // Notice this is an add method


                final EditText descriptionBox = new EditText(context);
                descriptionBox.setHint("Amount");
                layout.addView(descriptionBox);

                alertDialog.setView(layout);
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(titleBox.getText().toString().length()==0 || descriptionBox.getText().toString().length()==0){
                            Toast.makeText(context, "Did not spend", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Integer currentBalance=Integer.parseInt(sharedPreferences.getString("balance","0"));
                        Integer amountDemanded=Integer.parseInt(descriptionBox.getText().toString());

                        if(amountDemanded>currentBalance){
                            Toast.makeText(context, "Your balance is lesser than demanded", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Integer remainingBalance=currentBalance-amountDemanded;
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("balance",String.valueOf(remainingBalance));
                            currentBalancetv.setText("Current Balance: "+String.valueOf(remainingBalance));
                            editor.commit();
                            List<String> titles_Box=new ArrayList<>();
                            Date c = Calendar.getInstance().getTime();
                            System.out.println("Current time => " + c);

                            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                            String formattedDate = df.format(c);

                            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-4:00"));
                            Date currentLocalTime = cal.getTime();
                            DateFormat date = new SimpleDateFormat("KK:mm");
                            date.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
                            String localTime = date.format(currentLocalTime);

                            boolean a=db.addData(titleBox.getText().toString(),String.valueOf(amountDemanded),formattedDate,localTime);
                            if(a){
                                Toast.makeText(context, "Details added to database", Toast.LENGTH_SHORT).show();

                            }else{
                                Toast.makeText(context, "Error while uploading to database", Toast.LENGTH_SHORT).show();

                            }
                            titles_Box.add(titleBox.getText().toString());
                            Log.d("Title box", String.valueOf(titles_Box));


                        }

                    }
                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.show();

            }
        });

    spentDetailsBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(MainActivity.this,SpentDetailsActivity.class));
        }
    });
    }
}