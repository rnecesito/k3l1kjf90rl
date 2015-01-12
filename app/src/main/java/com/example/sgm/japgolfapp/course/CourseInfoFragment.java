package com.example.sgm.japgolfapp.course;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
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
import org.apache.http.client.methods.HttpDelete;
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
import java.util.ArrayList;
import java.util.List;

import static com.example.sgm.japgolfapp.R.array.holes_list;

public class CourseInfoFragment extends BaseFragment {
    private String retVal = "";
    private ProgressDialog pdialog;
    private boolean success = false;

	public static final String EMPTY = "";
    View c_info_view;
    String response2;
    TableLayout holes_table;
    boolean firstload = true;


    private class SingleCourseView extends AsyncTask<String, String, String> {

        public SingleCourseView() {
            pdialog = new ProgressDialog(getActivity());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog.setMessage(getResources().getString(R.string.loading_course_info));
            pdialog.show();
            pdialog.setCancelable(false);
            pdialog.setCanceledOnTouchOutside(false);
            firstload = true;
        }

        @Override
        protected String doInBackground(String... strings) {
            byte[] result = null;
            String result2 = "";

            File cDir = getActivity().getCacheDir();
            File tempFile = new File(cDir.getPath() + "/" + "golfapp_token.txt") ;
            String strLine="";
            StringBuilder text = new StringBuilder();
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

            File cDir2 = getActivity().getCacheDir();
            File tempFile2 = new File(cDir2.getPath() + "/" + "course_number.txt") ;
            String strLine2="";
            StringBuilder text2 = new StringBuilder();
            try {
                FileReader fReader = new FileReader(tempFile2);
                BufferedReader bReader = new BufferedReader(fReader);
                while( (strLine2=bReader.readLine()) != null  ){
                    text2.append(strLine2);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }

            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httppost = new HttpGet("http://zoogtech.com/golfapp/public/course/"+text2.toString()+"?access_token="+text.toString());

            try {
                HttpResponse response = httpclient.execute(httppost);
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    result = EntityUtils.toByteArray(response.getEntity());
                    result2 = new String(result, "UTF-8");
                    response2 = result2;
                    success = true;
                }else {
                    result = EntityUtils.toByteArray(response.getEntity());
                    result2 = new String(result, "UTF-8");
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
            if(pdialog != null && pdialog.isShowing()) {
                pdialog.dismiss();
            }
            if(success) {
                TableLayout tl = holes_table;
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response2);
                    EditText cname = (EditText) c_info_view.findViewById(R.id.course_name_info);
                    cname.setText(jsonObject.getString("name"));

                    Spinner holes_info = (Spinner) c_info_view.findViewById(R.id.hole_count_info);
                    //holes_info.setText(jsonObject.getString("holes"));
                    ArrayAdapter<CharSequence> adapter= ArrayAdapter.createFromResource(getActivity(), holes_list, android.R.layout.simple_spinner_dropdown_item);
                    if(!jsonObject.getString("holes").equals(null)){
                        int holePosition = adapter.getPosition(jsonObject.getString("holes"));
//                        holes_info.setSelection(holePosition);
                    }

//                    tl.removeAllViews();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONArray array = null;
                try {
                    array = new JSONArray(jsonObject.getString("hole_items"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < array.length(); i++) {
                    JSONObject row = null;
                    try {
                        row = array.getJSONObject(i);
                        LayoutInflater inflater = LayoutInflater.from(getContext());
                        final View item = inflater.inflate(R.layout.hole_row, holes_table, false);
                        TextView hnt = (TextView) item.findViewById(R.id.hole_number_text);
                        hnt.setText((i + 1) + "");
                        EditText hv = (EditText) item.findViewById(R.id.hole_value_info);
                        hv.setText(row.getString("par"));
                        hv.setTextColor(Color.BLACK);
                        holes_table.addView(item);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                Toast.makeText(getContext(), getResources().getString(R.string.information_loaded), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class CourseCreate extends AsyncTask<String, String, String> {
        StringBuilder course_number = new StringBuilder();
        public CourseCreate() {
            pdialog = new ProgressDialog(getActivity());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog.setMessage(getResources().getString(R.string.jap_registering));
            pdialog.show();
            pdialog.setCancelable(false);
            pdialog.setCanceledOnTouchOutside(false);

            File cDir = getActivity().getCacheDir();
            File tempFile = new File(cDir.getPath() + "/" + "course_number.txt") ;
            String strLine="";
            try {
                FileReader fReader = new FileReader(tempFile);
                BufferedReader bReader = new BufferedReader(fReader);
                while( (strLine=bReader.readLine()) != null  ){
                    course_number.append(strLine);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String course = strings[0];
            String holes = strings[1];
            String handicap = strings[2];
            byte[] result = null;
            String str = "";

            File cDir = getActivity().getCacheDir();
            File tempFile = new File(cDir.getPath() + "/" + "golfapp_token.txt") ;
            String strLine="";
            StringBuilder text = new StringBuilder();
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
            HttpClient httpclient = new DefaultHttpClient();
            HttpPut httppost = new HttpPut("http://zoogtech.com/golfapp/public/course/"+course_number.toString());

            try {
                List<NameValuePair> json = new ArrayList<NameValuePair>();
                json.add(new BasicNameValuePair("name", course));
                json.add(new BasicNameValuePair("holes", holes));
                json.add(new BasicNameValuePair("active", Boolean.toString(true)));
                for(int index = 0; index < holes_table.getChildCount(); index++){
                    int par1 = 0;
                    TableRow row = (TableRow) holes_table.getChildAt(index);
                    EditText et = (EditText) row.getChildAt(2);
                    String hole_par = et.getText().toString();
                    if(!hole_par.matches("")) {
                        par1 = Integer.parseInt(hole_par);
                    }
                    json.add(new BasicNameValuePair("hole_items["+index+"][hole_number]", index+""));
                    json.add(new BasicNameValuePair("hole_items["+index+"][par]", par1+""));
                }

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
//                    System.out.println(text.toString());
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
                Toast.makeText(getContext(), getResources().getString(R.string.jap_reg_success), Toast.LENGTH_SHORT).show();
                showFragment(new ViewCourseFragment());
            } else {
                Toast.makeText(getContext(), getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class CourseDelete extends AsyncTask<String, String, String> {
        StringBuilder course_number = new StringBuilder();
        public CourseDelete() {
            pdialog = new ProgressDialog(getActivity());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog.setMessage(getResources().getString(R.string.deleting_course));
            pdialog.show();
            pdialog.setCancelable(false);
            pdialog.setCanceledOnTouchOutside(false);
            /** Getting Cache Directory */
            File cDir = getActivity().getCacheDir();

            /** Getting a reference to temporary file, if created earlier */
            File tempFile = new File(cDir.getPath() + "/" + "course_number.txt") ;

            String strLine="";

            /** Reading contents of the temporary file, if already exists */
            try {
                FileReader fReader = new FileReader(tempFile);
                BufferedReader bReader = new BufferedReader(fReader);

                /** Reading the contents of the file , line by line */
                while( (strLine=bReader.readLine()) != null  ){
                    course_number.append(strLine);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            byte[] result = null;
            String str = "";
            File cDir = getActivity().getCacheDir();
            File tempFile = new File(cDir.getPath() + "/" + "golfapp_token.txt") ;
            String strLine="";
            StringBuilder text = new StringBuilder();
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
            HttpClient httpclient = new DefaultHttpClient();
            HttpDelete httppost = new HttpDelete("http://zoogtech.com/golfapp/public/course/"+course_number.toString());

            try {
                httppost.setHeader("Content-type", "application/x-www-form-urlencoded");
                httppost.setHeader("Authorization", text.toString());

                HttpResponse response = httpclient.execute(httppost);
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpURLConnection.HTTP_OK) {
                    result = EntityUtils.toByteArray(response.getEntity());
                    str = new String(result, "UTF-8");
                    System.out.println(str);
                    System.out.println("Success!");
                    success = true;
                    retVal = str;
                    Log.d("Response", retVal);
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
            return "Yeah";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(pdialog != null && pdialog.isShowing()) {
                pdialog.dismiss();
            }
            if(success) {
                Toast.makeText(getContext(), getResources().getString(R.string.course_deleted), Toast.LENGTH_SHORT).show();
                popBackStack();
            } else {
                Toast.makeText(getContext(), getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        }
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_view_single_course, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
        holes_table = (TableLayout) view.findViewById(R.id.holes_table_info);
        c_info_view = view;
        new SingleCourseView().execute();

        Button login = (Button) view.findViewById(R.id.create_course_info);
        Button del = (Button) view.findViewById(R.id.delete_course);
        final EditText coursename = (EditText) view.findViewById(R.id.course_name_info);
        final Spinner holes = (Spinner) view.findViewById(R.id.hole_count_info);
        final Spinner handicap = (Spinner) view.findViewById(R.id.handicap_info);
        holes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!firstload) {
                    String amount_str = holes.getSelectedItem().toString();
                    if (amount_str.matches("") || amount_str.matches(" ")) {
                        amount_str = "0";
                    }
                    int amount = Integer.parseInt(amount_str);

                    holes_table.removeAllViews();
                    for (int x = 0; x < amount; x++) {
                        LayoutInflater inflater = LayoutInflater.from(getContext());
                        final View item = inflater.inflate(R.layout.hole_row, holes_table, false);
                        TextView hnt = (TextView) item.findViewById(R.id.hole_number_text);
                        hnt.setText((x + 1) + "");
                        holes_table.addView(item);
                    }
                } else {
                    firstload = false;
                    return;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String cname_val = coursename.getText().toString();
                String holes_val = holes.getSelectedItem().toString();
                int holes_int = 0;
                if (!holes_val.matches("")) {
                    holes_int = Integer.parseInt(holes_val);
                }
                String hc_val = handicap.getSelectedItem().toString();
                if(cname_val.matches("")) {
                    Toast.makeText(getContext(), getResources().getString(R.string.enter_course_name), Toast.LENGTH_SHORT).show();
                    return;
                } else if(!( holes_int <= 18)) {
                    Toast.makeText(getContext(), getResources().getString(R.string.up_to_18_holes), Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    new CourseCreate().execute(cname_val, holes_val, hc_val);
                    System.out.println(retVal);
                }
            }
        });
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                new CourseDelete().execute();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(getResources().getString(R.string.sure)).setPositiveButton(getResources().getString(R.string.yes), dialogClickListener)
                        .setNegativeButton(getResources().getString(R.string.no), dialogClickListener).show();
            }
        });
	}
}

