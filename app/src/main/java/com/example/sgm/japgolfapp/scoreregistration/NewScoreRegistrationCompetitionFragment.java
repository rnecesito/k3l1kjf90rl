package com.example.sgm.japgolfapp.scoreregistration;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sgm.japgolfapp.BaseFragment;
import com.example.sgm.japgolfapp.R;
import com.example.sgm.japgolfapp.models.CompetitorCompact;
import com.example.sgm.japgolfapp.models.HoleRecordCompact;
import com.example.sgm.japgolfapp.models.Party;
import com.example.sgm.japgolfapp.scoreregistration.adapters.ScoreRegistrationAdapter;

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

public class NewScoreRegistrationCompetitionFragment extends BaseFragment{

    //FOR SCORE REGISTRATION
    private ListView lvCompetitors;
    private ArrayList<HoleRecordCompact> mItems;
    private ScoreRegistrationAdapter mAdapter;
    private Integer mHoleNumber;
    private TextView mBtnBack;
    private TextView saveTv;
    private TextView mBtnForward;
    private ArrayList<CompetitorCompact> mGroupMembers;

    private TextView mTvHoleNumber;
    private Party partyInformation;

    private String closed_competition_id;
    private String closed_competition_group_id;

    View view_container;
    boolean shown = false;
    private ProgressDialog pdialog;
    private boolean success = false;
    String retVal;

    private String mCompetitionId;

    public NewScoreRegistrationCompetitionFragment(){}

