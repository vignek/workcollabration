package com.WorkCollabration.App;

import java.io.File;
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
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Audio extends Activity {
	private static MediaRecorder mediaRecorder;
	private static MediaPlayer mediaPlayer;

	private static String audioFilePath;
	private static Button stopButton;
	private static Button playButton;
	private static Button recordButton;

	private boolean isRecording = false;
	private boolean hasMircrophone;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_audio);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = 
					new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
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
		playButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				playButton.setEnabled(false);
				recordButton.setEnabled(false);
				stopButton.setEnabled(true);

				mediaPlayer = new MediaPlayer();
				try {
					mediaPlayer.setDataSource(audioFilePath);
					mediaPlayer.prepare();

				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mediaPlayer.start();

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
					uploadaudio(audioFilePath);
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
