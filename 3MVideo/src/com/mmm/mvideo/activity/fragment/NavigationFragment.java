package com.mmm.mvideo.activity.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mmm.mvideo.R;
import com.mmm.mvideo.adapter.NavigationAdapter;
import com.mmm.mvideo.business.entity.MMMVideoItem;
import com.mmm.mvideo.business.service.MMMVideoRetrieveService;
import com.mmm.mvideo.common.ApplicationCommon;
import com.mmm.mvideo.common.MMMCommonUtils;
import com.mmm.mvideo.infrastructure.media.FileUtil;
import com.mmm.mvideo.webtrends.WebtrendsDataCollectionHelper;

/**
 * The Class NavigationFragment.
 * 
 * @author a37wczz
 */
public class NavigationFragment extends MMMBaseFragment {

	/** The observer. */
	private Fragment observer;

	/** The m listener. */
	private OnNavigationEventListener mListener;

	/** The m title. */
	private TextView mTitle;

	/** The m navis. */
	private ListView mNavis;

	/** The m back. */
	private ImageButton mBack;

	private NavigationAdapter adapter;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		boolean isDisplayRefresh = getArguments().getBoolean(FragmentParameterKey.SHOW_REFRESH_SETTING);
		setHasOptionsMenu(isDisplayRefresh);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onDetach()
	 */
	@Override
	public void onDetach() {
		super.onDetach();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.navigation, null, false);
		this.mTitle = (TextView) view.findViewById(R.id.tvNaviTitle);
		this.mNavis = (ListView) view.findViewById(R.id.lvNavigation);
		this.mBack = (ImageButton) view.findViewById(R.id.btnBack);
		final Bundle arguments = getArguments();
		boolean isShowBackBtn = arguments.getBoolean(FragmentParameterKey.SHOW_BACK_BTN);
		if (isShowBackBtn) {
			this.mBack.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mListener.onBackClicked(getId());
				}
			});
		} else {
			this.mBack.setVisibility(View.GONE);
		}
		this.mTitle.setText(arguments.getCharSequence(FragmentParameterKey.TITLE));

		ArrayList<MMMVideoItem> lines = (ArrayList<MMMVideoItem>) arguments.get(FragmentParameterKey.NAV_ITEMS);
		int layout_resource_id = arguments.getInt(FragmentParameterKey.NAV_LINE_RESOURCE_ID);
		adapter = new NavigationAdapter(inflater, lines, layout_resource_id);
		this.mNavis.setAdapter(adapter);

		this.mNavis.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				mListener.onNavigationSelected(position, arguments);

			}
		});
		boolean isShowContextMenu = arguments.getBoolean(FragmentParameterKey.SHOW_CONTEXT_MENU);
		if (isShowContextMenu) {
			registerForContextMenu(mNavis);
		}
		return view;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mListener = (OnNavigationEventListener) this.observer;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		menu.removeGroup(10);
		menu.add(10, 10, 0, "update playlist");
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 10:
			refreshPlayList();
			break;
		}
		return false;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
		MMMVideoItem selectedItem = (MMMVideoItem) this.mNavis.getItemAtPosition(info.position);
		menu.setHeaderTitle(selectedItem.getTitle());
		menu.add(Menu.NONE, Menu.FIRST + 1, 5, "delete").setIcon(getResources().getDrawable(R.drawable.delete));
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch (item.getItemId()) {
		case Menu.FIRST + 1:
			deleteFile(info.position);
			break;
		}
		return super.onContextItemSelected(item);  
	}

	/**
	 * @param selectedItem
	 */
	private void deleteFile(int selectPos) {
		MMMVideoItem selectedItem = (MMMVideoItem) this.mNavis.getItemAtPosition(selectPos);
		MMMVideoRetrieveService service = new MMMVideoRetrieveService(getActivity());
		String msg = "";
		if (selectedItem == null || selectedItem.getLocalPath() == null){
			return;
		}
		if (FileUtil.deleteFile(selectedItem.getLocalPath())) {
			service.deleteDownloadedFile(selectedItem.getUrl());
			msg = "Delete file sucessfully!";
			this.adapter.getGroups().remove(selectPos);
			this.adapter.notifyDataSetChanged();
		} 
		Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
	}

	private void refreshPlayList() {
		Bundle arguments = getArguments();
		MMMVideoItem item = (MMMVideoItem) arguments.get(FragmentParameterKey.PARENT_ITEM);
		adapter.setGroups(new ArrayList<MMMVideoItem>());
		adapter.notifyDataSetChanged();
		MMMCommonUtils.pullVideoPlayList(getActivity(), item, handler, true);
	}

	/**
	 * The listener interface for receiving onNavigationEvent events. The class
	 * that is interested in processing a onNavigationEvent event implements
	 * this interface, and the object created with that class is registered with
	 * a component using the component's
	 * <code>addOnNavigationEventListener</code> method. When the
	 * onNavigationEvent event occurs, that object's appropriate method is
	 * invoked.
	 * 
	 * @see OnNavigationEventEvent
	 */
	public interface OnNavigationEventListener {

		/**
		 * On navigation selected.
		 * 
		 * @param fragmentType
		 *            the fragment type
		 * @param position
		 *            the position
		 * @param params
		 *            the params
		 */
		public void onNavigationSelected(int position, Bundle arguments);

		/**
		 * On back clicked.
		 * 
		 * @param fragmentId
		 *            the fragment id
		 */
		public void onBackClicked(int fragmentId);
	}

	/**
	 * Gets the observer.
	 * 
	 * @return the observer
	 */
	public Fragment getObserver() {
		return this.observer;
	}

	/**
	 * Sets the observer.
	 * 
	 * @param observer
	 *            the new observer
	 */
	public void setObserver(Fragment observer) {
		this.observer = observer;
	}

	/**
	 * The Interface FragmentTypeType.
	 */
	public static interface FragmentTypeType {

		/** The group. */
		String GROUP = "GROUP";

		/** The video. */
		String VIDEO = "VIDEO";
	}

	/**
	 * The Interface FragmentParameterKey.
	 */
	public static interface FragmentParameterKey {

		/** The title. */
		public static final String TITLE = "TITLE";
		public static final String NAV_ITEMS = "NAV_ITEMS";
		/** show back button. */
		public static final String SHOW_BACK_BTN = "SHOW_BACK_BTN";
		public static final String SHOW_REFRESH_SETTING = "SHOW_REFRESH_SETTING";
		public static final String SHOW_CONTEXT_MENU = "SHOW_CONTEXT_MENU";
		public static final String CUR_ITEM_POSITION = "CUR_ITEM_POSITION";
		public static final String CUR_ITEM_LIST = "CUR_ITEM_LIST";
		public static final String PARENT_ITEM = "PARENT_ITEM";
		public static final String NAV_LINE_RESOURCE_ID = "NAV_LINE_RESOURCE_ID";
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			final Bundle arguments = msg.getData();
			switch (msg.what) {
			case ApplicationCommon.HandlerMessageType.CONNECT_TO_SERVER_ERROR:
				Toast.makeText(getActivity(), "Can't connect to the video server", Toast.LENGTH_SHORT).show();
				break;
			case ApplicationCommon.HandlerMessageType.PULL_PLAY_LIST:
				adapter.setGroups((ArrayList<MMMVideoItem>) arguments.get(FragmentParameterKey.NAV_ITEMS));
				adapter.notifyDataSetChanged();
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onStart() {
		super.onStart();
		WebtrendsDataCollectionHelper.getInstance().onViewStart("HomeActivity/" + getTag() + "/" + mTitle, null);
	}

	@Override
	public void onStop() {
		super.onStop();
		WebtrendsDataCollectionHelper.getInstance().onViewStart("HomeActivity/" + getTag() + "/" + mTitle, null);
	}
}
