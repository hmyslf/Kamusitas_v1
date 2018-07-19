package com.example.arham.kamusapps;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class KamusActivity extends AppCompatActivity {


    private ListView listview;
    private SearchView searchview;
    EditText et;
    ArrayList<Kamus> listAdapter;
    Intent intent;
    Bundle bundle;
    ArrayAdapter<String> adapter4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kamus);

        intent= getIntent();
        bundle = intent.getExtras();

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowTitleEnabled(true);
        String bhstarget = bundle.getString("bahasa");
        ab.setTitle(bhstarget);


        listview = findViewById(R.id.listView);
        searchview = findViewById(R.id.searchview);
        searchview.clearFocus();

        listAdapter = new ArrayList<Kamus>();
        int searchedit = searchview.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        et = searchview.findViewById(searchedit);
        et.selectAll();

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String word = listAdapter.get(position).getWord();
                String def = listAdapter.get(position).getDefinition();

                Intent i = new Intent(KamusActivity.this, DictionaryActivity.class);
                i.putExtra("word", word);
                i.putExtra("def", def);
                startActivity(i);
            }
        });

        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                listAdapter.clear();
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
                        JSONObject sumber = (JSONObject) jObj.get("data");
                        JSONArray arti = (JSONArray) sumber.get("arti");

                        if(arti != null && arti.length() > 0){

                            for (int i = 0; i < arti.length(); i++) {
                                JSONObject gObj = (JSONObject) arti.get(i);

                                Kamus kamus = new Kamus(gObj.getString("source"), gObj.getString("target"));
                                listAdapter.add(kamus);
                            }
                        } else {

                            Kamus notfound = new Kamus(jObj.getString("deskripsi"));
                            listAdapter.add(notfound);
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
        for(int i = 0; i < listAdapter.size(); i++){
            labels.add(listAdapter.get(i).getWord());
//           definition.add(listAdapter.get(i).getDefinition());
        }

        adapter4 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, labels);

        listview.setAdapter(adapter4);

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
