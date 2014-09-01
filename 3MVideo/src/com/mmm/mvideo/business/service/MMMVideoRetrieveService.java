package com.mmm.mvideo.business.service;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mmm.mvideo.business.entity.MMMVideoItem;
import com.mmm.mvideo.db.DbOpenHelper;

/**
 * @author Eric Liu
 * 
 *         cache video info derived from brightcove
 * 
 */
public class MMMVideoRetrieveService {
	private DbOpenHelper dbhelp;
	private SQLiteDatabase db;

	public MMMVideoRetrieveService(Context context) {
		this.dbhelp = new DbOpenHelper(context);
	}

	public void save(String updateTime, String token, String tags) {
		db = dbhelp.getWritableDatabase();
		db.execSQL("insert into cachetime(update_time, token, tags) values(?,?,?)", new Object[] { updateTime, token, tags });
		db.close();
	}

	public void saveDownloadFile(MMMVideoItem item) {
		db = dbhelp.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("title", item.getTitle());
		values.put("desc", item.getDesc());
		values.put("url", item.getUrl());
		values.put("imgUrl", item.getImgName());
		values.put("playType", item.getPlayType());
		values.put("localPath", item.getLocalPath());
		db.insert("downloadfiles", null, values);
		db.close();
	}

	public void save(ArrayList<MMMVideoItem> items) {
		db = dbhelp.getWritableDatabase();
		if (items != null) {
			for (MMMVideoItem item : items) {
				ContentValues values = new ContentValues();
				values.put("token", item.getToken());
				values.put("tags", item.getTags());
				values.put("title", item.getTitle());
				values.put("desc", item.getDesc());
				values.put("url", item.getUrl());
				values.put("imgUrl", item.getImgName());
				values.put("playType", MMMVideoItem.PlayType.PLAY_VIDEO);
				db.insert("videos", null, values);
			}
		}
		db.close();
	}

	/**
	 * @return
	 */
	public ArrayList<MMMVideoItem> getAllDownLoadedFiles() {
		ArrayList<MMMVideoItem> items = new ArrayList<MMMVideoItem>();
		db = dbhelp.getReadableDatabase();
		Cursor cursor = null;
		cursor = db.rawQuery("select * from downloadfiles", null);
		while (cursor != null && cursor.moveToNext()) {
			String title = cursor.getString(cursor.getColumnIndex("title"));
			String desc = cursor.getString(cursor.getColumnIndex("desc"));
			String url = cursor.getString(cursor.getColumnIndex("url"));
			String localPath = cursor.getString(cursor.getColumnIndex("localPath"));
			String imgUrl = cursor.getString(cursor.getColumnIndex("imgUrl"));
			int playType = cursor.getInt(cursor.getColumnIndex("playType"));
			MMMVideoItem item = new MMMVideoItem();
			item.setTitle(title);
			item.setDesc(desc);
			item.setUrl(url);
			item.setLocalPath(localPath);
			item.setImgName(imgUrl);
			item.setPlayType(playType);
			item.setPlayInLocal(true);
			items.add(item);
		}
		cursor.close();
		db.close();
		return items;
	}

	public boolean isDownloadingFileExisted(String url) {
		db = dbhelp.getReadableDatabase();
		Cursor cursor = null;
		cursor = db.rawQuery("select * from downloadfiles where url=?", new String[] { url });
		if (cursor != null && cursor.moveToNext()) {
			cursor.close();
			db.close();
			return true;
		}
		cursor.close();
		db.close();
		return false;
	}

	/**
	 * record a file that is downloading
	 * 
	 * @param url
	 */
	public void saveFileDownloadinglog(String url) {
		db = dbhelp.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("downloadurl", url);
		db.insert("downloadlog", null, values);
		db.close();
	}

	/**
	 * @param url
	 * @return
	 */
	public boolean isFileDownloading(String url) {
		db = dbhelp.getReadableDatabase();
		Cursor cursor = null;
		cursor = db.rawQuery("select * from downloadlog where downloadurl=?", new String[] { url });
		if (cursor != null && cursor.moveToNext()) {
			cursor.close();
			db.close();
			return true;
		}
		cursor.close();
		db.close();
		return false;
	}

