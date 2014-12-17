package com.example.sgm.japgolfapp.history.adapters.holders;

import android.view.View;
import android.widget.TextView;

import com.example.sgm.japgolfapp.R;
import com.example.sgm.japgolfapp.models.PlayHistory;


public class PlayHistoryViewBinder {

	public static class PlayHistoryHolder {
		TextView tvDate;
        TextView tvGolfCourseName;

	}

	public static void bindPlayHistoryHolder(PlayHistoryHolder holder, View view) {
		holder.tvDate = (TextView) view
				.findViewById(R.id.itemPlayHistoryDate);
        holder.tvGolfCourseName = (TextView) view
                .findViewById(R.id.itemPlayHistoryCourseName);
	}

	//
	public static void bindPlayHistoryInfo(final PlayHistoryHolder holder,
			PlayHistory history) {

			if (history != null) {

				if (holder.tvDate != null) {
					holder.tvDate.setText(history.getDate());
				}
                if (holder.tvGolfCourseName != null) {
                    holder.tvGolfCourseName.setText(history.getGolfCourseName());
                }

			}
		}
	}

