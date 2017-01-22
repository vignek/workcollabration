
package com.WorkCollabration.App;

import java.io.IOException;
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
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class Grouppage extends Activity{
	ArrayList<Integer> UserId=new ArrayList<Integer>();
	ArrayList<String> UserName=new ArrayList<String>();
	public static ArrayList<Integer> Selection=new ArrayList<Integer>();
	ArrayList<String> LocalPath=new ArrayList<String>();
	ListView groupList;
	CustomGroupPageAdapter groupListAdapter;
	String chatRoomCreationURL;
	HttpPost request;
	HttpClient httpClient;
	HttpResponse response;
	HttpEntity entity;
	JSONObject jsonObject;
	Grouppage gp=null;
	List<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grouppage);
//		UserId.clear();
//		UserName.clear();
//		Selection.clear();
//		LocalPath.clear();
		
		MainActivity.centraldb=openOrCreateDatabase(DatabaseHelper.DATABASE_NAME, Context.MODE_PRIVATE, null);	
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = 
					new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		String query1="select * from CONTACTS where IsAContact=1";
		Cursor c=null;
				
		c=MainActivity.centraldb.rawQuery(query1,null);
		if (c!=null){
			if(c.moveToFirst()){
				do{
					UserName.add(c.getString(c.getColumnIndex("Contacts_UserName")));
					UserId.add(c.getInt(c.getColumnIndex("Contacts_UserId")));
					LocalPath.add(c.getString(c.getColumnIndex("LocalPath")));
					Selection.add(0);
					
				}while(c.moveToNext());
			}
		}
		gp=this;
		groupList=(ListView)findViewById(R.id.conclist);
		//groupList=getListView();
		groupList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		System.out.println("******printing username"+UserId);
		groupListAdapter=new CustomGroupPageAdapter(gp,UserName,UserId,LocalPath,Selection);
		groupList.setAdapter(groupListAdapter);
	//	groupList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
	}
	
	public void createGroup(View v){
		
		String a=new String(";");
		System.out.println("**********values***"+Grouppage.Selection);
	
		for (int i = 0; i < groupList.getCount(); i++) {
		  System.out.println("**********SElevtionvaleu"+Grouppage.Selection.get(i));
			if(Grouppage.Selection.get(i)==1){
				a=new String(a+UserId.get(i)+";");
				
			}
			else{
				
			}
			
		}
		a=new String(a+MainActivity.globalUserId+";");
		EditText groupName=(EditText)findViewById(R.id.groupName);
		String gname=groupName.getText().toString();
		nameValuePairs.add(new BasicNameValuePair("IsGroupChat","true"));
		nameValuePairs.add(new BasicNameValuePair("UserIds",a));
		nameValuePairs.add(new BasicNameValuePair("GroupName",gname));
		String groupURL="https://web.njit.edu/~sm2239/WorkCollaboration/ChatRoomCreation.php";
		httpClient=new DefaultHttpClient();
		request=new HttpPost(groupURL);
		try {
			request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			response=httpClient.execute(request);
            entity=response.getEntity();
            String responseText=EntityUtils.toString(entity);
			jsonObject=new JSONObject(responseText);
          	int creation=Integer.parseInt(jsonObject.getString("Result").toString());
          	if(creation==0){
          		Toast.makeText(this,"Group Created",Toast.LENGTH_SHORT).show();
          		ASynchronousTasks.updateChatRoomUsersTable(this, MainActivity.centraldb);
          	}
          	else{
          		
          	}
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
		Intent chatwindow=new Intent(Grouppage.this,Homepage.class);
		startActivity(chatwindow);
	}
	
	public void cancelGroup(View v){
		Intent back=new Intent(Grouppage.this,Homepage.class);
		startActivity(back);
	}
}
