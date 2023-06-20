package com.uptree.lawdiary.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.uptree.lawdiary.R;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);


        findViewById(R.id.card_company).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, "UpTree Developers"); // query contains search string
                startActivity(intent);

            }
        });

        findViewById(R.id.card_website).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Getintent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.uptreedevelopers.com"));
                startActivity(Getintent);
            }
        });

        findViewById(R.id.card_email).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(Intent.ACTION_SEND);
                String[] recipients={"info@uptreedevelopers.com"};
                intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                intent.putExtra(Intent.EXTRA_SUBJECT,"");
                intent.putExtra(Intent.EXTRA_TEXT,"");
                intent.putExtra(Intent.EXTRA_CC,"mailcc@gmail.com");
                intent.setType("text/html");
                intent.setPackage("com.google.android.gm");
                startActivity(Intent.createChooser(intent, "Send mail"));

            }
        });

        findViewById(R.id.card_phone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phone = "+92 315 394 4711" ;

                Intent intent = new Intent(Intent.ACTION_DIAL);
                String temp = "tel:" + phone;
                intent.setData(Uri.parse(temp));

                startActivity(intent);

            }
        });
    }
}