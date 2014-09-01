package com.mmm.mvideo.xmlparser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.util.Log;

import com.mmm.mvideo.business.entity.MMMVideoItem;

/**
 * @author Eric Liu
 * 
 */
public class MMMPlayListXmlParser {
	private final static String TAG = "MMMPlayListXmlParser";

	/**
	 * 
	 * @param fileName
	 */
	public ArrayList<MMMVideoItem> domXmlParse(InputStream inStream) {
		ArrayList<MMMVideoItem> allItems = new ArrayList<MMMVideoItem>();
		MMMVideoItem levelItem1 = null;
		MMMVideoItem levelItem2 = null;
		MMMVideoItem levelItem3 = null;
		MMMVideoItem levelItem4 = null;
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		Document doc = null;
		try {
			docBuilder = docFactory.newDocumentBuilder();
			doc = docBuilder.parse(inStream);
			// plist
			Element rootEle = doc.getDocumentElement();
			// level 1
			NodeList arrays = XmlUtil.getNodeList(rootEle, "dict/array/dict");
			for (int i = 0; i < arrays.getLength(); i++) {
				Node dictElement = (Node) arrays.item(i);
				if (dictElement == null)
					continue;
				NodeList dictList = XmlUtil.getNodeList(dictElement, "string");
				levelItem1 = new MMMVideoItem();
				allItems.add(levelItem1);
				for (int j = 0; j < dictList.getLength(); j++) {
					Node rootItem = (Node) dictList.item(j);
					if (j == 0) {
						levelItem1.setTitle(rootItem.getTextContent());
					} else if (j == 1) {
						levelItem1.setImgName(rootItem.getTextContent());
					}

				}
				// level 2
				NodeList tokens = XmlUtil.getNodeList(dictElement, "array/dict");
				for (int k = 0; k < tokens.getLength(); k++) {
					Node tokenEl = (Node) tokens.item(k);
					if (tokenEl == null)
						continue;
					NodeList tokenList = XmlUtil.getNodeList(tokenEl, "string");
					levelItem2 = new MMMVideoItem();
					levelItem1.addVideoItem(levelItem2);
					for (int k1 = 0; k1 < tokenList.getLength(); k1++) {
						Node rootItem1 = (Node) tokenList.item(k1);
						if (k1 == 0) {
							levelItem2.setToken(rootItem1.getTextContent());
						} else if (k1 == 2) {
							levelItem2.setTitle(rootItem1.getTextContent());
						} else if (k1 == 3) {
							levelItem2.setTags(rootItem1.getTextContent());
						}
					}

					// level 3
					NodeList tagList = XmlUtil.getNodeList(tokenEl, "array/dict");
					for (int k11 = 0; k11 < tagList.getLength(); k11++) {
						Node tagEl = (Node) tagList.item(k11);
						if (tagEl == null)
							continue;
						NodeList tags = XmlUtil.getNodeList(tagEl, "string");
						levelItem3 = new MMMVideoItem();
						levelItem2.addVideoItem(levelItem3);
						for (int k3 = 0; k3 < tags.getLength(); k3++) {
							Node rootItem1 = (Node) tags.item(k3);
							if (k3 == 0) {
								levelItem3.setToken(rootItem1.getTextContent());
							} else if (k3 == 2) {
								levelItem3.setTitle(rootItem1.getTextContent());
							} else if (k3 == 3) {
								levelItem3.setTags(rootItem1.getTextContent());
							}
						}
						// level 4
						NodeList taglist4 = XmlUtil.getNodeList(tagEl, "array/dict");
						for (int k6 = 0; k6 < taglist4.getLength(); k6++) {
							Node tag4 = (Node) taglist4.item(k6);
							if (tag4 == null)
								continue;
							NodeList tags4 = XmlUtil.getNodeList(tag4, "string");
							levelItem4 = new MMMVideoItem();
							levelItem3.addVideoItem(levelItem4);
							for (int k3 = 0; k3 < tags4.getLength(); k3++) {
								Node rootItem1 = (Node) tags4.item(k3);
								if (k3 == 0) {
									levelItem4.setToken(rootItem1.getTextContent());
								} else if (k3 == 2) {
									levelItem4.setTitle(rootItem1.getTextContent());
								} else if (k3 == 3) {
									levelItem4.setTags(rootItem1.getTextContent());
								}
							}

						}

						NodeList pdftags = XmlUtil.getNodeList(tagEl, "key");
						for (int k4 = 0; k4 < pdftags.getLength(); k4++) {
							Element pdfNode = (Element) pdftags.item(k4);
							if ("PDFs".equals(pdfNode.getTextContent())) {
								levelItem3 = new MMMVideoItem();
								levelItem3.setTitle("PDFs");
								levelItem2.addVideoItem(levelItem3);
								Node pdfArrayNode = pdfNode.getNextSibling();
								pdfArrayNode = pdfArrayNode.getNextSibling();
								NodeList pdfList = XmlUtil.getNodeList(pdfArrayNode, "dict");
								for (int k5 = 0; k5 < pdfList.getLength(); k5++) {
									Node pNode1 = pdfList.item(k5);
									NodeList pdfList2 = XmlUtil.getNodeList(pNode1, "string");
									levelItem4 = new MMMVideoItem();
									levelItem3.addVideoItem(levelItem4);
									for (int k3 = 0; k3 < pdfList2.getLength(); k3++) {
										Node rootItem1 = (Node) pdfList2.item(k3);
										if (k3 == 0) {
											levelItem4.setTitle(rootItem1.getTextContent());
										} else if (k3 == 1) {
											levelItem4.setDesc(rootItem1.getTextContent());
										} else if (k3 == 2) {
											levelItem4.setUrl(rootItem1.getTextContent());
										}
									}

								}
							}
						}
					}
				}

			}

		} catch (ParserConfigurationException e1) {
			Log.e(TAG, e1.getMessage());
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		} catch (SAXException e) {
			Log.e(TAG, e.getMessage());
		}
		return allItems;
	}
}
