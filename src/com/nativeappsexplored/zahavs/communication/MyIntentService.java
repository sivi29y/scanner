package com.nativeappsexplored.zahavs.communication;


import com.nativeappsexplored.zahavs.MainActivity;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class MyIntentService extends IntentService {

	public static final String PREFS_NAME = MainActivity.PREFS_NAME;
	private static final String TAG = "zahavscanner.communication";

	@Override
	protected void onHandleIntent(Intent arg0) {
		// TODO Auto-generated method stub
		Log.i(TAG, "Intent Service started");
		// check if there is network connection
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		
		
		if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
			// check if there is shared prefence
			SharedPreferences settings = getSharedPreferences(PREFS_NAME,
					MODE_PRIVATE);
			while (true) {
				if (settings != null) {
					// try send data via asynch task
					try {
						
						synchronized (MyIntentService.class) {
							  MyIntentService.class.wait(5000);
							  Log.i(TAG, "Intent Service trying to send data..");
							  new AddClient(settings).execute();		
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						Log.d(TAG, "failed to execute add client" + e);
							}
				}else {
					// no data to send
					break;
					//stopService(new Intent(this.INPUT_SERVICE));
				}
				
			}

			
		}

	}

	public MyIntentService(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public MyIntentService() {
		super("MyIntentService");
	}

}