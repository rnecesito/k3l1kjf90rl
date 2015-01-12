package com.example.sgm.japgolfapp.scoreregistration;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sgm.japgolfapp.BaseFragment;
import com.example.sgm.japgolfapp.R;
import com.example.sgm.japgolfapp.counting.BetCountingFragment;
import com.example.sgm.japgolfapp.counting.CompetitionCountingFragment;
import com.example.sgm.japgolfapp.counting.ScoreCountingFragment;
import com.example.sgm.japgolfapp.history.PlayHistoryFragment;
import com.example.sgm.japgolfapp.models.CompetitorCompact;
import com.example.sgm.japgolfapp.models.HoleRecordCompact;
import com.example.sgm.japgolfapp.models.Party;
import com.example.sgm.japgolfapp.scoreregistration.adapters.ScoreRegistrationAdapter;
import com.example.sgm.japgolfapp.settings.MenuSettingsFragment;

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

    @OnClick(R.id.menu_button)
    public void showMenu() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        RelativeLayout rl = (RelativeLayout) view_container.findViewById(R.id.new_registration_main);
        View item = inflater.inflate(R.layout.side_menu, rl, false);
        if (!shown) {
            item.setTag("side_menu_tag");
            rl.addView(item);
            SlideToRight(item);

            Button settingButton = (Button)item.findViewById(R.id.settingButton);
            settingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFragmentAndAddToBackStack(new MenuSettingsFragment());
                }
            });
            ImageButton iSettingButton = (ImageButton)item.findViewById(R.id.imageSettingButton);
            iSettingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFragmentAndAddToBackStack(new MenuSettingsFragment());
                }
            });

            Button historyButton = (Button)item.findViewById(R.id.historyButton);
            historyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFragmentAndAddToBackStack(new PlayHistoryFragment());
                }
            });

            ImageView iHButton = (ImageView)item.findViewById(R.id.imageHistoryButton);
            iHButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFragmentAndAddToBackStack(new PlayHistoryFragment());
                }
            });

            Button scoreRegistrationButton = (Button)item.findViewById(R.id.scoreRegistrationButton);
            scoreRegistrationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFragmentAndAddToBackStack(new ScoreRegistrationChooseFragment());
                }
            });
            ImageButton iSButton = (ImageButton)item.findViewById(R.id.imageScoreRegistrationButton);
            iSButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFragmentAndAddToBackStack(new ScoreRegistrationChooseFragment());
                }
            });

            Button countingButton= (Button)item.findViewById(R.id.countingButton);
            countingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    countingButton(view_container);
//                showFragmentAndAddToBackStack(new MenuSettingsFragment());
                }
            });


            ImageButton iCButton = (ImageButton)item.findViewById(R.id.imageButton3);
            iCButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    countingButton(view_container);
                }
            });
//                showFragmentAndAddToBackStack(new MenuSettingsFragment());
            shown = true;
        } else {
            item = view_container.findViewWithTag("side_menu_tag");
            SlideToLeft(item);
            rl.removeView(item);
            shown = false;
        }

        Button countingButton= (Button)item.findViewById(R.id.countingButton);
        countingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                RelativeLayout rl = (RelativeLayout) view_container.findViewById(R.id.new_registration_main);
                final View item = inflater.inflate(R.layout.counting_sub_menu, rl, false);
                item.setTag("counting_sub_menu");
                View tagged = view_container.findViewWithTag("counting_sub_menu");
                if(tagged == null) {
                    rl.addView(item);
                }
                Button scoreCountingButton = (Button)item.findViewById(R.id.scoreCounting);
                scoreCountingButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showFragmentAndAddToBackStack(new ScoreCountingFragment());
                    }
                });

                Button betCountingButton = (Button)item.findViewById(R.id.betCounting);
                betCountingButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showFragmentAndAddToBackStack(new BetCountingFragment());
                    }
                });

                Button competitionCountingButton = (Button)item.findViewById(R.id.competitionCounting);
                competitionCountingButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showFragmentAndAddToBackStack(new CompetitionCountingFragment());
                    }
                });

            }
        });
    }

    @OnClick(R.id.logout_button)
    public void logout() {
        SharedPreferences prefs = getActivity().getSharedPreferences(
                "com.example.app", Context.MODE_PRIVATE);

        String hasLoggedIn = "com.example.app.hasloggedin";
        prefs.edit().putBoolean(hasLoggedIn, false).apply();

        clearBackStack();
    }

    public void SlideToRight(View view) {
        Animation slide = null;
        slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -5.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f);

        slide.setDuration(400);
        slide.setFillAfter(true);
        slide.setFillEnabled(true);
        view.startAnimation(slide);

        slide.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

        });

    }

    public void SlideToLeft(View view) {
        Animation slide = null;
        slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, -5.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f);

        slide.setDuration(400);
        slide.setFillAfter(true);
        slide.setFillEnabled(true);
        view.startAnimation(slide);

        slide.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

        });

    }

    private void countingButton(View view_container) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        RelativeLayout rl = (RelativeLayout) view_container.findViewById(R.id.new_registration_main);
        final View item = inflater.inflate(R.layout.counting_sub_menu, rl, false);
        item.setTag("counting_sub_menu");
        View tagged = view_container.findViewWithTag("counting_sub_menu");
        if(tagged == null) {
            rl.addView(item);
        }
        Button scoreCountingButton = (Button)item.findViewById(R.id.scoreCounting);
        scoreCountingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragmentAndAddToBackStack(new ScoreCountingFragment());
            }
        });

        Button betCountingButton = (Button)item.findViewById(R.id.betCounting);
        betCountingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragmentAndAddToBackStack(new BetCountingFragment());
            }
        });

        Button competitionCountingButton = (Button)item.findViewById(R.id.competitionCounting);
        competitionCountingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragmentAndAddToBackStack(new CompetitionCountingFragment());
            }
        });
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
        }

        @Override
        protected String doInBackground(String... strings) {
            byte[] result = null;
            String str = "";
            String token = readtoken();
            HttpClient httpclient = new DefaultHttpClient();
            HttpPut httppost = new HttpPut("http://zoogtech.com/golfapp/public/score-registration/closed-competition/" + partyInformation.getId());
            try {

                ArrayList<CompetitorCompact> toSaveData = mItems.get(mHoleNumber).getCompetitors();
                List<NameValuePair> json = new ArrayList<NameValuePair>();
                //TODO
                json.add(new BasicNameValuePair("hole_id", mItems.get(mHoleNumber).getId()));
                for(int i = 0; i < toSaveData.size(); i++) {
                    json.add(new BasicNameValuePair("scores["+ i +"][closed_competition_competitor_id]", toSaveData.get(i).getId()));
                    json.add(new BasicNameValuePair("scores["+ i +"][score]", toSaveData.get(i).getScore()));
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
        }
    }

}
