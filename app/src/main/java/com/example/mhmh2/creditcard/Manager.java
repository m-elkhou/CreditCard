package com.example.mhmh2.creditcard;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.owlike.genson.Genson;

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
import java.util.Scanner;

public class Manager extends AppCompatActivity {
    private ImageView img;
    private EditText idEditText, numberEditText, expiryDateEditText, controlNumberEditText, typeEditText;
    private Button GetButton, validButton, clearButton, showAllButton, insertButton;
    private String host;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        img = findViewById(R.id.img);
        idEditText = findViewById(R.id.idGET);
        GetButton = findViewById(R.id.get);
        numberEditText = findViewById(R.id.numberEditText);
        expiryDateEditText = findViewById(R.id.expiryDateEditText);
        controlNumberEditText = findViewById(R.id.controlNumberEditText);
        typeEditText = findViewById(R.id.typeEditText);
        validButton = findViewById(R.id.Validate);
        clearButton = findViewById(R.id.Clear);
        showAllButton = findViewById(R.id.ShowAll);
        insertButton = findViewById(R.id.insert);

        host = MainActivity.host;
        Log.i("isAdmin","Admin ############### "+Login.usr+" ## isAdmin : "+Login.isAdmin);
        if(Login.isAdmin){
            showAllButton.setVisibility(View.VISIBLE);
            showAllButton.setEnabled(true);
        }else{
            showAllButton.setVisibility(View.INVISIBLE);
            showAllButton.setVisibility(View.GONE);
            showAllButton.setEnabled(false);
        }

        GetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = idEditText.getText().toString();

                if(id.isEmpty()){
                    idEditText.setError("enter id");
                    return ;
                } else{
                    idEditText.setError(null);
                }

