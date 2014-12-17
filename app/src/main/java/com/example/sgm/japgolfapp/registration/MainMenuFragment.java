package com.example.sgm.japgolfapp.registration;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.sgm.japgolfapp.BaseFragment;
import com.example.sgm.japgolfapp.R;
import com.example.sgm.japgolfapp.counting.ScoreCountingFragment;
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
        final View view_container = view;
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

        Button countingButton= (Button)view.findViewById(R.id.countingButton);
        countingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                RelativeLayout rl = (RelativeLayout) view_container.findViewById(R.id.new_registration_main);
                final View item = inflater.inflate(R.layout.counting_sub_menu, rl, false);
                item.setTag("counting_sub_menu");
                View tagged = view_container.findViewWithTag("counting_sub_menu");
                if(tagged == null) {
                    rl.addView(item);
                }
                Button scoreCountingButton = (Button)item.findViewById(R.id.scoreCounting);
                scoreCountingButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showFragmentAndAddToBackStack(new ScoreCountingFragment());
                    }
                });
//                showFragmentAndAddToBackStack(new MenuSettingsFragment());
            }
        });

    }

}
