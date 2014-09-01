package com.mmm.mvideo.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TabHost;
import android.widget.Toast;

import com.mmm.mvideo.R;
import com.mmm.mvideo.activity.fragment.NavigationFragment;
import com.mmm.mvideo.activity.fragment.NavigationFragment.FragmentParameterKey;
import com.mmm.mvideo.activity.fragment.TabFragment;
import com.mmm.mvideo.application.MMMVideoApplication;
import com.mmm.mvideo.business.entity.MMMVideoItem;
import com.mmm.mvideo.business.service.MMMVideoRetrieveService;
import com.mmm.mvideo.common.ApplicationCommon;
import com.mmm.mvideo.common.TabManager;
import com.mmm.mvideo.widget.CustomProgressDialog;

/**
 * The Class HomeActivity.
 * 
 * @author Eric Liu
 * 
 */
public class HomeActivity extends FragmentActivity {
	private long exitTime = 0;
	/** The TAG. */
	protected final String TAG = this.getClass().getName();
	/** The context. */
	protected final Activity context = this;

	/** The m tab host. */
	private TabHost mTabHost;

	private TabManager tabManager;

	private ArrayList<MMMVideoItem> items;

	/** The progress dialog. */
	private CustomProgressDialog progressDialog;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmm.mvideo.activity.MMMActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_home);
		// init tabs
		Map<String, String> paramMap = new HashMap<String, String>();
		initTabs(paramMap);
		// Set the current tab if one exists.
		if (savedInstanceState != null) {
			this.mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
		}
	}


	/**
	 * 
	 */
	private void initTabs(Map<String, String> parmMap) {
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setPadding(mTabHost.getPaddingLeft(), mTabHost.getPaddingTop(), mTabHost.getPaddingRight(), mTabHost.getPaddingBottom() - 5);
		mTabHost.setup();
		// Initialize the tabs.
		this.tabManager = new TabManager(this, this.mTabHost, R.id.realtabcontent);
		// Home Tab
		Bundle args = new Bundle();
		args.putInt("layoutResource", R.layout.activity_main);
		this.tabManager.addTab(this.mTabHost.newTabSpec("Home").setIndicator("Home"), TabFragment.class, args);

		MMMVideoApplication application = (MMMVideoApplication) this.getApplication();
		items = application.getItems();
		Map<String, ArrayList<MMMVideoItem>> tabMap = getTabMap(items);

		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		String tabNamesString = settings.getString(ApplicationCommon.TAB_KEY_IN_SHARED_PREF, "");
		parmMap.put("Categories", tabNamesString);
		// Explode the string read in sharedpreferences
		String[] vals = parseStoredValue(tabNamesString);
		if (vals != null) {
			for (String tabName : vals) {
				tabName = tabName.trim();
				ArrayList<MMMVideoItem> navItems = tabMap.get(tabName);
				args = new Bundle();
				args.putInt("layoutResource", R.layout.tabfragment_layout);
				args.putCharSequence(NavigationFragment.FragmentParameterKey.TITLE, tabName);
				args.putSerializable(FragmentParameterKey.NAV_ITEMS, navItems);
				this.tabManager.addTab(this.mTabHost.newTabSpec(tabName).setIndicator(tabName), TabFragment.class, args);
			}
		}
		args = new Bundle();
		args.putInt("layoutResource", R.layout.tabfragment_layout);
		this.tabManager.addTab(this.mTabHost.newTabSpec("Downloaded").setIndicator("Downloaded"), TabFragment.class, args);
	}

	@Override
	public void openOptionsMenu() {
		super.openOptionsMenu();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, 100, 2, "Settings");
		menu.add(Menu.NONE, 101, 6, "Exit");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case 100:
			openSettingView();
			break;

		case 101:
			exitApp();
			break;

		}

		return false;

	}

	private void openSettingView() {
		Bundle args = new Bundle();
		args.putSerializable(FragmentParameterKey.NAV_ITEMS, items);
		Intent intent = new Intent(this, MMMPrefSettingActivity.class);
		intent.setAction("MMMPrefSettingActivity");
		intent.putExtras(args);
		startActivity(intent);

	}

	private void exitApp() {
		Builder builder = new AlertDialog.Builder(this, R.style.NoTitleDialog);
		builder.setMessage("Are you sure want to exit 3M Video？");

		builder.setPositiveButton("OK", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				finish();
			}
		});
		builder.setNegativeButton("Cancel", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("tab", this.mTabHost.getCurrentTabTag());
	}

	@Override
	protected void onDestroy() {
		MMMVideoRetrieveService service = new MMMVideoRetrieveService(this);
		service.clearDownloadingFile();
		super.onDestroy();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	/**
	 * @param items
	 * @return
	 */
	private Map<String, ArrayList<MMMVideoItem>> getTabMap(ArrayList<MMMVideoItem> items) {
		Map<String, ArrayList<MMMVideoItem>> map = new HashMap<String, ArrayList<MMMVideoItem>>();
		if (items == null) {
			return map;
		}
		for (MMMVideoItem item : items) {
			map.put(item.getTitle(), item.getSubItems());
		}
		return map;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(this, "Press again to exit!", Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				MMMVideoRetrieveService service = new MMMVideoRetrieveService(this);
				service.clearDownloadingFile();
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private String[] parseStoredValue(String val) {
		if ("".equals(val)) {
			return null;
		} else {
			return val.split(ApplicationCommon.SEPARATOR);
		}
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
