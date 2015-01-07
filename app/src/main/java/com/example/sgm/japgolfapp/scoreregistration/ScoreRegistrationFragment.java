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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sgm.japgolfapp.BaseFragment;
import com.example.sgm.japgolfapp.R;
import com.example.sgm.japgolfapp.counting.BetCountingFragment;
import com.example.sgm.japgolfapp.counting.CompetitionCountingFragment;
import com.example.sgm.japgolfapp.counting.ScoreCountingFragment;
import com.example.sgm.japgolfapp.history.PlayHistoryFragment;
import com.example.sgm.japgolfapp.models.BetSetting;
import com.example.sgm.japgolfapp.models.Competitor;
import com.example.sgm.japgolfapp.models.HoleRecord;
import com.example.sgm.japgolfapp.models.Party;
import com.example.sgm.japgolfapp.scoreregistration.adapters.BetRegistrationAdapter;
import com.example.sgm.japgolfapp.scoreregistration.adapters.ScoreRegistrationAdapter;
import com.example.sgm.japgolfapp.settings.MenuSettingsFragment;
import com.example.sgm.japgolfapp.settings.NewBetSettingFragment;

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

public class ScoreRegistrationFragment extends BaseFragment{

    //FOR SCORE REGISTRATION
    private ListView lvCompetitors;
    private ArrayList<HoleRecord> mItems;
    private ScoreRegistrationAdapter mAdapter;
    private Integer mHoleNumber;
    private TextView mBtnBack;
    private TextView mBtnForward;
    private ArrayList<Competitor> mGroupMembers;

    //FOR BET REGISTRATION
    private ListView lvCompetitors2;
    private ArrayList<HoleRecord> mItems2;
    private BetRegistrationAdapter mAdapter2;
    private Integer mBetNumber;
    private TextView tvPrev;
    private TextView tvNext;
    private TextView saveTv2;
    private ArrayList<BetSetting> mBetSettings;

    private TextView mTvHoleNumber;
    private TextView tvBetRegistration;
    private FrameLayout container;
    private Party partyInformation;
    private TextView tvBetType;

    public ScoreRegistrationFragment(){}

