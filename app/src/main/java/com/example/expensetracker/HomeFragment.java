package com.example.expensetracker;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Model.Data;
import Model.Personal;


public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }

    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mIncomeDatabase;
    private DatabaseReference mExpenseDatabase;
    private DatabaseReference mTotalExpense;
    private DatabaseReference mTotalIncome;
    private DatabaseReference mSavingData;

    String userId;
    String dateInt;
    private TextView totalIncomeTxt;
    private TextView avgIncomeTxt;
    private TextView totalExpenseTxt;
    private TextView avgExpenseTxt;
    private TextView nameTxt;
    private TextView currentMonth;
    private TextView currentMonthIncome;
    private TextView currentMonthExpense;
    private TextView currentMonthSaving;
    private TextView savingGoal;
    CardView cardViewSaving;
    // list count date;
    List<String> listIncomeDate;
    List<String> listExpenseDate;
    int currMonthSavingInt;
//    List<Integer> listSaving;

    LottieAnimationView animationLottie;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        totalExpenseTxt = view.findViewById(R.id.totalExpense);
        avgExpenseTxt = view.findViewById(R.id.avgExpense);
        totalIncomeTxt = view.findViewById(R.id.totalIncome);
        avgIncomeTxt = view.findViewById(R.id.avgIncome);
        nameTxt = view.findViewById(R.id.nameTxtUser);
        currentMonth = view.findViewById(R.id.currentMonthTxt);
        currentMonthExpense = view.findViewById(R.id.currMonthExpense);
        currentMonthIncome = view.findViewById(R.id.currMonthIncome);
        currentMonthSaving = view.findViewById(R.id.currMonthSaving);
        savingGoal = view.findViewById(R.id.savingMonthTxt);
        animationLottie = view.findViewById(R.id.animateLottie);
        cardViewSaving = view.findViewById(R.id.savingCard);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        userId = mUser.getUid();
        LocalDate localDate = LocalDate.now();
        Month monthObj = localDate.getMonth();
        String currMonth = String.valueOf(monthObj);
        currentMonth.setText("Current Month : "+currMonth);
        dateInt = String.valueOf(localDate.getDayOfMonth());

        listExpenseDate = new ArrayList<>();
        listIncomeDate = new ArrayList<>();
//        listSaving = new ArrayList<>();

        mIncomeDatabase = FirebaseDatabase.getInstance().getReference().child("IncomeData").child(userId).child(currMonth);

        mExpenseDatabase = FirebaseDatabase.getInstance().getReference().child("ExpenseData").child(userId).child(currMonth);
        mTotalExpense = FirebaseDatabase.getInstance().getReference().child("ExpenseData").child(userId).child("Total");
        mTotalIncome = FirebaseDatabase.getInstance().getReference().child("IncomeData").child(userId).child("Total");
        mSavingData = FirebaseDatabase.getInstance().getReference().child("ExpenseData").child(userId);

        showName();
        FloatingActionButton fabIncome = (FloatingActionButton)view.findViewById(R.id.incomeFab);
        FloatingActionButton fabExpense = (FloatingActionButton)view.findViewById(R.id.expenseFab);
        fabIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddIncome();
            }
        });
        fabExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddExpense();
            }
        });
        mTotalIncome.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                int totalIncome = 0;
                float avgTotalIncome;
                int countIncome = 0;
                for(DataSnapshot msnapShot: snapshot.getChildren()){
                    Data data = msnapShot.getValue(Data.class);
                    totalIncome+= data.getAmount();

                    if(listIncomeDate.size() == 0){
                        listIncomeDate.add(data.getDate());
                        countIncome++;
                    }
                    else {
                        if(listIncomeDate.contains(data.getDate())){
                        }
                        else{
                            listIncomeDate.add(data.getDate());
                            countIncome++;
                        }
                    }
                }

                totalIncomeTxt.setText(String.valueOf(totalIncome));
                avgTotalIncome = (float)totalIncome/countIncome;
                avgIncomeTxt.setText(String.valueOf(avgTotalIncome));
                listIncomeDate.removeAll(listIncomeDate);
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        mTotalExpense.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                int totalExpense = 0;
                float avgTotalExpense;
                int countExpense = 0;
                for(DataSnapshot msnapShot : snapshot.getChildren()){
                    Data data = msnapShot.getValue(Data.class);
                    totalExpense+=data.getAmount();
                    if(listExpenseDate.size() == 0){
                        listExpenseDate.add(data.getDate());
                        countExpense++;
                    }
                    else {
                        if(listExpenseDate.contains(data.getDate())){
                        }
                        else{
                            listExpenseDate.add(data.getDate());
                            countExpense++;
                        }
                    }
                }
                totalExpenseTxt.setText(String.valueOf(totalExpense));
                avgTotalExpense = (float)totalExpense/countExpense;
                avgExpenseTxt.setText(String.valueOf(avgTotalExpense));
                listExpenseDate.removeAll(listExpenseDate);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        currentMonthData();
        currentSavingsData();
