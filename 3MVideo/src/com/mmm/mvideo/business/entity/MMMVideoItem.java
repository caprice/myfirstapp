package com.mmm.mvideo.business.entity;

import java.io.Serializable;
import java.util.ArrayList;

import com.mmm.mvideo.common.MMMCommonUtils;

/**
 * @author Eric Liu
 * 
 */
public class MMMVideoItem implements Serializable {

	private static final long serialVersionUID = 432094076396374023L;
	private String title;
	private String desc;
	private String imgName;
	private String token;
	private String tags;
	private String url;
	private String type;
	private ArrayList<MMMVideoItem> subItems = new ArrayList<MMMVideoItem>();
	private int playType;
	private String localPath;
	private boolean isPlayInLocal = false;

	/**
	 * @return
	 */
	public boolean hasChildItems() {
		return MMMCommonUtils.isEmpty(url);
	}

	/**
	 * whether need to read videos from brightcove by readAPI
	 * 
	 * @return
	 */
	public boolean isFindVideoByAPI() {
		return !MMMCommonUtils.isEmpty(token) && !MMMCommonUtils.isEmpty(tags);
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public ArrayList<MMMVideoItem> getSubItems() {
		return subItems;
	}

	public void addVideoItem(MMMVideoItem subItem) {
		this.subItems.add(subItem);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getImgName() {
		return imgName;
	}

	public void setImgName(String imgName) {
		this.imgName = imgName;
	}

	public int getPlayType() {
		return playType;
	}

	public void setPlayType(int playType) {
		this.playType = playType;
	}

	public boolean isLeafNode() {
		return url != null && "".equals(this.url);
	}

	/**
	 * The Interface PlayType.
	 */
	public static interface PlayType {

		final static int PLAY_VIDEO = 0;
		final static int PLAY_PDF = 1;
	}

	public boolean isPlayInLocal() {
		return isPlayInLocal;
	}

	public void setPlayInLocal(boolean isPlayInLocal) {
		this.isPlayInLocal = isPlayInLocal;
	}

	public String getLocalPath() {
		return localPath;
	}

	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}

}
