package com.mmm.mvideo.webtrends;

import java.util.Map;

/**
 * @author Eric Liu
 * 
 */
public class WebtrendsDetailedInfo {
	private String eventPath;
	private String eventDescr;
	private String eventType;
	private Map<String, String> customData;
	private String contentGroup;

	/**
	 * 
	 */
	public WebtrendsDetailedInfo() {

	}

	/**
	 * @param eventPath
	 * @param eventDescr
	 * @param eventType
	 * @param contentGroup
	 */
	public WebtrendsDetailedInfo(String eventPath, String eventDescr, String eventType, String contentGroup) {
		this.eventPath = eventPath;
		this.eventDescr = eventDescr;
		this.eventType = eventType;
		this.contentGroup = contentGroup;
	}

	/**
	 * @param eventPath
	 * @param eventDescr
	 * @param eventType
	 * @param contentGroup
	 * @param customData
	 */
	public WebtrendsDetailedInfo(String eventPath, String eventDescr, String eventType, String contentGroup, Map<String, String> customData) {
		this(eventPath, eventDescr, eventType, contentGroup);
		this.customData = customData;
	}

	public String getEventPath() {
		return eventPath;
	}

	public void setEventPath(String eventPath) {
		this.eventPath = eventPath;
	}

	public String getEventDescr() {
		return eventDescr;
	}

	public void setEventDescr(String eventDescr) {
		this.eventDescr = eventDescr;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public Map<String, String> getCustomData() {
		return customData;
	}

	public void setCustomData(Map<String, String> customData) {
		this.customData = customData;
	}

	public String getContentGroup() {
		return contentGroup;
	}

	public void setContentGroup(String contentGroup) {
		this.contentGroup = contentGroup;
	}

}
