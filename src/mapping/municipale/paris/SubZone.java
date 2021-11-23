package mapping.municipale.paris;

public class SubZone {
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SubZone [number=");
		builder.append(number);
		builder.append(", value=");
		builder.append(value);
		builder.append("]");
		return builder.toString();
	}
	SubZone (String number,String value ){
		this.number = number;
		this.value = value ;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	String number ; 
	String value ; 
}
