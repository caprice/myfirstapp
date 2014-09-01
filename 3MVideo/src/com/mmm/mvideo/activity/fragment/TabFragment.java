package com.mmm.mvideo.activity.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mmm.mvideo.R;
import com.mmm.mvideo.activity.fragment.NavigationFragment.FragmentParameterKey;
import com.mmm.mvideo.activity.fragment.NavigationFragment.OnNavigationEventListener;
import com.mmm.mvideo.business.entity.MMMVideoItem;
import com.mmm.mvideo.business.entity.MMMVideoItem.PlayType;
import com.mmm.mvideo.business.service.MMMVideoRetrieveService;
import com.mmm.mvideo.common.ApplicationCommon;
import com.mmm.mvideo.common.MMMCommonUtils;
import com.mmm.mvideo.webtrends.WebtrendsDataCollectionHelper;
import com.webtrends.mobile.analytics.WebtrendsConfigurator;

/**
 * Fragment for a tab in the Tab View.
 * 
 * @author Eric Liu
 * 
 */
public class TabFragment extends MMMBaseFragment implements OnNavigationEventListener {
	private int layoutResource;
	private ArrayList<MMMVideoItem> navItems;

	/** The handler. */
	Handler handler = new TabFragmentHandler(this);


	@Override
	public void onCreate(Bundle savedInstanceState) {
		WebtrendsConfigurator.ConfigureDC(getActivity());
		super.onCreate(savedInstanceState);
		this.layoutResource = R.layout.tabfragment_layout;
		Bundle args = getArguments();
		if (args != null && args.getInt("layoutResource") != 0) {
			this.layoutResource = args.getInt("layoutResource");
		}
		args.putBoolean(FragmentParameterKey.SHOW_REFRESH_SETTING, false);
		setHasOptionsMenu(true);

	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Bundle args = getArguments();
		if (args != null && args.getInt("layoutResource") != 0) {
			this.layoutResource = args.getInt("layoutResource");
		}
		View view = inflater.inflate(this.layoutResource, container, false);
		if (!getTag().equalsIgnoreCase("HOME") && !getTag().equalsIgnoreCase("Downloaded")) {
			args.putInt(FragmentParameterKey.NAV_LINE_RESOURCE_ID, R.layout.navigation_line_group);
			args.putBoolean(FragmentParameterKey.SHOW_BACK_BTN, false);
			args.putBoolean(FragmentParameterKey.SHOW_REFRESH_SETTING, false);
			MMMCommonUtils.addNavFragment(this, args);
		} else if (getTag().equalsIgnoreCase("Downloaded")) {
			args = new Bundle();
			MMMVideoRetrieveService service = new MMMVideoRetrieveService(getActivity());
			ArrayList<MMMVideoItem> downloadFiles = service.getAllDownLoadedFiles();
			args.putCharSequence(NavigationFragment.FragmentParameterKey.TITLE, getTag());
			args.putSerializable(FragmentParameterKey.NAV_ITEMS, downloadFiles);
			args.putInt(FragmentParameterKey.NAV_LINE_RESOURCE_ID, R.layout.navigation_line);
			args.putBoolean(FragmentParameterKey.SHOW_BACK_BTN, false);
			args.putBoolean(FragmentParameterKey.SHOW_REFRESH_SETTING, false);
			args.putBoolean(FragmentParameterKey.SHOW_CONTEXT_MENU, true);
			MMMCommonUtils.addNavFragment(this, args);
		}
		return view;

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		menu.removeGroup(10);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mmm.mvideo.activity.fragment.NavigationFragment.OnNavigationEventListener
	 * #onNavigationSelected(java.lang.String, int, java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void onNavigationSelected(int position, Bundle arguments) {
		ArrayList<MMMVideoItem> navItems = (ArrayList<MMMVideoItem>) arguments.get(FragmentParameterKey.NAV_ITEMS);
		MMMVideoItem navItem = navItems.get(position);
		if (navItem.hasChildItems()) {
			ArrayList<MMMVideoItem> subItems = navItem.getSubItems();
			if (navItem.isFindVideoByAPI()) {
				MMMCommonUtils.pullVideoPlayList(getActivity(), navItem, handler);
			} else {
				Bundle args = new Bundle();
				if ("PDFs".equalsIgnoreCase(navItem.getTitle())) {
					args.putInt(FragmentParameterKey.NAV_LINE_RESOURCE_ID, R.layout.navigation_line);
					for (MMMVideoItem tempItem : subItems) {
						tempItem.setPlayType(PlayType.PLAY_PDF);
					}
				} else {
					args.putInt(FragmentParameterKey.NAV_LINE_RESOURCE_ID, R.layout.navigation_line_group);
				}
				args.putBoolean(FragmentParameterKey.SHOW_BACK_BTN, true);
				args.putBoolean(FragmentParameterKey.SHOW_REFRESH_SETTING, false);
				args.putString(FragmentParameterKey.TITLE, navItem.getTitle());
				args.putSerializable(FragmentParameterKey.NAV_ITEMS, subItems);
				MMMCommonUtils.addNavFragment(this, args);
			}
			WebtrendsDataCollectionHelper.getInstance().onScreenView("HomeActivity/" + getTag(), "select nav : " + navItem.getTitle(), "Select", null, "Select Nav");
		} else {
			if (navItem.getPlayType() == PlayType.PLAY_PDF) {
				Bundle args = new Bundle();
				args.putBoolean(FragmentParameterKey.SHOW_BACK_BTN, true);
				args.putString(FragmentParameterKey.TITLE, navItem.getTitle());
				args.putSerializable(FragmentParameterKey.CUR_ITEM_LIST, navItems);
				args.putInt(FragmentParameterKey.CUR_ITEM_POSITION, position);
				MMMCommonUtils.addContentFragment(this, MMMPDFPlayerFragment.class, args);
			} else if (navItem.getPlayType() == PlayType.PLAY_VIDEO) {
				Bundle args = new Bundle();
				args.putBoolean(FragmentParameterKey.SHOW_BACK_BTN, true);
				args.putString(FragmentParameterKey.TITLE, navItem.getTitle());
				args.putSerializable(FragmentParameterKey.CUR_ITEM_LIST, navItems);
				args.putInt(FragmentParameterKey.CUR_ITEM_POSITION, position);
				MMMCommonUtils.addContentFragment(this, MMMVideoPlayerFragment.class, args);
			} else {
			}
		}
	}

