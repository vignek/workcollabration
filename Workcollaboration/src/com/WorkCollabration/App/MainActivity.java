package com.WorkCollabration.App;



import com.sheikbro.onlinechat.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends Activity {
	private final int SPLASH_DISPLAY_LENGTH = 1000;
	public static int globalUserId;
	public static String globalUserName;
	public static String globalEmailId;
	public static String globalStatus;
	public static String globalProf;
	public static String globalLastDate;


	public static SQLiteDatabase centraldb;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MainActivity.centraldb=openOrCreateDatabase(DatabaseHelper.DATABASE_NAME, Context.MODE_PRIVATE, null);
		setContentView(R.layout.activity_main);
		initializeDatabase();
		/* New Handler to start the Menu-Activity 
		 * and close this Splash-Screen after some seconds.*/
		new Handler().postDelayed(new Runnable(){
			@Override
			public void run() {
				/* Create an Intent that will start the Menu-Activity. */
				 SharedPreferences userId= getSharedPreferences("com.onlinechat.app.userInfo", Context.MODE_PRIVATE);
	        	  MainActivity.globalUserId=userId.getInt("userId", 0);
	        	  MainActivity.globalUserName=userId.getString("userName", "User Name");
	        	  MainActivity.globalEmailId=userId.getString("emailId", "Email Id");
	        	  MainActivity.globalProf=userId.getString("ProfilePicture", null);
	        	  MainActivity.globalLastDate=userId.getString("dateAdded", null);
	        	  MainActivity.globalStatus=userId.getString("StatusUpdate", null);
				if (MainActivity.globalUserId==0){
					Intent mainIntent = new Intent(MainActivity.this,Loginpage.class);
					MainActivity.this.startActivity(mainIntent);
				}
				else{
					Intent mainIntent = new Intent(MainActivity.this,Homepage.class);
					MainActivity.this.startActivity(mainIntent);
				}

				//  ASynchronousTasks.updateChatMessagesTable(db);
				MainActivity.this.finish();

			}
		}, SPLASH_DISPLAY_LENGTH);

	}
	public void initializeDatabase(){

		SharedPreferences prefs = this.getSharedPreferences(
				"com.onlinechat.app.install", Context.MODE_PRIVATE);
		Boolean status= prefs.getBoolean("firstTime",false);
		Toast.makeText(getApplicationContext(), "this is my Toast message!!! =)"+status,
				Toast.LENGTH_LONG);
		DatabaseHelper db = new DatabaseHelper(getApplicationContext());
		if(!status){
			SharedPreferences sp = getSharedPreferences("com.onlinechat.app.install", Activity.MODE_PRIVATE);
			SharedPreferences.Editor editor = sp.edit();
			editor.putBoolean("firstTime", true);
			editor.commit();
		}
	}
}
