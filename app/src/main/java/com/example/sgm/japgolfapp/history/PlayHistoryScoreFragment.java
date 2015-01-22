package com.example.sgm.japgolfapp.history;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sgm.japgolfapp.BaseFragment;
import com.example.sgm.japgolfapp.R;
import com.example.sgm.japgolfapp.history.adapters.PlayHistoryScoreAdapter;
import com.example.sgm.japgolfapp.models.Competitor;

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


public class PlayHistoryScoreFragment extends BaseFragment{

    private View view_container;
    private ArrayList<Competitor> mItems;
    private PlayHistoryScoreAdapter adapter;
    private ListView lvPlayHistoryScores;
    private String pid;
    private boolean success = false;
    String score_json_string;
    private ProgressDialog pdialog;
    boolean shown = false;

    @OnClick(R.id.logout_button)
    public void click_logout() {
        logout();
    }

    @OnClick(R.id.menu_button)
    public void showMenu() {
        shown = sidemenu(view_container, shown);
    }

    private class GetScores extends AsyncTask<String, String, String> {

        public GetScores() {
            pdialog = new ProgressDialog(getActivity());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog.setMessage(getResources().getString(R.string.jap_loading_scores));
            pdialog.show();
            pdialog.setCancelable(false);
            pdialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected String doInBackground(String... strings) {
            byte[] score_byte = null;
            String score_string = "";

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

            HttpClient party_httpclient = new DefaultHttpClient();
            HttpGet party_httpget = new HttpGet("http://zoogtech.com/golfapp/public/counting/party-play/"+pid+"?access_token="+golfapp_token.toString());

            try {
                HttpResponse response = party_httpclient.execute(party_httpget);
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    score_byte = EntityUtils.toByteArray(response.getEntity());
                    score_string = new String(score_byte, "UTF-8");
                    System.out.println(score_string);
                    score_json_string = score_string;
                    success = true;
                }else {
                    score_byte = EntityUtils.toByteArray(response.getEntity());
                    score_string = new String(score_byte, "UTF-8");
                    System.out.println(score_string);
                    score_json_string = score_string;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return score_string;
        }

        @Override
        protected void onPostExecute(String result2) {
            if(pdialog != null && pdialog.isShowing()) {
                pdialog.dismiss();
            }
            if(success) {
                JSONArray array = null;
                try {
                    array = new JSONArray(score_json_string);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                TableLayout main_table = (TableLayout) view_container.findViewById(R.id.score_count_party_members);
//                main_table.removeAllViews();
                if (array != null) {
                    mItems = new ArrayList<Competitor>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject row = null;
                        try {
                            row = array.getJSONObject(i);
                            mItems.add(new Competitor(row.getString("name"), row.getString("gross"), row.getString("net"), row.getString("rank"), null));
//                            LayoutInflater inflater = LayoutInflater.from(getActivity());
//                            final View item = inflater.inflate(R.layout.score_counting_party_play_row, main_table, false);
//                            TextView player_name_col = (TextView) item.findViewById(R.id.party_play_row_name);
//                            player_name_col.setText(row.getString("name"));
//                            TextView gross_col = (TextView) item.findViewById(R.id.party_play_gross);
//                            gross_col .setText(row.getString("gross"));
//                            TextView net_col = (TextView) item.findViewById(R.id.party_play_net);
//                            net_col.setText(row.getString("net"));
//                            main_table.addView(item);
//
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                lvPlayHistoryScores = (ListView) view_container.findViewById(R.id.lvPartyHistoryScore);
                adapter = new PlayHistoryScoreAdapter(getActivity(), 0, mItems);
                lvPlayHistoryScores.setAdapter(adapter);
                Toast.makeText(getActivity(), getResources().getString(R.string.information_loaded), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.jap_something_wrong), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public PlayHistoryScoreFragment(){}

    public void setItems(ArrayList<Competitor> competitor){
        mItems = competitor;
    }

    public void setId(String id) {
        pid = id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_play_history_score, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view_container = view;
        new GetScores().execute();
//        lvPlayHistoryScores = (ListView) view.findViewById(R.id.lvPartyHistoryScore);
//        adapter = new PlayHistoryScoreAdapter(getActivity(), 0, mItems);
//        lvPlayHistoryScores.setAdapter(adapter);
    }

}
