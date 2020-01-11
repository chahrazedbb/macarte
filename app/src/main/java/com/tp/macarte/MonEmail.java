package com.tp.macarte;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by HOME on 03/06/2017.
 */

public class MonEmail  extends AppCompatActivity {

    ImageButton sendEmailButton;
    EditText emailAddress;
    EditText emailSubject;
    EditText message;
    public static String em ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_layout);

        emailAddress = (EditText) findViewById(R.id.email);
        emailAddress.setText(em);
        emailSubject = (EditText) findViewById(R.id.subject);
        message = (EditText) findViewById(R.id.message);
        sendEmailButton = (ImageButton) findViewById(R.id.imb);

        sendEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String toemailAddress = emailAddress.getText().toString();
                String msubject = emailSubject.getText().toString();
                String mmessage = message.getText().toString();

                Intent emailApp = new Intent(Intent.ACTION_SEND);
                emailApp.putExtra(Intent.EXTRA_EMAIL, new String[]{toemailAddress});
                emailApp.putExtra(Intent.EXTRA_SUBJECT, msubject);
                emailApp.putExtra(Intent.EXTRA_TEXT, mmessage);
                emailApp.setType("message/rfc822");
                startActivity(Intent.createChooser(emailApp, "Envoyer l'email via"));

            }
        });
    }
}
