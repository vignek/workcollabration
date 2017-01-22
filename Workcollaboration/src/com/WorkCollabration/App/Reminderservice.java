package com.WorkCollabration.App;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class Reminderservice extends Service {
	String asd="REMINDER STARTED";

	final Handler h = new Handler();
	ChatWindow cht = new ChatWindow();
	String messagee,groupidd,chatroomid;
	SQLiteDatabase db;





	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		chatroomid = intent.getExtras().getString("chatroomid");
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);

		chatroomid = intent.getExtras().getString("chatroomid");

		Log.d("Groupchat", asd);
		//this.stopSelf();
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		final int delay = 3000; //milliseconds
		// chatroomid = intent.getStringExtra("chatroomid");
		//Toast.makeText(getApplicationContext(), chatroomid, Toast.LENGTH_LONG).show();


		h.postDelayed(new Runnable(){
			public void run(){

				//do something
				String day = null,month = null,year=null,hour=null,minute=null;
				Calendar ca = Calendar.getInstance(); 
				db=openOrCreateDatabase("Reminder", Context.MODE_PRIVATE, null);
				db.execSQL("CREATE TABLE IF NOT EXISTS reminder(groupid VARCHAR,message VARCHAR,day VARCHAR,month VARCHAR ,year VARCHAR , hour VARCHAR , minute VARCHAR);");
				int dayy2 = ca.get(Calendar.DAY_OF_MONTH);
				String dayy=Integer.toString(dayy2);
				int monthh = ca.get(Calendar.MONTH);
				int newmonth2=monthh+1;
				String newmonth=Integer.toString(newmonth2);

				int yearr2 = ca.get(Calendar.YEAR);
				String yearr=Integer.toString(yearr2);

				int hourr2 = ca.get(Calendar.HOUR);
				String hourr=Integer.toString(hourr2);

				int minutee2 = ca.get(Calendar.MINUTE);
				String minutee=Integer.toString(minutee2);

				Cursor c=db.rawQuery("SELECT * FROM reminder",null);
				if(c!=null)
				{
					if  (c.moveToFirst()) 
					{
						do {
							groupidd=c.getString(c.getColumnIndex("groupid"));
							messagee=c.getString(c.getColumnIndex("message"));
							day = c.getString(c.getColumnIndex("day"));
							month = c.getString(c.getColumnIndex("month"));
							year = c.getString(c.getColumnIndex("year"));
							hour = c.getString(c.getColumnIndex("hour"));
							minute = c.getString(c.getColumnIndex("minute"));

							//System.out.println(abc);
							//	Toast.makeText(getApplicationContext(), abc, Toast.LENGTH_LONG).show();
							//	view.setText(abc);

						}while (c.moveToNext());
					}

				}
				else
				{
					//abc="";
					//	Toast.makeText(getApplicationContext(), "no data", Toast.LENGTH_LONG).show();

				}
				//				System.out.println(day+"=="+dayy );
				//				System.out.println(month+"=="+newmonth );
				//				System.out.println(year+"=="+yearr );
				//				System.out.println(hour+"=="+hourr );
				//				System.out.println(month+"=="+newmonth );


				if(Integer.parseInt(day) == Integer.parseInt(dayy) && Integer.parseInt(month) == Integer.parseInt(newmonth)&&Integer.parseInt(year) == Integer.parseInt(yearr)&&Integer.parseInt(hour) == Integer.parseInt(hourr)&&Integer.parseInt(minutee) == Integer.parseInt(minute))
				{
					sendmessage();
					//					Toast.makeText(getApplicationContext(), day+"=="+dayy, Toast.LENGTH_LONG).show();
					//					Toast.makeText(getApplicationContext(), month+"=="+newmonth, Toast.LENGTH_LONG).show();
					//					Toast.makeText(getApplicationContext(), year+"=="+yearr, Toast.LENGTH_LONG).show();
					//					Toast.makeText(getApplicationContext(), hour+"=="+hourr, Toast.LENGTH_LONG).show();
					//					Toast.makeText(getApplicationContext(), minute+"=="+minutee, Toast.LENGTH_LONG).show();

					//	Toast.makeText(getApplicationContext(), "Reminder Alert", Toast.LENGTH_LONG).show();
					db.execSQL("delete from reminder");

					stopSelf();
				}
				else
				{

				}
				h.postDelayed(this, delay);
				Log.d("","RUNNING");
			}
		}, delay);
		return Service.START_STICKY;
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		h.removeCallbacksAndMessages(null);
		Log.d("Groupchat","Reminder stopped");

	}

	public void sendmessage()

	{


		List<NameValuePair> nameValuePairs2=new ArrayList<NameValuePair>(3);
		@SuppressWarnings("unused")
		String sendMessageURL="https://web.njit.edu/~sm2239/WorkCollaboration/ChatMessage_Push.php";
		String message=messagee;
		//chatBox.setText("");
		nameValuePairs2.add(new BasicNameValuePair("IsTextMessage","true"));
		nameValuePairs2.add(new BasicNameValuePair("Message",""+message));
		nameValuePairs2.add(new BasicNameValuePair("FromUserId",""+MainActivity.globalUserId));
		nameValuePairs2.add(new BasicNameValuePair("ChatRoomId",""+groupidd));
		System.out.println("printing NameValuePairs********"+nameValuePairs2);
		DefaultHttpClient httpClient2 = new DefaultHttpClient();
		HttpPost request2 = new HttpPost(sendMessageURL);

		try {
			System.out.println("CheckPoint********50");

			request2.setEntity(new UrlEncodedFormEntity(nameValuePairs2));
			HttpResponse response2 = httpClient2.execute(request2);
			HttpEntity entity2 = response2.getEntity();
			String responseText=EntityUtils.toString(entity2);
			System.out.println("ResponseTExt*******"+responseText);
			JSONObject jsonObject=new JSONObject(responseText);
			int result=Integer.parseInt(jsonObject.getString("Result").toString());
			if (result==0){
				//Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_SHORT).show();
			}
			MainActivity.centraldb=openOrCreateDatabase(DatabaseHelper.DATABASE_NAME, Context.MODE_PRIVATE, null);

			ASynchronousTasks.updateChatMessagesTable(Reminderservice.this,MainActivity.centraldb);
			//////Code for if the chat room is not created
			//
			//
			//

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("******Send MessageButton is invoked*******");

		//ASynchronousTasks.updateChatMessagesTable(MainActivity.centraldb);
		//	sendMessage(v);
		//	updateMessageList();
		//messageList.setSelection(chatWindowCustomAdapter.getCount() - 1);

	}





}
