package com.example.meufortinite.MODEL.INTERFACE;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


import com.example.meufortinite.MODEL.User;

import java.util.ArrayList;

public interface CustomClick
{
    void onItemClick(View itemView, int position, Button button,
                     User meuUsario, User usuario);

    void onViewClick(View view,User usuario);

}
