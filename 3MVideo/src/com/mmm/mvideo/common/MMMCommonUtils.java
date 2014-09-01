package com.mmm.mvideo.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.brightcove.mobile.mediaapi.ReadAPI;
import com.brightcove.mobile.mediaapi.model.ItemCollection;
import com.brightcove.mobile.mediaapi.model.Video;
import com.brightcove.mobile.mediaapi.model.enums.MediaDeliveryTypeEnum;
import com.brightcove.mobile.mediaapi.model.enums.SortByTypeEnum;
import com.brightcove.mobile.mediaapi.model.enums.SortOrderTypeEnum;
import com.brightcove.mobile.mediaapi.model.enums.VideoFieldEnum;
import com.mmm.mvideo.R;
import com.mmm.mvideo.activity.fragment.NavigationFragment;
import com.mmm.mvideo.activity.fragment.NavigationFragment.FragmentParameterKey;
import com.mmm.mvideo.business.entity.MMMVideoItem;
import com.mmm.mvideo.business.entity.MMMVideoItem.PlayType;
import com.mmm.mvideo.business.entity.NavigationLine;
import com.mmm.mvideo.business.service.MMMVideoRetrieveService;
import com.mmm.mvideo.widget.CustomProgressDialog;

/**
 * @author Eric Liu
 * 
 */
public class MMMCommonUtils {
	/** The tag. */
	private final static String TAG = "MMMViewCommonUtils";
	public final static String DATE_FORMAT = "yyyy-MM-dd";
	private static Logger apiLogger = Logger.getLogger("Tab1Fragment");
	private static CustomProgressDialog progressDialog;

	/**
	 * Adds the nav fragment.
	 * 
	 * @param fragmentType
	 *            the fragment type
	 * @param arguments
	 *            the arguments
	 */
	public static void addNavFragment(Fragment fragment, Bundle arguments) {
		FragmentManager fragmentManager = fragment.getFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		NavigationFragment newFragment = new NavigationFragment();
		newFragment.setObserver(fragment);
		newFragment.setArguments(arguments);
		transaction.add(R.id.flNaviContainer, newFragment);
		transaction.commit();
	}

	/**
	 * @param fragment
	 * @param glass
	 * @param arguments
	 */
	public static void addContentFragment(Fragment fragment, Class<? extends Fragment> glass, Bundle arguments) {
		try {
			FragmentManager fragmentManager = fragment.getFragmentManager();
			FragmentTransaction transaction = fragmentManager.beginTransaction();
			Fragment newFragment = (Fragment) glass.newInstance();
			newFragment.setArguments(arguments);
			if (fragmentManager.findFragmentById(R.id.mmmContents) != null) {
				transaction.replace(R.id.mmmContents, newFragment);
			} else {
				transaction.add(R.id.mmmContents, newFragment);
			}
			transaction.commit();
		} catch (InstantiationException e) {
			Log.e(TAG, e.getMessage());
		} catch (IllegalAccessException e) {
			Log.e(TAG, e.getMessage());
		}
	}

	/**
	 * @param items
	 * @return
	 */
	public static ArrayList<NavigationLine> convertIntoNavItems(ArrayList<MMMVideoItem> items) {
		ArrayList<NavigationLine> lines = new ArrayList<NavigationLine>();
		if (items != null) {
			for (MMMVideoItem item : items) {
				NavigationLine line = new NavigationLine();
				line.setTitle(item.getTitle());
				lines.add(line);
			}
		}
		return lines;
	}

	public static void pullVideoPlayList(Context context, final MMMVideoItem navItem, final Handler handler) {
		pullVideoPlayList(context, navItem, handler, false);
	}

