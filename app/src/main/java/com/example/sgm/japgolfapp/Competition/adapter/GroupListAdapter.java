package com.example.sgm.japgolfapp.Competition.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.sgm.japgolfapp.R;
import com.example.sgm.japgolfapp.models.CompetitionGroupModel;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;

/**
 * Created by CarlAnthony on 12/26/2014.
 */
public class GroupListAdapter extends ArrayAdapter<CompetitionGroupModel> {

    private Context context;
    private List<CompetitionGroupModel> groupList;
    private View view;

    public GroupListAdapter(Context context, int resource, List<CompetitionGroupModel> objects) {
        super(context, resource, objects);
        this.context = context;
        this.groupList = objects;
    }

    public List<CompetitionGroupModel> getGroupList() {
        return this.groupList;
    }

    public void setGroupList(List<CompetitionGroupModel> list) {
        this.groupList = list;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.generic_3_column_item_layout, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.txtGName.setText(groupList.get(position).getName());
        holder.txtGName.setTextColor(Color.BLACK);
        holder.txtGMemberCount.setText(groupList.get(position).getCompetitors().size() + "");
        holder.txtGMemberCount.setTextColor(Color.BLACK);


        //IF STATEMENT MUST BE HERE
        Drawable drawable = context.getResources().getDrawable(android.R.drawable.ic_delete);
        holder.txtAction.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        holder.txtAction.setText(" ");
        holder.txtAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(groupList.get(position));
            }
        });

        //IF STATEMENT ENDS HERE

        if (position % 2 == 0) {
            holder.tableRow.setBackgroundColor(Color.WHITE);
        } else {
            holder.tableRow.setBackgroundColor(Color.LTGRAY);

        }

        return view;
    }

    class ViewHolder {
        @InjectView(R.id.tv_generic_column_1)
        TextView txtGName;

        @InjectView(R.id.tv_generic_column_2)
        TextView txtGMemberCount;

        @InjectView(R.id.tv_generic_column_3)
        TextView txtAction;

        @InjectView(R.id.tr_generic_row)
        TableRow tableRow;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
