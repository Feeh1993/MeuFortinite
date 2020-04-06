package com.example.meufortinite.VIEW.ACTIVITY;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.meufortinite.DAO.REMOTO.ConfiguracaoFirebase;
import com.example.meufortinite.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class Chat extends AppCompatActivity
{
    private String meuId,idUser;
    private DatabaseReference ref = ConfiguracaoFirebase.getFirebase();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Bundle bundle = getIntent().getExtras();
        meuId = bundle.getString("meu_id");
        idUser = bundle.getString("id_user");
        Log.d("CHAT_","CHAT INICIADO IDUSER: "+idUser);
        Log.d("CHAT_","CHAT INICIADO MEUID: "+meuId);
        recuperarMensagensRede(meuId,idUser);
    }
//Recuperar Banco firebase com mensagens
    private void recuperarMensagensRede(String meuId, String idUser)
    {
        ref.child("chat").child(meuId).child(idUser).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }
}
