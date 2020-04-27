package com.example.meufortinite.MODEL.INTERFACE;

import android.view.View;
import android.widget.Button;

import com.example.meufortinite.MODEL.GERAL.Mensagem;

public interface CustomConversa
{
    void onItemClick(View itemView, int position, Mensagem conversa);
}
