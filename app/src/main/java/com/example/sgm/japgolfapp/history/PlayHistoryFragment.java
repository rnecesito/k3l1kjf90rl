package com.example.sgm.japgolfapp.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.sgm.japgolfapp.BaseFragment;
import com.example.sgm.japgolfapp.R;
import com.example.sgm.japgolfapp.history.adapters.PlayHistoryAdapter;
import com.example.sgm.japgolfapp.models.PlayHistory;
import com.example.sgm.japgolfapp.models.PlayHistoryScore;

import java.util.ArrayList;

public class PlayHistoryFragment extends BaseFragment{

    View view_container;
    private ListView lvPlayHistory;
    ArrayList<PlayHistory> items;
    PlayHistoryAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_play_history, container, false);


    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view_container = view;
        lvPlayHistory = (ListView) view.findViewById(R.id.lvPlayHistory);
        items = new ArrayList<PlayHistory>();

        //TEST DATAS
        items.add(new PlayHistory("MM/DD/2017", "Golf course name", new ArrayList<PlayHistoryScore>()));
        items.add(new PlayHistory("MM/DD/2016", "Golf course name", new ArrayList<PlayHistoryScore>()));
        items.add(new PlayHistory("MM/DD/2015", "Golf course name", new ArrayList<PlayHistoryScore>()));
        items.add(new PlayHistory("MM/DD/2014", "Golf course name", new ArrayList<PlayHistoryScore>()));
        items.add(new PlayHistory("MM/DD/2013", "Golf course name", new ArrayList<PlayHistoryScore>()));
        items.add(new PlayHistory("MM/DD/2012", "Golf course name", new ArrayList<PlayHistoryScore>()));
        items.add(new PlayHistory("MM/DD/2011", "Golf course name", new ArrayList<PlayHistoryScore>()));
        items.add(new PlayHistory("MM/DD/2010", "Golf course name", new ArrayList<PlayHistoryScore>()));

        // -----------

        adapter = new PlayHistoryAdapter(getActivity(), 0, items);
        lvPlayHistory.setAdapter(adapter);
        lvPlayHistory.setFocusable(true);
        lvPlayHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showFragmentAndAddToBackStack(new PlayHistoryScoreFragment());
            }
        });
    }

}
