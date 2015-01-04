package com.example.sgm.japgolfapp.scoreregistration.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.sgm.japgolfapp.R;
import com.example.sgm.japgolfapp.models.Party;
import com.example.sgm.japgolfapp.scoreregistration.adapters.holders.GroupListViewBinder;

import java.util.ArrayList;

public class GroupListAdapter extends ArrayAdapter<Party> {


	private ArrayList<Party> mItems;

	public GroupListAdapter(Activity activity, int resource, ArrayList<Party> items) {
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
        PartyPlayHolder holder = new PartyPlayHolder();

        Party party = (Party) getItem(position);

		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				view = inflater.inflate(
						R.layout.generic_3_column_item_layout, parent,
						false);
            bindPartyPlayHolder(holder, view);

			view.setTag(holder);
		} else {
			holder = (PartyPlayHolder) view.getTag();
		}



		if (party != null) {
            bindPartyPlayInfo(holder, party);
            if (position % 2 == 0) {
                holder.tableRow.setBackgroundColor(Color.WHITE);
            } else {
                holder.tableRow.setBackgroundColor(Color.LTGRAY);
            }
		}

		return view;
	}

    public class PartyPlayHolder {
        TextView tvName;
        TextView tvDate;
        TextView tvCourse;
        TableRow tableRow;
    }


    public void bindPartyPlayHolder(PartyPlayHolder holder, View view) {


        holder.tvName = (TextView) view
                .findViewById(R.id.tv_generic_column_1);
        holder.tvDate = (TextView) view
                .findViewById(R.id.tv_generic_column_2);
        holder.tvCourse = (TextView) view
                .findViewById(R.id.tv_generic_column_3);
        holder.tableRow = (TableRow) view
                .findViewById(R.id.tr_generic_row);

    }

    //
    public void bindPartyPlayInfo(final PartyPlayHolder holder,
                                  final Party party) {

        if (party != null) {

            if (holder.tvName != null) {
                holder.tvName.setText(party.getName());
                holder.tvName.setTextColor(Color.BLACK);
            }

            if (holder.tvDate != null) {
                holder.tvDate.setText(party.getDate());
                holder.tvDate.setTextColor(Color.BLACK);
            }

            if (holder.tvCourse != null) {
                holder.tvCourse.setText(party.getCourse());
                holder.tvCourse.setTextColor(Color.BLACK);
            }


        }
    }
}
