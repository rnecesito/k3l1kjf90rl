package com.example.sgm.japgolfapp.settings.adapter.holders;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.sgm.japgolfapp.R;
import com.example.sgm.japgolfapp.models.BetSetting;
import com.example.sgm.japgolfapp.models.HoleRecord;


public class BetChooserViewBinder {

	public static class BetChooserHolder {
		TextView tvName;
        CheckBox cbBetCheck;
	}

	public static void bindBetChooserdHolder(BetChooserHolder holder, View view) {
		holder.tvName = (TextView) view
				.findViewById(R.id.tvBetName);
        holder.cbBetCheck = (CheckBox) view
                .findViewById(R.id.cbBetCheck);
	}

	public static void bindBetChooserInfo(final BetChooserHolder holder,
			BetSetting record) {

			if (record != null) {
				if (holder.tvName != null) {
					holder.tvName.setText(record.getName());
				}
                if(record.isChosen()){
                    holder.cbBetCheck.setChecked(true);
                }else{
                    holder.cbBetCheck.setChecked(false);
                }
			}
		}
	}

