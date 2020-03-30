package com.example.meufortinite.MODEL.INTERFACE;

import android.widget.ImageButton;

import com.example.meufortinite.MODEL.GERAL.Amigo;

public interface CustomMsgeNtfc
{
    void onNotificacaoClick(ImageButton button, int position, Amigo usuario);

    void onMessagemClick(ImageButton button, int position, Amigo usuario);
}
