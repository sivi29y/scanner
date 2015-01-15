package com.nativeappsexplored.zahavs.communication;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.nativeappsexplored.zahavs.MainActivity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Background Async Task to Create new product
 * */
public class AddClient extends AsyncTask<String, String, String> {

	public static final String PREFS_NAME = MainActivity.PREFS_NAME;
	

	private SharedPreferences settings;

	JSONParser jsonParser = new JSONParser();

	// url to create new product
	private static String url_create_product = "https://www.dataplus.co.il/hz/GetSignImg.asp";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";

	// Progress Dialog
	// private ProgressDialog pDialog;

	public AddClient(SharedPreferences settings) {
		this.settings = settings;
		
	}

	/**
	 * Before starting background thread Show Progress Dialog
	 * */
	@Override
	// protected void onPreExecute() {
	// super.onPreExecute();
	// pDialog = new ProgressDialog(NewProductActivity.this);
	// pDialog.setMessage("Creating Product..");
	// pDialog.setIndeterminate(false);
	// pDialog.setCancelable(true);
	// pDialog.show();
	// }
	/**
	 * Creating product
	 * */
	protected String doInBackground(String... args) {

//   	String name = settings.getString("name", "");
//		String phone = settings.getString("phone", "");
//		String family = settings.getString("family", "");
//		String car_type = settings.getString("car_type", "");
//		String car_number = settings.getString("car_number", "");
//		String comments = settings.getString("comments", "");

		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
        //Test Values		
//		params.add(new BasicNameValuePair("qr_code0", "111111111"));
//		params.add(new BasicNameValuePair("imei", "11111111"));
//     	params.add(new BasicNameValuePair("photo0", "base64photo"));
//		params.add(new BasicNameValuePair("time_stamp0", "1419503298"));
;

		// adding values to paramas
		Map<String, ?> valuesMap = settings.getAll();
		for (Entry<String, ?> entry : valuesMap.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue().toString();
			params.add(new BasicNameValuePair(key, value));
		}

		Log.d("ADD CLIENT", params.toString());

		// getting JSON Object
		// Note that create product url accepts POST method
		JSONObject json = jsonParser.makeHttpRequest(url_create_product,
				"POST", params);

		// check log cat fro response
		//Log.d("Create Response", json.toString());

		// check for success tag
		try {
			int success = json.getInt(TAG_SUCCESS);

			if (success == 1) {
				// successfully created product
				Log.d("ADDCLIENT ", "***SUCCESS***");
				
				//clear shared prefrences 
				settings.edit().clear().commit();

				// Intent i = new Intent(getApplicationContext(),
				// AllProductsActivity.class);
				// startActivity(i);
				//
				// // closing this screen
				// finish();
			} else {
				// failed to create product
				Log.d("ADDCLIENT ", "***failed to create call***");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * After completing background task Dismiss the progress dialog
	 * **/
	// protected void onPostExecute(String file_url) {
	// // dismiss the dialog once done
	// pDialog.dismiss();
	// }

}