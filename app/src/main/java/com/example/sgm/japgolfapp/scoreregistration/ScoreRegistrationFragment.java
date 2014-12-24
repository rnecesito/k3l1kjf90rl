package com.example.sgm.japgolfapp.scoreregistration;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sgm.japgolfapp.BaseFragment;
import com.example.sgm.japgolfapp.R;
import com.example.sgm.japgolfapp.counting.BetCountingFragment;
import com.example.sgm.japgolfapp.counting.CompetitionCountingFragment;
import com.example.sgm.japgolfapp.counting.ScoreCountingFragment;
import com.example.sgm.japgolfapp.history.PlayHistoryFragment;
import com.example.sgm.japgolfapp.models.Competitor;
import com.example.sgm.japgolfapp.models.HoleRecord;
import com.example.sgm.japgolfapp.scoreregistration.adapters.ScoreRegistrationAdapter;
import com.example.sgm.japgolfapp.settings.MenuSettingsFragment;

import java.util.ArrayList;

import butterknife.OnClick;

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

    boolean shown = false;

    @OnClick(R.id.menu_button)
    public void showMenu() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        RelativeLayout rl = (RelativeLayout) view_container.findViewById(R.id.new_registration_main);
        View item = inflater.inflate(R.layout.side_menu, rl, false);
        if (!shown) {
            item.setTag("side_menu_tag");
            rl.addView(item);
            SlideToRight(item);

            Button settingButton = (Button)item.findViewById(R.id.settingButton);
            settingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFragmentAndAddToBackStack(new MenuSettingsFragment());
                }
            });
            ImageButton iSettingButton = (ImageButton)item.findViewById(R.id.imageSettingButton);
            iSettingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFragmentAndAddToBackStack(new MenuSettingsFragment());
                }
            });

            Button historyButton = (Button)item.findViewById(R.id.historyButton);
            historyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFragmentAndAddToBackStack(new PlayHistoryFragment());
                }
            });

            ImageView iHButton = (ImageView)item.findViewById(R.id.imageHistoryButton);
            iHButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFragmentAndAddToBackStack(new PlayHistoryFragment());
                }
            });

            Button scoreRegistrationButton = (Button)item.findViewById(R.id.scoreRegistrationButton);
            scoreRegistrationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFragmentAndAddToBackStack(new ScoreRegistrationFragment());
                }
            });
            ImageButton iSButton = (ImageButton)item.findViewById(R.id.imageScoreRegistrationButton);
            iSButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFragmentAndAddToBackStack(new ScoreRegistrationFragment());
                }
            });

            Button countingButton= (Button)item.findViewById(R.id.countingButton);
            countingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    countingButton(view_container);
//                showFragmentAndAddToBackStack(new MenuSettingsFragment());
                }
            });


            ImageButton iCButton = (ImageButton)item.findViewById(R.id.imageButton3);
            iCButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    countingButton(view_container);
                }
            });
//                showFragmentAndAddToBackStack(new MenuSettingsFragment());
            shown = true;
        } else {
            item = view_container.findViewWithTag("side_menu_tag");
            SlideToLeft(item);
            rl.removeView(item);
            shown = false;
        }

        Button countingButton= (Button)item.findViewById(R.id.countingButton);
        countingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                RelativeLayout rl = (RelativeLayout) view_container.findViewById(R.id.new_registration_main);
                final View item = inflater.inflate(R.layout.counting_sub_menu, rl, false);
                item.setTag("counting_sub_menu");
                View tagged = view_container.findViewWithTag("counting_sub_menu");
                if(tagged == null) {
                    rl.addView(item);
                }
                Button scoreCountingButton = (Button)item.findViewById(R.id.scoreCounting);
                scoreCountingButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showFragmentAndAddToBackStack(new ScoreCountingFragment());
                    }
                });

                Button betCountingButton = (Button)item.findViewById(R.id.betCounting);
                betCountingButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showFragmentAndAddToBackStack(new BetCountingFragment());
                    }
                });

                Button competitionCountingButton = (Button)item.findViewById(R.id.competitionCounting);
                competitionCountingButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showFragmentAndAddToBackStack(new CompetitionCountingFragment());
                    }
                });

            }
        });
    }

    @OnClick(R.id.logout_button)
    public void logout() {
        SharedPreferences prefs = getActivity().getSharedPreferences(
                "com.example.app", Context.MODE_PRIVATE);

        String hasLoggedIn = "com.example.app.hasloggedin";
        prefs.edit().putBoolean(hasLoggedIn, false).apply();

        clearBackStack();
    }

    public void SlideToRight(View view) {
        Animation slide = null;
        slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -5.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f);

        slide.setDuration(400);
        slide.setFillAfter(true);
        slide.setFillEnabled(true);
        view.startAnimation(slide);

        slide.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

        });

    }

    public void SlideToLeft(View view) {
        Animation slide = null;
        slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, -5.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f);

        slide.setDuration(400);
        slide.setFillAfter(true);
        slide.setFillEnabled(true);
        view.startAnimation(slide);

        slide.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

        });

    }

    private void countingButton(View view_container) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        RelativeLayout rl = (RelativeLayout) view_container.findViewById(R.id.new_registration_main);
        final View item = inflater.inflate(R.layout.counting_sub_menu, rl, false);
        item.setTag("counting_sub_menu");
        View tagged = view_container.findViewWithTag("counting_sub_menu");
        if(tagged == null) {
            rl.addView(item);
        }
        Button scoreCountingButton = (Button)item.findViewById(R.id.scoreCounting);
        scoreCountingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragmentAndAddToBackStack(new ScoreCountingFragment());
            }
        });

        Button betCountingButton = (Button)item.findViewById(R.id.betCounting);
        betCountingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragmentAndAddToBackStack(new BetCountingFragment());
            }
        });

        Button competitionCountingButton = (Button)item.findViewById(R.id.competitionCounting);
        competitionCountingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragmentAndAddToBackStack(new CompetitionCountingFragment());
            }
        });
    }

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
