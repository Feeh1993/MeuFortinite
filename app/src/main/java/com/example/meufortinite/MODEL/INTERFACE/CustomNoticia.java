package com.example.meufortinite.MODEL.INTERFACE;

import android.view.View;
import android.widget.ImageButton;

import com.example.meufortinite.MODEL.GERAL.Noticia;

public interface CustomNoticia
{
    void onShareClick(ImageButton button, int position, Noticia noticia);

    void onLikeClick(ImageButton button, int position, Noticia noticia);

    void onClick(View button, int position, Noticia noticia);
}
