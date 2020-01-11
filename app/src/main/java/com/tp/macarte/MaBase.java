package com.tp.macarte;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by HOME on 18/03/2017.
 */

public class MaBase extends SQLiteOpenHelper {

    public static final String NOM_TABLE = "end";

    public static final String COL_ID = "id";
    public static final String TITRE = "titre";
    public static final String TYPE = "type" ;
    public static final String LAT = "lat";
    public static final String LNG="lng";
    public static final String IMAGE="image";
    public static final String VIDEO="video";
    public static final String DESCRIPTION="description";
    public static final String ADRESSE="adresse";
    public static final String TELEPHONE="telephone";
    public static final String EMAIL="email";
    public static final String VOTE="vote";
    public static final String FAV="fav";

    private static final int D_VERSION = 1;

    private static final String NOM_BDD = "maCarte.db";

    private static final String CREATION_BDD =
            "create TABLE " + NOM_TABLE + " ( "
                    + COL_ID + " integer primary key autoincrement, "
                    + TITRE + " text, "
                    + TYPE + " text, "
                    + LAT + " float, "
                    + LNG + " float, "
                    + IMAGE + " text, "
                    + VIDEO + " text, "
                    + DESCRIPTION + " text, "
                    + ADRESSE + " text, "
                    + TELEPHONE + " text, "
                    + EMAIL + " text,"
                    + VOTE + " float,"
                    + FAV + " integer );";

    public MaBase(Context c) {
        super(c, NOM_BDD, null, D_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase bdd) {
        bdd.execSQL(CREATION_BDD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase bdd, int ancVersion, int nouvVersion) {
        bdd.execSQL("DROP TABLE IF EXISTS " + NOM_TABLE);
        onCreate(bdd);
    }

}
