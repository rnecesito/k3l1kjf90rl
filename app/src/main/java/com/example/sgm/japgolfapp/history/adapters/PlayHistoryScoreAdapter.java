package com.example.sgm.japgolfapp.history.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.sgm.japgolfapp.R;
import com.example.sgm.japgolfapp.history.adapters.holders.PlayHistoryScoreViewBinder;
import com.example.sgm.japgolfapp.models.Competitor;

import java.util.ArrayList;

public class PlayHistoryScoreAdapter extends ArrayAdapter<Competitor> {


	private ArrayList<Competitor> items;
    private Activity activity;

	public PlayHistoryScoreAdapter(Activity activity, int resource, ArrayList<Competitor> items) {
		super(activity, resource, items);
		this.activity = activity;
		this.items = items;
	}

	@Override
	public int getCount() {
		return ((items == null || items.size() == 0) ? 0 : items.size());
	}

	@Override
	public Competitor getItem(int position) {
		if (getCount() > 0 && position > -1 && position < getCount()) {
			return items.get(position);
		}
		return null;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
        PlayHistoryScoreViewBinder.PlayHistoryScoreHolder holder = new PlayHistoryScoreViewBinder.PlayHistoryScoreHolder();

		Competitor history = (Competitor) getItem(position);

		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				view = inflater.inflate(
						R.layout.item_play_history_score_entry, parent,
						false);


            PlayHistoryScoreViewBinder.bindPlayHistoryScoreHolder(holder, view);
			view.setTag(holder);
		} else {
			holder = (PlayHistoryScoreViewBinder.PlayHistoryScoreHolder) view.getTag();
		}

		if (history != null) {
            PlayHistoryScoreViewBinder.bindPlayHistoryScoreInfo(holder, history);
		}

		return view;
	}

}
