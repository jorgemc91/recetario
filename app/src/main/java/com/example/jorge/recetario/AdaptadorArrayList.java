package com.example.jorge.recetario;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdaptadorArrayList extends ArrayAdapter<Receta>{
    private Context contexto;
    private ArrayList<Receta> lista;
    private int recurso;
    static LayoutInflater i;

    public AdaptadorArrayList(Context context, int resource, ArrayList<Receta> objects) {
        super(context, resource, objects);
        this.contexto = context;
        this.lista = objects;
        this.recurso = resource;
        this.i = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.v("LOG", "" + lista.size());
        viewHolder vh = null;
        if (convertView == null){
            convertView = i.inflate(recurso, null);
            vh = new viewHolder();
            vh.tv1 = (TextView) convertView.findViewById(R.id.tvTexto1);
            vh.tv2 = (TextView) convertView.findViewById(R.id.tvTexto2);
            vh.tv3 = (TextView) convertView.findViewById(R.id.tvTexto3);
            vh.iv = (ImageView) convertView.findViewById(R.id.ivImagen);
            vh.iv2 = (ImageView) convertView.findViewById(R.id.ivImagenBorrar);
            convertView.setTag(vh);
            vh.iv2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int elementoV2 = -1;
                    elementoV2 = (Integer)view.getTag();
                    borrar(elementoV2);
                }
            });
        }else {
            vh = (viewHolder) convertView.getTag();
        }
        vh.posicion = position;

        vh.tv1.setText(lista.get(position).getNombre());
        vh.tv2.setText(lista.get(position).getDescri());
        vh.tv3.setText(lista.get(position).getTipo());
        if(lista.get(position).getImg().compareTo("entrante")==0){
            vh.iv.setImageDrawable(contexto.getResources().getDrawable(R.drawable.entrante));
        }else if (lista.get(position).getImg().compareTo("plato")==0){
            vh.iv.setImageDrawable(contexto.getResources().getDrawable(R.drawable.plato));
        }else if (lista.get(position).getImg().compareTo("postre")==0){
            vh.iv.setImageDrawable(contexto.getResources().getDrawable(R.drawable.postre));
        }
        vh.iv.setTag(position);
        vh.iv2.setTag(position);
        TextView tv1 = (TextView) convertView.findViewById(R.id.tvTexto1);
        return convertView;
    }

    private void borrar(final int pos){
        AlertDialog.Builder alert = new AlertDialog.Builder(contexto);
        alert.setTitle(R.string.borrar);
        LayoutInflater inflater = LayoutInflater.from(contexto);
        alert.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        lista.remove(pos);
                        notifyDataSetChanged();
                    }
                });
        alert.setNegativeButton(android.R.string.no, null);
        alert.show();
    }

    static class viewHolder{
        public TextView tv1, tv2, tv3;
        public int posicion;
        public ImageView iv, iv2;
    }
}
