package com.rakesh.alarmmanagerexample;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.widget.Toast;

public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {

	final public static String ONE_TIME = "onetime";
	ArrayList<PendingIntent> intentArray = new ArrayList<PendingIntent>();
	
	@Override
	public void onReceive(Context context, Intent intent) {
		 PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
         PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
         //Acquire the lock
         wl.acquire();

         //You can do the processing here update the widget/remote views.
         Bundle extras = intent.getExtras();
         StringBuilder msgStr = new StringBuilder();
         
         if(extras != null && extras.getBoolean(ONE_TIME, Boolean.FALSE)){
        	 msgStr.append("One time Timer : ");
         }
         Format formatter = new SimpleDateFormat("hh:mm:ss a");
         msgStr.append(formatter.format(new Date()));

         //Toast.makeText(context, msgStr, Toast.LENGTH_LONG).show();
         NotificationAlarm(context);
         
         //Release the lock
         wl.release();
         
	}
	private void NotificationAlarm(Context context) {
		// TODO Auto-generated method stub
	    //取得Notification服務
	     NotificationManager notificationManager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
	    //設定當按下這個通知之後要執行的activity
	    Intent notifyIntent = new Intent(context,AlarmManagerActivity.class);
	    notifyIntent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK);
	    PendingIntent appIntent=PendingIntent.getActivity(context,0, notifyIntent,0);
	    Notification notification = new Notification();
	    //設定出現在狀態列的圖示
	    notification.icon=R.drawable.ic_launcher;
	    //顯示在狀態列的文字
	    notification.tickerText="notification on status bar.";
	    //會有通知預設的鈴聲、振動、light
	    notification.defaults=Notification.DEFAULT_ALL;
	    //設定通知的標題、內容
	    notification.setLatestEventInfo(context,"Title","content",appIntent);
	    //送出Notification
	    notificationManager.notify(0,notification);

	}
	public void SetAlarm(Context context)
    {
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        intent.putExtra(ONE_TIME, Boolean.FALSE);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        //After after 30 seconds
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 5 , pi); 
    }

    public void CancelAlarm(Context context,byte idx)
    {
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        //PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        PendingIntent sender = intentArray.get(idx);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
        intentArray.remove(idx);
    }
    public void setOnetimeTimer(Context context,byte idx){
    	AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        intent.putExtra(ONE_TIME, Boolean.TRUE);
        
        PendingIntent pi = PendingIntent.getBroadcast(context, idx, intent, 0);
    
        Calendar ca = forday(context,1,10,20);
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
//                calSet.getTimeInMillis(), 1 * 60 * 60 * 1000, pendingIntent);
        
        am.set(AlarmManager.RTC_WAKEUP, ca.getTimeInMillis(), pi);
        intentArray.add(pi);
    }
    
//    //取得Notification服務
//     NotificationManager notificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
//    //設定當按下這個通知之後要執行的activity
//    Intent notifyIntent = new Intent(NotificationExample.this,NotificationExample.class);
//    notifyIntent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK);
//    PendingIntent appIntent=PendingIntent.getActivity(NotificationExample.this,0,
//                                                                      notifyIntent,0);
//    Notification notification = new Notification();
//    //設定出現在狀態列的圖示
//    notification.icon=R.drawable.icon;
//    //顯示在狀態列的文字
//    notification.tickerText="notification on status bar.";
//    //會有通知預設的鈴聲、振動、light
//    notification.defaults=Notification.DEFAULT_ALL;
//    //設定通知的標題、內容
//    notification.setLatestEventInfo(NotificationExample.this,"Title","content",appIntent);
//    //送出Notification
//    notificationManager.notify(0,notification);
    
    public Calendar forday(Context context,int week,int hour, int minuts) {

    	Calendar calSet=Calendar.getInstance();
        calSet.set(Calendar.DAY_OF_WEEK, week);
        calSet.set(Calendar.HOUR_OF_DAY, hour);
        calSet.set(Calendar.MINUTE, minuts);
        calSet.set(Calendar.SECOND, 0);
        calSet.set(Calendar.MILLISECOND, 0);

      
        
//        am.setRepeating(AlarmManager.RTC_WAKEUP,
//                calSet.getTimeInMillis(), 1 * 60 * 60 * 1000, pendingIntent);
        return calSet;
    }    
}
