package com.example.findyourself;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	public double latitude;
	public double longitude;
	public int locationtype;
	public String phoneinfo;
	public String phoneinfo1;
	public String phoneinfo2;
	public String nownetwork;
	public boolean networkavl;
	public boolean havebeensent;
	public String url;
	public TextView t;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Intent intent1 = getIntent();
		if(intent1.getBooleanExtra("jinji", false))
		{jinji();}
		else
		{
			
			
		}
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}	
	public void jinji()
	{
		t = (TextView) this.findViewById(R.id.okview);
		
		url = "http://10.101.158.98:50060/report.jsp";
		
		cal(); // 计算位置
		Log.v("Find!","latitude:"+latitude+" , "+"longitude:"+longitude);
		cellphoneinfo();
		judgenetwork();

			try {
				sendmessage();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		if(havebeensent)
		{
			t.setText("你的信息已经被传出");
		}
		else
		{
			t.setText("你的信息还未传出");
			
		}
		
	}


	
	public void sendmessage() throws MalformedURLException
	{
		try{
		url += "?lati="+latitude+"&long="+longitude+"&";
		if(phoneinfo2.length()>5)
		{
			url += "phone="+phoneinfo2;
		}
		else
		{
			url += "phone="+phoneinfo1;
		}
		
		url += "&type="+locationtype;
		
		//URL getUrl = new URL(url);
		
		Log.v("Find!", "url is:"+url);
		
		url = "http://www.baidu.com"; // ....................................
		
		network n = new network();
		havebeensent = (Boolean) n.doInBackground(url);
		/*
		HttpURLConnection connection = (HttpURLConnection)getUrl.openConnection();
		
		
		Log.v("Find!",getUrl.toString());
		
		connection.connect();

		BufferedReader reader = new BufferedReader(new InputStreamReader(  
	                connection.getInputStream()));  
		String line; 
        while ((line = reader.readLine()) != null) {  
            Log.v("Find!",line);
        }  
        */
		}
		catch(Exception e)
		{}
	}
	
	public void judgenetwork()
	{
		ConnectivityManager mConnectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
		 NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
		 networkavl = false;
		 nownetwork = "";
		 if(mNetworkInfo.isAvailable())
		 {
			 networkavl = true;
			 nownetwork = mNetworkInfo.getTypeName();
			 
		 }
		 
		 Log.v("Find!",nownetwork);
		
	}
	public void cellphoneinfo()
	{
		 TelephonyManager tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);    
		 phoneinfo = "";
		 phoneinfo1 = "";
		 phoneinfo2 = "";
		 phoneinfo1 += tm.getDeviceId();
		 phoneinfo2 += tm.getLine1Number();
		
		 Log.v("Find!",phoneinfo);
	}

	
	public void cal()
	{
		locationtype = 0;
		latitude = -1;
		longitude = -1;
		LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
			Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			Log.v("Find!","here1");
			if(location != null){
				latitude = location.getLatitude();
				longitude = location.getLongitude();
				}
		}
		if(latitude > 0 )
		{
			locationtype = 1; // 通过GPS获得
		}
		else if(latitude <0)
		{
			Log.v("Find!","here2");
			LocationListener locationListener = new LocationListener() {
				
				// Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
				@Override
				public void onStatusChanged(String provider, int status, Bundle extras) {
					Log.v("Find!","bukeyong!");
				}
				
				// Provider被enable时触发此函数，比如GPS被打开
				@Override
				public void onProviderEnabled(String provider) {
					
				}
				
				// Provider被disable时触发此函数，比如GPS被关闭 
				@Override
				public void onProviderDisabled(String provider) {
					
				}
				
				//当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发 
				@Override
				public void onLocationChanged(Location location) {
					if (location != null) {   
						Log.e("Map", "Location changed : Lat: "  
						+ location.getLatitude() + " Lng: "  
						+ location.getLongitude());   
					}
				}
			};
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000, 0,locationListener);   
			Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);   
			if(location != null){   
				latitude = location.getLatitude(); //经度   
				longitude = location.getLongitude(); //纬度
				locationtype = 2; //通过网络获得
			}   
		}
		
	}
	
	public class network extends AsyncTask
	{

		@Override
		protected Object doInBackground(Object... arg0) {
			// TODO Auto-generated method stub
			byte b[]=new byte[2048];
			boolean ok=false;
			try{
			//URL getUrl = (URL) arg0[0];
			
			//getUrl=new URL("http://www.baidu.com");
			
			//HttpURLConnection connection = (HttpURLConnection)getUrl.openConnection();
			
			
			//Log.v("Find!",getUrl.toString());
			
			//connection.connect();

			//BufferedReader reader = new BufferedReader(new InputStreamReader(  
		   //             connection.getInputStream()));  
			//String line; 
	        //while ((line = reader.readLine()) != null) {  
	          //  Log.v("Find!",line);
	       // } 
	        
				HttpClient client = new DefaultHttpClient();
				URI uri = URI.create((String) arg0[0]);  
				HttpGet get = new HttpGet(uri);
				HttpResponse response = client.execute(get);  
				HttpEntity entity = response.getEntity(); 
				InputStream in = entity.getContent();
				in.read(b);
				in.close();
				
				//Log.v("Find!",entity.toString());
				//Log.v("Find!",in.toString());
				String str = new String(b);
				Log.v("Find!",str);
				
				if(str.contains("Hadoop OK"))
					ok = true;
				else
					ok = false;
			}
			catch (Exception e)
			{
				
			}
			
			return ok;
		}
		
		
		
	}
	
	
}
