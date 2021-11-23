package mapping.recorddetail;

public class RecordDetailPlus {
	private RecordDetailLabel label ;
	private RecordDetailData data;
	private String text;
	/**
	 * @return the label
	 */
	public RecordDetailLabel getLabel() {
		return label;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RecordDetail [label=");
		builder.append(label);
		builder.append(", data=");
		builder.append(data);

		return builder.toString();
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
	/**
	 * @param label the label to set
	 */
	public void setLabel(RecordDetailLabel label) {
		this.label = label;
	}
	/**
	 * @return the data
	 */
	public RecordDetailData getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(RecordDetailData data) {
		this.data = data;
	}

}
