package com.tp.macarte;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * Created by HOME on 31/05/2017.
 */

public class Fav extends AppCompatActivity {
    public  static MonMarqueur m ;
    public static String titre;
    public static String type;
    public static float lat;
    public static float lng;
    public static String image;
    public static String video;
    public static String description;
    public static String adresse;
    public static String telephone;
    public static String email;
    MonMarqueur t;

    ListView maListeView;
    ArrayList<MonMarqueur> mesMarqueurs=new ArrayList<>();
    BaseMarqueur data ;
    MonAdaptateur myAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.liste_layout);
        maListeView = (ListView) findViewById(R.id.ma_liste);
        for (int i = 0; i < MapsActivity.ma.size() ; i++)
        {
            mesMarqueurs.add(MapsActivity.ma.get(i));
        }
        myAdapter=new MonAdaptateur(this, R.layout.marqueur_layout,mesMarqueurs);
        maListeView.setAdapter(myAdapter);
        maListeView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = maListeView.getItemAtPosition(position);
                m = (MonMarqueur) o;
                titre = m.getTitre();
                type = m.getType();
                lat = m.getLat();
                lng = m.getLng();
                image = m.getImage();
                video = m.getVideo();
                description = m.getDescription();
                adresse = m.getAdresse();
                telephone = m.getTelephone();
                email = m.getEmail();
                Intent i = new Intent(Fav.this, LesDetailsFav.class);
                startActivityForResult(i,0);
            }
        });

        data = new BaseMarqueur(this);

        try {
            data.open();

        } catch (Exception e) {
            Log.i("Bonjours", "Bonjours");
        }



        registerForContextMenu(maListeView);
        myAdapter.setNotifyOnChange(true);
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        this.getMenuInflater().inflate(R.menu.act , menu);

    }


    /** This will be invoked when a menu item is selected */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
         t = mesMarqueurs.get(info.position);
        switch(item.getItemId()){
            case R.id.delt:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Vou etes sure, vous voulez supprimer cet endroit ?");
                alertDialogBuilder.setPositiveButton("oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        mesMarqueurs.clear();
                        mesMarqueurs.remove(t);
                        t.setFav(0);
                        data.setFav(t);
                        myAdapter.notifyDataSetChanged();
                    }
                });
                alertDialogBuilder.setNegativeButton("non", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            break;
        }
        return true;
    }
}
