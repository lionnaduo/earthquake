package com.example.findyourself;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMS_Receiver extends BroadcastReceiver {
	 
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		//System.out.println("收到短信");
		if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
			Log.v("Find!","收到短信！");
			Object[] pdus=(Object[])intent.getExtras().get("pdus");
			//不知道为什么明明只有一条消息，传过来的却是数组，也许是为了处理同时同分同秒同毫秒收到多条短信
			//但这个概率有点小
			SmsMessage[] message=new SmsMessage[pdus.length];
			StringBuilder sb=new StringBuilder();
			System.out.println("pdus长度"+pdus.length);
			for(int i=0;i<pdus.length;i++){
				//虽然是循环，其实pdus长度一般都是1
				message[i]=SmsMessage.createFromPdu((byte[])pdus[i]);
				sb.append("接收到短信来自:\n");
				sb.append(message[i].getDisplayOriginatingAddress()+"\n");

				sb.append("内容:"+message[i].getDisplayMessageBody());
				

			}
			System.out.println(sb.toString());
			Log.v("Find!",new String(sb));
			Log.v("Find!",message[0].getDisplayOriginatingAddress());
			if(message[0].getDisplayOriginatingAddress().contains("18911127828"))
			{
				Log.v("Find!","准备Intent!");
				
				intent.putExtra("jinji", true);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
				intent.setClass(context, MainActivity.class);
				context.startActivity(intent);
			}				
			
		}
	}
 
}
