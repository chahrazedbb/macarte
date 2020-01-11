package com.tp.macarte;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by HOME on 20/05/2017.
 */

public class Analyseur  extends AsyncTask<Void,Void,Integer> {

    Context c;
    BaseMarqueur data ;
    String jsonData;



    ProgressDialog pd;
    ArrayList<MonMarqueur> marqueurs=new ArrayList<>();

    public Analyseur(Context c, BaseMarqueur data, String jsonData) {
        this.c = c;
        this.data = data;
        this.jsonData = jsonData;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pd=new ProgressDialog(c);
        pd.setTitle("Parse");
        pd.setMessage("Parsing...Please wait");
        pd.show();
    }

    @Override
    protected Integer doInBackground(Void... params) {
        return this.parseData();
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);

        pd.dismiss();
        if(result==0)
        {
            Toast.makeText(c,"Unable to parse",Toast.LENGTH_SHORT).show();
        }else {

                for(int i = 0 ; i <marqueurs.size() ; i++)
                {
                    data.ajoutMarqueur(marqueurs.get(i));
                }

        }
    }

    private int parseData()
    {
        try {
            JSONArray ja=new JSONArray(jsonData);
            JSONObject jo=null;

            marqueurs.clear();
            MonMarqueur s=null;

            for(int i=0;i<ja.length();i++)
            {
                jo=ja.getJSONObject(i);

                String titre=jo.getString("titre");
                String type = jo.getString("type");
                String lat = jo.getString("lat");
                String lng = jo.getString("lng");
                String image = jo.getString("image");
                String video = jo.getString("video");
                String description = jo.getString("description");
                String adresse = jo.getString("adresse");
                String telephone = jo.getString("telephone");
                String email = jo.getString("email");

                s=new MonMarqueur();
                s.setTitre(titre);
                s.setType(type);
                s.setLat(Float.parseFloat(lat));
                s.setLng(Float.parseFloat(lng));
                s.setImage(image);
                s.setVideo(video);
                s.setDescription(description);
                s.setAdresse(adresse);
                s.setTelephone(telephone);
                s.setEmail(email);

                marqueurs.add(s);

            }

            return 1;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return 0;
    }
}















