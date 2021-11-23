package xml;

import java.util.ArrayList;
import java.util.List;

import mapping.Bibliographie;
import mapping.Document;
import mapping.Role;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SearchBiblioItemHandler extends DefaultHandler {
	boolean debugMode = false;// debug Mode activation
	private static String RESULT = "RESULT";
	private static String NAME = "NAME";
	private static String COUNTROLES = "COUNTROLES";

	private static String ROLE = "ROLE";
	private static String ROLENAME = "ROLENAME";
	private static String COUNT = "COUNT";

	private static String DOC = "DOC";
	private static String PPN = "PPN";
	private static String CITATION = "CITATION";

	boolean bfResult = false;
	boolean bfName = false;
	boolean bfCountRoles = false;

	boolean bfRole = false;
	boolean bfRoleName = false;
	boolean bfCount = false;

	boolean bfDoc = false;
	boolean bfCitation = false;
	boolean bfPpn = false;

	Bibliographie mBibliographie;
	List<Role> roles;
	List<Document> documents;
	Role role;
	Document document = null;

	int i = 1;

	public SearchBiblioItemHandler() {

	}

	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// start new element item in the response
		if (qName.equalsIgnoreCase(RESULT)) {
			log("==> Result");
			mBibliographie = new Bibliographie();
			roles = new ArrayList<Role>();
			bfResult = true;
		}
		if (qName.equalsIgnoreCase(NAME)) {
			log("==> Name");
			bfName = true;
		}
		if (qName.equalsIgnoreCase(COUNTROLES)) {
			log("==> countRoles");
			bfCountRoles = true;
		}

		if (qName.equalsIgnoreCase(ROLE)) {
			role = new Role();
			documents = new ArrayList<Document>();
			log("====> Role");
			bfRole = true;
		}
		if (qName.equalsIgnoreCase(ROLENAME)) {
			log("===> RoleName");
			bfRoleName = true;
		}
		if (qName.equalsIgnoreCase(COUNT)) {
			bfCount = true;
		}

		if (qName.equalsIgnoreCase(DOC)) {
			document = new Document();
			log("======> Doc");
			bfDoc = true;
		}
		if (document != null && qName.equalsIgnoreCase(PPN)) {
			bfPpn = true;
		}
		if (document != null && qName.equalsIgnoreCase(CITATION)) {
			bfCitation = true;
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
		if (qName.equalsIgnoreCase(RESULT)) {
			mBibliographie.setmRole(roles);
		}
		if (qName.equalsIgnoreCase(ROLE)) {
			i++;
			role.setmDocs(documents);
			roles.add(role);

		}
		if (qName.equalsIgnoreCase(DOC)) {
			documents.add(document);
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

		// result Tag Part
		if (bfResult) {
			if (bfName) {
				mBibliographie.setmName(new String(ch, start, length));
				bfName = false;
			}
			if (bfCountRoles) {
				mBibliographie.setmCountRoles(new String(ch, start, length));
				bfCountRoles = false;
			}

			if (bfRoleName) {
				role.setmRoleName(new String(ch, start, length));
				bfRoleName = false;
			}

			if (bfCount) {
				role.setmCount(new String(ch, start, length));
				bfCount = false;
			}

			if (bfCitation) {
				document.setCitation(new String(ch, start, length));
				bfCitation = false;
			}

			if (bfPpn) {
				document.setmPpn(new String(ch, start, length));
				bfPpn = false;
			}

			/*
			 * List<Document> documents=new ArrayList<Document>(); if(bfDoc){
			 * Document vDocument = new Document(); vDocument.setmPpn(new
			 * String(ch, start, length)); vDocument.setCitation(new String(ch,
			 * start, length)); documents.add(vDocument); bfDoc=false; }
			 * vRole.setmDocs(documents);
			 */

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

	public Bibliographie getmBibliographie() {
		return mBibliographie;
	}

	public void setmBibliographie(Bibliographie mBibliographie) {
		this.mBibliographie = mBibliographie;
	}

}