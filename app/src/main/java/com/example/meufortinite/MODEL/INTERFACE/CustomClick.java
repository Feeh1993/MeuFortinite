package com.example.meufortinite.MODEL.INTERFACE;

import android.view.View;
import android.widget.Button;


import com.example.meufortinite.MODEL.GERAL.Amigo;

public interface CustomClick
{
    void onItemClick(View itemView, int position, Button button,
                     Amigo meuUsario, Amigo usuario);

}
