package com.example.sgm.japgolfapp.scoreregistration.adapters.holders;

import android.view.View;
import android.widget.TextView;

import com.example.sgm.japgolfapp.R;
import com.example.sgm.japgolfapp.models.CompetitorCompact;


public class ScoreRegistratonViewBinder {

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
                if (holder.tvHandicap != null) {
                    holder.tvHandicap.setText("ハンディー " + competitor.getHandicap());
                }

                if (holder.ivIncrease != null){
                    holder.ivIncrease.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Integer i = Integer.parseInt(competitor.getScore()) + 1;
                            competitor.setScore(i.toString());
                            holder.tvScore.setText("" + competitor.getScore());
                        }
                    });
                }

                if (holder.ivDecrease != null){
                    holder.ivDecrease.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Integer i = Integer.parseInt(competitor.getScore());
                            if (i > 0) {
                                i--;
                                competitor.setScore(i.toString());
                            }
                                holder.tvScore.setText("" + competitor.getScore());
                        }
                    });
                }

			}
		}
	}

