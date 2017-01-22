package com.WorkCollabration.App;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.squareup.picasso.Picasso;

import android.content.Context;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Environment;

public class  ASynchronousTasks extends SQLiteOpenHelper{
	static String aSyncURL;
	static HttpPost request;
	static HttpClient httpClient;
	static HttpResponse response;
	static String jsonString;
	static StringEntity jsonStringEntity ;
	static HttpEntity entity;
	static List<NameValuePair> nameValuePairs;
	static SQLiteDatabase db;
	static String defaultDate="2015-08-23 23:23:02";
	static String asd = null;
	static String lastDate;
static Context commonContext;

	public ASynchronousTasks(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	public static void updateUsersTable(Context context,SharedPreferences userId){
		commonContext=context;
		ASynchronousTasks.db=db;
		aSyncURL="https://web.njit.edu/~sm2239/WorkCollaboration/A_UserDetails.php";
		nameValuePairs=new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("MaxDate",MainActivity.globalLastDate));
		nameValuePairs.add(new BasicNameValuePair("FromId",Integer.toString(MainActivity.globalUserId)));
		request=new HttpPost(aSyncURL);
		httpClient=new DefaultHttpClient();
		try {
			request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		try {
			HttpResponse response = httpClient.execute(request);
			entity=response.getEntity();
			String responseText=EntityUtils.toString(entity);
			JSONObject jsonObject = new JSONObject(responseText);
			int result=Integer.parseInt(jsonObject.getString("Result").toString());
			if (result==0){
				
			}
			else{
				JSONObject jsonObject2=new JSONObject(jsonObject.getString("UserDetails"));
				String pictureLink = jsonObject2.getString("UserPictureLink").toString();
				String dateAdded= jsonObject2.getString("UpdatedAt").toString();
				String localPath=getLocalPath(commonContext,pictureLink);
				String statusUpdate= jsonObject2.getString("StatusUpdate").toString();
				SharedPreferences tuserId= userId;
				SharedPreferences.Editor editor = tuserId.edit();
				editor.putInt("userId", MainActivity.globalUserId);
				editor.putString("emailId", MainActivity.globalEmailId);
				editor.putString("userName", MainActivity.globalUserName);
				editor.putString("ProfilePicture", localPath);
				editor.putString("StatusUpdate", statusUpdate);
				editor.putString("dateAdded",dateAdded );
				editor.commit();
				MainActivity.globalStatus=statusUpdate;
				MainActivity.globalProf=localPath;
				}
		
			}
		catch(Exception e){
			
		}
	}
	@SuppressWarnings("deprecation")
	public static void updateContactsTable(Context context,SQLiteDatabase db){
		commonContext=context;
		ASynchronousTasks.db=db;
		 lastDate=defaultDate;
		new ASyncContacts().execute();
		//ASynchronousTasks.db.close();
		//db.close();
	}
	static String getLocalPath(Context context,String pictureLink) {
		// TODO Auto-generated method stub
		if (pictureLink==null||pictureLink.equals("null")||pictureLink.equals("")){
			return null;
		}
		else{
			String localPath=pictureLink.substring(40);
			System.out.println("****calling get local path function");
			URL wallpaperURL;
			File cacheFile=null;
			try {
				System.out.println("InsideGetLocalPath******");
				String temp=new String("https://harp.njit.edu/~hhm4/UPLOADS/"+pictureLink.substring(40));
				System.out.println("********temp"+temp);
				wallpaperURL = new URL(temp);
				@SuppressWarnings("unused")
				URLConnection connection = wallpaperURL.openConnection();
				InputStream inputStream = new BufferedInputStream(wallpaperURL.openStream(), 10240);
				File cacheDir = getDataFolder(context);
				cacheFile = new File(cacheDir, pictureLink.substring(40));
				System.out.println("FilePath*******"+cacheFile.getAbsolutePath());
				FileOutputStream outputStream = new FileOutputStream(cacheFile);

				byte buffer[] = new byte[1024];
				int dataSize;
				int loadedSize = 0;
				while ((dataSize = inputStream.read(buffer)) != -1) {
					loadedSize += dataSize;
					outputStream.write(buffer, 0, dataSize);
				}

				outputStream.close();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			localPath=cacheFile.getAbsolutePath();
			return localPath;
		}
	}
	public static File getDataFolder(Context context) {
		File dataDir = null;
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			dataDir = new File(Environment.getExternalStorageDirectory(), "OnlineChat");
			if(!dataDir.isDirectory()) {
				dataDir.mkdirs();
			}
		}

		if(!dataDir.isDirectory()) {
			dataDir = context.getFilesDir();
		}

		return dataDir;
	}
	@SuppressWarnings("deprecation")
	public static void updateChatRoomUsersTable(Context context,SQLiteDatabase db){
		System.out.println("Update chat Room users table is invoked by async*************");
		ASynchronousTasks.db=db;
		commonContext=context;
		lastDate=defaultDate;
		new ASyncChatroom().execute();
	//	ASynchronousTasks.db.close();
		//db.close();
	}

	@SuppressWarnings("deprecation")
	public static void updateChatMessagesTable(Context context,SQLiteDatabase db){
		System.out.println("Update chat Messages table is invoked by async*************");
		ASynchronousTasks.db=db;
		commonContext=context;
		lastDate=defaultDate;
		new ASyncChatmessage().execute();
		//ASynchronousTasks.db.close();
		//db.close();
		//Select max(UpdatedAt) From CHATMESSAGES
		//Insert into CHATMESSAGES(MessageId,ChatRoomId,FromUserId,Message,MessageLink,UpdatedAt,LocalPath) values (1,1,1,"hi helo","dddd","3222","222222");
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
//new Asyncharisheikbro().execute();
	}

	public static class ASyncContacts extends AsyncTask<String, String, String>
	{
		 @Override
         protected void onPreExecute() {
             super.onPreExecute();
             // Shows Progress Bar Dialog and then call doInBackground method
//             showDialog(progress_bar_type);
         }
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			aSyncURL="https://web.njit.edu/~sm2239/WorkCollaboration/A_ContactDetails.php";
			String query1="Select max(Contacts_DateAdded) as maxdate From CONTACTS";
			Cursor c=null;
			c=ASynchronousTasks.db.rawQuery(query1, null);
			if (c!=null){
				if(c.moveToFirst()){

					do{
						System.out.println("value*****"+c.getString(c.getColumnIndex("maxdate")));
						lastDate=c.getString(c.getColumnIndex("maxdate"));
					}
					while(c.moveToNext());
				}
			}

			nameValuePairs=new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("MaxDate",lastDate));
			nameValuePairs.add(new BasicNameValuePair("FromId",Integer.toString(MainActivity.globalUserId)));
			request=new HttpPost(aSyncURL);
			httpClient=new DefaultHttpClient();
			try {
				request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			try {
				HttpResponse response = httpClient.execute(request);
				entity=response.getEntity();
				String responseText=EntityUtils.toString(entity);
				JSONObject jsonObject = new JSONObject(responseText);
				int result=Integer.parseInt(jsonObject.getString("Result").toString());
				if (result==0){

				}
				else{
					JSONArray contacts=jsonObject.getJSONArray("Contacts");
					for(int i=0;i<contacts.length();i++){
						JSONObject contact = contacts.getJSONObject(i);
						int contactId = Integer.parseInt(contact.getString("ContactId").toString());
						int userId = Integer.parseInt(contact.getString("Contacts_UserId").toString());
						int fromUserId = Integer.parseInt(contact.getString("Contacts_FromUserId").toString());
						int contactStatus = Integer.parseInt(contact.getString("Contacts_Status").toString());
						int isacontact=Integer.parseInt(contact.getString("IsAContact").toString());
						String contactUserName = contact.getString("Contacts_UserName").toString();
						String contactEmailId = contact.getString("Contacts_EmailId").toString();
						String contactStatusUpdate = contact.getString("Contacts_StatusUpdate").toString();
						String contactPictureLink = contact.getString("Contacts_PictureLink").toString();
						String contactDateAdded= contact.getString("Contacts_DateAdded").toString();
						String localPath=getLocalPath(commonContext,contactPictureLink);
						String query2="Insert or replace into CONTACTS(ContactId,Contacts_UserId,Contacts_FromUserId,Contacts_UserName,Contacts_EmailId,Contacts_Status,Contacts_PictureLink,Contacts_StatusUpdate,Contacts_DateAdded,IsAContact,LocalPath)"+
								"values ("+contactId+","+userId+","+fromUserId+",'"+contactUserName+"','"+contactEmailId+"',"+contactStatus+",'"+contactPictureLink+"','"+contactStatusUpdate+"','"+contactDateAdded+"',"+isacontact+",'"+localPath+"')";
						db.execSQL(query2);
					}
				}
			}
			catch(Exception e){

			}
			return null;
		}
		 // While Downloading Music File
        protected void onProgressUpdate(String... progress) {
            // Set progress percentage
//            prgDialog.setProgress(Integer.parseInt(progress[0]));
        }

        // Once Music File is downloaded
        @Override
        protected void onPostExecute(String file_url) {
            // Dismiss the dialog after the Music file was downloaded
//            dismissDialog(progress_bar_type);
//            Toast.makeText(getApplicationContext(), "Download complete, playing Music", Toast.LENGTH_LONG).show();
//            // Play the music
//            playMusic();
        }
	}

