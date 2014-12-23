package com.example.sgm.japgolfapp.scoreregistration;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.FrameLayout;
import android.widget.ImageView;

import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sgm.japgolfapp.BaseFragment;
import com.example.sgm.japgolfapp.R;
import com.example.sgm.japgolfapp.models.Competitor;
import com.example.sgm.japgolfapp.models.HoleRecord;

import com.example.sgm.japgolfapp.scoreregistration.adapters.ScoreRegistrationAdapter;

import java.util.ArrayList;

public class ScoreRegistrationFragment extends BaseFragment{

    private View view_container;
    private ListView lvCompetitors;
    private ListView lvCompetitors2;
    private ArrayList<HoleRecord> mItems;
    private ArrayList<HoleRecord> mItems2;
    private ScoreRegistrationAdapter mAdapter;
    private ScoreRegistrationAdapter mAdapter2;
    private Integer mHoleNumber;
    private Integer mHoleNumber2;
    private Integer mCeilSize;

    private TextView mBtnBack;
    private TextView mBtnForward;

    private TextView tvPrev;
    private TextView tvNext;
    private TextView saveTv2;

    private TextView mTvHoleNumber;
    private TextView tvBetRegistration;

    private FrameLayout container;

    //TEST DATA
    ArrayList<Competitor> dummy_one;

    ArrayList<Competitor> dummy_two;

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
        tvBetRegistration = (TextView)view.findViewById(R.id.tvBetRegistration);
        container = (FrameLayout)view.findViewById(R.id.bet_registration_container);
        saveTv2 = (TextView)view.findViewById(R.id.saveTv2);

        tvBetRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             container.setVisibility(View.VISIBLE);
            }
        });
        saveTv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                container.setVisibility(View.GONE);
            }
        });


        mItems = new ArrayList<HoleRecord>();
        mHoleNumber = 0;
        mTvHoleNumber = (TextView) view.findViewById(R.id.tvHoleNumber);
        mTvHoleNumber.setText("" + (mHoleNumber + 1));

        mBtnBack = (TextView) view_container.findViewById(R.id.ivBack);
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

        mBtnForward = (TextView) view_container.findViewById(R.id.ivForward);
        mBtnForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mHoleNumber < 17) {
                    mHoleNumber++;
                    if(mHoleNumber >= mItems.size())
                        mItems.add(new HoleRecord("" + mHoleNumber, dummy_one, null));
                    mTvHoleNumber.setText("" + (mHoleNumber + 1));
                    mAdapter = new ScoreRegistrationAdapter(getActivity(), 0, mItems.get(mHoleNumber).getCompetitors(), mHoleNumber);
                    lvCompetitors.setAdapter(mAdapter);
                    lvCompetitors.setFocusable(true);
                }
            }
        });


        //TEST DATAS
        dummy_one = new ArrayList<Competitor>();
        dummy_one.add(new Competitor("Mr. Showa", "0", "0", new ArrayList<Integer>()));
        dummy_one.add(new Competitor("Mr. Chow", "0", "0", new ArrayList<Integer>()));
        dummy_one.add(new Competitor("Mr. Mikado", "0", "0", new ArrayList<Integer>() ));

        mItems.add(new HoleRecord("1", dummy_one, null));
        // -----------
        mAdapter = new ScoreRegistrationAdapter(getActivity(), 0, mItems.get(mHoleNumber).getCompetitors(), mHoleNumber);
        lvCompetitors.setAdapter(mAdapter);
        lvCompetitors.setFocusable(true);


        lvCompetitors2 = (ListView) view.findViewById(R.id.lvCompetitors2);

        mItems2 = new ArrayList<HoleRecord>();
        mHoleNumber2 = 0;


        tvPrev = (TextView) view_container.findViewById(R.id.ivBack2);
        tvPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mHoleNumber2 != 0) {
                    mHoleNumber2--;
                    mAdapter2 = new ScoreRegistrationAdapter(getActivity(), 0, mItems2.get(mHoleNumber2).getCompetitors(), mHoleNumber2);
                    lvCompetitors2.setAdapter(mAdapter2);
                    lvCompetitors2.setFocusable(true);
                }
            }
        });

        tvNext = (TextView) view_container.findViewById(R.id.ivForward2);
        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHoleNumber2++;
                 mItems2.add(new HoleRecord("" + mHoleNumber2, dummy_two, null));
             //   mTvHoleNumber.setText("" + (mHoleNumber + 1));
                mAdapter2 = new ScoreRegistrationAdapter(getActivity(), 0, mItems2.get(mHoleNumber2).getCompetitors(), mHoleNumber2);
                lvCompetitors2.setAdapter(mAdapter2);
                lvCompetitors2.setFocusable(true);
            }
        });


        //TEST DATAS
        dummy_two = new ArrayList<Competitor>();
        dummy_two.add(new Competitor("Mr. Showa", "0", "0", new ArrayList<Integer>()));
        dummy_two.add(new Competitor("Mr. Chow", "0", "0", new ArrayList<Integer>()));
        dummy_two.add(new Competitor("Mr. Mikado", "0", "0", new ArrayList<Integer>() ));

        mItems2.add(new HoleRecord("1", dummy_two, null));
        // -----------
        mAdapter2 = new ScoreRegistrationAdapter(getActivity(), 0, mItems2.get(mHoleNumber2).getCompetitors(), mHoleNumber2);
        lvCompetitors2.setAdapter(mAdapter2);
        lvCompetitors2.setFocusable(true);


    }


}
