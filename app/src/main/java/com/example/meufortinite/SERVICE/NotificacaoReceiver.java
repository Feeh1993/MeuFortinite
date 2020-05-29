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
import com.example.meufortinite.MODEL.GERAL.Notificacao;
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
    private Intent it;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        db = new DatabaseHelper(context);
        meuUsuario.addAll(db.recuperarUsuarios());
        meuAvatar.addAll(db.recuperarAvatar());
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Bundle bundle = intent.getExtras();
        String idA = bundle.getString("idA");
        String mId = bundle.getString("mId");
        String iconeA = bundle.getString("icone");
        String nick = bundle.getString("nick");
        String notificacao_id = bundle.getString("notificacao_id");
        String acao = intent.getAction();
        Log.d(TAG, "ACTION: "+intent.getAction());
        Log.d(TAG, "onReceive: "+nick+idA+mId+iconeA+nick+notificacao_id);

        if (acao == "BORA")
        {
            Log.d(TAG, "ACAO É BORA" );
            Log.d(TAG, "BORA:  texto \n"
                    +"\n idA "+idA
                    +"\n mId "+mId
                    +"\n iconeA "+iconeA
                    +"\n nick "+nick
                    +"\n notificacao_id "+notificacao_id);
            ref.child("alerta").child(mId).removeValue();
            Intent chat = new Intent(context, Chat.class);
            Bundle dados = new Bundle();
            dados.putString("meu_id", meuUsuario.get(0).getId());
            dados.putString("id_user",idA);
            dados.putString("meu_nick",meuUsuario.get(0).getNickname());
            dados.putString("nick_amigo",nick);
            dados.putString("iconeA", iconeA);
            dados.putString("mIcone", meuAvatar.get(0).getAvatar());
            chat.putExtras(dados);
            context.startActivity(chat);
            notificationManager.cancel(Integer.parseInt(notificacao_id));
            context.sendBroadcast(it);
        }
        else if (acao == "NAO")
        {
            Log.d(TAG, "NAO:  texto \n"
                    +"\n idA "+idA
                    +"\n mId "+mId
                    +"\n iconeA "+iconeA
                    +"\n nick "+nick
                    +"\n notificacao_id "+notificacao_id);
            ref.child("alerta").child(idA).setValue(new Notificacao(
                    "1"+"###"+meuUsuario.get(0).getId()+"###"+idA+ "###"+meuAvatar.get(0).getAvatar()+"###"+meuUsuario.get(0).getNickname(),
                    meuUsuario.get(0).getId(),DatabaseHelper.getDateTime()));
            ref.child("alerta").child(mId).removeValue();
            notificationManager.cancel(Integer.parseInt(notificacao_id));
            context.sendBroadcast(it);
        }
    }
}