/*
 * ky
 */
package com.mmm.mvideo.infrastructure.http.transaction;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

// TODO: Auto-generated Javadoc
/**
 * Mapping the Transaction status and callback methods.
 * @author a37wczz
 */
public abstract class Callback extends Handler {

	/** The Constant TAG. */
	private static final String TAG = "Callback: ";

	/**
	 * After send.
	 */
	public void afterSend() {
		Log.d(TAG, "beforeExecute()");
	}

	/**
	 * After execute.
	 */
	public void afterExecute() {
		Log.d(TAG, "afterExecute()");
	}

	/**
	 * On ok.
	 */
	public void onOK() {
		Log.d(TAG, "onOk()");
	}

	/**
	 * On network error.
	 */
	public void onNetworkError() {
		Log.d(TAG, "onNetworkError()");
	}

	/**
	 * On cancel.
	 */
	public void onCancel() {
		Log.d(TAG, "onCancel()");

	}

	/**
	 * On error.
	 */
	public void onError() {
		Log.d(TAG, "onError()");

	}

	/**
	 * On timeout.
	 */
	public void onTimeout() {
		Log.d(TAG, "onTimeout()");

	}

	/**
	 * On ready.
	 */
	public void onReady() {
		Log.d(TAG, "onReady()");

	}

	/**
	 * .
	 * 
	 * @param status
	 *            the status
	 */
	public void callback(int status) {
		Log.d(TAG, "doStatus() " + status);
		sendEmptyMessage(status);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.Handler#handleMessage(android.os.Message)
	 */
	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
		case Transaction.STATUS_READY:
			break;
		case Transaction.STATUS_RUNNING:
			afterSend();
			break;
		case Transaction.STATUS_EXECUTED:
			afterExecute();
			break;
		case Transaction.STATUS_OK:
			onOK();
			break;
		case Transaction.STATUS_NETWORK_ERROR:
			onNetworkError();
			break;
		case Transaction.STATUS_CANCELED:
			onCancel();
			break;
		case Transaction.STATUS_ERROR:
			onError();
			break;
		case Transaction.STATUS_TIMEOUT:
			onTimeout();
			break;
		}
	}

}
