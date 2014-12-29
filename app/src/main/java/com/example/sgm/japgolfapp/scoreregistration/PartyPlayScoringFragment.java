package com.example.sgm.japgolfapp.scoreregistration;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sgm.japgolfapp.BaseFragment;
import com.example.sgm.japgolfapp.R;
import com.example.sgm.japgolfapp.counting.BetCountingFragment;
import com.example.sgm.japgolfapp.counting.CompetitionCountingFragment;
import com.example.sgm.japgolfapp.counting.ScoreCountingFragment;
import com.example.sgm.japgolfapp.history.PlayHistoryFragment;
import com.example.sgm.japgolfapp.models.Competitor;
import com.example.sgm.japgolfapp.models.HoleRecord;
import com.example.sgm.japgolfapp.models.Party;
import com.example.sgm.japgolfapp.scoreregistration.adapters.PartyPlayScoringAdapter;
import com.example.sgm.japgolfapp.scoreregistration.adapters.ScoreRegistrationAdapter;
import com.example.sgm.japgolfapp.settings.MenuSettingsFragment;

import java.util.ArrayList;
import java.util.ListIterator;

import butterknife.OnClick;

public class PartyPlayScoringFragment extends BaseFragment{

    private PartyPlayScoringAdapter mAdapter;
    private ListView lvPartyPlayGroups;

    @OnClick(R.id.logout_button)
    public void logout() {
        SharedPreferences prefs = getActivity().getSharedPreferences(
                "com.example.app", Context.MODE_PRIVATE);

        String hasLoggedIn = "com.example.app.hasloggedin";
        prefs.edit().putBoolean(hasLoggedIn, false).apply();

        clearBackStack();
    }

    //TEST DATA
    ArrayList<Party> dummy_one;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_party_play_scoring_list, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lvPartyPlayGroups = (ListView) view.findViewById(R.id.lvPartyPlayGroups);
        //TEST DATAS
        dummy_one = new ArrayList<Party>();
        dummy_one.add(new Party("1", "My Party", "10/21/91", "Golf 4"));
        dummy_one.add(new Party("2", "Go Party", "11/24/91", "Golf 3"));
        dummy_one.add(new Party("1", "My Party", "11/03/91", "Golf 4"));
        dummy_one.add(new Party("2", "Go Party", "11/21/91", "Golf 3"));
        dummy_one.add(new Party("3", "Shin Go" , "12/11/91", "Golf 5"));
        dummy_one.add(new Party("3", "Shin Go" , "12/26/91", "Golf 5"));

        mAdapter = new PartyPlayScoringAdapter(getActivity(),0,dummy_one);
        lvPartyPlayGroups.setAdapter(mAdapter);
        lvPartyPlayGroups.setFocusable(true);
        lvPartyPlayGroups.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showFragmentAndAddToBackStack(new ScoreRegistrationFragment());
            }
        });


    }


}
