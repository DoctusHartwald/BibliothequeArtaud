package org.biblio;

import org.apache.http.protocol.HttpContext;

import android.app.Application;

public class BibliMunicipaleSession extends Application {
	HttpContext localContext;

	public HttpContext getLocalContext() {
		return localContext;
	}

	public void setLocalContext(HttpContext localContext2) {
		this.localContext = localContext2;
	}
}
