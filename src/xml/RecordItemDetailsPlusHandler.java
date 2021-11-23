package xml;

import java.util.ArrayList;
import java.util.List;

import mapping.recorddetail.RecordDetailPlus;
import mapping.recorddetail.RecordDetailData;
import mapping.recorddetail.RecordDetailLabel;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;




public class RecordItemDetailsPlusHandler extends DefaultHandler{

	private final static String TABLE="psi:labelledTable";
	private final static String RECORD ="psi:labelledRow";

	private final static String LABEL="psi:labelledLabel";
	private final static String DATA= "psi:labelledData";

	private final static String LINE="psi:line";
	private final static String TEXT="psi:text";

	private boolean bfRecord  = false ;
	private boolean bfLabel = false ;
	private boolean bfText = false ;
	private boolean bfData = false ;
	private boolean bfLine = false ; 

	RecordDetailPlus recorddetail ;
	RecordDetailLabel recordDetailLabel ;
	RecordDetailData recordDetailData ;
	List<String> text;
	List<RecordDetailPlus> recorddetails ;

	public RecordItemDetailsPlusHandler(){
		recorddetails = new ArrayList<RecordDetailPlus>();
	}
	public void startElement(String uri, String localName,String qName,
			Attributes attributes) throws SAXException {
		if(qName.equalsIgnoreCase(RECORD)){
			recorddetail = new RecordDetailPlus();
			bfRecord = true ;
		}
		if(qName.equalsIgnoreCase(LABEL)){
			recordDetailLabel = new RecordDetailLabel();
			bfLabel=true ;
		}
		if(qName.equalsIgnoreCase(DATA)){
			text = new ArrayList<String>();
			recordDetailData = new RecordDetailData();
			bfData = true ;
		}
		if(qName.equalsIgnoreCase(LINE)){
			bfLine = true;
		}

		if(qName.equalsIgnoreCase(TEXT)){
			bfText = true ;
		}

	}
	public void endElement(String uri, String localName,
			String qName) throws SAXException {

		if(qName.equalsIgnoreCase(LABEL)){
			if(recordDetailLabel!=null && bfLabel){
				if(recorddetail.getText()!=null){
					if(!"".equals(recorddetail.getText().trim()))
					recordDetailLabel.setText(recorddetail.getText());
				}
				bfLabel = false;
			}
		}
		if(qName.equalsIgnoreCase(TEXT)&& bfData){
			if(recorddetail.getText()!=null ){
				text.add(recorddetail.getText());
			}
		}
		if(qName.equalsIgnoreCase(DATA) && bfData){
			StringBuilder sb = new StringBuilder();
			for (String element : text) {
				sb.append(element.trim());
			}
			recordDetailData.setLine(text);
			recordDetailData.setText(sb.toString());
			bfData = false ;
			bfText = false;
		}
		if(qName.equalsIgnoreCase(RECORD)){
			int flagLabel = 0 ;
			int flagRecord = 0 ;
			if(null != recordDetailLabel.getText()&&!"".equals(recordDetailLabel.getText().trim())  && !"null".equals(recordDetailLabel.getText().trim())){
				recorddetail.setLabel(recordDetailLabel);
				flagLabel =1;
			}
			if(null != recordDetailData.getText() &&!"".equals(recordDetailData.getText().trim())  && !"null".equals(recordDetailData.getText().trim())){
				recorddetail.setData(recordDetailData);
				flagRecord = 1 ;
			}
			if(flagRecord!=0 && flagLabel!=0)
				recorddetails.add(recorddetail);

			bfRecord = false;
		}
		if(qName.equalsIgnoreCase(TABLE)){
			setRecorddetails(recorddetails);
		}
	}
	public void characters(char ch[], int start, int length) throws SAXException {
		String resu = new String(ch, start, length);
		if(bfText && !"".equals(resu.trim()) && null!=resu){
			recorddetail.setText(resu);
			bfText = false ;
		}
	}
	/**
	 * @return the recorddetails
	 */
	public List<RecordDetailPlus> getRecorddetails() {
		return recorddetails;
	}
	/**
	 * @param recorddetails the recorddetails to set
	 */
	public void setRecorddetails(List<RecordDetailPlus> recorddetails) {
		this.recorddetails = recorddetails;
	}
}
