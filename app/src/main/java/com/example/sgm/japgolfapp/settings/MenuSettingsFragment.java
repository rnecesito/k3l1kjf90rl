package com.example.sgm.japgolfapp.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.example.sgm.japgolfapp.BaseFragment;
import com.example.sgm.japgolfapp.R;

import butterknife.OnClick;

public class MenuSettingsFragment extends BaseFragment {
    boolean shown = false;
    View view_container;

    @OnClick(R.id.logout_button)
    public void click_logout() {
        logout();
    }

    @OnClick(R.id.menu_button)
    public void showMenu() {
        shown = sidemenu(view_container, shown);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_menu, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view_container = view;
        LayoutInflater inflater = LayoutInflater.from(getContext());
        RelativeLayout rl = (RelativeLayout) view.findViewById(R.id.new_registration_main);
        final View item = inflater.inflate(R.layout.fragment_menu__settings, rl, false);
        rl.addView(item);

        Button memberCButton = (Button)view.findViewById(R.id.createCompetitionB);
        memberCButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragmentAndAddToBackStack(new MemberChangeFragment());
            }
        });

        ImageButton iMemberCB = (ImageButton)view.findViewById(R.id.iCreateCompetitionB);
        iMemberCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragmentAndAddToBackStack(new MemberChangeFragment());
            }
        });


        Button partyRButton = (Button)view.findViewById(R.id.ViewCompetitionB);
        partyRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragmentAndAddToBackStack(new PartyRegistrationFragment());
            }
        });
        ImageButton iPartyRButton = (ImageButton)view.findViewById(R.id.iViewCompetitionB);
        iPartyRButton.setOnClickListener(new View.OnClickListener() {
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
        ImageButton iCompetitionRButton = (ImageButton)view.findViewById(R.id.iCompetitionRButton);
        iCompetitionRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragmentAndAddToBackStack(new CompetitionRegistrationFragment());
            }
        });

        Button betRButton = (Button)view.findViewById(R.id.betRButton);
        betRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragmentAndAddToBackStack(new BetSettingPartyListFragment());
            }
        });

        ImageButton iBetRButton = (ImageButton)view.findViewById(R.id.ibetRButton);
        iBetRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragmentAndAddToBackStack(new BetSettingPartyListFragment());
            }
        });

        Button courseAButton = (Button)view.findViewById(R.id.courseAdministrationB);
        courseAButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragmentAndAddToBackStack(new CourseRegistrationSettingsFragment());
            }
        });
        ImageButton iCourseAButton = (ImageButton)view.findViewById(R.id.iCourseAdministrationB);
        iCourseAButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragmentAndAddToBackStack(new CourseRegistrationSettingsFragment());
            }
        });

        Button viewPartyButton = (Button)view.findViewById(R.id.partyView);
        viewPartyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragmentAndAddToBackStack(new ViewPartyPlayFragment());
            }
        });
        ImageButton iViewPartyButton = (ImageButton)view.findViewById(R.id.iPartyView);
        iViewPartyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragmentAndAddToBackStack(new ViewPartyPlayFragment());
            }
        });


    }

}
