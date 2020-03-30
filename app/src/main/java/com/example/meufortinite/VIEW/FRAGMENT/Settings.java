package com.example.meufortinite.VIEW.FRAGMENT;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.meufortinite.DAO.LOCAL.DatabaseHelper;
import com.example.meufortinite.DAO.REMOTO.ConfiguracaoFirebase;
import com.example.meufortinite.MODEL.GERAL.Avatar;
import com.example.meufortinite.MODEL.GERAL.Usuario;
import com.example.meufortinite.R;
import com.example.meufortinite.VIEW.ACTIVITY.Login;
import com.example.meufortinite.VIEW.ACTIVITY.SelecionarAvatar;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class Settings extends Fragment
{
    private Button btnPro,btnLogout;
    private ImageButton imgTrocarImg;
    private Switch swtchNtficacao,swtchOcultar;
    private DatabaseHelper db;
    private ArrayList<Avatar> avatars = new ArrayList<>();
    private ArrayList<Usuario> usuarios = new ArrayList<>();
    private DatabaseReference ref = ConfiguracaoFirebase.getFirebase();



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
        db = new DatabaseHelper(getContext());
        Log.d("SETTINGS_","TAMANHO DB: "+db.getQTDAvatares());
        usuarios.clear();
        usuarios.addAll(db.recuperarUsuarios());
        if (db.getQTDAvatares() > 0)
        {
            avatars.clear();
            avatars.addAll(db.recuperarAvatar());
            imgTrocarImg.setImageResource(Avatar.identificarAvatar(Integer.parseInt(avatars.get(0).getAvatar())));
            Log.d("SETTINGS_","AVATAR: "+avatars.get(0).getAvatar());
            Log.d("SETTINGS_","HORA DA ATUALIZAÇÃO: "+avatars.get(0).getCriado());
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (db.getQTDAvatares() > 0)
        {
            avatars.clear();
            avatars.addAll(db.recuperarAvatar());
            imgTrocarImg.setImageResource(Avatar.identificarAvatar(Integer.parseInt(avatars.get(0).getAvatar())));
            Log.d("SETTINGS_","AVATAR: "+avatars.get(0).getAvatar());
            Log.d("SETTINGS_","HORA DA ATUALIZAÇÃO: "+avatars.get(0).getCriado());
        }
    }

    private void fazerCast(View view)
    {
        btnPro = view.findViewById(R.id.btnPro);
        btnLogout = view.findViewById(R.id.btnLogout);
        imgTrocarImg = view.findViewById(R.id.imgAvatar_setting);
        swtchNtficacao = view.findViewById(R.id.swtchNotificacoes_settings);
        swtchOcultar = view.findViewById(R.id.swtchOcultarDados_settings);

        btnPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"Virando Pró",Toast.LENGTH_LONG).show();
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(getContext(),"ID desconectado :)",Toast.LENGTH_LONG).show();
                ref.child("usuarios").child(usuarios.get(0).getId()).child("tipo").setValue("deslogado");
                startActivity(new Intent(getContext(), Login.class));
            }
        });

        imgTrocarImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SelecionarAvatar.class);
                Bundle bundle = new Bundle();
                Log.d("SETTINGS_","IDUSER: "+usuarios.get(0).getId());
                bundle.putString("id_user",usuarios.get(0).getId());
                if (db.getQTDAvatares() > 0)
                {
                    bundle.putInt("id_avt",avatars.get(0).getId());
                }
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }


}
