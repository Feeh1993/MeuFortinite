package com.example.meufortinite.MODEL.INTERFACE;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.meufortinite.MODEL.GERAL.User;

public interface CustomMsgeNtfc
{
    void onNotificacaoClick(ImageButton button, int position, User usuario);

    void onMessagemClick(ImageButton button, int position, User usuario);
}
