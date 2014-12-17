package com.example.sgm.japgolfapp.scoreregistration.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.sgm.japgolfapp.R;
import com.example.sgm.japgolfapp.models.Competitor;
import com.example.sgm.japgolfapp.scoreregistration.adapters.holders.ScoreRegistratonViewBinder;

import java.util.ArrayList;

public class ScoreRegistrationAdapter extends ArrayAdapter<Competitor> {


	private ArrayList<Competitor> mItems;
    private Integer mHoleNumber;

	public ScoreRegistrationAdapter(Activity activity, int resource, ArrayList<Competitor> items, Integer holeNumber) {
		super(activity, resource, items);
		this.mItems = items;
        mHoleNumber = holeNumber;
	}

	@Override
	public int getCount() {
		return ((mItems == null || mItems.size() == 0) ? 0 : mItems.size());
	}

	@Override
	public Competitor getItem(int position) {
		if (getCount() > 0 && position > -1 && position < getCount()) {
			return mItems.get(position);
		}
		return null;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
        ScoreRegistratonViewBinder.CompetitorScoreHolder holder = new ScoreRegistratonViewBinder.CompetitorScoreHolder();

        Competitor competitor = (Competitor) getItem(position);

		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				view = inflater.inflate(
						R.layout.item_score_entry, parent,
						false);


            ScoreRegistratonViewBinder.bindCompetitorScoreHolder(holder, view, mHoleNumber);
			view.setTag(holder);
		} else {
			holder = (ScoreRegistratonViewBinder.CompetitorScoreHolder) view.getTag();
		}

		if (competitor != null) {
            ScoreRegistratonViewBinder.bindCompetitorScoreInfo(holder, competitor);
		}

		return view;
	}

}
