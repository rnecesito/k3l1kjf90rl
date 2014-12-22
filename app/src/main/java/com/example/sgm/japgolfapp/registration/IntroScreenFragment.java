package com.example.sgm.japgolfapp.registration;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sgm.japgolfapp.BaseFragment;
import com.example.sgm.japgolfapp.R;

import butterknife.OnClick;


public class IntroScreenFragment extends BaseFragment{

    @OnClick(R.id.to_new_registration)
    public void viewNewRegistration() {
        showFragmentAndAddToBackStack(new PrimaryScreenFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_intro_screen, container, false);
    }
}
