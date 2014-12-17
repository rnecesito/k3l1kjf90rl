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
import com.example.sgm.japgolfapp.models.Competitor;

import java.util.ArrayList;

public class PlayHistoryFragment extends BaseFragment{

    View view_container;
    private ListView lvPlayHistory;
    ArrayList<PlayHistory> mItems;
    PlayHistoryAdapter mAdapter;

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
        mItems = new ArrayList<PlayHistory>();

        //TEST DATAS
        ArrayList<Competitor> dummy_one = new ArrayList<Competitor>();
        dummy_one.add(new Competitor("Mr. Showa", "01", "01" , null));
        dummy_one.add(new Competitor("Mr. Chow", "02", "02" , null));
        dummy_one.add(new Competitor("Mr. Mikado", "03", "03", null ));

        ArrayList<Competitor> dummy_two = new ArrayList<Competitor>();
        dummy_two.add(new Competitor("Mr. Chow", "02", "02", null ));
        dummy_two.add(new Competitor("Mr. Mikado", "03", "03" , null));

        ArrayList<Competitor> dummy_three = new ArrayList<Competitor>();
        dummy_three.add(new Competitor("Mr. Showa", "01", "01", null ));
        dummy_three.add(new Competitor("Mr. Chow", "02", "02", null ));

        mItems.add(new PlayHistory("MM/DD/2017", "Golf course name", dummy_one));
        mItems.add(new PlayHistory("MM/DD/2016", "Golf course name", dummy_two));
        mItems.add(new PlayHistory("MM/DD/2015", "Golf course name", dummy_three));
        mItems.add(new PlayHistory("MM/DD/2014", "Golf course name", dummy_one));
        mItems.add(new PlayHistory("MM/DD/2013", "Golf course name", dummy_two));
        mItems.add(new PlayHistory("MM/DD/2012", "Golf course name", dummy_three));
        mItems.add(new PlayHistory("MM/DD/2011", "Golf course name", dummy_one));
        mItems.add(new PlayHistory("MM/DD/2010", "Golf course name", dummy_two));
        // -----------

        mAdapter = new PlayHistoryAdapter(getActivity(), 0, mItems);
        lvPlayHistory.setAdapter(mAdapter);
        lvPlayHistory.setFocusable(true);
        lvPlayHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PlayHistoryScoreFragment targetFragment = new PlayHistoryScoreFragment();
                targetFragment.setItems(mItems.get(i).getPlayHistoryScores());
                showFragmentAndAddToBackStack(targetFragment);
            }
        });
    }

}
