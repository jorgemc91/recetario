package com.example.jorge.recetario;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Xml;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class Principal extends Activity {
    private ArrayList<Receta> alReceta = new ArrayList<Receta>();
    private AdaptadorArrayList aal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_principal);

        aal = new AdaptadorArrayList(this, R.layout.lista_detalle, alReceta);
        final ListView lv = (ListView) findViewById(R.id.lvLista);
        lv.setAdapter(aal);
        registerForContextMenu(lv);
        LeerArchivo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }else if (id == R.id.action_anadir){
            Intent i = new Intent(this,Anadir.class);
            Bundle b=new Bundle();
            b.putParcelableArrayList("recetas",alReceta);
            i.putExtras(b);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;
        if (id == R.id.action_borrar) {
            alReceta.remove(index);
            aal.notifyDataSetChanged();
            tostada("Receta eliminada");
        } else if (id == R.id.action_editar) {
            return editar(index);
        }
        return super.onContextItemSelected(item);
    }

    private void LeerArchivo(){
        String nombre="",descri="",img="",tipo="";
        XmlPullParser leerxml=Xml.newPullParser();
        try {
            leerxml.setInput(new FileInputStream(new File(getExternalFilesDir(null),"archivo.xml")),"utf-8");
            int evento=leerxml.getEventType();

            while (evento!=XmlPullParser.END_DOCUMENT){
                if(evento==XmlPullParser.START_TAG){
                    String etiqueta=leerxml.getName();
                    if(etiqueta.compareTo("nombre")==0){
                        nombre = leerxml.nextText();
                    }else if(etiqueta.compareTo("descripcion")==0){
                        descri = leerxml.nextText();
                    }else if(etiqueta.compareTo("tipo")==0){
                        tipo = leerxml.nextText();
                    }else if(etiqueta.compareTo("foto")==0){
                        img = leerxml.nextText();
                        alReceta.add(new Receta(nombre, descri, tipo, img));
                    }
                }
                evento=leerxml.next();
                aal.notifyDataSetChanged();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean editar(final int index) {
        final Receta datosN = new Receta();
        final String nom,desc,tipo;
        Receta datosA = new Receta();
        datosA = alReceta.get(index);
        nom = datosA.getNombre();
        desc = datosA.getDescri();
        tipo = datosA.getTipo();
        Intent i = new Intent(this,Editar.class);
        Bundle b=new Bundle();
        b.putParcelableArrayList("recetas",alReceta);
        i.putExtras(b);
        i.putExtra("id",index);
        i.putExtra("nombre",nom);
        i.putExtra("descripcion",desc);
        i.putExtra("tipo",tipo);
        startActivity(i);
        return true;
    }

    private void tostada(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
