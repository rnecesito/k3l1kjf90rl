package com.example.sgm.japgolfapp.settings.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sgm.japgolfapp.R;
import com.example.sgm.japgolfapp.models.BetSetting;
import com.example.sgm.japgolfapp.settings.adapter.holders.BetChooserViewBinder;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class BetChooserAdapter extends ArrayAdapter<BetSetting> {


	private ArrayList<BetSetting> items;
    private Activity a;
	public BetChooserAdapter(Activity activity, int resource, ArrayList<BetSetting> items) {
		super(activity, resource, items);
		this.items = items;
        this.a = activity;
	}

	@Override
	public int getCount() {
		return ((items == null || items.size() == 0) ? 0 : items.size());
	}

	@Override
	public BetSetting getItem(int position) {
		if (getCount() > 0 && position > -1 && position < getCount()) {
			return items.get(position);
		}
		return null;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
        BetChooserViewBinder.BetChooserHolder holder = new BetChooserViewBinder.BetChooserHolder();

        BetSetting record = getItem(position);

		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				view = inflater.inflate(
						R.layout.item_bets, parent,
						false);


            BetChooserViewBinder.bindBetChooserdHolder(holder, view);
            holder.tvHelp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //new CustomDialogClass(a).show();
                    CustomDialogClass dialog = new CustomDialogClass(a);
                    dialog.getWindow().getAttributes().windowAnimations = R.anim.right_left_animation;
                    dialog.show();
                }
            });

			view.setTag(holder);

		} else {
			holder = (BetChooserViewBinder.BetChooserHolder) view.getTag();
		}

		if (record != null) {
            BetChooserViewBinder.bindBetChooserInfo(holder, record);
		}

		return view;
	}


    public class CustomDialogClass extends Dialog implements
            android.view.View.OnClickListener {

        public Context c;
        public Dialog d;
        public Button returnB;

        public CustomDialogClass(Context a) {
            super(a);
            this.c = a;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);

            setContentView(R.layout.dialog_help);

            returnB = (Button)findViewById(R.id.returnB);
            returnB.getBackground().setAlpha(51);
            returnB.setOnClickListener(this);

            TextView content = (TextView)findViewById(R.id.content);
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
