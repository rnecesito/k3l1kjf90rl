package com.example.sgm.japgolfapp.history.adapters.holders;

import android.view.View;
import android.widget.TextView;

import com.example.sgm.japgolfapp.R;
import com.example.sgm.japgolfapp.models.Competitor;


public class PlayHistoryScoreViewBinder {

	public static class PlayHistoryScoreHolder {
		TextView tvName;
        TextView tvGross;
        TextView tvNet;
	}

	public static void bindPlayHistoryScoreHolder(PlayHistoryScoreHolder holder, View view) {
		holder.tvName = (TextView) view
				.findViewById(R.id.itemPlayHistoryScoreName);
        holder.tvGross = (TextView) view
                .findViewById(R.id.itemPlayHistoryScoreGross);
        holder.tvNet = (TextView) view
                .findViewById(R.id.itemPlayHistoryScoreNet);
	}
	//
	public static void bindPlayHistoryScoreInfo(final PlayHistoryScoreHolder holder,
			Competitor historyScore) {

			if (historyScore != null) {
				if (holder.tvName != null) {
					holder.tvName.setText(historyScore.getRank() + "位 " + historyScore.getName());
				}
                if (holder.tvGross != null) {
                    holder.tvGross.setText(historyScore.getGross());
                }
                if (holder.tvNet != null) {
                    holder.tvNet.setText(historyScore.getNet());
                }

			}
		}
	}

