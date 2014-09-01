package com.mmm.mvideo.adapter;

import java.util.List;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mmm.mvideo.R;
import com.mmm.mvideo.business.entity.MMMVideoItem;
import com.mmm.mvideo.infrastructure.media.ImageLoader;

/**
 * The Class NavigationAdapter.
 * 
 * @author a37wczz
 */
public class NavigationAdapter extends BaseAdapter {

	private int layout_resourceId;
	/** The inflater. */
	private LayoutInflater inflater;

	/** The groups. */
	private List<MMMVideoItem> groups;


	/**
	 * Instantiates a new navigation adapter.
	 * 
	 * @param inflater
	 * @param groups
	 * @param layout_resourceId
	 */
	public NavigationAdapter(LayoutInflater inflater, List<MMMVideoItem> groups, int layout_resourceId) {
		super();
		this.inflater = inflater;
		this.groups = groups;
		this.layout_resourceId = layout_resourceId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		if (groups == null) {
			return 0;
		}
		return groups.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		if (groups == null) {
			return null;
		}
		if (position < groups.size()) {
			return groups.get(position);
		} else
			return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (inflater == null) {
			return null;
		}
		MMMVideoItem line = groups.get(position);
		View view;
		if (convertView != null) {
			view = convertView;
		} else {
			view = inflater.inflate(layout_resourceId, parent, false);
		}
		final ImageView mIcon = (ImageView) view.findViewById(R.id.ivLineIcon);
		if (line.getImgName() != null) {
			mIcon.setVisibility(View.VISIBLE);
			Bitmap thumb = ImageLoader.asynLoadImage(line.getImgName(), 128, 128, new ImageLoader.ImageCallback() {

				@Override
				public void imageLoaded(Bitmap imageDrawable, String imageUrl) {
					mIcon.setImageBitmap(imageDrawable);
				}
			}, true);
			if (thumb != null) {
				mIcon.setImageBitmap(thumb);
			}
		} else {
			mIcon.setVisibility(View.GONE);
		}

		TextView mDesc = (TextView) view.findViewById(R.id.tvLineDesc);
		if (line.getDesc() == null) {
			mDesc.setVisibility(View.GONE);
		} else {
			mIcon.setVisibility(View.VISIBLE);
			mDesc.setText(line.getDesc());
		}

		TextView mTitle = (TextView) view.findViewById(R.id.tvLineTitle);
		mTitle.setText(line.getTitle());
		return view;
	}

	public List<MMMVideoItem> getGroups() {
		return groups;
	}

	public void setGroups(List<MMMVideoItem> groups) {
		this.groups = groups;
	}
}
