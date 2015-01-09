package com.example.sgm.japgolfapp.Competition;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.example.sgm.japgolfapp.BaseFragment;
import com.example.sgm.japgolfapp.Competition.adapter.GroupListAdapter;
import com.example.sgm.japgolfapp.GolfApp;
import com.example.sgm.japgolfapp.R;
import com.example.sgm.japgolfapp.api.Api;
import com.example.sgm.japgolfapp.models.CompetitionGroupModel;
import com.example.sgm.japgolfapp.util.NetworkUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ViewClosedCompetitionGroupsFragment extends BaseFragment {
    private final String groupListQueue = "GroupListQueue";
    String response2;
    @InjectView(R.id.lv_competition_view_group)
    ListView listView;
    @InjectView(R.id.show_own_groups)
    CheckBox checkBox;
    private String retVal = null;
    private ProgressDialog pdialog;
    private boolean success = false;
    private List<CompetitionGroupModel> groupList, tempGroupList, myGroupList;
    private GroupListAdapter adapter, myAdapter;//, newAdapter;
    View view_container;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        groupList = new ArrayList<CompetitionGroupModel>();
        adapter = new GroupListAdapter(getActivity(), R.layout.generic_3_column_item_layout, groupList);

        myGroupList = new ArrayList<CompetitionGroupModel>();
        myAdapter = new GroupListAdapter(getActivity(), R.layout.generic_3_column_item_layout, myGroupList);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_closed_comeptition_view_groups, container, false);
