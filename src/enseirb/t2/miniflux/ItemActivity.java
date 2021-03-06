package enseirb.t2.miniflux;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.projet.miniflux.R;

public class ItemActivity extends Activity {

	private ListView listItems; 
	private Context context;
	private List<Item> itemsToShow;
	private ListItemAdapter adapter;
	private String link;
	static View view;

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			break;
		case R.id.action_favorites:
			item.setIcon(R.drawable.ic_menu_star_yellow);
			Uri.Builder builder1=new Uri.Builder();
			builder1.scheme("http")
			.authority("cdiop.rmorpheus.enseirb-matmeca.fr")
			.appendPath("Miniflux")
			.appendPath("rest")
			.appendPath("flux")
			.appendPath("favorites")
			.appendQueryParameter("link", getIntent().getExtras().getString("link"));
			new HttpCall3(ItemActivity.this).execute(builder1.build().toString());
			return true;
		case R.id.action_refresh:
			refresh();
			break;
		default:;	
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item);
		//Hide the status Bar
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		link=getIntent().getExtras().getString("link");
		Uri.Builder builder=new Uri.Builder();
		builder.scheme("http")
		.authority("cdiop.rmorpheus.enseirb-matmeca.fr")
		.appendPath("Miniflux")
		.appendPath("rest")
		.appendPath("flux")
		.appendPath("get")
		.appendQueryParameter("link", link);

		ListView list = (ListView)findViewById(R.id.list_items);
		list.setOnItemClickListener( new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub

				TextView tv=(TextView)arg1.findViewById(R.id.uri);
				TextView title=(TextView)arg1.findViewById(R.id.title);
				title.setTypeface(Typeface.SERIF, Typeface.ITALIC);
				Uri.Builder builder1=new Uri.Builder();
				builder1.scheme("http")
				.authority("cdiop.rmorpheus.enseirb-matmeca.fr")
				.appendPath("Miniflux")
				.appendPath("rest")
				.appendPath("item")
				.appendPath("read")
				.appendQueryParameter("link", link)
				.appendQueryParameter("title", title.getText().toString());
				new HttpCall2(ItemActivity.this).execute(builder1.build().toString());
				Intent intent=new Intent(ItemActivity.this, WebPageActivity.class);
				intent.putExtra("link", tv.getText());
				startActivity(intent);
			}
		});

		list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				view=arg1;
				CharSequence parameters[] = new CharSequence[] {
				"Favorite" };
				AlertDialog.Builder builder = new AlertDialog.Builder(ItemActivity.this);
				builder.setTitle("Set");
				builder.setItems(parameters, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// the user clicked on colors[which]

						switch (which) {
						case 0:
							TextView tv=(TextView)view.findViewById(R.id.title);
							setFavorite(getIntent().getExtras().getString("link"), tv.getText().toString());
							break;
						case 1 : {
							break;
						}
						}
					}
				});
				builder.show();
				return false;
			}

		});

		new HttpCall(this).execute(builder.build().toString());

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_items, menu);
		return super.onCreateOptionsMenu(menu);
	}

	private class HttpCall extends AsyncTask<String, Void, String> {
		protected ProgressDialog progressDialog;
		@SuppressWarnings("unused")
		protected Activity activity;

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
			itemsToShow=ManipulateJsonData.getItems(result);
			adapter=new ListItemAdapter(ItemActivity.this, itemsToShow);
			listItems.setAdapter(adapter);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			this.progressDialog.setMessage("Wait please...");
			this.progressDialog.show();
		} 
	}

	private class HttpCall1 extends HttpCall {

		public HttpCall1(Activity activity) {
			super(activity);
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if(progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			listItems=(ListView)findViewById(R.id.list_items);
			List<Item> items=ManipulateJsonData.getItems(result);
			itemsToShow.addAll(0,items);
			adapter=new ListItemAdapter(ItemActivity.this, itemsToShow);
			listItems.setAdapter(adapter);
			adapter.notifyDataSetChanged();

		}
	}

	private class HttpCall2 extends HttpCall {

		public HttpCall2(Activity activity) {
			super(activity);
		}

		@Override
		protected String doInBackground(String... urls) {
			// TODO Auto-generated method stub
			try {
				URL url=new URL(urls[0]);
				HttpURLConnection con = (HttpURLConnection) url
						.openConnection();
				String s=readStream(con.getInputStream());
				System.out.println(s);
				return s;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
		} 
	}

	private class HttpCall3 extends HttpCall {

		public HttpCall3(Activity activity) {
			super(activity);
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if(progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			listItems=(ListView)findViewById(R.id.list_items);
			List<Item> items=ManipulateJsonData.getItems(result);
			itemsToShow=items;
			adapter=new ListItemAdapter(ItemActivity.this, itemsToShow);
			listItems.setAdapter(adapter);
			adapter.notifyDataSetChanged();

		}
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

	public void refresh() {
		String link=getIntent().getExtras().getString("link");
		Uri.Builder builder=new Uri.Builder();
		builder.scheme("http")
		.authority("cdiop.rmorpheus.enseirb-matmeca.fr")
		.appendPath("Miniflux")
		.appendPath("rest")
		.appendPath("flux")
		.appendPath("refresh")
		.appendQueryParameter("link", link);

		new HttpCall1(this).execute(builder.build().toString());
	}

	public void setFavorite(String link, String title) {
		Uri.Builder builder=new Uri.Builder();
		builder.scheme("http")
		.authority("cdiop.rmorpheus.enseirb-matmeca.fr")
		.appendPath("Miniflux")
		.appendPath("rest")
		.appendPath("item")
		.appendPath("favorite")
		.appendQueryParameter("link", link)
		.appendQueryParameter("title", title);

		new HttpCall1(this).execute(builder.build().toString());
	}
}
