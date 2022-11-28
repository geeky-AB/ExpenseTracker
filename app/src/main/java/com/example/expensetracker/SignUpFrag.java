package com.example.expensetracker;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class SignUpFrag extends Fragment {
    public EditText nameSignupEt;
    public EditText emailSignupEt;
    public EditText passwordSignupEt;
    public TextView alreadyUserTxt;
    public EditText savingUserEdt;
    public Button signUpBtn;
    MainActivity mainActivity;

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
////        nameSignupEt = (EditText) getView().findViewById(R.id.nameSignup);
//
//    }


    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.signup_frag,container,false);
        nameSignupEt = (EditText) root.findViewById(R.id.nameSignup);
        emailSignupEt = (EditText) root.findViewById(R.id.emailSignup);
        passwordSignupEt = (EditText) root.findViewById(R.id.passwordSignup);
        alreadyUserTxt = (TextView) root.findViewById(R.id.alreadyUser1);
        savingUserEdt = (EditText) root.findViewById(R.id.savingInfoEdt);
//        AdapterLogin adapterLogin = new AdapterLogin(getChildFragmentManager(),getActivity(),1);
        signUpBtn =(Button) root.findViewById(R.id.btnSignup);
        mainActivity = (MainActivity) getActivity();
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameSignupEt.getText().toString();
                String email = emailSignupEt.getText().toString();
                String pass = passwordSignupEt.getText().toString();
                String savings = savingUserEdt.getText().toString();
                if(TextUtils.isEmpty(name)){
                    nameSignupEt.setError("Name Required");
                    return;
                }
                if(TextUtils.isEmpty(savings)){
                    savingUserEdt.setError("Enter desired amount you want to save each month");
                    return;
                }
                if(TextUtils.isEmpty(email)){
                    emailSignupEt.setError("Email Required");
                    return;
                }
                if(TextUtils.isEmpty(pass)){
                    passwordSignupEt.setError("Password Required");
                    return;
                }
                mainActivity.register(name, savings, email, pass);
            }
        });
        return root;
    }
}
