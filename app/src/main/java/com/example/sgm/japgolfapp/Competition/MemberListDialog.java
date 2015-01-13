package com.example.sgm.japgolfapp.Competition;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.example.sgm.japgolfapp.GolfApp;
import com.example.sgm.japgolfapp.R;
import com.example.sgm.japgolfapp.api.Api;
import com.example.sgm.japgolfapp.models.UserModel;
import com.example.sgm.japgolfapp.util.NetworkUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;


public class MemberListDialog extends Dialog {

    private Context context;
    @InjectView(R.id.sp_competition_create_group_add_member)
    Spinner spinner;

    @InjectView(R.id.b_competition_create_group_add_member)
    Button btnAddMember;

    @InjectView(R.id.ll_competition_create_group_add_member)
    LinearLayout containerArea;

    @InjectView(R.id.pb_competition_create_group_add_member)
    ProgressBar loader;

    private final String loadAllUserTag = "getAllUser";

    private List<UserModel> tempUser = new ArrayList<UserModel>();
    private List<UserModel> listUser = new ArrayList<UserModel>();
    private SpinnerAdapter adapter;


    public MemberListDialog(Context context, List<UserModel> tempUser) {
        super(context, android.R.style.Theme_Holo_Dialog_MinWidth);
        this.context = context;
        this.tempUser.addAll(tempUser);

        requestWindowFeature(Window.FEATURE_NO_TITLE);


        setContentView(R.layout.competition_create_group_addmember_dialog);

        ButterKnife.inject(this);
        populate();
    }

    private void populate() {

        adapter = new SpinnerAdapter(context, android.R.layout.simple_spinner_dropdown_item, listUser);
        spinner.setAdapter(adapter);
    }

    @Override
    public void show() {
        super.show();
        LoadMembers();
    }

    @OnClick(R.id.b_competition_create_group_add_member)
    public void addMember() {

        if (checker((UserModel) spinner.getSelectedItem())) {
            EventBus.getDefault().post((UserModel) spinner.getSelectedItem());
            dismiss();
        } else {

        }
    }

    private void viewMode(boolean flag) {
        loader.setVisibility(flag ? View.VISIBLE : View.INVISIBLE);
        containerArea.setVisibility(flag ? View.INVISIBLE : View.VISIBLE);
    }


    private void LoadMembers() {
        //http://zoogtech.com/golfapp/public/user/all
        String urlString = Api.WEB_URL + "user/all";

        viewMode(true);
        Log.d(GolfApp.TAG, urlString);

        StringRequest strReq = new StringRequest(Request.Method.GET, urlString,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        Gson gson = new Gson();
                        Type typeOfT = new TypeToken<List<UserModel>>() {
                        }.getType();
                        List<UserModel> request = gson.fromJson(
                                NetworkUtil.getReader(response), typeOfT);
                        if (request != null) {
                            listUser.addAll(request);
                            adapter.notifyDataSetChanged();
                        }
                        viewMode(false);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(GolfApp.TAG, error.getMessage());
                viewMode(true);
                dismiss();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> headers = super.getHeaders();

                if (headers == null
                        || headers.equals(Collections.emptyMap())) {
                    headers = new HashMap<String, String>();
                }

                headers.put(Api.AUTHORIZATION,
                        Api.BEARER + Api.getToken(context));

                return headers;
            }

        };

        GolfApp.addToRequestQueue(strReq, loadAllUserTag);

    }


    private class SpinnerAdapter extends ArrayAdapter<UserModel> {


        private SpinnerAdapter(Context context, int resource, List<UserModel> objects) {
            super(context, resource, objects);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView text = new TextView(context);
            text.setTextColor(Color.BLACK);
            String str = listUser.get(position).getFirstName() + " " + listUser.get(position).getLastName() + " " + listUser.get(position).getEmail().charAt(0);
            text.setText(str);
            text.setMaxLines(1);

            return text;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {


            TextView text = new TextView(context);
            text.setTextColor(Color.WHITE);
            String str = listUser.get(position).getFirstName() + " " + listUser.get(position).getLastName() + " " + listUser.get(position).getEmail().charAt(0);
            text.setText(str);
            text.setMaxLines(1);

            int padding = context.getResources().getDimensionPixelSize(R.dimen.text_view_padding);
            text.setPadding(padding, padding, padding, padding);

            return text;
        }


    }

    public boolean checker(UserModel userModel) {
        boolean flag = false;
        if (tempUser.size() > 0) {
            if (tempUser.size() == 3) {
                Toast.makeText(context, "これ以上追加できません", Toast.LENGTH_SHORT).show();

            } else {
                for (int i = 0; i < tempUser.size(); i++) {
                    if (userModel.getId().contentEquals(tempUser.get(i).getId())) {
                        flag = false;
                        Toast.makeText(context, " 既に登録済みです。", Toast.LENGTH_SHORT).show();
                        break;
                    } else {
                        flag = true;
                    }
                }
            }

        } else {
            flag = true;
        }


        return flag;
    }


}
