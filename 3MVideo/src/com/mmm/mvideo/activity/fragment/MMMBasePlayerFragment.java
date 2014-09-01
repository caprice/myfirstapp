package com.mmm.mvideo.activity.fragment;

import java.util.ArrayList;

import com.mmm.mvideo.business.entity.MMMVideoItem;

/**
 * @author Eric Liu
 * 
 */
public class MMMBasePlayerFragment extends MMMBaseFragment {
	protected int position;
	protected ArrayList<MMMVideoItem> playList;

	public int getPosition() {
		return position;
	}

	public ArrayList<MMMVideoItem> getPlayList() {
		return playList;
	}

}
