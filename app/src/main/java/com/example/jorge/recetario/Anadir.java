package com.example.jorge.recetario;


import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Xml;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Anadir extends Activity{
    private ArrayList<Receta> alReceta = new ArrayList<Receta>();
    private EditText et1, et2;
    private Spinner sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialogo_anadir);

        Bundle b = getIntent().getExtras();
        alReceta = b.getParcelableArrayList("recetas");

        Spinner spinner = (Spinner) this.findViewById(R.id.sTipo);
        ArrayAdapter<CharSequence> adaptador = ArrayAdapter.createFromResource(this, R.array.tipos,
                android.R.layout.simple_spinner_item);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adaptador);
        et1 = (EditText) this.findViewById(R.id.etNombre);
        et2 = (EditText) this.findViewById(R.id.etDescripcion);
        sp = (Spinner) this.findViewById(R.id.sTipo);
    }

    public void anadir(View v) {
        final Receta receta = new Receta();
        try {
            String nom = et1.getText().toString();
            String desc = et2.getText().toString();
            String tipo = String.valueOf(sp.getSelectedItem());
            receta.setNombre(nom);
            receta.setDescri(desc);
            receta.setTipo(tipo);
            if (tipo.equals("Entrante")) {
                receta.setImg("entrante");
                alReceta.add(receta);
            } else if (tipo.equals("Plato")) {
                receta.setImg("plato");
                alReceta.add(receta);
            } else if (tipo.equals("Postre")) {
                receta.setImg("postre");
                alReceta.add(receta);
            }
            tostada("Receta Añadida");
            escribirXML();
            Intent i = new Intent(this,Principal.class);
            startActivity(i);
        }catch (Exception e){
            tostada("No se ha podido añadir");
        }
    }

    private void escribirXML(){
        FileOutputStream fxml= null;

        try {
            fxml = new FileOutputStream(new File(getExternalFilesDir(null),"archivo.xml"));
            XmlSerializer docxml = Xml.newSerializer();
            docxml.setOutput(fxml, "UTF-8");
            docxml.startDocument(null, Boolean.valueOf(true));
            docxml.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            docxml.startTag(null, "recetas");
            for (int i = 0; i < alReceta.size() ; i++) {
                docxml.startTag(null, "receta");
                docxml.startTag(null, "nombre");
                docxml.text(alReceta.get(i).getNombre());
                docxml.endTag(null, "nombre");
                docxml.startTag(null, "descripcion");
                docxml.text(alReceta.get(i).getDescri());
                docxml.endTag(null, "descripcion");
                docxml.startTag(null, "tipo");
                docxml.text(alReceta.get(i).getTipo());
                docxml.endTag(null, "tipo");
                docxml.startTag(null, "foto");
                docxml.text(alReceta.get(i).getImg());
                docxml.endTag(null, "foto");
                docxml.endTag(null,"receta");
            }
            docxml.endTag(null,"recetas");
            docxml.endDocument();
            docxml.flush();
            fxml.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void tostada(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
