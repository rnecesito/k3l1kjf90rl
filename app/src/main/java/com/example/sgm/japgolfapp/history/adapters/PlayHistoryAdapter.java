package com.example.sgm.japgolfapp.history.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.sgm.japgolfapp.R;
import com.example.sgm.japgolfapp.history.adapters.holders.PlayHistoryViewBinder;
import com.example.sgm.japgolfapp.models.PlayHistory;

import java.util.ArrayList;

public class PlayHistoryAdapter extends ArrayAdapter<PlayHistory> {


	private ArrayList<PlayHistory> items;
    private Activity activity;

	public PlayHistoryAdapter(Activity activity, int resource, ArrayList<PlayHistory> items) {
		super(activity, resource, items);
		this.activity = activity;
		this.items = items;
	}

	@Override
	public int getCount() {
		return ((items == null || items.size() == 0) ? 0 : items.size());
	}

	@Override
	public PlayHistory getItem(int position) {
		if (getCount() > 0 && position > -1 && position < getCount()) {
			return items.get(position);
		}
		return null;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
        PlayHistoryViewBinder.PlayHistoryHolder holder = new PlayHistoryViewBinder.PlayHistoryHolder();

		PlayHistory history = (PlayHistory) getItem(position);

		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				view = inflater.inflate(
						R.layout.item_play_history_entry, parent,
						false);


            PlayHistoryViewBinder.bindPlayHistoryHolder(holder, view);
			view.setTag(holder);
		} else {
			holder = (PlayHistoryViewBinder.PlayHistoryHolder) view.getTag();
		}

		if (history != null) {
            PlayHistoryViewBinder.bindPlayHistoryInfo(holder, history);
		}

		return view;
	}

}
