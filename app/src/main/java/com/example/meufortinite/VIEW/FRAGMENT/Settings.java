package com.example.meufortinite.VIEW.FRAGMENT;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.meufortinite.R;

public class Settings extends Fragment
{
    private Button btnPro;
    private ImageButton imgTrocarImg;
    private Switch swtchNtficacao,swtchOcultar;


    public Settings()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        fazerCast(view);

        return view;
    }

    private void fazerCast(View view)
    {
        btnPro = view.findViewById(R.id.btnPro);
        imgTrocarImg = view.findViewById(R.id.imgAvatar_setting);
        swtchNtficacao = view.findViewById(R.id.swtchNotificacoes_settings);
        swtchOcultar = view.findViewById(R.id.swtchOcultarDados_settings);

        btnPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"Virando Pró",Toast.LENGTH_LONG).show();
            }
        });

        imgTrocarImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

}
