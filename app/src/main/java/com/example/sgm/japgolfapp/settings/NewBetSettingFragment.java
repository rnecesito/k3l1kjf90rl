package com.example.sgm.japgolfapp.settings;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sgm.japgolfapp.BaseFragment;
import com.example.sgm.japgolfapp.R;
import com.example.sgm.japgolfapp.models.BetSetting;
import com.example.sgm.japgolfapp.models.HoleRecord;
import com.example.sgm.japgolfapp.settings.adapter.BetSettingAdapter;

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

public class NewBetSettingFragment extends BaseFragment{

    private ListView lvHoldeRecords;
    public static ArrayList<HoleRecord> mItems;
    BetSettingAdapter mAdapter;
    TextView tvSet;
    String partyId = "";
    View view_container;
    boolean shown = false;
    private ProgressDialog pdialog;
    private boolean success = false;
    String retVal;

    public void setPartyId(String partyId){
        this.partyId = partyId;
    }

    @OnClick(R.id.logout_button)
    public void click_logout() {
        logout();
    }

    @OnClick(R.id.menu_button)
    public void showMenu() {
        shown = sidemenu(view_container, shown);
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

        TextView txt = (TextView) view.findViewById(R.id.tvSaveReturn);
        txt.setVisibility(TextView.INVISIBLE);
        getPartyPlayGroupList init = new getPartyPlayGroupList();
        init.execute();
    }


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

    private class getPartyPlayGroupList extends AsyncTask<String, String, String> {

        public getPartyPlayGroupList() {
            pdialog = new ProgressDialog(getActivity());
            pdialog.setCancelable(false);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog.setMessage(getResources().getString(R.string.jap_getting_holes));
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
            HttpGet httppost = new HttpGet("http://zoogtech.com/golfapp/public/bet-registration/bets/"+ partyId +"?access_token="+token.toString());
            try {
                HttpResponse response = httpclient.execute(httppost);
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    result = EntityUtils.toByteArray(response.getEntity());
                    str = new String(result, HTTP.UTF_8);
                    success = true;
                    retVal = str;
                }else {
                    result = EntityUtils.toByteArray(response.getEntity());
                    str = new String(result, HTTP.UTF_8);
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
            if(pdialog != null && pdialog.isShowing()) {
                pdialog.dismiss();
            }
            if(success) {
                JSONObject info = null;
                try {
                    info = new JSONObject(retVal);

                    JSONArray hole = info.getJSONObject("course").getJSONArray("hole_items");
                    lvHoldeRecords = (ListView) view_container.findViewById(R.id.lvBets);

                    tvSet = (TextView) view_container.findViewById(R.id.tvSaveReturn);
                    tvSet.setText(getResources().getString(R.string.save));

                    mItems = new ArrayList<HoleRecord>();

                    for(int i = 0 ; i < hole.length() ; i++){
                        ArrayList<BetSetting> holeBetSettings = new ArrayList<BetSetting>();
                        JSONArray jsonHoleBetSettings = hole.getJSONObject(i).getJSONArray("bets");
                        for(int a = 0 ; a < jsonHoleBetSettings.length() ; a++){
                            JSONObject obj = jsonHoleBetSettings.getJSONObject(a);
                            holeBetSettings.add(new BetSetting(obj.getString("bet_type_id")
                                    ,obj.getString("amount")
                                    ,obj.getJSONObject("bet_type").getString("name")
                                    ,obj.getJSONObject("bet_type").getString("description")
                                    ,true ));
                        }
//                        HoleRecord newHoleRecord = new HoleRecord(hole.getJSONObject(i).getString("id"), hole.getJSONObject(i).getString("hole_number"), null, holeBetSettings);
                        HoleRecord newHoleRecord = new HoleRecord(hole.getJSONObject(i).getString("id"), "" + (i + 1), null, holeBetSettings);
                        mItems.add(newHoleRecord);
                    }


                    // -----------
                    mAdapter = new BetSettingAdapter(getActivity(), 0, mItems);
                    lvHoldeRecords.setAdapter(mAdapter);
                    lvHoldeRecords.setFocusable(true);
                    lvHoldeRecords.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            BetSettingChooserFragment targetFragment = new BetSettingChooserFragment();
                            targetFragment.setItems(mItems.get(i).getBetSettings());
                            targetFragment.setPartyId(partyId);
                            targetFragment.setHoleId(mItems.get(i).getId());
                            showFragmentAndAddToBackStack(targetFragment);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
