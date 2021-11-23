package org.biblio.sqli.dao;

import org.biblio.sqli.Compteur;
import org.biblio.sqli.db.MaBaseSQLiteCompteur;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CompteurBdd {
	final String TAG = getClass().getSimpleName();
	
	private static int VERSION_BDD = 1; 
	private SQLiteDatabase bdd;
	private MaBaseSQLiteCompteur maBaseSQLite ; 
	
	public CompteurBdd(Context context) {
		maBaseSQLite = new MaBaseSQLiteCompteur(context, MaBaseSQLiteCompteur.TABLE_LIVRES_COMPTEUR, null, VERSION_BDD);
	}
	
	public void open(){
		//on ouvre la BDD en écriture
		bdd = maBaseSQLite.getWritableDatabase();
	}
	
	public void close(){
		//on ferme l'accès à la BDD
		bdd.close();
	}
 
	public SQLiteDatabase getBDD(){
		return bdd;
	}
	public long insertCompteur(Compteur compteur){
		//Création d'un ContentValues (fonctionne comme une HashMap)
		ContentValues values = new ContentValues();
		values.put(MaBaseSQLiteCompteur.COL_COMPTEUR, compteur.getCompteur());
		return bdd.insert(MaBaseSQLiteCompteur.TABLE_LIVRES_COMPTEUR, null ,  values);
	}
	
	public Compteur getCompteur(){
		//Récupère dans un Cursor les valeur correspondant à un livre contenu dans la BDD (ici on sélectionne le livre grâce à son titre)
		String query =" SELECT *   FROM  "+MaBaseSQLiteCompteur.TABLE_LIVRES_COMPTEUR+" ORDER BY "+MaBaseSQLiteCompteur.COL_COMPTEUR+"  desc";
		Cursor c = bdd.rawQuery(query, null);
		
		
		if(c!=null){
			Log.d(TAG, "getCompteur  : "+String.valueOf(c.getCount()));
		}
			
		
		 //bdd.query(MaBaseSQLiteCompteur.TABLE_LIVRES_COMPTEUR, new String[] {MaBaseSQLiteCompteur.COL_ID, MaBaseSQLiteCompteur.COL_COMPTEUR}, MaBaseSQLiteCompteur.COL_COMPTEUR + " ORDER BY DESC \"" + titre +"\"", null, null, );
		return cursorToCompteur(c);
	}
 
	
	private Compteur cursorToCompteur(Cursor c){
		//si aucun élément n'a été retourné dans la requête, on renvoie null
		if (c.getCount() == 0)
			return null;
		
		//Sinon on se place sur le premier élément
		c.moveToFirst();
		Compteur compteur = new Compteur();
		compteur.setId(c.getInt(MaBaseSQLiteCompteur.COL_ID_NUM));
		compteur.setCompteur(c.getInt(MaBaseSQLiteCompteur.COL_COMPTEUR_NUM));
		c.close();
		
		Log.d(TAG, compteur.toString());
		return compteur;
	}
}
