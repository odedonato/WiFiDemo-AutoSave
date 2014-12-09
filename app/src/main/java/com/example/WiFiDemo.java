package com.example;

import java.text.DateFormat;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.StringBuilder;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import java.text.SimpleDateFormat;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
//import android.net.wifi.WifiConfiguration;
//import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
//import android.view.View;
//import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import 	android.view.KeyEvent;


public class WiFiDemo extends Activity {
	private static final String TAG = "WiFiDemo";
	WifiManager wifi;
	BroadcastReceiver receiver;

	TextView textStatus;
	Handler h=new Handler();
	
	ArrayList<String> listArray = new ArrayList<String>();
	
	private long lastPressedTime;
	private static final int PERIOD = 2000;
	
	/** Called when the activity is first created. */
	
	@Override
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
	        switch (event.getAction()) {
	        case KeyEvent.ACTION_DOWN:
	            if (event.getDownTime() - lastPressedTime < PERIOD) {
	            	System.exit(0);
	            } else {
	                Toast.makeText(getApplicationContext(), "Press again to exit.",
	                        Toast.LENGTH_SHORT).show();
	                lastPressedTime = event.getEventTime();
	            }
	            return true;
	        }
	    }
	    return false;
	}
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Setup UI
		textStatus = (TextView) findViewById(R.id.textStatus);
		
		// Setup WiFi
		wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		
		// Register Broadcast Receiver
		registerReceiver(receiver, new IntentFilter(
				WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		Log.d(TAG, "onCreate()");
		
		// List available networks
		//textStatus.setText("\n\n" + wifi.getScanResults());
		
		h.post(new Runnable(){
			@Override
				public void run() {
					// call your function
					h.postDelayed(this,10000);
					listArray.clear();
					String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
					textStatus.setText("List available networks @ " + currentDateTimeString + ":");
					listArray.add("List available networks @ " + currentDateTimeString + ":");
					List<ScanResult> results = wifi.getScanResults();
					for (ScanResult result : results) {
						textStatus.append("\n\n" + result.toString());
				        listArray.add(result.toString());
						}
					//System.out.println(listArray);
					String format = "ddMMyyyy_HHmmss";
					SimpleDateFormat df = new SimpleDateFormat(format, Locale.ITALIAN);
					String saveDateTimeString = df.format(new Date());
					Log.d(TAG, "save to file: " + Environment.getExternalStorageDirectory() + File.separator + saveDateTimeString + ".txt");
					File file = new File(Environment.getExternalStorageDirectory() + File.separator + saveDateTimeString + ".txt");
					try {
						file.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
					StringBuilder out = new StringBuilder();
					for (Object o : listArray)
					{
					  out.append(o.toString());
					  out.append("\n\n");
					}
						try {
						 FileOutputStream fo = new FileOutputStream(file);
						 ObjectOutputStream oos = new ObjectOutputStream(fo);
					     oos.writeObject(out.toString());
					     oos.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
			});
	}
}	