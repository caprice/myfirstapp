package com.mmm.mvideo.webtrends;

import java.util.Map;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.webtrends.mobile.analytics.IllegalWebtrendsParameterValueException;
import com.webtrends.mobile.analytics.WebtrendsDataCollector;

/**
 * @author Eric Liu
 * 
 */
public class WebtrendsDataCollectionHelper {
	private final static String TAG = "WebtrendsDataCollectionHelper";

	private WebtrendsDataCollectionHelper() {
	}

	private static class SingletonHolder {
		private static final WebtrendsDataCollectionHelper INSTANCE = new WebtrendsDataCollectionHelper();
	}

	public static WebtrendsDataCollectionHelper getInstance() {
		return SingletonHolder.INSTANCE;
	}

	/**
	 * @param paramString
	 * @param paramMap
	 */
	public void onViewStart(String paramString, Map<String, String> paramMap) {
		try {
			WebtrendsDataCollector.getInstance().onActivityStart(paramString, paramMap);
		} catch (IllegalWebtrendsParameterValueException e) {
			Log.e(TAG, e.getMessage());
			WebtrendsDataCollector.getLog().e(e.getMessage());
		}
	}

	/**
	 * @param paramString
	 * @param paramMap
	 */
	public void onViewEnd(String paramString, Map<String, String> paramMap) {
		try {
			WebtrendsDataCollector.getInstance().onActivityEnd(paramString, paramMap);
		} catch (IllegalWebtrendsParameterValueException e) {
			Log.e(TAG, e.getMessage());
			WebtrendsDataCollector.getLog().e(e.getMessage());
		}
	}

	/**
	 * <p>
	 * The onScreenView method collects data when content such as a screen,
	 * article, or image is viewed.
	 * </p>
	 * <p>
	 * For example, WebtrendsDataCollector.getInstance().onScreenView(
	 * "/HelloWorld/screen/view", "HelloWorld Screen View", "view", customData,
	 * "Content Group");
	 * </p>
	 * 
	 * @param eventPath
	 * @param eventDescr
	 * @param eventType
	 * @param customData
	 * @param contentGroup
	 */
	public void onScreenView(String eventPath, String eventDescr, String eventType, Map<String, String> customData, String contentGroup) {
		try {
			WebtrendsDataCollector.getInstance().onScreenView(eventPath, eventDescr, eventType, customData, contentGroup);
		} catch (IllegalWebtrendsParameterValueException e) {
			Log.e(TAG, e.getMessage());
			WebtrendsDataCollector.getLog().e(e.getMessage());
		}

	}

	/**
	 * <p>
	 * example: WebtrendsDataCollector.getInstance()
	 * .onMediaEvent("/HelloWorld/media/event", "HelloWorld Media Event",
	 * "event", customData, "Content Group", "Media Name", "Media Type",
	 * "Media Event Type");
	 * </p>
	 * 
	 * @param eventPath
	 * @param eventDesc
	 * @param eventType
	 * @param customData
	 * @param contentGroup
	 * @param mediaName
	 * @param mediaType
	 * @param mediaEventType
	 */
	public void onMediaEvent(String eventPath, String eventDesc, String eventType, Map<String, String> customData, String contentGroup, String mediaName, String mediaType, String mediaEventType) {
		try {
			WebtrendsDataCollector.getInstance().onMediaEvent(eventPath, eventDesc, eventType, customData, contentGroup, mediaName, mediaType, mediaEventType);
		} catch (IllegalWebtrendsParameterValueException e) {
			Log.e(TAG, e.getMessage());
			WebtrendsDataCollector.getLog().e(e.getMessage());
		}
	}

	/**
	 * <p>
	 * The onButtonClick method collects data when a user clicks an application
	 * button.
	 * </p>
	 * <p>
	 * </p>
	 * 
	 * @param eventPath
	 * @param eventDescr
	 * @param eventType
	 * @param customData
	 */
	public void onButtonClick(String eventPath, String eventDescr, String eventType, Map<String, String> customData) {
		try {
			WebtrendsDataCollector.getInstance().onButtonClick(eventPath, eventDescr, eventType, customData);
		} catch (IllegalWebtrendsParameterValueException e) {
			Log.e(TAG, e.getMessage());
			WebtrendsDataCollector.getLog().e(e.getMessage());
		}

	}

	/**
	 * @param paramUri
	 * @param paramContext
	 */
	public void setSessionInfo(Uri paramUri, Context paramContext) {
		WebtrendsDataCollector.getInstance().setSessionInfo(paramUri, paramContext);
	}

}
