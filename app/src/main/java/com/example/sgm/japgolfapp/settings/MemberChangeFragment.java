package com.example.sgm.japgolfapp.settings;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sgm.japgolfapp.BaseFragment;
import com.example.sgm.japgolfapp.R;

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
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

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
            pdialog.setMessage(getResources().getString(R.string.information_loaded));
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
            HttpGet httppost = new HttpGet("http://zoogtech.com/golfapp/public/user/profile?access_token="+token.toString());
            try {
                HttpResponse response = httpclient.execute(httppost);
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    result = EntityUtils.toByteArray(response.getEntity());
                    str = new String(result, HTTP.UTF_8);
                    System.out.println(str);
                    System.out.println("Success!");
                    success = true;
                    retVal = str;
                }else {
                    result = EntityUtils.toByteArray(response.getEntity());
                    str = new String(result, HTTP.UTF_8);
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

    private class UpdateCall extends AsyncTask<String, String, String> {

        public UpdateCall() {
            pdialog = new ProgressDialog(getActivity());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog.setMessage(getResources().getString(R.string.jap_updating_profile));
            pdialog.show();
            pdialog.setCancelable(false);
            pdialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected String doInBackground(String... strings) {
            String fname = strings[0];
            String lname = strings[1];
            String gender = strings[2];
            String handicap = strings[3];
            String email = strings[4];
            String pass = strings[5];
            byte[] result = null;
            String str = "";
            HttpClient httpclient = new DefaultHttpClient();
            HttpPut httppost = new HttpPut("http://zoogtech.com/golfapp/public/user/profile");
            try {
                List<NameValuePair> json1 = new ArrayList<NameValuePair>(6);
                json1.add(new BasicNameValuePair("firstname", fname));
                json1.add(new BasicNameValuePair("lastname", lname));
                json1.add(new BasicNameValuePair("email", email));
                json1.add(new BasicNameValuePair("gender", gender));
                json1.add(new BasicNameValuePair("handicap", handicap));
                if(!pass.matches("")) {
                    json1.add(new BasicNameValuePair("password", pass));
                }

                httppost.setHeader("Content-type", "application/x-www-form-urlencoded");
                httppost.setHeader("Authorization", readtoken());
                httppost.setEntity(new UrlEncodedFormEntity(json1, HTTP.UTF_8));
                HttpResponse response = httpclient.execute(httppost);
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpURLConnection.HTTP_OK) {
                    result = EntityUtils.toByteArray(response.getEntity());
                    str = new String(result, HTTP.UTF_8);
                    System.out.println(str);
                    System.out.println("Success!");
                    success = true;
                    retVal = str;
                }else {
                    result = EntityUtils.toByteArray(response.getEntity());
                    str = new String(result, HTTP.UTF_8);
                    System.out.println("Failed!");
                    success = false;
                    System.out.println(str);
                    System.out.println(json1.toString());
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
                Toast.makeText(getContext(), getResources().getString(R.string.jap_profile_updated), Toast.LENGTH_LONG).show();
                popBackStack();
            } else {
                JSONObject err;
                JSONArray msg = null;
                try {
                    err = new JSONObject(retVal);
                    msg = new JSONArray(err.getString("email"));
                    if (!msg.getString(0).equals("The email has already been taken.")) {
                        Toast.makeText(getContext(), getResources().getString(R.string.jap_reg_failed), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), getResources().getString(R.string.jap_email_registered), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @OnClick(R.id.logout_button)
    public void click_logout() {
        logout();
    }

    @OnClick(R.id.menu_button)
    public void showMenu() {
        shown = sidemenu(view_container, shown);
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


    @InjectView(R.id.imgMinusB)
    ImageView minus;
    @InjectView(R.id.imgAddB)
    ImageView add;

    @OnClick(R.id.imgMinusB)
    public void minusHandicap() {
        TextView handicap = (TextView) view_container.findViewById(R.id.textViewC1Count);
        handicap.setText((Integer.parseInt(handicap.getText().toString()) - 1)+"");
    }

    @OnClick(R.id.imgAddB)
    public void plusHandicap() {
        TextView handicap = (TextView) view_container.findViewById(R.id.textViewC1Count);
        handicap.setText((Integer.parseInt(handicap.getText().toString()) + 1)+"");
    }

    public void validate(){
        boolean pass1 = false;
        boolean pass2 = false;
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
            invalidEmail.setVisibility(View.VISIBLE);
            pass1 = false;
        }
        else if(name.getText().toString().isEmpty() ){
            invalidName.setVisibility(View.VISIBLE);
            pass2 = false;
        }
//        else if(password.getText().toString().isEmpty()){
//            invalidPassword.setVisibility(View.VISIBLE);
//            return;
//        }

        if(android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
            invalidEmail.setVisibility(View.INVISIBLE);
            pass1 = true;
        }
        if(!name.getText().toString().isEmpty()){
            invalidName.setVisibility(View.INVISIBLE);
            pass2 = true;
        }
//        if(!password.getText().toString().isEmpty()){
//            invalidPassword.setVisibility(View.INVISIBLE);
//            return;
//        }
        if (pass1 && pass2) {
            TextView handicap = (TextView) view_container.findViewById(R.id.textViewC1Count);
            new UpdateCall().execute(name.getText().toString(), "-", "Male", handicap.getText().toString(), email.getText().toString(), password.getText().toString() );
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
