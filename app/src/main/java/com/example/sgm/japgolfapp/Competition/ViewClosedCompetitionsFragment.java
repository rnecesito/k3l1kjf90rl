package com.example.sgm.japgolfapp.Competition;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import org.apache.http.impl.client.DefaultHttpClient;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ViewClosedCompetitionsFragment extends BaseFragment {
    private ProgressDialog pdialog;
    private boolean success = false;
    String competitions_json;
    TableLayout main_table;

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
            HttpGet httpget = new HttpGet("http://zoogtech.com/golfapp/public/closed-competition?access_token="+golfapp_token.toString());

            try {
                HttpResponse response = httpclient.execute(httpget);
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    result_byte = EntityUtils.toByteArray(response.getEntity());
                    result_string  = new String(result_byte, "UTF-8");
                    competitions_json = result_string ;
                    success = true;
                }else {
                    result_byte = EntityUtils.toByteArray(response.getEntity());
                    result_string  = new String(result_byte, "UTF-8");
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result_string ;
        }

        @Override
        protected void onPostExecute(String result2) {
            if(pdialog != null && pdialog.isShowing()) {
                pdialog.dismiss();
            }
            if(success) {
                JSONArray competitions_ja = null;
                try {
                    competitions_ja= new JSONArray(competitions_json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < competitions_ja.length(); i++) {
                    JSONObject competition_row = null;
                    try {
                        competition_row = competitions_ja.getJSONObject(i);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date comp_date = sdf.parse(competition_row.getString("date"));
//                        if (new Date().before(comp_date)) {
                            LayoutInflater inflater = LayoutInflater.from(getContext());
                            final View item = inflater.inflate(R.layout.competition_row_view, main_table, false);
                            TextView competition_name = (TextView) item.findViewById(R.id.comp_name);
                            competition_name.setText(competition_row.getString("name"));
                            TextView date_col = (TextView) item.findViewById(R.id.comp_date);
                            date_col.setText(competition_row.getString("date"));
                            if(i % 2 == 0) {
                                item.setBackgroundColor(Color.WHITE);
                            } else {
                                item.setBackgroundColor(Color.LTGRAY);
                            }
                            ImageView edit_btn = (ImageView) item.findViewById(R.id.edit_competition);
                            edit_btn.setOnClickListener(edit_comp);
                            edit_btn.setTag(competition_row.getString("id"));
                            ImageView view_btn = (ImageView) item.findViewById(R.id.view_competition);
                            view_btn.setOnClickListener(view_comp);
                            view_btn.setTag(competition_row.getString("id"));
                            main_table.addView(item);
//                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                Toast.makeText(getContext(), getResources().getString(R.string.courses_loaded), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), getResources().getString(R.string.information_loaded), Toast.LENGTH_SHORT).show();
            }
        }
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_view_closed_competitions, container, false);
        main_table = (TableLayout) view.findViewById(R.id.closed_competition_table);

        new CompetitionView().execute();

        return view;
	}

    private View.OnClickListener view_comp = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final String TEMP_FILE_NAME = "competition_number.txt";
            File tempFile;
            File cDir = getActivity().getCacheDir();
            tempFile = new File(cDir.getPath() + "/" + TEMP_FILE_NAME) ;
            FileWriter writer=null;
            try {
                writer = new FileWriter(tempFile);
                writer.write(view.getTag()+"");
                writer.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            showFragmentAndAddToBackStack(new ClosedCompetitionInfoFragment());
        }
    };

    private View.OnClickListener edit_comp = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final String TEMP_FILE_NAME = "competition_number.txt";
            File tempFile;
            File cDir = getActivity().getCacheDir();
            tempFile = new File(cDir.getPath() + "/" + TEMP_FILE_NAME) ;
            FileWriter writer=null;
            try {
                writer = new FileWriter(tempFile);
                writer.write(view.getTag()+"");
                writer.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            showFragmentAndAddToBackStack(new ClosedCompetitionEditFragment());
        }
    };
}
