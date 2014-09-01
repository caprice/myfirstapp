package com.mmm.mvideo.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

import com.mmm.mvideo.R;
import com.mmm.mvideo.activity.fragment.NavigationFragment.FragmentParameterKey;
import com.mmm.mvideo.business.entity.MMMVideoItem;
import com.mmm.mvideo.common.ApplicationCommon;
import com.mmm.mvideo.view.MMMVideoView;
import com.mmm.mvideo.webtrends.WebtrendsDataCollectionHelper;
import com.mmm.mvideo.widget.MyMediaController;
import com.webtrends.mobile.analytics.WebtrendsConfigurator;

// TODO: Auto-generated Javadoc
/**
 * The Class FullScreenActivity.
 * 
 * @author a38c1zz
 */
public class FullScreenActivity extends MMMActivity {

	private final static String TAG = "FullScreenActivity";
	private int mLayout = 0;

	public static List<MMMVideoItem> playList = new ArrayList<MMMVideoItem>();

	private int playingPos;
	private static int position;

	private MMMVideoView vv = null;
	private MyMediaController controller;

	private GestureDetector mGestureDetector = null;

	private boolean isControllerShow = false;

	/** Called when the activity is first created. */
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fullscreen);
		position = getIntent().getExtras().getInt(FragmentParameterKey.CUR_ITEM_POSITION);
		playList = (ArrayList<MMMVideoItem>) getIntent().getExtras().get(FragmentParameterKey.CUR_ITEM_LIST);
		MMMVideoItem item = playList.get(position);
		vv = (MMMVideoView) findViewById(R.id.vv);
		controller = new MyMediaController(this);
		controller.setShowExpandImage(false);
		controller.setAnchorView(vv);
		controller.setPadding(0, 0, 0, 3);
		vv.setMediaController(controller);

		if (item.isPlayInLocal()) {
			vv.setVideoPath(item.getLocalPath());
		} else {
			vv.setVideoURI(Uri.parse(item.getUrl()));
		}

		mGestureDetector = new GestureDetector(this, new SimpleOnGestureListener() {

			@Override
			public boolean onDoubleTap(final MotionEvent e) {
				// TODO Auto-generated method stub
				if (mLayout == MMMVideoView.VIDEO_LAYOUT_ZOOM)
					mLayout = MMMVideoView.VIDEO_LAYOUT_ORIGIN;
				else
					mLayout++;
				if (vv != null)
					vv.setVideoLayout(mLayout, 0);
				Log.d(TAG, "onDoubleTap");

				return true;
			}

			@Override
			public boolean onSingleTapConfirmed(final MotionEvent e) {
				// TODO Auto-generated method stub
				if (!isControllerShow) {
				} else {
				}
				return true;
			}

			@Override
			public void onLongPress(final MotionEvent e) {
				if (vv.isPlaying()) {
					vv.pause();
				} else {
					vv.start();
				}
			}
		});

		vv.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				controller.show(0);
			}
		});

		initWebtrends();
		WebtrendsDataCollectionHelper.getInstance().onScreenView("FullScreenActivity/OnCreateView", "play the video : " + item.getUrl(), "View", null, "Play Video");

	}

	@Override
	protected void onStart() {
		// progressDialog = ProgressDialog.show(this, "Loading the video...",
		// "starting to play ");
		playingPos = getIntent().getExtras().getInt(ApplicationCommon.TabFragmentParmKey.CUR_PLAYING_POS);
		startProgressDialog("");
		vv.setOnPreparedListener(new OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer arg0) {
				// TODO Auto-generated method stub
				controller.show();
				stopProgressDialog();
				vv.seekTo(playingPos);
				vv.start();
			}
		});
		super.onStart();
	}

	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub

		boolean result = mGestureDetector.onTouchEvent(event);

		if (!result) {
			if (event.getAction() == MotionEvent.ACTION_UP) {

			}
			result = super.onTouchEvent(event);
		}

		return result;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		if (vv != null)
			vv.setVideoLayout(mLayout, 0);
		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onPause() {
		super.onPause();
		stopPlayer();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (vv != null)
			vv.resume();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(ApplicationCommon.TabFragmentParmKey.CUR_PLAYING_POS, vv.getCurrentPosition());
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		stopPlayer();
		super.onDestroy();
		WebtrendsDataCollectionHelper.getInstance().onViewEnd("FullScreenActivity/OnCreateView", null);
	}

	private void stopPlayer() {
		if (vv != null)
			vv.pause();
	}

	/**
	 * 
	 */
	private void initWebtrends() {
		WebtrendsConfigurator.ConfigureDC(this);
		Uri localUri = getIntent().getData();
		WebtrendsDataCollectionHelper.getInstance().setSessionInfo(localUri, this);
	}
}
