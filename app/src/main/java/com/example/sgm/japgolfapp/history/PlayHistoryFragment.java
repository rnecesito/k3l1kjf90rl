package com.example.sgm.japgolfapp.history;

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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.sgm.japgolfapp.BaseFragment;
import com.example.sgm.japgolfapp.R;
import com.example.sgm.japgolfapp.counting.BetCountingFragment;
import com.example.sgm.japgolfapp.counting.CompetitionCountingFragment;
import com.example.sgm.japgolfapp.counting.ScoreCountingFragment;
import com.example.sgm.japgolfapp.history.adapters.PlayHistoryAdapter;
import com.example.sgm.japgolfapp.models.Competitor;
import com.example.sgm.japgolfapp.models.PlayHistory;
import com.example.sgm.japgolfapp.scoreregistration.ScoreRegistrationChooseFragment;
import com.example.sgm.japgolfapp.settings.MenuSettingsFragment;

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

    @OnClick(R.id.logout_button)
    public void logout() {
        SharedPreferences prefs = getActivity().getSharedPreferences(
                "com.example.app", Context.MODE_PRIVATE);

        String hasLoggedIn = "com.example.app.hasloggedin";
        prefs.edit().putBoolean(hasLoggedIn, false).apply();

        clearBackStack();
    }

    private void countingButton(View view_container) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        RelativeLayout rl = (RelativeLayout) view_container.findViewById(R.id.new_registration_main);
        final View item = inflater.inflate(R.layout.counting_sub_menu, rl, false);
        item.setTag("counting_sub_menu");
        View tagged = view_container.findViewWithTag("counting_sub_menu");
        if(tagged == null) {
            rl.addView(item);
            SlideToRight(item);
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


    private class InitLists extends AsyncTask<String, String, String> {

        public InitLists() {
            pdialog = new ProgressDialog(getActivity());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog.setMessage(getResources().getString(R.string.jap_loading_parties));
            pdialog.show();
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
                    success = true;
                }else {
                    result_byte = EntityUtils.toByteArray(response.getEntity());
                    result_string = new String(result_byte, "UTF-8");
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
                        JSONArray array2 = null;
//                        try {
//                            array2 = new JSONArray(new GetScores().execute(row.getString("id")).get());
//                            if (array2 != null) {
//                                for (int i2 = 0; i2 < array2.length(); i2++) {
//                                    JSONObject row2 = null;
//                                    try {
//                                        row2 = array2.getJSONObject(i2);
//                                        dummy_one.add(new Competitor(row2.getString("name"), row2.getString("gross"), row2.getString("net"), null));
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        } catch (ExecutionException e) {
//                            e.printStackTrace();
//                        }
                        mItems.add(new PlayHistory(row.getString("id"), row.getString("date"), row.getString("name"), dummy_one));
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

        //TEST DATAS
//        ArrayList<Competitor> dummy_one = new ArrayList<Competitor>();
//        dummy_one.add(new Competitor("Mr. Showa", "01", "01" , null));
//        dummy_one.add(new Competitor("Mr. Chow", "02", "02" , null));
//        dummy_one.add(new Competitor("Mr. Mikado", "03", "03", null ));
//
//        ArrayList<Competitor> dummy_two = new ArrayList<Competitor>();
//        dummy_two.add(new Competitor("Mr. Chow", "02", "02", null ));
//        dummy_two.add(new Competitor("Mr. Mikado", "03", "03" , null));
//
//        ArrayList<Competitor> dummy_three = new ArrayList<Competitor>();
//        dummy_three.add(new Competitor("Mr. Showa", "01", "01", null ));
//        dummy_three.add(new Competitor("Mr. Chow", "02", "02", null ));
//
//        mItems.add(new PlayHistory("MM/DD/2017", "Golf course name", dummy_one));
//        mItems.add(new PlayHistory("MM/DD/2016", "Golf course name", dummy_two));
//        mItems.add(new PlayHistory("MM/DD/2015", "Golf course name", dummy_three));
//        mItems.add(new PlayHistory("MM/DD/2014", "Golf course name", dummy_one));
//        mItems.add(new PlayHistory("MM/DD/2013", "Golf course name", dummy_two));
//        mItems.add(new PlayHistory("MM/DD/2012", "Golf course name", dummy_three));
//        mItems.add(new PlayHistory("MM/DD/2011", "Golf course name", dummy_one));
//        mItems.add(new PlayHistory("MM/DD/2010", "Golf course name", dummy_two));
        // -----------


    }

}
