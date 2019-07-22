package com.example.mhmh2.creditcard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class Login extends AppCompatActivity {

    private EditText pswd, user;
    private TextView signup;
    private Button login;
    private String res,pwd,host;

    public static String usr;
    public static boolean isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = findViewById(R.id.login);
        user = (EditText) findViewById(R.id.usrusr);
        pswd = (EditText) findViewById(R.id.pswrdd);
        signup = (TextView) findViewById(R.id.signup);

        host=MainActivity.host;

        signup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent it = new Intent(Login.this, Signup.class);
                startActivity(it);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }
    private void login(){
        Log.d("Login", "#################### Login(): #######################");
        if(!validate()) {
            return;
        }

        String url = "http://" + host + ":8080/Cd/webresources/cd";
        Login.Asyn_task con = new Login.Asyn_task();
        con.execute(url);
    }


    public boolean validate() {
        boolean valid = true;

         usr = user.getText().toString();
         pwd = pswd.getText().toString();

        if (usr.isEmpty() || usr.length() < 3) {
            user.setError("!! Enter a valid user !!");
            valid = false;
        }else{
            user.setError(null);
        }

        if (pwd.isEmpty() || pwd.length() < 4 || pwd.length() > 10) {
            pswd.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            pswd.setError(null);
        }

        return valid;
    }

    public class Asyn_task extends AsyncTask<String, Void, String> {
        final ProgressDialog progressDialog = new ProgressDialog(Login.this, R.style.AppTheme_Dialog);

        public Asyn_task() {
            super();
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage(" Authenticating ...");
            progressDialog.show();
            super.onPreExecute();
        }

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
                obj.put("user", usr);
                obj.put("pswd", pwd);
                obj.put("mail", "");
                obj.put("phone", "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            System.out.println(obj.toString());

            try {
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestProperty("Content-Type", "application/json");

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

        @Override
        protected void onPostExecute(String s) {
            res=s;
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        }
                    }, 500);

            if(s!=null && !s.isEmpty()){
                if(s.equals("false")){
                    Toast.makeText(Login.this, "Login failed !!", Toast.LENGTH_LONG).show();
                    user.setError("!!  User or Password is incorect !!");
                }
                else if(s.equals("true")){

                    if(usr.equals("admin"))
                        isAdmin=true;
                    else isAdmin = false;

                    Toast.makeText(Login.this, "Welcome back : "+usr, Toast.LENGTH_LONG).show();
                    Intent it = new Intent(Login.this, Manager.class);
                    startActivity(it);
                }else
                    Toast.makeText(Login.this, "Authenticating failed !!", Toast.LENGTH_LONG).show();
            }else
                Toast.makeText(Login.this, "Authenticating failed !!", Toast.LENGTH_LONG).show();

            super.onPostExecute(s);
        }
    }
}
