package com.WorkCollabration.App;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sheikbro.onlinechat.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChatWindow extends Activity{
	URI website;
	ListView messageList;
	ChatWindowCustomAdapter chatWindowCustomAdapter;
	SQLiteDatabase db;
	String notee;
	String groupidd,messagee,hourr,minutee,yearr,dayy,monthh;
	//View rootView;
	EditText message,day,month,year,hour,minute,note;
	TextView title;
	int UPDATE_TIME=3000;
	//	SQLiteDatabase MainAcivity.centraldb;
	public ChatWindow  chw=null;
	int isGroupChat;
	int i=1;
	String chatRoomName;
	String chatRoomImage;
	String userIds;
	Uri selectedImageUri;
	String  selectedPath;
	int chatroomId;
	ArrayList<String> fromUsersName;
	ArrayList<Integer> fromUserId;
	ArrayList<String> time;
	ArrayList<String> textMessage;
	ArrayList<String> messageLink;
	ArrayList<String> localPath;
	ArrayList<String> fromUserName;
	String messagePushURL;
	HashMap<Integer,String> UserNames;
	String chatRoomCreationURL;
	HttpPost request;
	HttpClient httpClient;
	HttpResponse response;
	String jsonString;
	StringEntity jsonStringEntity ;
	HttpEntity entity;
	List<NameValuePair> nameValuePairs;
	List<NameValuePair> nameValuePairs2;
	Button sendButton;
	ImageView camera;
	ImageView audio;
	private static MediaRecorder mediaRecorder;
	private static MediaPlayer mediaPlayer;

	private static String audioFilePath;
	private static Button stopButton;
	private static Button playButton;
	private static Button recordButton;

	private boolean isRecording = false;
	private boolean hasMircrophone;
	EditText chatBox;
	int count=0;
	@SuppressWarnings("deprecation")

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chatwindow);

		db=openOrCreateDatabase("Reminder", Context.MODE_PRIVATE, null);
		db.execSQL("CREATE TABLE IF NOT EXISTS reminder(groupid VARCHAR,message VARCHAR,day VARCHAR,month VARCHAR ,year VARCHAR , hour VARCHAR , minute VARCHAR);");
		PackageManager pmanager = this.getPackageManager();
		hasMircrophone=pmanager.hasSystemFeature(
				PackageManager.FEATURE_MICROPHONE);
		audioFilePath = 
				Environment.getExternalStorageDirectory().getAbsolutePath() 
				+ "/myaudio.3gp";
		stopButton=(Button)findViewById(R.id.stop);
		playButton=(Button)findViewById(R.id.play);
		recordButton=(Button)findViewById(R.id.record);

		if (!hasMircrophone)
		{
			stopButton.setEnabled(false);
			playButton.setEnabled(false);
			recordButton.setEnabled(false);
		} else {
			playButton.setEnabled(false);
			stopButton.setEnabled(false);
		}
		MainActivity.centraldb=openOrCreateDatabase(DatabaseHelper.DATABASE_NAME, Context.MODE_PRIVATE, null);	
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = 
					new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
			ASynchronousTasks.updateChatMessagesTable(this,MainActivity.centraldb);
		}


		//rootView = inflater.inflate(R.layout.activity_chatwindow, container, false);
		messageList=(ListView)findViewById(R.id.chatWindow);
		MainActivity.centraldb=openOrCreateDatabase(DatabaseHelper.DATABASE_NAME,Context.MODE_PRIVATE,null);

		//ASynchronousTasks.updateChatRoomUsersTable(this,MainActivity.centraldb);
		//ASynchronousTasks.updateChatMessagesTable(this,MainActivity.centraldb);
		fromUsersName=new ArrayList<String>();
		fromUserId=new ArrayList<Integer>();
		time=new ArrayList<String>();
		textMessage=new ArrayList<String>();
		messageLink=new ArrayList<String>();
		localPath=new ArrayList<String>();
		fromUserName=new ArrayList<String>();
		UserNames=new HashMap<Integer,String>();
		messagePushURL="https://web.njit.edu/~sm2239/WorkCollaboration/ChatMessage_Push.php";
		httpClient=new DefaultHttpClient();
		request=new HttpPost(messagePushURL);
		List<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>(3);
		fromUsersName.clear();
		fromUserId.clear();
		time.clear();
		textMessage.clear();
		messageLink.clear();
		localPath.clear();
		fromUserName.clear();
		title=(TextView)findViewById(R.id.title);
		sendButton=(Button)findViewById(R.id.sendButton);
		camera=(ImageView)findViewById(R.id.camera);
		audio=(ImageView)findViewById(R.id.audio);
		chatBox=(EditText)findViewById(R.id.chatBox);
		chatBox.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if(count==0)
				{
					camera.setVisibility(View.VISIBLE);
					audio.setVisibility(View.VISIBLE);
					sendButton.setVisibility(View.GONE);

				}
				else
				{
					camera.setVisibility(View.GONE);
					audio.setVisibility(View.GONE);
					sendButton.setVisibility(View.VISIBLE);

				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		audio.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//				if(i%2!=0)//odd
				//				{
				//					camera.setVisibility(View.GONE);
				//					chatBox.setVisibility(View.GONE);
				//					recordButton.setVisibility(View.VISIBLE);
				//					playButton.setVisibility(View.VISIBLE);
				//					stopButton.setVisibility(View.VISIBLE);
				//					i++;
				//				}
				//				else //even
				//				{
				//					camera.setVisibility(View.VISIBLE);
				//					chatBox.setVisibility(View.VISIBLE);
				//					recordButton.setVisibility(View.GONE);
				//					playButton.setVisibility(View.GONE);
				//					stopButton.setVisibility(View.GONE);
				//					i++;
				//				}
				final Dialog dialog = new Dialog(ChatWindow.this);
				dialog.setContentView(R.layout.custom2);
				dialog.setTitle("Notes");

				note = (EditText) dialog.findViewById(R.id.note);
				viewnote();

				note.setText(notee);


				Button Set = (Button) dialog.findViewById(R.id.dialogButtonOK);
				// if button is clicked, close the custom dialog
				Set.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						//dialog.dismiss();
						
						notee=note.getText().toString();
						sendnote();
						dialog.dismiss();
					}
				});

				Button Cancel = (Button) dialog.findViewById(R.id.dialogButtonCancel);
				// if button is clicked, close the custom dialog
				Cancel.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});

				dialog.show();
			}

		});

		Bundle bundle=getIntent().getExtras();
		chatroomId=bundle.getInt("chatRoomId");
		isGroupChat=bundle.getInt("isAGroupChat");
		int	friendId=bundle.getInt("friendId");

		System.out.println("*******"+chatroomId);
		System.out.println("*******"+isGroupChat);
		System.out.println("*******"+friendId);

		//is groupchat,friends id,  chatroom id.
		if (isGroupChat==1){
			String query2="Select * from CHATROOM_USERS where ChatRoomId="+chatroomId;
			Cursor d=MainActivity.centraldb.rawQuery(query2,null);
			if (d!=null){
				//Log.d("NOT RETRIVED", "RRr");
				if  (d.moveToFirst()) 
				{
					do {
						chatRoomName=d.getString(d.getColumnIndex("GroupName"));
						chatRoomImage=d.getString(d.getColumnIndex("LocalPath"));
						userIds=d.getString(d.getColumnIndex("UserIds"));
					}while (d.moveToNext());
				}
			}
			d.close();
			title.setText(chatRoomName);
			userIds=userIds.substring(1,userIds.length());
			String []uids=userIds.split(";");
			for (String uid:uids){
				String query3="Select * from CONTACTS where Contacts_UserId="+Integer.parseInt(uid);
				MainActivity.centraldb=openOrCreateDatabase(DatabaseHelper.DATABASE_NAME,Context.MODE_PRIVATE, null);
				Cursor cur=MainActivity.centraldb.rawQuery(query3, null);
				if (cur!=null){
					if(cur.moveToFirst())
					{
						do{
							UserNames.put(Integer.parseInt(uid),cur.getString(cur.getColumnIndex("Contacts_UserName")));
						}while(cur.moveToNext());	
					}
				}
			}


		}
		else{
			for (int i:fromUserId){
				UserNames.put(i, " ");
			}

			System.out.println("Printing frieds id *****"+friendId);
			String query2="SELECT * FROM CONTACTS WHERE Contacts_UserId="+friendId;
			System.out.println("Print Qery******"+query2);
			Cursor d=MainActivity.centraldb.rawQuery(query2,null);
			if (d!=null){
				System.out.println("DDDDDDDDDDDD1");
				Log.d("NOT RETRIVED", "RRr");
				if  (d.moveToFirst()) 
				{
					System.out.println("DDDDDDDDDDDD2");
					do {System.out.println("DDDDDDDDDDDD3"+d.getColumnIndex("Contacts_UserName"));
					chatRoomName=d.getString(d.getColumnIndex("Contacts_UserName"));
					chatRoomImage=d.getString(d.getColumnIndex("LocalPath"));
					}while (d.moveToNext());
				}
			}
			d.close();
			System.out.println("**********ChatRoomName for Individual CHat"+chatRoomName);
			title.setText(chatRoomName);

		}
		chw=this;
		chatWindowCustomAdapter=new ChatWindowCustomAdapter(chw,isGroupChat,time,fromUserId,UserNames,textMessage,messageLink);
		messageList.setAdapter(chatWindowCustomAdapter);
		//messageList.setSelection(chatWindowCustomAdapter.getCount() - 1);
		//chatWindowCustomAdapter.notifyDataSetChanged();
		getMessages();



		camera.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				//selectImage();
				final Dialog dialog = new Dialog(ChatWindow.this);
				dialog.setContentView(R.layout.custom);
				dialog.setTitle("Reminder");

				message = (EditText) dialog.findViewById(R.id.remindername);
				month = (EditText) dialog.findViewById(R.id.month);
				day = (EditText) dialog.findViewById(R.id.day);
				year = (EditText) dialog.findViewById(R.id.year);
				hour = (EditText) dialog.findViewById(R.id.hour);
				minute = (EditText) dialog.findViewById(R.id.minute);




				Button Set = (Button) dialog.findViewById(R.id.dialogButtonOK);
				// if button is clicked, close the custom dialog
				Set.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						//dialog.dismiss();
						//db.execSQL("CREATE TABLE IF NOT EXISTS reminder(groupid VARCHAR,message VARCHAR,day VARCHAR,month VARCHAR ,year VARCHAR , hour VARCHAR , minute VARCHAR);");

						db.execSQL("INSERT INTO reminder VALUES('"+chatroomId+"','"+message.getText().toString()+"','"+day.getText().toString()+"','"+month.getText().toString()+"','"+year.getText().toString()+"','"+hour.getText().toString()+"','"+minute.getText().toString()+"');");
						Cursor c=db.rawQuery("SELECT * FROM reminder",null);

						if(c!=null)
						{
							if  (c.moveToFirst()) 
							{
								do {

									dayy = c.getString(c.getColumnIndex("day"));
									monthh = c.getString(c.getColumnIndex("month"));
									yearr= c.getString(c.getColumnIndex("year"));
									hourr = c.getString(c.getColumnIndex("hour"));
									minutee= c.getString(c.getColumnIndex("minute"));
									messagee=c.getString(c.getColumnIndex("message"));


									//System.out.println(abc);

									//									Toast.makeText(getApplicationContext(), abc, Toast.LENGTH_SHORT).show();
									//									Toast.makeText(getApplicationContext(), abc1, Toast.LENGTH_SHORT).show();
									//									Toast.makeText(getApplicationContext(), abc2, Toast.LENGTH_SHORT).show();
									//									Toast.makeText(getApplicationContext(), abc3, Toast.LENGTH_SHORT).show();
									//									Toast.makeText(getApplicationContext(), abc4, Toast.LENGTH_SHORT).show();

									//	view.setText(abc);

								}while (c.moveToNext());
							}

						}
						else
						{
							//abc="";
							Toast.makeText(getApplicationContext(), "no data", Toast.LENGTH_LONG).show();

						}
						Intent service = new Intent(ChatWindow.this,Reminderservice.class);
						//Toast.makeText(getApplicationContext(), chatroomId, Toast.LENGTH_LONG).show();

						service.putExtra("chatroomid", chatroomId);
						startService(service);
						dialog.dismiss();


					}
				});

				Button Cancel = (Button) dialog.findViewById(R.id.dialogButtonCancel);
				// if button is clicked, close the custom dialog
				Cancel.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						db.execSQL("delete from reminder");

						dialog.dismiss();
					}
				});

				dialog.show();
			}
		});



		playButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				camera.setVisibility(View.VISIBLE);
				chatBox.setVisibility(View.VISIBLE);
				recordButton.setVisibility(View.GONE);
				playButton.setVisibility(View.GONE);
				stopButton.setVisibility(View.GONE);
				i++;
				try {
					uploadaudio(audioFilePath);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		stopButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				stopButton.setEnabled(false);
				playButton.setEnabled(true);

				if (isRecording)
				{	
					recordButton.setEnabled(false);
					mediaRecorder.stop();
					mediaRecorder.release();
					mediaRecorder = null;
					isRecording = false;
				} else {
					mediaPlayer.release();
					mediaPlayer = null;
					recordButton.setEnabled(true);
				}
				Log.d("PATH",audioFilePath);
				try {

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		recordButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isRecording = true;
				stopButton.setEnabled(true);
				playButton.setEnabled(false);
				recordButton.setEnabled(false);

				try {
					mediaRecorder = new MediaRecorder();
					mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
					mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
					mediaRecorder.setOutputFile(audioFilePath);
					mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
					mediaRecorder.prepare();
				} catch (Exception e) {
					e.printStackTrace();
				}

				mediaRecorder.start();	


			}
		});





		sendButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub


				List<NameValuePair> nameValuePairs2=new ArrayList<NameValuePair>(3);
				@SuppressWarnings("unused")
				String sendMessageURL="https://web.njit.edu/~sm2239/WorkCollaboration/ChatMessage_Push.php";
				String message=chatBox.getText().toString();
				chatBox.setText("");
				nameValuePairs2.add(new BasicNameValuePair("IsTextMessage","true"));
				nameValuePairs2.add(new BasicNameValuePair("Message",""+message));
				nameValuePairs2.add(new BasicNameValuePair("FromUserId",""+MainActivity.globalUserId));
				nameValuePairs2.add(new BasicNameValuePair("ChatRoomId",""+chatroomId));
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
						Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_SHORT).show();
					}
					MainActivity.centraldb=openOrCreateDatabase(DatabaseHelper.DATABASE_NAME, Context.MODE_PRIVATE, null);

					ASynchronousTasks.updateChatMessagesTable(ChatWindow.this,MainActivity.centraldb);
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
				sendButton.setVisibility(View.GONE);
				camera.setVisibility(View.VISIBLE);
				audio.setVisibility(View.VISIBLE);
				//ASynchronousTasks.updateChatMessagesTable(MainActivity.centraldb);
				sendMessage(v);
				updateMessageList();
				messageList.setSelection(chatWindowCustomAdapter.getCount() - 1);
			}
		});
	}

	private void selectImage() {

		final CharSequence[] options = { "Choose from Gallery","Cancel" };

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Add Photo!");
		builder.setItems(options, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (options[item].equals("Take Photo"))
				{
					Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
					startActivityForResult(cameraIntent, 1); 
				}
				else if (options[item].equals("Choose from Gallery"))
				{
					Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					System.out.println("******ImagePath"+android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

					startActivityForResult(intent, 2);

				}
				else if (options[item].equals("Cancel")) {
					dialog.dismiss();
				}
			}
		});
		builder.show();
	}

	public void openGallery(int req_code){

		Intent intent = new Intent();

		intent.setType("image/*");

		intent.setAction(Intent.ACTION_GET_CONTENT);

		startActivityForResult(Intent.createChooser(intent,"Select file to upload "), req_code);

	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {



		if (resultCode == RESULT_OK) {
			if(data.getData() != null){
				selectedImageUri = data.getData();
			}else{
				Log.d("selectedPath1 : ","Came here its null !");
				Toast.makeText(getApplicationContext(), "failed to get Image!", 500).show();
			}

			if (requestCode == 1 && resultCode == RESULT_OK) {  
				Bitmap photo = (Bitmap) data.getExtras().get("data");
				//	photo.compress(format, quality, stream)

				selectedPath = getPath(selectedImageUri);
				//	camera.setImageURI(selectedImageUri);
				Log.d("selectedPath1 : " ,selectedPath);
				try {
					upload(selectedPath);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} 

			if (requestCode == 2)

			{

				Uri selectedImage = data.getData();
				String[] filePath = { MediaStore.Images.Media.DATA };
				Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
				c.moveToFirst();
				int columnIndex = c.getColumnIndex(filePath[0]);
				String picturePath = c.getString(columnIndex);
				c.close();
				Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
				Log.w("path of image ", picturePath+"");
				//	camera.setImageBitmap(thumbnail);
				try {
					upload(picturePath);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}

	}

	public void upload(String path) throws Exception
	{
		HttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		HttpPost httppost = new HttpPost("https://web.njit.edu/~sm2239/WorkCollaboration/ChatMessage_Push.php");
		//		HttpPost httppost = new HttpPost("http://localhost/online_chat/upload_file.php");
		try
		{
			//File file = new File(path);
			//			BufferedImage image = ImageIO.read(file);
			//
			//		      File compressedImageFile = new File(path);
			//		      OutputStream os =new FileOutputStream(compressedImageFile);
			//
			//		      Iterator<ImageWriter>writers =  ImageIO.getImageWritersByFormatName("jpg");
			//		      ImageWriter writer = (ImageWriter) writers.next();
			//
			//		      ImageOutputStream ios = ImageIO.createImageOutputStream(os);
			//		      writer.setOutput(ios);
			//
			//		      ImageWriteParam param = writer.getDefaultWriteParam();
			//		      
			//		      param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			//		      param.setCompressionQuality(0.05f);
			//		      writer.write(null, new IIOImage(image, null, null), param);
			//		      
			//		      os.close();
			//		      ios.close();
			//		      writer.dispose();
			//			
			//			FileReader fileReader = new FileReader(file);
			//			BufferedReader bufferedReader = new BufferedReader(fileReader);
			//			StringBuffer stringBuffer = new StringBuffer();
			//			String line;
			//			while ((line = bufferedReader.readLine()) != null)
			//			{
			//				stringBuffer.append(line);
			//				stringBuffer.append("\n");
			//			}
			//			fileReader.close();
			//			System.out.println("Contents of file:");
			//			System.out.println(stringBuffer.toString());
			// File file2 = new File(path);
			Bitmap bmp = BitmapFactory.decodeFile(path);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			bmp.compress(CompressFormat.JPEG, 70, bos);
			InputStream in = new ByteArrayInputStream(bos.toByteArray());
			ContentBody foto = new InputStreamBody(in, "image/jpeg", "filename");
			MultipartEntity mpEntity = new MultipartEntity();
			//ContentBody contentFile = new FileBody(file2);
			mpEntity.addPart("FromUserId", new StringBody(""+MainActivity.globalUserId));
			mpEntity.addPart("IsTextMessage",new StringBody("false"));
			mpEntity.addPart("ChatRoomId",new StringBody(""+chatroomId));
			mpEntity.addPart("userfile", foto);

			httppost.setEntity(mpEntity);
			System.out.println("executing request " + httppost.getRequestLine());
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity resEntity = response.getEntity();
			//			System.out.println(response.getStatusLine().toString());
			if((response.getStatusLine().toString().equals("HTTP/1.1 200 OK")))
			{
				System.out.println("success");
			}
			else
			{
				System.out.println("failed");
			}
			System.out.println(response.getStatusLine());
			if(resEntity!=null)
			{
				//				System.out.println("resEntity:");
				System.out.println(EntityUtils.toString(resEntity));
				resEntity.consumeContent();
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	public String getPath(Uri uri) {

		String[] projection = { MediaStore.Images.Media.DATA };

		Cursor cursor = managedQuery(uri, projection, null, null, null);

		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

		cursor.moveToFirst();

		return cursor.getString(column_index);

	}


	private void getMessages() {
		// TODO Auto-generated method stub

		String query1="SELECT * FROM CHATMESSAGES WHERE ChatRoomId="+chatroomId;
		MainActivity.centraldb=openOrCreateDatabase(DatabaseHelper.DATABASE_NAME, Context.MODE_PRIVATE, null);
		Cursor c=MainActivity.centraldb.rawQuery(query1,null);
		System.out.println("***************data from chatmessages");
		fromUserId.clear();
		time.clear();
		textMessage.clear();
		messageLink.clear();

		if(c!=null)
		{
			System.out.println("****ckp 1****");
			if  (c.moveToFirst()) 
			{
				System.out.println("****ckp 2****");
				do {
					System.out.println("****ckp 3****");
					int u=c.getInt(c.getColumnIndex("FromUserId"));
					System.out.println(u);
					fromUserId.add(u);

					time.add(c.getString(c.getColumnIndex("UpdatedAt")));
					System.out.println(c.getString(c.getColumnIndex("UpdatedAt")));

					textMessage.add(c.getString(c.getColumnIndex("Message")));
					messageLink.add(c.getString(c.getColumnIndex("LocalPath")));
					System.out.println("UUURRRIII*****"+c.getString(c.getColumnIndex("LocalPath")));
					String un;
					if (UserNames.get(u) != null){
						un=UserNames.get(u);
					}
					else{
						un="";
					}
					fromUserName.add(un);
					//fromUserName.add(c.getString(c.getColumnIndex("FromUserName")));

				}while (c.moveToNext());
				System.out.println("Message LInk content *****"+messageLink);
			}
		}
		c.close();
		new Handler().postDelayed(new Runnable(){
			@Override
			public void run() {
				updateMessageList();
			}
		}, UPDATE_TIME);
	}

	public void updateMessageList(){
		MainActivity.centraldb=openOrCreateDatabase(DatabaseHelper.DATABASE_NAME, Context.MODE_PRIVATE, null);

		ASynchronousTasks.updateChatMessagesTable(this,MainActivity.centraldb);
		getMessages();
		chatWindowCustomAdapter.notifyDataSetChanged();

	}


	public void openConversationsPage(View v){
		MainActivity.centraldb=openOrCreateDatabase(DatabaseHelper.DATABASE_NAME, Context.MODE_PRIVATE, null);

		ASynchronousTasks.updateChatRoomUsersTable(this,MainActivity.centraldb);
		MainActivity.centraldb=openOrCreateDatabase(DatabaseHelper.DATABASE_NAME, Context.MODE_PRIVATE, null);

		ASynchronousTasks.updateChatMessagesTable(this,MainActivity.centraldb);
		Intent conversation=new Intent(ChatWindow.this,Homepage.class);
		startActivity(conversation);
	}

	@SuppressWarnings("deprecation")
	public void sendMessage(View v){
		hideSoftKeyboard(this);
	}

	public static void hideSoftKeyboard(Activity activity) {
		InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
	}

	public void uploadaudio(String path) throws Exception
	{
		HttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		HttpPost httppost = new HttpPost("https://web.njit.edu/~sm2239/WorkCollaboration/ChatMessage_Push.php");
		//		HttpPost httppost = new HttpPost("http://localhost/online_chat/upload_file.php");
		try
		{
			File file = new File(path);
			//			FileReader fileReader = new FileReader(file);
			//			BufferedReader bufferedReader = new BufferedReader(fileReader);
			//			StringBuffer stringBuffer = new StringBuffer();
			//			String line;
			//			while ((line = bufferedReader.readLine()) != null)
			//			{
			//				stringBuffer.append(line);
			//				stringBuffer.append("\n");
			//			}
			//			fileReader.close();
			//			System.out.println("Contents of file:");
			//			System.out.println(stringBuffer.toString());

			MultipartEntity mpEntity = new MultipartEntity();
			ContentBody contentFile = new FileBody(file);
			mpEntity.addPart("FromUserId", new StringBody(""+MainActivity.globalUserId));
			mpEntity.addPart("IsTextMessage",new StringBody("false"));
			mpEntity.addPart("ChatRoomId",new StringBody(""+chatroomId));
			mpEntity.addPart("userfile", contentFile);
			httppost.setEntity(mpEntity);
			System.out.println("executing request " + httppost.getRequestLine());
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity resEntity = response.getEntity();
			//			System.out.println(response.getStatusLine().toString());
			if((response.getStatusLine().toString().equals("HTTP/1.1 200 OK")))
			{
				System.out.println("success");
			}
			else
			{
				System.out.println("failed");
			}
			System.out.println(response.getStatusLine());
			if(resEntity!=null)
			{
				//				System.out.println("resEntity:");
				System.out.println(EntityUtils.toString(resEntity));
				resEntity.consumeContent();
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}

	}

	public void sendnote()

	{


		List<NameValuePair> nameValuePairs2=new ArrayList<NameValuePair>(3);
		@SuppressWarnings("unused")
		String sendMessageURL="https://web.njit.edu/~sm2239/WorkCollaboration/InsertNote.php";
		String message=messagee;
		//Toast.makeText(getApplicationContext(), notee+""+chatroomId, Toast.LENGTH_SHORT).show();
		//chatBox.setText("");
		//	nameValuePairs2.add(new BasicNameValuePair("Message","true"));
		nameValuePairs2.add(new BasicNameValuePair("IsTextMessage","true"));

		nameValuePairs2.add(new BasicNameValuePair("NoteId",""+chatroomId));
		nameValuePairs2.add(new BasicNameValuePair("NoteData",notee));
		//nameValuePairs2.add(new BasicNameValuePair("FromUserId",""+MainActivity.globalUserId));
		//	nameValuePairs2.add(new BasicNameValuePair("ChatRoomId",""+chatroomId));
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

			ASynchronousTasks.updateChatMessagesTable(ChatWindow.this,MainActivity.centraldb);
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
		sendButton.setVisibility(View.GONE);
		camera.setVisibility(View.VISIBLE);
		audio.setVisibility(View.VISIBLE);
		//ASynchronousTasks.updateChatMessagesTable(MainActivity.centraldb);
		//	sendMessage(v);
		updateMessageList();
		messageList.setSelection(chatWindowCustomAdapter.getCount() - 1);

	}


	
	public void viewnote()

	{


		List<NameValuePair> nameValuePairs2=new ArrayList<NameValuePair>(3);
		@SuppressWarnings("unused")
		String sendMessageURL="https://web.njit.edu/~sm2239/WorkCollaboration/GetNotes.php";
		String message=messagee;
		//Toast.makeText(getApplicationContext(), notee+""+chatroomId, Toast.LENGTH_SHORT).show();
		//chatBox.setText("");
		//	nameValuePairs2.add(new BasicNameValuePair("Message","true"));
		nameValuePairs2.add(new BasicNameValuePair("IsTextMessage","true"));

		nameValuePairs2.add(new BasicNameValuePair("NoteId",""+chatroomId));
		//nameValuePairs2.add(new BasicNameValuePair("FromUserId",""+MainActivity.globalUserId));
		//	nameValuePairs2.add(new BasicNameValuePair("ChatRoomId",""+chatroomId));
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
			 notee=jsonObject.getJSONObject("NoteData").getString("NoteData");
			int result=Integer.parseInt(jsonObject.getString("Result").toString());
			if (result==0){
				//Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_SHORT).show();
			}
			MainActivity.centraldb=openOrCreateDatabase(DatabaseHelper.DATABASE_NAME, Context.MODE_PRIVATE, null);

			ASynchronousTasks.updateChatMessagesTable(ChatWindow.this,MainActivity.centraldb);
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
		sendButton.setVisibility(View.GONE);
		camera.setVisibility(View.VISIBLE);
		audio.setVisibility(View.VISIBLE);
		//ASynchronousTasks.updateChatMessagesTable(MainActivity.centraldb);
		//	sendMessage(v);
		updateMessageList();
		messageList.setSelection(chatWindowCustomAdapter.getCount() - 1);

	}

	//	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//	super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		//MainActivity.centraldb.close();
		super.onDestroy();
	}
}