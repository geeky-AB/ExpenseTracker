package com.example.expensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import Model.Data;

public class ReportsActivity extends AppCompatActivity {

    ArrayList<BarEntry> incomeMonth;
    ArrayList<BarEntry> expenseMonth;
    FirebaseAuth mAuth;
    DatabaseReference mExpenseMonthDatabase;
    DatabaseReference mIncomeMonthDatabase;
    FirebaseUser mUser;
    String userId;
    BarChart expenseBarChart;
    BarChart incomeBarChart;

    String userMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        expenseBarChart = findViewById(R.id.expenseBarChartMonth);
        incomeBarChart = findViewById(R.id.incomeBarChartMonth);



        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        userId = mUser.getUid();

        Intent intent = getIntent();
        userMonth = intent.getStringExtra("MONTH");
        mExpenseMonthDatabase = FirebaseDatabase.getInstance().getReference().child("ExpenseData").child(userId).child(userMonth);
        mIncomeMonthDatabase = FirebaseDatabase.getInstance().getReference().child("IncomeData").child(userId).child(userMonth);
        expenseMonth = new ArrayList<>();
        incomeMonth = new ArrayList<>();
        mExpenseMonthDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                int amt;
                int date;
                for(DataSnapshot mSnapShot:snapshot.getChildren()){
                    Data data = mSnapShot.getValue(Data.class);
                    date = Integer.parseInt(data.getDateInt());
                    amt = data.getAmount();
                    expenseMonth.add(new BarEntry(date,amt));
                }

//                Log.d("size of list",String.valueOf(expenseMonth.size()));
                BarDataSet expBarDataSet = new BarDataSet(expenseMonth,userMonth+" EXPENSES");
                expBarDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                expBarDataSet.setValueTextColor(Color.BLACK);
                expBarDataSet.setValueTextSize(16f);


                BarData expBarData = new BarData(expBarDataSet);
                expenseBarChart.setFitBars(true);
                expenseBarChart.setData(expBarData);
                expenseBarChart.getDescription().setEnabled(false);
                expenseBarChart.animateY(2000);

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        mIncomeMonthDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                int date;
                int amt;
                for(DataSnapshot mSnapShot:snapshot.getChildren()){
                    Data data = mSnapShot.getValue(Data.class);
                    date = Integer.parseInt(data.getDateInt());
                    amt = data.getAmount();
                    incomeMonth.add(new BarEntry(date,amt));
                }
                BarDataSet incBarDataSet = new BarDataSet(incomeMonth,userMonth+" INCOMES");
                incBarDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                incBarDataSet.setValueTextColor(Color.BLACK);
                incBarDataSet.setValueTextSize(16f);


                BarData incBarData = new BarData(incBarDataSet);
                incomeBarChart.setFitBars(true);
                incomeBarChart.setData(incBarData);
                incomeBarChart.getDescription().setEnabled(false);
                incomeBarChart.animateY(2000);

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });






    }
}