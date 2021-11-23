package mapping.municipale.paris;

public class Holding {
	private String code ; 
	private String value ;
	
	public Holding(){}

	Holding(String code,String value ){
		this.code=code; 
		this.value = value;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Holding [code=");
		builder.append(code);
		builder.append(", value=");
		builder.append(value);
		builder.append("]");
		return builder.toString();
	}
}
