package org.biblio.mapping.sudoc.recorddetail;

public class RecordDetailLabel {
	private String text ;

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RecordDetailLabel [text=");
		builder.append(text);
		builder.append("]");
		return builder.toString();
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}
}
