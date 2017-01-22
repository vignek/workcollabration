package com.WorkCollabration.App;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;




import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sheikbro.onlinechat.R;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.Animator.AnimatorListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Loginpage extends Activity {
	int userStatus;
	int uId;
	int contactId;
	Timer timer;

	TimerTask timerTask;
	String baselink;
	final Handler handler = new Handler();
	int contacts_userId;
	int contacts_FromUserId;
	private static int SPLASH_TIME_OUT = 3500;

	int contacts_status;
	EditText editName;
	private TextView windowsTvOne;
	private TextView windowsTvThree;
	private TextView windowsTvTwo;
	private int screenWidth;
	private AnimatorSet windowsAnimatorSet;
	EditText editEmail;
	EditText editPassword;
	String userName;
	String userEmailId;
	String userPassword;
	String updatedAt;
	String signInQuery;
	String contacts_UserName;
	String contacts_EmailId;
	String contacts_dateAdded;
	String contacts_pictureLink;
	String contacts_statusUpdate;
	String userPictureLink;
	String statusUpdate;
	String contactsQuery;
	JSONArray contacts;
	JSONArray chatRoom;
	JSONArray chatMessage;
	JSONObject jsonObject;
	JSONObject contactDetails;
	JSONObject chatRoomDetails;
	JSONObject chatMessageDetails;
	String signInURL;
	HttpPost request;
	HttpClient httpClient;
	HttpResponse response;
	String jsonString;
	StringEntity jsonStringEntity ;
	HttpEntity entity;
	List<NameValuePair> nameValuePairs;

	public void openSignUp(View v){
		Intent signUp=new Intent(Loginpage.this,Signup.class);
		startActivity(signUp);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loginpage);
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = 
					new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		windowsTvOne = (TextView) findViewById(R.id.windowsTvOne);
		windowsTvTwo = (TextView) findViewById(R.id.windowsTvTwo);
		windowsTvThree = (TextView) findViewById(R.id.windowsTvThree);
		windowsTvOne.setVisibility(View.GONE);
		windowsTvTwo.setVisibility(View.GONE);
		windowsTvThree.setVisibility(View.GONE);
		DisplayMetrics displaymetrics = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		screenWidth = displaymetrics.widthPixels;



	}
	@SuppressWarnings("deprecation")
	public void submitSignIn(View v){
		if(isNetworkAvailable(this))
		{

			windowsAnimation();
			editEmail= (EditText)findViewById(R.id.editEmail);
			editPassword= (EditText)findViewById(R.id.editPassword);
			userEmailId=new String(editEmail.getText().toString());
			userPassword=new String(editPassword.getText().toString());
			signInURL=new String("https://web.njit.edu/~sm2239/WorkCollaboration/SignIn.php");
			httpClient=new DefaultHttpClient();
			request=new HttpPost(signInURL);
			nameValuePairs=new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("Password",userPassword));
			nameValuePairs.add(new BasicNameValuePair("EmailId",userEmailId));
			try {
				request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			try {
				HttpResponse response = httpClient.execute(request);
				entity=response.getEntity();
				String responseText=EntityUtils.toString(entity);
				System.out.println("ResponseTExt*******"+responseText);
				jsonObject=new JSONObject(responseText);
				int authentication=Integer.parseInt(jsonObject.getString("Result").toString());
				if (authentication==1){
					uId=Integer.parseInt(jsonObject.getString("UserId").toString());
					String usnm=jsonObject.getString("UserName").toString();
					String emld=jsonObject.getString("EmailId").toString();
					String statUpdt=jsonObject.getString("StatusUpdate").toString();
					String prof=jsonObject.getString("UserPictureLink").toString();
					SharedPreferences userId= getSharedPreferences("com.onlinechat.app.userInfo", Context.MODE_PRIVATE);
					SharedPreferences.Editor editor = userId.edit();
					editor.putInt("userId", uId);
					editor.putString("emailId", emld);
					editor.putString("userName", usnm);
					editor.putString("StatusUpdate", statUpdt);
					statUpdt=ASynchronousTasks.getLocalPath(this, prof);
					editor.putString("ProfilePicture", prof);
					editor.commit();
					MainActivity.globalUserId=uId;
					MainActivity.globalEmailId=emld;
					MainActivity.globalUserName=usnm;
					MainActivity.globalStatus=statUpdt;
					MainActivity.globalProf=prof;
					Intent homepage=new Intent(Loginpage.this,Homepage.class);
					homepage.putExtra("emailid", userEmailId);
					System.out.println("****BeforeSetUpDatabase*******");
					setUpDatabase();
					System.out.println("****AfterSetUpDatabase*******");
					startActivity(homepage);
				}
				else{
					Toast.makeText(getApplicationContext(), "Invalid Credentials, Try Again", Toast.LENGTH_SHORT).show();
				}

			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();

			}catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
		}
	}
	public void setUpDatabase(){
		DatabaseHelper db = new DatabaseHelper(getApplicationContext());
		SQLiteDatabase db2 = db.getWritableDatabase();
		insertUserQuery(db2);
		insertContactsQuery(db2);
		insertChatroomQuery(db2);
		insertChatmessageQuery(db2);
		//db.close();
		Toast.makeText(getApplicationContext(), "Database Setup completed", Toast.LENGTH_SHORT).show();

	}
	private void insertChatmessageQuery(SQLiteDatabase db2) {
		// TODO Auto-generated method stub

		try {
			chatMessage=jsonObject.getJSONArray("Chatmessage");
			for(int i=0;i<chatMessage.length();i++){
				chatMessageDetails = chatMessage.getJSONObject(i);
				int messageId = Integer.parseInt(chatMessageDetails.getString("MessageId").toString());
				int chatMessageRoomId = Integer.parseInt(chatMessageDetails.getString("ChatRoomId").toString());
				int fromUserId = Integer.parseInt(chatMessageDetails.getString("FromUserId").toString());
				String message = chatMessageDetails.getString("Message").toString();
				String messageLink = chatMessageDetails.getString("MessageLink").toString();
				String localPath=new String();
				localPath=ASynchronousTasks.getLocalPath(this,messageLink);
				String chatMessageupdatedAt = chatMessageDetails.getString("UpdatedAt").toString();
				String chatMessageQuery = "insert into CHATMESSAGES (MessageId, ChatRoomId, FromUserId, Message, MessageLink,LocalPath, UpdatedAt) values ("+messageId+","+chatMessageRoomId+","+fromUserId+",'"+message+"','"+messageLink+"','"+localPath+"','"+chatMessageupdatedAt+"')";
				db2.execSQL(chatMessageQuery);
				System.out.println(chatMessageQuery);
			}
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void insertChatroomQuery(SQLiteDatabase db2) {
		// TODO Auto-generated method stub

		try {

			chatRoom=jsonObject.getJSONArray("Chatroom");
			for(int i=0;i<chatRoom.length();i++){
				chatRoomDetails = chatRoom.getJSONObject(i);
				int chatRoomId = Integer.parseInt(chatRoomDetails.getString("ChatRoomId").toString());
				String userIds = chatRoomDetails.getString("UserIds").toString();
				int isGroupChat = Integer.parseInt(chatRoomDetails.getString("IsGroupChat").toString());
				String groupName = chatRoomDetails.getString("GroupName").toString();
				String groupImage = chatRoomDetails.getString("GroupImage").toString();
				String localPath=new String();
				localPath=ASynchronousTasks.getLocalPath(this,groupImage);
				String chatRoomUpdatedAt = chatRoomDetails.getString("UpdatedAt").toString();
				String chatRoomQuery = "insert into CHATROOM_USERS (ChatRoomId, UserIds, IsGroupChat, GroupName, GroupImage,LocalPath, UpdatedAt) values ("+chatRoomId+",'"+userIds+"',"+isGroupChat+",'"+groupName+"','"+groupImage+"','"+localPath+"','"+chatRoomUpdatedAt+"')";
				db2.execSQL(chatRoomQuery);
				System.out.println(chatRoomQuery);
			}
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void insertContactsQuery(SQLiteDatabase db2) {
		// TODO Auto-generated method stub


		try {

			contacts=jsonObject.getJSONArray("Contacts");
			for(int i=0;i<contacts.length();i++){
				contactDetails = contacts.getJSONObject(i);
				contactId=Integer.parseInt(contactDetails.getString("ContactId").toString());
				contacts_userId=Integer.parseInt(contactDetails.getString("Contacts_UserId").toString());
				contacts_FromUserId=Integer.parseInt(contactDetails.getString("Contacts_FromUserId").toString());
				contacts_status=Integer.parseInt(contactDetails.getString("Contacts_Status").toString());
				contacts_UserName=contactDetails.getString("Contacts_UserName").toString();
				contacts_EmailId=contactDetails.getString("Contacts_EmailId").toString();
				contacts_dateAdded=contactDetails.getString("Contacts_DateAdded").toString();
				contacts_pictureLink=contactDetails.getString("Contacts_PictureLink").toString();
				String localPath=new String();
				localPath=ASynchronousTasks.getLocalPath(this,contacts_pictureLink);
				contacts_statusUpdate=contactDetails.getString("Contacts_StatusUpdate").toString();
				int isacontact=Integer.parseInt(contactDetails.getString("IsAContact").toString());
				contactsQuery="Insert into CONTACTS (ContactId,Contacts_UserId,Contacts_FromUserId,Contacts_UserName,Contacts_EmailId,Contacts_Status,Contacts_DateAdded,Contacts_PictureLink,Contacts_StatusUpdate,LocalPath, IsAContact)"+
						"values("+contactId+","+contacts_userId+","+contacts_FromUserId+",'"+contacts_UserName+"','"+contacts_EmailId+"',"+contacts_status+",'"+contacts_dateAdded+"','"+contacts_pictureLink+"','"+contacts_statusUpdate+"','"+localPath+"',"+isacontact+")";
				db2.execSQL(contactsQuery);
			}
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void windowsAnimation() {

		final ValueAnimator valueTvOne_x = ObjectAnimator.ofFloat(windowsTvOne, "x", windowsTvOne.getX() - 40, windowsTvOne.getX() - 50, (screenWidth / 2) + 10f, (screenWidth / 2) + 25f,
				(screenWidth / 2) + 50f//,(screenWidth / 2)+55f//,(screenWidth / 2)+80f//,(screenWidth/2 )+25f,(screenWidth / 2) +30f//, (screenWidth / 2)+35f,(screenWidth / 2)+40f,(screenWidth / 2)+45f //,(screenWidth / 2)+6.6f,(screenWidth / 2)+7.7f,(screenWidth / 2)+8.8f //, (screenWidth / 2)+9,(screenWidth / 2)+10,
				, screenWidth * .92f, screenWidth + 5);

		valueTvOne_x.setDuration(5200);
		valueTvOne_x.setRepeatCount(0);
		valueTvOne_x.setRepeatMode(ValueAnimator.REVERSE);

		valueTvOne_x.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {
				windowsTvOne.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animator animation) {

			}

			@Override
			public void onAnimationEnd(Animator animation) {

			}

			@Override
			public void onAnimationCancel(Animator animation) {

			}
		});

		final ValueAnimator valueTvTwo_x = ObjectAnimator.ofFloat(windowsTvTwo, "x", windowsTvTwo.getX() - 50, (screenWidth / 2.1f) + 10f, (screenWidth / 2.1f) + 25f, (screenWidth / 2.1f) + 50f//, (screenWidth / 2.1f) +55f//, (screenWidth / 2.1f) +80f//, (screenWidth / 2.1f) +25f,(screenWidth / 2.1f) +30f
				, screenWidth * .94f, screenWidth + 5);

		valueTvTwo_x.setDuration(6000);
		valueTvTwo_x.setRepeatCount(0);
		valueTvTwo_x.setStartDelay(200);
		valueTvTwo_x.setRepeatMode(ValueAnimator.REVERSE);

		valueTvTwo_x.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {
				windowsTvTwo.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animator animation) {

			}

			@Override
			public void onAnimationEnd(Animator animation) {

			}

			@Override
			public void onAnimationCancel(Animator animation) {

			}
		});

		final ValueAnimator valueTvThree_x = ObjectAnimator.ofFloat(windowsTvThree, "x", windowsTvThree.getX() - 50, (screenWidth / 2.2f) + 10f, (screenWidth / 2.2f) + 25f, (screenWidth / 2.2f) + 50f//,(screenWidth / 2.2f) +55f//,(screenWidth / 2.2f) +80f//,(screenWidth / 2.2f) +25f,(screenWidth / 2.2f) +30f
				, screenWidth * .94f, screenWidth + 5);

		valueTvThree_x.setDuration(6500);
		valueTvThree_x.setRepeatCount(0);
		valueTvTwo_x.setStartDelay(500);
		valueTvThree_x.setRepeatMode(ValueAnimator.REVERSE);

		valueTvThree_x.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {
				windowsTvThree.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animator animation) {

			}

			@Override
			public void onAnimationEnd(Animator animation) {

			}

			@Override
			public void onAnimationCancel(Animator animation) {

			}
		});

		windowsAnimatorSet = new AnimatorSet();
		windowsAnimatorSet.playTogether(valueTvTwo_x, valueTvThree_x, valueTvOne_x);

		windowsAnimatorSet.start();
		windowsAnimatorSet.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {

			}

			@Override
			public void onAnimationRepeat(Animator animation) {

			}

			@Override
			public void onAnimationEnd(Animator animation) {

				windowsAnimatorSet.start();
			}

			@Override
			public void onAnimationCancel(Animator animation) {
			}
		});

	}
	public void initializeTimerTask() {



		timerTask = new TimerTask() {

			public void run() {



				//use a handler to run a toast that shows the current timestamp

				handler.post(new Runnable() {

					public void run() {

						//get the current timeStamp



					}

				});

			}

		};

	}

	public boolean isNetworkAvailable(final Context context) {
		final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
		return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
	}


	private void insertUserQuery(SQLiteDatabase db2) {
		// TODO Auto-generated method stub
		try {
			userName=jsonObject.getJSONObject("Users").getString("UserName").toString();
			updatedAt=jsonObject.getJSONObject("Users").getString("UpdatedAt").toString();
			userStatus=Integer.parseInt(jsonObject.getJSONObject("Users").getString("UserStatus").toString());
			userPictureLink=jsonObject.getJSONObject("Users").getString("UserPictureLink").toString();
			String localPath=new String();
			localPath=ASynchronousTasks.getLocalPath(this,userPictureLink);
			statusUpdate=jsonObject.getJSONObject("Users").getString("StatusUpdate").toString();
			signInQuery="Insert into USERS(UserId,UserName,EmailId,User_Password,LocalPath,UpdatedAt) values ("+uId+",'"+userName+"','"+userEmailId+"','"+userPassword+"','"+localPath+"','"+updatedAt+"');";
			db2.execSQL(signInQuery);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
