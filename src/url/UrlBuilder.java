package url;

public class UrlBuilder {
	String url = "http://www.idref.fr/Sru/Solr?"
			+ "q=persname_t:boulnois%20olivier" + "&sort=score%20desc"
			+ "&version=2.2" + "&start=0" + "&rows=3000" + "&indent=on"
			+ "&fl=id,ppn_z,recordtype_z,affcourt_z";
	private static String SOLDR = "http://www.idref.fr/Sru/Solr?";

	public String contructUrl() {
		String url = null;
		String searchCriteria = urlSearchIndex();

		return SOLDR.concat("q=" + searchCriteria + "&sort=score%20desc"
				+ "&version=2.2" + "&start=0" + "&rows=3000" + "&indent=on"
				+ "&fl=id,ppn_z,recordtype_z,affcourt_z");
		// &d103_s
		// filterAlive(getFalive());

	}
	
	public String constructUrlSuggest(String param){
		return "http://www.idref.fr/Sru/Solr?q=%28all:"+param+"*%29&start=&rows=15&httpAccept=text/xml&fl=affcourt_z";
	}

	StringBuilder mUrl = null;
	// ** Liste Index Search
	// Index de type text : _t Apache Soldr
	private String mpersnameSearch; // Nom de personne
	private String mcorpnameSearch; // Nom collectivitŽ
	private String msubjectheadingSearch; // Sujet (MesH ou Rameau)
	private String mgeognameSearch; // Nom gŽographique
	private String mfamnameSearch; // Famille
	private String muniformtitleSearch; // Titre uniforme
	private String mNametitleSearch; // Auteur Titre
	private String mtrademarkSearch; // Nom de marque
	private String mppnSearch; // PPN (identifiant de notice Sudoc)
	private String mrcr; // Biblioth�ques Sudoc
	private String mall; // search all Index

	private String falive;

	public String urlSearchIndex() {
		if (null != getMpersnameSearch()) {
			return "persname_t:%22" + getMpersnameSearch() + "%22";
		}
		if (null != getMcorpnameSearch()) {
			return "corpname_t:%22" + getMcorpnameSearch() + "%22";
		}
		if (null != getMsubjectheadingSearch()) {
			return "subjectheading_t:%22" + getMsubjectheadingSearch() + "%22";
		}
		if (null != getMgeognameSearch()) {
			return "geogname_t:%22" + getMgeognameSearch() + "%22";
		}
		if (null != getMfamnameSearch()) {
			return "famname_t:%22" + getMfamnameSearch() + "%22";
		}
		if (null != getMuniformtitleSearch()) {
			return "uniformtitle_t:%22" + getMuniformtitleSearch() + "%22";
		}
		if (null != getmNametitleSearch()) {
			return "nametitle_t:%22" + getmNametitleSearch() + "%22";
		}
		if (null != getMtrademarkSearch()) {
			return "trademark_t:%22" + getMtrademarkSearch() + "%22";
		}
		if (null != getMppnSearch()) {
			return "ppn_z:" + getMppnSearch();
		}
		if (null != getMrcr()) {
			return "rcr_t:" + getMrcr();
		}
		if (null != getMall()) {
			return "all:%22" + getMall() + "%22";
		}
		return null;
	}

	// Filter search d103_s .
	// Exemple : &d103_s:mort or &d103_s:autre
	// http://www.idref.fr/Sru/Solr?q=persname_t:boulnois%20olivier&d103_s:mort&heading_z=ok&sort=score%20desc&version=2.2&start=0&rows=30&indent=on&fl=id,ppn_z,recordtype_z,affcourt_z
	public String filterAlive(int flag) {
		if (flag == 0) {
			return "mort";
		} else {
			return "autre";
		}
	}

	// Filter heading_z Utilisable en t�te de vedette

	// Sort result output

	public String getMpersnameSearch() {
		return mpersnameSearch;
	}

	public void setMpersnameSearch(String mpersnameSearch) {
		this.mpersnameSearch = mpersnameSearch;
	}

	public String getMcorpnameSearch() {
		return mcorpnameSearch;
	}

	public void setMcorpnameSearch(String mcorpnameSearch) {
		this.mcorpnameSearch = mcorpnameSearch;
	}

	public String getMsubjectheadingSearch() {
		return msubjectheadingSearch;
	}

	public void setMsubjectheadingSearch(String msubjectheadingSearch) {
		this.msubjectheadingSearch = msubjectheadingSearch;
	}

	public String getMgeognameSearch() {
		return mgeognameSearch;
	}

	public void setMgeognameSearch(String mgeognameSearch) {
		this.mgeognameSearch = mgeognameSearch;
	}

	public String getMfamnameSearch() {
		return mfamnameSearch;
	}

	public void setMfamnameSearch(String mfamnameSearch) {
		this.mfamnameSearch = mfamnameSearch;
	}

	public String getMuniformtitleSearch() {
		return muniformtitleSearch;
	}

	public void setMuniformtitleSearch(String muniformtitleSearch) {
		this.muniformtitleSearch = muniformtitleSearch;
	}

	public String getmNametitleSearch() {
		return mNametitleSearch;
	}

	public void setmNametitleSearch(String mNametitleSearch) {
		this.mNametitleSearch = mNametitleSearch;
	}

	public String getMtrademarkSearch() {
		return mtrademarkSearch;
	}

	public void setMtrademarkSearch(String mtrademarkSearch) {
		this.mtrademarkSearch = mtrademarkSearch;
	}

	public String getMppnSearch() {
		return mppnSearch;
	}

	public void setMppnSearch(String mppnSearch) {
		this.mppnSearch = mppnSearch;
	}

	public String getMrcr() {
		return mrcr;
	}

	public void setMrcr(String mrcr) {
		this.mrcr = mrcr;
	}

	public String getMall() {
		return mall;
	}

	public void setMall(String mall) {
		this.mall = mall;
	}

	public String getFalive() {
		return falive;
	}

	public void setFalive(String falive) {
		this.falive = falive;
	}
}