//        main_table = (TableLayout) view.findViewById(R.id.closed_groups_table);
//        new CompetitionView().execute();
        ButterKnife.inject(this, view);
        initLayout();

        File cDir = getContext().getCacheDir();
        File tempFile = new File(cDir.getPath() + "/" + "competition_number.txt");
        String strLine = "";
        StringBuilder competition_number = new StringBuilder();
        try {
            FileReader fReader = new FileReader(tempFile);
            BufferedReader bReader = new BufferedReader(fReader);
            while ((strLine = bReader.readLine()) != null) {
                competition_number.append(strLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (competition_number.toString().equals(' ')) {
//            checkBox.setChecked(false);
        }



        return view;
    }

//    private class RefreshList extends AsyncTask<Object, Object, Object> {
//
//        @Override
//        protected Object doInBackground(Object... params) {
//            return null;
//        }
//
//        protected void onPostExecute(String result) {
//            EditText etSearch = (EditText) view_container.findViewById(R.id.competition_name);
//            clearAdapter();
//            if (!etSearch.getText().toString().equals("")) {
//                for(int i=0 ;i < listView.getCount() ;i++){
//                    if(groupList.get(i).getName().toUpperCase().contains(etSearch.getText().toString().toUpperCase())){
//                        tempGroupList.add(groupList.get(i));
//                        Log.d("Group Name", groupList.get(i).getName());
//                    }
//                }
//
////                newAdapter = new GroupListAdapter(getActivity(), R.layout.generic_3_column_item_layout, tempGroupList );
//                adapter = new GroupListAdapter(getActivity(), R.layout.generic_3_column_item_layout, tempGroupList);
//                listView.setAdapter(adapter);
//
//            }else{
//                adapter = new GroupListAdapter(getActivity(), R.layout.generic_3_column_item_layout, groupList);
//                listView.setAdapter(adapter);
//            }
//        }
//    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LoadAllGroups();
        view_container = view;
        tempGroupList = adapter.getGroupList();
        EditText etSearch = (EditText) view.findViewById(R.id.competition_name);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals("")) {
//                    List<CompetitionGroupModel> tempList = groupList;
                    List<CompetitionGroupModel> tempList = new ArrayList<CompetitionGroupModel>();

//                    for(int i=0 ;i < listView.getCount() ;i++){
//                        if(tempList.get(i).getName().toUpperCase().contains(s.toString().toUpperCase())){
//                            groupList.add(tempList.get(i));
//                            Log.d("Group Name", tempList.get(i).getName());
//                        }
//                    }
//                    adapter.setGroupList(groupList);
//                    adapter.notifyDataSetChanged();
//                    listView.setAdapter(adapter);
//
//                    for (int i=0; i < listView.getCount(); i++) {
//                        Log.d("Group name 2 is ", groupList.get(i).getName());
//                    }

                    for (int i = 0; i < listView.getCount(); i++) {
                        if (groupList.get(i).getName().toUpperCase().contains(s.toString().toUpperCase())) {
                            tempList.add(groupList.get(i));
                        }
                    }

                    adapter.setGroupList(tempList);
                    adapter.notifyDataSetChanged();
                    listView.setAdapter(adapter);
                    listView.invalidateViews();

//                    listView.setAdapter(adapter);
//                    for (int i = 0; i < listView.getCount(); i++) {
//                        Log.d("Group name 2 is ", groupList.get(i).getName());
//                    }
//                    Toast.makeText(getActivity(), "CHANGE!", Toast.LENGTH_SHORT).show();

                } else {
                    adapter = new GroupListAdapter(getActivity(), R.layout.generic_3_column_item_layout, groupList);
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initLayout() {
//        listView.setAdapter(adapter);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (myGroupList.size() < 1) {
                        LoadAllMyGroups();
                        listViewChange(true);
                    } else {
                        listViewChange(true);
                    }
                } else {
                    if (groupList.size() < 1) {
                        LoadAllGroups();
                        listView.invalidate();
                    } else {
                        listViewChange(false);
                    }
                }
            }
        });
    }

    private void LoadAllMyGroups() {
        //http://zoogtech.com/golfapp/public/closed-competition/group/mine
        String urlString = Api.WEB_URL + "closed-competition/group/mine";

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading Your Groups...");
        pDialog.show();
        Log.d(GolfApp.TAG, urlString);

        StringRequest strReq = new StringRequest(Request.Method.GET, urlString,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        Gson gson = new Gson();
                        Type typeOfT = new TypeToken<List<CompetitionGroupModel>>() {
                        }.getType();
                        List<CompetitionGroupModel> request = gson.fromJson(
                                NetworkUtil.getReader(response), typeOfT);
                        if (request != null) {
                            myGroupList.addAll(request);
                            myAdapter.notifyDataSetChanged();
                            listView.setAdapter(myAdapter);
                        }
                        pDialog.dismiss();
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

        GolfApp.addToRequestQueue(strReq, groupListQueue);

    }

    private void clearAdapter() {
        groupList.clear();
        myGroupList.clear();
        adapter.notifyDataSetChanged();
        myAdapter.notifyDataSetChanged();
    }

    private void LoadAllGroups() {
        //http://zoogtech.com/golfapp/public/closed-competition/group
        String urlString = Api.WEB_URL + "closed-competition/group";

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading Groups...");
        pDialog.show();
        Log.d(GolfApp.TAG, urlString);
        clearAdapter();

        StringRequest strReq = new StringRequest(Request.Method.GET, urlString,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        Gson gson = new Gson();
                        Type typeOfT = new TypeToken<List<CompetitionGroupModel>>() {
                        }.getType();
                        List<CompetitionGroupModel> request = gson.fromJson(
                                NetworkUtil.getReader(response), typeOfT);
                        if (request != null) {
                            groupList.addAll(request);
                            adapter.notifyDataSetChanged();
                            listView.setAdapter(adapter);
                        }
                        pDialog.dismiss();
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

        GolfApp.addToRequestQueue(strReq, groupListQueue);

    }

    private void listViewChange(boolean mine) {
        if (mine) {
            listView.setAdapter(myAdapter);
            listView.invalidate();
        } else {
            listView.setAdapter(adapter);
            listView.invalidate();
        }
    }

    public void onEvent(CompetitionGroupModel cModel) {
        EditGroup group = EditGroup.newInstance(cModel);
        showFragmentAndAddToBackStack(group);
    }
}
