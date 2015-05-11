package com.dotapk.moviesnow;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	static final String DATABASE_TABLE = "newstable";
	public static final String DATABASE_NAME = "news.db";
	public static final String KEY_ID = "_id";
	public static final String TYPE = "newstype";
	public static final String TITLE = "title";
	public static final String DETAILS = "details";
	public static final String CAPTION = "caption";
	public static final String DATABASE_CREATE="create table if not exists "+
			"newstable" + " (" + KEY_ID +
			" integer primary key autoincrement, " +
			TYPE + " text , " +
			TITLE + " text , " +
			DETAILS + " text , " +
			CAPTION + " text );";
	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
     db.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
     db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
     db.execSQL(DATABASE_CREATE);
	}

}
