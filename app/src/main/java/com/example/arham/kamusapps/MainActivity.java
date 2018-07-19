package com.example.arham.kamusapps;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnItemSelectedListener{

    private Spinner s1, s2;

    private ArrayList<Bahasa> listBahasa;
    ArrayAdapter adapter1, adapter2, adapter3;
    Button btn1, btnistilah, btnbahasa;
    String urls, bhs, defspinner;
    ImageView copyright, appslogo;

    private String url_getbahasa = "http://api.prime-strategy.co.id/kamusitas/v1/kamus";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.SplashScreen);
        try {
            Thread.sleep(2000);
        } catch(InterruptedException e){
            e.printStackTrace();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        s1 = findViewById(R.id.spinner1);
        s2 = findViewById(R.id.spinner2);
        btn1 = findViewById(R.id.btn1);
        btnistilah = findViewById(R.id.btnistilah);
        btnbahasa = findViewById(R.id.btn_bhs);
        copyright = findViewById(R.id.copyright);
        copyright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent();
                intent2.setAction(Intent.ACTION_VIEW);
                intent2.addCategory(Intent.CATEGORY_BROWSABLE);
                intent2.setData(Uri.parse("http://prime-strategy.co.id/"));
                startActivity(intent2);
            }
        });

        appslogo = findViewById(R.id.appslogo);
        appslogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainsite = new Intent();
                mainsite.setAction(Intent.ACTION_VIEW);
                mainsite.addCategory(Intent.CATEGORY_BROWSABLE);
                mainsite.setData(Uri.parse("http://kamusitas.com"));
                startActivity(mainsite);
            }
        });
        btn1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
//                urls = s2.getSelectedItem().toString();
                Intent intent =new Intent(MainActivity.this, KamusActivity.class);
                intent.putExtra("URL", urls);
                intent.putExtra("bahasa", bhs);
                startActivity(intent);
            }
        });

        listBahasa = new ArrayList<Bahasa>();

        btnistilah.setSelected(true);
        btnistilah.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                listBahasa.clear();
                new GetIstilahFromServer().execute();
                btnistilah.setBackgroundColor(getResources().getColor(R.color.lightgrey));
                btnbahasa.setBackgroundColor(getResources().getColor(R.color.greyer));
                return false;
            }
        });
        btnbahasa.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                listBahasa.clear();
                new GetBahasaFromServer().execute();
                btnistilah.setBackgroundColor(getResources().getColor(R.color.greyer));
                btnbahasa.setBackgroundColor(getResources().getColor(R.color.lightgrey));
                return false;
            }
        });



