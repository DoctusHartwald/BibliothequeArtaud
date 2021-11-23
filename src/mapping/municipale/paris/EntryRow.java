package mapping.municipale.paris;

public class EntryRow {

	Integer position;

	String title;
	String editeur;
	String auteur;
	String hiddenIsbn;
	String hiddenTitle;
	String hiddenCreator;
	String hiddenStatus;
	String hiddenNew;

	String publie;
	String format;
	String collection;
	String contenu;
	String typeDeDocument;
	String autreInformation;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("EntryRow [position=");
		builder.append(position);
		builder.append(", title=");
		builder.append(title);
		builder.append(", editeur=");
		builder.append(editeur);
		builder.append(", auteur=");
		builder.append(auteur);
		builder.append(", hiddenIsbn=");
		builder.append(hiddenIsbn);
		builder.append(", hiddenTitle=");
		builder.append(hiddenTitle);
		builder.append(", hiddenCreator=");
		builder.append(hiddenCreator);
		builder.append(", hiddenStatus=");
		builder.append(hiddenStatus);
		builder.append(", hiddenNew=");
		builder.append(hiddenNew);
		builder.append(", publie=");
		builder.append(publie);
		builder.append(", format=");
		builder.append(format);
		builder.append(", collection=");
		builder.append(collection);
		builder.append(", contenu=");
		builder.append(contenu);
		builder.append(", typeDeDocument=");
		builder.append(typeDeDocument);
		builder.append(", autreInformation=");
		builder.append(autreInformation);
		builder.append("]");
		return builder.toString();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuteur() {
		return auteur;
	}

	public void setAuteur(String auteur) {
		this.auteur = auteur;
	}

	public String getHiddenIsbn() {
		return hiddenIsbn;
	}

	public void setHiddenIsbn(String hiddenIsbn) {
		this.hiddenIsbn = hiddenIsbn;
	}

	public String getHiddenTitle() {
		return hiddenTitle;
	}

	public void setHiddenTitle(String hiddenTitle) {
		this.hiddenTitle = hiddenTitle;
	}

	public String getHiddenCreator() {
		return hiddenCreator;
	}

	public void setHiddenCreator(String hiddenCreator) {
		this.hiddenCreator = hiddenCreator;
	}

	public String getHiddenStatus() {
		return hiddenStatus;
	}

	public void setHiddenStatus(String hiddenStatus) {
		this.hiddenStatus = hiddenStatus;
	}

	public String getHiddenNew() {
		return hiddenNew;
	}

	public void setHiddenNew(String hiddenNew) {
		this.hiddenNew = hiddenNew;
	}

	public String getEditeur() {
		return editeur;
	}

	public void setEditeur(String editeur) {
		this.editeur = editeur;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public String getPublie() {
		return publie;
	}

	public void setPublie(String publie) {
		this.publie = publie;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getCollection() {
		return collection;
	}

	public void setCollection(String collection) {
		this.collection = collection;
	}

	public String getContenu() {
		return contenu;
	}

	public String getAutreInformation() {
		return autreInformation;
	}

	public void setAutreInformation(String autreInformation) {
		this.autreInformation = autreInformation;
	}

	public void setContenu(String contenu) {
		this.contenu = contenu;
	}

	public String getTypeDeDocument() {
		return typeDeDocument;
	}

	public void setTypeDeDocument(String typeDeDocument) {
		this.typeDeDocument = typeDeDocument;
	}
}
