package com.example.sgm.japgolfapp.registration;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.sgm.japgolfapp.BaseFragment;
import com.example.sgm.japgolfapp.R;
import com.example.sgm.japgolfapp.counting.BetCountingFragment;
import com.example.sgm.japgolfapp.counting.CompetitionCountingFragment;
import com.example.sgm.japgolfapp.counting.ScoreCountingFragment;
import com.example.sgm.japgolfapp.history.PlayHistoryFragment;
import com.example.sgm.japgolfapp.scoreregistration.ScoreRegistrationChooseFragment;
import com.example.sgm.japgolfapp.settings.MenuSettingsFragment;

import butterknife.OnClick;


public class MainMenuFragment extends BaseFragment {
    boolean shown = false;
    View view_container;

    @OnClick(R.id.menu_button)
    public void showMenu() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        RelativeLayout rl = (RelativeLayout) view_container.findViewById(R.id.new_registration_main);
        View item = inflater.inflate(R.layout.side_menu, rl, false);
        if (!shown) {
            item.setTag("side_menu_tag");
            rl.addView(item);
            SlideToRight(item);

            Button settingButton = (Button) item.findViewById(R.id.settingButton);
            settingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences prefs = getActivity().getSharedPreferences("com.golf.app", Context.MODE_PRIVATE);
                    String hasLoggedIn = "com.golf.app.back";
                    prefs.edit().putBoolean(hasLoggedIn, true).apply();
                    showFragmentAndAddToBackStack(new MenuSettingsFragment());
                }
            });
            ImageButton iSettingButton = (ImageButton) item.findViewById(R.id.imageSettingButton);
            iSettingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences prefs = getActivity().getSharedPreferences("com.golf.app", Context.MODE_PRIVATE);
                    String hasLoggedIn = "com.golf.app.back";
                    prefs.edit().putBoolean(hasLoggedIn, true).apply();
                    showFragmentAndAddToBackStack(new MenuSettingsFragment());
                }
            });

            Button historyButton = (Button) item.findViewById(R.id.historyButton);
            historyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences prefs = getActivity().getSharedPreferences("com.golf.app", Context.MODE_PRIVATE);
                    String hasLoggedIn = "com.golf.app.back";
                    prefs.edit().putBoolean(hasLoggedIn, true).apply();
                    showFragmentAndAddToBackStack(new PlayHistoryFragment());
                }
            });

            ImageView iHButton = (ImageView) item.findViewById(R.id.imageHistoryButton);
            iHButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences prefs = getActivity().getSharedPreferences("com.golf.app", Context.MODE_PRIVATE);
                    String hasLoggedIn = "com.golf.app.back";
                    prefs.edit().putBoolean(hasLoggedIn, true).apply();
                    showFragmentAndAddToBackStack(new PlayHistoryFragment());
                }
            });

            Button scoreRegistrationButton = (Button) item.findViewById(R.id.scoreRegistrationButton);
            scoreRegistrationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences prefs = getActivity().getSharedPreferences("com.golf.app", Context.MODE_PRIVATE);
                    String hasLoggedIn = "com.golf.app.back";
                    prefs.edit().putBoolean(hasLoggedIn, true).apply();
                    showFragmentAndAddToBackStack(new ScoreRegistrationChooseFragment());
                }
            });
            ImageButton iSButton = (ImageButton) item.findViewById(R.id.imageScoreRegistrationButton);
            iSButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences prefs = getActivity().getSharedPreferences("com.golf.app", Context.MODE_PRIVATE);
                    String hasLoggedIn = "com.golf.app.back";
                    prefs.edit().putBoolean(hasLoggedIn, true).apply();
                    showFragmentAndAddToBackStack(new ScoreRegistrationChooseFragment());
                }
            });

            Button countingButton = (Button) item.findViewById(R.id.countingButton);
            countingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences prefs = getActivity().getSharedPreferences("com.golf.app", Context.MODE_PRIVATE);
                    String hasLoggedIn = "com.golf.app.back";
                    prefs.edit().putBoolean(hasLoggedIn, true).apply();
//                    countingButton(view_container);
                    showFragmentAndAddToBackStack(new MainMenuCountingFragment());
//                showFragmentAndAddToBackStack(new MenuSettingsFragment());
                }
            });


            ImageButton iCButton = (ImageButton) item.findViewById(R.id.imageButton3);
            iCButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences prefs = getActivity().getSharedPreferences("com.golf.app", Context.MODE_PRIVATE);
                    String hasLoggedIn = "com.golf.app.back";
                    prefs.edit().putBoolean(hasLoggedIn, true).apply();
