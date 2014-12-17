package com.example.sgm.japgolfapp.scoreregistration;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sgm.japgolfapp.BaseFragment;
import com.example.sgm.japgolfapp.R;
import com.example.sgm.japgolfapp.models.Competitor;
import com.example.sgm.japgolfapp.models.HoleRecord;
import com.example.sgm.japgolfapp.scoreregistration.adapters.ScoreRegistrationAdapter;

import java.util.ArrayList;

public class ScoreRegistrationFragment extends BaseFragment{

    private View view_container;
    private ListView lvCompetitors;
    private ArrayList<HoleRecord> mItems;
    private ScoreRegistrationAdapter mAdapter;
    private Integer mHoleNumber;
    private Integer mCeilSize;

    private ImageView mBtnBack;
    private ImageView mBtnForward;

    private TextView mTvHoleNumber;

    //TEST DATA
    ArrayList<Competitor> dummy_one;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_score_registration, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view_container = view;
        lvCompetitors = (ListView) view.findViewById(R.id.lvCompetitors);
        mItems = new ArrayList<HoleRecord>();
        mHoleNumber = 0;
        mTvHoleNumber = (TextView) view.findViewById(R.id.tvHoleNumber);
        mTvHoleNumber.setText("" + (mHoleNumber + 1));

        mBtnBack = (ImageView) view_container.findViewById(R.id.ivBack);
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mHoleNumber != 0) {
                    mHoleNumber--;
                    mTvHoleNumber.setText("" + (mHoleNumber + 1));
                    mAdapter = new ScoreRegistrationAdapter(getActivity(), 0, mItems.get(mHoleNumber).getCompetitors(), mHoleNumber);
                    lvCompetitors.setAdapter(mAdapter);
                    lvCompetitors.setFocusable(true);
                }
            }
        });

        mBtnForward = (ImageView) view_container.findViewById(R.id.ivForward);
        mBtnForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHoleNumber++;
                if(mHoleNumber >= mItems.size())
                    mItems.add(new HoleRecord("" + mHoleNumber, dummy_one));
                mTvHoleNumber.setText("" + (mHoleNumber + 1));
                mAdapter = new ScoreRegistrationAdapter(getActivity(), 0, mItems.get(mHoleNumber).getCompetitors(), mHoleNumber);
                lvCompetitors.setAdapter(mAdapter);
                lvCompetitors.setFocusable(true);
            }
        });


        //TEST DATAS
        dummy_one = new ArrayList<Competitor>();
        dummy_one.add(new Competitor("Mr. Showa", "0", "0", new ArrayList<Integer>()));
        dummy_one.add(new Competitor("Mr. Chow", "0", "0", new ArrayList<Integer>()));
        dummy_one.add(new Competitor("Mr. Mikado", "0", "0", new ArrayList<Integer>() ));

        mItems.add(new HoleRecord("1", dummy_one));
        // -----------
        mAdapter = new ScoreRegistrationAdapter(getActivity(), 0, mItems.get(mHoleNumber).getCompetitors(), mHoleNumber);
        lvCompetitors.setAdapter(mAdapter);
        lvCompetitors.setFocusable(true);

    }

}
