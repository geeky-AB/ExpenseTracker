package com.example.expensetracker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Model.Data;


public class IncomeFragment extends Fragment implements MenuItem.OnMenuItemClickListener{

    public IncomeFragment(){

    }

    private FirebaseAuth mAuth;
    private DatabaseReference mIncomeDatabase;
//    private DatabaseReference incomeByMonthDatabase;

    //Recycler View
    private RecyclerView recyclerView;
    //TextView
    private TextView incomeTotalTxt;
    private String type;
    private String amount;
    private String comment;
    private String date;
    private String post_key;
    String userId;
    String userMonth;

    TextView monthEdt;
    ImageButton search;
//    Spinner spinner;
    List<Integer> list;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;
        view = inflater.inflate(R.layout.fragment_income, container, false);

        monthEdt = view.findViewById(R.id.currMonthTxt);
        search = view.findViewById(R.id.searchBtn);


        mAuth = FirebaseAuth.getInstance();

        FirebaseUser mUser = mAuth.getCurrentUser();
        userId = mUser.getUid();
        LocalDate localDate = LocalDate.now();
        Month monthObj = localDate.getMonth();
        String currMonth = String.valueOf(monthObj);
        monthEdt.setText(currMonth);
        userMonth = monthEdt.getText().toString().toUpperCase().trim();
        mIncomeDatabase = FirebaseDatabase.getInstance().getReference().child("IncomeData").child(userId).child(userMonth);
        mIncomeDatabase.keepSynced(true);

        // recycler view

