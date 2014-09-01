package com.mmm.mvideo.common;

import java.util.HashMap;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;

import com.mmm.mvideo.R;
import com.mmm.mvideo.webtrends.WebtrendsDataCollectionHelper;

/**
 * This is a helper class that implements a generic mechanism for associating
 * fragments with the tabs in a tab host. It relies on a trick. Normally a tab
 * host has a simple API for supplying a View or Intent that each tab will show.
 * This is not sufficient for switching between fragments. So instead we make
 * the content part of the tab host 0dp high (it is not shown) and the
 * TabManager supplies its own dummy view to show as the tab content. It listens
 * to changes in tabs, and takes care of switch to the correct fragment shown in
 * a separate content area whenever the selected tab changes.
 * 
 */
public class TabManager implements OnTabChangeListener {
	private final FragmentActivity activity;
	private final TabHost tabHost;
	private final int containerId;
	private final HashMap<String, TabInfo> tabInfoMap = new HashMap<String, TabInfo>();
	private TabInfo currentTab;

	/** Represents the information of a tab. */
	static final class TabInfo {
		private final String tag;
		private final Class<?> clss;
		private final Bundle args;
		private Fragment fragment;

		TabInfo(String tag, Class<?> clss, Bundle args) {
			this.tag = tag;
			this.clss = clss;
			this.args = args;
		}
	}

	/** Creates an empty tab. */
	static class DummyTabFactory implements TabContentFactory {
		private final Context context;

		public DummyTabFactory(Context context) {
			this.context = context;
		}

		@Override
		public View createTabContent(String tag) {
			View view = new View(this.context);
			view.setMinimumWidth(0);
			view.setMinimumHeight(0);
			return view;
		}
	}

	public TabManager(FragmentActivity activity, TabHost tabHost, int containerId) {
		this.activity = activity;
		this.tabHost = tabHost;
		this.containerId = containerId;
		this.tabHost.setOnTabChangedListener(this);
	}

	public void addTab(TabSpec tabSpec, Class<?> clss, Bundle args) {
		tabSpec.setContent(new DummyTabFactory(this.activity));
		String tag = tabSpec.getTag();
		TabInfo tabInfo = new TabInfo(tag, clss, args);

		// Check to see if there is already a fragment for this tab. If so,
		// deactivate it, because the initial state is that a tab isn't shown.
		tabInfo.fragment = this.activity.getSupportFragmentManager().findFragmentByTag(tag);
		if (tabInfo.fragment != null && !tabInfo.fragment.isDetached()) {
			FragmentTransaction transaction = this.activity.getSupportFragmentManager().beginTransaction();
			transaction.detach(tabInfo.fragment).commit();
		}
		this.tabInfoMap.put(tag, tabInfo);
		this.tabHost.addTab(tabSpec);
	}

	@Override
	public void onTabChanged(String tabId) {
		TabInfo newTab = this.tabInfoMap.get(tabId);
		if (this.currentTab != newTab) {
			FragmentTransaction transaction = this.activity.getSupportFragmentManager().beginTransaction();
			if (this.currentTab != null && this.currentTab.fragment != null) {
				transaction.detach(this.currentTab.fragment);
			}
			if (newTab != null) {
				if (newTab.fragment == null) {
					newTab.fragment = Fragment.instantiate(this.activity, newTab.clss.getName(), newTab.args);
					transaction.add(this.containerId, newTab.fragment, newTab.tag);
				} else {
					transaction.attach(newTab.fragment);
				}
			}
			this.currentTab = newTab;
			transaction.commit();
			this.activity.getSupportFragmentManager().executePendingTransactions();
			this.setTabWidgetPreference(this.tabHost.getTabWidget());
			WebtrendsDataCollectionHelper.getInstance().onButtonClick("HomeActivity/TabManager", "choose the tab : "+ this.currentTab.tag, "Click", null);
		}
	}

	/**
	 * set TabWidget
	 * 
	 * @param tabWidget
	 * @param size
	 *            font size
	 * @param color
	 *            font color
	 */
	private void setTabWidgetPreference(TabWidget tabWidget) {
		TextView tv = null;
		for (int i = 0, count = tabWidget.getChildCount(); i < count; i++) {
			// View view = this.tabHost.getCurrentView();
			tv = ((TextView) tabWidget.getChildAt(i).findViewById(android.R.id.title));
			if (this.tabHost.getCurrentTab() == i) {
				tv.setTextColor(this.activity.getResources().getColorStateList(R.color.aliceblue_100));
			} else {
				tv.setTextColor(this.activity.getResources().getColorStateList(R.color.black_100));
			}
		}
	}
}
