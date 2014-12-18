package com.example.sgm.japgolfapp.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sgm.japgolfapp.BaseFragment;
import com.example.sgm.japgolfapp.R;
import com.example.sgm.japgolfapp.history.adapters.PlayHistoryScoreAdapter;
import com.example.sgm.japgolfapp.models.BetSetting;
import com.example.sgm.japgolfapp.models.Competitor;
import com.example.sgm.japgolfapp.settings.adapter.BetChooserAdapter;

import java.util.ArrayList;


public class BetSettingChooserFragment extends BaseFragment{

    private View view_container;
    private ArrayList<BetSetting> mItems;
    private BetChooserAdapter adapter;
    private ListView lvBetSettings;
    private TextView tvReturn;
    private Integer mItemNumber;

    private final Integer MAXSETTINGS = 3;
    private Integer settingsCount = 0;
    public BetSettingChooserFragment(){}

    public void setItems(ArrayList<BetSetting> competitor){
        mItems = competitor;
    }

    public void setItemNumber(Integer i){
        this.mItemNumber = i;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_bet_setting, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view_container = view;

        tvReturn = (TextView) view_container.findViewById(R.id.tvSaveReturn);
        tvReturn.setText("Return");
        tvReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SAVE ALL CHOOSEN BET SETTINGS TO HOLE RECORD
                getActivity().onBackPressed();
            }
        });

        //TEST DATAS -----------------------
        mItems.add(new BetSetting("aaa", "aaa help",false));
        mItems.add(new BetSetting("bbb", "bbb help",false));
        mItems.add(new BetSetting("ccc", "ccc help",false));
        mItems.add(new BetSetting("ddd", "ddd help",false));
        mItems.add(new BetSetting("eee", "eee help",false));
        mItems.add(new BetSetting("fff", "fff help",false));
        mItems.add(new BetSetting("ggg", "ggg help",false));
        mItems.add(new BetSetting("hhh", "hhh help",false));
        // ---------------------------------


        lvBetSettings = (ListView) view.findViewById(R.id.lvBets);
        adapter = new BetChooserAdapter(getActivity(), 0, mItems);
        lvBetSettings.setAdapter(adapter);
        lvBetSettings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (lvBetSettings.getChildAt(position) != null) {
                        CheckBox cb = ((CheckBox) lvBetSettings.getChildAt(position).findViewById(R.id.cbBetCheck));
                        if (cb.isChecked()) {
                            mItems.get(position).setIsChosen(false);
                            adapter.notifyDataSetChanged();
                            settingsCount--;
                        } else { //do something else}
                            if(settingsCount < MAXSETTINGS) {
                                NewBetSettingFragment.mItems.get(mItemNumber).getBetSettings().add(mItems.get(position));
                                mItems.get(position).setIsChosen(true);
                                adapter.notifyDataSetChanged();
                                settingsCount++;
                            }else{
                                Toast.makeText(getActivity(), "Cannot Add More than 3 Settings", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

            }
        });
    }

}
