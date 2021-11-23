package org.biblio.sqli;

public class Compteur {
	private int id;
	private int compteur;
	public Compteur(){}
	public Compteur(int compteur){
		this.compteur=compteur;
	}
	@Override
	public String toString() {
		return "Compteur [id=" + id + ", compteur=" + compteur + "]";
	}
	public int getCompteur() {
		return compteur;
	}

	public void setCompteur(int compteur) {
		this.compteur = compteur;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
}
