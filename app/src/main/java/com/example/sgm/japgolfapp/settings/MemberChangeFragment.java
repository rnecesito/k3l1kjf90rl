package com.example.sgm.japgolfapp.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sgm.japgolfapp.BaseFragment;
import com.example.sgm.japgolfapp.R;
import com.example.sgm.japgolfapp.counting.ScoreCountingFragment;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by archie on 12/16/2014.
 */
public class MemberChangeFragment extends BaseFragment{

    View view_container;

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

    @InjectView(R.id.courseSpinner)
    EditText email;
    @InjectView(R.id.cName1)
    EditText name;
    @InjectView(R.id.editTextPassword)
    EditText password;

    @InjectView(R.id.emailValidateText)
    TextView invalidEmail;
    @InjectView(R.id.nameValidateText)
    TextView invalidName;
    @InjectView(R.id.passwordValidateText)
    TextView invalidPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_member_change, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view_container = view;
    }

    @OnClick(R.id.saveB)
    public void saveButton(){
        validate();
    }

    public void validate(){
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
            invalidEmail.setVisibility(View.VISIBLE);
        }
        if(name.getText().toString().isEmpty() ){
            invalidName.setVisibility(View.VISIBLE);
        }
        if(password.getText().toString().isEmpty()){
            invalidPassword.setVisibility(View.VISIBLE);
        }

        if(android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
            invalidEmail.setVisibility(View.INVISIBLE);
        }
        if(!name.getText().toString().isEmpty()){
            invalidName.setVisibility(View.INVISIBLE);
        }
        if(!password.getText().toString().isEmpty()){
            invalidPassword.setVisibility(View.INVISIBLE);
        }
    }
}
