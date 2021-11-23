package org.biblio.mapping.sudoc.record;

public class Library {
	String rcr ; 
	String shortname ;
	public String getRcr() {
		return rcr;
	}
	public void setRcr(String rcr) {
		this.rcr = rcr;
	}
	public String getShortname() {
		return shortname;
	}
	public void setShortname(String shortname) {
		this.shortname = shortname;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Library [rcr=");
		builder.append(rcr);
		builder.append(", shortname=");
		builder.append(shortname);
		builder.append("]");
		return builder.toString();
	}
}
