package com.example.sgm.japgolfapp.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.sgm.japgolfapp.BaseFragment;
import com.example.sgm.japgolfapp.R;
import com.example.sgm.japgolfapp.history.adapters.PlayHistoryScoreAdapter;
import com.example.sgm.japgolfapp.models.Competitor;

import java.util.ArrayList;


public class PlayHistoryScoreFragment extends BaseFragment{

    private View view_container;
    private ArrayList<Competitor> mItems;
    private PlayHistoryScoreAdapter adapter;
    private ListView lvPlayHistoryScores;

    public PlayHistoryScoreFragment(){}

    public void setItems(ArrayList<Competitor> competitor){
        mItems = competitor;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_play_history_score, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view_container = view;

        lvPlayHistoryScores = (ListView) view.findViewById(R.id.lvPartyHistoryScore);
        adapter = new PlayHistoryScoreAdapter(getActivity(), 0, mItems);
        lvPlayHistoryScores.setAdapter(adapter);

    }

}
