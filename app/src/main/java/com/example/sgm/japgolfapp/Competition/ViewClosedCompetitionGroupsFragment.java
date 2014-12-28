package com.example.sgm.japgolfapp.Competition;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ViewClosedCompetitionGroupsFragment extends BaseFragment {
    private String retVal = null;
    private ProgressDialog pdialog;
    private boolean success = false;
    String response2;

    @InjectView(R.id.lv_competition_view_group)
    ListView listView;

    @InjectView(R.id.show_own_groups)
    CheckBox checkBox;

    private List<CompetitionGroupModel> groupList, myGroupList;
    private GroupListAdapter adapter, myAdapter;
    private final String groupListQueue = "GroupListQueue";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        groupList = new ArrayList<CompetitionGroupModel>();
        adapter = new GroupListAdapter(getActivity(), R.layout.generic_3_column_item_layout, groupList);

        myGroupList = new ArrayList<CompetitionGroupModel>();
        myAdapter = new GroupListAdapter(getActivity(), R.layout.generic_3_column_item_layout, myGroupList);
    }


    //    private class CompetitionView extends AsyncTask<String, String, String> {
//
//        public CompetitionView() {
//            pdialog = new ProgressDialog(getActivity());
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pdialog.setMessage("Loading courses...");
//            pdialog.show();
//        }
//
//        @Override
//        protected String doInBackground(String... strings) {
//            byte[] result = null;
//            String result2 = "";
//            File cDir = getActivity().getCacheDir();
//            File tempFile = new File(cDir.getPath() + "/" + "golfapp_token.txt") ;
//            String strLine="";
//            StringBuilder text = new StringBuilder();
//            try {
//                FileReader fReader = new FileReader(tempFile);
//                BufferedReader bReader = new BufferedReader(fReader);
//                while( (strLine=bReader.readLine()) != null  ){
//                    text.append(strLine);
//                }
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }catch(IOException e){
//                e.printStackTrace();
//            }
//
//            HttpClient httpclient = new DefaultHttpClient();
//            HttpGet httppost = new HttpGet("http://zoogtech.com/golfapp/public/closed-competition/group?access_token="+text.toString());
//
//            try {
//                HttpResponse response = httpclient.execute(httppost);
//                StatusLine statusLine = response.getStatusLine();
//                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
//                    result = EntityUtils.toByteArray(response.getEntity());
//                    result2 = new String(result, "UTF-8");
//                    System.out.println(result2);
//                    System.out.println("Success!");
//                    response2 = result2;
//                    success = true;
//                }else {
//                    result = EntityUtils.toByteArray(response.getEntity());
//                    result2 = new String(result, "UTF-8");
//                    System.out.println(result2);
//                    System.out.println("Failed!");
//                }
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            } catch (ClientProtocolException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            return result2;
//        }
//
//        @Override
//        protected void onPostExecute(String result2) {
////            super.onPostExecute(result2);
//            if(pdialog != null && pdialog.isShowing()) {
//                pdialog.dismiss();
//            }
//            if(success) {
//                JSONArray array = null;
//                try {
//                    array = new JSONArray(result2);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                for (int i = 0; i < array.length(); i++) {
//                    JSONObject row = null;
//                    try {
//                        row = array.getJSONObject(i);
//                        LayoutInflater inflater = LayoutInflater.from(getContext());
//                        final View item = inflater.inflate(R.layout.competition_closed_groups_row, main_table, false);
//                        TextView tv_group_name = (TextView) item.findViewById(R.id.cgr_grp_name);
//                        tv_group_name.setText(row.getString("name"));
//                        TextView tv_member = (TextView) item.findViewById(R.id.cgr_member_cnt);
//                        tv_member.setText(i+"");
//                        ImageView iv_action = (ImageView) item.findViewById(R.id.approve_competition);
//                        iv_action.setTag(row.getString("id"));
//                        if(i % 2 == 0) {
//                            item.setBackgroundColor(Color.WHITE);
//                        } else {
//                            item.setBackgroundColor(Color.LTGRAY);
//                        }
//                        main_table.addView(item);
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                Toast.makeText(getContext(), "Groups loaded.", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(getContext(), "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_closed_comeptition_view_groups, container, false);
//        main_table = (TableLayout) view.findViewById(R.id.closed_groups_table);
//        new CompetitionView().execute();
        ButterKnife.inject(this, view);
        initLayout();
        LoadAllGroups();

        return view;
    }

    private void initLayout() {
        listView.setAdapter(adapter);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (myGroupList.size() < 1) {
                        LoadAllMyGroups();
                    } else {
                        listViewChange(true);
                    }
                } else {
                    if (groupList.size() < 1) {
                        LoadAllGroups();
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


    private void LoadAllGroups() {
        //http://zoogtech.com/golfapp/public/closed-competition/group
        String urlString = Api.WEB_URL + "closed-competition/group";

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading Groups...");
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
        } else {
            listView.setAdapter(adapter);
        }
    }

    public void onEvent(CompetitionGroupModel cModel) {
        EditGroup group = EditGroup.newInstance(cModel);
        showFragmentAndAddToBackStack(group);
    }

}
