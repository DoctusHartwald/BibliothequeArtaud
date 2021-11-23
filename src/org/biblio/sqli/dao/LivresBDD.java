package org.biblio.sqli.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.biblio.sqli.Livre;
import org.biblio.sqli.db.MaBaseSQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class LivresBDD {
	final String TAG = getClass().getSimpleName();
	private static final int VERSION_BDD = 1;
	private static final String NOM_BDD = "bookworm.db";

	private String[] allColumns = {MaBaseSQLite.COL_ID,  MaBaseSQLite.COL_PPN,
			MaBaseSQLite.COL_TITRE, MaBaseSQLite.COL_AUTEUR,
			 MaBaseSQLite.COL_DESCRIPTION , MaBaseSQLite.COL_DATE,MaBaseSQLite.COL_CHECKED};


	private SQLiteDatabase bdd;

	private MaBaseSQLite maBaseSQLite;

	public LivresBDD(Context context) {
		// On créer la BDD et sa table
		maBaseSQLite = new MaBaseSQLite(context, NOM_BDD, null, VERSION_BDD);
	}

	public void open() {
		// on ouvre la BDD en écriture
		bdd = maBaseSQLite.getWritableDatabase();
	}

	public void close() {
		// on ferme l'accès à la BDD
		bdd.close();
	}

	public SQLiteDatabase getBDD() {
		return bdd;
	}

	public long insertLivre(Livre livre) {
		// Création d'un ContentValues (fonctionne comme une HashMap)
		ContentValues values = new ContentValues();
		// on lui ajoute une valeur associé à une clé (qui est le nom de la
		// colonne dans laquelle on veut mettre la valeur)
		values.put(MaBaseSQLite.COL_PPN, livre.getPpn());
		values.put(MaBaseSQLite.COL_TITRE, livre.getTitre());
		values.put(MaBaseSQLite.COL_AUTEUR, livre.getAuteur());
		values.put(MaBaseSQLite.COL_DESCRIPTION, livre.getDescription());

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		values.put(MaBaseSQLite.COL_DATE, dateFormat.format(date));
		values.put(MaBaseSQLite.COL_CHECKED, livre.getCheck());


		
		Log.d(TAG, "==== Insert Livre ======");
		Log.d(TAG,livre.toString());
		
		// on insère l'objet dans la BDD via le ContentValues
		return bdd.insert(MaBaseSQLite.TABLE_LIVRES, null, values);
	}

	public long insertLivreWithAuteur(Livre livre) {
		// Création d'un ContentValues (fonctionne comme une HashMap)
		ContentValues values = new ContentValues();
		// on lui ajoute une valeur associé à une clé (qui est le nom de la
		// colonne dans laquelle on veut mettre la valeur)
		values.put(MaBaseSQLite.COL_PPN, livre.getPpn());
		values.put(MaBaseSQLite.COL_TITRE, livre.getTitre());
		values.put(MaBaseSQLite.COL_AUTEUR, livre.getAuteur());
		values.put(MaBaseSQLite.COL_CHECKED, livre.getCheck());

		if (!"".equals(livre.getDescription()))
			values.put(MaBaseSQLite.COL_DESCRIPTION, livre.getDescription());

		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		values.put(MaBaseSQLite.COL_DATE, dateFormat.format(date));

		// on insère l'objet dans la BDD via le ContentValues
		return bdd.insert(MaBaseSQLite.TABLE_LIVRES, null, values);
	}

	// Update ----------------------------------------
	public int updateLivre(int id, Livre livre) {
		// La mise à jour d'un livre dans la BDD fonctionne plus ou moins comme
		// une insertion
		// il faut simple préciser quelle livre on doit mettre à jour grâce à
		// l'ID
		ContentValues values = new ContentValues();
		values.put(MaBaseSQLite.COL_PPN, livre.getPpn());
		values.put(MaBaseSQLite.COL_TITRE, livre.getTitre());

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		values.put(MaBaseSQLite.COL_DATE, dateFormat.format(date));
		return bdd.update(MaBaseSQLite.TABLE_LIVRES, values, MaBaseSQLite.COL_PPN + " = " + id, null);
	}

	public int updateLivreWithAuteur(int id, Livre livre) {
		// La mise à jour d'un livre dans la BDD fonctionne plus ou moins comme
		// une insertion
		// il faut simple préciser quelle livre on doit mettre à jour grâce à
		// l'ID
		ContentValues values = new ContentValues();
		values.put(MaBaseSQLite.COL_PPN, livre.getPpn());
		values.put(MaBaseSQLite.COL_AUTEUR, livre.getAuteur());

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		values.put(MaBaseSQLite.COL_DATE, dateFormat.format(date));
		return bdd.update(MaBaseSQLite.TABLE_LIVRES, values, MaBaseSQLite.COL_PPN + " = " + id, null);
	}

	public int updateLivreWithDescription(int id, Livre livre) {
		// La mise à jour d'un livre dans la BDD fonctionne plus ou moins comme
		// une insertion
		// il faut simple préciser quelle livre on doit mettre à jour grâce à
		// l'ID
		ContentValues values = new ContentValues();
		values.put(MaBaseSQLite.COL_PPN, livre.getPpn());
		values.put(MaBaseSQLite.COL_DESCRIPTION, livre.getDescription());

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		values.put(MaBaseSQLite.COL_DATE, dateFormat.format(date));
		return bdd.update(MaBaseSQLite.TABLE_LIVRES, values, MaBaseSQLite.COL_PPN + " = " + id, null);
	}

	// Remove ----------------------------------------
	public int removeLivreWithID(int id) {
		// Suppression d'un livre de la BDD grâce à l'ID
		return bdd.delete(MaBaseSQLite.TABLE_LIVRES, MaBaseSQLite.COL_PPN + " = " + id, null);
	}

	// GET ----------------------------------------
	// retrieve specific Book
	public Livre getLivreWithTitre(String titre) {
		// Récupère dans un Cursor les valeur correspondant à un livre contenu
		// dans la BDD (ici on sélectionne le livre grâce à son titre)
		Cursor c = bdd.query(MaBaseSQLite.TABLE_LIVRES, allColumns, MaBaseSQLite.COL_TITRE + " LIKE \""
				+ titre + "\"", null, null, null, null);
		return cursorToLivre(c);
	}

	public Livre getLivreWithAuteur(String auteur) {
		// Récupère dans un Cursor les valeur correspondant à un livre contenu
		// dans la BDD (ici on sélectionne le livre grâce à son titre)
		Cursor c = bdd.query(MaBaseSQLite.TABLE_LIVRES, allColumns, MaBaseSQLite.COL_AUTEUR + " LIKE \""
				+ auteur + "\"", null, null, null, null);
		return cursorToLivre(c);
	}

	public Livre getLivreByDate(String date) {
		// Récupère dans un Cursor les valeur correspondant à un livre contenu
		// dans la BDD (ici on sélectionne le livre grâce à son titre)
		Cursor c = bdd.query(MaBaseSQLite.TABLE_LIVRES, allColumns, MaBaseSQLite.COL_DATE + " LIKE \""
				+ date + "\"", null, null, null, null);
		return cursorToLivre(c);
	}

	// Cette méthode permet de convertir un cursor en un livre
	private Livre cursorToLivre(Cursor c) {
		Livre livre = new Livre();
		// on lui affecte toutes les infos grâce aux infos contenues dans le
		// Cursor
		livre.setId(c.getInt(MaBaseSQLite.NUM_COL_ID));
		livre.setPpn(c.getString(MaBaseSQLite.NUM_COL_PPN));
		livre.setTitre(c.getString(MaBaseSQLite.NUM_COL_TITRE));
		livre.setAuteur(c.getString(MaBaseSQLite.NUM_COL_AUTEUR));
		livre.setDescription(c.getString(MaBaseSQLite.NUM_COL_DESCRIPTION));
		livre.setDate(c.getString(MaBaseSQLite.NUM_COL_DATE));
		livre.setCheck(c.getInt(MaBaseSQLite.NUM_COL_CHECKED));
		
		Log.d(TAG, livre.toString());
		return livre;
	}

	public Cursor getAllLivresCursor() {

		Cursor cursor = bdd.query(MaBaseSQLite.TABLE_LIVRES, allColumns,null,
				null, null, null, null);
		return cursor;
	}

	public List<Livre> getAllLivres() {
		List<Livre> livres = new ArrayList<Livre>();
		Cursor cursor = bdd.query(MaBaseSQLite.TABLE_LIVRES, allColumns, null,
				null, null, null, null);

		if (cursor.getCount() == 0) {
			Log.d("DATA", "No data retrieve from database");
			return null;
		}

		cursor.moveToFirst(); // Sinon on se place sur le premier élément
		while (!cursor.isAfterLast()) {
			Livre livre = cursorToLivre(cursor);
			Log.d(TAG, "Cursor <<<<Link ");
			Log.d(TAG, livre.toString());
			livres.add(livre);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		Log.d("=>DATA", "Size : " + livres.size());
		cursor.close();
		return livres;
	}

}