
package enseirb.t2.miniflux;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.projet.miniflux.R;



//Classe qui gére l'affichage de la page principale
public class HomeActivity extends ListActivity{

	static final String DATABASE_NAME = "Db";
	static final String TABLE_NAME="allFluxName";
	static final String KEY="_id";
	static final String LINK = "link";
	static final String WEBSITE = "website";
	static View view;
	private SQLiteDatabase db = null;
	private Cursor cursor = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Hide the status Bar
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_home);

		db = (new ContactDatabaseHelper(this)).getWritableDatabase();

		cursor = db.rawQuery("SELECT _id," + LINK + "," + WEBSITE + " FROM "+ TABLE_NAME +" ORDER BY " + WEBSITE, null);

		ListAdapter adapter = new SimpleCursorAdapter(this,
				R.layout.activity_home_row, cursor, new String[] {WEBSITE}, new int[] {R.id.website});


		ListView list = getListView();
		list.setOnItemClickListener( new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				TextView tv=(TextView)arg1.findViewById(R.id.website);
				String website=tv.getText().toString();
				Cursor c=db.rawQuery("select " + LINK + " from " + TABLE_NAME + " where " + WEBSITE + " LIKE ?", new String[]{website});
				c.moveToFirst();
				String link=c.getString(0);
			    Intent intent=new Intent(HomeActivity.this, ItemActivity.class);
				intent.putExtra("link", link);
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
				"Delete feed" };
				AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
				builder.setTitle("Do");
				builder.setItems(parameters, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// the user clicked on colors[which]

						switch (which) {
						case 0:
							TextView tv=(TextView)view.findViewById(R.id.website);
							String website=tv.getText().toString();
							db.delete(TABLE_NAME, WEBSITE + "=? ", new String[]{website});
							cursor.requery();
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

		setListAdapter(adapter);
		registerForContextMenu(getListView());

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_home, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id=item.getItemId();

		switch(id) {
		case R.id.add_feed:add();
		}
		return super.onOptionsItemSelected(item);
	}

	//add new feed
	private void add() {
		LayoutInflater inflater = LayoutInflater.from(this);
		View addView = inflater.inflate(R.layout.dialog_wrapper_add_feed,
				null);

		final DialogWrapper wrapper = new DialogWrapper(addView);

		new AlertDialog.Builder(this)
		.setTitle(R.string.add_feed)
		.setView(addView)
		.setPositiveButton(R.string.register,
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,
					int whichButton) {
				processAdd(wrapper);
			}
		})
		.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,
					int whichButton) {
				// ignore, just dismiss
			}
		}).show();
	}


	private void processAdd(DialogWrapper wrapper) {

		ContentValues values = new ContentValues();

		values.put(WEBSITE, wrapper.getWebsite());
		values.put(LINK, wrapper.getLink());

		db.insert(TABLE_NAME, null, values);

		cursor.requery();
	}


	public boolean isConnected(){
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) 
			return true;
		else
			return false;   
	}

	class DialogWrapper {
		EditText website = null;
		EditText link = null;
		View base = null;

		DialogWrapper(View base) {
			this.base = base;
			website = (EditText) base.findViewById(R.id.website_field);
			link = (EditText) base.findViewById(R.id.link_field);
		}

		String getWebsite() {
			return (getWebsiteField().getText().toString());
		}

		String getLink() {
			return (getLinkField().getText().toString());
		}


		private EditText getWebsiteField() {
			if (website == null) {
				website = (EditText) base.findViewById(R.id.website_field);
			}

			return (website);
		}

		private EditText getLinkField() {
			if (link == null) {
				link = (EditText) base.findViewById(R.id.link_field);
			}

			return (link);
		}
	}
}
