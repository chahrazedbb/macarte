package com.tp.macarte;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by HOME on 21/05/2017.
 */

public class MonAdaptateur extends ArrayAdapter<MonMarqueur> {

    ArrayList<MonMarqueur> marqueurs = new ArrayList<>();

    public MonAdaptateur(Context context, int textViewResourceId, ArrayList<MonMarqueur> objects) {
        super(context, textViewResourceId, objects);
        marqueurs = objects;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        LayoutInflater inflater = LayoutInflater.from(getContext());
        v = inflater.inflate(R.layout.marqueur_layout, null);
        TextView textView = (TextView) v.findViewById(R.id.titre);
        ImageView imageView = (ImageView) v.findViewById(R.id.comic_image);

        textView.setText(marqueurs.get(position).getTitre());

        Picasso.with(getContext()).load(Uri.parse("http://10.0.2.2/macarte/mesimages/"+marqueurs.get(position).getImage())).into(imageView);


        return v;

    }
}