package org.biblio.record;

import java.util.List;

public class Record {
	boolean selected ;
	public List<String> lines ;
	private String line;
	private String ppn;
	private String typeSupport;//type de  support
	
	public List<String> getLines() {
		return lines;
	}
	public void setLines(List<String> lines) {
		this.lines = lines;
	}
	public String getLine() {
		return line;
	}
	public void setLine(String line) {
		this.line = line;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Record [selected=");
		builder.append(selected);
		builder.append(", lines=");
		builder.append(lines);
		builder.append(", line=");
		builder.append(line);
		builder.append(", ppn=");
		builder.append(ppn);
		builder.append(", numberHits=");
		builder.append("]");
		return builder.toString();
	}
	public String getPpn() {
		return ppn;
	}
	public void setPpn(String ppn) {
		this.ppn = ppn;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public String getTypeSupport() {
		return typeSupport;
	}
	public void setTypeSupport(String typeSupport) {
		this.typeSupport = typeSupport;
	}
	
	
}
