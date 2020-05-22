package com.example.meufortinite.SERVICE;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.meufortinite.DAO.LOCAL.DatabaseHelper;
import com.example.meufortinite.DAO.REMOTO.ConfiguracaoFirebase;
import com.example.meufortinite.MODEL.GERAL.Avatar;
import com.example.meufortinite.MODEL.GERAL.Usuario;
import com.example.meufortinite.VIEW.ACTIVITY.Chat;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class NotificacaoReceiver extends BroadcastReceiver
{
    String TAG = "BRDCST_";
    private DatabaseReference ref = ConfiguracaoFirebase.getFirebase();
    private ArrayList<Usuario> meuUsuario = new ArrayList<>();
    private ArrayList<Avatar> meuAvatar = new ArrayList<>();
    private DatabaseHelper db;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        db = new DatabaseHelper(context);
        meuUsuario.addAll(db.recuperarUsuarios());
        meuAvatar.addAll(db.recuperarAvatar());
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String texto = intent.getStringExtra("texto");
        String acao = intent.getAction();
        String textos[] = texto.split(":");
        //idA+":"+mId+":"+icone+":"+nick+":"id
        Log.d(TAG, "ACTION: "+intent.getAction());
        Log.d(TAG, "onReceive: "+texto);

        if (acao == "BORA")
        {
            Log.d(TAG, "ACAO É BORA" );
            ref.child("alerta").child(textos[1]).removeValue();
            Intent chat = new Intent(context, Chat.class);
            Bundle dados = new Bundle();
            dados.putString("meu_id", textos[1]);
            dados.putString("id_user",textos[0]);
            dados.putString("meu_nick",meuUsuario.get(0).getNickname());
            dados.putString("nick_amigo", textos[3]);
            dados.putString("iconeA", textos[2]);
            dados.putString("mIcone", meuAvatar.get(0).getAvatar());
            chat.putExtras(dados);
            //notificationManager.cancel(Integer.parseInt(id));
        }
        else
        {
            Log.d(TAG, "ACAO NÃO É BORA" );
            //notificationManager.cancel(Integer.parseInt(id));
        }
    }
}
