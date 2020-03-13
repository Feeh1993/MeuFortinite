package com.example.meufortinite.VIEW;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.meufortinite.DAO.LOCAL.DatabaseHelper;
import com.example.meufortinite.DAO.REMOTO.ConfiguracaoFirebase;
import com.example.meufortinite.MODEL.GERAL.Usuario;
import com.example.meufortinite.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Loading extends AppCompatActivity
{
    private TextView txt,txtLogo;
    private DatabaseHelper dbLocal;
    private  ArrayList<Usuario> usuarios = new ArrayList<>();
    private DatabaseReference ref = ConfiguracaoFirebase.getFirebase();
    private Typeface fortniteFont;
    private Animation animation;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        txt = (TextView) findViewById(R.id.txt_descricao);
        txtLogo = (TextView) findViewById(R.id.txt_logo);
        fortniteFont = Typeface.createFromAsset(getAssets(), getString(R.string.fortnite_font_resource));
        txtLogo.setTypeface(fortniteFont);
        txt.setTypeface(fortniteFont);
        startAnimations();

    }
    private void startAnimations()
    {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        LinearLayout l = (LinearLayout) findViewById(R.id.main_layout);
        if (l != null)
        {
            l.clearAnimation();
            l.startAnimation(anim);
        }
        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();

        animation = AnimationUtils.loadAnimation(this, R.anim.bounce);
        txtLogo.startAnimation(animation);
        int SPLASH_DISPLAY_LENGTH = 2500;
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                    txt.setText("bem vindo de volta ");
                consultarDados();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        dbLocal = new DatabaseHelper(getApplicationContext());

    }
//recupera banco e verifica login
    private void consultarDados()
    {
        recuperarBancoLocal();
        try
        {
            Log.d("LOADING_", "SIZE USUARIOS"+String.valueOf(usuarios.size()));
            ref.child("usuarios").child(usuarios.get(0).id).child("tipo").addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    if (dataSnapshot.exists())
                    {
                        if (dataSnapshot.getValue().toString().equals("deslogado"))
                        {
                            startActivity(new Intent(getApplicationContext(), Login.class));
                        }
                        else
                        {
                            startActivity(new Intent(getApplicationContext(), PainelPrincipal.class));
                        }
                    }
                    else startActivity(new Intent(getApplicationContext(), PainelPrincipal.class));
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {

                }
            });
        } catch (IndexOutOfBoundsException e)
        {
            startActivity(new Intent(getApplicationContext(), Login.class));
        }
    }
    //VERIFICA SE O BANCO LOCAL NÃO ESTÁ VAZIO
    private void recuperarBancoLocal()
    {
        try
        {
            Log.d("LOADING_","SIZE BANCO "+dbLocal.getQTDUsuarios());
            if (dbLocal.getQTDUsuarios() > 0 )
            {
                usuarios.addAll( dbLocal.recuperarUsuarios());
                Log.d("LOADING_",usuarios.get(0).getId());
            }
        }
        catch (NullPointerException e)
        {
            usuarios = null;
        }
    }

}

