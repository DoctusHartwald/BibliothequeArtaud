package org.biblio.mapping.viaf;


import java.util.List;

public class Viaf {
	private String viafId;
	private String birthDate ;
	private String dateType ;
	private String deathDate ;

	private List<Titre> titre;
	private List<CoAuthors> coauthors;
	private List<Publishers> publishers ;
	private List<String> libraries ;
	private List<Countries> countries ;
	private List<LanguageOfEntity> languageOfEntity;
	private List<NationalityOfEntity> nationalityOfEntity ; 

	/**
	 * @return the libraries
	 */
	public List<String> getLibraries() {
		return libraries;
	}
	/**
	 * @param libraries the libraries to set
	 */
	public void setLibraries(List<String> libraries) {
		this.libraries = libraries;
	}
	public String getViafId() {
		return viafId;
	}
	public void setViafId(String viafId) {
		this.viafId = viafId;
	}

	/**
	 * @return the deathDate
	 */
	public String getDeathDate() {
		return deathDate;
	}
	/**
	 * @param deathDate the deathDate to set
	 */
	public void setDeathDate(String deathDate) {
		this.deathDate = deathDate;
	}

	public List<Titre> getTitre() {
		return titre;
	}
	public void setTitre(List<Titre> titre) {
		this.titre = titre;
	}
	/**
	 * @return the coauthors
	 */
	public List<CoAuthors> getCoauthors() {
		return coauthors;
	}
	/**
	 * @param coauthors the coauthors to set
	 */
	public void setCoauthors(List<CoAuthors> coauthors) {
		this.coauthors = coauthors;
	}
	/**
	 * @return the publishers
	 */
	public List<Publishers> getPublishers() {
		return publishers;
	}
	/**
	 * @param publishers the publishers to set
	 */
	public void setPublishers(List<Publishers> publishers) {
		this.publishers = publishers;
	}
	/**
	 * @return the countries
	 */
	public List<Countries> getCountries() {
		return countries;
	}

	/**
	 * @param countries the countries to set
	 */
	public void setCountries(List<Countries> countries) {
		this.countries = countries;
	}
	/**
	 * @return the languageOfEntity
	 */
	public List<LanguageOfEntity> getLanguageOfEntity() {
		return languageOfEntity;
	}
	/**
	 * @param languageOfEntity the languageOfEntity to set
	 */
	public void setLanguageOfEntity(List<LanguageOfEntity> languageOfEntity) {
		this.languageOfEntity = languageOfEntity;
	}
	/**
	 * @return the birthDate
	 */
	public String getBirthDate() {
		return birthDate;
	}
	/**
	 * @param birthDate the birthDate to set
	 */
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
	/**
	 * @return the dateType
	 */
	public String getDateType() {
		return dateType;
	}
	/**
	 * @param dateType the dateType to set
	 */
	public void setDateType(String dateType) {
		this.dateType = dateType;
	}
	/**
	 * @return the nationalityOfEntity
	 */
	public List<NationalityOfEntity> getNationalityOfEntity() {
		return nationalityOfEntity;
	}
	/**
	 * @param nationalityOfEntity the nationalityOfEntity to set
	 */
	public void setNationalityOfEntity(List<NationalityOfEntity> nationalityOfEntity) {
		this.nationalityOfEntity = nationalityOfEntity;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Viaf [viafId=");
		builder.append(viafId);
		builder.append(", birthDate=");
		builder.append(birthDate);
		builder.append(", dateType=");
		builder.append(dateType);
		builder.append(", deathDate=");
		builder.append(deathDate);
		builder.append(", titre=");
		builder.append(titre);
		builder.append(", coauthors=");
		builder.append(coauthors);
		builder.append(", publishers=");
		builder.append(publishers);
		builder.append(", libraries=");
		builder.append(libraries);
		builder.append(", countries=");
		builder.append(countries);
		builder.append(", languageOfEntity=");
		builder.append(languageOfEntity);
		builder.append(", nationalityOfEntity=");
		builder.append(nationalityOfEntity);
		builder.append("]");
		return builder.toString();
	}
}