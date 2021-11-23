package org.biblio.mapping.sudoc.record;

public class Document {
	private String mPpn;
	private String citation;
	private String mRoleName;
	private String mAuteur;
	private boolean selected;
	private String countRoles;

	public String getmPpn() {
		return mPpn;
	}

	public void setmPpn(String mPpn) {
		this.mPpn = mPpn;
	}

	public String getCitation() {
		return citation;
	}

	public void setCitation(String citation) {
		this.citation = citation;
	}

	public String getmRoleName() {
		return mRoleName;
	}

	public void setmRoleName(String mRoleName) {
		this.mRoleName = mRoleName;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String getmAuteur() {
		return mAuteur;
	}

	public void setmAuteur(String mAuteur) {
		this.mAuteur = mAuteur;
	}

	public String getCountRoles() {
		return countRoles;
	}

	public void setCountRoles(String countRoles) {
		this.countRoles = countRoles;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Document [citation=");
		builder.append(citation);
		builder.append(", mAuteur=");
		builder.append(mAuteur);
		builder.append(", mPpn=");
		builder.append(mPpn);
		builder.append(", mRoleName=");
		builder.append(mRoleName);
		builder.append(", selected=");
		builder.append(selected);
		builder.append("]");
		return builder.toString();
	}

}
