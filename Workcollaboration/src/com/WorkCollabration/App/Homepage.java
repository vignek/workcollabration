package com.WorkCollabration.App;



import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.WorkCollabration.App.ContactsCustomAdapter.ViewHolder;
import com.sheikbro.onlinechat.R;

import android.R.color;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Homepage extends Activity {

	LinearLayout contactss,chatss,myaccc;
	Fragment Contactspage= new Contactspage();
	Fragment Chatpage= new Chatpage();
	Fragment Myaccpage= new Myaccpage();
	TextView conc,chattt,myacccc;
	//SQLiteDatabase MainActivity.centraldb;
	String deleteUsers;
	String deleteContacts;
	HttpPost request;
	HttpClient httpClient;
	HttpResponse response;
	String jsonString;
	StringEntity jsonStringEntity ;
	HttpEntity entity;
	List<NameValuePair> nameValuePairs;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_homepage);
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = 
					new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		MainActivity.centraldb=openOrCreateDatabase(DatabaseHelper.DATABASE_NAME, Context.MODE_PRIVATE, null);
		ASynchronousTasks.updateChatRoomUsersTable(this,MainActivity.centraldb);
		 MainActivity.centraldb=openOrCreateDatabase(DatabaseHelper.DATABASE_NAME, Context.MODE_PRIVATE, null);

		ASynchronousTasks.updateChatMessagesTable(this,MainActivity.centraldb);
		contactss=(LinearLayout)findViewById(R.id.contacts);
		chatss=(LinearLayout)findViewById(R.id.chat);
		myaccc=(LinearLayout)findViewById(R.id.myacc);
		conc=(TextView)findViewById(R.id.contacctt);
		chattt=(TextView)findViewById(R.id.chattxt);
		myacccc=(TextView)findViewById(R.id.myacctxt);
		conc.setTextColor(Color.parseColor("#ffffff"));
		chattt.setTextColor(Color.parseColor("#000000"));
		myacccc.setTextColor(Color.parseColor("#000000"));
		contactss.setBackgroundResource(com.sheikbro.onlinechat.R.color.violet);
		chatss.setBackgroundResource(com.sheikbro.onlinechat.R.color.white);
		myaccc.setBackgroundResource(com.sheikbro.onlinechat.R.color.white);
		RelativeLayout customfrag=(RelativeLayout)findViewById(R.id.customfrag);
		RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);

		p.addRule(RelativeLayout.BELOW, R.id.custom);
		p.addRule(RelativeLayout.ABOVE, R.id.footer);

		customfrag.setLayoutParams(p);
		FragmentTransaction transaction = getFragmentManager().beginTransaction();

		
		//kumkibro
		transaction.replace(R.id.fragment_container,Contactspage);
		transaction.addToBackStack(null);

		
		transaction.commit();


		contactss.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub
				conc.setTextColor(Color.parseColor("#ffffff"));
				chattt.setTextColor(Color.parseColor("#000000"));
				myacccc.setTextColor(Color.parseColor("#000000"));

				contactss.setBackgroundResource(com.sheikbro.onlinechat.R.color.violet);
				chatss.setBackgroundResource(com.sheikbro.onlinechat.R.color.white);
				myaccc.setBackgroundResource(com.sheikbro.onlinechat.R.color.white);

				RelativeLayout customfrag=(RelativeLayout)findViewById(R.id.customfrag);
				RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
						ViewGroup.LayoutParams.WRAP_CONTENT);

				p.addRule(RelativeLayout.BELOW, R.id.custom);
				p.addRule(RelativeLayout.ABOVE, R.id.footer);

				customfrag.setLayoutParams(p);
				FragmentTransaction transaction = getFragmentManager().beginTransaction();

				// Replace whatever is in the fragment_container view with this fragment,
				// and add the transaction to the back stack if needed
				transaction.replace(R.id.fragment_container,Contactspage);
				transaction.addToBackStack(null);

				// Commit the transaction
				transaction.commit();
				return false;
			}
		});
		chatss.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				conc.setTextColor(Color.parseColor("#000000"));
				chattt.setTextColor(Color.parseColor("#ffffff"));
				myacccc.setTextColor(Color.parseColor("#000000"));
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub
				chatss.setBackgroundResource(com.sheikbro.onlinechat.R.color.violet);
				myaccc.setBackgroundResource(com.sheikbro.onlinechat.R.color.white);
				contactss.setBackgroundResource(com.sheikbro.onlinechat.R.color.white);



				RelativeLayout customfrag=(RelativeLayout)findViewById(R.id.customfrag);
				RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
						ViewGroup.LayoutParams.WRAP_CONTENT);

				p.addRule(RelativeLayout.BELOW, R.id.custom);
				p.addRule(RelativeLayout.ABOVE, R.id.footer);

				customfrag.setLayoutParams(p);
				FragmentTransaction transaction = getFragmentManager().beginTransaction();

				// Replace whatever is in the fragment_container view with this fragment,
				// and add the transaction to the back stack if needed
				transaction.replace(R.id.fragment_container,Chatpage);
				transaction.addToBackStack(null);

				// Commit the transaction
				transaction.commit();
				return false;
			}
		});
		myaccc.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub

				conc.setTextColor(Color.parseColor("#000000"));
				chattt.setTextColor(Color.parseColor("#000000"));
				myacccc.setTextColor(Color.parseColor("#ffffff"));
				myaccc.setBackgroundResource(com.sheikbro.onlinechat.R.color.violet);
				chatss.setBackgroundResource(com.sheikbro.onlinechat.R.color.white);
				contactss.setBackgroundResource(com.sheikbro.onlinechat.R.color.white);

				RelativeLayout customfrag=(RelativeLayout)findViewById(R.id.customfrag);
				RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
						ViewGroup.LayoutParams.WRAP_CONTENT);

				p.addRule(RelativeLayout.BELOW, R.id.custom);
				p.addRule(RelativeLayout.ABOVE, R.id.footer);

				customfrag.setLayoutParams(p);
				FragmentTransaction transaction = getFragmentManager().beginTransaction();

				// Replace whatever is in the fragment_container view with this fragment,
				// and add the transaction to the back stack if needed
				transaction.replace(R.id.fragment_container,Myaccpage);
				transaction.addToBackStack(null);

				// Commit the transaction
				transaction.commit();
				return false;
			}
		});



	}
	public void openAddContacts(View v){
		Intent addContacts=new Intent(Homepage.this,AddContact.class);
		startActivity(addContacts);
	}
	
	public void logOut(View v){
		SharedPreferences userId= getSharedPreferences("com.onlinechat.app.userInfo", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = userId.edit();
		editor.putInt("userId", 0);
		editor.putString("StatusUpdate", "");
		editor.putString("emailId", "");
		
		editor.commit();
		MainActivity.globalUserId=0;
		MainActivity.globalEmailId="";
		MainActivity.globalStatus="";
		MainActivity.globalUserName="";
		clearDatabase();
		Intent signIn=new Intent(Homepage.this,Loginpage.class);
		startActivity(signIn);
	}
	private void clearDatabase() {
		// TODO Auto-generated method stub
		deleteUsers="delete from USERS";
		deleteContacts="delete from CONTACTS";
		String deleteChatRoomUsers = "delete from CHATROOM_USERS";
		String CHATMESSAGES="delete from CHATMESSAGES";
		MainActivity.centraldb = openOrCreateDatabase(DatabaseHelper.DATABASE_NAME,Context.MODE_PRIVATE,null);
		MainActivity.centraldb.execSQL(deleteUsers);
		MainActivity.centraldb.execSQL(deleteContacts);
		MainActivity.centraldb.execSQL(deleteChatRoomUsers);
		MainActivity.centraldb.execSQL(CHATMESSAGES);
		
	}
	@SuppressWarnings({ "static-access", "deprecation", "unchecked", "rawtypes" })
	public void onItemClick(View v,int mPosition, int flagType,String userEmailId, int  friendId) {
		// TODO Auto-generated method stub;
		int chatRoomId=0;
		if (flagType==1){
			
			if(MainActivity.globalUserId>friendId){
				chatRoomId=(MainActivity.globalUserId*1000)+friendId;
			}
			else{
				chatRoomId=(friendId*1000)+MainActivity.globalUserId;
			}
			Bundle bundle = new Bundle();
			Boolean exists=false;
			MainActivity.centraldb=openOrCreateDatabase(DatabaseHelper.DATABASE_NAME, Context.MODE_PRIVATE, null);
			String query3="Select * from CHATROOM_USERS where ChatRoomId="+chatRoomId;
			Cursor a=MainActivity.centraldb.rawQuery(query3, null);
			if (a!=null){
				//Log.a("NOT RETRIVED", "RRr");
				if  (a.moveToFirst()) 
				{
					do {
						if (chatRoomId==a.getInt(a.getColumnIndex("ChatRoomId"))){
							exists=true;
						}

					}while(a.moveToNext());
				}
			}
			if(!exists){
List nameValuePairs2=new ArrayList<NameValuePair>(3);
				nameValuePairs2.add(new BasicNameValuePair("IsGroupChat","0"));
				nameValuePairs2.add(new BasicNameValuePair("UserIds",";"+MainActivity.globalUserId+";"+friendId+";"));
				nameValuePairs2.add(new BasicNameValuePair("ChatRoomId",""+chatRoomId));
				String chatRoomCreationURL="https://web.njit.edu/~sm2239/WorkCollaboration/ChatRoomCreation.php";
				httpClient=new DefaultHttpClient();
				request=new HttpPost(chatRoomCreationURL);
				try {

					request.setEntity(new UrlEncodedFormEntity(nameValuePairs2));
					response=httpClient.execute(request);
					entity=response.getEntity();
					String responseText=EntityUtils.toString(entity);
					System.out.println("ResponseTExt*******"+responseText);
					JSONObject jsonObject=new JSONObject(responseText);
					int result=Integer.parseInt(jsonObject.getString("Result").toString());
					if (result==0){
						Toast.makeText(getApplicationContext(), "Chat Room Created", Toast.LENGTH_SHORT).show();
					}
					 MainActivity.centraldb=openOrCreateDatabase(DatabaseHelper.DATABASE_NAME, Context.MODE_PRIVATE, null);

					ASynchronousTasks.updateChatRoomUsersTable(this,MainActivity.centraldb);
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


			}
			
			
			
			
		Intent chat=new Intent(Homepage.this,ChatWindow.class);
		bundle.putInt("chatRoomId", chatRoomId);
		bundle.putInt("friendId", friendId);
		bundle.putInt("isAGroupChat", 0);
		System.out.println("******(*****");
		chat.putExtras(bundle);
		startActivity(chat);
		}
		if (flagType==2){
			int contactId=0;
			String query1="SELECT * FROM CONTACTS where Contacts_EmailId='"+userEmailId+"'";
			String deleteContactURL=new String("https://web.njit.edu/~sm2239/WorkCollaboration/DeleteContact.php");
			MainActivity.centraldb = openOrCreateDatabase(DatabaseHelper.DATABASE_NAME,Context.MODE_PRIVATE,null);
			Cursor e =null;
			e= MainActivity.centraldb.rawQuery(query1, null);
			if(e!=null)
			{
				if  (e.moveToFirst()) 
				{
					do {
						contactId=e.getInt(e.getColumnIndex("ContactId"));
						}while (e.moveToNext());
					}
				}
			httpClient=new DefaultHttpClient();
			request=new HttpPost(deleteContactURL);
			nameValuePairs=new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("ContactId",Integer.toString(contactId)));
			try {
				request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			} catch (UnsupportedEncodingException exp) {
				System.out.println("Exception handleed********");
				exp.printStackTrace();
			}

			try {
				HttpResponse response = httpClient.execute(request);
				entity=response.getEntity();
				String responseText=EntityUtils.toString(entity);
				System.out.println("ResponseTExt*******"+responseText);
				JSONObject jsonObject = new JSONObject(responseText);
				int result=Integer.parseInt(jsonObject.getString("Result").toString());
				if (result==0){
					Toast.makeText(getApplicationContext(), "ContactDeletedSuccefully", Toast.LENGTH_SHORT).show();
				}
			    MainActivity.centraldb=openOrCreateDatabase(DatabaseHelper.DATABASE_NAME,Context.MODE_PRIVATE,null);
			    //DatabaseHelper MainActivity.centraldb = new DatabaseHelper(getApplicationContext());
			  //  SQLiteDatabase db2 = MainActivity.centraldb.getWritableDatabase();
				ASynchronousTasks.updateContactsTable(this,MainActivity.centraldb);
				
			}
			catch(Exception ed){
				
			}
		}
	}
	public void openChatWindow(int chatRoomId, int friendId, int isAGroupChat) {
		// TODO Auto-generated method stub
		Bundle bundle=new Bundle();
		Intent conversations=new Intent(Homepage.this,ChatWindow.class);
		bundle.putInt("chatRoomId", chatRoomId);
		bundle.putInt("friendId", friendId);
		bundle.putInt("isAGroupChat", isAGroupChat);
		conversations.putExtras(bundle);
		startActivity(conversations);
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		//MainActivity.centraldb.close();
		super.onDestroy();
	}
	public void updateStatus(View v){
		Button done=(Button)findViewById(R.id.statusEditDoneButton);
		Button edit=(Button)findViewById(R.id.statusEditButton);
		TextView status=(TextView)findViewById(R.id.statusUpdate);
		TextView tempStatus=(TextView)findViewById(R.id.editStatusUpdate);
		edit.setVisibility(View.GONE);
		done.setVisibility(View.VISIBLE);
		
		status.setVisibility(View.GONE);
		tempStatus.setVisibility(View.VISIBLE);
		tempStatus.setText(status.getText().toString());

	}
	public void doneStatus(View v){
		Button done=(Button)findViewById(R.id.statusEditDoneButton);
		Button edit=(Button)findViewById(R.id.statusEditButton);
		TextView status=(TextView)findViewById(R.id.statusUpdate);
		TextView tempStatus=(TextView)findViewById(R.id.editStatusUpdate);
		String s=tempStatus.getText().toString();
		
		done.setVisibility(View.GONE);
		edit.setVisibility(View.VISIBLE);
		tempStatus.setVisibility(View.GONE);
		status.setVisibility(View.VISIBLE);
		
	String statusUpdateURL="https://web.njit.edu/~sm2239/WorkCollaboration/StatusUpdate.php";
		httpClient=new DefaultHttpClient();
		request=new HttpPost(statusUpdateURL);
		ArrayList nameValuePairs2=new ArrayList<NameValuePair>(1);
		nameValuePairs2.add(new BasicNameValuePair("UserId",Integer.toString(MainActivity.globalUserId)));
		nameValuePairs2.add(new BasicNameValuePair("StatusUpdate",s));

		try {
			request.setEntity(new UrlEncodedFormEntity(nameValuePairs2));
		} catch (UnsupportedEncodingException exp) {
			System.out.println("Exception handleed********");
			exp.printStackTrace();
		}

		try {
			HttpResponse response = httpClient.execute(request);
			entity=response.getEntity();
			String responseText=EntityUtils.toString(entity);
			System.out.println("ResponseTExt*******"+responseText);
			JSONObject jsonObject = new JSONObject(responseText);
			int result=Integer.parseInt(jsonObject.getString("Result").toString());
			if (result==0){
				Toast.makeText(getApplicationContext(), "Status Updated Succefully", Toast.LENGTH_SHORT).show();
			
			}
		    MainActivity.centraldb=openOrCreateDatabase(DatabaseHelper.DATABASE_NAME,Context.MODE_PRIVATE,null);
		    //DatabaseHelper MainActivity.centraldb = new DatabaseHelper(getApplicationContext());
		  //  SQLiteDatabase db2 = MainActivity.centraldb.getWritableDatabase();
			ASynchronousTasks.updateUsersTable(this,getSharedPreferences("com.onlinechat.app.userInfo", Context.MODE_PRIVATE));
			
		}
		catch(Exception ed){
			
		}
		status.setText(MainActivity.globalStatus);
		
	}
	
	public void editProfilePicture(View v){
		
		///Insert image code here
		
		
		
		
		
		
		ImageView prof=(ImageView)findViewById(R.id.profilePicture);
		
		File imgFile = new  File(MainActivity.globalProf);
	    if(imgFile.exists())
	    {
	    	System.out.println("UUURRRRIII****location");
	       // prof.setImageURI(Uri.fromFile(imgFile));

	    }
	}
public void groupCreation(View v){
		Intent createGroup=new Intent(Homepage.this,Grouppage.class);
		startActivity(createGroup);
	}
	

}



