package com.example.arham.kamusapps;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class KamusActivity extends AppCompatActivity {


    private ListView listview, listview2;
    private SearchView searchview;
    EditText et;
    ArrayList<Kamus> listAdapter, listAdapter2;
    Intent intent;
    TextView kamustxt, kamustxt2;
    String def, divider, bhstarget, bahasa, bahasatgt, txt_kamus, txt_kamus2, word, word2, def2;
    Bundle bundle;
    ArrayAdapter<String> adapter4, adapter5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kamus);

        intent= getIntent();
        bundle = intent.getExtras();

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowTitleEnabled(true);
        bhstarget = bundle.getString("bahasa");
        divider = bundle.getString("divider");
        ab.setTitle(divider + "\n" + bhstarget);


        listview = findViewById(R.id.listView);
        listview2 = findViewById(R.id.listView2);
        searchview = findViewById(R.id.searchview);
        searchview.clearFocus();
        kamustxt = findViewById(R.id.kamustxt);
        kamustxt2 = findViewById(R.id.kamustxt2);
        bahasa = bundle.getString("bhs");
        bahasatgt = bundle.getString("bhstgt");

        txt_kamus = bahasa + "-" + bahasatgt;
        txt_kamus2 = bahasatgt + "-" + bahasa;
        kamustxt.setText(txt_kamus);
        kamustxt2.setText(txt_kamus2);
        listAdapter = new ArrayList<>();
        listAdapter2 = new ArrayList<>();
        int searchedit = searchview.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        et = searchview.findViewById(searchedit);
        et.selectAll();

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                word = "";
                def= "";
                if(bahasatgt.equals("KBBI")) {
                    word = String.valueOf(Html.fromHtml(listAdapter.get(position).getWord()));

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        def = String.valueOf(Html.fromHtml(listAdapter.get(position).getDefinition(), Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        def = String.valueOf(Html.fromHtml(listAdapter.get(position).getDefinition()));
                    }
                } else {
                    word = listAdapter.get(position).getWord();
                    def = listAdapter.get(position).getDefinition();
                }
                Intent i = new Intent(KamusActivity.this, DictionaryActivity.class);
                i.putExtra("word", word);
                i.putExtra("def", def);
                i.putExtra("kamustxt", txt_kamus);
                startActivity(i);
            }
        });


        listview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                word2 = listAdapter2.get(position).getWord();
                def2 = listAdapter2.get(position).getDefinition();

                Intent inten2 = new Intent(KamusActivity.this, DictionaryActivity2.class);
                inten2.putExtra("word2", word2);
                inten2.putExtra("def2", def2);
                inten2.putExtra("kamustxt", txt_kamus2);
                startActivity(inten2);
            }
        });


        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                listAdapter.clear();
                listAdapter2.clear();
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
                    kamustxt.setVisibility(View.VISIBLE);
                    listview.setVisibility(View.VISIBLE);
                    kamustxt2.setVisibility(View.GONE);
                    listview2.setVisibility(View.GONE);
                } else {
                    kamustxt.setVisibility(View.VISIBLE);
                    listview.setVisibility(View.VISIBLE);
                    kamustxt2.setVisibility(View.VISIBLE);
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
    protected void onResume(){
        super.onResume();
    }


    private class GetDataFromServer extends AsyncTask<Void, Void, Void> {

        private String sv = searchview.getQuery().toString();

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
//            pDiaLog = new ProgressDialog(KamusActivity.this);
//            pDiaLog.setMessage("Fetching Data");
//            pDiaLog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {


            final String url_getdata = bundle.getString("URL");

            Handler kamusParser = new Handler();

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("kata", sv));
            String kamusjson = kamusParser.requestData(url_getdata, Handler.POST, params);

            Log.e("Response: ", "> " + kamusjson);

            if(kamusjson != null) {
                try {
                    JSONObject jObj = new JSONObject(kamusjson);

                    if (kamusjson != null){
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

                                listAdapter.add(kamus);
                            }
                        } else {

                            Kamus notfound = new Kamus(sumber.getString("deskripsi"));
                            listAdapter.add(notfound);
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
//                pDiaLog.dismiss();
            populateListView();
        }

    }
    private void populateListView(){
        ArrayList<String> labels = new ArrayList<String>();
        ArrayList<String> labels2 = new ArrayList<>();
        for(int i = 0; i < listAdapter.size(); i++){
            labels.add(listAdapter.get(i).getWord());
//           definition.add(listAdapter.get(i).getDefinition());
        }
        for(int i = 0; i < listAdapter2.size(); i++){
            labels2.add(listAdapter2.get(i).getWord());
//           definition.add(listAdapter.get(i).getDefinition());
        }

        adapter4 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, labels);
        adapter5 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, labels2);
        listview.setAdapter(adapter4);
        listview2.setAdapter(adapter5);

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
