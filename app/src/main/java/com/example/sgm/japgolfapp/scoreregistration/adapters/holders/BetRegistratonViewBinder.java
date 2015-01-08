package com.example.sgm.japgolfapp.scoreregistration.adapters.holders;

import android.view.View;
import android.widget.TextView;

import com.example.sgm.japgolfapp.R;
import com.example.sgm.japgolfapp.models.Competitor;
import com.example.sgm.japgolfapp.models.CompetitorCompact;


public class BetRegistratonViewBinder {

	public static class CompetitorScoreHolder {
		TextView tvCompetitor;
        TextView tvHandicap;
        TextView ivIncrease;
        TextView ivDecrease;
        TextView tvScore;

        Integer mHoleNumber;
	}

	public static void bindCompetitorScoreHolder(CompetitorScoreHolder holder, View view, Integer holeNumber) {

		holder.tvCompetitor = (TextView) view
				.findViewById(R.id.tvCompetitorName);
        holder.tvHandicap = (TextView) view
                .findViewById(R.id.tvHandicap);
        holder.ivIncrease = (TextView) view
                .findViewById(R.id.ivIncrease);
        holder.ivDecrease = (TextView) view
                .findViewById(R.id.ivDecrease);
        holder.tvScore = (TextView) view
                .findViewById(R.id.tvScore);

        holder.mHoleNumber = holeNumber;
	}

	//
	public static void bindCompetitorScoreInfo(final CompetitorScoreHolder holder,
			final CompetitorCompact competitor) {

			if (competitor != null) {

				if (holder.tvCompetitor != null) {
					holder.tvCompetitor.setText(competitor.getName());
				}
                if (holder.tvScore != null) {
                    holder.tvScore.setText("" + competitor.getScore());
                }

                if (holder.ivIncrease != null){
                    holder.ivIncrease.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Integer i = new Integer(competitor.getScore()).intValue() + 1;
                            competitor.setScore(i.toString());
                            holder.tvScore.setText("" + competitor.getScore());
                        }
                    });
                }

                if (holder.ivDecrease != null){
                    holder.ivDecrease.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Integer i = new Integer(competitor.getScore()).intValue() - 1;
                            competitor.setScore(i.toString());
                            holder.tvScore.setText("" + competitor.getScore());
                        }
                    });
                }

			}
		}
	}

