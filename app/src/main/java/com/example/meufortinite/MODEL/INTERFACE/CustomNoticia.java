package com.example.meufortinite.MODEL.INTERFACE;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.meufortinite.MODEL.GERAL.Noticia;

public interface CustomNoticia
{
    void onShareClick(ImageButton button, int position, Noticia noticia);

    void onLikeClick(ImageButton button, TextView textView, int position, Noticia noticia,boolean shareclick);

    void onClick(View button, int position, Noticia noticia);
}
