package com.example.sgm.japgolfapp.scoreregistration.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.sgm.japgolfapp.R;
import com.example.sgm.japgolfapp.models.CompetitorCompact;
import com.example.sgm.japgolfapp.scoreregistration.adapters.holders.BetRegistratonViewBinder;

import java.util.ArrayList;

public class BetRegistrationAdapter extends ArrayAdapter<CompetitorCompact> {


	private ArrayList<CompetitorCompact> mItems;
    private Integer mHoleNumber;

	public BetRegistrationAdapter(Activity activity, int resource, ArrayList<CompetitorCompact> items, Integer holeNumber) {
		super(activity, resource, items);
		this.mItems = items;
        this.mHoleNumber = holeNumber;
	}

	@Override
	public int getCount() {
		return ((mItems == null || mItems.size() == 0) ? 0 : mItems.size());
	}

	@Override
	public CompetitorCompact getItem(int position) {
		if (getCount() > 0 && position > -1 && position < getCount()) {
			return mItems.get(position);
		}
		return null;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
        BetRegistratonViewBinder.CompetitorScoreHolder holder = new BetRegistratonViewBinder.CompetitorScoreHolder();

        CompetitorCompact competitor = getItem(position);

		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				view = inflater.inflate(
						R.layout.item_score_entry, parent,
						false);


            BetRegistratonViewBinder.bindCompetitorScoreHolder(holder, view, mHoleNumber);
			view.setTag(holder);
		} else {
			holder = (BetRegistratonViewBinder.CompetitorScoreHolder) view.getTag();
		}

		if (competitor != null) {
            BetRegistratonViewBinder.bindCompetitorScoreInfo(holder, competitor);
		}

		return view;
	}

}
