package org.biblio.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mapping.recorddetail.RecordDetailPaging;

import org.biblio.record.Record;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SearchRecordItemHandler extends DefaultHandler {
	private final static String RECORDS = "PSI:RECORDS";
	private final static String RECORD = "PSI:RECORD";
	private final static String LINE = "PSI:LINE";
	private final static String TEXT = "PSI:TEXT";
	private final static String PAGEURL = "psi:url";
	private final static String SESSIONVAR = "psi:sessionVar";
	private final static String CONTEXTVAR = "psi:contextVar";
	private final static String HITS="psi:queryvar";
	
	boolean bfRecord = false;
	boolean bfLine = false;
	boolean bfText = false;
	boolean bfPpn = false;
	boolean bfSupport = false;
	private boolean bfNext = false;
	private boolean bfPrevious = false;
	private boolean bfCookie = false;
	private boolean bfPpnHeader = false ;
	private boolean bfHits = false;

	List<Record> records;
	Record record;
	List<String> lines;
	String name;
	String value;
	RecordDetailPaging paging;
	String cookie; String ppnHeaderContext; //contextVar
	int compteurResults = 0 ;
	String hitsResult ;
	
	Map<String,String> supportType;

	public void cacheContruct(){
		supportType = new HashMap<String,String>();
		supportType.put("MAT_O", "Electronique");
		supportType.put("MAT_B", "Livre");
		supportType.put("MAT_Y", "These");
		supportType.put("MAT_A", "Article");
		supportType.put("MAT_T","Periodique");
		supportType.put("MAT_M","Partitions");
	}
	public SearchRecordItemHandler() {
		cacheContruct();
		records = new ArrayList<Record>();
		paging = new RecordDetailPaging();
	}

	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if (qName.equalsIgnoreCase(PAGEURL)) {
			for (int i = 0; i < attributes.getLength(); i++) {
				if ("NEXT".equalsIgnoreCase(attributes.getValue(i))) {
					bfNext = true;
				}
				if ("PREV".equalsIgnoreCase(attributes.getValue(i))) {
					bfPrevious = true;
				}
			}
		}
		if (qName.equalsIgnoreCase(SESSIONVAR)) {
			for (int i = 0; i < attributes.getLength(); i++) {
				if ("COOKIE".equalsIgnoreCase(attributes.getValue(i))) {
					bfCookie = true;
				}
			}
		}
		
		if (qName.equalsIgnoreCase(CONTEXTVAR)) {
			for (int i = 0; i < attributes.getLength(); i++) {
				if ("PPN".equalsIgnoreCase(attributes.getValue(i))) {
					bfPpnHeader = true ; 
				}
			}
		}

		if (qName.equalsIgnoreCase(RECORD)) {
			record = new Record();
			lines = new ArrayList<String>();
			bfRecord = true;
			compteurResults++;
		}
		if (qName.equalsIgnoreCase(LINE)) {
			bfLine = true;
		}

		if (qName.equalsIgnoreCase(TEXT)) {
			bfText = true;
		}
		
		if (qName.equalsIgnoreCase(HITS)) {
			for (int i = 0; i < attributes.getLength(); i++) {
				if ("HITS".equalsIgnoreCase(attributes.getValue(i))) {
					bfHits = true;
				}
			}
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
			if (name.equals("mattype")) {
				String typeSupportMedia= cacheRetrieveValue(value);
				if(null != typeSupportMedia){
					record.setTypeSupport(typeSupportMedia);
				}
				else{
					record.setTypeSupport("Livre"); //si pas dans le cache a default cest un livre
				}
				bfSupport = true;
			}
		}
	}
	
	public String cacheRetrieveValue(String key){
		return supportType.get(key);
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
		String resu = new String(ch, start, length);

		if(bfPpnHeader){
			setPpnHeaderContext(resu);
			bfPpnHeader = false ;
		}
		if (bfCookie) {
			setCookie(resu);
			bfCookie = false;
		}
		if (bfText) {
			record.setLine(new String(ch, start, length));
			bfText = false;
		}
		if (bfNext) {
			paging.setNextPage(resu);
			bfNext = false;
		}
		if (bfPrevious) {
			paging.setPreviousPage(resu);
			bfPrevious = false;
		}
		if(bfHits){
			setHitsResult(resu);
			bfHits=false;
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

	public RecordDetailPaging getPaging() {
		return paging;
	}

	public void setPaging(RecordDetailPaging paging) {
		this.paging = paging;
	}

	public String getCookie() {
		return cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

	public int getCompteurResults() {
		return compteurResults;
	}

	public void setCompteurResults(int compteurResults) {
		this.compteurResults = compteurResults;
	}

	public String getHitsResult() {
		return hitsResult;
	}

	public void setHitsResult(String hitsResult) {
		this.hitsResult = hitsResult;
	}
	public String getPpnHeaderContext() {
		return ppnHeaderContext;
	}
	public void setPpnHeaderContext(String ppnHeaderContext) {
		this.ppnHeaderContext = ppnHeaderContext;
	}
}
