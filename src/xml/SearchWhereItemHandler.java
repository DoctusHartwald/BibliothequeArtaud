package xml;

import java.util.ArrayList;
import java.util.List;

import mapping.Library;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class SearchWhereItemHandler  extends DefaultHandler{

	private final static String  RCR ="RCR";
	private final static String LIBRARY="LIBRARY";
	private final static String SHORTNAME = "SHORTNAME";
	private final static String QUERY ="QUERY";

	boolean bfRcr = false ; 
	boolean bfLibrary = false ;
	boolean  bfShortName = false ;

	private Library library ; 
	private List<Library> mLibraries ; 
	public SearchWhereItemHandler() {

	}
	public void startElement(String uri, String localName,String qName,
			Attributes attributes) throws SAXException {

		if(qName.equalsIgnoreCase(QUERY)){
			mLibraries = new ArrayList<Library>();
		}
		if(qName.equalsIgnoreCase(LIBRARY)) {
			library = new Library();
			bfLibrary = true ; 
		}

		if(qName.equalsIgnoreCase(RCR)){			
			bfRcr = true ; 
		}
		if(qName.equalsIgnoreCase(SHORTNAME)){
			bfShortName = true ; 
		}

	}
	public void endElement(String uri, String localName,
			String qName) throws SAXException {
		if(qName.equalsIgnoreCase(LIBRARY)){
			mLibraries.add(library);
		}
		if(qName.equalsIgnoreCase(QUERY)){
			setmLibraries(mLibraries);
		}
	}
	public void characters(char ch[], int start, int length) throws SAXException {
		if(bfRcr){
			library.setRcr(new String(ch, start, length));
			bfRcr = false ; 
		}
		if(bfShortName){
			library.setShortname(new String(ch,start,length));
			bfShortName =  false ; 
		}
	}
	public List<Library> getmLibraries() {
		return mLibraries;
	}
	public void setmLibraries(List<Library> mLibraries) {
		this.mLibraries = mLibraries;
	}


}
