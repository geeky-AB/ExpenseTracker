package com.example.expensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import Model.Data;


public class PieChartActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference mExpenseMonthDatabase;
    DatabaseReference mIncomeMonthDatabase;
    FirebaseUser mUser;
    String userId;
    String userMonth;

    PieChart expenseChart;
    PieChart incomeChart;

    ArrayList<PieEntry> expenseMonth;
    ArrayList<PieEntry> incomeMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        userId = mUser.getUid();
        userMonth = getIntent().getStringExtra("MONTH");
        mExpenseMonthDatabase = FirebaseDatabase.getInstance().getReference().child("ExpenseData").child(userId).child(userMonth);
        mIncomeMonthDatabase = FirebaseDatabase.getInstance().getReference().child("IncomeData").child(userId).child(userMonth);

        expenseChart = findViewById(R.id.expensePieChart);
        incomeChart = findViewById(R.id.incomePieChart);

        showExpensePieChart();
        showIncomePieChart();

    }

    private void showIncomePieChart() {
        incomeMonth = new ArrayList<>();
        mIncomeMonthDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String date;
                int amt;
                for(DataSnapshot mSnapShot: snapshot.getChildren()){
                    Data data = mSnapShot.getValue(Data.class);
                    date = data.getDateInt();
                    amt = data.getAmount();
                    incomeMonth.add(new PieEntry(amt,date));
                }
                PieDataSet incomeDataSet = new PieDataSet(incomeMonth,userMonth+" INCOMES");
                incomeDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                incomeDataSet.setValueTextColor(Color.BLACK);
                incomeDataSet.setValueTextSize(16f);

                PieData incomeData = new PieData(incomeDataSet);
                incomeChart.setData(incomeData);
                incomeChart.setCenterText(userMonth+" INCOMES");
                incomeChart.getDescription().setEnabled(false);
                incomeChart.animate();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void showExpensePieChart() {
        expenseMonth = new ArrayList<>();
        mExpenseMonthDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String date;
                int amt;
                for(DataSnapshot mSnapShot:snapshot.getChildren()){
                    Data data = mSnapShot.getValue(Data.class);
                    date = data.getDateInt();
                    amt = data.getAmount();
                    expenseMonth.add(new PieEntry(amt,date));
                }
                PieDataSet expenseDataSet = new PieDataSet(expenseMonth,userMonth+" EXPENSES");
                expenseDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                expenseDataSet.setValueTextColor(Color.BLACK);
                expenseDataSet.setValueTextSize(16f);

                PieData expenseData = new PieData(expenseDataSet);
                expenseChart.setData(expenseData);
                expenseChart.setCenterText(userMonth+" EXPENSES");
                expenseChart.getDescription().setEnabled(false);
                expenseChart.animate();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}