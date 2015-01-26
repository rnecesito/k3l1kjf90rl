package com.example.sgm.japgolfapp.registration;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sgm.japgolfapp.BaseFragment;
import com.example.sgm.japgolfapp.R;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;


public class NewRegistrationFragment extends BaseFragment{
    private String retVal = "";
    private ProgressDialog pdialog;
    private boolean success = false;
    View view_container;

    private class RegisterCall extends AsyncTask<String, String, String> {

        public RegisterCall() {
            pdialog = new ProgressDialog(getActivity());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog.setMessage(getResources().getString(R.string.jap_registering));
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
            HttpPost httppost = new HttpPost("http://zoogtech.com/golfapp/public/register");
            try {
                List<NameValuePair> json = new ArrayList<NameValuePair>();
                json.add(new BasicNameValuePair("firstname", fname));
                json.add(new BasicNameValuePair("lastname", lname));
                json.add(new BasicNameValuePair("email", email));
                json.add(new BasicNameValuePair("password", pass));
                json.add(new BasicNameValuePair("gender", gender));
                json.add(new BasicNameValuePair("handicap", handicap));

                httppost.setHeader("Content-type", "application/x-www-form-urlencoded");
                httppost.setEntity(new UrlEncodedFormEntity(json, HTTP.UTF_8));

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
                    System.out.println(str);
                    retVal = str;
                }
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
                Toast.makeText(getContext(), getResources().getString(R.string.jap_reg_success), Toast.LENGTH_LONG).show();
                showFragment(new MainMenuFragment());
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
                        popBackStack();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @OnClick(R.id.saveB)
    public void register() {
        final EditText fname = (EditText) view_container.findViewById(R.id.cName1);
        final EditText email = (EditText) view_container.findViewById(R.id.courseSpinner);
        final EditText pass = (EditText) view_container.findViewById(R.id.editTextPassword);
            String fname_val = fname.getText().toString();
            String email_val = email.getText().toString();
            String pass_val = pass.getText().toString();

//            if(email_val.matches("")) {
//                    Toast.makeText(getContext(), getResources().getString(R.string.jap_enter_email), Toast.LENGTH_SHORT).show();
//                return;
//            } else if(fname_val.matches("")) {
//                Toast.makeText(getContext(), getResources().getString(R.string.jap_enter_firstname), Toast.LENGTH_SHORT).show();
//            return;
//            } else if(pass_val.matches("")) {
//                Toast.makeText(getContext(), getResources().getString(R.string.jap_enter_pass), Toast.LENGTH_SHORT).show();
//                return;
//            } else {
                validate();
                System.out.println(retVal);
//            }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_registration, container, false);
    }
    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view_container = view;
    }

    @InjectView(R.id.courseSpinner)
    EditText email;
    @InjectView(R.id.cName1)
    EditText name;
    @InjectView(R.id.editTextPassword)
    EditText password;

    @InjectView(R.id.emailValidateText)
    TextView invalidEmail;
    @InjectView(R.id.nameValidateText)
    TextView invalidName;
    @InjectView(R.id.passwordValidateText)
    TextView invalidPassword;

    public void validate(){
        boolean pass1 = false;
        boolean pass2 = false;
        boolean pass3 = false;
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
            invalidEmail.setVisibility(View.VISIBLE);
            pass1 = false;
        }
        else if(name.getText().toString().isEmpty() ){
            invalidName.setVisibility(View.VISIBLE);
            pass2 = false;
        }
        else if(password.getText().toString().isEmpty()){
            invalidPassword.setVisibility(View.VISIBLE);
            pass3 = false;
        }

        if(android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
            invalidEmail.setVisibility(View.INVISIBLE);
            pass1 = true;
        }
        if(!name.getText().toString().isEmpty()){
            invalidName.setVisibility(View.INVISIBLE);
            pass2 = true;
        }
        if(!password.getText().toString().isEmpty()){
            invalidPassword.setVisibility(View.INVISIBLE);
            pass3 = true;
        }
        if (pass1 && pass2 && pass3) {
            new RegisterCall().execute(name.getText().toString(), "-", "Male", "1", email.getText().toString(), password.getText().toString());
        }

    }
}
