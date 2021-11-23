package org.biblio.mapping.sudoc.record;

import java.util.HashMap;
import java.util.Map;
 
/**
 * <p>
 * Build a the cache for recordType return by the rest webservice.
 * </p>
 * @author Artaud Antoine
 *
 */
public class RecordTypeMapping {
    Map<String,String> mRecordTypeCache;
    public RecordTypeMapping() {
        mRecordTypeCache = new HashMap<String, String>();
    }
    public HashMap<String,String> contructCache(){
        mRecordTypeCache.put("a", "Personne");
        mRecordTypeCache.put("b", "Collectivit�");
        mRecordTypeCache.put("c", "Nom g�ographique");
        mRecordTypeCache.put("d", "Marque");
        mRecordTypeCache.put("e", "Famille");
        mRecordTypeCache.put("f", "Titre uniforme");
        mRecordTypeCache.put("h", "Auteur Titre");
        mRecordTypeCache.put("l", "FmesH");
        mRecordTypeCache.put("r", "Rameau");
        mRecordTypeCache.put("w", "RCR = Biblioth�que Sudoc");
        return (HashMap<String, String>) mRecordTypeCache;
    }
}