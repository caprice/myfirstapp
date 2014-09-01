package com.mmm.mvideo.activity;

import com.mmm.mvideo.widget.CustomProgressDialog;
import com.webtrends.mobile.android.WebtrendsActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

// TODO: Auto-generated Javadoc
/**
 * Base Activity of this application, define some custom properties and methods.
 * 
 * @author a37wczz
 */
public class MMMActivity extends WebtrendsActivity {
	/** The TAG. */
	protected final String TAG = this.getClass().getName();
	/** The context. */
	protected final Activity context = this;

	/** The progress dialog. */
	private CustomProgressDialog progressDialog;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// require full screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
