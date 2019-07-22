package com.example.mhmh2.creditcard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;


public class MainActivity extends AppCompatActivity {

    private EditText hostEditText;
    private Button connectButton;
    public static String host;
    private String res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hostEditText = findViewById(R.id.host);
        connectButton = findViewById(R.id.connectButton);

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validate()) return;
                Hosting();
            }
        });
    }

    public boolean validate() {
        host = hostEditText.getText().toString();

        if (host.isEmpty() || !Patterns.IP_ADDRESS.matcher(host).matches()) {
            hostEditText.setError("enter a valid host");
            return false;
        }else{
            hostEditText.setError(null);
        }
        return true;
    }

    private  void Hosting(){
        res="true";
        new Thread(new Runnable() {
            public void run() {
                HttpURLConnection urlConnection = null;

                try {
                    URL url = new URL("http://" + host + ":8080/Cd/webresources/cd/");
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setReadTimeout(10000);
                    urlConnection.setConnectTimeout(15000);
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setDoInput(true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    Scanner scaner = new Scanner(in);
                    res =scaner.nextLine();

                    in.close();
                } catch (Exception e) {
                    Log.e("Exception-JSON", "!! EREUR !!!!", e);
                    if (urlConnection != null) urlConnection.disconnect();
                    res="false";
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(res!=null && !res.isEmpty()){
                            if(res.equals("false")){
                                Toast.makeText(MainActivity.this, "Hosting failed !!", Toast.LENGTH_LONG).show();
                                hostEditText.setError("change the host");
                            }
                            else if(res.equals("true")){
                                Toast.makeText(MainActivity.this, "Welcome", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(MainActivity.this, Login.class);
                                Bundle b = new Bundle();
                                b.putString("host", host);
                                intent.putExtra("b", b);
                                startActivity(intent);
                            }else {
                                Toast.makeText(MainActivity.this, "Authenticating failed !!", Toast.LENGTH_LONG).show();
                                hostEditText.setError("change the host");
                            }
                        }else {
                            Toast.makeText(MainActivity.this, "Authenticating failed !!", Toast.LENGTH_LONG).show();
                            hostEditText.setError("change the host");
                        }
                    }
                });
            }
        }).start();
    }

}
