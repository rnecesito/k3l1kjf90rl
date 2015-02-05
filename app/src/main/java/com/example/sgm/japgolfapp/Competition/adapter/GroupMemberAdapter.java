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
import com.example.sgm.japgolfapp.models.UserModel;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by CarlAnthony on 12/26/2014.
 */

public class GroupMemberAdapter extends ArrayAdapter<UserModel> {


    private Context context;
    private List<UserModel> listUser;
    private View view;


    public GroupMemberAdapter(Context context, int resource, List<UserModel> objects) {
        super(context, resource, objects);
        this.context = context;
        this.listUser = objects;
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

        String name = listUser.get(position).getFirstName() + " " + listUser.get(position).getLastName() + " " + listUser.get(position).getEmail().substring(0, 1);


        final float scale = getContext().getResources().getDisplayMetrics().density;
        int pixels = (int) (130 * scale + 0.5f);
        holder.txtName.setText(name);
        holder.txtName.setTextColor(Color.BLACK);
        holder.txtName.setLayoutParams(new TableRow.LayoutParams(pixels, TableRow.LayoutParams.MATCH_PARENT));
        holder.txtEmail.setText(listUser.get(position).getEmail());
        holder.txtEmail.setTextColor(Color.BLACK);
        holder.txtEmail.setVisibility(View.GONE);

        Drawable drawable = context.getResources().getDrawable(android.R.drawable.ic_delete);
        holder.txtAction.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        holder.txtAction.setText(" ");
        holder.txtAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(getItem(position));
                notifyDataSetChanged();
            }
        });

        if (position % 2 == 0) {
            holder.tableRow.setBackgroundColor(Color.WHITE);
        } else {
            holder.tableRow.setBackgroundColor(Color.LTGRAY);

        }

        return view;
    }

    class ViewHolder {
        @InjectView(R.id.tv_generic_column_1)
        TextView txtName;

        @InjectView(R.id.tv_generic_column_2)
        TextView txtEmail;

        @InjectView(R.id.tv_generic_column_3)
        TextView txtAction;

        @InjectView(R.id.tr_generic_row)
        TableRow tableRow;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}