package com.example.sgm.japgolfapp.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.sgm.japgolfapp.BaseFragment;
import com.example.sgm.japgolfapp.R;

import butterknife.OnClick;

public class BetSettingFragment extends BaseFragment{

    View view_container;
    boolean shown = false;

    @OnClick(R.id.logout_button)
    public void click_logout() {
        logout();
    }

    @OnClick(R.id.menu_button)
    public void showMenu() {
        shown = sidemenu(view_container, shown);
    }

    @OnClick(R.id.hole_next)
    public void nextHole() {
        TextView hole = (TextView) view_container.findViewById(R.id.current_hole);
        int hole_int = Integer.parseInt(hole.getText().toString());
        hole.setText((hole_int + 1) + "");
    }

    @OnClick(R.id.hole_prev)
    public void prevHole() {
        TextView hole = (TextView) view_container.findViewById(R.id.current_hole);
        int hole_int = Integer.parseInt(hole.getText().toString());
        hole.setText((hole_int - 1) + "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bet_registration, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view_container = view;
        TableLayout comp_list = (TableLayout) view.findViewById(R.id.bet_setting_table);
        for (int index = 0; index < comp_list.getChildCount(); index++) {
            TableRow current_row = (TableRow) comp_list.getChildAt(index);
            current_row.getChildAt(1).setOnClickListener(sub);
            current_row.getChildAt(3).setOnClickListener(add);
        }
    }

    private View.OnClickListener add = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            TableRow row = (TableRow) v.getParent();
            TextView score = (TextView) row.getChildAt(2);
            int score_val = Integer.parseInt(score.getText().toString());
            score.setText((score_val + 1) + "");
        }
    };

    private View.OnClickListener sub = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            TableRow row = (TableRow) v.getParent();
            TextView score = (TextView) row.getChildAt(2);
            int score_val = Integer.parseInt(score.getText().toString());
            score.setText((score_val - 1) + "");
        }
    };
}
