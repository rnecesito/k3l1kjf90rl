package com.example.sgm.japgolfapp;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment {

	protected String TAG_CLASS_NAME;

	protected Context getContext() {
		return getActivity().getApplicationContext();
	}

	protected void showDialog(DialogFragment dialog) {
		dialog.show(getFragmentManager(), null);
	}

	protected Fragment showFragment(Fragment fragment) {
		if (fragment != null) {
			popBackStack();

			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                    .replace(R.id.container, fragment)
					.commit();
		}

		return fragment;
	}

	protected void popBackStack() {
		FragmentManager manager = getFragmentManager();
		if (manager.getBackStackEntryCount() > 0)
			manager.popBackStack();
	}

	protected void showFragmentAndAddToBackStack(Fragment fragment) {
		if (fragment == null)
			return;

		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
                .remove(this)
                .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                .replace(R.id.container, fragment)
                .addToBackStack(fragment.getClass().getSimpleName())
                .commit();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		TAG_CLASS_NAME = getClass().getSimpleName();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ButterKnife.inject(this, view);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		ButterKnife.reset(this);
	}

}
