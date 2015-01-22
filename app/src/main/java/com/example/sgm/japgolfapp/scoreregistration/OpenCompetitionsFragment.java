package com.example.sgm.japgolfapp.scoreregistration;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.sgm.japgolfapp.BaseFragment;
import com.example.sgm.japgolfapp.R;
import com.example.sgm.japgolfapp.models.Party;
import com.example.sgm.japgolfapp.scoreregistration.adapters.GroupListAdapter;

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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import butterknife.OnClick;

public class OpenCompetitionsFragment extends BaseFragment{

    private GroupListAdapter mAdapter;
    private ListView lvPartyPlayGroups;
    //SERIOUS DATA
    private ArrayList<Party> mPartyPlayGroups;

    View view_container;
    boolean shown = false;
    private ProgressDialog pdialog;
    private boolean success = false;
    String retVal;

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
        return inflater.inflate(R.layout.fragment_party_play_scoring_list, container, false);
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
            pdialog.setMessage(getResources().getString(R.string.jap_getting_groups));
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
            HttpGet httppost = new HttpGet("http://zoogtech.com/golfapp/public/open-competition?access_token="+token.toString());
            try {
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
            if(pdialog != null && pdialog.isShowing()) {
                pdialog.dismiss();
            }
            if(success) {
                JSONArray info = null;
                try {
                    info = new JSONArray(retVal);
                    mPartyPlayGroups = new ArrayList<Party>();
                    for(int i = 0; i < info.length(); i++){

                        String courseName, holes;
                        try{
                            courseName = info.getJSONObject(i).getJSONObject("course").getString("name");
                            JSONArray holesData = info.getJSONObject(i).getJSONObject("course").getJSONArray("hole_items");
                            holes = "" + holesData.length();
                        }catch (Exception e){
                            courseName = "";
                            holes = "0";
                        }
                        Party newParty = new Party(
                                info.getJSONObject(i).getString("id"),
                                info.getJSONObject(i).getString("name"),
                                info.getJSONObject(i).getString("date"),
                                courseName);
                        newParty.setHoles(holes);
                        mPartyPlayGroups.add(newParty);
                    }

                    mAdapter = new GroupListAdapter(getActivity(),0,mPartyPlayGroups);
                    lvPartyPlayGroups.setAdapter(mAdapter);
                    lvPartyPlayGroups.setFocusable(true);
                    lvPartyPlayGroups.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            ScoreRegistrationFragment targetFragment = new ScoreRegistrationFragment();
                            targetFragment.setScoreRegistrationFragmentParty(mPartyPlayGroups.get(position));
                            showFragmentAndAddToBackStack(targetFragment);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view_container = view;
        lvPartyPlayGroups = (ListView) view.findViewById(R.id.lvPartyPlayGroups);
        //TEST DATAS

        TableRow tableRow = (TableRow) view
                .findViewById(R.id.tr_generic_row);
        tableRow.setBackgroundColor(Color.LTGRAY);

        TextView tvName = (TextView) view
                .findViewById(R.id.tv_generic_column_1);
        tvName.setText(getResources().getString(R.string.jap_party_name));
        TextView tvDate = (TextView) view
                .findViewById(R.id.tv_generic_column_2);
        tvDate.setText(getResources().getString(R.string.date));
        TextView tvCourse = (TextView) view
                .findViewById(R.id.tv_generic_column_3);
        tvCourse.setText(getResources().getString(R.string.jap_course));

        getPartyPlayGroupList init = new getPartyPlayGroupList();
        init.execute();

    }

}
