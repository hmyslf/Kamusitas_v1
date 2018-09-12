package com.kamusitas.psi.kamusapps;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnItemSelectedListener{

    ProgressDialog pdialog;
    ArrayList<Kamus> listAdaptMain, listAdapter2;
    ArrayAdapter adapter2, adapter3;
    Button btn1, btnistilah, btnbahasa;
    String urls, bhs, defspinner, divider, titlebhs, bahasa, bahasatgt, lcbahasa, word, def, txt_kamus, txt_kamus2;
    ImageView copyright, appslogo;
    TextView kmstxt, kmstxt2;
    EditText et;
    SearchView sv1;
    public static Context context;
    ArrayAdapter<String> adapt2, adapt48;
    ArrayList<String> istilah, lables;
    private Spinner s2;
    private ArrayList<Bahasa> listBahasa;
    private ListView listview48, listview2;
    private String url_getbahasa = "https://api.prime-strategy.co.id/kamusitas/v1/kamus";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //set splash screen time
        setTheme(R.style.SplashScreen);
        try {
            Thread.sleep(2000);
        } catch(InterruptedException e){
            e.printStackTrace();
        }
        super.onCreate(savedInstanceState);
        MainActivity.context = getApplicationContext();
        setContentView(R.layout.activity_main);
        // Layout initiation
        s2 = findViewById(R.id.spinner2);
        sv1 = findViewById(R.id.sv1);
        listview48 = findViewById(R.id.listview1);
        sv1.clearFocus();
        listview2 = findViewById(R.id.listView2);
        int searchedit = sv1.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        et = findViewById(searchedit);
        et.setTextColor(getResources().getColor(R.color.black));
        et.setHintTextColor(getResources().getColor(R.color.black));
        int src_plate = sv1.getContext().getResources().getIdentifier("android:id/search_plate", null, null);
        View searchplate = findViewById(src_plate);
        searchplate.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY);
        kmstxt = findViewById(R.id.kamustxt);
        kmstxt.setTextColor(getResources().getColor(R.color.black));
        kmstxt2 = findViewById(R.id.kamustxt2);
        kmstxt2.setTextColor(getResources().getColor(R.color.black));
        btnistilah = findViewById(R.id.btnistilah);
        btnbahasa = findViewById(R.id.btn_bhs);
        copyright = findViewById(R.id.copyright);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Chivo.ttf");
        btnistilah.setTypeface(custom_font, Typeface.BOLD);
        btnbahasa.setTypeface(custom_font, Typeface.BOLD);
        pdialog = new ProgressDialog(this);
        // Set onclick to footer logo
        copyright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent();
                intent2.setAction(Intent.ACTION_VIEW);
                intent2.addCategory(Intent.CATEGORY_BROWSABLE);
                intent2.setData(Uri.parse("https://prime-strategy.co.id/"));
                startActivity(intent2);
            }
        });
        //initiate and set onclick header logo
        appslogo = findViewById(R.id.appslogo);
        appslogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainsite = new Intent();
                mainsite.setAction(Intent.ACTION_VIEW);
                mainsite.addCategory(Intent.CATEGORY_BROWSABLE);
                mainsite.setData(Uri.parse("https://kamusitas.com"));
                startActivity(mainsite);
            }
        });
        // initiate arraylist for spinner
        lables = new ArrayList<>();
        listAdaptMain = new ArrayList<>();
        listAdapter2 = new ArrayList<>();
        listBahasa = new ArrayList<>();
        //change button color with onclick
        final Drawable roundDrawable = getResources().getDrawable(R.drawable.selection_border);
        roundDrawable.setColorFilter(getResources().getColor(R.color.darkblue), PorterDuff.Mode.SRC_ATOP);
        final Drawable roundDrawable2 = getResources().getDrawable(R.drawable.selection_border);
        roundDrawable2.setColorFilter(getResources().getColor(R.color.orange), PorterDuff.Mode.SRC_ATOP);

        btnistilah.setBackground(roundDrawable);
        btnbahasa.setBackground(roundDrawable2);
        btnbahasa.setTextColor(getResources().getColor(R.color.white));
        divider = btnbahasa.getText().toString();

        // clear spinner items and populate spinner items
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

        sv1.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                listAdaptMain.clear();
                listAdapter2.clear();
                sv1.clearFocus();
                if (bahasatgt.equals("Adsense") || bahasatgt.equals("Komputer")
                        || bahasatgt.equals("KBBI")|| bahasatgt.equals("Akuntansi")
                        || bahasatgt.equals("Bahasa-gaul-singkatan-inggris")
                        || bahasatgt.equals("Sansekerta")
                        || bahasatgt.equals("Blogger")
                        || bahasatgt.equals("Politik")
                        || bahasatgt.equals("Geografi")
                        || bahasatgt.equals("Kesehatan")
                        || bahasatgt.equals("Kamuskita")
                        || bahasatgt.equals("Sms-gaul")
                        || bahasatgt.equals("Tokoh")
                        || bahasatgt.equals("Jaringan")
                        || bahasatgt.equals("Geologi")
                        || bahasatgt.equals("Perbankan")
                        || bahasatgt.equals("Padanan-komputer")
                        || bahasatgt.equals("Persahabatan")) {
                    kmstxt.setVisibility(View.VISIBLE);
                    listview48.setVisibility(View.VISIBLE);
                    kmstxt2.setVisibility(View.GONE);
                    listview2.setVisibility(View.GONE);
                } else {
                    kmstxt.setVisibility(View.VISIBLE);
                    listview48.setVisibility(View.VISIBLE);
                    kmstxt2.setVisibility(View.VISIBLE);
                    listview2.setVisibility(View.VISIBLE);
                }
                new GetDataFromServer().execute();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                return false;
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
            sv1.setQuery("", true);
            sv1.clearFocus();
            kmstxt.setVisibility(View.GONE);
            listview48.setVisibility(View.GONE);
            kmstxt2.setVisibility(View.GONE);
            listview2.setVisibility(View.GONE);
            txt_kamus = bahasa + "-" + bahasatgt;
            txt_kamus2 = bahasatgt + "-" + bahasa;
            kmstxt.setText(txt_kamus);
            kmstxt2.setText(txt_kamus2);
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

    private void populateListView(){
        ArrayList<String> labels4 = new ArrayList<>();
        ArrayList<String> labels2 = new ArrayList<>();
        for(int i = 0; i < listAdaptMain.size(); i++){
            labels4.add(listAdaptMain.get(i).getWord());
//           definition.add(listAdapter.get(i).getDefinition());
        }
        for(int i = 0; i < listAdapter2.size(); i++){
            labels2.add(listAdapter2.get(i).getWord());
//           definition.add(listAdapter.get(i).getDefinition());
        }

        if(listAdaptMain.get(0).getWord().contains("tidak ditemukan dalam kamus") && listAdapter2.get(0).getWord().contains("tidak ditemukan dalam kamus")){
            adapt48 = new ArrayAdapter<>(this, R.layout.list_black_text2, R.id.list_content2, labels4);

            adapt2 = new ArrayAdapter<>(this, R.layout.list_black_text2, R.id.list_content2, labels2);
        } else if(listAdaptMain.get(0).getWord().contains("tidak ditemukan dalam kamus")) {
            adapt48 = new ArrayAdapter<>(this, R.layout.list_black_text2, R.id.list_content2, labels4);

            adapt2 = new ArrayAdapter<>(this, R.layout.list_black_text, R.id.list_content, labels2);
        } else if(listAdapter2.get(0).getWord().contains("tidak ditemukan dalam kamus")){
            adapt48 = new ArrayAdapter<>(this, R.layout.list_black_text, R.id.list_content, labels4);

            adapt2 = new ArrayAdapter<>(this, R.layout.list_black_text2, R.id.list_content2, labels2);
        } else {
            adapt48 = new ArrayAdapter<>(this, R.layout.list_black_text, R.id.list_content, labels4);

            adapt2 = new ArrayAdapter<>(this, R.layout.list_black_text, R.id.list_content, labels2);

        }

        if(listAdaptMain.get(0).getWord().contains("tidak ditemukan dalam kamus")){
            listview48.setOnItemClickListener(null);
        } else {
            listview48.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    word = "";
                    def = "";
                    if(bahasatgt.equals("KBBI")) {
                        word = String.valueOf(Html.fromHtml(listAdaptMain.get(position).getWord()));

                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            def = String.valueOf(Html.fromHtml(listAdaptMain.get(position).getDefinition(), Html.FROM_HTML_MODE_COMPACT));
                        } else {
                            def = String.valueOf(Html.fromHtml(listAdaptMain.get(position).getDefinition()));
                        }
                    } else {
                        word = listAdaptMain.get(position).getWord();
                        def = listAdaptMain.get(position).getDefinition();
                    }
                    Intent i = new Intent(MainActivity.this, DictionaryActivity.class);
                    i.putExtra("word", word);
                    i.putExtra("def", def);
                    i.putExtra("kamustxt", txt_kamus);
                    startActivity(i);
                }
            });

        }

        if (listAdapter2.get(0).getWord().contains("tidak ditemukan dalam kamus")) {
            listview2.setOnItemClickListener(null);
        } else {
            listview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                    word = listAdapter2.get(position).getWord();
                    def = listAdapter2.get(position).getDefinition();
                    Intent inten2 = new Intent(MainActivity.this, DictionaryActivity2.class);
                    inten2.putExtra("word2", word);
                    inten2.putExtra("def2", def);
                    inten2.putExtra("kamustxt", txt_kamus2);
                    startActivity(inten2);
                }
            });
        }


        listview48.setAdapter(adapt48);
        listview2.setAdapter(adapt2);


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

    private class GetDataFromServer extends AsyncTask<Void, Void, Void> {

        private String sv = sv1.getQuery().toString();

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdialog.setMessage("Loading Data...");
            pdialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            Handler searchParser = new Handler();

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("kata", sv));
            String kamusjson = searchParser.makeServiceCall(urls, Handler.POST, params);

            Log.e("Response: ", "> " + kamusjson);

            if(kamusjson != null) {
                try {
                    JSONObject jObj = new JSONObject(kamusjson);

                        JSONObject sumber = (JSONObject) jObj.get("0");
                        JSONObject target = (JSONObject) jObj.get("1");
                        JSONObject data2 = (JSONObject) target.get("data");
                        JSONArray arti2 = (JSONArray) data2.get("arti");
                        JSONObject data = (JSONObject) sumber.get("data");
                        JSONArray arti = (JSONArray) data.get("arti");


                        if(arti != null && arti.length() > 0){

                            for (int i = 0; i < arti.length(); i++) {
                                JSONObject gObj = (JSONObject) arti.get(i);

                                Kamus kamus = new Kamus(gObj.getString("source"), gObj.getString("target"));

                                listAdaptMain.add(kamus);
                            }
                        } else {

                            Kamus notfound = new Kamus(sumber.getString("deskripsi"));
                            listAdaptMain.add(notfound);
                        }
                        if(arti != null && arti2.length() > 0){

                            for (int i = 0; i < arti2.length(); i++) {
                                JSONObject gObj = (JSONObject) arti2.get(i);

                                Kamus kamus = new Kamus(gObj.getString("source"), gObj.getString("target"));
                                listAdapter2.add(kamus);
                            }
                        } else {

                            Kamus notfound = new Kamus(target.getString("deskripsi"));
                            listAdapter2.add(notfound);
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
            populateListView();
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

}


