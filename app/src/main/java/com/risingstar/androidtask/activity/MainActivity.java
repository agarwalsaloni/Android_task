package com.risingstar.androidtask.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.risingstar.androidtask.databases.DataEntity;
import com.risingstar.androidtask.databases.Database;
import com.risingstar.androidtask.model.ModelMain;
import com.risingstar.androidtask.R;
import com.risingstar.androidtask.adapter.MainAdapter;
import com.risingstar.androidtask.utility.ConnectionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    MainAdapter recyclerAdapter;
    Button btnClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //final TextView textView = (TextView) findViewById(R.id.text);

        setTitle("ASIA");

        recyclerView = findViewById(R.id.recyclerView);
        btnClear = findViewById(R.id.btnClear);
        layoutManager = new LinearLayoutManager(this);

        ArrayList<String> bordersList = new ArrayList<>();
        ArrayList<String> languageList = new ArrayList<>();


        ArrayList<ModelMain> dataList = new ArrayList<>();

        if (new ConnectionManager().checkConnectivity(getApplicationContext())){

            RequestQueue queue = Volley.newRequestQueue(this);
            String url ="https://restcountries.eu/rest/v2/all";

            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,url,null,

                    new Response.Listener<JSONArray>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onResponse(JSONArray response) {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject obj = response.getJSONObject(i);
                                    String region = obj.getString("region");
                                    if (region.compareToIgnoreCase("Asia") == 0) {
                                        String name = obj.getString("name");
                                        String capital = obj.getString("capital");
                                        String subregion = obj.getString("subregion");
                                        String population = obj.getString("population");
                                        String flag = obj.getString("flag");
                                        //textView.append("name " + name + "\ncapital " + capital +"\nregion "+region + "\nsubregion "+ subregion + "\npopulation "+ population + "\nflag " + flag+"\nborders ");
                                        JSONArray borders = obj.getJSONArray("borders");
                                        for (int a =0 ;a<borders.length();a++){
                                            bordersList.add(borders.getString(a));
                                            //textView.append(borders.getString(a) +"\n");
                                        }
                                        JSONArray languages = obj.getJSONArray("languages");
                                        for (int b = 0 ; b<languages.length();b++){
                                            languageList.add(languages.getJSONObject(b).getString("name"));
                                            //textView.append(languages.getJSONObject(b).getString("name") + "\n");
                                        }
                                        ModelMain eachData = new ModelMain(
                                                name,
                                                capital,
                                                region,
                                                subregion,
                                                population,
                                                flag,
                                                bordersList,
                                                languageList
                                        );
                                        dataList.add(eachData);
                                        recyclerAdapter = new MainAdapter(MainActivity.this,dataList);
                                        recyclerView.setAdapter(recyclerAdapter);
                                        recyclerView.setLayoutManager(layoutManager);
                                        DataEntity dataEntity = new DataEntity(
                                                name,
                                                capital,
                                                region,
                                                subregion,
                                                population,
                                                flag,
                                                bordersList,
                                                languageList
                                        );
                                        try {
                                            List<DataEntity> dataEntityList = new DBAsyncRetrieve().execute().get();
                                            if (dataEntityList.isEmpty()){
                                            if (new DBAsyncInsert(dataEntity).execute().get()){
                                                Toast.makeText(getApplicationContext().getApplicationContext(),"Data loaded in db",Toast.LENGTH_SHORT).show();
                                            }}

                                        } catch (ExecutionException e) {
                                            e.printStackTrace();
                                            Toast.makeText(getApplicationContext(),"Catch block 1 db insert",Toast.LENGTH_SHORT).show();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                            Toast.makeText(getApplicationContext(),"Catch block 2 db insert",Toast.LENGTH_SHORT).show();


                                        }
                                    }
                                } catch(JSONException e){
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(),"Catch block",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    },
                new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),"Error listener",Toast.LENGTH_SHORT).show();
                }
            });

            queue.add(jsonArrayRequest);
        }

        else {
            btnClear.setVisibility(View.VISIBLE);
            try {
                List<DataEntity> dataEntityList = new DBAsyncRetrieve().execute().get();
                for (int c= 0 ; c<dataEntityList.size();c++) {
                    dataList.add(
                            new ModelMain(dataEntityList.get(c).name,
                                    dataEntityList.get(c).capital,
                                    dataEntityList.get(c).region,
                                    dataEntityList.get(c).subRegion,
                                    dataEntityList.get(c).population,
                                    dataEntityList.get(c).flag,
                                    dataEntityList.get(c).borders,
                                    dataEntityList.get(c).languages
                            ));

                    recyclerAdapter = new MainAdapter(MainActivity.this.getApplicationContext(),dataList);
                    recyclerView.setAdapter(recyclerAdapter);
                    recyclerView.setLayoutManager(layoutManager);
                }
            } catch (ExecutionException e) {
                Toast.makeText(getApplicationContext(),"Catch block 1 db retrieve",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } catch (InterruptedException e) {
                Toast.makeText(getApplicationContext(),"Catch block 2 db retrieve",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            btnClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if(new DBAsyncClear().execute().get()){
                            Toast.makeText(getApplicationContext(),"Cleared DB",Toast.LENGTH_SHORT).show();}
                    } catch (ExecutionException e) {
                        Toast.makeText(getApplicationContext(),"Catch block 1 db clear",Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        Toast.makeText(getApplicationContext(),"Catch block 2 db clear",Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @SuppressLint("StaticFieldLeak")
    class DBAsyncRetrieve extends AsyncTask<Void,Void, List<DataEntity>>{
//        @SuppressLint("StaticFieldLeak")
//        Context context;

        Database db= Room.databaseBuilder(getApplicationContext(),Database.class,"app_db").build();


//        public DBAsyncRetrieve(@NonNull Context context) {
//            this.context = context;
//        }


        @Override
        protected List<DataEntity> doInBackground(Void... voids) {
            List<DataEntity> data = db.dataDao().getAllData();
            db.close();
            return data;
        }
    }

    @SuppressLint("StaticFieldLeak")
    class DBAsyncClear extends AsyncTask<Void,Void,Boolean>{

//        @SuppressLint("StaticFieldLeak")
//        Context context;
//
//        public DBAsyncClear(@NonNull Context context) {
//            this.context = context;
//        }

        Database db = Room.databaseBuilder(getApplicationContext(),Database.class,"app_db").build();


        @Override
        protected Boolean doInBackground(Void... voids) {
            db.dataDao().clearAll();
            db.close();
            return true;
        }
    }

    @SuppressLint("StaticFieldLeak")
    class DBAsyncInsert extends AsyncTask<Void,Void,Boolean>{
        //@SuppressLint("StaticFieldLeak")
        //Context context;
        DataEntity dataEntity;

        public DBAsyncInsert(DataEntity dataEntity) {
            this.dataEntity = dataEntity;
        }

        Database db = Room.databaseBuilder(getApplicationContext(),Database.class,"app_db").build();



        @Override
        protected Boolean doInBackground(Void... voids) {
            db.dataDao().insert(dataEntity);
            db.close();
            return true;
        }
    }

}


/*
 */