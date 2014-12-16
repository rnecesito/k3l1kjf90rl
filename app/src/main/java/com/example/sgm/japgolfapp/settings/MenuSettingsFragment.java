package com.example.sgm.japgolfapp.settings;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.sgm.japgolfapp.BaseFragment;
import com.example.sgm.japgolfapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MenuSettingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MenuSettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuSettingsFragment extends BaseFragment {



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_menu, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        RelativeLayout rl = (RelativeLayout) view.findViewById(R.id.new_registration_main);
        final View item = inflater.inflate(R.layout.fragment_menu__settings, rl, false);
        rl.addView(item);

        Button memberCButton = (Button)view.findViewById(R.id.memberChangeButton);
        memberCButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragmentAndAddToBackStack(new MemberChangeFragment());
            }
        });

        Button partyRButton = (Button)view.findViewById(R.id.partyRButton);
        partyRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragmentAndAddToBackStack(new PartyRegistrationFragment());
            }
        });

        Button competitionRButton = (Button)view.findViewById(R.id.competitionRButton);
        competitionRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragmentAndAddToBackStack(new CompetitionRegistrationFragment());
            }
        });

    }

}
