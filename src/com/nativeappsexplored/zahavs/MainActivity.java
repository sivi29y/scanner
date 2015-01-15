package com.nativeappsexplored.zahavs;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.nativeappsexplored.zahavs.camera.CameraActivity;
import com.nativeappsexplored.zahavs.communication.MyIntentService;

public class MainActivity extends Activity {

	public static final String PREFS_NAME = "ZahavScannerPerferencesFile";
	//private static final Integer REQUEST = 0;
	private String brcd;
	private String ts;
	private String imei;
	private int brcd_counter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		
setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		//retrieve global task counter
		SharedPreferences settings = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
		brcd_counter = settings.getInt("brcd_counter", 0);
		
		
		//getting imei
		TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		imei = telephonyManager.getDeviceId();
		
		//Starting service 
		Intent intent = new Intent(this.getApplicationContext(), MyIntentService.class);
    	startService(intent);
		

	}



	public void scanIntent(View view) {
		// Intent intent = new Intent(this, CameraTestActivity.class);
		// startActivity(intent);

		IntentIntegrator scanIntegrator = new IntentIntegrator(this);
		scanIntegrator.initiateScan();
	}

	public void historyIntent(View view) {
		Intent intent = new Intent(getApplicationContext(),
				HistoryActivity.class);
		startActivity(intent);

	}
	
	
	public void onPause() {
		super.onPause();
		  // Store values between instances here
		  SharedPreferences settings = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
		  SharedPreferences.Editor editor = settings.edit();

		  brcd_counter = settings.getInt("brcd_counter", 0);
		   if (settings.contains("qr_code"+brcd_counter)){
			   brcd_counter+=1;
			   editor.putInt("brcd_counter", brcd_counter);
		   }
		   if(brcd!=null && ts!=null)
		   editor.putString("qr_code" + brcd_counter, brcd);		   
		   editor.putString("time_stamp"+brcd_counter, ts);
		   editor.putString("imei", imei);
		   editor.commit();
			Log.d("prefernces", settings.getAll().toString()); 
		
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// retrieve scan result
		IntentResult scanningResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, intent);
		if (scanningResult != null && resultCode==RESULT_OK) {
			// we have a result
			//String scanContent = scanningResult.getContents();
			//String scanFormat = scanningResult.getFormatName();

			// entering value to share prefernce string
			brcd = scanningResult.getContents();

			Long tsLong = System.currentTimeMillis() / 1000;
			ts = tsLong.toString();

			Intent i = new Intent(getApplicationContext(),
					CameraActivity.class);
			startActivity(i);

			// formatTxt.setText("FORMAT: " + scanFormat);
			// contentTxt.setText("CONTENT: " + scanContent);
		} else {
			Toast toast = Toast.makeText(getApplicationContext(),
					"No scan data received!", Toast.LENGTH_LONG);
			toast.show();
		}

	}

}