	@Override
	public void onBackClicked(int fragmentId) {
		FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
		fragmentTransaction.remove(getFragmentManager().findFragmentById(fragmentId));
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
		WebtrendsDataCollectionHelper.getInstance().onButtonClick("Nav/back", "click nav back button", "Click", null);
	}

	/**
	 * Handler to update video UI
	 * 
	 * @author A38C1ZZ
	 * 
	 */
	public static class TabFragmentHandler extends Handler {
		public static final int HIDE_FULL_SCREEN_BTN = 10;
		private TabFragment tabFragment;

		public TabFragmentHandler(TabFragment tabFragment) {
			this.tabFragment = tabFragment;
		}

		@Override
		public void handleMessage(Message msg) {
			final Bundle arguments = msg.getData();
			switch (msg.what) {
			case ApplicationCommon.HandlerMessageType.CONNECT_TO_SERVER_ERROR:
				Toast.makeText(tabFragment.getActivity(), "Can't connect to the video server", Toast.LENGTH_SHORT).show();
				break;
			case ApplicationCommon.HandlerMessageType.PULL_PLAY_LIST:
				arguments.putInt(FragmentParameterKey.NAV_LINE_RESOURCE_ID, R.layout.navigation_line);
				MMMCommonUtils.addNavFragment(tabFragment, arguments);
				break;
			default:
				break;
			}
		}

	}

	@Override
	public void onStart() {
		super.onStart();
		WebtrendsDataCollectionHelper.getInstance().onViewStart("HomeActivity/TabFragment/" + getTag(), null);
	}

	@Override
	public void onStop() {
		super.onStop();
		WebtrendsDataCollectionHelper.getInstance().onViewEnd("HomeActivity/TabFragment/" + getTag(), null);
	}

}