	public static class ASyncChatroom extends AsyncTask<String, String, String>
	{
		 @Override
         protected void onPreExecute() {
             super.onPreExecute();
             // Shows Progress Bar Dialog and then call doInBackground method
//             showDialog(progress_bar_type);
         }
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub}
			String query1="Select max(UpdatedAt) as maxdate From CHATROOM_USERS";
			
			//SQLiteDatabase db2;
			aSyncURL="https://web.njit.edu/~sm2239/WorkCollaboration/A_ChatRoomDetails.php";
			Cursor c=null;
			
			c=ASynchronousTasks.db.rawQuery(query1, null);
			
			if (c!=null){
				if(c.moveToFirst()){

					do{
						System.out.println("value*****"+c.getString(c.getColumnIndex("maxdate")));
						lastDate=c.getString(c.getColumnIndex("maxdate"));
						System.out.println("While loop invoked by async*************");
					}
					while(c.moveToNext());
				}
			}

			nameValuePairs=new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("MaxDate",lastDate));
			nameValuePairs.add(new BasicNameValuePair("FromId",Integer.toString(MainActivity.globalUserId)));
			request=new HttpPost(aSyncURL);
			httpClient=new DefaultHttpClient();
			System.out.println("chkppppp1******************");
			try {
				//	System.out.println("chkppppp2******************");
				request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			try {
				//	System.out.println("chkppppp3******************");
				HttpResponse response = httpClient.execute(request);
				//	System.out.println("chkppppp4******************");
				entity=response.getEntity();

				String responseText=EntityUtils.toString(entity);
				JSONObject jsonObject = new JSONObject(responseText);
				//	System.out.println("chkppppp5******************");
				int result=Integer.parseInt(jsonObject.getString("Result").toString());
				if (result==0){
					//		System.out.println("chkppppp6******************");
				}
				else{
					//		System.out.println("chkppppp7******************");
					JSONArray chatRoomDetails=jsonObject.getJSONArray("ChatRoomDetails");
					for(int i=0;i<chatRoomDetails.length();i++){
						System.out.println("chkppppp8******************");
						JSONObject chatRoom = chatRoomDetails.getJSONObject(i);
						int chatRoomId = Integer.parseInt(chatRoom.getString("ChatRoomId").toString());
						String userIds = chatRoom.getString("UserIds").toString();
						int isGroupChat=Integer.parseInt(chatRoom.getString("IsGroupChat").toString());
						String groupName = chatRoom.getString("GroupName").toString();
						String groupImage = chatRoom.getString("GroupImage").toString();
						String updatedAt = chatRoom.getString("UpdatedAt").toString();
						String localPath=getLocalPath(commonContext,groupImage);
						String query2="Insert into CHATROOM_USERS(ChatRoomId,UserIds,IsGroupChat,GroupName,GroupImage,UpdatedAt,LocalPath)"+ 
								"values ("+chatRoomId+",'"+userIds+"',"+isGroupChat+",'"+groupName+"','"+groupImage+"','"+updatedAt+"','"+localPath+"')";
						db.execSQL(query2);
						System.out.println("chatroom users end ********");
					}
				}
			}
			catch(Exception e){
				//ASynchronousTasks.db.close();
			}
			return null;
		}
		 // While Downloading Music File
        protected void onProgressUpdate(String... progress) {
            // Set progress percentage
//            prgDialog.setProgress(Integer.parseInt(progress[0]));
        }

        // Once Music File is downloaded
        @Override
        protected void onPostExecute(String file_url) {
            // Dismiss the dialog after the Music file was downloaded
//            dismissDialog(progress_bar_type);
//            Toast.makeText(getApplicationContext(), "Download complete, playing Music", Toast.LENGTH_LONG).show();
//            // Play the music
//            playMusic();
        }
	}

	public static class ASyncChatmessage extends AsyncTask<String, String, String>
	{
		 @Override
         protected void onPreExecute() {
             super.onPreExecute();
             // Shows Progress Bar Dialog and then call doInBackground method
//             showDialog(progress_bar_type);
         }
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub}}
			String query1="Select max(UpdatedAt) as maxdate From CHATMESSAGES";

			
			aSyncURL="https://web.njit.edu/~sm2239/WorkCollaboration/A_ChatMessages.php";
			Cursor c=null;
			c=ASynchronousTasks.db.rawQuery(query1, null);
			if (c!=null){

				if(c.moveToFirst()){

					do{
						System.out.println("value*****"+c.getString(c.getColumnIndex("maxdate")));
						lastDate=c.getString(c.getColumnIndex("maxdate"));
					}
					while(c.moveToNext());
				}
			}

			nameValuePairs=new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("MaxDate",lastDate));
			nameValuePairs.add(new BasicNameValuePair("FromId",Integer.toString(MainActivity.globalUserId)));
			request=new HttpPost(aSyncURL);
			httpClient=new DefaultHttpClient();
			System.out.println("*****CCCCCCCpoint1*******");
			try {
				System.out.println("*****CCCCCCCpoint2*******");

				request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			try {
				System.out.println("*****CCCCCCCpoint3*******");

				HttpResponse response = httpClient.execute(request);
				entity=response.getEntity();
				String responseText=EntityUtils.toString(entity);
				JSONObject jsonObject = new JSONObject(responseText);
				System.out.println("*****CCCCCCCpoint4*******");

				int result=Integer.parseInt(jsonObject.getString("Result").toString());
				if (result==0){
					System.out.println("*****CCCCCCCpoint5*******");

				}
				else{
					System.out.println("*****CCCCCCCpoint6*******");

					JSONArray messages=jsonObject.getJSONArray("Messages");
					System.out.println("*****CCCCCCCpoint7*******");

					for(int i=0;i<messages.length();i++){
						JSONObject message = messages.getJSONObject(i);
						System.out.println("*****CCCCCCCpoint8*******");

						int messageId = Integer.parseInt(message.getString("MessageId").toString());
						String chatRoomId = message.getString("ChatRoomId").toString();
						int fromUserId=Integer.parseInt(message.getString("FromUserId").toString());
						String messageText = message.getString("Message").toString();
						String messageLink = message.getString("MessageLink").toString();
						String updatedAt = message.getString("UpdatedAt").toString();
						String localPath=getLocalPath(commonContext,messageLink);
						System.out.println("*****CCCCCCCpoint9*******");

						String query2="Insert into CHATMESSAGES (MessageId,ChatRoomId,FromUserId,Message,MessageLink,UpdatedAt,LocalPath) values"+ 
								"("+messageId+","+chatRoomId+","+fromUserId+",'"+messageText+"','"+messageLink+"','"+updatedAt+"','"+localPath+"')" ;

						System.out.println("query****"+query2);
						db.execSQL(query2);
						System.out.println("*****CCCCCCCpoint10*******");
					}
				}
			}
			catch(Exception e){

			}
			return null;
		}
		 // While Downloading Music File
        protected void onProgressUpdate(String... progress) {
            // Set progress percentage
//            prgDialog.setProgress(Integer.parseInt(progress[0]));
        }

        // Once Music File is downloaded
        @Override
        protected void onPostExecute(String file_url) {
            // Dismiss the dialog after the Music file was downloaded
//            dismissDialog(progress_bar_type);
//            Toast.makeText(getApplicationContext(), "Download complete, playing Music", Toast.LENGTH_LONG).show();
//            // Play the music
//            playMusic();
        }
	}

}
