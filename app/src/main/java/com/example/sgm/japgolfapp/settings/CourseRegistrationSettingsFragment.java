package com.example.sgm.japgolfapp.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.example.sgm.japgolfapp.BaseFragment;
import com.example.sgm.japgolfapp.R;
import com.example.sgm.japgolfapp.R.id;
import com.example.sgm.japgolfapp.course.CreateCourseFragment;
import com.example.sgm.japgolfapp.course.ViewCourseFragment;

import butterknife.OnClick;

public class CourseRegistrationSettingsFragment extends BaseFragment {
    View view_container;
    boolean shown = false;

    @OnClick(R.id.logout_button)
    public void click_logout() {
        logout();
    }

    @OnClick(R.id.menu_button)
    public void showMenu() {
        shown = sidemenu(view_container, shown);
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_main_menu, container,
				false);
	}

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view_container = view;
        LayoutInflater inflater = LayoutInflater.from(getContext());
        RelativeLayout rl = (RelativeLayout) view.findViewById(R.id.new_registration_main);
        final View item = inflater.inflate(R.layout.fragment_menu_course, rl, false);
        rl.addView(item);

        Button b1 = (Button) view.findViewById(id.createCoursesB);
        Button b2 = (Button) view.findViewById(id.viewCoursesB);
        ImageButton ib1 = (ImageButton) view.findViewById(id.imageSettingButton);
        ImageButton ib2 = (ImageButton) view.findViewById(id.imageButton2);

        b1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showFragmentAndAddToBackStack(new CreateCourseFragment());
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showFragmentAndAddToBackStack(new ViewCourseFragment());
            }
        });

        ib1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showFragmentAndAddToBackStack(new CreateCourseFragment());
            }
        });

        ib2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showFragmentAndAddToBackStack(new ViewCourseFragment());
            }
        });


    }
}
