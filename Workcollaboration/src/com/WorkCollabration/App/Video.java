package com.WorkCollabration.App;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

import com.sheikbro.onlinechat.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Video extends Activity {
	private String videoPath;
	Uri selectedImageUri;

	Button video;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = 
					new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		video=(Button)findViewById(R.id.bat);
		video.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
				startActivityForResult(Intent.createChooser(cameraIntent, "Take Video"), 1);


			}
		});
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			if(data.getData() != null){
				selectedImageUri = data.getData();
			}else{
				Log.d("selectedPath1 : ","Came here its null !");
				Toast.makeText(getApplicationContext(), "failed to get Video!", 500).show();
			}

			if (requestCode == 1 && resultCode == RESULT_OK) {  
				videoPath = getRealPathFromURI(selectedImageUri);
				Log.d("selectedPath1 : ",videoPath);
				try {
					upload(videoPath);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//				path(videoPath);
			}


		} 

		//	Uri vid = data.getData();




	}

	public String getRealPathFromURI(Uri contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(contentUri, proj, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}


	public void upload(String path) throws Exception
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
			mpEntity.addPart("FromUserId", new StringBody("12345"));
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


}
