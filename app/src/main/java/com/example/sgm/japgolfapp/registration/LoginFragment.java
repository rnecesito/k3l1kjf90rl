package com.example.sgm.japgolfapp.registration;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;


public class LoginFragment extends BaseFragment{
    private String retVal = "";
    private ProgressDialog pdialog;
    private boolean success = false;
    View view_container;

    private class LoginCall extends AsyncTask<String, String, String> {

        public LoginCall() {
            pdialog = new ProgressDialog(getActivity());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pdialog.setMessage(getResources().getString(R.string.jap_logging_in));
            pdialog.setMessage("Logging in...");
            pdialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String email = strings[0];
            String password = strings[1];
            byte[] result_byte = null;
            String result_string = "";
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://zoogtech.com/golfapp/public/login");
            try {
                List<NameValuePair> json1 = new ArrayList<NameValuePair>(5);
                json1.add(new BasicNameValuePair("client_id", "GOLFAPP1234"));
                json1.add(new BasicNameValuePair("client_secret", "thequickbrownfox1234"));
                json1.add(new BasicNameValuePair("grant_type", "password"));
                json1.add(new BasicNameValuePair("username", email));
                json1.add(new BasicNameValuePair("password", password));

                httppost.setHeader("Content-type", "application/x-www-form-urlencoded");
                httppost.setEntity(new UrlEncodedFormEntity(json1));
                HttpResponse response = httpclient.execute(httppost);
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpURLConnection.HTTP_OK) {
                    result_byte = EntityUtils.toByteArray(response.getEntity());
                    result_string = new String(result_byte, "UTF-8");
                    System.out.println(result_string);
                    System.out.println("Success!");
                    success = true;
                    retVal = result_string;
                }else {
                    result_byte = EntityUtils.toByteArray(response.getEntity());
                    result_string = new String(result_byte, "UTF-8");
                    System.out.println("Failed!");
                    System.out.println(result_string);
                    System.out.println(json1.toString());
                    retVal = result_string;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return "Yeah";
        }

        @Override
        protected void onPostExecute(String result) {
            final String TEMP_FILE_NAME = "golfapp_token.txt";
            File tempFile;
            super.onPostExecute(result);
            if(pdialog != null && pdialog.isShowing()) {
                pdialog.dismiss();
            }
            if(success) {
                try {
//                    Toast.makeText(getActivity(), getResources().getString(R.string.jap_login_success), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity(), "Login successful.", Toast.LENGTH_SHORT).show();

                    File cDir = getActivity().getCacheDir();
                    tempFile = new File(cDir.getPath() + "/" + TEMP_FILE_NAME);
                    FileWriter writer=null;
                    try {
                        writer = new FileWriter(tempFile);

                        JSONObject user_token = new JSONObject(retVal);
                        writer.write(user_token.getString("access_token"));
                        writer.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                            .replace(R.id.container, new MainMenuFragment())
                            .addToBackStack(null).commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
//                Toast.makeText(getActivity(), getResources().getString(R.string.jap_invalid_login), Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(), "Invalid username/password.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @OnClick(R.id.login)
    public void login() {
        Button login = (Button) view_container.findViewById(R.id.login);
        final EditText un = (EditText) view_container.findViewById(R.id.login_email);
        final EditText pw = (EditText) view_container.findViewById(R.id.login_password);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

//                showFragment(new MainMenuFragment());

                String email_val = un.getText().toString();
                String pass_val = pw.getText().toString();
                if(email_val.matches("")) {
//                    Toast.makeText(getActivity(), getResources().getString(R.string.jap_enter_email), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity(), "Please enter email", Toast.LENGTH_SHORT).show();
                    return;
                } else if (pass_val.matches("")) {
                    Toast.makeText(getActivity(), "Please enter password", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    new LoginCall().execute(email_val, pass_val);
                    System.out.println(retVal);

                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view_container = view;
    }
}
