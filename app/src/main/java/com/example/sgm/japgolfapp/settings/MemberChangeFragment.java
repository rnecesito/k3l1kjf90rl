package com.example.sgm.japgolfapp.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sgm.japgolfapp.BaseFragment;
import com.example.sgm.japgolfapp.R;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by archie on 12/16/2014.
 */
public class MemberChangeFragment extends BaseFragment{

    View view_container;

    @InjectView(R.id.editTextEmail)
    EditText email;
    @InjectView(R.id.editTextName)
    EditText name;
    @InjectView(R.id.editTextPassword)
    EditText password;

    @InjectView(R.id.emailValidateText)
    TextView invalidEmail;
    @InjectView(R.id.nameValidateText)
    TextView invalidName;
    @InjectView(R.id.passwordValidateText)
    TextView invalidPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_member_change, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view_container = view;
    }

    @OnClick(R.id.saveB)
    public void saveButton(){
        validate();
    }
public void validate(){
    if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
        invalidEmail.setVisibility(View.VISIBLE);
    }
    if(name.getText().toString().isEmpty() ){
        invalidName.setVisibility(View.VISIBLE);
    }
    if(password.getText().toString().isEmpty()){
        invalidPassword.setVisibility(View.VISIBLE);
    }

    if(android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
        invalidEmail.setVisibility(View.INVISIBLE);
    }
    if(!name.getText().toString().isEmpty()){
        invalidName.setVisibility(View.INVISIBLE);
    }
    if(!password.getText().toString().isEmpty()){
        invalidPassword.setVisibility(View.INVISIBLE);
    }
}
}