    public void setScoreRegistrationFragmentParty(String competitionId, Party party){
        mCompetitionId = competitionId;
        partyInformation = party;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_score_registration_competition, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view_container = view;

        lvCompetitors = (ListView) view_container.findViewById(R.id.lvCompetitors);

        saveTv = (TextView) view_container.findViewById(R.id.saveTv);
        saveTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaceScores saveScore = new PlaceScores();
                saveScore.execute();
            }
        });

        mHoleNumber = 0;
        mTvHoleNumber = (TextView) view.findViewById(R.id.tvHoleNumber);
        mTvHoleNumber.setText("" + (mHoleNumber + 1));

        GetPartyPlayScoresList init = new GetPartyPlayScoresList();
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

    private class GetPartyPlayScoresList extends AsyncTask<String, String, String> {

        public GetPartyPlayScoresList() {
            pdialog = new ProgressDialog(getActivity());
            pdialog.setCancelable(false);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog.setMessage(getResources().getString(R.string.jap_getting_members));
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

            HttpGet httppost = new HttpGet("http://zoogtech.com/golfapp/public/score-registration/closed-competition/"+ mCompetitionId +"?group_id="+partyInformation.getId()+"&access_token="+token.toString());
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

            mItems = new ArrayList<HoleRecordCompact>();

            if(pdialog != null && pdialog.isShowing()) {
                pdialog.dismiss();
            }
            if(success) {
                try {
                    final JSONObject info = new JSONObject(retVal);
                    closed_competition_group_id = info.getString("id");
                    closed_competition_id = info.getString("closed_competition_id");
                    for(int a = 0 ; a < Integer.valueOf(info.getJSONObject("closed_competition").getJSONObject("course").getString("holes")); a++){

                        mGroupMembers = new ArrayList<CompetitorCompact>();

                        for(int i = 0 ; i < info.getJSONArray("competitors").length(); i++) {
                            JSONObject obj = info.getJSONArray("competitors").getJSONObject(i);
                            if(a < obj.getJSONArray("scores").length()){
                            mGroupMembers.add(new CompetitorCompact(obj.getString("id"), obj.getJSONObject("member").getString("firstname") + " " + obj.getJSONObject("member").getString("lastname")
                                    , obj.getJSONArray("scores").getJSONObject(a).getString("score"), obj.getJSONObject("member").getString("handicap")));
                            }else{
                            mGroupMembers.add(new CompetitorCompact(obj.getString("id"), obj.getJSONObject("member").getString("firstname") + " " + obj.getJSONObject("member").getString("lastname")
                                    , "0", obj.getJSONObject("member").getString("handicap")));
                            }
                        }
                        mItems.add(new HoleRecordCompact(info.getJSONObject("closed_competition").getJSONObject("course").getJSONArray("hole_items").getJSONObject(a).getString("id"), info.getString("name"), mGroupMembers));
                    }

                    mAdapter = new ScoreRegistrationAdapter(getActivity(), 0, mItems.get(mHoleNumber).getCompetitors(), mHoleNumber);
                    lvCompetitors.setAdapter(mAdapter);
                    lvCompetitors.setFocusable(true);


                    //ACTIVATE BUTTONS -------------------------------------------------------------

                    mBtnBack = (TextView) view_container.findViewById(R.id.ivBack);
                    mBtnBack.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(mHoleNumber >= 0) {

                                --mHoleNumber;
                                mTvHoleNumber.setText("" + (mHoleNumber + 1));
                                mAdapter = new ScoreRegistrationAdapter(getActivity(), 0,
                                        mItems.get(mHoleNumber).getCompetitors(), mHoleNumber);
                                lvCompetitors.setAdapter(mAdapter);
                                lvCompetitors.setFocusable(true);
                                if(mHoleNumber == 0){
                                    mBtnBack.setVisibility(View.INVISIBLE);
                                }
                                mBtnForward.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                    mBtnForward = (TextView) view_container.findViewById(R.id.ivForward);
                    mBtnForward.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //CAN'T GO OVER HOW MANY HOLES
                            if(mHoleNumber < Integer.valueOf(partyInformation.getHoles()) - 1) {

                                ++mHoleNumber;
                                mTvHoleNumber.setText("" + (mHoleNumber + 1));
                                mAdapter = new ScoreRegistrationAdapter(getActivity(), 0,
                                        mItems.get(mHoleNumber).getCompetitors(), mHoleNumber);
                                lvCompetitors.setAdapter(mAdapter);
                                lvCompetitors.setFocusable(true);

                                if(mHoleNumber == Integer.valueOf(partyInformation.getHoles()) - 1){
                                    mBtnForward.setVisibility(View.INVISIBLE);
                                }
                                mBtnBack.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                    if(Integer.valueOf(partyInformation.getHoles()) > 1){
                        mBtnForward.setVisibility(TextView.VISIBLE);
                    }else{
                        mBtnForward.setVisibility(TextView.INVISIBLE);
                    }

                    //------------------------------------------------------------------------------



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class PlaceScores extends AsyncTask<String, String, String> {

        public PlaceScores() {
            pdialog = new ProgressDialog(getActivity());
            pdialog.setCancelable(false);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog.setMessage(getResources().getString(R.string.jap_saving_scores));
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
            HttpPut httppost = new HttpPut("http://zoogtech.com/golfapp/public/score-registration/closed-competition/" + closed_competition_id);
            try {

                ArrayList<CompetitorCompact> toSaveData = mItems.get(mHoleNumber).getCompetitors();
                List<NameValuePair> json = new ArrayList<NameValuePair>();
                //TODO
                json.add(new BasicNameValuePair("hole_id", mItems.get(mHoleNumber).getId()));
                for(int i = 0; i < toSaveData.size(); i++) {
                    json.add(new BasicNameValuePair("scores["+ i +"][closed_competition_competitor_id]", toSaveData.get(i).getId()));
                    json.add(new BasicNameValuePair("scores["+ i +"][score]", toSaveData.get(i).getScore()));
                }
//                json.add(new BasicNameValuePair("closed_competition_id", closed_competition_id));
                json.add(new BasicNameValuePair("closed_competition_group_id", closed_competition_group_id));
                json.add(new BasicNameValuePair("access_token", token));

                Log.d("CC_ID", closed_competition_id);
                Log.d("CCG_ID", closed_competition_group_id);

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
        }
    }

}
