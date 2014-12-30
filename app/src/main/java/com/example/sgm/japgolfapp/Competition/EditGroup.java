package com.example.sgm.japgolfapp.Competition;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.sgm.japgolfapp.BaseFragment;
import com.example.sgm.japgolfapp.Competition.adapter.GroupMemberAdapter;
import com.example.sgm.japgolfapp.GolfApp;
import com.example.sgm.japgolfapp.R;
import com.example.sgm.japgolfapp.api.Api;
import com.example.sgm.japgolfapp.models.CloseCompetitionModel;
import com.example.sgm.japgolfapp.models.CompetitionGroupModel;
import com.example.sgm.japgolfapp.models.UserModel;
import com.example.sgm.japgolfapp.util.NetworkUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class EditGroup extends BaseFragment {

    private final String CLOSE_COMPETITION_QUEUE = "closeCompetition";
    private final String editGroupQueue = "EditGroup";
    public CompetitionGroupModel cModel;
    public CloseCompetitionModel closeCompetitionModel;
    @InjectView(R.id.b_competition_edit_name)
    Button btnUpdate;
    @InjectView(R.id.lv_competition_edit_group_addmember)
    ListView listView;
    @InjectView(R.id.tv_competition_edit_group_close_name)
    TextView txtCloseCompetition;
    @InjectView(R.id.et_competition_edit_name)
    EditText txtGroupName;
    @InjectView(R.id.ll_competition_edit_name)
    LinearLayout btnAddMember;
    private View view;
    private List<CloseCompetitionModel> closeList;
    private GroupMemberAdapter adapter;
    private List<UserModel> userList;

    public static EditGroup newInstance(CompetitionGroupModel model) {
        EditGroup frag = new EditGroup();
        frag.cModel = model;
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        closeList = new ArrayList<CloseCompetitionModel>();
        userList = new ArrayList<UserModel>();
        adapter = new GroupMemberAdapter(getActivity(), R.layout.generic_3_column_item_layout, userList);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.competition_edit_group, container, false);

        ButterKnife.inject(this, view);

        LoadAllCompetition();
        return view;
    }


    private void LoadAllCompetition() {
        //http://zoogtech.com/golfapp/public/closed-competition
        String urlString = Api.WEB_URL + "closed-competition";

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading Group Data...");
        pDialog.show();
        Log.d(GolfApp.TAG, urlString);

        StringRequest strReq = new StringRequest(Request.Method.GET, urlString,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        Gson gson = new Gson();
                        Type typeOfT = new TypeToken<List<CloseCompetitionModel>>() {
                        }.getType();
                        List<CloseCompetitionModel> request = gson.fromJson(
                                NetworkUtil.getReader(response), typeOfT);
                        if (request != null) {
                            closeList.addAll(request);

                        }
                        pDialog.dismiss();
                        populate();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(GolfApp.TAG, error.getMessage());

                pDialog.dismiss();
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
                        Api.BEARER + Api.getToken(getActivity()));

                return headers;
            }

        };

        GolfApp.addToRequestQueue(strReq, CLOSE_COMPETITION_QUEUE);

    }

    private void populate() {


        for (int i = 0; i < cModel.getCompetitors().size(); i++) {
            userList.add(cModel.getCompetitors().get(i).getUserModel());
        }
        adapter.notifyDataSetChanged();
        for (int i = 0; i < closeList.size(); i++) {
            if (cModel.getClosedCompetitionId().contentEquals(closeList.get(i).getId())) {

                txtCloseCompetition.setText(closeList.get(i).getName());
                break;
            }
        }

        txtGroupName.setText(cModel.getName());
        listView.setAdapter(adapter);

    }


    @OnClick(R.id.ll_competition_edit_name)
    public void callAddMember() {
        MemberListDialog dialog = new MemberListDialog(getActivity(), userList);
        dialog.show();
    }

    @OnClick(R.id.b_competition_edit_name)
    public void update() {
        UpdateGroup();
    }

    public void onEvent(UserModel userModel) {
        userList.add(userModel);
        adapter.notifyDataSetChanged();
    }

    private void UpdateGroup() {
        //http://zoogtech.com/golfapp/public/closed-competition/1/create-group
        String urlString = Api.WEB_URL + "closed-competition/" + cModel.getClosedCompetitionId() + "/group/" + cModel.getId();

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Updating group...");
        pDialog.show();

        JSONObject requestObject = new JSONObject();
        JSONArray arr = new JSONArray(generateMemberArray());
        try {


            requestObject.put("name", txtGroupName.getText().toString());
            requestObject.put("members", arr);
            requestObject.put("closed_competition_id", cModel.getClosedCompetitionId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(GolfApp.TAG, urlString);


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT, urlString,
                requestObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {


                pDialog.dismiss();
                Toast.makeText(getActivity(), "Group created.", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(GolfApp.TAG, error.getMessage());
                pDialog.dismiss();
                Toast.makeText(getActivity(), getResources().getString(R.string.jap_something_wrong), Toast.LENGTH_SHORT).show();
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
                        Api.BEARER + Api.getToken(getActivity()));

                return headers;
            }

        };

        GolfApp.addToRequestQueue(jsonObjReq, editGroupQueue);

    }

    private List<String> generateMemberArray() {
        List<String> str = new ArrayList<String>();
        if (userList.size() > 0) {
            for (int i = 0; i < userList.size(); i++) {
                str.add(userList.get(i).getId());
            }
        }

        return str;
    }

}
