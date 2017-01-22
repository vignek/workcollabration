package com.WorkCollabration.App;

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

import com.sheikbro.onlinechat.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddContact extends Activity {
	EditText editName;
	EditText editEmail;
	String userName;
	String userEmailId;
	//SQLiteDatabase MainActivity.centraldb;
	JSONObject jsonObject,inviteJsonObject;
	String addContactURL,inviteURL;
	@SuppressWarnings("deprecation")
	HttpPost request,inviteRequest;
	HttpClient httpClient,inviteHttpClient;
	HttpResponse response,inviteResponse;
	String jsonString;
	StringEntity jsonStringEntity ;
	HttpEntity entity,inviteEntity;
	List<NameValuePair> nameValuePairs,inviteValuePair;
	String responseText,inviteResponseText;
	int result,inviteResult;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_contact);
		MainActivity.centraldb=openOrCreateDatabase(DatabaseHelper.DATABASE_NAME,Context.MODE_PRIVATE,null);
		if (android.os.Build.VERSION.SDK_INT > 9) {
		    StrictMode.ThreadPolicy policy = 
		        new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
		}
	}
	public void openContacts(View v){
		Intent openContacts=new Intent(AddContact.this,Homepage.class);
		startActivity(openContacts);
	}
	@SuppressWarnings("deprecation")
	public void addContact(View v){
		editName=(EditText)findViewById(R.id.editName);
		editEmail=(EditText)findViewById(R.id.editEmail);
		userName=editName.getText().toString();
		userEmailId=editEmail.getText().toString();
		nameValuePairs=new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("EmailId",userEmailId));
		nameValuePairs.add(new BasicNameValuePair("ContactName",userName));
		nameValuePairs.add(new BasicNameValuePair("FromUserId",Integer.toString(MainActivity.globalUserId)));
		addContactURL=new String("https://web.njit.edu/~sm2239/WorkCollaboration/AddContact.php");
		request=new HttpPost(addContactURL);
		httpClient=new DefaultHttpClient();
		try {
			request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			response=httpClient.execute(request);
			entity=response.getEntity();
			responseText=EntityUtils.toString(entity);
			jsonObject=new JSONObject(responseText);
			result=Integer.parseInt(jsonObject.getString("Result").toString());
			Toast.makeText(getApplicationContext(), "OUTSIDE LOOP",Toast.LENGTH_SHORT).show();
			if(result==0){
				System.out.println("check point 0000000000000000000000");
				Log.d("checkpoint","not coming");
				System.out.println("check point 22222222222222222222");
				Intent openContacts=new Intent(AddContact.this,Homepage.class);
				System.out.println("check point 11111111111111");
				startActivity(openContacts);
				System.out.println("printing because contacts is invoked");
			}
			else if(result==1){
				Toast.makeText(getApplicationContext(), "Contact Already Exists", Toast.LENGTH_SHORT).show();
				
			}
			else{
				DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
				    @Override
				    public void onClick(DialogInterface dialog, int which) {
				        switch (which){
				        case DialogInterface.BUTTON_POSITIVE:
				        	inviteValuePair=new ArrayList<NameValuePair>(1);
				        	inviteValuePair.add(new BasicNameValuePair("EmailId",userEmailId));
				        	inviteHttpClient=new DefaultHttpClient();
				        	inviteURL=new String("https://web.njit.edu/~sm2239/WorkCollaboration/Invite.php");
				        	inviteRequest=new HttpPost(inviteURL);
				        	try {
								inviteRequest.setEntity(new UrlEncodedFormEntity(inviteValuePair));
							
				        	inviteResponse=inviteHttpClient.execute(inviteRequest);
				        	inviteEntity=inviteResponse.getEntity();
				        	inviteResponseText=EntityUtils.toString(inviteEntity);
				        	inviteJsonObject=new JSONObject(inviteResponseText);
				        	inviteResult=Integer.parseInt(inviteJsonObject.getString("Result").toString());
				        	} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
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
				        	if (inviteResult==0){
				        		Toast.makeText(getApplicationContext(), "Invite Sent Succefully",Toast.LENGTH_SHORT).show();
				        	}
				        	else{
				        		Toast.makeText(getApplicationContext(), "Invite Not Sent, Try Again ",Toast.LENGTH_SHORT).show();
				        	}
				            //Yes button clicked
				            break;

				        case DialogInterface.BUTTON_NEGATIVE:
				            //No button clicked
				            break;
				        }
				    }
				};
				AlertDialog.Builder builder=new AlertDialog.Builder(this);
				builder.setMessage("User Doesn't have an account in Online Chatting Applicaiton, Do you want to send an Invite?").setPositiveButton("yes",dialogClickListener).setNegativeButton("No",dialogClickListener).show();
			editName.setText("");
			editEmail.setText("");
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		//DatabaseHelper.centraldb=
		// DatabaseHelper.centraldb=openOrCreateDatabase(DatabaseHelper.DATABASE_NAME, Context.MODE_PRIVATE, null);
		ASynchronousTasks.updateContactsTable(this,MainActivity.centraldb);
		
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		//MainActivity.centraldb.close();
		super.onDestroy();
	}
}
