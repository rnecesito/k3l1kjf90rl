package com.example.sgm.japgolfapp.Competition.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.sgm.japgolfapp.R;
import com.example.sgm.japgolfapp.models.CompetitionGroupModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;

/**
 * Created by CarlAnthony on 12/26/2014.
 */
public class GroupListAdapter extends ArrayAdapter<CompetitionGroupModel> implements Filterable {

    private Context context;
    private List<CompetitionGroupModel> groupList;
    private View view;
    private Filter planetFilter;

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
    public int getCount() {
        return ((groupList == null || groupList.size() == 0) ? 0 : groupList.size());
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

        try {
            holder.txtGName.setText(groupList.get(position).getName());
            holder.txtGName.setTextColor(Color.BLACK);
            holder.txtGMemberCount.setText(groupList.get(position).getCompetitors().size() + "");
            holder.txtGMemberCount.setTextColor(Color.BLACK);


            //IF STATEMENT MUST BE HERE
            Drawable drawable = context.getResources().getDrawable(android.R.drawable.ic_menu_edit);
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
        } catch (Exception e) {

        }


        return view;
    }

    @Override
    public Filter getFilter() {
        if (planetFilter == null) {
            planetFilter = new PlanetFilter();
        }

        return planetFilter;
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

    private class PlanetFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            // We implement here the filter logic
            if (constraint == null || constraint.length() == 0) {
                // No filter implemented we return all the list
                results.values = groupList;
                results.count = groupList.size();
            } else {
                // We perform filtering operation
                List<CompetitionGroupModel> nPlanetList = new ArrayList<CompetitionGroupModel>();

                for (CompetitionGroupModel p : groupList) {
                    if (p.getName().toUpperCase().startsWith(constraint.toString().toUpperCase()))
                        nPlanetList.add(p);
                }

                results.values = nPlanetList;
                results.count = nPlanetList.size();

            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            // Now we have to inform the adapter about the new list filtered
            if (results.count == 0)
                notifyDataSetInvalidated();
            else {
                groupList = (List<CompetitionGroupModel>) results.values;
                notifyDataSetChanged();
            }
        }

    }

}
