package com.example.sgm.japgolfapp.settings.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.sgm.japgolfapp.R;
import com.example.sgm.japgolfapp.models.BetSetting;
import com.example.sgm.japgolfapp.settings.adapter.holders.BetChooserViewBinder;

import java.util.ArrayList;

public class BetChooserAdapter extends ArrayAdapter<BetSetting> {


	private ArrayList<BetSetting> items;

	public BetChooserAdapter(Activity activity, int resource, ArrayList<BetSetting> items) {
		super(activity, resource, items);
		this.items = items;
	}

	@Override
	public int getCount() {
		return ((items == null || items.size() == 0) ? 0 : items.size());
	}

	@Override
	public BetSetting getItem(int position) {
		if (getCount() > 0 && position > -1 && position < getCount()) {
			return items.get(position);
		}
		return null;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
        BetChooserViewBinder.BetChooserHolder holder = new BetChooserViewBinder.BetChooserHolder();

        BetSetting record = getItem(position);

		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				view = inflater.inflate(
						R.layout.item_bets, parent,
						false);


            BetChooserViewBinder.bindBetChooserdHolder(holder, view);
			view.setTag(holder);
		} else {
			holder = (BetChooserViewBinder.BetChooserHolder) view.getTag();
		}

		if (record != null) {
            BetChooserViewBinder.bindBetChooserInfo(holder, record);
		}

		return view;
	}

}