        recyclerView = view.findViewById(R.id.recyclerIncome);
        incomeTotalTxt = view.findViewById(R.id.totalIncomeTxt);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);


        // recycler view



        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStart();
            }
        });
        monthEdt.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showPopUp1(v);
                return true;
            }
        });

        return view;
    }



    @Override
    public void onStart() {
        super.onStart();
        userMonth = monthEdt.getText().toString().toUpperCase().trim();
        mIncomeDatabase = FirebaseDatabase.getInstance().getReference().child("IncomeData").child(userId).child(userMonth);

        FirebaseRecyclerAdapter<Data, MyViewHolder> adapter = new FirebaseRecyclerAdapter<Data, MyViewHolder>
                (
                        Data.class,
                        R.layout.recycler_data,
                        MyViewHolder.class,
                        mIncomeDatabase
                ) {
            @Override
            protected void populateViewHolder(MyViewHolder myViewHolder, Data data, int i) {
//                Log.i("data",data.toString());
                myViewHolder.setType(data.getType());
                myViewHolder.setComment(data.getComment());
                myViewHolder.setDate(data.getDate());
                myViewHolder.setAmount(data.getAmount());
                registerForContextMenu(myViewHolder.mView);
                myViewHolder.mView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        post_key = getRef(i).getKey();
                        type = data.getType();
                        comment = data.getComment();
                        date = data.getDate();
                        amount = String.valueOf(data.getAmount());
                        showPopup(v);
                        return true;
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);

        mIncomeDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {


                int totalIncome = 0;

                list = new ArrayList<>();

                for(DataSnapshot mSnapshot:snapshot.getChildren()){
                    Data data = mSnapshot.getValue(Data.class);

                    list.add(data.getAmount());
                    totalIncome += data.getAmount();

                }
                String totalValIncome = String.valueOf(totalIncome);
                incomeTotalTxt.setText(totalValIncome);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public MyViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        private void setType(String type){
            TextView mType = mView.findViewById(R.id.typeTxt);
            mType.setText("  "+type);
        }
        private void setComment(String comment){
            TextView mComment = mView.findViewById(R.id.commentTxt);
            mComment.setText("  "+comment);
        }
        private void setDate(String date){
            TextView mDate = mView.findViewById(R.id.dateTxt);
            mDate.setText(date);
        }
        private void setAmount(int amount){
            TextView mAmount = mView.findViewById(R.id.amountTxt);
            mAmount.setText(String.valueOf(amount));
            mAmount.setTextColor(Color.parseColor("#50C878"));
        }

    }

    public void showPopUp1(View v){
        PopupMenu popUp = new PopupMenu(getActivity(),v);
        popUp.setOnMenuItemClickListener(this::onMenuItemClick);
        popUp.inflate(R.menu.months_item);
        popUp.show();
    }

    public void showPopup(View v){
        PopupMenu popup = new PopupMenu(getActivity(),v);
        popup.setOnMenuItemClickListener(this::onMenuItemClick);
        popup.inflate(R.menu.recycler_item_menu);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.update :
                UpdateIncome();
                return true;
            case R.id.delete :
                DeleteIncome();
                return true;
            case R.id.view :
                ViewIncome();
                return true;
            case R.id.jan:
                monthEdt.setText("JANUARY");
                return true;
            case R.id.feb:
                monthEdt.setText("FEBRUARY");
                return true;
            case R.id.mar:
                monthEdt.setText("MARCH");
                return true;
            case R.id.apr:
                monthEdt.setText("APRIL");
                return true;
            case R.id.may:
                monthEdt.setText("MAY");
                return true;
            case R.id.jun:
                monthEdt.setText("JUNE");
                return true;
            case R.id.jul:
                monthEdt.setText("JULY");
                return true;
            case R.id.aug:
                monthEdt.setText("AUGUST");
                return true;
            case R.id.sep:
                monthEdt.setText("SEPTEMBER");
                return true;
            case R.id.oct:
                monthEdt.setText("OCTOBER");
                return true;
            case R.id.nov:
                monthEdt.setText("NOVEMBER");
                return true;
            case R.id.dec:
                monthEdt.setText("DECEMBER");
                return true;
            default :
                return false;
        }
    }

    public void UpdateIncome(){
        AlertDialog.Builder mDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View mView = inflater.inflate(R.layout.dialog_layout_insert,null);
        mDialog.setView(mView);

        AlertDialog dialog = mDialog.create();

        dialog.setCancelable(false);

        EditText amountEdt = mView.findViewById(R.id.amountEdt);
        EditText commentEdt = mView.findViewById(R.id.commentEdt);
        EditText typeEdt = mView.findViewById(R.id.typeEdt);
        Button updateBtn = mView.findViewById(R.id.addBtn);
        updateBtn.setText("UPDATE");
        Button cancelBtn = mView.findViewById(R.id.cancelBtn);

        amountEdt.setText(amount);
        commentEdt.setText(comment);
        typeEdt.setText(type);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String updateType = typeEdt.getText().toString();
                String updateComment = commentEdt.getText().toString();
                String updateAmount = amountEdt.getText().toString();
                int updateValAmount = Integer.parseInt(updateAmount);
//                String updateDate = DateFormat.getDateInstance().format(new Date());
                Data data = new Data(updateValAmount,updateType,updateComment,post_key,date,String.valueOf(LocalDate.now().getDayOfMonth()));
                mIncomeDatabase.child(post_key).setValue(data);
                FirebaseDatabase.getInstance().getReference().child("IncomeData").child(userId).child("Total").child(post_key).setValue(data);
                Toast.makeText(getActivity(), "Income Updated", Toast.LENGTH_SHORT).show();
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

    private void DeleteIncome() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Delete this Expense?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mIncomeDatabase.child(post_key).removeValue();
                FirebaseDatabase.getInstance().getReference().child("IncomeData").child(userId).child("Total").child(post_key).removeValue();
                Toast.makeText(getActivity(), "Income Removed", Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    private void ViewIncome() {
        AlertDialog.Builder mDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View mView = inflater.inflate(R.layout.view_data,null);
        mDialog.setView(mView);

        AlertDialog dialog = mDialog.create();
        dialog.setCancelable(false);

        TextView amountTxt = mView.findViewById(R.id.amountTxtView);
        TextView commentTxt = mView.findViewById(R.id.commentTxtView);
        TextView typeTxt = mView.findViewById(R.id.typeTxtView);
        TextView dateTxt = mView.findViewById(R.id.dateTxtView);
        Button closeBtn = mView.findViewById(R.id.closeBtn);

        amountTxt.setText(amount);
        commentTxt.setText(comment);
        typeTxt.setText(type);
        dateTxt.setText(date);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    // add income and show dialog
}