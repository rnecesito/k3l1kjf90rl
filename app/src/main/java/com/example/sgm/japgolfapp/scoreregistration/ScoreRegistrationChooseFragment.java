package com.example.sgm.japgolfapp.scoreregistration;

import android.app.ProgressDialog;
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

public class ScoreRegistrationChooseFragment extends BaseFragment {


    View view_container;
    boolean shown = false;
    private ProgressDialog pdialog;
    private boolean success = false;
    String retVal;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_menu, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        RelativeLayout rl = (RelativeLayout) view.findViewById(R.id.new_registration_main);
        final View item = inflater.inflate(R.layout.fragment_menu_score_registration, rl, false);
        rl.addView(item);

        view_container = view;
        //Party Scoring
        Button btnPartyScoring = (Button)view.findViewById(R.id.btnPartyScoring);
        btnPartyScoring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragmentAndAddToBackStack(new PartyPlayScoringFragment());
            }
        });

        ImageButton btnPartyScoringArrow = (ImageButton)view.findViewById(R.id.btnPartyScoringArrow);
        btnPartyScoringArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragmentAndAddToBackStack(new PartyPlayScoringFragment());
            }
        });


        //Competition Scoring
        Button btnCompetitionScoring = (Button)view.findViewById(R.id.btnCompetitionScoring);
        btnCompetitionScoring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragmentAndAddToBackStack(new ClosedCompetitionsFragment());
            }
        });
        ImageButton btnCompetitionScoringArrow = (ImageButton)view.findViewById(R.id.btnCompetitionScoringArrow);
        btnCompetitionScoringArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragmentAndAddToBackStack(new ClosedCompetitionsFragment());
            }
        });
    }



    @OnClick(R.id.logout_button)
    public void click_logout() {
        logout();
    }

    @OnClick(R.id.menu_button)
    public void showMenu() {
        shown = sidemenu(view_container, shown);
    }

}
