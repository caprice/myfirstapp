package com.mmm.mvideo.common;

import android.os.Environment;

// TODO: Auto-generated Javadoc
/**
 * The Class ApplicationCommon.
 * @author a37wczz
 */
public class ApplicationCommon {

    /** The Constant SD_PATH. */
    public static final String SD_PATH = Environment
            .getExternalStorageDirectory().getPath() + "/";

    /** The Constant APP_FOLDER. */
    public static final String APP_FOLDER = SD_PATH + "/3mvideo/";

    /** The Constant IMAGE_CACHE_PATH. */
    public static final String IMAGE_CACHE_PATH = APP_FOLDER + "/cache/image/";

    /** The Constant VIDEO_SAVE_PATH. */
    public static final String VIDEO_SAVE_PATH = APP_FOLDER + "/video/";
    /** The key for sharepreference. */
    public static final String TAB_KEY_IN_SHARED_PREF = "mvideo.tabs";
    
    public static final String SEPARATOR = "OV=I=XseparatorX=I=VO";
    public static final String DEFAULT_SEPARATOR = "OV=I=XseparatorX=I=VO";
    public static final int REQ_SYSTEM_SETTINGS = 100;
    public static final int REQ_SYSTEM_FULLSCREEN = 110;
    
	public interface TabFragmentParmKey{
		public static final String NAV_ITEMS = "NAV_ITEMS";
		public static final String CUR_ITEM_POSITION = "CUR_ITEM_POSITION";
		public static final String CUR_ITEM_LIST = "CUR_ITEM_LIST";
		public static final String CUR_PLAYING_POS = "CUR_PLAYING_POS";
	}
	/**
	 * The Interface PlayType.
	 */
	public static interface HandlerMessageType {
		final static int CONNECT_TO_SERVER_ERROR = 0;
		final static int PULL_PLAY_LIST = 5;
		final static int DOWN_LOAD_FILE = 10;
		final static int HIDE_FULL_SCREEN_BTN = 15;
	}

    /**
     * if has SD card.
     * 
     * @return true, if is sD card available
     */
    public static boolean isSDCardAvailable() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

}
