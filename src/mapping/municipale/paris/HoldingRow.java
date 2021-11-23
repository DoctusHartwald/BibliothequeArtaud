package mapping.municipale.paris;

public class HoldingRow {
	String section;
	String souslocation; 
	String site ; 
	String institution; 
	String codeInstitution;
	String codeSection;
	String support ; 
	String note ; 
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	public String getSouslocation() {
		return souslocation;
	}
	public void setSouslocation(String souslocation) {
		this.souslocation = souslocation;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public String getInstitution() {
		return institution;
	}
	public void setInstitution(String institution) {
		this.institution = institution;
	}
	public String getCodeInstitution() {
		return codeInstitution;
	}
	public void setCodeInstitution(String codeInstitution) {
		this.codeInstitution = codeInstitution;
	}
	public String getCodeSection() {
		return codeSection;
	}
	public void setCodeSection(String codeSection) {
		this.codeSection = codeSection;
	}
	public String getSupport() {
		return support;
	}
	public void setSupport(String support) {
		this.support = support;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getCote() {
		return cote;
	}
	public void setCote(String cote) {
		this.cote = cote;
	}
	public String getStatut() {
		return statut;
	}
	public void setStatut(String statut) {
		this.statut = statut;
	}
	public String getCodeStatut() {
		return codeStatut;
	}
	public void setCodeStatut(String codeStatut) {
		this.codeStatut = codeStatut;
	}
	public String getCodeBarre() {
		return codeBarre;
	}
	public void setCodeBarre(String codeBarre) {
		this.codeBarre = codeBarre;
	}
	public String getServicePoint() {
		return servicePoint;
	}
	public void setServicePoint(String servicePoint) {
		this.servicePoint = servicePoint;
	}
	public String getReservation() {
		return reservation;
	}
	public void setReservation(String reservation) {
		this.reservation = reservation;
	}
	String cote ; 
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("HoldingRow [section=");
		builder.append(section);
		builder.append(", souslocation=");
		builder.append(souslocation);
		builder.append(", site=");
		builder.append(site);
		builder.append(", institution=");
		builder.append(institution);
		builder.append(", codeInstitution=");
		builder.append(codeInstitution);
		builder.append(", codeSection=");
		builder.append(codeSection);
		builder.append(", support=");
		builder.append(support);
		builder.append(", note=");
		builder.append(note);
		builder.append(", cote=");
		builder.append(cote);
		builder.append(", statut=");
		builder.append(statut);
		builder.append(", codeStatut=");
		builder.append(codeStatut);
		builder.append(", codeBarre=");
		builder.append(codeBarre);
		builder.append(", servicePoint=");
		builder.append(servicePoint);
		builder.append(", reservation=");
		builder.append(reservation);
		builder.append("]");
		return builder.toString();
	}
	String statut ;
	String codeStatut ; 
	String codeBarre ;
	String servicePoint ; 
	String reservation ; 
	
}
