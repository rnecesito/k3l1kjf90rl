package com.example.sgm.japgolfapp.registration;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.sgm.japgolfapp.BaseFragment;
import com.example.sgm.japgolfapp.R;
import com.example.sgm.japgolfapp.history.PlayHistoryFragment;
import com.example.sgm.japgolfapp.scoreregistration.ScoreRegistrationFragment;
import com.example.sgm.japgolfapp.settings.MenuSettingsFragment;


public class MainMenuFragment extends BaseFragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_menu, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        RelativeLayout rl = (RelativeLayout) view.findViewById(R.id.new_registration_main);
        final View item = inflater.inflate(R.layout.side_menu, rl, false);
        rl.addView(item);

        Button settingButton = (Button)view.findViewById(R.id.settingButton);
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragmentAndAddToBackStack(new MenuSettingsFragment());
            }
        });

        Button historyButton = (Button)view.findViewById(R.id.historyButton);
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragmentAndAddToBackStack(new PlayHistoryFragment());
            }
        });

        ImageView historyButton2 = (ImageView)view.findViewById(R.id.imageHistoryButton);
        historyButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragmentAndAddToBackStack(new PlayHistoryFragment());
            }
        });


        Button scoreRegistrationButton = (Button)view.findViewById(R.id.scoreRegistrationButton);
        scoreRegistrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragmentAndAddToBackStack(new ScoreRegistrationFragment());
            }
        });

        ImageView scoreRegistrationButton2 = (ImageView)view.findViewById(R.id.imageScoreRegistrationButton);
        scoreRegistrationButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragmentAndAddToBackStack(new ScoreRegistrationFragment());
            }
        });

    }

}