                new Thread(new Runnable() {
                    public void run() {
                        HttpURLConnection urlConnection = null;
                        try {
                            URL url = new URL("http://" + host + ":8080/Cd/webresources/cd/" + idEditText.getText());
                            urlConnection = (HttpURLConnection) url.openConnection();
                            urlConnection.setRequestMethod("GET");

                            InputStream is = urlConnection.getInputStream();
                            BufferedReader in = new BufferedReader(new InputStreamReader(is));
                            Scanner scaner = new Scanner(in);
                            Log.i(" BufferedReader", "Result ==" + in.toString());
                            Log.i("scaner", "Result ==" + scaner.toString());

                            final CreditCard creditCard = new Genson().deserialize(scaner.nextLine(), CreditCard.class);

                            Log.i("Exchange-JSON", "Result ==" + creditCard);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    numberEditText.setText("" + creditCard.getNumber());
                                    expiryDateEditText.setText("" + creditCard.getExpiryDate());
                                    controlNumberEditText.setText("" + creditCard.getControlNumber());
                                    typeEditText.setText("" + creditCard.getType());

                                    String name = AdaptateurPersonnalise.getImgType(creditCard.getType());
                                    Uri uri = Uri.parse("android.resource://" + getBaseContext().getResources().getResourcePackageName(R.drawable.img) + "/drawable/"+name);
                                    img.setImageURI(uri);
                                    new android.os.Handler().postDelayed(
                                            new Runnable() {
                                                public void run() {
                                                    Uri uri = Uri.parse("android.resource://" + getBaseContext().getResources().getResourcePackageName(R.drawable.img) + "/drawable/card");
                                                    img.setImageURI(uri);
                                                }
                                            }, 3000);
                                }
                            });
                            in.close();
                        } catch (Exception e) {
                            Log.e("Exception-JSON", "!! EREUR !!!!", e);
                            if (urlConnection != null) urlConnection.disconnect();
                        }
                    }
                }).start();
            }
        });

        validButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validate())
                    return;

                String url = "http://" + host + ":8080/Cd/webresources/cd/valid";
                Manager.Asyn_task con = new Manager.Asyn_task();
                con.execute(url);
            }
        });

        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validate())
                    return;

                String url = "http://" + host + ":8080/Cd/webresources/cd/create";
                Manager.Asyn_task con = new Manager.Asyn_task();
                con.execute(url);
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idEditText.setText("");
                numberEditText.setText("");
                expiryDateEditText.setText("");
                controlNumberEditText.setText("");
                typeEditText.setText("");
            }
        });

        showAllButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Manager.this, ListCard.class);
                Bundle b = new Bundle();
                b.putString("host", host.toString());
                intent.putExtra("b", b);
                startActivity(intent);
            }
        });
    }

    public class Asyn_task extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            URL url = null;
            try {
                url = new URL(strings[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String res = null;
            JSONObject obj = new JSONObject();

            try {
                obj.put("id", Long.parseLong(idEditText.getText().toString()));
                obj.put("number", numberEditText.getText().toString());
                obj.put("expiryDate", expiryDateEditText.getText().toString());
                obj.put("controlNumber", Integer.parseInt(controlNumberEditText.getText().toString()));
                obj.put("type", typeEditText.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestProperty("Content-Type", "application/json");

                System.out.println(obj.toString());

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(obj.toString());
                writer.flush();
                writer.close();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                res = reader.readLine();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return res;
        }

        public Asyn_task() {
            super();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            if(s!=null && !s.isEmpty()){
                if(s.equals("false")){
                    Toast.makeText(Manager.this, "Operation failed !!", Toast.LENGTH_LONG).show();
                    idEditText.setError("!!  Data is incorect !!");
                }
                else if(s.equals("true")){
                    String name = AdaptateurPersonnalise.getImgType(typeEditText.getText().toString());
                    Uri uri = Uri.parse("android.resource://" + getBaseContext().getResources().getResourcePackageName(R.drawable.img) + "/drawable/"+name);
                    img.setImageURI(uri);
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    Uri uri = Uri.parse("android.resource://" + getBaseContext().getResources().getResourcePackageName(R.drawable.img) + "/drawable/card");
                                    img.setImageURI(uri);
                                }
                            }, 3000);

                    Toast.makeText(Manager.this, "Operation Successful ", Toast.LENGTH_LONG).show();
                }else
                    Toast.makeText(Manager.this, "Authenticating failed !!", Toast.LENGTH_LONG).show();
            }else
                Toast.makeText(Manager.this, "Authenticating failed !!", Toast.LENGTH_LONG).show();

            super.onPostExecute(s);
        }
    }

    public boolean validate() {
        boolean valid = true;

        String id = idEditText.getText().toString();
        String number = numberEditText.getText().toString();
        String expiryDate = expiryDateEditText.getText().toString();
        String controlNumber = controlNumberEditText.getText().toString();
        String type = typeEditText.getText().toString();

        if (host.isEmpty() || !Patterns.IP_ADDRESS.matcher(host).matches()) {
            idEditText.setError("enter a valid host");
            valid = false;
        } else {
            idEditText.setError(null);
        }

        if (id.isEmpty()) {
            idEditText.setError("at least 1 characters");
            valid = false;
        } else {
            idEditText.setError(null);
        }

        if (number.isEmpty() || number.length() != 16) {
            numberEditText.setError("enter a valid number");
            valid = false;
        } else {
            numberEditText.setError(null);
        }

        if (expiryDate.isEmpty() || expiryDate.length() != 5) {
            expiryDateEditText.setError("month/year exmp:01/19");
            valid = false;
        } else {
            expiryDateEditText.setError(null);
        }

        if (controlNumber.isEmpty()) {
            controlNumberEditText.setError("enter controlNumber");
            valid = false;
        } else {
            controlNumberEditText.setError(null);
        }

        if (type.isEmpty()) {
            typeEditText.setError("enter a valid type");
            valid = false;
        } else {
            typeEditText.setError(null);
        }

        return valid;
    }
}