//        adapter1 = ArrayAdapter.createFromResource(this, R.array.pilihKamus, R.layout.spinner_item);
//        s1.setAdapter(adapter1);
//        s1.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        Spinner spinner = (Spinner) adapterView;
        if(spinner.getId() == R.id.spinner2){
            urls = listBahasa.get(position).getUrl();
            bhs = listBahasa.get(position).getFullName();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    private class GetBahasaFromServer extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            Handler jsonParser = new Handler();
            String json = jsonParser.makeServiceCall(url_getbahasa, Handler.GET);

            Log.e("Response: ", "> " + json);

            if(json != null) {
                try {
                    JSONObject jo = new JSONObject(json);
                    if (jo != null){
                        JSONArray source = jo.getJSONArray("list_endpoint");

                        for (int i = 0; i< source.length(); i++){
                            JSONObject bObj = (JSONObject) source.get(i);

                            Bahasa bahasa = new Bahasa(bObj.getString("from").toUpperCase(), bObj.getString("to").toUpperCase(), bObj.getString("uri"));
                            if (bObj.getString("from").equals("indo")){
                                bahasa.setSource("INDONESIA");
                            } else if (bObj.getString("to").equals("indo")){
                                bahasa.setTarget("INDONESIA");
                            }
                            listBahasa.add(bahasa);
                            if (bObj.getString("from").equals("indo") && bObj.getString("to").equals("adsense") || bObj.getString("to").equals("komputer")
                                    || bObj.getString("to").equals("kbbi")|| bObj.getString("to").equals("akuntansi")
                                    || bObj.getString("to").equals("bahasa-gaul-singkatan-inggris")
                                    || bObj.getString("to").equals("sansekerta")
                                    || bObj.getString("to").equals("blogger")
                                    || bObj.getString("to").equals("politik")
                                    || bObj.getString("to").equals("geografi")
                                    || bObj.getString("to").equals("kesehatan")
                                    || bObj.getString("to").equals("kamuskita")
                                    || bObj.getString("to").equals("sms-gaul")
                                    || bObj.getString("to").equals("tokoh")
                                    || bObj.getString("to").equals("jaringan")
                                    || bObj.getString("to").equals("geologi")
                                    || bObj.getString("to").equals("akuntansi")
                                    || bObj.getString("to").equals("perbankan")
                                    || bObj.getString("to").equals("padanan-komputer")
                                    || bObj.getString("to").equals("persahabatan")){
                                bahasa.setSource("INDONESIA");
                                listBahasa.remove(bahasa);
                            }
                        }
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            } else {
                Log.e("JSON Data", "Data not received");
            }

            return null;
        }

        protected void onPostExecute(Void result){
            super.onPostExecute(result);
            populateSpinnerModel();
        }

    }
    private void populateSpinnerModel(){
        ArrayList<String> lables = new ArrayList<String>();
//        ArrayList<String> urls = new ArrayList<String>();

        for(int i = 0; i < listBahasa.size(); i++){
            lables.add(listBahasa.get(i).getFullName());
//            urls.add(listURL.get(i).getUrl());
        }
        adapter2 = new ArrayAdapter<String>(this, R.layout.spinner_item, lables);
        s2.setAdapter(adapter2);
        s2.setOnItemSelectedListener(this);
    }

    private class GetIstilahFromServer extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            Handler jsonParser = new Handler();
            String json = jsonParser.makeServiceCall(url_getbahasa, Handler.GET);

            Log.e("Response: ", "> " + json);

            if(json != null) {
                try {
                    JSONObject jo = new JSONObject(json);
                    if (jo != null){
                        JSONArray source = jo.getJSONArray("list_endpoint");

                        for (int i = 0; i< source.length(); i++){
                            JSONObject bObj = (JSONObject) source.get(i);

                            Bahasa bahasa = new Bahasa(bObj.getString("from").toUpperCase(), bObj.getString("to").toUpperCase(), bObj.getString("uri"));
                            if (bObj.getString("from").equals("indo") && bObj.getString("to").equals("adsense") || bObj.getString("to").equals("komputer")
                                    || bObj.getString("to").equals("kbbi")|| bObj.getString("to").equals("akuntansi")
                                    || bObj.getString("to").equals("bahasa-gaul-singkatan-inggris")
                                    || bObj.getString("to").equals("sansekerta")
                                    || bObj.getString("to").equals("blogger")
                                    || bObj.getString("to").equals("politik")
                                    || bObj.getString("to").equals("geografi")
                                    || bObj.getString("to").equals("kesehatan")
                                    || bObj.getString("to").equals("kamuskita")
                                    || bObj.getString("to").equals("sms-gaul")
                                    || bObj.getString("to").equals("tokoh")
                                    || bObj.getString("to").equals("jaringan")
                                    || bObj.getString("to").equals("geologi")
                                    || bObj.getString("to").equals("akuntansi")
                                    || bObj.getString("to").equals("perbankan")
                                    || bObj.getString("to").equals("padanan-komputer")
                                    || bObj.getString("to").equals("persahabatan")){
                                bahasa.setSource("INDONESIA");
                                listBahasa.add(bahasa);
                            } else if (bObj.getString("to").equals("indo")){
                                bahasa.setTarget("INDONESIA");
                            }

                        }
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            } else {
                Log.e("JSON Data", "Data not received");
            }

            return null;
        }

        protected void onPostExecute(Void result){
            super.onPostExecute(result);
            populateSpinner2Model();
        }

    }
    private void populateSpinner2Model(){
//        ArrayList<String> lables = new ArrayList<String>();
        ArrayList<String> istilah = new ArrayList<>();

        for(int i = 0; i < listBahasa.size(); i++){
            istilah.add(listBahasa.get(i).getTarget());
//            urls.add(listURL.get(i).getUrl());
        }
        adapter3 = new ArrayAdapter<>(this, R.layout.spinner_item, istilah);
        s2.setAdapter(adapter3);
        s2.setOnItemSelectedListener(this);
    }
}


