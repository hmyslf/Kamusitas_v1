package com.example.arham.kamusapps;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class DictionaryActivity extends AppCompatActivity {

    TextView word, wordMeaning;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);



        String words = bundle.getString("word");
        String definition = bundle.getString("def");

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
