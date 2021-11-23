package data;

import java.util.ArrayList;
import java.util.List;
 
public class SearchItems {
    public List<SearchItem> getSearchitems() {
        if(searchitems==null){
            return new ArrayList<SearchItem>();
        }
        return searchitems;
    }
 
    public void setSearchitems(List<SearchItem> searchitems) {
        this.searchitems = searchitems;
    }
 
    List<SearchItem> searchitems ;
}