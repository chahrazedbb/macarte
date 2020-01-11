package com.tp.macarte;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.rtoshiro.view.video.FullscreenVideoLayout;
import com.squareup.picasso.Picasso;

import java.io.IOException;

/**
 * Created by HOME on 31/05/2017.
 */

public class LesDetailsFav extends AppCompatActivity {

    ImageView image;
    RatingBar ratingbar1;
    BaseMarqueur data;
    TextView e4 ;
    TextView e5 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay);

        TextView e1 = (TextView) findViewById(R.id.titre);
        TextView e2 = (TextView) findViewById(R.id.type);
        TextView e3 = (TextView) findViewById(R.id.description);
        e4 = (TextView) findViewById(R.id.telephone);
         e5 = (TextView) findViewById(R.id.email);
        TextView e6 = (TextView) findViewById(R.id.adresse);
        image = (ImageView) findViewById(R.id.imageView);
        ratingbar1 = (RatingBar) findViewById(R.id.ratingBar2);

        ratingbar1.setRating(Fav.m.getVote());
        Toast.makeText(getApplicationContext(), String.valueOf(Fav.m.getVote()), Toast.LENGTH_LONG).show();


        e1.setText(Fav.titre);
        e2.setText(Fav.type);
        e3.setText(Fav.description);
        e4.setText(Fav.telephone);
        e5.setText(Fav.email);
        e6.setText(Fav.adresse);

        Picasso.with(this).load(Uri.parse("http://10.0.2.2/macarte/mesimages/" + Fav.image)).into(image);

        FullscreenVideoLayout videoLayout;

        videoLayout = (FullscreenVideoLayout) findViewById(R.id.videoview);
        videoLayout.setActivity(this);

        Uri videoUri = Uri.parse("http://10.0.2.2/macarte/mesvideos/" + Fav.video);
        try {
            videoLayout.setVideoURI(videoUri);

        } catch (IOException e) {
            e.printStackTrace();
        }

        data = new BaseMarqueur(this);

        try {
            data.open();

        } catch (Exception e) {
            Log.i("Bonjours", "Bonjours");
        }


    }

    public void voter(View v) {
        String rating = String.valueOf(ratingbar1.getRating());

        Fav.m.setVote(ratingbar1.getRating());
        data.setVote(Fav.m);

        Toast.makeText(getApplicationContext(), rating, Toast.LENGTH_LONG).show();
    }

    public void telephoner(View v) {
        try {
            String uri = "tel:" + e4.getText().toString();
            Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(uri));

            startActivity(dialIntent);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Votre Appel a echoué ...",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void send(View v)
    {
        Intent i = new Intent(this,MonEmail.class);
        MonEmail.em = String.valueOf(e5.getText());
        startActivityForResult(i,0);
    }


}
