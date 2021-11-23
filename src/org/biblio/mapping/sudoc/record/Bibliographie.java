package org.biblio.mapping.sudoc.record;

import java.util.List;


public class Bibliographie {
    private String mName;
    private String mCountRoles;
    private List<Role> mRole;

    public String getmName() {
        return mName;
    }
    public void setmName(String mName) {
        this.mName = mName;
    }
    public String getmCountRoles() {
        return mCountRoles;
    }
    public void setmCountRoles(String mCountRoles) {
        this.mCountRoles = mCountRoles;
    }
    public List<Role> getmRole() {
        return mRole;
    }
    public void setmRole(List<Role> mRole) {
        this.mRole = mRole;
    }
   
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Bibliographie [mCountRoles=");
		builder.append(mCountRoles);
		builder.append(", mName=");
		builder.append(mName);
		builder.append(", mRole=");
		builder.append(mRole);
	
		builder.append("]");
		return builder.toString();
	}
}
 