package enseirb.t2.miniflux;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ContactDatabaseHelper extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME="Db";
	static final String LINK="link";
	static final String WEBSITE="website";
	
	public static final String TABLE_CREATE="CREATE TABLE allFluxName (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
			LINK +" TEXT NOT NULL, " +
			WEBSITE +" TEXT NOT NULL);";
	
	public static final String TABLE_DROP="DROP TABLE IF EXISTS allFluxName";
	
	public ContactDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, 1);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		android.util.Log.w(DATABASE_NAME, "Maj de la base, suppression de toutes les anciennes données");
		db.execSQL(TABLE_DROP);
		onCreate(db);
	}
}
