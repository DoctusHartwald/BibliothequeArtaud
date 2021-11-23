package org.biblio.xml;

import java.util.ArrayList;
import java.util.List;

import mapping.municipale.paris.EntryRecord;
import mapping.municipale.paris.Holding;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SearchMunicipaleHandler extends DefaultHandler {

	// TAG
	private static String ENTRY = "ENTRY";
	private static String TITLE = "TITLE";
	private static String RAWVALUE = "RAWVALUE";
	private static String ORDERNUMBER = "ORDER_NUMBER";
	private static String VALUE = "VALUE";
	private static String ZONE = "ZONE";
	private static String SUZONE = "SUBZONE";

	// TAG for HOLDING Bibli
	private static String HOLDINGS = "HOLDINGS";
	private static String HOLDINGITEM = "ITEM";
	private static String HOLDING = "HOLDING";
	private static String HOLDINGCODE = "CODE";

	boolean bfEntry = false;
	boolean bfTitle = false;
	boolean bfRawValue = false;
	boolean bfOrderNumber = false;
	boolean bfValue = false;
	boolean bfZone = false;
	boolean bfSubZone = false;

	// holding
	boolean bfHoldings = false;
	boolean bfHolding = false;
	boolean bfHoldingValue = false;
	boolean bfHoldingCode = false;

	List<EntryRecord> records;
	List<Holding> holdings;
	Holding holding;

	EntryRecord record;
	int positionIndex = 0;

	public SearchMunicipaleHandler() {
		records = new ArrayList<EntryRecord>();
	}

	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if (qName.equalsIgnoreCase(ENTRY)) {
			record = new EntryRecord();
			bfEntry = true;
		}

		// holding part
		if (qName.equalsIgnoreCase(HOLDINGS)) {
			holdings = new ArrayList<Holding>();
			bfHoldings = true;
		}
		if (qName.equalsIgnoreCase(HOLDINGITEM)) {
			holding = new Holding();
			bfHolding = true;
		}

		// / generic part
		if (qName.equalsIgnoreCase(TITLE)) {
			bfTitle = true;
		}
		if (qName.equalsIgnoreCase(RAWVALUE)) {
			bfRawValue = true;
		}
		if (qName.equalsIgnoreCase(ORDERNUMBER)) {
			bfRawValue = true;
		}
		if (qName.equalsIgnoreCase(VALUE)) {
			bfValue = true;
			bfHoldingValue = true;
		}
		if (qName.equalsIgnoreCase(HOLDINGCODE)) {
			bfHoldingCode = true;
		}

		if (qName.equalsIgnoreCase(ZONE)) {
			bfZone = true;
		}
		if (qName.equalsIgnoreCase(SUZONE)) {
			bfSubZone = true;
		}

	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (qName.equalsIgnoreCase(ENTRY)) {
			record.setPositionIndex(positionIndex);
			records.add(record);
			bfEntry = false;
			positionIndex++;
		}
		if (qName.equalsIgnoreCase(HOLDINGITEM)) {
			if (bfHoldings) {
				holdings.add(holding);
				bfHolding = false;
			}
		}

	}

	public void characters(char ch[], int start, int length)
			throws SAXException {
		String resultParse = new String(ch, start, length);
		resultParse.replace("&", " et ");
		if (bfTitle) {
			record.setTitle(resultParse);
			bfTitle = false;
		}
		if (bfRawValue) {
			record.setRawValue(resultParse);
			bfRawValue = false;
		}
		if (bfOrderNumber) {
			record.setOrderNumber(resultParse);
			bfOrderNumber = false;
		}
		if (bfValue) {
			record.setValueRecord(resultParse);
			bfValue = false;
		}
		if (bfHolding) {
			if (bfHoldingValue) {
				holding.setValue(resultParse);
				bfHoldingValue = false;
			}
			if (bfHoldingCode) {
				holding.setCode(resultParse);
				bfHoldingCode = false;
			}
		}

		if (bfZone) {
			record.setZone(resultParse);
			bfZone = false;
		}
		if (bfSubZone) {
			record.setSubZone(resultParse);
			bfSubZone = false;
		}
	}

	public List<EntryRecord> getRecords() {
		return records;
	}

	public void setRecords(List<EntryRecord> records) {
		this.records = records;
	}

	public List<Holding> getHoldings() {
		return holdings;
	}

	public void setHoldings(List<Holding> holdings) {
		this.holdings = holdings;
	}
}
