package com.example.sgm.japgolfapp.course;


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


public class ViewCourseFragment extends BaseFragment {
    private String retVal = null;
    private ProgressDialog pdialog;
    private boolean success = false;
    String response2;
    TableLayout main_table;

//	@OnClick({ R.id.edit1, R.id.edit2, R.id.edit3 })
//	public void showEditCourse(ImageView view) {
//		String title = view.getTag().toString();
//		showFragmentAndAddToBackStack(new CreateCourseFragment(title));
//	}

    private class CourseView extends AsyncTask<String, String, String> {

        public CourseView() {
            pdialog = new ProgressDialog(getActivity());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog.setMessage(getResources().getString(R.string.loading_courses));
            pdialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            byte[] result = null;
            String result2 = "";

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
                    System.out.println(result2);
                    System.out.println("Success!");
                    response2 = result2;
                    success = true;
                }else {
                    result = EntityUtils.toByteArray(response.getEntity());
                    result2 = new String(result, "UTF-8");
                    System.out.println(result2);
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
        protected void onPostExecute(String result2) {
//            super.onPostExecute(result2);
            if(pdialog != null && pdialog.isShowing()) {
                pdialog.dismiss();
            }
            if(success) {
                TableLayout tl = main_table;

                JSONArray array = null;
                try {
                    array = new JSONArray(result2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < array.length(); i++) {
                    JSONObject row = null;
                    try {
                        row = array.getJSONObject(i);
                        LayoutInflater inflater = LayoutInflater.from(getContext());
                        final View item = inflater.inflate(R.layout.course_row, main_table, false);
                        TextView course_name_col = (TextView) item.findViewById(R.id.c_row_name);
                        course_name_col.setText(row.getString("name"));
                        TextView holes_col = (TextView) item.findViewById(R.id.c_row_holes);
                        holes_col.setText(row.getString("holes"));
                        if(i % 2 == 0) {
                            item.setBackgroundColor(Color.WHITE);
                        } else {
                            item.setBackgroundColor(Color.LTGRAY);
                        }
                        ImageView edit_btn = (ImageView) item.findViewById(R.id.editCourse);
                        edit_btn.setOnClickListener(btnListener);
                        edit_btn.setTag(row.getString("id"));
                        main_table.addView(item);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Toast.makeText(getContext(), getResources().getString(R.string.courses_loaded), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        }
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_view_course, container, false);
        main_table = (TableLayout) view.findViewById(R.id.courses_table);


        new CourseView().execute();

        return view;
	}

    private View.OnClickListener btnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            /** Saving user email for comparison later */
            final String TEMP_FILE_NAME = "course_number.txt";
            File tempFile;
            /** Getting Cache Directory */
            File cDir = getActivity().getCacheDir();
            /** Getting a reference to temporary file, if created earlier */
            tempFile = new File(cDir.getPath() + "/" + TEMP_FILE_NAME) ;
            FileWriter writer=null;
            try {
                writer = new FileWriter(tempFile);

                /** Saving the contents to the file*/
                writer.write(view.getTag()+"");

                /** Closing the writer object */
                writer.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            /** End save */
            //todo
            showFragmentAndAddToBackStack(new CourseInfoFragment());
        }
    };
}
