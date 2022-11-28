package com.example.expensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import Model.Personal;

public class MainActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    Personal personal;

    boolean flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        tabLayout.addTab(tabLayout.newTab().setText("Login"));
        tabLayout.addTab(tabLayout.newTab().setText("Sign Up"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final AdapterLogin adapterLogin = new AdapterLogin(getSupportFragmentManager(),this,tabLayout.getTabCount());
        viewPager.setAdapter(adapterLogin);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        Intent intent = getIntent();
        if(intent.getStringExtra("EXIT") != null && intent.getStringExtra("EXIT").equals("exit")){
            Log.d("rana","chalra");
            onBackPressed();
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void register(String name,String saving, String email, String password){
        progressDialog.setMessage("Registering..");
        progressDialog.show();
        personal = new Personal(name, saving);
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
//                    Log.d("rana","successful");
                    FirebaseDatabase.getInstance().getReference().child("ExpenseData").child(firebaseAuth.getCurrentUser().getUid()).setValue(personal);
                    FirebaseDatabase.getInstance().getReference().child("IncomeData").child(firebaseAuth.getCurrentUser().getUid()).setValue(personal);
                    Toast.makeText(MainActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    flag = true;
                    Intent intent1 = new Intent(getApplicationContext(),HomeActivity.class);
                    intent1.putExtra("name",name);
                    startActivity(intent1);

                }
                else{
                    progressDialog.dismiss();
//                    Log.d("rahul","unsuccessful");
                    flag = false;
                    Toast.makeText(MainActivity.this, "Registration Unsuccessful", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void login(String email, String password){
//        Log.d("rana","rahul");
        progressDialog.setMessage("Logging In");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Wrong Credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void resetPassword(View v){
//        startActivity(new Intent(this, ResetActivity.class));
        EditText emailPassReset;
        emailPassReset = new EditText(v.getContext());
        emailPassReset.setBackgroundColor(Color.WHITE);
        emailPassReset.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("Reset Password!");
        builder.setMessage("Enter email to receive reset password link :");
        builder.setView(emailPassReset);

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String emailResetPass = emailPassReset.getText().toString();
                mAuth.sendPasswordResetEmail(emailResetPass).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(MainActivity.this, "Reset Email Sent", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error! Email not sent : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

