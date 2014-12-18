package com.example.sgm.japgolfapp.settings.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.sgm.japgolfapp.R;
import com.example.sgm.japgolfapp.history.adapters.holders.PlayHistoryViewBinder;
import com.example.sgm.japgolfapp.models.HoleRecord;
import com.example.sgm.japgolfapp.models.PlayHistory;
import com.example.sgm.japgolfapp.settings.adapter.holders.BetSettingViewBinder;

import java.util.ArrayList;

public class BetSettingAdapter extends ArrayAdapter<HoleRecord> {


	private ArrayList<HoleRecord> items;

	public BetSettingAdapter(Activity activity, int resource, ArrayList<HoleRecord> items) {
		super(activity, resource, items);
		this.items = items;
	}

	@Override
	public int getCount() {
		return ((items == null || items.size() == 0) ? 0 : items.size());
	}

	@Override
	public HoleRecord getItem(int position) {
		if (getCount() > 0 && position > -1 && position < getCount()) {
			return items.get(position);
		}
		return null;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
        BetSettingViewBinder.HoleRecordHolder holder = new BetSettingViewBinder.HoleRecordHolder();

        HoleRecord record = (HoleRecord) getItem(position);

		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				view = inflater.inflate(
						R.layout.item_hole_bets, parent,
						false);


            BetSettingViewBinder.bindHoleRecordHolder(holder, view);
			view.setTag(holder);
		} else {
			holder = (BetSettingViewBinder.HoleRecordHolder) view.getTag();
		}

		if (record != null) {
            BetSettingViewBinder.bindHoleRecordInfo(holder, record);
		}

		return view;
	}

}
