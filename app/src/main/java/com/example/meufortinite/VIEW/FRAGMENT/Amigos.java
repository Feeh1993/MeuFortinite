package com.example.meufortinite.VIEW.FRAGMENT;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.meufortinite.DAO.REMOTO.ConfiguracaoFirebase;
import com.example.meufortinite.MODEL.User;
import com.example.meufortinite.R;
import com.example.meufortinite.VIEW.ADAPTER.AdaptadorAmigos;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class Amigos extends Fragment
{

    private ImageButton btnVoltar;
    private EditText srchBuscar;
    private TextView txtComuni, txtUsuario;
    private DatabaseReference ref = ConfiguracaoFirebase.getFirebase();
    private ArrayList<User> listaUsuarios = new ArrayList<>();
    private RecyclerView recUsuarios;
    private FirebaseAuth user = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private boolean resultado = true;
    private ProgressBar prgUser,prgComu;
    private User meuUser;
    private LinearLayoutManager linearLayoutManager;
    private AdaptadorAmigos rvAdapter ;


    public Amigos()
    {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_amigos, container, false);
    }

}
