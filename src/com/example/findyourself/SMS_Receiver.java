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
		//System.out.println("�յ�����");
		if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
			Log.v("Find!","�յ����ţ�");
			Object[] pdus=(Object[])intent.getExtras().get("pdus");
			//��֪��Ϊʲô����ֻ��һ����Ϣ����������ȴ�����飬Ҳ����Ϊ�˴���ͬʱͬ��ͬ��ͬ�����յ���������
			//����������е�С
			SmsMessage[] message=new SmsMessage[pdus.length];
			StringBuilder sb=new StringBuilder();
			System.out.println("pdus����"+pdus.length);
			for(int i=0;i<pdus.length;i++){
				//��Ȼ��ѭ������ʵpdus����һ�㶼��1
				message[i]=SmsMessage.createFromPdu((byte[])pdus[i]);
				sb.append("���յ���������:\n");
				sb.append(message[i].getDisplayOriginatingAddress()+"\n");

				sb.append("����:"+message[i].getDisplayMessageBody());
				

			}
			System.out.println(sb.toString());
			Log.v("Find!",new String(sb));
			Log.v("Find!",message[0].getDisplayOriginatingAddress());
			if(message[0].getDisplayOriginatingAddress().contains("18911127828"))
			{
				Log.v("Find!","׼��Intent!");
				
				intent.putExtra("jinji", true);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
				intent.setClass(context, MainActivity.class);
				context.startActivity(intent);
			}				
			
		}
	}
 
}
