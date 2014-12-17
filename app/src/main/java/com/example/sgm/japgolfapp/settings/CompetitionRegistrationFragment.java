package com.example.sgm.japgolfapp.settings;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.sgm.japgolfapp.BaseFragment;
import com.example.sgm.japgolfapp.Competition.CreateClosedCompetitionFragment;
import com.example.sgm.japgolfapp.Competition.CreateClosedCompetitionGroupFragment;
import com.example.sgm.japgolfapp.Competition.JoinClosedCompetitionFragment;
import com.example.sgm.japgolfapp.Competition.ViewClosedCompetitionsFragment;
import com.example.sgm.japgolfapp.R;
import com.example.sgm.japgolfapp.counting.ScoreCountingFragment;

import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CompetitionRegistrationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CompetitionRegistrationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompetitionRegistrationFragment extends BaseFragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_menu, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        RelativeLayout rl = (RelativeLayout) view.findViewById(R.id.new_registration_main);
        final View item = inflater.inflate(R.layout.fragment_close_competition, rl, false);
        rl.addView(item);

        Button createCompetitionB = (Button)view.findViewById(R.id.createCompetitionB);
        createCompetitionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragmentAndAddToBackStack(new CreateClosedCompetitionFragment());
            }
        });

        Button viewCompetitionB = (Button)view.findViewById(R.id.viewCompetitionB);
        viewCompetitionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragmentAndAddToBackStack(new ViewClosedCompetitionsFragment());
            }
        });

        Button joinCompetitionB = (Button)view.findViewById(R.id.joinCompetitionB);
        joinCompetitionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragmentAndAddToBackStack(new JoinClosedCompetitionFragment());
            }
        });

        Button createGroupB = (Button)view.findViewById(R.id.createGroupB);
        createGroupB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragmentAndAddToBackStack(new CreateClosedCompetitionFragment());
            }
        });

        Button viewGroupsB = (Button)view.findViewById(R.id.viewGroupsB);
        viewGroupsB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragmentAndAddToBackStack(new CreateClosedCompetitionGroupFragment());
            }
        });
    }
}
