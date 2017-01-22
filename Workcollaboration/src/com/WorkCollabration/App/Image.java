package com.WorkCollabration.App;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

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
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.Toast;

public class Image extends Activity {
	Button bttt;
	ImageView im;
	Uri selectedImageUri;
	String  selectedPath;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image);
		bttt=(Button)findViewById(R.id.bttt);
		im=(ImageView)findViewById(R.id.imagee);
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = 
					new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		bttt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				selectImage();

			}
		});
	}



	private void selectImage() {

		final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

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
				selectedPath = getPath(selectedImageUri);
				im.setImageURI(selectedImageUri);
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
				im.setImageBitmap(thumbnail);
				try {
					upload(picturePath);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}

	}


	public String getPath(Uri uri) {

		String[] projection = { MediaStore.Images.Media.DATA };

		Cursor cursor = managedQuery(uri, projection, null, null, null);

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
