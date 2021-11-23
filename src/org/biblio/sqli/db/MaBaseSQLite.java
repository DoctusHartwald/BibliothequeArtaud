package org.biblio.sqli.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MaBaseSQLite extends SQLiteOpenHelper {

	public static final String TABLE_LIVRES = "table_livres_records";
	
	public static final String COL_ID = "_id"; 
	public static final String COL_PPN = "PPN"; 
	public static final String COL_TITRE = "Titre";
	public static final String COL_AUTEUR = "Auteur"; 
	public static final String COL_DATE = "Date"; 
	public static final String COL_DESCRIPTION = "Description";
	public static final String COL_CHECKED = "Checked";
	
	public static final int NUM_COL_ID = 0;
	public static final int NUM_COL_PPN = 1;
	public static final int NUM_COL_TITRE = 2;
	public static final int NUM_COL_AUTEUR = 3;
	public static final int NUM_COL_DESCRIPTION = 4;
	public static final int NUM_COL_DATE = 5;
	public static final int NUM_COL_CHECKED = 6;

	private static final String CREATE_BDD = "CREATE TABLE " + TABLE_LIVRES
			+ " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT  , "
			+ COL_PPN + "  TEXT NOT NULL UNIQUE, "
			+ COL_TITRE + " TEXT NOT NULL,"
			+ COL_AUTEUR+ " TEXT NULL, "
			+ COL_DESCRIPTION + " TEXT NOT NULL," 
			+ COL_DATE+ " TEXT NOT NULL, "
			+ COL_CHECKED+" INTEGER );";

	public MaBaseSQLite(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// on créé la table à partir de la requête écrite dans la variable
		// CREATE_BDD
		db.execSQL(CREATE_BDD);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// On peut fait ce qu'on veut ici moi j'ai décidé de supprimer la table
		// et de la recréer
		// comme ça lorsque je change la version les id repartent de 0
		db.execSQL("DROP TABLE " + TABLE_LIVRES + ";");
		onCreate(db);
	}

}
