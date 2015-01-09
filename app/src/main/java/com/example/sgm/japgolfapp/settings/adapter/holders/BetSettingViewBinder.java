package com.example.sgm.japgolfapp.settings.adapter.holders;

import android.view.View;
import android.widget.TextView;

import com.example.sgm.japgolfapp.R;
import com.example.sgm.japgolfapp.models.HoleRecord;


public class BetSettingViewBinder {

	public static class HoleRecordHolder {
		TextView tvName;
        TextView tvFirstBet;
        TextView tvSecondBet;
        TextView tvThirdBet;
	}

	public static void bindHoleRecordHolder(HoleRecordHolder holder, View view) {
		holder.tvName = (TextView) view
				.findViewById(R.id.itemHoleName);
        holder.tvFirstBet = (TextView) view
                .findViewById(R.id.itemBetStyleOne);
        holder.tvSecondBet = (TextView) view
                .findViewById(R.id.itemBetStyleTwo);
        holder.tvThirdBet = (TextView) view
                .findViewById(R.id.itemBetStyleThree);

	}

	//
	public static void bindHoleRecordInfo(final HoleRecordHolder holder,
			HoleRecord record) {

			if (record != null) {

				if (holder.tvName != null) {
					holder.tvName.setText(record.getName() + " ホール");
				}

                holder.tvFirstBet.setVisibility(View.INVISIBLE);
                holder.tvSecondBet.setVisibility(View.INVISIBLE);
                holder.tvThirdBet.setVisibility(View.INVISIBLE);

                if(record.getBetSettings().size() > 0) {
                    holder.tvFirstBet.setVisibility(View.VISIBLE);
                    holder.tvFirstBet.setText(record.getBetSettings().get(0).getName());
                }

                if(record.getBetSettings().size() > 1) {
                    holder.tvSecondBet.setVisibility(View.VISIBLE);
                    holder.tvSecondBet.setText(record.getBetSettings().get(1).getName());
                }

                if(record.getBetSettings().size() > 2) {
                    holder.tvThirdBet.setVisibility(View.VISIBLE);
                    holder.tvThirdBet.setText(record.getBetSettings().get(2).getName());
                }




			}
		}
	}