//                    countingButton(view_container);
                    showFragmentAndAddToBackStack(new MainMenuCountingFragment());
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

        Button countingButton = (Button) item.findViewById(R.id.countingButton);
        countingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                LayoutInflater inflater = LayoutInflater.from(getContext());
//                RelativeLayout rl = (RelativeLayout) view_container.findViewById(R.id.new_registration_main);
//                final View item = inflater.inflate(R.layout.counting_sub_menu, rl, false);
//                item.setTag("counting_sub_menu");
//                View tagged = view_container.findViewWithTag("counting_sub_menu");
//                if ((tagged == null)) {
//                    rl.addView(item);
//                }
//                Button scoreCountingButton = (Button) item.findViewById(R.id.scoreCounting);
//                scoreCountingButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        SharedPreferences prefs = getActivity().getSharedPreferences("com.golf.app", Context.MODE_PRIVATE);
//                        String hasLoggedIn = "com.golf.app.fromcounting";
//                        prefs.edit().putBoolean(hasLoggedIn, true).apply();
//                        showFragmentAndAddToBackStack(new ScoreCountingFragment());
//                    }
//                });
//
//                Button betCountingButton = (Button) item.findViewById(R.id.betCounting);
//                betCountingButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        SharedPreferences prefs = getActivity().getSharedPreferences("com.golf.app", Context.MODE_PRIVATE);
//                        String hasLoggedIn = "com.golf.app.fromcounting";
//                        prefs.edit().putBoolean(hasLoggedIn, true).apply();
//                        showFragmentAndAddToBackStack(new BetCountingFragment());
//                    }
//                });
//
//                Button competitionCountingButton = (Button) item.findViewById(R.id.competitionCounting);
//                competitionCountingButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        SharedPreferences prefs = getActivity().getSharedPreferences("com.golf.app", Context.MODE_PRIVATE);
//                        String hasLoggedIn = "com.golf.app.fromcounting";
//                        prefs.edit().putBoolean(hasLoggedIn, true).apply();
//                        showFragmentAndAddToBackStack(new CompetitionCountingFragment());
//                    }
//                });

            }
        });
    }

    @OnClick(R.id.logout_button)
    public void logout() {
        SharedPreferences prefs = getActivity().getSharedPreferences(
                "com.golf.app", Context.MODE_PRIVATE);
        String firstTime = "com.golf.app.firstTimeCheck";
        prefs.edit().clear().apply();
        prefs.edit().putBoolean(firstTime, true).apply();
        clearBackStack();
    }

    private void countingButton(View view_container) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        RelativeLayout rl = (RelativeLayout) view_container.findViewById(R.id.new_registration_main);
        final View item = inflater.inflate(R.layout.counting_sub_menu, rl, false);
        item.setTag("counting_sub_menu");
        View tagged = view_container.findViewWithTag("counting_sub_menu");
        if (tagged == null) {
            rl.addView(item);
            SlideToRight(item);
        }
        Button scoreCountingButton = (Button) item.findViewById(R.id.scoreCounting);
        scoreCountingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragmentAndAddToBackStack(new ScoreCountingFragment());
            }
        });

        Button betCountingButton = (Button) item.findViewById(R.id.betCounting);
        betCountingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragmentAndAddToBackStack(new BetCountingFragment());
            }
        });

        Button competitionCountingButton = (Button) item.findViewById(R.id.competitionCounting);
        competitionCountingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragmentAndAddToBackStack(new CompetitionCountingFragment());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_menu, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        shown = false;
        view_container = view;

        final SharedPreferences prefs = getActivity().getSharedPreferences(
                "com.golf.app", Context.MODE_PRIVATE);
        final String hasLoggedIn = "com.golf.app.fromcounting";
        final Boolean b = prefs.getBoolean(hasLoggedIn, false);
        final SharedPreferences prefs2 = getActivity().getSharedPreferences(
                "com.golf.app", Context.MODE_PRIVATE);
        final String back = "com.golf.app.back";
        final Boolean b2 = prefs2.getBoolean(back, false);
        if (b2) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            RelativeLayout rl = (RelativeLayout) view_container.findViewById(R.id.new_registration_main);
//            inflater.inflate(R.layout.side_menu, rl, false);
            if (!shown) {
                showMenu();
                if (b) {
                    countingButton(rl);
                }
            }
        }

        getView().setFocusableInTouchMode(true);
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    View item = getView().findViewWithTag("counting_sub_menu");
                    if (item != null) {
                        SlideToLeft(item);
                        RelativeLayout rl = (RelativeLayout) getView().findViewById(R.id.new_registration_main);
                        rl.removeView(item);
                        return true;
                    } else {
                        return false;
                    }
                }
                return false;
            }
        });
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

}
