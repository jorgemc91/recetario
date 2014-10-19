package com.example.jorge.recetario;


import android.graphics.drawable.Drawable;
import android.widget.Spinner;

public class DatosArrayList <ArrayList>{
    private String nombre, descri, tipo;
    private Drawable img;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescri() {
        return descri;
    }

    public void setDescri(String descri) {
        this.descri = descri;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Drawable getImg() {
        return img;
    }

    public void setImg(Drawable img) {
        this.img = img;
    }

}
