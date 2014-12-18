package com.example.sgm.japgolfapp.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sgm.japgolfapp.BaseFragment;
import com.example.sgm.japgolfapp.R;
import com.example.sgm.japgolfapp.history.PlayHistoryScoreFragment;
import com.example.sgm.japgolfapp.history.adapters.PlayHistoryAdapter;
import com.example.sgm.japgolfapp.models.BetSetting;
import com.example.sgm.japgolfapp.models.Competitor;
import com.example.sgm.japgolfapp.models.HoleRecord;
import com.example.sgm.japgolfapp.models.PlayHistory;
import com.example.sgm.japgolfapp.settings.adapter.BetSettingAdapter;

import java.util.ArrayList;

public class NewBetSettingFragment extends BaseFragment{

    View view_container;
    private ListView lvHoldeRecords;
    public static ArrayList<HoleRecord> mItems;
    BetSettingAdapter mAdapter;
    TextView tvSet;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_bet_setting, container, false);


    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view_container = view;
        lvHoldeRecords = (ListView) view.findViewById(R.id.lvBets);
        mItems = new ArrayList<HoleRecord>();
        tvSet = (TextView) view_container.findViewById(R.id.tvSaveReturn);
        tvSet.setText("Save");

        mItems.add(new HoleRecord("1", null, new ArrayList<BetSetting>()));
        mItems.add(new HoleRecord("2", null, new ArrayList<BetSetting>()));
        mItems.add(new HoleRecord("3", null, new ArrayList<BetSetting>()));
        mItems.add(new HoleRecord("4", null, new ArrayList<BetSetting>()));

        // -----------
        mAdapter = new BetSettingAdapter(getActivity(), 0, mItems);
        lvHoldeRecords.setAdapter(mAdapter);
        lvHoldeRecords.setFocusable(true);
        lvHoldeRecords.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                BetSettingChooserFragment targetFragment = new BetSettingChooserFragment();
                targetFragment.setItems(mItems.get(i).getBetSettings());
                targetFragment.setItemNumber(i);
                showFragmentAndAddToBackStack(targetFragment);
            }
        });
    }

}
