package com.tp.macarte;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HOME on 21/05/2017.
 */

public class MaListe extends AppCompatActivity {
    public  static  MonMarqueur m ;
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
    public List<MonMarqueur> maliste ;

    ListView maListeView;
    ArrayList<MonMarqueur> mesMarqueurs=new ArrayList<>();
    BaseMarqueur data ;
    MonAdaptateur myAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.liste_layout);
        maListeView = (ListView) findViewById(R.id.ma_liste);
        for (int i = 0; i < MapsActivity.maliste.size() ; i++)
        {
            mesMarqueurs.add(MapsActivity.maliste.get(i));
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
                Intent i = new Intent(MaListe.this,LesDetails.class);
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
        this.getMenuInflater().inflate(R.menu.menu , menu);

    }


    /** This will be invoked when a menu item is selected */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        MonMarqueur t = mesMarqueurs.get(info.position);
        switch(item.getItemId()){
            case R.id.add:
                myAdapter.notifyDataSetChanged();
                if(t.getFav() == 0) {
                    t.setFav(1);
                    data.setFav(t);
                    Toast.makeText(getApplicationContext(),"l'endroit est ajouté aux favouris", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getApplicationContext(),"l'endroit est dejas ajouté aux favoris",Toast.LENGTH_LONG).show();
                }
                break;
        }
        return true;
    }

}
