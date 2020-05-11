package com.example.meufortinite.VIEW.FRAGMENT;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.example.meufortinite.DAO.LOCAL.DatabaseHelper;
import com.example.meufortinite.DAO.REMOTO.ConfiguracaoFirebase;
import com.example.meufortinite.MODEL.GERAL.Noticia;
import com.example.meufortinite.MODEL.INTERFACE.CustomNoticia;
import com.example.meufortinite.R;
import com.example.meufortinite.VIEW.ADAPTER.AdaptadorNoticias;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Noticias extends Fragment
{
    private DatabaseReference ref = ConfiguracaoFirebase.getFirebase();
    private ProgressBar progress;
    private RecyclerView recNoticias;
    private ArrayList<Noticia> listNoticias = new ArrayList<>();
    private AdaptadorNoticias adapter;
    private String TAG = "NOTICIAS_";
    private ValueEventListener noticiasValueListener;

    public Noticias()
    {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_noticias, container, false);
        fazerCast(view);
        return view;
    }

    private void fazerCast(View view)
    {
        recNoticias = view.findViewById(R.id.recNoticias);
        progress = view.findViewById(R.id.progress);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG,"onStart");
        recuperarDadosLocais();
    }

    private void recuperarDadosLocais()
    {
        recNoticias.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AdaptadorNoticias(getContext(), listNoticias, new CustomNoticia()
        {
            @Override
            public void onShareClick(ImageButton button, int position, Noticia noticia) {

            }

            @Override
            public void onLikeClick(ImageButton button, int position, Noticia noticia) {

            }

            @Override
            public void onClick(View button, int position, Noticia noticia) {

            }
        });
        recNoticias.setAdapter(adapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG,"onStop");
    }

    @Override
    public void onPause()
    {
        super.onPause();
        adapter.notifyDataSetChanged();
        Log.d(TAG,"onPause");
    }

    @Override
    public void onResume()
    {
        super.onResume();
        noticiasValueListener = new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    progress.setVisibility(View.VISIBLE);
                    Noticia noticia = snapshot.getValue(Noticia.class);
                    listNoticias.add(noticia);
                }
                adapter.notifyDataSetChanged();
                progress.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        };
        ref.child("noticias").addValueEventListener(noticiasValueListener);

    }

}
