package org.biblio.mapping.sudoc.search;

import android.os.Parcel;
import android.os.Parcelable;

public class SearchItem implements Parcelable {
	public String id;
	public String ppn;
	public String auteur;
	public String recordType;
	private boolean selected;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPpn() {
		return ppn;
	}

	public void setPpn(String ppn) {
		this.ppn = ppn;
	}

	public String getAuteur() {
		return auteur;
	}

	public void setAuteur(String auteur) {
		this.auteur = auteur;
	}

	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SearchItem [id=");
		builder.append(id);
		builder.append(", ppn=");
		builder.append(ppn);
		builder.append(", auteur=");
		builder.append(auteur);
		builder.append(", recordType=");
		builder.append(recordType);
		builder.append("]");
		return builder.toString();
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel arg0, int arg1) {
		// TODO Auto-generated method stub

	}

}