package com.example.sgm.japgolfapp.Competition;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.sgm.japgolfapp.Competition.adapter.GroupMemberAdapter;
import com.example.sgm.japgolfapp.EventFragment;
import com.example.sgm.japgolfapp.GolfApp;
import com.example.sgm.japgolfapp.R;
import com.example.sgm.japgolfapp.R.id;
import com.example.sgm.japgolfapp.api.Api;
import com.example.sgm.japgolfapp.models.UserModel;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class CreateClosedCompetitionGroupFragment extends EventFragment {
    View view_container;
    private boolean success = false;
    private ProgressDialog pdialog;
    String courses_json;
    TextView date_view;
    TextView date_view2;
    TableLayout competitor_list;
    String retVal;

    private List<UserModel> listUser = new ArrayList<UserModel>();
    private GroupMemberAdapter listAdapter;

    @InjectView(R.id.competitor_container)
    LinearLayout btnAddMember;

    @InjectView(R.id.lv_competition_create_group_addmember)
    ListView listView;

    private View view;
    private final String createGroupQueue = "CreateGroupQueue";

    private class Courses {
        int id = 0;
        String course = "";

        public Courses(int _id, String _course) {
            id = _id;
            course = _course;
        }

        public String toString() {
            return course;
        }
    }

    private class InitLists extends AsyncTask<String, String, String> {
        public InitLists() {
            pdialog = new ProgressDialog(getActivity());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog.setMessage(getResources().getString(R.string.jap_loading_competition_info));
            pdialog.show();
            pdialog.setCancelable(false);
            pdialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected String doInBackground(String... strings) {
            byte[] result_byte = null;
            String result_string = "";
            success = false;
            File cDir = getActivity().getCacheDir();
            File tempFile = new File(cDir.getPath() + "/" + "golfapp_token.txt");
            String strLine = "";
            StringBuilder golfapp_token = new StringBuilder();
            try {
                FileReader fReader = new FileReader(tempFile);
                BufferedReader bReader = new BufferedReader(fReader);
                while ((strLine = bReader.readLine()) != null) {
                    golfapp_token.append(strLine);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet("http://zoogtech.com/golfapp/public/closed-competition?access_token=" + golfapp_token.toString());

            try {
                HttpResponse response = httpclient.execute(httpget);
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    result_byte = EntityUtils.toByteArray(response.getEntity());
                    result_string = new String(result_byte, "UTF-8");
                    courses_json = result_string;
                    success = true;
                } else {
                    result_byte = EntityUtils.toByteArray(response.getEntity());
                    result_string = new String(result_byte, "UTF-8");
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result_string;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pdialog != null && pdialog.isShowing()) {
                pdialog.dismiss();
            }
            if (success) {
                JSONArray array = null;
                try {
                    array = new JSONArray(courses_json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Spinner spinner = (Spinner) view_container.findViewById(id.competition_spinner);
                ArrayList<Courses> course_list = new ArrayList<Courses>();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject row = null;
                    try {
                        row = array.getJSONObject(i);
                        course_list.add(new Courses(Integer.parseInt(row.getString("id")), row.getString("name")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, course_list);
                spinner.setAdapter(spinnerArrayAdapter);

                Toast.makeText(getActivity(), getResources().getString(R.string.jap_loading_complete), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.jap_something_wrong), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class CompetitionCreate extends AsyncTask<String, String, String> {
        StringBuilder text = new StringBuilder();

        public CompetitionCreate() {
            pdialog = new ProgressDialog(getActivity());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog.setMessage(getResources().getString(R.string.jap_registering));
            pdialog.show();
            pdialog.setCancelable(false);
            pdialog.setCanceledOnTouchOutside(false);

            File cDir = getActivity().getCacheDir();
            File tempFile = new File(cDir.getPath() + "/" + "golfapp_token.txt");

            String strLine = "";

            try {
                FileReader fReader = new FileReader(tempFile);
                BufferedReader bReader = new BufferedReader(fReader);
                while ((strLine = bReader.readLine()) != null) {
                    text.append(strLine);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String pname = strings[0];
            String comp = strings[1];
            byte[] result = null;
            String str = "";
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://zoogtech.com/golfapp/public/closed-competition/" + comp + "/create-group");

            try {
                List<NameValuePair> json = new ArrayList<NameValuePair>();
                json.add(new BasicNameValuePair("name", pname));


                httppost.setHeader("Content-type", "application/x-www-form-urlencoded");
                httppost.setHeader("Authorization", text.toString());
                httppost.setEntity(new UrlEncodedFormEntity(json));

                HttpResponse response = httpclient.execute(httppost);
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpURLConnection.HTTP_OK) {
                    result = EntityUtils.toByteArray(response.getEntity());
                    str = new String(result, "UTF-8");
                    success = true;
                    retVal = str;
                } else {
                    result = EntityUtils.toByteArray(response.getEntity());
                    str = new String(result, "UTF-8");
                    retVal = str;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Yeah";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pdialog != null && pdialog.isShowing()) {
                pdialog.dismiss();
            }
            if (success) {
                Toast.makeText(getActivity(), getResources().getString(R.string.jap_reg_success), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.jap_something_wrong), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater
                .inflate(R.layout.fragment_closed_comeptition_create_group, container, false);

        ButterKnife.inject(this, view);
        listAdapter = new GroupMemberAdapter(getActivity(), R.layout.generic_3_column_item_layout, listUser);
        listView.setAdapter(listAdapter);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view_container = view;
        //  competitor_list = (TableLayout) view.findViewById(id.competitors);
        new InitLists().execute();

        Button b = (Button) view.findViewById(id.group_create);

        final EditText party_name = (EditText) view.findViewById(id.competition_name);
        final Spinner course_name = (Spinner) view.findViewById(id.competition_spinner);
        b.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String pn_val = party_name.getText().toString();
                Courses c = (Courses) course_name.getSelectedItem();
                int course_id = c.id;
                if (pn_val.matches("")) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.jap_enter_party_name), Toast.LENGTH_SHORT).show();
                    return;
                } else {
//                    new CompetitionCreate().execute(pn_val, course_id + "");
                    CreateGroup(pn_val, course_id + "");
                }
            }
        });


    }

    private void CreateGroup(String groupName, String competition) {
        //http://zoogtech.com/golfapp/public/closed-competition/1/create-group
        String urlString = Api.WEB_URL + "closed-competition/" + competition + "/create-group";

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage(getResources().getString(R.string.jap_registering));
        pDialog.show();

        JSONObject requestObject = new JSONObject();

        try {
            requestObject.put("name", groupName);
            JSONArray arr = new JSONArray(generateMemberArray());
            requestObject.put("members", arr);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(GolfApp.TAG, urlString);


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, urlString,
                requestObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                pDialog.dismiss();
                Toast.makeText(getActivity(), getResources().getString(R.string.jap_reg_success), Toast.LENGTH_SHORT).show();
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

        GolfApp.addToRequestQueue(jsonObjReq, createGroupQueue);

    }

    private List<String> generateMemberArray() {
        List<String> str = new ArrayList<String>();
        if (listUser.size() > 0) {
            for (int i = 0; i < listUser.size(); i++) {
                str.add(listUser.get(i).getId());
            }
        }

        return str;
    }

    @OnClick(R.id.competitor_container)
    public void callAddMember() {
        MemberListDialog dialog = new MemberListDialog(getActivity(), listUser);
        dialog.show();
    }

    public void onEvent(UserModel userModel) {
        listUser.add(userModel);
        listAdapter.notifyDataSetChanged();
    }


}
