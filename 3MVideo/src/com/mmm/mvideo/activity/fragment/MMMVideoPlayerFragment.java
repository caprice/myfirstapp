package com.mmm.mvideo.activity.fragment;

import java.util.ArrayList;
import java.util.logging.Logger;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mmm.mvideo.R;
import com.mmm.mvideo.activity.fragment.NavigationFragment.FragmentParameterKey;
import com.mmm.mvideo.business.entity.MMMVideoItem;
import com.mmm.mvideo.common.ApplicationCommon;
import com.mmm.mvideo.infrastructure.media.FileDownloadResult;
import com.mmm.mvideo.infrastructure.media.HttpDownloader;
import com.mmm.mvideo.view.MMMVideoView;
import com.mmm.mvideo.widget.MyMediaController;

/**
 * @author Eric Liu
 * 
 */
public class MMMVideoPlayerFragment extends MMMBasePlayerFragment {
	private static int playingPos = 0;

	Logger apiLogger = Logger.getLogger("MMMVideoPlayerFragment");

	/** The m player. */
	private MMMVideoView mPlayer;

	/** The m btn download. */
	private ImageButton mBtnDownload;

	private MMMVideoItem curiItem;

	/** The m video desc. */
	private TextView mVideoTitle, mVideoDesc;

	private MyMediaController controller;