	public static void pullVideoPlayList(final Context context, final MMMVideoItem navItem, final Handler handler, boolean isRefreshing) {
		final String token = navItem.getToken();
		final String tags = navItem.getTags();

		final MMMVideoRetrieveService service = new MMMVideoRetrieveService(context);
		String last_update_time = service.getLatestUpdateTime(token, tags);
		if (isNeedUpdateVideoList(last_update_time) || isRefreshing) {
			String[] tagArray = tags.split(",");

			final Set<String> orTagSet = new HashSet<String>();
			final Set<String> andTagSet = new HashSet<String>();

			// skip 1 for test
			for (int i = 0; i < tagArray.length; i++) {
				if (i == 0) {
					andTagSet.add(tagArray[i]);
				} else {
					orTagSet.add(tagArray[i]);
				}
			}
			startProgressDialog(context, "updating playlist...");
			final ReadAPI readAPI = new ReadAPI(token);
			readAPI.setMediaDeliveryType(MediaDeliveryTypeEnum.HTTP);
			readAPI.setLogger(apiLogger);
			new Thread(new Runnable() {
				@Override
				public void run() {
					EnumSet<VideoFieldEnum> videoFields = getVideoEnumSet();
					ArrayList<MMMVideoItem> items = new ArrayList<MMMVideoItem>();
					try {
						ItemCollection<Video> coll = readAPI.findVideosByTags(andTagSet, orTagSet, 30, 0, SortByTypeEnum.MODIFIED_DATE, SortOrderTypeEnum.DESC, videoFields, null);
						addToLeftNavItems(items, coll, token, tags);
						// clear previous stored data
						service.deleteLastUpdateTime(token, tags);
						service.clearVideoCache(token, tags);
						SimpleDateFormat sdfDateFormat = new SimpleDateFormat(DATE_FORMAT);
						// add current update time
						service.save(sdfDateFormat.format(Calendar.getInstance().getTime()), token, tags);
						// cache current play list
						service.save(items);
					} catch (Exception e) {
						e.printStackTrace();
						handler.sendEmptyMessage(ApplicationCommon.HandlerMessageType.CONNECT_TO_SERVER_ERROR);
					} finally {
						sendMessage(navItem, handler, items);
						stopProgressDialog();
					}
				}

			}).start();
		} else {
			ArrayList<MMMVideoItem> items = service.getScrollData(token, tags);
			sendMessage(navItem, handler, items);
		}

	}

	/**
	 * @param navItem
	 * @param handler
	 * @param items
	 */
	private static void sendMessage(final MMMVideoItem navItem, final Handler handler, ArrayList<MMMVideoItem> items) {
		Message msg = new Message();
		msg.what = ApplicationCommon.HandlerMessageType.PULL_PLAY_LIST;
		Bundle data = new Bundle();
		data.putBoolean(FragmentParameterKey.SHOW_BACK_BTN, true);
		data.putBoolean(FragmentParameterKey.SHOW_REFRESH_SETTING, true);
		data.putSerializable(FragmentParameterKey.PARENT_ITEM, navItem);
		data.putCharSequence(NavigationFragment.FragmentParameterKey.TITLE, navItem.getTitle());
		data.putSerializable(FragmentParameterKey.NAV_ITEMS, items);
		msg.setData(data);
		handler.sendMessage(msg);
	}

	/**
	 * @return
	 */
	private static EnumSet<VideoFieldEnum> getVideoEnumSet() {
		EnumSet<VideoFieldEnum> videoFields = VideoFieldEnum.createEmptyEnumSet();
		videoFields.add(VideoFieldEnum.ID);
		videoFields.add(VideoFieldEnum.NAME);
		videoFields.add(VideoFieldEnum.RENDITIONS);
		videoFields.add(VideoFieldEnum.FLVURL);
		videoFields.add(VideoFieldEnum.LINKURL);
		videoFields.add(VideoFieldEnum.VIDEOSTILLURL);
		videoFields.add(VideoFieldEnum.SHORTDESCRIPTION);
		videoFields.add(VideoFieldEnum.THUMBNAILURL);
		videoFields.add(VideoFieldEnum.FLVFULLLENGTH);
		return videoFields;
	}

	/**
	 * @param coll
	 */
	private static void addToLeftNavItems(ArrayList<MMMVideoItem> items, ItemCollection<Video> coll, String token, String tags) {
		for (Video video : coll.getItems()) {
			MMMVideoItem item = new MMMVideoItem();
			item.setToken(token);
			item.setTags(tags);
			item.setTitle(video.getName());
			item.setUrl(video.getFlvUrl());
			item.setImgName(video.getThumbnailUrl());
			item.setDesc(video.getShortDescription());
			item.setPlayType(PlayType.PLAY_VIDEO);
			items.add(item);
		}
	}

	/**
	 * @param last_update_time
	 * @return
	 */
	private static boolean isNeedUpdateVideoList(String last_update_time) {
		if (isEmpty(last_update_time)) {
			return true;
		}
		try {
			Log.i("debug", last_update_time);
			SimpleDateFormat sdfDateFormat = new SimpleDateFormat(DATE_FORMAT);
			long lastUpdateDate = sdfDateFormat.parse(last_update_time).getTime();
			long currentDate = Calendar.getInstance().getTimeInMillis();
			if ((currentDate - lastUpdateDate) / (24 * 60 * 60 * 1000) >= 1) {
				return true;
			}
		} catch (ParseException e) {
			Log.e(TAG, e.getMessage());
		}
		return false;

	}

	/**
	 * @param msg
	 */
	public static void startProgressDialog(Context context, String msg) {
		if (progressDialog == null) {
			progressDialog = CustomProgressDialog.createDialog(context);
			progressDialog.setMessage(msg);
		}
		progressDialog.show();
	}

	/**
	 * 
	 */
	public static void stopProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

	/**
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		return str == null || "".equals(str);
	}

}
