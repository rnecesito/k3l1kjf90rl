package com.example.sgm.japgolfapp.Competition;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sgm.japgolfapp.BaseFragment;
import com.example.sgm.japgolfapp.R;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
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

public class JoinClosedCompetitionFragment extends BaseFragment {
    private String retVal = null;
    private ProgressDialog pdialog;
    private boolean success = false;
    String competitions_json_string;
    String groups_json_string;
    TableLayout main_table;
    ArrayList<Groups> group_list;
    ArrayAdapter spinnerArrayAdapter;
    StringBuilder comp_number = new StringBuilder();

    private class Groups {
        int id = 0;
        String group_name = "";

        public Groups(int _id, String _group_name) {
            id = _id;
            group_name = _group_name;
        }

        public String toString() {
            return group_name;
        }
    }

    private class CompetitionView extends AsyncTask<String, String, String> {

        public CompetitionView() {
            pdialog = new ProgressDialog(getActivity());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog.setMessage(getResources().getString(R.string.jap_loading_competition_info));
            pdialog.show();
            pdialog.setCancelable(false);
            pdialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected String doInBackground(String... strings) {
            byte[] result_byte = null;
            String result_string = "";
            byte[] result_byte_groups = null;
            String result_string_groups = "";
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
            HttpGet httppost = new HttpGet("http://zoogtech.com/golfapp/public/closed-competition?access_token="+golfapp_token.toString());

            try {
                HttpResponse response = httpclient.execute(httppost);
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    result_byte = EntityUtils.toByteArray(response.getEntity());
                    result_string = new String(result_byte, HTTP.UTF_8);
                    competitions_json_string = result_string;
                    success = true;
                }else {
                    result_byte = EntityUtils.toByteArray(response.getEntity());
                    result_string = new String(result_byte, HTTP.UTF_8);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            HttpClient httpclient_groups = new DefaultHttpClient();
            HttpGet httppost_groups  = new HttpGet("http://zoogtech.com/golfapp/public/closed-competition/group?access_token="+golfapp_token.toString());

            try {
                HttpResponse response = httpclient_groups.execute(httppost_groups);
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    result_byte_groups = EntityUtils.toByteArray(response.getEntity());
                    result_string_groups = new String(result_byte_groups, HTTP.UTF_8);
                    System.out.println(result_string_groups);
                    groups_json_string = result_string_groups;
                    success = true;
                }else {
                    result_byte_groups = EntityUtils.toByteArray(response.getEntity());
                    result_string_groups = new String(result_byte_groups, HTTP.UTF_8);
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
        protected void onPostExecute(String result2) {
            if(pdialog != null && pdialog.isShowing()) {
                pdialog.dismiss();
            }
            if(success) {
                JSONArray array = null;
                try {
                    array = new JSONArray(competitions_json_string);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < array.length(); i++) {
                    JSONObject row = null;
                    try {
                        row = array.getJSONObject(i);
                        LayoutInflater inflater = LayoutInflater.from(getContext());
                        final View item = inflater.inflate(R.layout.competition_closed_row_join, main_table, false);
                        TextView course_name_col = (TextView) item.findViewById(R.id.name_closed_competition);
                        course_name_col.setText(row.getString("name"));
                        TextView date_col = (TextView) item.findViewById(R.id.date_closed_competition);
                        date_col.setText(row.getString("date"));
                        if(i % 2 == 0) {
                            item.setBackgroundColor(Color.WHITE);
                        } else {
                            item.setBackgroundColor(Color.LTGRAY);
                        }
                        ImageView view_btn = (ImageView) item.findViewById(R.id.join_closed_competition);
                        view_btn.setOnClickListener(join_comp);
                        view_btn.setTag(row.getString("id"));
                        main_table.addView(item);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                JSONArray array2 = null;
                try {
                    array2 = new JSONArray(groups_json_string);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ArrayList<Groups> player_list_container = new ArrayList<Groups>();
                for (int i = 0; i < array2.length(); i++) {
                    JSONObject row = null;
                    try {
                        row = array2.getJSONObject(i);
                        player_list_container.add(new Groups(Integer.parseInt(row.getString("id")), row.getString("name")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                group_list = player_list_container;
                spinnerArrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, group_list);

                Toast.makeText(getContext(), getResources().getString(R.string.information_loaded), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), getResources().getString(R.string.jap_something_wrong), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class GetGroups extends AsyncTask<String, String, String> {

        public GetGroups() {
            pdialog = new ProgressDialog(getActivity());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog.setMessage(getResources().getString(R.string.jap_loading_competition_groups));
            pdialog.show();
            pdialog.setCancelable(false);
            pdialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected String doInBackground(String... strings) {
            String competition_id = strings[0];
            byte[] result_byte = null;
            String result_string = "";
            byte[] result_byte_groups = null;
            String result_string_groups = "";
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

            HttpClient httpclient_groups = new DefaultHttpClient();
            HttpGet httppost_groups  = new HttpGet("http://zoogtech.com/golfapp/public/closed-competition/"+competition_id+"/group?access_token="+golfapp_token.toString());

            try {
                HttpResponse response = httpclient_groups.execute(httppost_groups);
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    result_byte = EntityUtils.toByteArray(response.getEntity());
                    result_string = new String(result_byte, HTTP.UTF_8);
                    System.out.println(result_string);
                    groups_json_string = result_string;
                    success = true;
                }else {
                    result_byte = EntityUtils.toByteArray(response.getEntity());
                    result_string = new String(result_byte, HTTP.UTF_8);
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
        protected void onPostExecute(String result2) {
            if(pdialog != null && pdialog.isShowing()) {
                pdialog.dismiss();
            }
            if(success) {
                JSONArray array2 = null;
                try {
                    array2 = new JSONArray(groups_json_string);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ArrayList<Groups> player_list_container = new ArrayList<Groups>();
                for (int i = 0; i < array2.length(); i++) {
                    JSONObject row = null;
                    try {
                        row = array2.getJSONObject(i);
                        player_list_container.add(new Groups(Integer.parseInt(row.getString("id")), row.getString("name")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                group_list = player_list_container;
                spinnerArrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, group_list);

            } else {
                Toast.makeText(getContext(), getResources().getString(R.string.jap_something_wrong), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class JoinCompetition extends AsyncTask<String, String, String> {
        public JoinCompetition() {
            pdialog = new ProgressDialog(getActivity());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            success = false;
            pdialog.setMessage(getResources().getString(R.string.jap_registering));
            pdialog.show();
            pdialog.setCancelable(false);
            pdialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected String doInBackground(String... strings) {
            String number = strings[0];
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
            HttpPost httppost = new HttpPost("http://zoogtech.com/golfapp/public/open-competition/"+ number +"/join");

            try {
                httppost.setHeader("Content-type", "application/x-www-form-urlencoded");
                httppost.setHeader("Authorization", golfapp_token.toString());
                HttpResponse response = httpclient.execute(httppost);
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    result_2 = EntityUtils.toByteArray(response.getEntity());
                    result2_2 = new String(result_2, HTTP.UTF_8);
                    System.out.println(result2_2);
                    success = true;
                }else {
                    result_2 = EntityUtils.toByteArray(response.getEntity());
                    result2_2 = new String(result_2, HTTP.UTF_8);
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
//                Toast.makeText(getActivity(), "Loading Complete.", Toast.LENGTH_SHORT).show();
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getResources().getString(R.string.jap_reg_success)).setMessage(getResources().getString(R.string.jap_reg_success)).setPositiveButton("OK", dialogClickListener).show();
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.jap_something_wrong), Toast.LENGTH_SHORT).show();
            }
        }
    }


	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_join_closed_competition, container, false);
        main_table = (TableLayout) view.findViewById(R.id.competition_table);

        new CompetitionView().execute();
//        new GetGroups().execute();

        return view;
	}

    private View.OnClickListener join_comp = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

//            final String TEMP_FILE_NAME = "competition_number.txt";
//            File tempFile;
//            File cDir = getActivity().getCacheDir();
//            tempFile = new File(cDir.getPath() + "/" + TEMP_FILE_NAME) ;
//            FileWriter writer=null;
//            try {
//                writer = new FileWriter(tempFile);
//                writer.write(view.getTag()+"");
//                writer.close();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            new GetGroups().execute(view.getTag()+"");
            final Spinner group_spinner = new Spinner(getActivity());
            group_spinner.setAdapter(spinnerArrayAdapter);

            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            Groups sel_group = (Groups) group_spinner.getSelectedItem();
                            int selected_group = sel_group.id;
                            new JoinCompetition().execute(selected_group+"");
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(getResources().getString(R.string.jap_select_group))
                    .setView(group_spinner)
                    .setPositiveButton(getResources().getString(R.string.jap_okay), dialogClickListener)
                    .show();
        }
    };
}
