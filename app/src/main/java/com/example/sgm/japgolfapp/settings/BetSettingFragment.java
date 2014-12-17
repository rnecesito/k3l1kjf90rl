package com.example.sgm.japgolfapp.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.sgm.japgolfapp.BaseFragment;
import com.example.sgm.japgolfapp.R;
import com.example.sgm.japgolfapp.counting.ScoreCountingFragment;

import butterknife.OnClick;

public class BetSettingFragment extends BaseFragment{

    View view_container;
    boolean shown = false;

    @OnClick(R.id.menu_button)
    public void showMenu() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
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
            shown = true;
        } else {
            item = view_container.findViewWithTag("side_menu_tag");
            SlideToLeft(item);
            rl.removeView(item);
            shown = false;
        }

        Button countingButton= (Button)view_container.findViewById(R.id.countingButton);
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
//                showFragmentAndAddToBackStack(new MenuSettingsFragment());
            }
        });
    }

    @OnClick(R.id.hole_next)
    public void nextHole() {
        TextView hole = (TextView) view_container.findViewById(R.id.current_hole);
        int hole_int = Integer.parseInt(hole.getText().toString());
        hole.setText((hole_int + 1) + "");
    }

    @OnClick(R.id.hole_prev)
    public void prevHole() {
        TextView hole = (TextView) view_container.findViewById(R.id.current_hole);
        int hole_int = Integer.parseInt(hole.getText().toString());
        hole.setText((hole_int - 1) + "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bet_registration, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view_container = view;
        TableLayout comp_list = (TableLayout) view.findViewById(R.id.bet_setting_table);
        for (int index = 0; index < comp_list.getChildCount(); index++) {
            TableRow current_row = (TableRow) comp_list.getChildAt(index);
            current_row.getChildAt(1).setOnClickListener(sub);
            current_row.getChildAt(3).setOnClickListener(add);
        }
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

    private View.OnClickListener add = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            TableRow row = (TableRow) v.getParent();
            TextView score = (TextView) row.getChildAt(2);
            int score_val = Integer.parseInt(score.getText().toString());
            score.setText((score_val + 1) + "");
        }
    };

    private View.OnClickListener sub = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            TableRow row = (TableRow) v.getParent();
            TextView score = (TextView) row.getChildAt(2);
            int score_val = Integer.parseInt(score.getText().toString());
            score.setText((score_val - 1) + "");
        }
    };
}
