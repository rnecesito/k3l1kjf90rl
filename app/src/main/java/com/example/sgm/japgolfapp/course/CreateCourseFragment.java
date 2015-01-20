package com.example.sgm.japgolfapp.course;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class CreateCourseFragment extends BaseFragment {
    private String retVal = "";
    private ProgressDialog pdialog;
    private boolean success = false;

	public static final String EMPTY = "";

    TableLayout holes_table;

    private class CourseCreate extends AsyncTask<String, String, String> {
        StringBuilder text = new StringBuilder();
        public CourseCreate() {
            pdialog = new ProgressDialog(getActivity());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog.setMessage(getResources().getString(R.string.create_courses));
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
            String course = strings[0];
            String holes = strings[1];
            String handicap = strings[2];
            byte[] result = null;
            String str = "";
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://zoogtech.com/golfapp/public/course");

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
                httppost.setEntity(new UrlEncodedFormEntity(json, HTTP.UTF_8));

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
                Toast.makeText(getContext(), getResources().getString(R.string.course_created), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        }
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_create_course, container, false);
	}
	
	@Override
	public void onViewCreated(final View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
        holes_table = (TableLayout) view.findViewById(R.id.holes_table);

        Button login = (Button) view.findViewById(R.id.create_course);
        final EditText coursename = (EditText) view.findViewById(R.id.course_name);
        final Spinner holes = (Spinner) view.findViewById(R.id.hole_count);
//        final Spinner handicap = (Spinner) view.findViewById(R.id.handicap);
        holes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String amount_str = holes.getSelectedItem().toString();
                if (amount_str.matches("") || amount_str.matches(" ")) {
                    amount_str = "0";
                }
                int amount = Integer.parseInt(amount_str);

                holes_table.removeAllViews();
                for(int x = 0; x < amount; x++) {
                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    final View item = inflater.inflate(R.layout.hole_row, holes_table, false);
                    TextView hnt = (TextView) item.findViewById(R.id.hole_number_text);
                    hnt.setText((x + 1) + "");
                    holes_table.addView(item);
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
//                String hc_val = handicap.getSelectedItem().toString();
                byte[] result = null;
                String str = "";
                if (cname_val.matches("")) {
                    Toast.makeText(getContext(), getResources().getString(R.string.create_course_name), Toast.LENGTH_SHORT).show();
                    return;
                } else if (holes_val.matches("")) {
                    Toast.makeText(getContext(), getResources().getString(R.string.up_to_18_holes), Toast.LENGTH_SHORT).show();
                } else if (!(holes_int <= 18)) {
                    Toast.makeText(getContext(), getResources().getString(R.string.up_to_18_holes), Toast.LENGTH_SHORT).show();
                    return;
                } else if (!checkPar()) {
                    Toast.makeText(getContext(), "パー数を入力してください", Toast.LENGTH_SHORT).show();
                }else {
                    new CourseCreate().execute(cname_val, holes_val, "No");
                    showFragmentAndAddToBackStack(new ViewCourseFragment());
                }
            }
        });
	}

    public boolean checkPar() {
        boolean flag = true;
        for(int index = 0; index < holes_table.getChildCount(); index++){
            TableRow row = (TableRow) holes_table.getChildAt(index);
            EditText et = (EditText) row.getChildAt(2);
            String hole_par = et.getText().toString();
            if(hole_par.matches("")) {
                flag = false;
            }
        }
        return flag;
    }
}
