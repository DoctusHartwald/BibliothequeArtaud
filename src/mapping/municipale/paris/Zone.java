package mapping.municipale.paris;

public class Zone {
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Zone [number=");
		builder.append(number);
		builder.append(", indicator=");
		builder.append(indicator);
		builder.append("]");
		return builder.toString();
	}

	private String number;
	private String indicator;

	public Zone(String number ){
		this.number = number;
	}
	public Zone(String number,String indicator){
		this.number = number; 
		this.indicator = indicator;
	}
	public String getIndicator() {
		return indicator;
	}

	public void setIndicator(String indicator) {
		this.indicator = indicator;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	} 
	
}
