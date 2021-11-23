package mapping;
public class Document {
    private String mPpn;
    private String citation ;
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
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("\n\tDocument [mPpn=");
        builder.append(mPpn);
        builder.append(", citation=");
        builder.append(citation);
        builder.append("]");
        return builder.toString();
    } 
 
}
