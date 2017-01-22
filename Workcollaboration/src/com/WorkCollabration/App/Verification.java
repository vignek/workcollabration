package com.WorkCollabration.App;

import java.io.IOException;
import java.io.InputStream;
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
import org.json.JSONObject;

import com.sheikbro.onlinechat.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Verification extends Activity{
	EditText verificationId;
	String userEmailId;
	JSONObject jsonObject;
	String signUpURL;
	HttpPost request;
	HttpClient httpClient;
	HttpResponse response;
	String jsonString;
	StringEntity jsonStringEntity ;
	HttpEntity entity;
	InputStream is;
	String verificationCode;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle=getIntent().getExtras();
		userEmailId=bundle.getString("emailid");
		setContentView(R.layout.activity_verification);
		if (android.os.Build.VERSION.SDK_INT > 9) {
		    StrictMode.ThreadPolicy policy = 
		        new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
		}

	}
	@SuppressWarnings("deprecation")
	public void submitVerificaitonId(View v){
		verificationId=(EditText)findViewById(R.id.editVerificationId);
		int verificationCode=Integer.parseInt(verificationId.getText().toString());

		signUpURL=new String("https://web.njit.edu/~sm2239/WorkCollaboration/Verification.php");
		httpClient=new DefaultHttpClient();
		 request=new HttpPost(signUpURL);
     	System.out.println("**************BO"+verificationCode);

		List<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>(3);
		nameValuePairs.add(new BasicNameValuePair("VerificationCode",Integer.toString(verificationCode)));
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
        	System.out.println("ResponseTExt"+responseText);
          	jsonObject=new JSONObject(responseText);
          	int registration=Integer.parseInt(jsonObject.getString("Result").toString());
          	if (registration==1){
          		Toast.makeText(getApplicationContext(), "Successfully Registered", Toast.LENGTH_SHORT).show();
          		Intent signIn=new Intent(Verification.this,Loginpage.class);
          		startActivity(signIn);
          	}
          	else{
          		Toast.makeText(getApplicationContext(), "Invalid Verfication Code, Try Again", Toast.LENGTH_SHORT).show();
          	}
			Log.d("Http Response:", response.toString());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();

		}catch(Exception e){
        	System.out.println("EXCEPTION HANDLED");
        }
            
	}
}
