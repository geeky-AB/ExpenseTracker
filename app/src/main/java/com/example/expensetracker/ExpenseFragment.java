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

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
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
import java.util.Date;

import Model.Data;


public class ExpenseFragment extends Fragment implements PopupMenu.OnMenuItemClickListener {


//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//
//    private String mParam1;
//    private String mParam2;
//
//
//    // TODO: Rename and change types and number of parameters
//
//    public static ExpenseFragment newInstance(String param1, String param2) {
//        ExpenseFragment fragment = new ExpenseFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }

    public ExpenseFragment() {
        // Required empty public constructor
    }

    private FirebaseAuth mAuth;
    private DatabaseReference mExpenseDatabase;
    private DatabaseReference expenseByMonthDatabase;

    private RecyclerView recyclerView;
    private TextView expenseTotalTxt;

    private String type;
    private String comment;
    private String amount;
    private String date;
    private String post_key;

    String userId;
    String userMonth;

    TextView monthEdt;
    ImageButton search;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;
        view = inflater.inflate(R.layout.fragment_expense, container, false);

        monthEdt = view.findViewById(R.id.currMonthTxtExpense);
        search = view.findViewById(R.id.searchBtnExpense);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        userId = mUser.getUid();
        LocalDate localDate = LocalDate.now();
        Month monthObj = localDate.getMonth();
        String currMonth = String.valueOf(monthObj);
        monthEdt.setText(currMonth);
        userMonth = monthEdt.getText().toString().toUpperCase().trim();

        mExpenseDatabase = FirebaseDatabase.getInstance().getReference().child("ExpenseData").child(userId).child(userMonth);

        recyclerView = view.findViewById(R.id.recyclerExpense);
        expenseTotalTxt = view.findViewById(R.id.totalExpenseTxt);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);


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
        mExpenseDatabase = FirebaseDatabase.getInstance().getReference().child("ExpenseData").child(userId).child(userMonth);
        FirebaseRecyclerAdapter<Data, MyViewHolder> adapter = new FirebaseRecyclerAdapter<Data, MyViewHolder>
                (
                        Data.class,
                        R.layout.recycler_data,
                        MyViewHolder.class,
                        mExpenseDatabase
                ) {
            @Override
            protected void populateViewHolder(MyViewHolder myViewHolder, Data data, int i) {
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
                        ShowPopup(v);
                        return true;
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);

        mExpenseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                int totalExpense = 0;
                for(DataSnapshot mSnapshot:snapshot.getChildren()){
                    Data data = mSnapshot.getValue(Data.class);

                    totalExpense+= data.getAmount();
                }
                String expenseValTotal = String.valueOf(totalExpense);
                expenseTotalTxt.setText(expenseValTotal);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    public void showPopUp1(View v){
        PopupMenu popUp = new PopupMenu(getActivity(),v);
        popUp.setOnMenuItemClickListener(this::onMenuItemClick);
        popUp.inflate(R.menu.months_item);
        popUp.show();
    }

    public void ShowPopup(View v){
        PopupMenu popupMenu = new PopupMenu(getActivity(),v);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.recycler_item_menu);
        popupMenu.show();

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.update :
                UpdateExpense();
                return true;
            case R.id.delete :
                DeleteExpense();
                return true;
            case R.id.view :
                ViewExpense();
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



    public static class MyViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public MyViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        private void setType(String type){
            TextView mType = mView.findViewById(R.id.typeTxt);
            mType.setText("  " + type);
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
            mAmount.setTextColor(Color.parseColor("#FF5733"));
        }
    }

    private void ViewExpense() {
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
    private void DeleteExpense() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Delete this Expense?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mExpenseDatabase.child(post_key).removeValue();
                FirebaseDatabase.getInstance().getReference().child("ExpenseData").child(userId).child("Total").child(post_key).removeValue();
                Toast.makeText(getActivity(), "Expense Removed", Toast.LENGTH_SHORT).show();
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

    private void UpdateExpense() {
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
        updateBtn.setText("Update");
        Button cancelBtn = mView.findViewById(R.id.cancelBtn);

        typeEdt.setText(type);
        amountEdt.setText(amount);
        commentEdt.setText(comment);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                type = typeEdt.getText().toString().trim();
                amount = amountEdt.getText().toString().trim();
                int updateAmount = Integer.parseInt(amount);
                comment = commentEdt.getText().toString().trim();
//                String date = DateFormat.getDateInstance().format(new Date());
                Data data = new Data(updateAmount,type,comment,post_key,date,String.valueOf(LocalDate.now().getDayOfMonth()));
                mExpenseDatabase.child(post_key).setValue(data);
                FirebaseDatabase.getInstance().getReference().child("ExpenseData").child(userId).child("Total").child(post_key).setValue(data);
                Toast.makeText(getActivity(), "Expense Updated", Toast.LENGTH_SHORT).show();
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