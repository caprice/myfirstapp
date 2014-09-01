package com.mmm.mvideo.xmlparser;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * XMl utility methods.
 * 
 */
public class XmlUtil {
	/**
	 * Document validation types.
	 */
	public static enum VALIDATION_TYPE {
		/**
		 * No validation.
		 */
		NONE,
		/**
		 * DTD based validation.
		 */
		DTD,
		/**
		 * XSD based validation.
		 */
		XSD,
	}

	private static String ELEMENT_NAME_FUNC = "/name()";
	private static XPathFactory xPathFactory = XPathFactory.newInstance();

	/**
	 * Get the W3C Node instance associated with the XPath selection supplied.
	 * 
	 * @param node
	 *            The document node to be searched.
	 * @param xpath
	 *            The XPath String to be used in the selection.
	 * @return The W3C Node instance at the specified location in the document,
	 *         or null.
	 */
	public static Node getNode(Node node, String xpath) {
		NodeList nodeList = getNodeList(node, xpath);
		if (nodeList == null || nodeList.getLength() == 0) {
			return null;
		} else {
			return nodeList.item(0);
		}
	}

	/**
	 * Get the W3C NodeList instance associated with the XPath selection
	 * supplied.
	 * 
	 * @param node
	 *            The document node to be searched.
	 * @param xpath
	 *            The XPath String to be used in the selection.
	 * @return The W3C NodeList instance at the specified location in the
	 *         document, or null.
	 */
	public static NodeList getNodeList(Node node, String xpath) {
		if (node == null) {
			throw new IllegalArgumentException("null document arg in method call.");
		} else if (xpath == null) {
			throw new IllegalArgumentException("null xpath arg in method call.");
		}
		try {
			XPath xpathEvaluater = xPathFactory.newXPath();
			if (xpath.endsWith(ELEMENT_NAME_FUNC)) {
				return (NodeList) xpathEvaluater.evaluate(xpath.substring(0, xpath.length() - ELEMENT_NAME_FUNC.length()), node, XPathConstants.NODESET);
			} else {
				return (NodeList) xpathEvaluater.evaluate(xpath, node, XPathConstants.NODESET);
			}
		} catch (XPathExpressionException e) {
			throw new IllegalArgumentException("bad xpath expression [" + xpath + "].");
		}
	}
}
