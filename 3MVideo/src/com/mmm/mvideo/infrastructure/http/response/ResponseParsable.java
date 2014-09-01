/*
 * ky
 */
package com.mmm.mvideo.infrastructure.http.response;

// TODO: Auto-generated Javadoc
/**
 * The Interface IResponseParser.
 * @author a37wczz
 */
public interface ResponseParsable {

	/**
	 * Parses the response both head and body.
	 * 
	 * @param resStr
	 *            the res str
	 */
	void parseResponse(String resStr);
}
