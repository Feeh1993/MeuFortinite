package com.example.meufortinite.MODEL.INTERFACE;

import android.widget.ImageButton;

import com.example.meufortinite.MODEL.GERAL.Amigo;

public interface CustomBusca
{
    void onCopiar(ImageButton button, int position, Amigo usuario);

    void onMessagemClick(ImageButton button, int position, Amigo usuario);
}
