package org.biblio.url;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

import android.util.Log;

public class UrlBuilder {
	String url = "http://www.idref.fr/Sru/Solr?"
			+ "q=persname_t:boulnois%20olivier" + "&sort=score%20desc"
			+ "&version=2.2" + "&start=0" + "&rows=3000" + "&indent=on"
			+ "&fl=id,ppn_z,recordtype_z,affcourt_z";
	private static String SOLDR = "http://www.idref.fr/Sru/Solr?";

	public String contructUrl() throws UnsupportedEncodingException {
		String url = null;
		String searchCriteria = urlSearchIndex();

		return SOLDR.concat("q=" + searchCriteria + "&sort=score%20desc"
				+ "&version=2.2" + "&start=0" + "&rows=3000" + "&indent=on"
				+ "&fl=id,ppn_z,recordtype_z,affcourt_z");
		// &d103_s
		// filterAlive(getFalive());

	}

	public String contructUrl2(String orderBy, String numberResultats)
			throws UnsupportedEncodingException, URISyntaxException {
		URI base = new URI("http", "www.idref.fr", "/Sru/Solr", "q="
				+ urlSearchIndex() + "&sort=" + orderBy + "&version=2.2"
				+ "&start=0" + "&rows=" + numberResultats + "&indent=on"
				+ "&fl=id,ppn_z,recordtype_z,affcourt_z", null);
		Log.i(getClass().getSimpleName(), "URL " + base.toString());
		return base.toString();

	}

	public String constructUrlSuggest(String param) {
		return "http://www.idref.fr/Sru/Solr?q=%28all:" + param
				+ "*%29&start=&rows=15&httpAccept=text/xml&fl=affcourt_z";
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

	public String urlSearchIndex() throws UnsupportedEncodingException {
		if (null != getMpersnameSearch()) {
			// return "persname_t:%22" + getMpersnameSearch() + "%22";
			// return "persname_t:"
			// + URLEncoder.encode(getMpersnameSearch().trim() + "",
			// "ISO-8859-1");
			return "persname_t:" + getMpersnameSearch();
		}
		if (null != getMcorpnameSearch()) {
			// return "corpname_t:%22" + getMcorpnameSearch() + "%22";
			// return "corpname_t:"+
			// URLEncoder.encode(getMcorpnameSearch().trim() + "","UTF-8");
			return "corpname_t:" + getMcorpnameSearch().trim();
		}
		if (null != getMsubjectheadingSearch()) {
			// return "subjectheading_t:%22" + getMsubjectheadingSearch() +
			// "%22";
			// return "subjectheading_t:"+
			// URLEncoder.encode(getMsubjectheadingSearch().trim() +
			// "","UTF-8");
			return "subjectheading_t:" + getMsubjectheadingSearch().trim();
		}
		if (null != getMgeognameSearch()) {
			// return "geogname_t:%22" + getMgeognameSearch() + "%22";
			// return "geogname_t:"+
			// URLEncoder.encode(getMgeognameSearch().trim() + "","UTF-8");
			return "geogname_t:" + getMgeognameSearch().trim();
		}
		if (null != getMfamnameSearch()) {
			// return "famname_t:%22" + getMfamnameSearch() + "%22";
			// return "famname_t:"+ URLEncoder.encode(getMfamnameSearch().trim()
			// + "","UTF-8");
			return "famname_t:" + getMfamnameSearch().trim();
		}
		if (null != getMuniformtitleSearch()) {
			// return "uniformtitle_t:%22" + getMuniformtitleSearch() + "%22";
			// return "uniformtitle_t:"+
			// URLEncoder.encode(getMuniformtitleSearch().trim() + "","UTF-8");
			return "uniformtitle_t:" + getMuniformtitleSearch().trim();
		}
		if (null != getmNametitleSearch()) {
			// return "nametitle_t:%22" + getmNametitleSearch() + "%22";
			// return "nametitle_t:"+
			// URLEncoder.encode(getmNametitleSearch().trim() + "","UTF-8");
			return "nametitle_t:" + getmNametitleSearch().trim();

		}
		if (null != getMtrademarkSearch()) {
			// return "trademark_t:%22" + getMtrademarkSearch() + "%22";
			// return "trademark_t:"+
			// URLEncoder.encode(getMtrademarkSearch().trim() + "","UTF-8");
			return "trademark_t:" + getMtrademarkSearch().trim();

		}
		if (null != getMppnSearch()) {
			// return "ppn_z:" + getMppnSearch();
			// return "ppn_z:"+ URLEncoder.encode(getMppnSearch().trim() + "",
			// "UTF-8");
			return "ppn_z:" + getMppnSearch().trim();
		}
		if (null != getMrcr()) {
			// return "rcr_t:" + getMrcr();
			// return "rcr_t:" + URLEncoder.encode(getMrcr().trim() + "",
			// "UTF-8");
			return "rcr_t:" + getMrcr().trim();
		}
		if (null != getMall()) {
			// return "all:%22" + getMall() + "%22";
			// return "all:" + URLEncoder.encode(getMall().trim(), "UTF-8");
			return "all:" + getMall().trim();
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
