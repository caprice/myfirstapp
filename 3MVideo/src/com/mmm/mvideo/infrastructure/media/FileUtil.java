package com.mmm.mvideo.infrastructure.media;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import com.mmm.mvideo.common.ApplicationCommon;

/**
 * The Class FileUtil.
 * 
 * @author a37wczz
 */
public class FileUtil {
	private static final int FILESIZE = 1 * 1024;

	/**
	 * write the ImputStream into SD
	 * 
	 * @param path
	 * @param fileName
	 * @param input
	 * @return
	 */
	public static boolean write2SDFromInput(String path, String fileName, InputStream input) {
		File file = null;
		if (ApplicationCommon.isSDCardAvailable()) {
			{
				OutputStream output = null;
				try {
					createSDDir(path);
					file = createSDFile(path + fileName);
					output = new FileOutputStream(file);
					byte[] buffer = new byte[FILESIZE];
					int length;
					while ((length = (input.read(buffer))) > 0) {
						output.write(buffer, 0, length);
					}
					output.flush();
					return true;
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						output.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return false;
	}

	/**
	 * Gets the pdf file intent.
	 * 
	 * @param file
	 *            the file
	 * @return the pdf file intent
	 */
	public static Intent getPdfFileIntent(File file) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(file);
		intent.setDataAndType(uri, "application/pdf");
		return intent;
	}

	/**
	 * Gets the text file intent.
	 * 
	 * @param file
	 *            the file
	 * @return the text file intent
	 */
	public static Intent getTextFileIntent(File file) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri2 = Uri.fromFile(file);
		intent.setDataAndType(uri2, "text/plain");
		return intent;
	}

	/**
	 * Gets the video file intent.
	 * 
	 * @param file
	 *            the file
	 * @return the video file intent
	 */
	public static Intent getVideoFileIntent(File file) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("oneshot", 0);
		intent.putExtra("configchange", 0);
		Uri uri = Uri.fromFile(file);
		intent.setDataAndType(uri, "video/*");
		return intent;
	}

	/**
	 * Gets the ppt file intent.
	 * 
	 * @param file
	 *            the file
	 * @return the ppt file intent
	 */
	public static Intent getPptFileIntent(File file) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(file);
		intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
		return intent;
	}

	/**
	 * Checks if is intent available.
	 * 
	 * @param context
	 *            the context
	 * @param intent
	 *            the intent
	 * @return true, if is intent available
	 */
	public static boolean isIntentAvailable(Context context, Intent intent) {
		final PackageManager packageManager = context.getPackageManager();
		List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.GET_ACTIVITIES);
		return list.size() > 0;
	}

	/**
	 * Unify file name.
	 * 
	 * @param fileName
	 *            the file name
	 * @return the string
	 */
	public static String unifyFileName(String fileName) {
		return fileName.substring(fileName.lastIndexOf("/") + 1).replaceAll("[\\/:*?\"<>|]", "");
	}

	/**
	 * create a file in SD
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static File createSDFile(String fileName) throws IOException {
		File file = new File(fileName);
		file.createNewFile();
		return file;
	}

	/**
	 * Create a directory in SD.
	 * 
	 * @param dirName
	 * @return
	 */
	public static File createSDDir(String dirName) {
		File dir = new File(dirName);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}

	/**
	 * Whether the file has been existed.
	 * 
	 * @param fileName
	 * @return
	 */
	public static boolean isFileExisted(String fileName) {
		File file = new File(fileName);
		return file.exists();
	}

	/**
	 * Whether the file has been existed.
	 * 
	 * @param fileName
	 * @return
	 */
	public static boolean deleteFile(String fileName) {
		File file = new File(fileName);
		if(file.exists()){
			return file.delete();
		}
		return true;
	}

}
