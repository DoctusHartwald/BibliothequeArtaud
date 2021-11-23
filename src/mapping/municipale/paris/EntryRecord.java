package mapping.municipale.paris;

/**
 * <ENTRY POSITION="1">
	<TITLE>Titre</TITLE>
	<USERDATA/>
	<PREMIERINDICATEUR/>
	<SOUSZONESECONDAIRE/>
	<RAWVALUE>Le Père Goriot</RAWVALUE>
	<ORDER_NUMBER>11</ORDER_NUMBER>
	<VALUE>Le Père Goriot</VALUE>
	<ZONE>200</ZONE>
	<SUBZONE>a</SUBZONE>
</ENTRY>
 * @author Administrateur
 *
 */
public class EntryRecord {

	private int  positionIndex;
	private  String Title ; 
	private  String rawValue ; 
	private String orderNumber ; 
	private String valueRecord ; 
	private String zone ; 
	private String subZone;
	
	public int getPositionIndex() {
		return positionIndex;
	}
	public void setPositionIndex(int positionIndex) {
		this.positionIndex = positionIndex;
	}
	
	
	
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getRawValue() {
		return rawValue;
	}
	public void setRawValue(String rawValue) {
		this.rawValue = rawValue;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getValueRecord() {
		return valueRecord;
	}
	public void setValueRecord(String valueRecord) {
		this.valueRecord = valueRecord;
	}
	public String getZone() {
		return zone;
	}
	public void setZone(String zone) {
		this.zone = zone;
	}
	public String getSubZone() {
		return subZone;
	}
	public void setSubZone(String subZone) {
		this.subZone = subZone;
	}
	@Override
	public String toString() {
		return "EntryRecord [positionIndex=" + positionIndex + ", Title="
				+ Title + ", rawValue=" + rawValue + ", orderNumber="
				+ orderNumber + ", valueRecord=" + valueRecord + ", zone="
				+ zone + ", subZone=" + subZone + "]";
	}
	
}
