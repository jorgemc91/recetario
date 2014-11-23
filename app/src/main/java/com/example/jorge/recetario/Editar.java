package com.example.jorge.recetario;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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


public class Editar extends Activity {
    private ArrayList<Receta> alReceta = new ArrayList<Receta>();
    private EditText et1, et2;
    private Spinner sp;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialogo_anadir);
        String nom, desc, tipo;
        Bundle rec = getIntent().getExtras();
        alReceta = rec.getParcelableArrayList("recetas");
        index = rec.getInt("id");
        nom = rec.getString("nombre");
        desc = rec.getString("descripcion");
        tipo = rec.getString("tipo");
        et1 = (EditText) this.findViewById(R.id.etNombre);
        et2 = (EditText) this.findViewById(R.id.etDescripcion);
        sp = (Spinner) this.findViewById(R.id.sTipo);
        ArrayAdapter<CharSequence> adaptador = ArrayAdapter.createFromResource(this, R.array.tipos, android.R.layout.simple_spinner_item);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adaptador);
        et1.setText(nom);
        et2.setText(desc);
        if (tipo.equals("Entrante")) {
            sp.setSelection(0);
        } else if (tipo.equals("Plato")) {
            sp.setSelection(1);
        } else if (tipo.equals("Postre")) {
            sp.setSelection(2);
        }
    }

    public void anadir(View v) {
        final Receta datosN = new Receta();

        String nom = et1.getText().toString().trim();
        String desc = et2.getText().toString().trim();
        String tipo = String.valueOf(sp.getSelectedItem());
        if (nom.length() > 0 || desc.length() > 0) {
            datosN.setNombre(nom);
            datosN.setDescri(desc);
            datosN.setTipo(tipo);
            if (tipo.equals("Entrante")) {
                datosN.setImg("entrante");
                alReceta.set(index, datosN);
            } else if (tipo.equals("Plato")) {
                datosN.setImg("plato");
                alReceta.set(index, datosN);
            } else if (tipo.equals("Postre")) {
                datosN.setImg("postre");
                alReceta.set(index, datosN);
            }
            tostada("Receta modificada");
            escribirXML();
            Intent i = new Intent(this,Principal.class);
            startActivity(i);
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
