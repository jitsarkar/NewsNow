package com.dotapk.moviesnow;

import com.dotapk.moviesnow.data.NewsDbHelper;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;


public class NewsProvider extends ContentProvider {
	public static final String CONTENT_AUTHORITY = "com.dotapk.moviesnow";
	public static final Uri CONTENT_URI = 
			Uri.parse("content://"+CONTENT_AUTHORITY+ "/" + DBHelper.DATABASE_TABLE);
	private static final int ALLROWS = 1;
	private static final int SINGLEROW = 2;
	private NewsDbHelper dbhelper;
	SQLiteDatabase db;
	private static final UriMatcher urimatcher;
	static{
		urimatcher = new UriMatcher(UriMatcher.NO_MATCH);
		urimatcher.addURI(CONTENT_AUTHORITY, DBHelper.DATABASE_TABLE, ALLROWS);
	}
	
	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		dbhelper = new NewsDbHelper(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String Sort) {
		db=dbhelper.getReadableDatabase();
		Cursor cursor;
		cursor=db.query(DBHelper.DATABASE_TABLE, projection, selection, selectionArgs, null, null, Sort);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		
		db=dbhelper.getWritableDatabase();
		
			db.insert(DBHelper.DATABASE_TABLE, null, values);
		
		db.close();
		getContext().getContentResolver().notifyChange(uri, null);
		
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

}
