package com.example.mhmh2.creditcard;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

import com.owlike.genson.Genson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

public class ListCard extends AppCompatActivity {

    private String host = "192.168.1.10";
    private ArrayList<CreditCard> arl;
    private ListView listView;
    private AdaptateurPersonnalise adapter;
    Bundle b;
    String ob;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_card);
        listView = findViewById(R.id.listView);

        arl = new ArrayList<CreditCard>();

        adapter = new AdaptateurPersonnalise(this,R.layout.layout_credit_card,arl);
        listView.setAdapter(adapter);

        Intent intent = getIntent();
        if(intent != null) {
            b = intent.getBundleExtra("b");
            host = b.getString("host");
        }

        new Thread(new Runnable(){
            public void run(){
                HttpURLConnection urlConnection = null;
                try{
                    URL url = new URL("http://"+host+":8080/Cd/webresources/cd/all");
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setReadTimeout(10000);
                    urlConnection.setConnectTimeout(15000);
                    urlConnection.setRequestMethod("GET");

                    InputStream is =urlConnection.getInputStream();
                    BufferedReader in = new BufferedReader(new InputStreamReader(is));
                    Scanner scaner = new Scanner(in);
                    ob = scaner.nextLine();
                    runOnUiThread(new Runnable(){
                        @Override
                        public void run(){
                            arl.clear();
                            try {
                                JSONArray tous_enrg = new JSONArray(ob);
                                for(int i=0; i<tous_enrg.length();i++){

                                    JSONObject elem = tous_enrg.getJSONObject(i);

                                    long id = Long.parseLong(elem.getString("id"));
                                    String number = elem.getString("number");
                                    String expiryDate = elem.getString("expiryDate");
                                    int controlNumber = Integer.parseInt(elem.getString("controlNumber"));
                                    String type = elem.getString("type");

                                    CreditCard card =new CreditCard( id, number,expiryDate, controlNumber, type);

                                    arl.add(card);
                                }
                                adapter.notifyDataSetChanged();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            adapter.notifyDataSetChanged();
                        }
                    });

                    in.close();
                }catch( Exception e){
                    Log.e("Exception-JSON", "!! EREUR !!!!", e);
                    if( urlConnection != null) urlConnection.disconnect();
                }
            }
        }).start();
    }
}
