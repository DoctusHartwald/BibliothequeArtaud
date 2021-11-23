package org.biblio.sqli.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MaBaseSQLiteCompteur extends SQLiteOpenHelper {
	public static final String TABLE_LIVRES_COMPTEUR = "table_compteur_records";
	public  static final String COL_ID = "ID";
	public  static final int COL_ID_NUM = 0;
	public static final String COL_COMPTEUR = "COMPTEUR";
	public static final int COL_COMPTEUR_NUM = 1;
	
	private static String CREATE_BDD=" CREATE TABLE "+TABLE_LIVRES_COMPTEUR+" ("+
			COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+COL_COMPTEUR + " INTEGER NOT NULL) ;";

	public MaBaseSQLiteCompteur(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//on créé la table à partir de la requête écrite dans la variable CREATE_BDD
		db.execSQL(CREATE_BDD);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//On peut fait ce qu'on veut ici moi j'ai décidé de supprimer la table et de la recréer
		//comme ça lorsque je change la version les id repartent de 0
		db.execSQL("DROP TABLE " + TABLE_LIVRES_COMPTEUR + ";");
		onCreate(db);
	}


}
