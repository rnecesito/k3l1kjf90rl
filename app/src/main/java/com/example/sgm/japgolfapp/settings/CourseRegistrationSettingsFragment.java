package com.example.sgm.japgolfapp.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sgm.japgolfapp.BaseFragment;
import com.example.sgm.japgolfapp.R.id;
import com.example.sgm.japgolfapp.R.layout;
import com.example.sgm.japgolfapp.course.CreateCourseFragment;
import com.example.sgm.japgolfapp.course.ViewCourseFragment;

import butterknife.OnClick;

public class CourseRegistrationSettingsFragment extends BaseFragment {

	@OnClick(id.createCoursesB)
	public void createCourse() {
		showFragmentAndAddToBackStack(new CreateCourseFragment());
	}

    @OnClick(id.imageSettingButton)
    public void createCourse2() {
        showFragmentAndAddToBackStack(new CreateCourseFragment());
    }

	@OnClick(id.viewCoursesB)
	public void viewCourse() {
        showFragmentAndAddToBackStack(new ViewCourseFragment());
	}

    @OnClick(id.imageButton2)
    public void viewCourse2() {
        showFragmentAndAddToBackStack(new ViewCourseFragment());
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(layout.fragment_menu_course, container,
				false);
	}
}
