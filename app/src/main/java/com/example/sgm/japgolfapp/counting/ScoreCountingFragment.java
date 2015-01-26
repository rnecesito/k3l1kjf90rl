package com.example.sgm.japgolfapp.counting;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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
import org.apache.http.protocol.HTTP;
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
import butterknife.OnItemSelected;

/**
 * A simple {@link android.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.example.sgm.japgolfapp.counting.ScoreCountingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link com.example.sgm.japgolfapp.counting.ScoreCountingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScoreCountingFragment extends BaseFragment {
    private ProgressDialog pdialog;
    private boolean success = false;
    String party_string;
    String score_json_string;
    String party_id;
    View view_container;
    ArrayList<Parties> party_list;
    boolean shown = false;

    private class Parties {
        int id = 0;
        String name = "";

        public Parties(int _id, String _name) {
            id = _id;
            name = _name;
        }

        public String toString() {
            return name;
        }
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

            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet("http://zoogtech.com/golfapp/public/party-play?access_token="+golfapp_token.toString());

            try {
                HttpResponse response = httpclient.execute(httpget);
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    result_byte = EntityUtils.toByteArray(response.getEntity());
                    result_string = new String(result_byte, HTTP.UTF_8);
                    party_string = result_string;
                    success = true;
                }else {
                    result_byte = EntityUtils.toByteArray(response.getEntity());
                    result_string = new String(result_byte, HTTP.UTF_8);
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
            if(pdialog != null && pdialog.isShowing()) {
                pdialog.dismiss();
            }
            if(success) {
                JSONArray array = null;
                try {
                    array = new JSONArray(party_string);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Spinner spinner = (Spinner) view_container.findViewById(R.id.score_count_party_spinner2);
                ArrayList<Parties> party_list = new ArrayList<Parties>();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject row = null;
                    try {
                        row = array.getJSONObject(i);
                        party_list.add(new Parties(Integer.parseInt(row.getString("id")), row.getString("name")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(getActivity(), R.layout.spinner_background, party_list);
                spinner.setAdapter(spinnerArrayAdapter);
                Toast.makeText(getActivity(), getResources().getString(R.string.information_loaded), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.jap_something_wrong), Toast.LENGTH_SHORT).show();
            }
        }
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
            String pid = strings[0];
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
                    score_string = new String(score_byte, HTTP.UTF_8);
                    System.out.println(score_string);
                    score_json_string = score_string;
                    success = true;
                }else {
                    score_byte = EntityUtils.toByteArray(response.getEntity());
                    score_string = new String(score_byte, HTTP.UTF_8);
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

            return "Done";
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
                TableLayout main_table = (TableLayout) view_container.findViewById(R.id.score_count_party_members);
                main_table.removeAllViews();
                if (array != null) {
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject row = null;
                        try {
                            row = array.getJSONObject(i);
                            LayoutInflater inflater = LayoutInflater.from(getActivity());
                            final View item = inflater.inflate(R.layout.score_counting_party_play_row, main_table, false);
                            TextView player_name_col = (TextView) item.findViewById(R.id.party_play_row_name);
                            player_name_col.setText(row.getString("rank") + "位 " + row.getString("name"));
                            TextView gross_col = (TextView) item.findViewById(R.id.party_play_gross);
                            gross_col .setText(row.getString("gross"));
                            TextView net_col = (TextView) item.findViewById(R.id.party_play_net);
                            net_col.setText(row.getString("net"));
                            main_table.addView(item);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                Toast.makeText(getActivity(), getResources().getString(R.string.information_loaded), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.jap_something_wrong), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @OnItemSelected(R.id.score_count_party_spinner2)
    public void changeParty(AdapterView<?> adapterView, View view2, int i, long l) {
        Parties p = (Parties) adapterView.getSelectedItem();
        if (adapterView.getSelectedItem() != null) {
            System.out.println(p.id+"");
            new GetScores().execute(p.id+"");
        }
    }

    @OnClick(R.id.logout_button)
    public void click_logout() {
        logout();
    }

    @OnClick(R.id.menu_button)
    public void showMenu() {
        shown = sidemenu(view_container, shown);
    }

    @OnClick(R.id.return_btn)
    public void goBack() {
        SharedPreferences prefs = getActivity().getSharedPreferences(
                "com.golf.app", Context.MODE_PRIVATE);

        String hasLoggedIn = "com.golf.app.fromcounting";
        prefs.edit().putBoolean(hasLoggedIn, true).apply();
        popBackStack();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_score_counting, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view_container = view;
        new InitLists().execute();
        SharedPreferences prefs = getActivity().getSharedPreferences(
                "com.golf.app", Context.MODE_PRIVATE);

        String hasLoggedIn = "com.golf.app.fromcounting";
        prefs.edit().putBoolean(hasLoggedIn, true).apply();
    }

}
