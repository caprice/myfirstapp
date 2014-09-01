package com.mmm.mvideo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @author Eric Liu
 * 
 */
public class DbOpenHelper extends SQLiteOpenHelper {
	private static final String DATABASENAME = "mmm_video.db";
	private static final int DATABASVERSION = 4;

	public DbOpenHelper(Context context) {
		// create database
		super(context, DATABASENAME, null, DATABASVERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i("DB", "Tables created");
		db.execSQL("create table cachetime(token varchar(80), tags varchar(50),update_time varchar(10), primary key(token, tags))");
		db.execSQL("create table videos(id integer primary key autoincrement, token varchar(80), tags varchar(50), title varchar(50), desc varchar(500), url varchar(200), imgUrl varchar(200), playType integer)");
		db.execSQL("create table downloadfiles(id integer primary key autoincrement, title varchar(50), desc varchar(500), url varchar(200), localPath varchar(200), imgUrl varchar(200), playType integer)");
		db.execSQL("create table downloadlog(id integer primary key autoincrement, downloadurl varchar(200))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i("DB", "Database upgrade");
		db.execSQL("DROP TABLE IF EXISTS cachetime");
		db.execSQL("DROP TABLE IF EXISTS videos");
		db.execSQL("DROP TABLE IF EXISTS downloadfiles");
		onCreate(db);
	}

}
