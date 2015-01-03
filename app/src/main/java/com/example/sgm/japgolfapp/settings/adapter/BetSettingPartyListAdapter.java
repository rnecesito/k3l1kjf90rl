package com.example.sgm.japgolfapp.settings.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TableRow;

import com.example.sgm.japgolfapp.R;
import com.example.sgm.japgolfapp.models.Party;
import com.example.sgm.japgolfapp.scoreregistration.adapters.holders.PartyPlayScoringViewBinder;

import java.util.ArrayList;

public class BetSettingPartyListAdapter extends ArrayAdapter<Party> {


	private ArrayList<Party> mItems;

	public BetSettingPartyListAdapter(Activity activity, int resource, ArrayList<Party> items) {
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
						R.layout.generic_3_column_item_layout, parent,
						false);

            TableRow tableRow = (TableRow) view
                    .findViewById(R.id.tr_generic_row);

            if (position % 2 == 0) {
                tableRow.setBackgroundColor(Color.WHITE);
            } else {
                tableRow.setBackgroundColor(Color.LTGRAY);
            }


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
