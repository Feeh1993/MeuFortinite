package com.example.meufortinite.VIEW.FRAGMENT;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.meufortinite.DAO.REMOTO.ConfiguracaoFirebase;
import com.example.meufortinite.MODEL.GERAL.CHAT.ChatModel;
import com.example.meufortinite.R;
import com.example.meufortinite.VIEW.ACTIVITY.Chat;
import com.example.meufortinite.VIEW.ADAPTER.AdaptadorChat;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;

public class Conversas extends Fragment
{
    private DatabaseReference ref = ConfiguracaoFirebase.getFirebase();
    private RecyclerView recChat;
    private FloatingActionButton fabNmsg;
    private Button btnAmigos;
    private ViewPager viewPager;
    private AdaptadorChat adaptadorChat;

    public Conversas()
    {
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("CHAT_","onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("CHAT_","onStop");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("CHAT_","onPause");
    }

    @Override
    public void onResume()
    {
        super.onResume();
        //ref.child("novaMensagem").child(meuUsuario.get(0).getId())
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        Log.d("CHAT_","onViewCreated");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_conversa, container, false);
        fazerCast(view);
        iniciarRecycler();
        return view;
    }

    private void iniciarRecycler()
    {

    }

    private void fazerCast(View view)
    {
        recChat = view.findViewById(R.id.recChat);
        fabNmsg = view.findViewById(R.id.fabNovaMsg);
        viewPager = (ViewPager) getActivity().findViewById(R.id.vp_painel);
        btnAmigos = view.findViewById(R.id.btnAmigos);

        btnAmigos.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(1);
            }
        });
        fabNmsg.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                abrirListaAmigos();
            }
        });


    }
//criar algo para ab
    private void abrirListaAmigos()
    {

    }


}
