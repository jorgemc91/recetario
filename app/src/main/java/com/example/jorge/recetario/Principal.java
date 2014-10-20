package com.example.jorge.recetario;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;


public class Principal extends Activity {
    private ArrayList<DatosArrayList> datosv2 = new ArrayList<DatosArrayList>();
    private AdaptadorArrayList aal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_principal);
        initComponents();
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
            return anadir();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;
        if (id == R.id.action_borrar) {
            datosv2.remove(index);
            aal.notifyDataSetChanged();
            tostada("Receta eliminada");
        } else if (id == R.id.action_editar) {
            return editar(index);
        }
        return super.onContextItemSelected(item);
    }

    private void initComponents(){
        DatosArrayList dato1 = new DatosArrayList("Montaditos de Salmón","En primer lugar picamos la cebolla y la pochamos en una sartén con un poco " +
                "de aceite de oliva. Mientras tanto picamos el salmón, lo ponemos en un bol.","Entrante",this.getResources().getDrawable(R.drawable.entrante));
        DatosArrayList dato2 = new DatosArrayList("Paella Valenciana","La receta de la paella valenciana tal y como se elabora en la huerta de Valencia" +
                " de forma tradicional.","Plato",this.getResources().getDrawable(R.drawable.plato));
        DatosArrayList dato3 = new DatosArrayList("Tiramisú","El Tiramisu es un postre de origen italiano que es realmente popular" +
                " en casi todo el mundo, y concretamente en nuestro pais tiene bastante éxito.","Postre",this.getResources().getDrawable(R.drawable.postre));

        datosv2.add(dato1);
        datosv2.add(dato2);
        datosv2.add(dato3);
        aal = new AdaptadorArrayList(this, R.layout.lista_detalle, datosv2);
        final ListView lv = (ListView) findViewById(R.id.lvLista);
        lv.setAdapter(aal);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tv = (TextView) view.findViewById(R.id.tvTexto1);
                Object o = view.getTag();
                AdaptadorArrayList.viewHolder vh;
                vh = (AdaptadorArrayList.viewHolder) o;
                tostada(vh.tv1.getText().toString());
            }
        });
        registerForContextMenu(lv);
    }

    private boolean anadir() {
        final DatosArrayList aux = new DatosArrayList();
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(R.string.anadir);
        LayoutInflater inflater = LayoutInflater.from(this);
        final View vista = inflater.inflate(R.layout.dialogo_anadir, null);
        alert.setView(vista);
        Spinner spinner = (Spinner) vista.findViewById(R.id.sTipo);
        ArrayAdapter<CharSequence> adaptador = ArrayAdapter.createFromResource(this, R.array.tipos,
                                                            android.R.layout.simple_spinner_item);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adaptador);
        alert.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        EditText et1, et2;
                        Spinner sp;
                        Drawable img;
                        et1 = (EditText) vista.findViewById(R.id.etNombre);
                        et2 = (EditText) vista.findViewById(R.id.etDescripcion);
                        sp = (Spinner) vista.findViewById(R.id.sTipo);
                        String nom = et1.getText().toString();
                        String desc = et2.getText().toString();
                        String tipo = String.valueOf(sp.getSelectedItem());
                        aux.setNombre(nom);
                        aux.setDescri(desc);
                        aux.setTipo(tipo);
                        if (tipo.equals("Entrante")){
                            img = vista.getResources().getDrawable(R.drawable.entrante);
                            aux.setImg(img);
                            datosv2.add(aux);
                        }else if (tipo.equals("Plato")){
                            img = vista.getResources().getDrawable(R.drawable.plato);
                            aux.setImg(img);
                            datosv2.add(aux);
                        }else if (tipo.equals("Postre")){
                            img = vista.getResources().getDrawable(R.drawable.postre);
                            aux.setImg(img);
                            datosv2.add(aux);
                        }
                        aal.notifyDataSetChanged();
                        tostada("Receta añadida");
                    }
                });
        alert.setNegativeButton(android.R.string.no, null);
        alert.show();
        return true;
    }

    private boolean editar(final int index) {
        final DatosArrayList datosN = new DatosArrayList();
        final EditText et1, et2;
        final Spinner sp;
        DatosArrayList datosA = new DatosArrayList();
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(R.string.editar);
        LayoutInflater inflater = LayoutInflater.from(this);
        final View vista = inflater.inflate(R.layout.dialogo_anadir, null);
        alert.setView(vista);
        datosA = datosv2.get(index);
        et1 = (EditText) vista.findViewById(R.id.etNombre);
        et1.setText(datosA.getNombre());
        et2 = (EditText) vista.findViewById(R.id.etDescripcion);
        et2.setText(datosA.getDescri());
        sp = (Spinner) vista.findViewById(R.id.sTipo);
        ArrayAdapter<CharSequence> adaptador = ArrayAdapter.createFromResource(this, R.array.tipos, android.R.layout.simple_spinner_item);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adaptador);
        String Tselect = datosA.getTipo();
        if(Tselect.equals("Entrante")){sp.setSelection(0);}
        else if(Tselect.equals("Plato")){sp.setSelection(1);}
        else if(Tselect.equals("Postre")){sp.setSelection(2);}

        alert.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Drawable img;
                        String nom = et1.getText().toString().trim();
                        String desc = et2.getText().toString().trim();
                        String tipo = String.valueOf(sp.getSelectedItem());
                        if(nom.length()>0 || desc.length()>0){
                            datosN.setNombre(nom);
                            datosN.setDescri(desc);
                            datosN.setTipo(tipo);
                            if (tipo.equals("Entrante")){
                                img = vista.getResources().getDrawable(R.drawable.entrante);
                                datosN.setImg(img);
                                datosv2.set(index,datosN);
                            }else if (tipo.equals("Plato")){
                                img = vista.getResources().getDrawable(R.drawable.plato);
                                datosN.setImg(img);
                                datosv2.set(index,datosN);
                            }else if (tipo.equals("Postre")){
                                img = vista.getResources().getDrawable(R.drawable.postre);
                                datosN.setImg(img);
                                datosv2.set(index,datosN);
                            }
                            aal.notifyDataSetChanged();
                            tostada("Receta modificada");
                        }
                    }
                });
        alert.setNegativeButton(android.R.string.no, null);
        alert.show();
        return true;
    }

    private void tostada(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
