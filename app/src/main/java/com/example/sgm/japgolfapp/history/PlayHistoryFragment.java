package com.example.sgm.japgolfapp.history;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sgm.japgolfapp.BaseFragment;
import com.example.sgm.japgolfapp.R;
import com.example.sgm.japgolfapp.history.adapters.PlayHistoryAdapter;
import com.example.sgm.japgolfapp.models.Competitor;
import com.example.sgm.japgolfapp.models.PlayHistory;

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
import java.util.ArrayList;

import butterknife.OnClick;

public class PlayHistoryFragment extends BaseFragment{

    private ListView lvPlayHistory;
    ArrayList<PlayHistory> mItems;
    PlayHistoryAdapter mAdapter;
    private ProgressDialog pdialog;
    private boolean success = false;
    String party_string;
    String score_json_string;
    String party_id;
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

    private class InitLists extends AsyncTask<String, String, String> {

        public InitLists() {
            pdialog = new ProgressDialog(getActivity());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog.setMessage(getResources().getString(R.string.jap_loading_parties));
            pdialog.show();
            pdialog.setCancelable(false);
            pdialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected String doInBackground(String... strings) {
            byte[] result_byte = null;
            String result_string = "";

            File cDir = getActivity().getCacheDir();
            File tempFile = new File(cDir.getPath() + "/" + "golfapp_token.txt") ;
            String strLine="";
            StringBuilder golfapp_token = new StringBuilder();

            try {
                FileReader fReader = new FileReader(tempFile);
                BufferedReader bReader = new BufferedReader(fReader);
                while( (strLine=bReader.readLine()) != null  ){
                    golfapp_token.append(strLine);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }

            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet("http://zoogtech.com/golfapp/public/party-play?access_token="+golfapp_token.toString());

            try {
                HttpResponse response = httpclient.execute(httpget);
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    result_byte = EntityUtils.toByteArray(response.getEntity());
                    result_string = new String(result_byte, "UTF-8");
                    party_string = result_string;
                    Log.d("Data", result_string);
                    success = true;
                }else {
                    result_byte = EntityUtils.toByteArray(response.getEntity());
                    result_string = new String(result_byte, "UTF-8");
                    party_string = result_string;
                    success = false;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Done";
        }

        @Override
        protected void onPostExecute(String result2) {
            if(success) {
                JSONArray array = null;
                try {
                    array = new JSONArray(party_string);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mItems = new ArrayList<PlayHistory>();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject row = null;
                    ArrayList<Competitor> dummy_one = new ArrayList<Competitor>();
                    try {
                        row = array.getJSONObject(i);
                        JSONObject course_info = new JSONObject(row.getString("course"));
                        mItems.add(new PlayHistory(row.getString("id"), row.getString("date"), course_info.getString("name"), dummy_one));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                lvPlayHistory = (ListView) view_container.findViewById(R.id.lvPlayHistory);
                mAdapter = new PlayHistoryAdapter(getActivity(), 0, mItems);
                lvPlayHistory.setAdapter(mAdapter);
                lvPlayHistory.setFocusable(true);
                lvPlayHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        PlayHistoryScoreFragment targetFragment = new PlayHistoryScoreFragment();
                        targetFragment.setId(mItems.get(i).getId());
                        showFragmentAndAddToBackStack(targetFragment);
                    }
                });
                if(pdialog != null && pdialog.isShowing()) {
                    pdialog.dismiss();
                }
                Toast.makeText(getActivity(), getResources().getString(R.string.information_loaded), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.jap_something_wrong), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_play_history, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view_container = view;
        new InitLists().execute();
    }

}
