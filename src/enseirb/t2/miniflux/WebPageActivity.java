package enseirb.t2.miniflux;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.WindowManager;
import android.webkit.WebView;

import com.projet.miniflux.R;

public class WebPageActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webpage_activity);
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		WebView wv=(WebView)findViewById(R.id.webview);
		wv.loadUrl(getIntent().getExtras().getString("link"));
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent intent=new Intent(WebPageActivity.this, ItemActivity.class);
		intent.putExtra("link", getParentActivityIntent().getExtras().getString("link"));
		startActivity(intent);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
