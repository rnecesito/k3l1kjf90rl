package com.example.sgm.japgolfapp.settings.adapter.holders;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sgm.japgolfapp.R;
import com.example.sgm.japgolfapp.models.BetSetting;
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

                holder.tvHelp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //new CustomDialogClass(a).show();
                        CustomDialogClass dialog = new CustomDialogClass(mContext, record.getHelp());
//                        dialog.getWindow().getAttributes().windowAnimations = R.anim.right_left_animation;
                        dialog.show();
                    }
                });


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

    public static class CustomDialogClass extends Dialog implements
            android.view.View.OnClickListener {

        public Context c;
        public Dialog d;
        public Button returnB;
        public String desc;

        public CustomDialogClass(Context a, String s) {
            super(a);
            this.c = a;
            this.desc = s;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);

            setContentView(R.layout.dialog_help);

            returnB = (Button)findViewById(R.id.returnB);
//            returnB.getBackground().setAlpha(51);
            returnB.setOnClickListener(this);

            TextView content = (TextView)findViewById(R.id.content);
            content.setText(desc);
            content.setMovementMethod(new ScrollingMovementMethod());

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.returnB:
                    dismiss();
                    break;
                default:
                    break;
            }
            dismiss();
        }
    }


}

