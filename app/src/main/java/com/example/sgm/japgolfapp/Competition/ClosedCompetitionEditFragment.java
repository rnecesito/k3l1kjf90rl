package com.example.sgm.japgolfapp.Competition;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sgm.japgolfapp.R;
import com.example.sgm.japgolfapp.R.id;

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
import java.net.HttpURLConnection;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ClosedCompetitionEditFragment extends Fragment {
    View view_container;
    private boolean success = false;
    private ProgressDialog pdialog;
    String courses_json;
    String comp_info_json;
    TextView date_view;
    TableLayout competitor_list;
    String retVal;

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

    private class InitLists extends AsyncTask<String, String, String> {
        public InitLists() {
            pdialog = new ProgressDialog(getActivity());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog.setMessage("Loading courses and players...");
            pdialog.show();
            pdialog.setCancelable(false);
            pdialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected String doInBackground(String... strings) {
            byte[] result = null;
            String result2 = "";

            byte[] result_2 = null;
            String result2_2 = "";

            /** Getting Cache Directory */
            File cDir = getActivity().getCacheDir();

            /** Getting a reference to temporary file, if created earlier */
            File tempFile = new File(cDir.getPath() + "/" + "golfapp_token.txt") ;

            String strLine="";
            StringBuilder golfapp_token = new StringBuilder();

            /** Reading contents of the temporary file, if already exists */
            try {
                FileReader fReader = new FileReader(tempFile);
                BufferedReader bReader = new BufferedReader(fReader);

                /** Reading the contents of the file , line by line */
                while( (strLine=bReader.readLine()) != null  ){
                    golfapp_token.append(strLine);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }

            /** Getting Cache Directory */
            File cDir2 = getActivity().getCacheDir();

            /** Getting a reference to temporary file, if created earlier */
            File tempFile2 = new File(cDir2.getPath() + "/" + "competition_number.txt") ;

            String strLine2="";
            StringBuilder comp_number = new StringBuilder();

            /** Reading contents of the temporary file, if already exists */
            try {
                FileReader fReader = new FileReader(tempFile2);
                BufferedReader bReader = new BufferedReader(fReader);

                /** Reading the contents of the file , line by line */
                while( (strLine2=bReader.readLine()) != null  ){
                    comp_number.append(strLine2);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }

            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httppost = new HttpGet("http://zoogtech.com/golfapp/public/course?access_token="+golfapp_token.toString());

            HttpClient httpclient_2 = new DefaultHttpClient();
            HttpGet httppost_2 = new HttpGet("http://zoogtech.com/golfapp/public/closed-competition/"+comp_number.toString()+"?access_token="+golfapp_token.toString());

            try {
                HttpResponse response = httpclient.execute(httppost);
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    result = EntityUtils.toByteArray(response.getEntity());
                    result2 = new String(result, "UTF-8");
                    System.out.println("Success!");
                    courses_json = result2;
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

            try {
                HttpResponse response = httpclient_2.execute(httppost_2);
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    result_2 = EntityUtils.toByteArray(response.getEntity());
                    result2_2 = new String(result_2, "UTF-8");
                    System.out.println("Success!");
                    comp_info_json = result2_2;
                    success = true;
                }else {
                    result_2 = EntityUtils.toByteArray(response.getEntity());
                    result2_2 = new String(result_2, "UTF-8");
                    System.out.println("Failed!");
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
                    array = new JSONArray(courses_json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Spinner spinner = (Spinner) view_container.findViewById(id.courses_spinner);
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

                ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, course_list);
                spinner.setAdapter(spinnerArrayAdapter);

                JSONObject comp_info = null;
                try {
                    comp_info = new JSONObject(comp_info_json);
                    int cnt2 = 0;
                    int index2 = 0;
                    for (Courses c : course_list) {
                        cnt2++;
                        if(c.id == Integer.parseInt(comp_info.getString("course_id"))) {
                            index2 = cnt2 - 1;
                        }
                    }
                    spinner.setSelection(index2);
                    final EditText comp_name = (EditText) view_container.findViewById(id.competition_name);
                    comp_name.setText(comp_info.getString("name"));
                    date_view.setText(comp_info.getString("date"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Toast.makeText(getActivity(), getResources().getString(R.string.jap_loading_complete), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.jap_something_wrong), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class CompetitionUpdate extends AsyncTask<String, String, String> {
        StringBuilder text = new StringBuilder();
        public CompetitionUpdate() {
            pdialog = new ProgressDialog(getActivity());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog.setMessage(getResources().getString(R.string.jap_updating_competition));
            pdialog.show();
            pdialog.setCancelable(false);
            pdialog.setCanceledOnTouchOutside(false);
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
            HttpPut httppost = new HttpPut("http://zoogtech.com/golfapp/public/closed-competition");

            try {
                List<NameValuePair> json = new ArrayList<NameValuePair>();
                json.add(new BasicNameValuePair("name", pname));
                json.add(new BasicNameValuePair("date", date));
                json.add(new BasicNameValuePair("course_id", course));

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
                Toast.makeText(getActivity(), getResources().getString(R.string.jap_competition_updated), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.jap_something_wrong), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater
                .inflate(R.layout.fragment_edit_closed_competition, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view_container = view;
       // competitor_list = (TableLayout) view.findViewById(id.competitors);
        new InitLists().execute();

        Button b = (Button) view.findViewById(id.comp_update_btn);
        final EditText party_name = (EditText) view.findViewById(id.competition_name);
        final Spinner course_name = (Spinner) view.findViewById(id.courses_spinner);
        date_view = (TextView) view.findViewById(id.competition_date);
        ImageView iv = (ImageView) view.findViewById(id.set_date);
        iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                        new mDateSetListener(), mYear, mMonth, mDay);
                dialog.show();
            }
        });

        b.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String pn_val = party_name.getText().toString();
                Courses c = (Courses) course_name.getSelectedItem();
                int course_id = c.id;
                String p_date = date_view.getText().toString();
                if(pn_val.matches("")) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.jap_enter_competition_name), Toast.LENGTH_SHORT).show();
                    return;
                } else if (p_date.matches("") || p_date.matches("0000-00-00") || p_date.matches("MM/dd/yy")) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.jap_enter_valid_date), Toast.LENGTH_SHORT).show();
                } else {
                    new CompetitionUpdate().execute(pn_val, p_date, course_id+"");
                }
            }
        });
    }

    class mDateSetListener implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            int mYear = year;
            int mMonth = monthOfYear;
            int mDay = dayOfMonth;

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date today = new Date();
                Date comp_date = sdf.parse(String.valueOf(new StringBuilder()
                        .append(new DecimalFormat("00").format(mYear)).append("-")
                        .append(new DecimalFormat("00").format(mMonth + 1)).append("-")
                        .append(new DecimalFormat("00").format(mDay))));
                if (comp_date.compareTo(today) > 0){
                    Toast.makeText(getActivity(),getResources().getString(R.string.jap_enter_valid_date), Toast.LENGTH_SHORT).show();
                    date_view.setText("");
                } else {
                    date_view.setText(new StringBuilder()
                            // Month is 0 based so add 1
                            .append(new DecimalFormat("00").format(mYear)).append("-")
                            .append(new DecimalFormat("00").format(mMonth + 1)).append("-")
                            .append(new DecimalFormat("00").format(mDay)));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private OnClickListener btnListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            TableRow row = (TableRow) view.getParent().getParent();
            TableLayout parent = (TableLayout) row.getParent();
            parent.removeView(row);
        }
    };
}
