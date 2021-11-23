package org.biblio.mapping.sudoc.recorddetail;

import java.util.List;

public class RecordDetailData {
	List<String> line;

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		//builder.append("RecordDetailData [line=");
		//builder.append(line);
		builder.append("RecordDetailData [");
		builder.append("text=");
		builder.append(text);
		builder.append("]");
		return builder.toString();
	}
	String text ;
	/**
	 * @return the line
	 */
	public List<String> getLine() {
		return line;
	}
	/**
	 * @param line the line to set
	 */
	public void setLine(List<String> line) {
		this.line = line;
	}
	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}
	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}
}

