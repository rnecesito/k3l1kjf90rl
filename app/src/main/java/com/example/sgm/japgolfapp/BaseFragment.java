package com.example.sgm.japgolfapp;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.sgm.japgolfapp.registration.IntroScreenFragment;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

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

    protected void clearBackStack() {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        getFragmentManager().beginTransaction()
                .remove(this)
                .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                .replace(R.id.container, new IntroScreenFragment())
                .commit();
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


    public void onEvent(Object object) {
    }


    @Override
    public void onStart() {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        super.onStart();

    }

    @Override
    public void onResume() {

        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        super.onDestroy();

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    protected boolean isStickyAvailabe() {
        return false;
    }
}
