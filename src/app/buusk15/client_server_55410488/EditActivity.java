package app.buusk15.client_server_55410488;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends Activity implements OnClickListener {
	private EditText edt1, edt2, edt3;
	private Button btn1, btn2;
	private ProgressDialog dialog;
	private JSONParser jParser = new JSONParser();
	private static String url_update_student = "http://10.202.44.153:88/android/update_student.php";
	private static String url_details_student = "http://10.202.44.153:88/android/student_details.php";
	private static String url_delete_student = "http://10.202.44.153:88/android/delete_student.php";
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_STUDENT = "student";
	private static final String TAG_ID = "id";
	private static final String TAG_STUID = "stu_id";
	private static final String TAG_NAME = "name";
	private static final String TAG_TEL = "tel";
	String sid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit);

		Intent i = getIntent();
		sid = i.getStringExtra(TAG_ID);
		btn1 = (Button) findViewById(R.id.btnsavechange);
		btn1.setOnClickListener(this);
		btn2 = (Button) findViewById(R.id.btndelete);
		btn2.setOnClickListener(this);
		new GetStudentDetails().execute();
	}

	class GetStudentDetails extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(EditActivity.this);
			dialog.setMessage("Loading student. Please wait...");
			dialog.setIndeterminate(false);
			dialog.setCancelable(false);
			dialog.show();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			int success;
			try {
				List<NameValuePair> list = new ArrayList<NameValuePair>();
				list.add(new BasicNameValuePair("id", sid));

				JSONObject json = jParser.makeHttpRequest(url_details_student,
						"GET", list);
				Log.d("Single student Details", json.toString());
				success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
					JSONArray stuobj = json.getJSONArray(TAG_STUDENT);
					JSONObject student = stuobj.getJSONObject(0);
					edt1 = (EditText) findViewById(R.id.edt1);
					edt2 = (EditText) findViewById(R.id.edt2);
					edt3 = (EditText) findViewById(R.id.edt3);

					edt1.setText(student.getString(TAG_STUID));
					edt2.setText(student.getString(TAG_NAME));
					edt3.setText(student.getString(TAG_TEL));

				} else {

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
			super.onPostExecute(result);
		}
	}

	class SaveProductDetails extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(EditActivity.this);
			dialog.setMessage("Saving student ...");
			dialog.setIndeterminate(false);
			dialog.setCancelable(true);
			dialog.show();
			super.onPreExecute();
		}

		protected String doInBackground(String... params) {
			String stu_id = edt1.getText().toString();
			String name = edt2.getText().toString();
			String tel = edt3.getText().toString();
			List<NameValuePair> list1 = new ArrayList<NameValuePair>();
			list1.add(new BasicNameValuePair("id", sid));
			list1.add(new BasicNameValuePair(TAG_STUID, stu_id));
			list1.add(new BasicNameValuePair(TAG_NAME, name));
			list1.add(new BasicNameValuePair(TAG_TEL, tel));
			JSONObject json = jParser.makeHttpRequest(url_update_student,
					"POST", list1);
			try {
				int success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
					Intent i = getIntent();
					setResult(100, i);
					finish();
				} else {
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(String file_url) {
			dialog.dismiss();
		}

	}
	class DeleteStudent extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(EditActivity.this);
			dialog.setMessage("Deleting Student...");
			dialog.setIndeterminate(false);
			dialog.setCancelable(true);
			dialog.show();
		}

		protected String doInBackground(String... args) {
			int success;
			try {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("id", sid));

				JSONObject json = jParser.makeHttpRequest(
						url_delete_student, "POST", params);

				Log.d("Delete Student", json.toString());

				success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
					Intent i = getIntent();
					setResult(100, i);
					finish();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String file_url) {
			dialog.dismiss();

		}


	
}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnsavechange:
			new SaveProductDetails().execute();
			break;
		case R.id.btndelete:
			new DeleteStudent().execute();
			break;
		default:
			break;

		}
		
	}
}
