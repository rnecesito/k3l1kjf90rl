package com.example.sgm.japgolfapp.settings;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sgm.japgolfapp.BaseFragment;
import com.example.sgm.japgolfapp.R;
import com.example.sgm.japgolfapp.models.BetSetting;
import com.example.sgm.japgolfapp.settings.adapter.BetChooserAdapter;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
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
import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;


public class BetSettingChooserFragment extends BaseFragment{

    private ArrayList<BetSetting> mItems;
    private ArrayList<BetSetting> mInitialItems;
    private BetChooserAdapter adapter;
    private ListView lvBetSettings;
    private TextView tvReturn;

    private String bettype_json_string;

    public static Integer STATICsettingsCount;

    ArrayList<BetSetting> mChoosen;

    public BetSettingChooserFragment(){}

    @OnClick(R.id.logout_button)
    public void click_logout() {
        logout();
    }

    @OnClick(R.id.menu_button)
    public void showMenu() {
        shown = sidemenu(view_container, shown);
    }

    public void setItems(ArrayList<BetSetting> competitor){
        mItems = competitor;
        mInitialItems = competitor;
    }

    private String mPartyId;
    public void setPartyId(String partyId){
        mPartyId = partyId;
    }

    private String mHoleId;
    public void setHoleId(String id){
        mHoleId = id;
    }

    View view_container;
    boolean shown = false;
    private ProgressDialog pdialog;
    private boolean success = false;
    String retVal;

    private String readtoken() {
        /** Getting Cache Directory */
        File cDir = getActivity().getCacheDir();

        /** Getting a reference to temporary file, if created earlier */
        File tempFile = new File(cDir.getPath() + "/" + "golfapp_token.txt") ;

        String strLine="";
        StringBuilder text = new StringBuilder();

        /** Reading contents of the temporary file, if already exists */
        try {
            FileReader fReader = new FileReader(tempFile);
            BufferedReader bReader = new BufferedReader(fReader);

            /** Reading the contents of the file , line by line */
            while( (strLine=bReader.readLine()) != null  ){
                text.append(strLine);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        return text.toString();
    }

    private class PlaceBetsList extends AsyncTask<String, String, String> {

        public PlaceBetsList() {
            pdialog = new ProgressDialog(getActivity());
            pdialog.setCancelable(false);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog.setMessage(getResources().getString(R.string.jap_saving_bets));
            pdialog.show();
            pdialog.setCancelable(false);
            pdialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected String doInBackground(String... strings) {
            byte[] result = null;
            String str = "";
            String token = readtoken();
            HttpClient httpclient = new DefaultHttpClient();
            HttpPut httppost = new HttpPut("http://zoogtech.com/golfapp/public/bet-registration/register/" + mPartyId);
            try {

                List<NameValuePair> json = new ArrayList<NameValuePair>();
                //TODO
                json.add(new BasicNameValuePair("hole_id", mHoleId));
                for(int i = 0; i < mChoosen.size(); i++) {
                    json.add(new BasicNameValuePair("bets["+ i +"][bet_type_id]", mChoosen.get(i).getId()));
                    json.add(new BasicNameValuePair("bets["+ i +"][amount]", mChoosen.get(i).getId()));
                }
                json.add(new BasicNameValuePair("access_token", token));

                httppost.setHeader("Content-type", "application/x-www-form-urlencoded");
//                httppost.setHeader("Authorization", text.toString());
                httppost.setEntity(new UrlEncodedFormEntity(json));

                HttpResponse response = httpclient.execute(httppost);
                StatusLine statusLine = response.getStatusLine();

                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    result = EntityUtils.toByteArray(response.getEntity());
                    str = new String(result, "UTF-8");
                    System.out.println(str);
                    System.out.println("Success!");
                    success = true;
                    retVal = str;
                }else {
                    result = EntityUtils.toByteArray(response.getEntity());
                    str = new String(result, "UTF-8");
                    System.out.println("Failed!");
                    System.out.println(str);
                    retVal = str;
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return str;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pdialog != null && pdialog.isShowing()) {
                pdialog.dismiss();
            }
            getActivity().onBackPressed();
        }
    }


    private class InitLists extends AsyncTask<String, String, String> {
        public InitLists() {
            pdialog = new ProgressDialog(getActivity());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pdialog.setMessage(getResources().getString(R.string.);
            pdialog.setMessage(getResources().getString(R.string.jap_getting_bets));
            pdialog.show();
            pdialog.setCancelable(false);
            pdialog.setCanceledOnTouchOutside(false);
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
                    System.out.print(bets_string);
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
                            BetSetting addThis = new BetSetting(row.getString("id"), "0" ,row.getString("name"), row.getString("description"), false);
                            Boolean willAddThis = true;
                            for(int a  = 0; a < mInitialItems.size(); a++){
                                if(addThis.getName().equals(mInitialItems.get(a).getName())) {
                                    willAddThis = false;
                                }
                            }
                            if(willAddThis){
                                mItems.add(addThis);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    lvBetSettings = (ListView) view_container.findViewById(R.id.lvBets);
                    adapter = new BetChooserAdapter(getActivity(), 0, mItems);
                    lvBetSettings.setAdapter(adapter);
                    lvBetSettings.setFocusable(true);

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
        STATICsettingsCount = mInitialItems.size();
        tvReturn = (TextView) view_container.findViewById(R.id.tvSaveReturn);
        tvReturn.setText("保存");
        tvReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SAVE ALL CHOOSEN BET SETTINGS TO HOLE RECORD
                mChoosen = new ArrayList<BetSetting>();

                for(int i = 0; i < mItems.size(); i++){
                    if(mItems.get(i).isChosen()){
                        mChoosen.add(mItems.get(i));
                    }
                }
                PlaceBetsList send = new PlaceBetsList();
                send.execute();
//                Toast.makeText(getActivity(), "" + mChoosen.get(0).getId(), Toast.LENGTH_LONG).show();
//                getActivity().onBackPressed();
            }
        });

        lvBetSettings = (ListView) view.findViewById(R.id.lvBets);
        adapter = new BetChooserAdapter(getActivity(), 0, mItems);
        lvBetSettings.setAdapter(adapter);
        lvBetSettings.setFocusable(true);
    }

}
