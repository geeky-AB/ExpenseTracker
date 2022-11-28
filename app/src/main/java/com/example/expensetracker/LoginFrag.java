package com.example.expensetracker;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class LoginFrag extends Fragment {
    public EditText emailEt;
    public EditText passwordEt;
    public TextView frgtPassTxt;
    public Button logInBtn;
    MainActivity mainActivity;

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_frag,container,false);
        emailEt = (EditText) root.findViewById(R.id.emailLogin);
        passwordEt = (EditText) root.findViewById(R.id.passwordLogin);
        frgtPassTxt = (TextView) root.findViewById(R.id.frgtPass);
        logInBtn = (Button) root.findViewById(R.id.btnLogin);
        mainActivity = (MainActivity) getActivity();
        frgtPassTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.resetPassword(v);
            }
        });
        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEt.getText().toString();
                String pass = passwordEt.getText().toString();
                if(TextUtils.isEmpty(email)){
                    emailEt.setError("Email Required");
                    return;
                }
                if(TextUtils.isEmpty(pass)){
                    passwordEt.setError("Password Required");
                    return;
                }
                mainActivity.login(email, pass);
            }
        });
        return root;
    }
}