    public void setScoreRegistrationFragmentParty(Party party){
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_score_registration, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view_container = view;

        tvBetType = (TextView) view_container.findViewById(R.id.tvBetType);
        lvCompetitors = (ListView) view_container.findViewById(R.id.lvCompetitors);
        lvCompetitors2 = (ListView) view_container.findViewById(R.id.lvCompetitors2);
        tvBetRegistration = (TextView)view_container.findViewById(R.id.tvBetRegistration);
        container = (FrameLayout)view_container.findViewById(R.id.bet_registration_container);
        saveTv2 = (TextView)view_container.findViewById(R.id.saveTv2);

        tvBetRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             GetBetsList getbets = new GetBetsList();
             getbets.execute();

            }
        });
        saveTv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                container.setVisibility(View.GONE);
                tvPrev.setVisibility(TextView.VISIBLE);
                tvNext.setVisibility(TextView.VISIBLE);
            }
        });

        mHoleNumber = 0;
        mTvHoleNumber = (TextView) view.findViewById(R.id.tvHoleNumber);
        mTvHoleNumber.setText("" + (mHoleNumber + 1));

        GetPartyPlayScoresList init = new GetPartyPlayScoresList();
        init.execute();

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

    private class GetPartyPlayScoresList extends AsyncTask<String, String, String> {

        public GetPartyPlayScoresList() {
            pdialog = new ProgressDialog(getActivity());
            pdialog.setCancelable(false);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog.setMessage("Getting Members");
            pdialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            byte[] result = null;
            String str = "";
            String token = readtoken();
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httppost = new HttpGet("http://zoogtech.com/golfapp/public/score-registration/scores/"+partyInformation.getId()+"?access_token="+token.toString());
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

            mItems = new ArrayList<HoleRecord>();

            if(pdialog != null && pdialog.isShowing()) {
                pdialog.dismiss();
            }
            if(success) {
                try {
                    final JSONObject info = new JSONObject(retVal);

                    mGroupMembers = new ArrayList<Competitor>();

                    for(int i = 0 ; i < info.getJSONArray("members").length(); i++) {
                        JSONObject obj = info.getJSONArray("members").getJSONObject(i);
                        mGroupMembers.add(new Competitor(obj.getJSONObject("member").getString("firstname") + " " + obj.getJSONObject("member").getString("lastname")
                                , "0"
                                , "0"
                                , new ArrayList<Integer>()));
                    }

                    mItems.add(new HoleRecord(info.getString("id"), mGroupMembers, null));
                    mAdapter = new ScoreRegistrationAdapter(getActivity(), 0, mItems.get(mHoleNumber).getCompetitors(), mHoleNumber);
                    lvCompetitors.setAdapter(mAdapter);
                    lvCompetitors.setFocusable(true);

                    //ACTIVATE BUTTONS -------------------------------------------------------------

                    TextView toBetSettings = (TextView) view_container.findViewById(R.id.textView17);
                    toBetSettings.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            NewBetSettingFragment targetFragment = new NewBetSettingFragment();
                            try {
                                targetFragment.setPartyId(info.getString("id"));
                            }catch (Exception e){

                            }
                            showFragmentAndAddToBackStack(targetFragment);
                        }
                    });

                    mBtnBack = (TextView) view_container.findViewById(R.id.ivBack);
                    mBtnBack.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(mHoleNumber >= 0) {
                                mHoleNumber--;
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
                                mHoleNumber++;
                                if(mHoleNumber >= mItems.size())
                                    //TODO
                                    mItems.add(new HoleRecord("" + mHoleNumber, mGroupMembers, null));
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

                    //------------------------------------------------------------------------------

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class GetBetsList extends AsyncTask<String, String, String> {

        public GetBetsList() {
            pdialog = new ProgressDialog(getActivity());
            pdialog.setCancelable(false);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog.setMessage("Getting Bets");
            pdialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            byte[] result = null;
            String str = "";
            String token = readtoken();
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httppost = new HttpGet("http://zoogtech.com/golfapp/public/bet-registration/bets/"+partyInformation.getId()+"?access_token="+token.toString());
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

            mItems2 = new ArrayList<HoleRecord>();
            mBetNumber = 0;

            if(pdialog != null && pdialog.isShowing()) {
                pdialog.dismiss();
            }
            if(success) {
                try {
                    final JSONObject info = new JSONObject(retVal);

                    mBetSettings = new ArrayList<BetSetting>();

                    JSONArray bets = info.getJSONObject("course").getJSONArray("hole_items").getJSONObject(mHoleNumber).getJSONArray("bets");
                    for(int i = 0 ; i < bets.length(); i++) {
                        JSONObject obj = bets.getJSONObject(i);

                        //TODO INSERT BET SETTING HERE
                        mBetSettings.add(new BetSetting(obj.getString("id"),
                                obj.getString("amount"),
                                obj.getJSONObject("bet_type").getString("name"),
                                obj.getJSONObject("bet_type").getString("description"), false));
                    }

                    mItems2.add(new HoleRecord(info.getString("id"), mGroupMembers, mBetSettings));
                    mAdapter2 = new BetRegistrationAdapter(getActivity(), 0,
                            mItems2.get(mBetNumber).getCompetitors(), mBetNumber);
                    lvCompetitors2.setAdapter(mAdapter2);
                    lvCompetitors2.setFocusable(true);

                    if(mBetSettings.size() != 0) {
                        tvBetType.setText(mBetSettings.get(0).getName());

                        //ACTIVATE BUTTONS FOR NEXT AND PREV -------------------------------------------

                        tvPrev = (TextView) view_container.findViewById(R.id.ivBack2);
                        tvPrev.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (mBetNumber != 0) {
                                    mBetNumber--;
                                    tvBetType.setText(mBetSettings.get(mBetNumber).getName());
                                    mAdapter2 = new BetRegistrationAdapter(getActivity(), 0,
                                            mItems2.get(mBetNumber).getCompetitors(), mBetNumber);
                                    lvCompetitors2.setAdapter(mAdapter2);
                                    lvCompetitors2.setFocusable(true);
                                    if (mBetNumber == 0) {
                                        tvPrev.setVisibility(View.INVISIBLE);
                                    }
                                    tvNext.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                        tvPrev.setVisibility(TextView.INVISIBLE);

                        tvNext = (TextView) view_container.findViewById(R.id.ivForward2);
                        tvNext.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //CHANGE TO HOW MANY BET SETTINGS THERE ARE
                                if (mBetNumber < mBetSettings.size() - 1) {
                                    mBetNumber++;
                                    tvBetType.setText(mBetSettings.get(mBetNumber).getName());
                                    //TODO
                                    if (mBetNumber >= mItems2.size())
                                        mItems2.add(new HoleRecord("" + mHoleNumber, mGroupMembers, mBetSettings));

                                    mAdapter2 = new BetRegistrationAdapter(getActivity(), 0,
                                            mItems2.get(mBetNumber).getCompetitors(), mBetNumber);
                                    lvCompetitors2.setAdapter(mAdapter2);
                                    lvCompetitors2.setFocusable(true);

                                    if (mBetNumber == mBetSettings.size() - 1) {
                                        tvNext.setVisibility(View.INVISIBLE);
                                    }
                                    tvPrev.setVisibility(View.VISIBLE);
                                }
                            }
                        });

                        container.setVisibility(View.VISIBLE);
                        //------------------------------------------------------------------------------
                    }else{
                        Toast.makeText(getActivity(), "There are no bets set for this hole", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //TODO
    private class PlaceScores extends AsyncTask<String, String, String> {

        public PlaceScores() {
            pdialog = new ProgressDialog(getActivity());
            pdialog.setCancelable(false);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog.setMessage("Saving Bets");
            pdialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            byte[] result = null;
            String str = "";
            String token = readtoken();
            HttpClient httpclient = new DefaultHttpClient();
            HttpPut httppost = new HttpPut("http://zoogtech.com/golfapp/public/bet-registration/register/");// + mPartyId);
            try {

                List<NameValuePair> json = new ArrayList<NameValuePair>();
                //TODO
//                json.add(new BasicNameValuePair("hole_id", mHoleId));
//                for(int i = 0; i < mChoosen.size(); i++) {
//                    json.add(new BasicNameValuePair("bets["+ i +"][bet_type_id]", mChoosen.get(i).getId()));
//                    json.add(new BasicNameValuePair("bets["+ i +"][amount]", mChoosen.get(i).getId()));
//                }
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

    //TODO
    private class PlaceBetScores extends AsyncTask<String, String, String> {

        public PlaceBetScores() {
            pdialog = new ProgressDialog(getActivity());
            pdialog.setCancelable(false);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog.setMessage("Saving Bets");
            pdialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            byte[] result = null;
            String str = "";
            String token = readtoken();
            HttpClient httpclient = new DefaultHttpClient();
            HttpPut httppost = new HttpPut("http://zoogtech.com/golfapp/public/bet-registration/register/");// + mPartyId);
            try {

                List<NameValuePair> json = new ArrayList<NameValuePair>();
                //TODO
//                json.add(new BasicNameValuePair("hole_id", mHoleId));
//                for(int i = 0; i < mChoosen.size(); i++) {
//                    json.add(new BasicNameValuePair("bets["+ i +"][bet_type_id]", mChoosen.get(i).getId()));
//                    json.add(new BasicNameValuePair("bets["+ i +"][amount]", mChoosen.get(i).getId()));
//                }
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
}
