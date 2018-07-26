package com.example.arham.kamusapps;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class DictionaryActivity2 extends AppCompatActivity {
    TextView word, wordMeaning, kamustxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowTitleEnabled(true);
        String txt_kamus = bundle.getString("kamustxt");
        ab.setTitle("Kamus\t" + txt_kamus);



        String words = bundle.getString("word2");
        String definition = bundle.getString("def2");

        word = findViewById(R.id.word);
        wordMeaning = findViewById(R.id.dictionary);
        word.setText(words);
        wordMeaning.setText(definition);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == android.R.id.home){
            onBackPressed();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
