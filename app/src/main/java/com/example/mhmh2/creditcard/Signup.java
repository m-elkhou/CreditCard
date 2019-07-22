package com.example.mhmh2.creditcard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
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

public class Signup extends AppCompatActivity
{
    private EditText mail,mophone,pswd, user;
    private TextView lin;
    private Button sup;
    private String res,usr,pwd,email,phon,host;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        sup = (Button) findViewById(R.id.signup);
        lin = (TextView) findViewById(R.id.login);
        user = (EditText) findViewById(R.id.usrusr);
        pswd = (EditText) findViewById(R.id.pswrdd);
        mail = (EditText) findViewById(R.id.mail);
        mophone = (EditText) findViewById(R.id.mobphone);

        host = MainActivity.host;

        sup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validate()) return;

                Hosting();
            }
        });
        lin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent it = new Intent(Signup.this,Login.class);
                startActivity(it);
            }
        });
    }

    public boolean validate() {
        boolean valid = true;

        usr = user.getText().toString();
        pwd = pswd.getText().toString();
        phon = mophone.getText().toString();
        email = mail.getText().toString();

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

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mail.setError("enter a valid email address");
            valid = false;
        } else {
            mail.setError(null);
        }

        if (phon.isEmpty() || !Patterns.PHONE.matcher(phon).matches()) {
            mophone.setError("enter a valid email address");
            valid = false;
        } else {
            mophone.setError(null);
        }

        return valid;
    }

    private  void Hosting(){
        String url = "http://" + host + ":8080/Cd/webresources/cd";
        Signup.Asyn_task con = new Signup.Asyn_task();
        con.execute(url);
    }

    public class Asyn_task extends AsyncTask<String, Void, String> {

        final ProgressDialog progressDialog = new ProgressDialog(Signup.this, R.style.AppTheme_Dialog);
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
                obj.put("mail", email);
                obj.put("phone", phon);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            System.out.println(obj.toString());

            try {
                conn.setRequestMethod("PUT");
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
                    }, 1000);

            if(s!=null && !s.isEmpty()){
                if(s.equals("false")){
                    Toast.makeText(Signup.this, "SignUp failed !!", Toast.LENGTH_SHORT).show();
                    mail.setError("!!  User or Password is incorect !!");
                }
                else if(s.equals("true")){
                    //welcome back commander
                    Toast.makeText(Signup.this, "Welcome Mr : "+usr, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Signup.this, Login.class);
                    Bundle b = new Bundle();
                    b.putString("user", usr);
                    b.putString("pswd", pwd);
                    intent.putExtra("signup", b);
                    startActivity(intent);
                }else{
                    Toast.makeText(Signup.this, "Authenticating failed !!", Toast.LENGTH_LONG).show();
                }
            }else
                Toast.makeText(Signup.this, "Authenticating failed !!", Toast.LENGTH_LONG).show();

            super.onPostExecute(s);
        }
    }
}
