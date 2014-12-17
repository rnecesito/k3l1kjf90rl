package com.example.sgm.japgolfapp.Competition;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sgm.japgolfapp.BaseFragment;
import com.example.sgm.japgolfapp.R;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
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

public class ViewClosedCompetitionGroupsFragment extends BaseFragment {
    private String retVal = null;
    private ProgressDialog pdialog;
    private boolean success = false;
    String response2;
    TableLayout main_table;

    private class CompetitionView extends AsyncTask<String, String, String> {

        public CompetitionView() {
            pdialog = new ProgressDialog(getActivity());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog.setMessage("Loading courses...");
            pdialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            byte[] result = null;
            String result2 = "";
            File cDir = getActivity().getCacheDir();
            File tempFile = new File(cDir.getPath() + "/" + "golfapp_token.txt") ;
            String strLine="";
            StringBuilder text = new StringBuilder();
            try {
                FileReader fReader = new FileReader(tempFile);
                BufferedReader bReader = new BufferedReader(fReader);
                while( (strLine=bReader.readLine()) != null  ){
                    text.append(strLine);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }

            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httppost = new HttpGet("http://zoogtech.com/golfapp/public/closed-competition/group?access_token="+text.toString());

            try {
                HttpResponse response = httpclient.execute(httppost);
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    result = EntityUtils.toByteArray(response.getEntity());
                    result2 = new String(result, "UTF-8");
                    System.out.println(result2);
                    System.out.println("Success!");
                    response2 = result2;
                    success = true;
                }else {
                    result = EntityUtils.toByteArray(response.getEntity());
                    result2 = new String(result, "UTF-8");
                    System.out.println(result2);
                    System.out.println("Failed!");
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result2;
        }

        @Override
        protected void onPostExecute(String result2) {
//            super.onPostExecute(result2);
            if(pdialog != null && pdialog.isShowing()) {
                pdialog.dismiss();
            }
            if(success) {
                JSONArray array = null;
                try {
                    array = new JSONArray(result2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < array.length(); i++) {
                    JSONObject row = null;
                    try {
                        row = array.getJSONObject(i);
                        LayoutInflater inflater = LayoutInflater.from(getContext());
                        final View item = inflater.inflate(R.layout.competition_closed_groups_row, main_table, false);
                        TextView tv_group_name = (TextView) item.findViewById(R.id.cgr_grp_name);
                        tv_group_name.setText(row.getString("name"));
                        TextView tv_member = (TextView) item.findViewById(R.id.cgr_member_cnt);
                        tv_member.setText(i+"");
                        ImageView iv_action = (ImageView) item.findViewById(R.id.approve_competition);
                        iv_action.setTag(row.getString("id"));
                        if(i % 2 == 0) {
                            item.setBackgroundColor(Color.WHITE);
                        } else {
                            item.setBackgroundColor(Color.LTGRAY);
                        }
                        main_table.addView(item);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Toast.makeText(getContext(), "Groups loaded.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_closed_comeptition_view_groups, container, false);
        main_table = (TableLayout) view.findViewById(R.id.closed_groups_table);

        new CompetitionView().execute();

        return view;
	}
}
