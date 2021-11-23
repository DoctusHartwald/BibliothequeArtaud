package org.biblio.sqli;

public class Livre {

	private int id;
	private String ppn;
	private String titre;
	private String auteur;
	private String description;
	private String date;
	private int  check;

	public Livre() {
	}

	public Livre(String ppn, String titre, String description, String auteur,int check) {
		this.ppn = ppn;
		this.titre = titre;
		this.description = description;
		this.auteur = auteur;
		this.check = check; 
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPpn() {
		return ppn;
	}

	public void setPpn(String ppn) {
		this.ppn = ppn;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public String getAuteur() {
		return auteur;
	}

	public void setAuteur(String auteur) {
		this.auteur = auteur;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	

	public int getCheck() {
		return check;
	}

	public void setCheck(int check) {
		this.check = check;
	}

	public String toString() {
		return "ID : " + id + "\nPPN : " + ppn + "\nTitre : " + titre
				+ "\nAuteur : " + auteur + "\nDescription : " + description
				+ "\nDate : " + date;
	}
}