package com.mmm.mvideo.application;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.util.Log;

import com.mmm.mvideo.business.entity.MMMVideoItem;
import com.mmm.mvideo.widget.CustomProgressDialog;
import com.mmm.mvideo.xmlparser.MMMPlayListXmlParser;
import com.webtrends.mobile.android.WebtrendsApplication;

/**
 * @author Eric Liu
 * 
 */
public class MMMVideoApplication extends WebtrendsApplication{
	private ArrayList<MMMVideoItem> items;
	/** The progress dialog. */
	private CustomProgressDialog progressDialog;

	@Override
	public void onCreate() {
		super.onCreate();
		MMMPlayListXmlParser playListParser = new MMMPlayListXmlParser();
		try {
			InputStream inStream = getAssets().open("plist.xml");
			items = playListParser.domXmlParse(inStream);
			Log.i("info", items.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	public ArrayList<MMMVideoItem> getItems() {
		return items;
	}

	/**
	 * @param msg
	 */
	public void startProgressDialog(String msg) {
		if (progressDialog == null) {
			progressDialog = CustomProgressDialog.createDialog(this);
			progressDialog.setMessage(msg);
		}

		progressDialog.show();
	}

	/**
	 * 
	 */
	public void stopProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

}
