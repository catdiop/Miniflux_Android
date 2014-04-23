package enseirb.t2.miniflux;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

import com.projet.miniflux.R;

public class ItemActivity extends Activity {
	private ListView listItems; 
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item);
		//Hide the status Bar
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		String link=getIntent().getExtras().getString("link");
		Uri.Builder builder=new Uri.Builder();
		builder.scheme("http")
		.authority("cdiop.rmorpheus.enseirb-matmeca.fr")
		.appendPath("Miniflux")
		.appendPath("rest")
		.appendPath("flux")
		.appendPath("get")
		.appendQueryParameter("link", link);
		
		Toast.makeText(ItemActivity.this, builder.build().toString() , Toast.LENGTH_LONG).show();
		
		new HttpCall(this).execute(builder.build().toString());
	}


	private class HttpCall extends AsyncTask<String, Void, String> {
		private ProgressDialog progressDialog;
		private Activity activity;
		
		public HttpCall(Activity activity) {
			this.activity=activity;
			context=activity;
			progressDialog=new ProgressDialog(context);
		}
		
		@Override
		protected String doInBackground(String... urls) {
			// TODO Auto-generated method stub
			try {
				URL url=new URL(urls[0]);
				HttpURLConnection con = (HttpURLConnection) url
						.openConnection();
				String s=readStream(con.getInputStream());
				return s;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if(progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			listItems=(ListView)findViewById(R.id.list_items);
			List<Item> items=ManipulateJsonData.getItems(result);
			Toast.makeText(ItemActivity.this, items.get(0).toString(), Toast.LENGTH_LONG).show();
			listItems.setAdapter(new ListItemAdapter(ItemActivity.this, items));
		}

		private String readStream(InputStream in) {
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new InputStreamReader(in));
				String line = "";
				String s="";
				while ((line = reader.readLine()) != null) {
					s=s.concat(line);
				}
				return s;
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			this.progressDialog.setMessage("Wait please...");
			this.progressDialog.show();
		} 
	}
}