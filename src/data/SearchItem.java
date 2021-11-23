package data;


public class SearchItem {
    public String id ;
    public String ppn;
    public String  auteur;
    public String recordType;
 
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
 
}