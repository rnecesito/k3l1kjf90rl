package com.example.sgm.japgolfapp.Competition;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.sgm.japgolfapp.R;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CreateClosedCompetitionFragment extends Fragment {
    View view_container;
    private boolean success = false;
    private ProgressDialog pdialog;
    String courses_json;
    TextView date_view;
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
            pdialog.setMessage(getResources().getString(R.string.jap_loading_courses_players));
//            pdialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            byte[] result_byte = null;
            String result_string = "";
            success = false;
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
            HttpGet httpget = new HttpGet("http://zoogtech.com/golfapp/public/course?access_token="+golfapp_token.toString());

            try {
                HttpResponse response = httpclient.execute(httpget);
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    result_byte = EntityUtils.toByteArray(response.getEntity());
                    result_string = new String(result_byte, "UTF-8");
                    courses_json = result_string;
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

            return result_string;
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
                Spinner spinner = (Spinner) view_container.findViewById(R.id.closed_courses_spinner);
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

//                Toast.makeText(getActivity(),getResources().getString(R.string.jap_loading_complete), Toast.LENGTH_SHORT).show();
            } else {
//                Toast.makeText(getActivity(), getResources().getString(R.string.jap_something_wrong), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class CompetitionCreate extends AsyncTask<String, String, String> {
        StringBuilder text = new StringBuilder();
        public CompetitionCreate() {
            pdialog = new ProgressDialog(getActivity());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog.setMessage(getResources().getString(R.string.jap_creating_competition));
            pdialog.show();
            pdialog.setCancelable(false);
            pdialog.setCanceledOnTouchOutside(false);

            File cDir = getActivity().getCacheDir();
            File tempFile = new File(cDir.getPath() + "/" + "golfapp_token.txt") ;

            String strLine="";

            try {
                FileReader fReader = new FileReader(tempFile);
                BufferedReader bReader = new BufferedReader(fReader);
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
            HttpPost httppost = new HttpPost("http://zoogtech.com/golfapp/public/closed-competition");

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
                    success = true;
                    retVal = str;
                }else {
                    result = EntityUtils.toByteArray(response.getEntity());
                    str = new String(result, "UTF-8");
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
                Toast.makeText(getActivity(),getResources().getString(R.string.jap_competition_created), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.jap_something_wrong), Toast.LENGTH_SHORT).show();
            }
        }
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_create_closed_competition, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

        view_container = view;
        new InitLists().execute();

        Button b = (Button) view.findViewById(R.id.closed_save_btn);
        date_view = (TextView) view.findViewById(R.id.closed_competition_date);
        ImageView iv = (ImageView) view.findViewById(R.id.closed_set_date);
        iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("DatePicker", "True");
                Calendar c = Calendar.getInstance();
                final int mYear = c.get(Calendar.YEAR);
                final int mMonth = c.get(Calendar.MONTH);
                final int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(getActivity(), new mDateSetListener(), mYear, mMonth, mDay);
                dialog.show();
            }
        });

        final EditText party_name = (EditText) view.findViewById(R.id.closed_competition_name);
        final Spinner course_name = (Spinner) view.findViewById(R.id.closed_courses_spinner);
        b.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String pn_val = party_name.getText().toString();
                Courses c = (Courses) course_name.getSelectedItem();
                int course_id = c.id;
                String p_date = date_view.getText().toString();
                if(pn_val.matches("")) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.jap_enter_competition_name), Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (p_date.matches("") || p_date.matches("0000-00-00") || p_date.matches("MM/dd/yy")) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.jap_enter_valid_date), Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        new CompetitionCreate().execute(pn_val, p_date, course_id + "");
                    }
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
}
