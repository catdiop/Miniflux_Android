package enseirb.t2.miniflux;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.VideoView;

import com.projet.miniflux.R;


public class VideoActivity extends Activity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getActionBar().hide();
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		//Hide the status Bar
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.video_activity);

		VideoView video;
		video = (VideoView)findViewById(R.id.videoView);
		Uri chemin = Uri.parse("android.resource://"+this.getPackageName()+"/"+R.raw.video);
		video.setVideoURI(chemin);
		video.start();

		video.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(VideoActivity.this, MainActivity.class);
				startActivity(intent);
			}

		});
	}

}
