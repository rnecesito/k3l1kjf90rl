package com.example.sgm.japgolfapp.settings;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sgm.japgolfapp.BaseFragment;
import com.example.sgm.japgolfapp.R;
import com.example.sgm.japgolfapp.counting.BetCountingFragment;
import com.example.sgm.japgolfapp.counting.CompetitionCountingFragment;
import com.example.sgm.japgolfapp.counting.ScoreCountingFragment;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by archie on 12/16/2014.
 */
public class MemberChangeFragment extends BaseFragment{

    View view_container;
    boolean shown = false;
    private ProgressDialog pdialog;
    private boolean success = false;
    String retVal;

    private class GetProfile extends AsyncTask<String, String, String> {

        public GetProfile() {
            pdialog = new ProgressDialog(getActivity());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pdialog.setMessage(getResources().getString(R.string.jap_loading_user_profile));
            pdialog.setMessage("Loading profile...");
            pdialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            byte[] result = null;
            String str = "";
            String token = readtoken();
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httppost = new HttpGet("http://zoogtech.com/golfapp/public/user/profile?access_token="+token.toString());
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
                JSONObject info = null;
                try {
                    info = new JSONObject(retVal);
                    EditText fname = (EditText) view_container.findViewById(R.id.user_name);
//                    EditText lname = (EditText) view_container.findViewById(id.user_lastname);
//                    Spinner gender = (Spinner) view_container.findViewById(id.user_gender);
//                    Spinner hc = (Spinner) view_container.findViewById(id.user_handicap);
                    EditText email = (EditText) view_container.findViewById(R.id.user_email);
//                    EditText passw = (EditText) view_container.findViewById(id.user_pass);
                    fname.setText(info.getString("firstname"));
//                    lname.setText(info.getString("lastname"));
                    email.setText(info.getString("email"));
//                    passw.setText(info.getString("password"), TextView.BufferType.EDITABLE);
//                    if(info.getString("gender").matches("Male") || info.getString("gender").matches("M")) {
//                        gender.setSelection(0);
//                    } else {
//                        gender.setSelection(1);
//                    }
//                    hc.setSelection(Integer.parseInt(info.getString("handicap")) - 1);

                    /** Saving user email for comparison later */
                    final String TEMP_FILE_NAME = "golfapp_user_email.txt";
                    File tempFile;
                    /** Getting Cache Directory */
                    File cDir = getActivity().getCacheDir();
                    /** Getting a reference to temporary file, if created earlier */
                    tempFile = new File(cDir.getPath() + "/" + TEMP_FILE_NAME) ;
                    FileWriter writer=null;
                    try {
                        writer = new FileWriter(tempFile);

                        /** Saving the contents to the file*/
                        writer.write(info.getString("email"));

                        /** Closing the writer object */
                        writer.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    /** End save */

                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                Toast.makeText(getContext(), "Data ", Toast.LENGTH_LONG).show();
            } else {
//                Toast.makeText(getContext(), "Update failed. Please try again.", Toast.LENGTH_LONG).show();
            }
        }
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
            shown = true;
        } else {
            item = view_container.findViewWithTag("side_menu_tag");
            SlideToLeft(item);
            rl.removeView(item);
            shown = false;
        }

        Button countingButton= (Button)view_container.findViewById(R.id.countingButton);
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
//                showFragmentAndAddToBackStack(new MenuSettingsFragment());
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

    @InjectView(R.id.user_email)
    EditText email;
    @InjectView(R.id.user_name)
    EditText name;
    @InjectView(R.id.user_password)
    EditText password;

    @InjectView(R.id.emailValidateText)
    TextView invalidEmail;
    @InjectView(R.id.nameValidateText)
    TextView invalidName;
    @InjectView(R.id.passwordValidateText)
    TextView invalidPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_member_change, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view_container = view;
        new GetProfile().execute();
    }

    @OnClick(R.id.saveB)
    public void saveButton(){
        validate();
    }

    public void validate(){
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
            invalidEmail.setVisibility(View.VISIBLE);
        }
        if(name.getText().toString().isEmpty() ){
            invalidName.setVisibility(View.VISIBLE);
        }
        if(password.getText().toString().isEmpty()){
            invalidPassword.setVisibility(View.VISIBLE);
        }

        if(android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
            invalidEmail.setVisibility(View.INVISIBLE);
        }
        if(!name.getText().toString().isEmpty()){
            invalidName.setVisibility(View.INVISIBLE);
        }
        if(!password.getText().toString().isEmpty()){
            invalidPassword.setVisibility(View.INVISIBLE);
        }
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
}
