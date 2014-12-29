package com.example.sgm.japgolfapp.scoreregistration.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.sgm.japgolfapp.R;
import com.example.sgm.japgolfapp.models.Competitor;
import com.example.sgm.japgolfapp.models.Party;
import com.example.sgm.japgolfapp.scoreregistration.adapters.holders.PartyPlayScoringViewBinder;
import com.example.sgm.japgolfapp.scoreregistration.adapters.holders.ScoreRegistratonViewBinder;

import java.util.ArrayList;

public class PartyPlayScoringAdapter extends ArrayAdapter<Party> {


	private ArrayList<Party> mItems;

	public PartyPlayScoringAdapter(Activity activity, int resource, ArrayList<Party> items) {
		super(activity, resource, items);
		this.mItems = items;
	}

	@Override
	public int getCount() {
		return ((mItems == null || mItems.size() == 0) ? 0 : mItems.size());
	}

	@Override
	public Party getItem(int position) {
		if (getCount() > 0 && position > -1 && position < getCount()) {
			return mItems.get(position);
		}
		return null;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
        PartyPlayScoringViewBinder.PartyPlayHolder holder = new PartyPlayScoringViewBinder.PartyPlayHolder();

        Party party = (Party) getItem(position);

		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				view = inflater.inflate(
						R.layout.item_table_list_row, parent,
						false);


            PartyPlayScoringViewBinder.bindPartyPlayHolder(holder, view);
			view.setTag(holder);
		} else {
			holder = (PartyPlayScoringViewBinder.PartyPlayHolder) view.getTag();
		}

		if (party != null) {
            PartyPlayScoringViewBinder.bindPartyPlayInfo(holder, party);
		}

		return view;
	}

}
