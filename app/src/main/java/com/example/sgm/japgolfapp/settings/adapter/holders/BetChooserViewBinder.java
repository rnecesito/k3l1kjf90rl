package com.example.sgm.japgolfapp.settings.adapter.holders;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sgm.japgolfapp.R;
import com.example.sgm.japgolfapp.models.BetSetting;
import com.example.sgm.japgolfapp.models.HoleRecord;
import com.example.sgm.japgolfapp.settings.BetSettingChooserFragment;


public class BetChooserViewBinder {

    static Context mContext;
	public static class BetChooserHolder {
		TextView tvName;
        CheckBox cbBetCheck;
        public TextView tvHelp;
	}

	public static void bindBetChooserdHolder(Context context, BetChooserHolder holder, View view) {
		holder.tvName = (TextView) view
				.findViewById(R.id.tvBetName);
        holder.cbBetCheck = (CheckBox) view
                .findViewById(R.id.cbBetCheck);
        holder.tvHelp = (TextView)view.findViewById(R.id.tvHelp);

        mContext = context;
	}

	public static void bindBetChooserInfo(final BetChooserHolder holder,
			final BetSetting record) {

			if (record != null) {
				if (holder.tvName != null) {
					holder.tvName.setText(record.getName());
				}
                if(record.isChosen()){
                    holder.cbBetCheck.setChecked(true);
                }else{
                    holder.cbBetCheck.setChecked(false);
                }

//                holder.cbBetCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                            record.setIsChosen(!isChecked);
//                    }
//                });

                holder.cbBetCheck.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                            if (holder.cbBetCheck.isChecked()) {
                                if(BetSettingChooserFragment.STATICsettingsCount < 3) {
                                    record.setIsChosen(true);
                                    BetSettingChooserFragment.STATICsettingsCount++;
                                }else{
                                    holder.cbBetCheck.setChecked(false);
                                    Toast.makeText(mContext, mContext.getResources().getString(R.string.cannot_choose_more_than_three), Toast.LENGTH_LONG).show();
                                }
                            } else {
                                record.setIsChosen(false);
                                BetSettingChooserFragment.STATICsettingsCount--;
                            }

                    }
                });
			}
		}
	}

