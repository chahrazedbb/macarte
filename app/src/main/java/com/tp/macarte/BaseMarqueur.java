package com.tp.macarte;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HOME on 18/03/2017.
 */

public class BaseMarqueur {
    MaBase mabase;
    SQLiteDatabase bdd;

    String[] colonnes = {MaBase.TITRE,
                         MaBase.TYPE,
                         MaBase.LAT,
                         MaBase.LNG,
                         MaBase.IMAGE,
                         MaBase.VIDEO,
                         MaBase.DESCRIPTION,
                         MaBase.ADRESSE,
                         MaBase.TELEPHONE,
                         MaBase.EMAIL ,
                         MaBase.VOTE ,
                         MaBase.FAV};

    public BaseMarqueur(Context c) {
        mabase = new MaBase(c);
    }

    public void open() throws SQLException {
        bdd = mabase.getWritableDatabase();
    }

    public void close(){
        bdd.close();
    }

    public void ajoutMarqueur(MonMarqueur m){
        ContentValues v = new ContentValues();

        v.put(mabase.TITRE, m.getTitre());
        v.put(mabase.TYPE, m.getType());
        v.put(mabase.LAT, m.getLat());
        v.put(mabase.LNG, m.getLng());
        v.put(mabase.IMAGE, m.getImage());
        v.put(mabase.VIDEO, m.getVideo());
        v.put(mabase.DESCRIPTION, m.getDescription());
        v.put(mabase.ADRESSE, m.getAdresse());
        v.put(mabase.TELEPHONE, m.getTelephone());
        v.put(mabase.EMAIL, m.getEmail());
        v.put(mabase.VOTE, m.getVote());
        v.put(mabase.FAV,m.getFav());

        bdd.insert(mabase.NOM_TABLE, null, v);

    }

    public List<MonMarqueur> getMonMar(){
        List<MonMarqueur> marqueurs = new ArrayList<MonMarqueur>();

        Cursor cur = bdd.query(mabase.NOM_TABLE, colonnes,null, null, null, null, null);

        cur.moveToFirst();
        while (!cur.isAfterLast()) {
            MonMarqueur m = curVersMar(cur);
            marqueurs.add(m);
            cur.moveToNext();
        }
        cur.close();
        return marqueurs;
    }

    public List<MonMarqueur> getFav(){
        List<MonMarqueur> marqueurs = new ArrayList<MonMarqueur>();

        Cursor cur = bdd.query(mabase.NOM_TABLE, colonnes, mabase.FAV + " = 1  ; ", null, null, null, null);

        cur.moveToFirst();
        while (!cur.isAfterLast()) {
            MonMarqueur m = curVersMar(cur);
            marqueurs.add(m);
            cur.moveToNext();
        }
        cur.close();
        return marqueurs;
    }

    public List<MonMarqueur> getMonMarqueur(String s){
        List<MonMarqueur> marqueurs = new ArrayList<MonMarqueur>();

        Cursor cur = bdd.query(mabase.NOM_TABLE, colonnes,"(" +  mabase.TYPE + " = '" + s + "') or (" + mabase.TITRE +" like '%"+ s + "%') ; ", null, null, null, null);

        cur.moveToFirst();
        while (!cur.isAfterLast()) {
            MonMarqueur m = curVersMar(cur);
            marqueurs.add(m);
            cur.moveToNext();
        }
        cur.close();
        return marqueurs;
    }

    public void SuppMarqueur(MonMarqueur m){
        bdd.delete(MaBase.NOM_TABLE,MaBase.TITRE + " = '" + m.getTitre() + "'", null);
    }


    public void setVote(MonMarqueur m){
        ContentValues v = new ContentValues();
        v.put(mabase.VOTE, m.getVote());
        bdd.update(mabase.NOM_TABLE, v,mabase.TITRE + " = '" + m.getTitre() + "'",null);
    }

    public void setFav(MonMarqueur m){
        ContentValues v = new ContentValues();
        v.put(mabase.FAV, m.getFav());
        bdd.update(mabase.NOM_TABLE, v,mabase.TITRE + " = '" + m.getTitre() + "'",null);
    }


    private MonMarqueur curVersMar(Cursor cur) {
        MonMarqueur m = new MonMarqueur();
        m.setTitre(cur.getString(0));
        m.setType(cur.getString(1));
        m.setLat(cur.getFloat(2));
        m.setLng(cur.getFloat(3));
        m.setImage(cur.getString(4));
        m.setVideo(cur.getString(5));
        m.setDescription(cur.getString(6));
        m.setAdresse(cur.getString(7));
        m.setTelephone(cur.getString(8));
        m.setEmail(cur.getString(9));
        m.setVote(cur.getFloat(10));
        m.setFav(cur.getInt(11));
        return m;
    }

}
