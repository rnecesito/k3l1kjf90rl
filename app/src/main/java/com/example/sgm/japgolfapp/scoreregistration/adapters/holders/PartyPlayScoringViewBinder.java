package com.example.sgm.japgolfapp.scoreregistration.adapters.holders;

import android.view.View;
import android.widget.TextView;

import com.example.sgm.japgolfapp.R;
import com.example.sgm.japgolfapp.models.Party;


public class PartyPlayScoringViewBinder {

	public static class PartyPlayHolder {
		TextView tvName;
        TextView tvDate;
        TextView tvCourse;

	}

	public static void bindPartyPlayHolder(PartyPlayHolder holder, View view) {

		holder.tvName = (TextView) view
				.findViewById(R.id.tvFirstItem);
        holder.tvDate = (TextView) view
                .findViewById(R.id.tvSecondItem);
        holder.tvCourse = (TextView) view
                .findViewById(R.id.tvThirdItem);

	}

	//
	public static void bindPartyPlayInfo(final PartyPlayHolder holder,
			final Party party) {

			if (party != null) {

				if (holder.tvName != null) {
					holder.tvName.setText(party.getName());
				}

                if (holder.tvDate != null) {
                    holder.tvDate.setText(party.getDate());
                }

                if (holder.tvCourse != null) {
                    holder.tvCourse.setText(party.getCourse());
                }


			}
		}
	}

