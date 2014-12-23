package com.example.sgm.japgolfapp.settings;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sgm.japgolfapp.BaseFragment;
import com.example.sgm.japgolfapp.R;
import com.example.sgm.japgolfapp.history.adapters.PlayHistoryScoreAdapter;
import com.example.sgm.japgolfapp.models.BetSetting;
import com.example.sgm.japgolfapp.models.Competitor;
import com.example.sgm.japgolfapp.settings.adapter.BetChooserAdapter;

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


public class BetSettingChooserFragment extends BaseFragment{

    private View view_container;
    private ArrayList<BetSetting> mItems;
    private BetChooserAdapter adapter;
    private ListView lvBetSettings;
    private TextView tvReturn;
    private Integer mItemNumber;

    private boolean success = false;
    private ProgressDialog pdialog;
    private String bettype_json_string;

    private final Integer MAXSETTINGS = 3;
    private Integer settingsCount = 0;
    public BetSettingChooserFragment(){}

    public void setItems(ArrayList<BetSetting> competitor){
        mItems = competitor;
    }

    public void setItemNumber(Integer i){
        this.mItemNumber = i;
    }

    private class InitLists extends AsyncTask<String, String, String> {
        public InitLists() {
            pdialog = new ProgressDialog(getActivity());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pdialog.setMessage(getResources().getString(R.string.);
            pdialog.setMessage("Loading bets");
            pdialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            success = false;
            byte[] bets_byte = null;
            String bets_string = "";

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

            HttpClient bets_httpclient = new DefaultHttpClient();
            HttpGet bets_httpget = new HttpGet("http://zoogtech.com/golfapp/public/bet-type?access_token=" + golfapp_token.toString());

            try {
                HttpResponse response = bets_httpclient.execute(bets_httpget);
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    bets_byte = EntityUtils.toByteArray(response.getEntity());
                    bets_string = new String(bets_byte, "UTF-8");
                    bettype_json_string = bets_string;
                    success = true;
                } else {
                    bets_byte = EntityUtils.toByteArray(response.getEntity());
                    bets_string = new String(bets_byte, "UTF-8");
                    bettype_json_string = bets_string;
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
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pdialog != null && pdialog.isShowing()) {
                pdialog.dismiss();
            }
            if (success) {
                JSONArray bets_array = null;
                try {
                    bets_array = new JSONArray(bettype_json_string);
                    for (int i = 0; i < bets_array.length(); i++) {
                        JSONObject row = null;
                        try {
                            row = bets_array.getJSONObject(i);
                            mItems.add(new BetSetting(row.getString("name"), row.getString("description"), false));
//                            bets_list.add(new Bets(Integer.parseInt(row.getString("id")), row.getString("description"), row.getString("name")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    lvBetSettings = (ListView) view_container.findViewById(R.id.lvBets);
                    adapter = new BetChooserAdapter(getActivity(), 0, mItems);
                    lvBetSettings.setAdapter(adapter);
                    lvBetSettings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (lvBetSettings.getChildAt(position) != null) {
                                CheckBox cb = ((CheckBox) lvBetSettings.getChildAt(position).findViewById(R.id.cbBetCheck));
                    /*TextView helpTv = ((TextView)lvBetSettings.getChildAt(position).findViewById(R.id.tvHelp));*/
                                if (cb.isChecked()) {
                                    mItems.get(position).setIsChosen(false);
                                    adapter.notifyDataSetChanged();
                                    settingsCount--;
                                }
                                else { //do something else}
                                    if(settingsCount < MAXSETTINGS) {
                                        NewBetSettingFragment.mItems.get(mItemNumber).getBetSettings().add(mItems.get(position));
                                        mItems.get(position).setIsChosen(true);
                                        adapter.notifyDataSetChanged();
                                        settingsCount++;
                                    }else{
                                        Toast.makeText(getActivity(), "Cannot Add More than 3 Settings", Toast.LENGTH_SHORT).show();
                                    }
                                }
                        /*helpTv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                              new CustomDialogClass(getActivity());
                            }
                        });*/
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Toast.makeText(getActivity(), getResources().getString(R.string.jap_loading_complete), Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getActivity(), getResources().getString(R.string.jap_something_wrong), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_bet_setting, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view_container = view;
        new InitLists().execute();

        tvReturn = (TextView) view_container.findViewById(R.id.tvSaveReturn);
        tvReturn.setText("Return");
        tvReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SAVE ALL CHOOSEN BET SETTINGS TO HOLE RECORD
                getActivity().onBackPressed();
            }
        });

        //TEST DATAS -----------------------
//        mItems.add(new BetSetting("aaa", "aaa help",false));
//        mItems.add(new BetSetting("bbb", "bbb help",false));
//        mItems.add(new BetSetting("ccc", "ccc help",false));
//        mItems.add(new BetSetting("ddd", "ddd help",false));
//        mItems.add(new BetSetting("eee", "eee help",false));
//        mItems.add(new BetSetting("fff", "fff help",false));
//        mItems.add(new BetSetting("ggg", "ggg help",false));
//        mItems.add(new BetSetting("hhh", "hhh help",false));
        // ---------------------------------


        lvBetSettings = (ListView) view.findViewById(R.id.lvBets);
        adapter = new BetChooserAdapter(getActivity(), 0, mItems);
        lvBetSettings.setAdapter(adapter);
        lvBetSettings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (lvBetSettings.getChildAt(position) != null) {
                    CheckBox cb = ((CheckBox) lvBetSettings.getChildAt(position).findViewById(R.id.cbBetCheck));
                    /*TextView helpTv = ((TextView)lvBetSettings.getChildAt(position).findViewById(R.id.tvHelp));*/
                    if (cb.isChecked()) {
                        mItems.get(position).setIsChosen(false);
                        adapter.notifyDataSetChanged();
                        settingsCount--;
                    }
                    else { //do something else}
                        if(settingsCount < MAXSETTINGS) {
                            NewBetSettingFragment.mItems.get(mItemNumber).getBetSettings().add(mItems.get(position));
                            mItems.get(position).setIsChosen(true);
                            adapter.notifyDataSetChanged();
                            settingsCount++;
                        }else{
                            Toast.makeText(getActivity(), "Cannot Add More than 3 Settings", Toast.LENGTH_SHORT).show();
                        }
                    }

                }


            }
        });
    }

}
