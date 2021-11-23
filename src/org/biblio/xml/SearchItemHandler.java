package org.biblio.xml;

import java.util.List;
import java.util.Map;

import org.biblio.mapping.sudoc.record.RecordTypeMapping;
import org.biblio.mapping.sudoc.search.SearchItem;
import org.biblio.mapping.sudoc.search.SearchItems;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SearchItemHandler extends DefaultHandler {

	boolean debugMode = false;// debug Mode activation

	private static String DOC = "DOC";

	boolean bfStr = false;
	boolean bfId = false;
	boolean bfppn = false;
	boolean bfauteur = false;
	boolean bfrecordType = false;

	SearchItem mItem;
	List<SearchItem> vItems;
	RecordTypeMapping vRecordTypeMapping;
	Map<String, String> mRecordTypeCache;
	int compteur;

	public SearchItemHandler() {
		compteur = 0;
		setCompteur(compteur);

		SearchItems vSearchItems = new SearchItems();
		vItems = vSearchItems.getSearchitems();
		vRecordTypeMapping = new RecordTypeMapping();
		mRecordTypeCache = vRecordTypeMapping.contructCache();
	}

	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		// start new element item in the response
		if (qName.equalsIgnoreCase(DOC)) {
			mItem = new SearchItem();
		}

		int length = attributes.getLength();
		for (int i = 0; i < length; i++) {
			// Get names and values to each attribute
			String name = attributes.getQName(i);
			String value = attributes.getValue(i);

			log(name + " <=> " + value + "\n");

			if (value.equals("affcourt_z")) {
				bfauteur = true;
			}

			if (value.equals("id")) {
				bfId = true;
			}

			if (value.equals("ppn_z")) {
				bfppn = true;
			}

			if (value.equals("recordtype_z")) {
				bfrecordType = true;
			}
		}
	}

	/**
	 * <p>
	 * When the parsing of Doc element is finished we fill in our array .
	 * 
	 * @author artaud antoine
	 */
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		log("<== End Element :" + qName);
		if (qName.equalsIgnoreCase(DOC)) {
			vItems.add(mItem);
			compteur++;
			setCompteur(compteur);
		}
	}

	/**
	 * <p>
	 * extract the data receive from Rest Webservice response.
	 * </p>
	 * 
	 * @author artaud antoine
	 */
	public void characters(char ch[], int start, int length)
			throws SAXException {

		// System.out.println(new String(ch, start, length));
		if (bfauteur) {
			log("Auteur  : " + new String(ch, start, length));
			mItem.setAuteur(new String(ch, start, length));
			bfauteur = false;
		}

		if (bfId) {
			log("ID  : " + new String(ch, start, length));
			mItem.setId(new String(ch, start, length));
			bfId = false;
		}

		if (bfppn) {
			log("PPN  : " + new String(ch, start, length));
			mItem.setPpn(new String(ch, start, length));
			bfppn = false;
		}
		if (bfrecordType) {
			log("RecordType  : " + new String(ch, start, length));
			String recordType = mRecordTypeCache.get(new String(ch, start,
					length));
			if (recordType != null) {
				mItem.setRecordType(recordType);
			} else {
				mItem.setRecordType(new String(ch, start, length));
			}

			bfrecordType = false;
		}

	}

	/**
	 * log debug message function
	 * 
	 * @author artaud antoine
	 * @param message
	 */
	public void log(String message) {
		if (debugMode) {
			System.out.println(message);
		}
	}

	public List<SearchItem> getvItems() {
		return vItems;
	}

	public void setvItems(List<SearchItem> vItems) {
		this.vItems = vItems;
	}

	public int getCompteur() {
		return compteur;
	}

	public void setCompteur(int compteur) {
		this.compteur = compteur;
	}
}