//        animationLottie.setAnimation("");



        return view;
    }

    private void showAnimationPos() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View mView = inflater.inflate(R.layout.lottie,null);
        builder.setView(mView);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showAnimationNeg(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View mView = inflater.inflate(R.layout.lottiesad,null);
        builder.setView(mView);

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void currentSavingsData() {
        mSavingData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                Personal personal = snapshot.getValue(Personal.class);
                savingGoal.setText(personal.getSaving());
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    private void currentMonthData() {
        final int[] currMonthInc = {0};
        final int[] currMonthExp = {0};
        mIncomeDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                currMonthInc[0] = 0;
                for(DataSnapshot mSnapShot: snapshot.getChildren()){
                    Data data = mSnapShot.getValue(Data.class);
                    currMonthInc[0] +=data.getAmount();
                }
                currentMonthIncome.setText(String.valueOf(currMonthInc[0]));
                currMonthSavingInt = currMonthInc[0] - currMonthExp[0];
                if(currMonthInc[0] > currMonthExp[0]) currentMonthSaving.setTextColor(Color.parseColor("#00FF00"));
                else if(currMonthInc[0] < currMonthExp[0]) currentMonthSaving.setTextColor(Color.parseColor("#FF0000"));
                currentMonthSaving.setText(String.valueOf(currMonthSavingInt));
//                cardViewSaving.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if(currMonthSavingInt>0){
//                            showAnimationPos();
//                        }
//                        else{
//                            showAnimationNeg();
//                        }
//                    }
//                });
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        mExpenseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                currMonthExp[0] = 0;
                for(DataSnapshot mSnapShot: snapshot.getChildren()){
                    Data data = mSnapShot.getValue(Data.class);
                    currMonthExp[0] +=data.getAmount();
                }
                currentMonthExpense.setText(String.valueOf(currMonthExp[0]));
                Log.d("currMonthinc", String.valueOf(currMonthInc[0]));
                currMonthSavingInt = currMonthInc[0] - currMonthExp[0];
                if(currMonthInc[0] > currMonthExp[0]) currentMonthSaving.setTextColor(Color.parseColor("#00FF00"));
                else if(currMonthInc[0] < currMonthExp[0]) currentMonthSaving.setTextColor(Color.parseColor("#FF0000"));
                currentMonthSaving.setText(String.valueOf(currMonthSavingInt));
//                cardViewSaving.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if(currMonthSavingInt>0){
//                            showAnimationPos();
//                        }
//                        else{
//                            showAnimationNeg();
//                        }
//                    }
//                });

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        cardViewSaving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currMonthSavingInt>0){
                    showAnimationPos();
                }
                else{
                    showAnimationNeg();
                }
            }
        });

    }

    public void showName(){
        Log.d("TAG", "showName: "+userId);
        FirebaseDatabase.getInstance().getReference().child("ExpenseData").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                Personal personal = snapshot.getValue(Personal.class);

                nameTxt.setText("Welcome : "+personal.getName());
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    public void AddIncome(){
        AlertDialog.Builder mDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View mView = inflater.inflate(R.layout.dialog_layout_insert,null);
        mDialog.setView(mView);

        AlertDialog dialog = mDialog.create();

        dialog.setCancelable(false);

        EditText amountEdt = mView.findViewById(R.id.amountEdt);
        EditText commentEdt = mView.findViewById(R.id.commentEdt);
        EditText typeEdt = mView.findViewById(R.id.typeEdt);

        Button addBtn = mView.findViewById(R.id.addBtn);
        Button cancelBtn = mView.findViewById(R.id.cancelBtn);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                String type = typeEdt.getText().toString().trim();
                String amount = amountEdt.getText().toString().trim();
                String comment = commentEdt.getText().toString().trim();

                if(TextUtils.isEmpty(amount)){
                    amountEdt.setError("Required");
                    return;
                }
                if(TextUtils.isEmpty(type)){
                    typeEdt.setError("Required");
                    return;
                }

                int realAmount = Integer.parseInt(amount);
                if(TextUtils.isEmpty(comment)){
                    commentEdt.setError("Required");
                    return;
                }
                String id = mIncomeDatabase.push().getKey();
                String idTotal = mTotalIncome.push().getKey();
                String mDate = DateFormat.getDateInstance().format(new Date());

                Data data = new Data(realAmount, type, comment, id, mDate,dateInt);
//                Data dataTotal = new Data(realAmount, type, comment, idTotal, mDate,dateInt);
                mIncomeDatabase.child(id).setValue(data);
                mTotalIncome.child(id).setValue(data);

                Toast.makeText(getActivity(), "Income Added", Toast.LENGTH_SHORT).show();
                dialog.dismiss();


            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public void AddExpense(){
        AlertDialog.Builder mDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View mView = inflater.inflate(R.layout.dialog_layout_insert,null);
        mDialog.setView(mView);

        AlertDialog dialog = mDialog.create();
        dialog.setCancelable(false);

        EditText amountEdt = mView.findViewById(R.id.amountEdt);
        EditText commentEdt = mView.findViewById(R.id.commentEdt);
        EditText typeEdt = mView.findViewById(R.id.typeEdt);

        Button addBtn = mView.findViewById(R.id.addBtn);
        Button cancelBtn = mView.findViewById(R.id.cancelBtn);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String type = typeEdt.getText().toString().trim();
                String amount = amountEdt.getText().toString().trim();
                String comment = commentEdt.getText().toString().trim();

                if(TextUtils.isEmpty(amount)){
                    amountEdt.setError("Required");
                    return;
                }
                if(TextUtils.isEmpty(type)){
                    typeEdt.setError("Required");
                    return;
                }

                int realAmount = Integer.parseInt(amount);
                if(TextUtils.isEmpty(comment)){
                    commentEdt.setError("Required");
                    return;
                }

                String id = mExpenseDatabase.push().getKey();
                String idTotal = mTotalExpense.push().getKey();
                String mDate = DateFormat.getDateInstance().format(new Date());
                Data data = new Data(realAmount, type, comment, id, mDate,dateInt);
//                Data dataTotal = new Data(realAmount, type, comment, idTotal, mDate,dateInt);
                mExpenseDatabase.child(id).setValue(data);
                mTotalExpense.child(id).setValue(data);
                Toast.makeText(getActivity(), "Expense Added", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}