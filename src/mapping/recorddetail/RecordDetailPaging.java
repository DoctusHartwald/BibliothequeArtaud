package mapping.recorddetail;


public class RecordDetailPaging {
    private String nextPage ;
    private String previousPage;
 
    /**
     * @return the previousPage
     */
    public String getPreviousPage() {
        return previousPage;
    }
    /**
     * @param previousPage the previousPage to set
     */
    public void setPreviousPage(String previousPage) {
        this.previousPage = previousPage;
    }
 
    /**
     * @return the nextPage
     */
    public String getNextPage() {
        return nextPage;
    }
    /**
     * @param nextPage the nextPage to set
     */
    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("RecordDetailPaging [nextPage=");
        builder.append(nextPage);
        builder.append(", previousPage=");
        builder.append(previousPage);
        builder.append("]");
        return builder.toString();
    }
}