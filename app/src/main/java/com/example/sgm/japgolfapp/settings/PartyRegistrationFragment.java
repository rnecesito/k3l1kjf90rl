package com.example.sgm.japgolfapp.settings;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sgm.japgolfapp.BaseFragment;
import com.example.sgm.japgolfapp.R;
import com.example.sgm.japgolfapp.counting.BetCountingFragment;
import com.example.sgm.japgolfapp.counting.CompetitionCountingFragment;
import com.example.sgm.japgolfapp.counting.ScoreCountingFragment;
import com.example.sgm.japgolfapp.history.PlayHistoryFragment;
import com.example.sgm.japgolfapp.scoreregistration.ScoreRegistrationChooseFragment;
import com.example.sgm.japgolfapp.scoreregistration.ScoreRegistrationFragment;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
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
import java.net.HttpURLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PartyRegistrationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PartyRegistrationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PartyRegistrationFragment extends BaseFragment{

    boolean shown = false;
    View view_container;
    private boolean success = false;

    String response2;
    String response2_2;
    String user_info_json_string;
    TextView date_view;
    TableLayout competitor_list;
    ArrayList<Players> player_list;
    String retVal;

    private ProgressDialog pdialog;
    private ProgressDialog pdialog2;

    private class Courses {
        int id = 0;
        String course = "";

        public Courses(int _id, String _course) {
            id = _id;
            course = _course;
        }

        public String toString() {
            return course;
        }
    }

    private class Players {
        int id = 0;
        String name = "";

        public Players(int _id, String _name) {
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
            pdialog.setMessage(getResources().getString(R.string.jap_loading_courses_players));
//            pdialog.setMessage("Loading courses and players...");
            pdialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            byte[] result = null;
            String result2 = "";

            byte[] result_2 = null;
            String result2_2 = "";

            byte[] user_info_result = null;
            String user_info_result_string = "";

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

            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httppost = new HttpGet("http://zoogtech.com/golfapp/public/course?access_token="+text.toString());

            try {
                HttpResponse response = httpclient.execute(httppost);
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    result = EntityUtils.toByteArray(response.getEntity());
                    result2 = new String(result, "UTF-8");
                    System.out.println("Success!");
                    response2 = result2;
                    success = true;
                }else {
                    result = EntityUtils.toByteArray(response.getEntity());
                    result2 = new String(result, "UTF-8");
                    System.out.println("Failed!");
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            HttpClient httpclient_2 = new DefaultHttpClient();
            HttpGet httppost_2 = new HttpGet("http://zoogtech.com/golfapp/public/user/all?access_token="+text.toString());

            try {
                HttpResponse response = httpclient_2.execute(httppost_2);
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    result_2 = EntityUtils.toByteArray(response.getEntity());
                    result2_2 = new String(result_2, "UTF-8");
                    System.out.println(result2_2);
                    System.out.println("Success!");
                    response2_2 = result2_2;
                    success = true;
                }else {
                    result_2 = EntityUtils.toByteArray(response.getEntity());
                    result2_2 = new String(result_2, "UTF-8");
                    System.out.println(result2_2);
                    System.out.println("Failed!");
                    success = false;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            HttpClient httpclient_3 = new DefaultHttpClient();
            HttpGet httppost_3 = new HttpGet("http://zoogtech.com/golfapp/public/user/profile?access_token="+text.toString());

            try {
                HttpResponse response = httpclient_3.execute(httppost_3);
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    user_info_result = EntityUtils.toByteArray(response.getEntity());
                    user_info_result_string = new String(user_info_result, "UTF-8");
                    System.out.println(user_info_result_string);
                    System.out.println("Success!");
                    user_info_json_string = user_info_result_string;
                    success = true;
                }else {
                    user_info_result = EntityUtils.toByteArray(response.getEntity());
                    user_info_result_string = new String(user_info_result, "UTF-8");
                    System.out.println(user_info_result_string);
                    System.out.println("Failed!");
                    success = false;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result2;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(pdialog != null && pdialog.isShowing()) {
                pdialog.dismiss();
            }
            if(success) {
                JSONArray array = null;
                try {
                    array = new JSONArray(response2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Spinner spinner = (Spinner) view_container.findViewById(R.id.courseSpinner);
                ArrayList<Courses> course_list = new ArrayList<Courses>();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject row = null;
                    try {
                        row = array.getJSONObject(i);
                        course_list.add(new Courses(Integer.parseInt(row.getString("id")), row.getString("name")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                JSONArray array2 = null;
                try {
                    array2 = new JSONArray(response2_2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ArrayList<Players> player_list_container = new ArrayList<Players>();
                for (int i = 0; i < array2.length(); i++) {
                    JSONObject row = null;
                    try {
                        row = array2.getJSONObject(i);
                        player_list_container.add(new Players(Integer.parseInt(row.getString("id")), row.getString("firstname")+" "+row.getString("lastname")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                player_list = player_list_container;
//                ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, course_list);
                ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(getActivity(), R.layout.spinner_background, course_list);
                spinner.setAdapter(spinnerArrayAdapter);

                Spinner spinner2 = (Spinner) view_container.findViewById(R.id.cName1);
//                ArrayAdapter spinnerArrayAdapter2 = new ArrayAdapter(getActivity(), R.layout.spinner_background, player_list_container);
//                spinner2.setAdapter(spinnerArrayAdapter2);
                Spinner spinner3 = (Spinner) view_container.findViewById(R.id.cName2);
                ArrayAdapter spinnerArrayAdapter3 = new ArrayAdapter(getActivity(), R.layout.spinner_background, player_list_container);
                spinner3.setAdapter(spinnerArrayAdapter3);
                Spinner spinner4 = (Spinner) view_container.findViewById(R.id.cName3);
                ArrayAdapter spinnerArrayAdapter4 = new ArrayAdapter(getActivity(), R.layout.spinner_background, player_list_container);
                spinner4.setAdapter(spinnerArrayAdapter4);
                Spinner spinner5 = (Spinner) view_container.findViewById(R.id.cName4);
                ArrayAdapter spinnerArrayAdapter5 = new ArrayAdapter(getActivity(), R.layout.spinner_background, player_list_container);
                spinner5.setAdapter(spinnerArrayAdapter5);


                int index = 0;
                JSONObject user_info_jo;
                try {
                    ArrayAdapter spinnerArrayAdapter2 = new ArrayAdapter(getActivity(), R.layout.spinner_background, player_list);
                    user_info_jo = new JSONObject(user_info_json_string);
                    spinner2.setAdapter(spinnerArrayAdapter2);
                    for (Players p : player_list) {
                        if (p.id == Integer.parseInt(user_info_jo.getString("id"))) {
                            int pos = spinnerArrayAdapter2.getPosition(p);
                            spinner2.setSelection(pos);
                            spinner2.setClickable(false);
                        }
                        index = index + 1;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Toast.makeText(getContext(), getResources().getString(R.string.jap_loading_complete), Toast.LENGTH_SHORT).show();
//                Toast.makeText(getContext(), "Loading complete.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), getResources().getString(R.string.jap_something_wrong), Toast.LENGTH_SHORT).show();
//                Toast.makeText(getContext(), "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @OnClick(R.id.logout_button)
    public void logout() {
        SharedPreferences prefs = getActivity().getSharedPreferences(
                "com.example.app", Context.MODE_PRIVATE);

        String hasLoggedIn = "com.example.app.hasloggedin";
        prefs.edit().putBoolean(hasLoggedIn, false).apply();

        clearBackStack();
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

    private class PartyCreate extends AsyncTask<String, String, String> {
        StringBuilder text = new StringBuilder();
        public PartyCreate() {
            pdialog = new ProgressDialog(getActivity());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog.setMessage(getResources().getString(R.string.jap_creating_party));
            pdialog.show();
            /** Getting Cache Directory */
            File cDir = getActivity().getCacheDir();

            /** Getting a reference to temporary file, if created earlier */
            File tempFile = new File(cDir.getPath() + "/" + "golfapp_token.txt") ;

            String strLine="";

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
        }

        @Override
        protected String doInBackground(String... strings) {
            String pname = strings[0];
            String date = strings[1];
            String course= strings[2];
            byte[] result = null;
            String str = "";
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://zoogtech.com/golfapp/public/party-play");

            try {
                List<NameValuePair> json = new ArrayList<NameValuePair>();
                json.add(new BasicNameValuePair("name", pname));
                json.add(new BasicNameValuePair("date", date));
                json.add(new BasicNameValuePair("course_id", course));

                Spinner sp1 = (Spinner) view_container.findViewById(R.id.cName1);
                Spinner sp2 = (Spinner) view_container.findViewById(R.id.cName2);
                Spinner sp3 = (Spinner) view_container.findViewById(R.id.cName3);
                Spinner sp4 = (Spinner) view_container.findViewById(R.id.cName4);

                Players p1 = (Players) sp1.getSelectedItem();
                Players p2 = (Players) sp2.getSelectedItem();
                Players p3 = (Players) sp3.getSelectedItem();
                Players p4 = (Players) sp4.getSelectedItem();

                int p1_id = p1.id;
                int p2_id = p2.id;
                int p3_id = p3.id;
                int p4_id = p4.id;

                json.add(new BasicNameValuePair("members[0]", p1_id+""));
                json.add(new BasicNameValuePair("members[1]", p2_id+""));
                json.add(new BasicNameValuePair("members[2]", p3_id+""));
                json.add(new BasicNameValuePair("members[3]", p4_id+""));

                httppost.setHeader("Content-type", "application/x-www-form-urlencoded");
                httppost.setHeader("Authorization", text.toString());
                httppost.setEntity(new UrlEncodedFormEntity(json));

                HttpResponse response = httpclient.execute(httppost);
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpURLConnection.HTTP_OK) {
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
                    System.out.println(new UrlEncodedFormEntity(json).toString());
                    retVal = str;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Yeah";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(pdialog != null && pdialog.isShowing()) {
                pdialog.dismiss();
            }
            if(success) {
                Toast.makeText(getContext(), getResources().getString(R.string.jap_party_created), Toast.LENGTH_SHORT).show();
                popBackStack();
            } else {
                Toast.makeText(getContext(), getResources().getString(R.string.jap_something_wrong), Toast.LENGTH_SHORT).show();
            }
        }
    }


    @OnClick(R.id.imgMinusB)
    public void minus1() {
        TextView score_tv = (TextView) view_container.findViewById(R.id.textViewC1Count);
        int score = Integer.parseInt(score_tv.getText().toString());
        if (score != 0)
            score_tv.setText((score - 1) + "");
    }

    @OnClick(R.id.imgAddB)
    public void add1() {
        TextView score_tv = (TextView) view_container.findViewById(R.id.textViewC1Count);
        int score = Integer.parseInt(score_tv.getText().toString());
        if (score < 64)
            score_tv.setText((score + 1) + "");
    }

    @OnClick(R.id.imgMinusB2)
    public void minus2() {
        TextView score_tv = (TextView) view_container.findViewById(R.id.textViewC2Count);
        int score = Integer.parseInt(score_tv.getText().toString());
        if (score != 0)
            score_tv.setText((score - 1) + "");
    }

    @OnClick(R.id.imgAddB2)
    public void add2() {
        TextView score_tv = (TextView) view_container.findViewById(R.id.textViewC2Count);
        int score = Integer.parseInt(score_tv.getText().toString());
        if (score < 64)
            score_tv.setText((score + 1) + "");
    }

    @OnClick(R.id.imgMinusB3)
    public void minus3() {
        TextView score_tv = (TextView) view_container.findViewById(R.id.textViewC3Count);
        int score = Integer.parseInt(score_tv.getText().toString());
        if (score != 0)
            score_tv.setText((score - 1) + "");
    }

    @OnClick(R.id.imgAddB3)
    public void add3() {
        TextView score_tv = (TextView) view_container.findViewById(R.id.textViewC3Count);
        int score = Integer.parseInt(score_tv.getText().toString());
        if (score < 64)
            score_tv.setText((score + 1) + "");
    }

    @OnClick(R.id.imgMinusB4)
    public void minus4() {
        TextView score_tv = (TextView) view_container.findViewById(R.id.textViewC4Count);
        int score = Integer.parseInt(score_tv.getText().toString());
        if (score != 0)
            score_tv.setText((score - 1) + "");
    }

    @OnClick(R.id.imgAddB4)
    public void add4() {
        TextView score_tv = (TextView) view_container.findViewById(R.id.textViewC4Count);
        int score = Integer.parseInt(score_tv.getText().toString());
        if (score < 64)
            score_tv.setText((score + 1) + "");
    }

    @OnClick(R.id.party_date_value)
    public void setdate() {
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                new mDateSetListener(), mYear, mMonth, mDay);
        dialog.show();
    }

    class mDateSetListener implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            // getCalender();
            int mYear = year;
            int mMonth = monthOfYear;
            int mDay = dayOfMonth;
            date_view.setText(new StringBuilder()
                    // Month is 0 based so add 1
                    .append(new DecimalFormat("00").format(mYear)).append("-")
                    .append(new DecimalFormat("00").format(mMonth + 1)).append("-")
                    .append(new DecimalFormat("00").format(mDay)));
            System.out.println(date_view.getText().toString());

        }
    }

    @OnClick(R.id.saveB)
    public void saveB(){
//        pdialog2 = new ProgressDialog(getActivity());
//        pdialog2.setMessage("Saving...");
//        pdialog2.show();
//
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            public void run() {
//              pdialog2.dismiss();
//              popBackStack();
//            }
//        }, 2000);
        EditText party_name = (EditText) view_container.findViewById(R.id.party_name_value);
        Spinner course_name = (Spinner) view_container.findViewById(R.id.courseSpinner);
        date_view = (TextView) view_container.findViewById(R.id.party_date_value);
        String pn_val = party_name.getText().toString();
        Courses c = (Courses) course_name.getSelectedItem();
        int course_id = c.id;
        String p_date = date_view.getText().toString();
        if(pn_val.matches("")) {
            Toast.makeText(getContext(), getResources().getString(R.string.jap_enter_party_name), Toast.LENGTH_SHORT).show();
            return;
        } else if (p_date.matches("") || p_date.matches("0000-00-00") || p_date.matches("MM/dd/yy")) {
            Toast.makeText(getContext(), getResources().getString(R.string.jap_enter_valid_date), Toast.LENGTH_SHORT).show();
        } else {
            new PartyCreate().execute(pn_val, p_date, course_id+"");
        }
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_party_registration, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view_container = view;
        date_view = (TextView) view_container.findViewById(R.id.party_date_value);
        date_view.setInputType(InputType.TYPE_NULL);
        new InitLists().execute();
    }

}
