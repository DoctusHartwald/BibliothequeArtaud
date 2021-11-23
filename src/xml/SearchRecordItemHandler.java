package xml;

import java.util.ArrayList;
import java.util.List;

import org.biblio.record.Record;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SearchRecordItemHandler extends DefaultHandler {
	private final static String RECORDS = "PSI:RECORDS";
	private final static String RECORD = "PSI:RECORD";
	private final static String LINE = "PSI:LINE";
	private final static String TEXT = "PSI:TEXT";

	boolean bfRecord = false;
	boolean bfLine = false;
	boolean bfText = false;
	boolean bfPpn = false;

	List<Record> records;
	Record record;
	List<String> lines;
	String name;
	String value;

	public SearchRecordItemHandler() {
		records = new ArrayList<Record>();
	}

	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		if (qName.equalsIgnoreCase(RECORD)) {
			record = new Record();
			lines = new ArrayList<String>();
			bfRecord = true;
		}
		if (qName.equalsIgnoreCase(LINE)) {
			bfLine = true;
		}

		if (qName.equalsIgnoreCase(TEXT)) {
			bfText = true;
		}
		int length = attributes.getLength();

		for (int i = 0; i < length; i++) {
			// Get names and values to each attribute
			name = attributes.getQName(i);
			value = attributes.getValue(i);
			if (name.equals("ppn")) {
				record.setPpn(value);
				bfPpn = true;
			}
		}
	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (qName.equalsIgnoreCase(LINE)) {
			lines.add(record.getLine());
		}
		if (qName.equalsIgnoreCase(RECORD)) {
			record.setLines(lines);
			records.add(record);
		}
		if (qName.equalsIgnoreCase(RECORDS)) {
			setRecords(records);
		}
	}

	public void characters(char ch[], int start, int length)
			throws SAXException {
		if (bfText) {
			record.setLine(new String(ch, start, length));
			bfText = false;
		}
		/*
		 * if(bfPpn){ System.out.println("==>"+new String(ch,start,length)); if
		 * (record!=null) record.setPpn(new String(ch,start,length)); bfPpn
		 * =false; }
		 */
	}

	public List<Record> getRecords() {
		return records;
	}

	public void setRecords(List<Record> records) {
		this.records = records;
	}
}