	private GestureDetector mGestureDetector = null;

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			final Bundle arguments = msg.getData();
			switch (msg.what) {
			case ApplicationCommon.HandlerMessageType.DOWN_LOAD_FILE:
				FileDownloadResult result = (FileDownloadResult) arguments.get("DOWNLOADRESULT");
				String displayMsg = "";
				if (result.getStatus() == FileDownloadResult.FILE_DOWNLOAD_SUCC) {
					displayMsg = "Downloaded successfully!";
				} else if (result.getStatus() == FileDownloadResult.FILE_IS_EXISTED) {
					displayMsg = "File has been downloaded!";
				} else if (result.getStatus() == FileDownloadResult.FILE_IS_DOWNDOADING) {
					displayMsg = "File is downloading!";
				} else {
					displayMsg = "Download file error";
				}
				if (getActivity() != null)
					Toast.makeText(getActivity(), displayMsg, Toast.LENGTH_SHORT).show();
			default:
				break;
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			playingPos = savedInstanceState.getInt(ApplicationCommon.TabFragmentParmKey.CUR_PLAYING_POS);
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.i(TAG, ".onCreateView." + this);
		View view = inflater.inflate(R.layout.video_fragment, container, false);
		final Bundle args = getArguments();
		position = args.getInt(FragmentParameterKey.CUR_ITEM_POSITION);
		playList = (ArrayList<MMMVideoItem>) args.get(FragmentParameterKey.CUR_ITEM_LIST);
		curiItem = playList.get(position);
		mPlayer = (MMMVideoView) view.findViewById(R.id.player);
		mPlayer.setArgs(args);
		view.setBackgroundResource(R.drawable.round_border);
		this.initMediaPlayer();
		mBtnDownload = (ImageButton) view.findViewById(R.id.btnDownload);
		mBtnDownload.setVisibility(View.INVISIBLE);
		mVideoTitle = (TextView) view.findViewById(R.id.tvVideoTitle);
		mVideoDesc = (TextView) view.findViewById(R.id.tvVideoDesc);

		mBtnDownload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startProgressDialog("");
				new Thread(new Runnable() {

					@Override
					public void run() {
						HttpDownloader fileDownloader = new HttpDownloader(getActivity());
						FileDownloadResult result = fileDownloader.downFile(curiItem, ApplicationCommon.VIDEO_SAVE_PATH);
						Message msg = new Message();
						msg.what = ApplicationCommon.HandlerMessageType.DOWN_LOAD_FILE;
						Bundle data = new Bundle();
						data.putSerializable("DOWNLOADRESULT", result);
						msg.setData(data);
						handler.sendMessage(msg);
						stopProgressDialog();
					}
				}).start();
			}
		});

		return view;
	}

	@SuppressWarnings("deprecation")
	private void initMediaPlayer() {
		controller = new MyMediaController(getActivity());
		controller.setShowExpandImage(true);
		controller.setAnchorView(mPlayer);
		mPlayer.setMediaController(controller);
		controller.setPadding(0, 0, 0, 180);
		mGestureDetector = new GestureDetector(new SimpleOnGestureListener() {

			@Override
			public boolean onDoubleTap(final MotionEvent e) {
				if (mPlayer.isPlaying()) {
					mPlayer.pause();
				} else {
					mPlayer.start();
				}
				return true;
			}

			@Override
			public boolean onSingleTapConfirmed(final MotionEvent e) {
				if (controller.isShowing()) {
					controller.hide();
				} else {
					controller.show();
				}
				return true;
			}

			@Override
			public void onLongPress(final MotionEvent e) {
				// if (mPlayer.isPlaying()) {
				// mPlayer.pause();
				// } else {
				// mPlayer.start();
				// }
			}
		});
		mPlayer.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mGestureDetector.onTouchEvent(event);
				return true;
			}

		});
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.i(TAG, ".onStart." + this);
		playVideo();
	}

	private void playVideo() {
		if (mPlayer != null && mPlayer.isShown()) {
			mVideoTitle.setText(curiItem.getTitle());
			mVideoDesc.setText(curiItem.getDesc());
			if (mPlayer.isPlaying()) {
				mPlayer.stopPlayback();
			}
			mPlayer.setBackgroundColor(Color.BLACK);
			startProgressDialog("");
			final String  videoPath;
			if (curiItem.isPlayInLocal()) {
				videoPath = curiItem.getLocalPath();
				mPlayer.setVideoPath(videoPath);
			} else {
				videoPath = curiItem.getUrl();
				mBtnDownload.setVisibility(View.VISIBLE);
				mPlayer.setVideoURI(Uri.parse(videoPath));
			}
			mPlayer.setSoundEffectsEnabled(true);
			mPlayer.setOnPreparedListener(new OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mp) {
					startProgressDialog("");
					mPlayer.requestFocus();
					mPlayer.setBackgroundColor(Color.TRANSPARENT);
					mPlayer.seekTo(playingPos);
					mPlayer.start();
					controller.show();
					stopProgressDialog();
				}
			});
			Log.d("Mv", videoPath);
			Log.d("Mv", mPlayer.getDuration() + "");
		}
	}

	/**
	 * let a button show in a cerntain time, and then hide it /**
	 * 
	 * @param timeout
	 */
	// private void showFullScreenBtn(int timeout) {
	// // btnFullScreen.setVisibility(View.VISIBLE);
	// Message msg =
	// handler.obtainMessage(ApplicationCommon.HandlerMessageType.HIDE_FULL_SCREEN_BTN);
	// if (timeout != 0) {
	// handler.removeMessages(ApplicationCommon.HandlerMessageType.HIDE_FULL_SCREEN_BTN);
	// handler.sendMessageDelayed(msg, timeout * 1000);
	// }
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		Log.d(TAG, ".onResume." + this);
		super.onResume();
		mVideoTitle.setText(curiItem.getTitle());
		mVideoDesc.setText(curiItem.getDesc());
		if (curiItem.isPlayInLocal()) {
			mPlayer.setVideoPath(curiItem.getLocalPath());
		} else {
			mBtnDownload.setVisibility(View.VISIBLE);
			mPlayer.setVideoURI(Uri.parse(curiItem.getUrl()));
		}
		mPlayer.setOnPreparedListener(new OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				startProgressDialog("");
				mPlayer.requestFocus();
				mPlayer.setBackgroundColor(Color.TRANSPARENT);
				playingPos = getArguments().getInt(ApplicationCommon.TabFragmentParmKey.CUR_PLAYING_POS);
				mPlayer.seekTo(playingPos);
				mPlayer.start();
				controller.show();
				stopProgressDialog();
			}
		});

	}

	@Override
	public void onPause() {
		super.onPause();
		Log.i(TAG, ".onPause." + this);
		if (mPlayer != null)
			mPlayer.pause();
		onSaveInstanceState(getArguments());
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		Log.d(TAG, ".onSaveInstanceState." + this);
		playingPos = mPlayer.getCurrentPosition();
		outState.putInt(ApplicationCommon.TabFragmentParmKey.CUR_PLAYING_POS, playingPos);
		super.onSaveInstanceState(outState);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onStop()
	 */
	@Override
	public void onStop() {
		Log.d(TAG, ".onStop." + this);
		stopPlayer();
		super.onStop();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		if (mPlayer != null)
			mPlayer.stopPlayback();
		super.onDestroy();
	}

	public interface VideoFragmentParameterKey {
		public static final String PLAYING_VIDEO_ITEM = "PLAYING_VIDEO_ITEM";
		public static final String CURRENT_VIDEO_LIST = "CURRENT_VIDEO_LIST";
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ApplicationCommon.REQ_SYSTEM_FULLSCREEN) {
			super.onActivityResult(requestCode, resultCode, data);
			playingPos = data.getExtras().getInt(ApplicationCommon.TabFragmentParmKey.CUR_PLAYING_POS);
		}
	}

	private void stopPlayer() {
		if (mPlayer != null)
			mPlayer.pause();
	}

}
