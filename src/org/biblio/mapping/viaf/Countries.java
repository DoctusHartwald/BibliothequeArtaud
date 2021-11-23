package org.biblio.mapping.viaf;


import java.util.List;

/**
 * countries - countries where things published associated with this authority
 * @author artaud
 *
 */
public class Countries {
	private String text;
	private List<String> source;
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("\n Countries [source=");
		builder.append(source);
		builder.append(", text=");
		builder.append(text);
		builder.append("]");
		return builder.toString();
	}
	public List<String> getSource() {
		return source;
	}
	public void setSource(List<String> source) {
		//this.source.addAll(source);
		this.source = source;
	}
}