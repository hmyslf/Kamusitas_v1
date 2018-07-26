package com.example.arham.kamusapps;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
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
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnItemSelectedListener{

    private Spinner s2;
    ProgressDialog pdialog;
    private ArrayList<Bahasa> listBahasa;
    ArrayAdapter adapter2, adapter3;
    Button btn1, btnistilah, btnbahasa, btndefault;
    String urls, bhs, defspinner, divider, titlebhs, bahasa, bahasatgt, lcbahasa;
    ImageView copyright, appslogo;
    ArrayList<String> istilah, lables;
    SharedPreferences sharedPref;
    int usersChoice;

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

        s2 = findViewById(R.id.spinner2);
        btn1 = findViewById(R.id.btn1);
        btnistilah = findViewById(R.id.btnistilah);
        btnbahasa = findViewById(R.id.btn_bhs);
        btndefault = findViewById(R.id.btn2);
        copyright = findViewById(R.id.copyright);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Chivo.ttf");
        btnistilah.setTypeface(custom_font, Typeface.BOLD);
        btnbahasa.setTypeface(custom_font, Typeface.BOLD);
        btn1.setTypeface(custom_font, Typeface.BOLD);
        btndefault.setTypeface(custom_font,Typeface.BOLD);
        pdialog = new ProgressDialog(this);
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

        lables = new ArrayList<>();

        btn1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent =new Intent(MainActivity.this, KamusActivity.class);
                intent.putExtra("URL", urls);
                intent.putExtra("bahasa", titlebhs);
                intent.putExtra("divider", divider);
                intent.putExtra("bhs", bahasa);
                intent.putExtra("bhstgt", bahasatgt);
                startActivity(intent);
            }
        });
        btndefault.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                usersChoice = s2.getSelectedItemPosition();
                sharedPref = getSharedPreferences("FileName",0);
                SharedPreferences.Editor prefEditor = sharedPref.edit();
                prefEditor.putInt("s2", usersChoice);
                prefEditor.commit();
                if(sharedPref.contains("s2")){
                        Toast.makeText(MainActivity.this, "Pilihan dijadikan default", Toast.LENGTH_LONG).show();
                }
            }
        });

        listBahasa = new ArrayList<Bahasa>();
        final Drawable roundDrawable = getResources().getDrawable(R.drawable.selection_border);
        roundDrawable.setColorFilter(getResources().getColor(R.color.lightblue), PorterDuff.Mode.SRC_ATOP);
        final Drawable roundDrawable2 = getResources().getDrawable(R.drawable.selection_border);
        roundDrawable2.setColorFilter(getResources().getColor(R.color.darkblue), PorterDuff.Mode.SRC_ATOP);

        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            btnistilah.setBackgroundDrawable(roundDrawable);
            btnbahasa.setTextColor(getResources().getColor(R.color.white));
         } else {
            btnistilah.setBackground(roundDrawable);
            btnbahasa.setTextColor(getResources().getColor(R.color.white));
            divider = btnbahasa.getText().toString();
         }

         listBahasa.clear();
        new GetBahasaFromServer().execute();

        btnistilah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listBahasa.clear();
                new GetIstilahFromServer().execute();

                    btnbahasa.setBackground(roundDrawable);
                    btnistilah.setBackground(roundDrawable2);
                    btnistilah.setTextColor(getResources().getColor(R.color.white));
                    btnbahasa.setTextColor(getResources().getColor(R.color.black));
                    divider = btnistilah.getText().toString();

            }
        });
        btnbahasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listBahasa.clear();
                new GetBahasaFromServer().execute();

                    btnistilah.setBackground(roundDrawable);
                    btnbahasa.setBackground(roundDrawable2);
                    btnbahasa.setTextColor(getResources().getColor(R.color.white));
                    btnistilah.setTextColor(getResources().getColor(R.color.black));
                    divider = btnbahasa.getText().toString();

            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        Spinner spinner = (Spinner) adapterView;
        if(spinner.getId() == R.id.spinner2){
            if(listBahasa != null && listBahasa.size() != 0) {
                urls = listBahasa.get(position).getUrl();
                bhs = listBahasa.get(position).getTarget().toLowerCase();
                bahasa = listBahasa.get(position).getSource();
                bahasatgt = listBahasa.get(position).getTarget().toLowerCase();
                if(bahasatgt.equals("kbbi")){
                    bahasatgt = bahasatgt.toUpperCase();
                } else {
                    bahasatgt = bahasatgt.substring(0, 1).toUpperCase() + bahasatgt.substring(1);
                }

            }
            if(bhs.equals("kbbi")){
                titlebhs = bhs.toUpperCase();
            } else {
                titlebhs = bhs.substring(0, 1).toUpperCase() + bhs.substring(1);
            }
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    private class GetBahasaFromServer extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdialog.setMessage("Loading Data...");
            pdialog.show();
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

                            if(bObj.getString("to").length() > 3){
                                lcbahasa = bObj.getString("to").substring(0, 1).toUpperCase() + bObj.getString("to").substring(1);
                            } else {
                                lcbahasa = bObj.getString("to").toUpperCase();
                            }


                            Bahasa bahasa = new Bahasa(bObj.getString("from").toUpperCase(), lcbahasa, bObj.getString("uri"));
                            if (bObj.getString("from").equals("indo")){
                                bahasa.setSource("Indonesia");
                            } else if (bObj.getString("to").equals("indo")){
                                bahasa.setTarget("Indonesia");
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
                                bahasa.setSource("Indonesia");
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
            if(pdialog.isShowing()){
                pdialog.dismiss();
            }
            populateSpinnerModel();
        }

    }
    private void populateSpinnerModel(){
        lables = new ArrayList<>();
        for(int i = 0; i < listBahasa.size(); i++){
            lables.add(listBahasa.get(i).getTarget());
        }
        adapter2 = new ArrayAdapter<>(this, R.layout.spinner_item, lables);
        s2.setAdapter(adapter2);
        s2.setOnItemSelectedListener(this);
        defspinner = "Selayar";
        SharedPreferences sharedPref = getSharedPreferences("FileName",MODE_PRIVATE);
        int spinnerValue = sharedPref.getInt("s2", -1);
        if(sharedPref.contains("s2")){
            if(spinnerValue != -1) {
                // set the selected value of the spinner
                s2.setSelection(spinnerValue);
            }
        } else {
            int spinnerPosition = adapter2.getPosition(defspinner);
            s2.setSelection(spinnerPosition);
        }

    }

    private class GetIstilahFromServer extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdialog.setMessage("Loading Data...");
            pdialog.show();
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

                            if(bObj.getString("to").length() > 4){
                                lcbahasa = bObj.getString("to").substring(0, 1).toUpperCase() + bObj.getString("to").substring(1);
                            } else {
                                lcbahasa = bObj.getString("to").toUpperCase();
                            }


                            Bahasa bahasa = new Bahasa(bObj.getString("from").toUpperCase(), lcbahasa, bObj.getString("uri"));
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
                                bahasa.setSource("Indonesia");
                                listBahasa.add(bahasa);
                            } else if (bObj.getString("to").equals("indo")){
                                bahasa.setTarget("Indonesia");
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
            if(pdialog.isShowing()){
                pdialog.dismiss();
            }
            populateSpinner2Model();
        }

    }
    private void populateSpinner2Model(){
        istilah = new ArrayList<>();

        for(int i = 0; i < listBahasa.size(); i++){
            istilah.add(listBahasa.get(i).getTarget());
//            urls.add(listURL.get(i).getUrl());
        }

        adapter3 = new ArrayAdapter<>(this, R.layout.spinner_item, istilah);
        s2.setAdapter(adapter3);
        s2.setOnItemSelectedListener(this);
        defspinner = "KBBI";
        int spinnerPosition = adapter3.getPosition(defspinner);
        s2.setSelection(spinnerPosition);

    }

}


