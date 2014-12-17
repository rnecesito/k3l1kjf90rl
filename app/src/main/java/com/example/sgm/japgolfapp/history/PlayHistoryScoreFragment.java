package com.example.sgm.japgolfapp.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sgm.japgolfapp.BaseFragment;
import com.example.sgm.japgolfapp.R;

/**
 * A simple {@link android.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.example.sgm.japgolfapp.history.PlayHistoryScoreFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link com.example.sgm.japgolfapp.history.PlayHistoryScoreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlayHistoryScoreFragment extends BaseFragment{

    View view_container;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_play_history_score, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view_container = view;
    }

}
