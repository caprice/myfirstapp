package com.mmm.mvideo.common;

public class Log {
	public static final String TAG = "MMMVideo";

	public static void i(String msg, Object...args) {
		if (msg == null)
			return;
		try {
			android.util.Log.i("MMMVideo", String.format(msg, args));
		} catch (Exception e) {
			android.util.Log.e("MMMVideo", "com.mmm.video.common.Log", e);
			android.util.Log.i("MMMVideo", msg);
		}
	}

	public static void d(String msg, Object...args) {
		if (msg == null)
			return;
		try {
			android.util.Log.d("MMMVideo", String.format(msg, args));
		} catch (Exception e) {
			android.util.Log.e("MMMVideo", "com.mmm.video.common.Log", e);
			android.util.Log.d("MMMVideo", msg);
		}
	}

	public static void e(String msg, Object...args) {
		if (msg == null)
			return;
		try {
			android.util.Log.e("MMMVideo", String.format(msg, args));
		} catch (Exception e) {
			android.util.Log.e("MMMVideo", "com.mmm.video.common.Log", e);
			android.util.Log.e("MMMVideo", msg);
		}
	}

	public static void e(String msg, Throwable t) {
		if (msg == null)
			return;
		android.util.Log.e("MMMVideo", msg, t);
	}
}