	/**
	 * 
	 */
	public void deleteDownloadedFile(String url) {
		db = dbhelp.getWritableDatabase();
		db.execSQL("delete from downloadfiles where url=?", new Object[] { url });
		db.close();
	}
	/**
	 * 
	 */
	public void deleteDownloadingLog(String url) {
		db = dbhelp.getWritableDatabase();
		db.execSQL("delete from downloadlog where downloadurl=?", new Object[] { url });
		db.close();
	}
	
	/**
	 * 
	 */
	public void clearDownloadingFile() {
		db = dbhelp.getWritableDatabase();
		db.execSQL("delete from downloadlog");
		db.close();
	}

	/**
	 * 
	 */
	public void deleteLastUpdateTime(String token, String tags) {
		db = dbhelp.getWritableDatabase();
		db.execSQL("delete from cachetime where token = ? and tags = ?", new Object[] { token, tags });
		db.close();
	}

	/**
	 * 
	 */
	public void clearVideoCache(String token, String tags) {
		db = dbhelp.getWritableDatabase();
		db.execSQL("delete from videos where token = ? and tags = ?", new Object[] { token, tags });
		db.close();
	}

	/**
	 * @return
	 */
	public String getLatestUpdateTime(String token, String tags) {
		db = dbhelp.getReadableDatabase();
		Cursor cursor = db.rawQuery("select update_time from cachetime where token=? and tags=?", new String[] { token, tags });
		String updateTimeString = null;
		if (cursor != null && cursor.moveToNext()) {
			updateTimeString = cursor.getString(cursor.getColumnIndex("update_time"));
		}
		cursor.close();
		db.close();
		return updateTimeString;
	}

	/**
	 * @param token
	 * @param tags
	 * @param offset
	 * @param maxResult
	 * @return
	 */
	public ArrayList<MMMVideoItem> getScrollData(String token, String tags, Integer offset, Integer maxResult) {
		ArrayList<MMMVideoItem> items = new ArrayList<MMMVideoItem>();
		db = dbhelp.getReadableDatabase();
		Cursor cursor = null;
		if (offset == null || maxResult == null) {
			cursor = db.rawQuery("select * from videos where token=? and tags = ?", new String[] { token, tags });
		} else {
			cursor = db.rawQuery("select * from videos where token=? and tags = ? limit ?,?", new String[] { token, tags, offset.toString(), maxResult.toString() });
		}
		while (cursor != null && cursor.moveToNext()) {
			String title = cursor.getString(cursor.getColumnIndex("title"));
			String desc = cursor.getString(cursor.getColumnIndex("desc"));
			String url = cursor.getString(cursor.getColumnIndex("url"));
			String imgUrl = cursor.getString(cursor.getColumnIndex("imgUrl"));
			int playType = cursor.getInt(cursor.getColumnIndex("playType"));
			MMMVideoItem item = new MMMVideoItem();
			item.setToken(token);
			item.setTags(tags);
			item.setTitle(title);
			item.setDesc(desc);
			item.setUrl(url);
			item.setImgName(imgUrl);
			item.setPlayType(playType);
			items.add(item);
		}
		cursor.close();
		db.close();
		return items;
	}

	/**
	 * @param token
	 * @param tags
	 * @return
	 */
	public ArrayList<MMMVideoItem> getScrollData(String token, String tags) {
		return this.getScrollData(token, tags, null, null);
	}

	public long getCount() {
		db = dbhelp.getReadableDatabase();
		Cursor cusor = db.rawQuery("select count(*) from videos", null);
		cusor.moveToFirst();
		long count = cusor.getLong(0);
		cusor.close();
		db.close();
		return count;
	}

	public void close() {
		if (db != null && db.isOpen()) {
			db.close();
		}

		if (dbhelp != null) {
			dbhelp.close();
		}
	}
}
