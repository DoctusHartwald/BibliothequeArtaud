package mapping;

import java.util.List;

public class Role {
    private String mRoleName;
    private String mCount;
    private List<Document> mDocs;
    public String getmRoleName() {
        return mRoleName;
    }
    public void setmRoleName(String mRoleName) {
        this.mRoleName = mRoleName;
    }
    public String getmCount() {
        return mCount;
    }
    public void setmCount(String mCount) {
        this.mCount = mCount;
    }
    public List<Document> getmDocs() {
        return mDocs;
    }
    public void setmDocs(List<Document> mDocs) {
        this.mDocs = mDocs;
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Role [mRoleName=");
        builder.append(mRoleName);
        builder.append(", mCount=");
        builder.append(mCount);
        builder.append(", \n\tmDocs=");
        builder.append(mDocs);
        builder.append("]");
        return builder.toString();
    }
}