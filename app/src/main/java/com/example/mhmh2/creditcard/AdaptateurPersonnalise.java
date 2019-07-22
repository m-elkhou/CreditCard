package com.example.mhmh2.creditcard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class AdaptateurPersonnalise extends ArrayAdapter<CreditCard> {
    private Context context;
    private int resource;
    private ArrayList <CreditCard> arl;
    private int p=0;

    public int getP() {
        return p;
    }

    public AdaptateurPersonnalise(Context context, int resource, ArrayList<CreditCard> objects) {
        super(context, resource, objects);

        this.arl = objects;
        this.resource= resource;
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view =  inflater.inflate(resource,parent,false);

        TextView textNumber = view.findViewById(R.id.number);
        TextView textControlNumber = view.findViewById(R.id.controlNumber);
        TextView textType = view.findViewById(R.id.type);

        CreditCard creditCard = arl.get(position);

        textNumber.setText(""+creditCard.getNumber());
        textControlNumber.setText(""+creditCard.getControlNumber());
        textType.setText(""+creditCard.getType());

        ImageView img = view.findViewById(R.id.imageView);

        String name = getImgType(creditCard.getType());
        Uri uri = Uri.parse("android.resource://" + context.getResources().getResourcePackageName(R.drawable.img) + "/drawable/"+name);
        img.setImageURI(uri);
        return view;
    }

    public static String getImgType(String type){
        if(type.equals("VISA")) return "visa";
        else if(type.equals("MASTERCARD")) return "mastercard";
        else if(type.equals("AMEX")) return "amex";
        else if(type.equals("DINERS")) return "diners";
        else if(type.equals("HIPERCARD")) return "hipercard";
        else if(type.equals("ELO")) return "elo";
        else if(type.equals("HIPER")) return "hiper";
        return "img" ;
    }
}
