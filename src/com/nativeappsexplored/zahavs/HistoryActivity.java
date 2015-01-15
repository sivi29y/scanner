package com.nativeappsexplored.zahavs;

import java.util.ArrayList;
import java.util.Map;

import com.nativeappsexplored.zahavs.R;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
  
public class HistoryActivity extends Activity {  
	
	 public static final String PREFS_NAME = MainActivity.PREFS_NAME;
	
  private ListView mainListView ;  
  private ArrayAdapter<String> listAdapter ;  
    
  /** Called when the activity is first created. */  
  @Override  
  public void onCreate(Bundle savedInstanceState) {  
    super.onCreate(savedInstanceState);  
    setContentView(R.layout.history_list);  
      
    // Find the ListView resource.   
    mainListView = (ListView) findViewById( R.id.mainListView );  
  
    // Create and populate a List of planet names.  
//    String[] scanns = null ;    
      ArrayList<String> scanList = new ArrayList<String>(); 
//    if (scanns !=null){
//   scanList.addAll( Arrays.asList(scanns) ); 
//    
//    }
      
    // Create ArrayAdapter using the planet list.  
    listAdapter = new ArrayAdapter<String>(this, R.layout.simple_row, scanList);  
      
    // Add more planets. If you passed a String[] instead of a List<String>   
    // into the ArrayAdapter constructor, you must not add more items.   
    // Otherwise an exception will occur.
   
   // SharedPreferences settings = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
    SharedPreferences prefs = getSharedPreferences(PREFS_NAME, 0);
    
    
    if (prefs!= null){
    Map<String,?> keys = prefs.getAll();
    for(Map.Entry<String,?> entry : keys.entrySet()){
                //Log.d("map values",entry.getKey() + ": " + entry.getValue().toString());
                if (entry.getKey().contains("qr_code") ){
                listAdapter.add( entry.getKey() + ": " + entry.getValue().toString());
                }
               
     }
    }
    
    
    
   // listAdapter.add( "Ceres" );  
    

	
      
    // Set the ArrayAdapter as the ListView's adapter.  
    mainListView.setAdapter( listAdapter );        
  }  
  
  
 

public void GetoutIntent(View view) {
//	  Intent intent = new Intent(Intent.ACTION_MAIN);
//	  intent.addCategory(Intent.CATEGORY_HOME);
//	  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//	  startActivity(intent);
	 
	//  this.getApplication().getBaseContext().getAssets().close();
//	 if(android.os.Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN)
//	  this.finishAffinity();
//	 else {
//		 this.finish();
//		 
//	 }
	  
	 this.finish();
	  
	  Intent intent = new Intent(Intent.ACTION_MAIN);
	  intent.addCategory(Intent.CATEGORY_HOME);
	  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	  startActivity(intent);
	 
	  
	  
		// inserting mysqli row 
//	    SharedPreferences prefs = getSharedPreferences(PREFS_NAME, 0);
//	    if (prefs != null)
//		try{
//		   Log.d("HISTORY", "DEBUGGED");
//		  //new AddClient(prefs).execute();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			Log.d("History", "failed to execute add clien" + e);
//		}
	  
//	  startActivity(intent);
	}
  
  public void ReScanIntent(View view) {
	  Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
//	  IntentIntegrator scanIntegrator = new IntentIntegrator(this);
//		scanIntegrator.initiateScan();
		
		
	}
	
}  