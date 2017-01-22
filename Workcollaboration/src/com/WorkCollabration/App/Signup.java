package com.WorkCollabration.App;

/**
 * Created by Hari on 10/17/2015.
 */

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ParseException;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.sheikbro.onlinechat.R;

public class Signup extends Activity {

	EditText editName;
	EditText editEmail;
	EditText editPassword;
	String userName;
	String userEmailId;
	String userPassword;
	JSONObject jsonObject;
	String signUpURL;
	HttpPost request;
	HttpClient httpClient;
	HttpResponse response;
	String jsonString;
	StringEntity jsonStringEntity ;
	HttpEntity entity;
	//SQLiteDatabase db;
	InputStream is;
	StringBuilder stringBuilder;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
		    StrictMode.ThreadPolicy policy = 
		        new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
		}

	}
	public void openSignIn(View v){
		Intent openSignIn=new Intent(Signup.this,Loginpage.class);
		startActivity(openSignIn);
	}
	//    public void openSignIn(View view){
		//
		////
		////        String query1="Insert into Users (UserName, EmailId, User_Password, CreatedAt, UserStatus, UserPictureLink, StatusUpdate) values (\" "+editName.getText()+"\",\""+editEmail.getText()+"\",\""+editPassword.getText()+"\", \"Test123\",1,null,null)";
	////        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
	////        SQLiteDatabase db2 = db.getWritableDatabase();
	////        db2.execSQL(query1);
	////        db.close();
	//        Intent intent=new Intent(this, SignIn.class);
	//        startActivity(intent);
	//    }
	@SuppressWarnings("deprecation")
	public void submitSignUp(View v){
		

		editName   = (EditText)findViewById(R.id.editname);
		editEmail   = (EditText)findViewById(R.id.editemail);
		editPassword   = (EditText)findViewById(R.id.editpassword);
		userName=new String(editName.getText().toString());
		userEmailId=new String(editEmail.getText().toString());
		userPassword=new String(editPassword.getText().toString());
		signUpURL=new String("https://web.njit.edu/~sm2239/WorkCollaboration/SignUp.php");
		httpClient=new DefaultHttpClient();
		 request=new HttpPost(signUpURL);
		List<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>(3);
		nameValuePairs.add(new BasicNameValuePair("UserName",userName));
		nameValuePairs.add(new BasicNameValuePair("Password",userPassword));
		nameValuePairs.add(new BasicNameValuePair("EmailId",userEmailId));
		System.out.println("**********basenamevalue");
		
		try {
			request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			System.out.println("**********baseEntity");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		try {
			HttpResponse response = httpClient.execute(request);
            entity=response.getEntity();
            String responseText=EntityUtils.toString(entity);
			jsonObject=new JSONObject(responseText);
          	int existing=Integer.parseInt(jsonObject.getString("Result").toString());
          	if (existing==0){
          		Intent verification=new Intent(Signup.this,Verification.class);
          		verification.putExtra("emailid", userEmailId);
          		startActivity(verification);
          	}
          	else{
          		Toast.makeText(getApplicationContext(), "User Already Exists, Enter another Email ID", Toast.LENGTH_SHORT).show();
          	}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();

		}catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (org.apache.http.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		//db.close();
		super.onDestroy();
	}
}