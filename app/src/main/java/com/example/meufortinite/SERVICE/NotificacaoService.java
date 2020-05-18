package com.example.meufortinite.SERVICE;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.meufortinite.DAO.LOCAL.DatabaseHelper;
import com.example.meufortinite.DAO.REMOTO.ConfiguracaoFirebase;
import com.example.meufortinite.MODEL.GERAL.Avatar;
import com.example.meufortinite.MODEL.GERAL.Mensagem;
import com.example.meufortinite.MODEL.GERAL.Noticia;
import com.example.meufortinite.MODEL.GERAL.Notificacao;
import com.example.meufortinite.MODEL.GERAL.Usuario;
import com.example.meufortinite.R;
import com.example.meufortinite.VIEW.ACTIVITY.Chat;
import com.example.meufortinite.VIEW.ACTIVITY.ConteudoNoticia;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class NotificacaoService extends Service

{
    private DatabaseReference ref = ConfiguracaoFirebase.getFirebase();
    private ChildEventListener mensagemStartListener,mensagemEndListener,noticiaListener,notificacaoListener;
    private String TAG = "SERVICO_";
    private DatabaseHelper db;
    private ArrayList<Usuario> meuUsuario = new ArrayList<>();
    private ArrayList<Avatar> meuAvatar = new ArrayList<>();
    private int NOTIFICATION_ID;
    private String CHANEL_NOTICIAS = "1",CHANEL_CHAT = "2",CHANEL_NOTIFICACAO = "3";
    private ArrayList<Notificacao> minhasNotificacoes = new ArrayList<>();

    private int cont = 1;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.d(TAG, "Iniciando Service");
        return START_NOT_STICKY;
    }

    @Override
    public void onCreate()
    {
        Log.d(TAG, "onCreate");
        db = new DatabaseHelper(getApplicationContext());
        minhasNotificacoes.clear();
        meuUsuario.clear();
        meuAvatar.clear();
        minhasNotificacoes.addAll(db.recuperaNotificacao());
        meuUsuario.addAll(db.recuperarUsuarios());
        meuAvatar.addAll(db.recuperarAvatar());
        //escutar alterações em mensagens
        escutarNovasMensagens();
        //escutar e notificar novas noticias
        escutarNovasNoticias();
        super.onCreate();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.d(TAG, "onTaskRemoved: ");
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.d(TAG, "Destruindo service");
        ref.child("noticias").removeEventListener(noticiaListener);
        ref.child("conversas").orderByKey().endAt(meuUsuario.get(0).getId()).removeEventListener(mensagemEndListener);
        ref.child("conversas").orderByKey().startAt(meuUsuario.get(0).getId()).removeEventListener(mensagemStartListener);
        ref.child("conversas").orderByKey().startAt(meuUsuario.get(0).getId()).removeEventListener(notificacaoListener);

        stopForeground(true);
        stopSelf();
    }
    private void escutarNovasNotificacoes()
    {
        notificacaoListener = new ChildEventListener()
        {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {
                String notificacao = dataSnapshot.getValue().toString();

                Log.d("service_","nova notificacao : "+ notificacao);
                enviarNotificacao(notificacao,null,null,3);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        ref.child("notificacao").child(meuUsuario.get(0).getId()).addChildEventListener(notificacaoListener);
    }

    private void escutarNovasNoticias()
    {
       noticiaListener = new ChildEventListener()
        {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {
                Noticia noticia = dataSnapshot.getValue(Noticia.class);
                Log.d("service_","nova noticia : "+ noticia.getTitulo());
                verificarNotificacao(noticia.getId(),noticia.getTitulo(),2,null,noticia);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        ref.child("noticias").addChildEventListener(noticiaListener);
    }

    private void escutarNovasMensagens()
    {
       mensagemStartListener = new ChildEventListener()
        {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {
                Mensagem new_mensagem = dataSnapshot.getValue(Mensagem.class);
                Log.d("service_","nova mensagem startAt : "+ new_mensagem.getMessagem());
                verificarNotificacao(new_mensagem.getId(),new_mensagem.getMessagem(),0,new_mensagem,null);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {
                Mensagem new_mensagem = dataSnapshot.getValue(Mensagem.class);
                Log.d("service_","nova mensagem alterada startAt : "+ new_mensagem.getMessagem());
                verificarNotificacao(new_mensagem.getId(),new_mensagem.getMessagem(),1,new_mensagem,null);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mensagemEndListener= new ChildEventListener()
        {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {
                Mensagem new_mensagem = dataSnapshot.getValue(Mensagem.class);
                Log.d("service_","nova mensagem endAt : "+ new_mensagem.getMessagem());
                verificarNotificacao(new_mensagem.getId(),new_mensagem.getMessagem(),0,new_mensagem,null);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {
                Mensagem new_mensagem = dataSnapshot.getValue(Mensagem.class);
                Log.d("service_","nova mensagem alterada endAt : "+ new_mensagem.getMessagem());
                verificarNotificacao(new_mensagem.getId(),new_mensagem.getMessagem(),1,new_mensagem,null);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot)
            {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        };
        ref.child("conversas").orderByKey().endAt(meuUsuario.get(0).getId()).addChildEventListener(mensagemEndListener);
        ref.child("conversas").orderByKey().startAt(meuUsuario.get(0).getId()).addChildEventListener(mensagemStartListener);
    }

    private void verificarNotificacao(String id,String recebido,int tipo,Mensagem mensagem,Noticia noticia)
    {
        /******************************** tipos:**************
         * tipo == 0 é mensagem adicionada
         * tipo == 1 é mensagem alterada
         * tipo == 2 é noticia adicionada
         * tipo == 3 é notificacao adicionada
          */
        boolean res = false;
        for (int i = 0; i < minhasNotificacoes.size(); i++)
        {
            Log.d(TAG, "IDLISTA: "+minhasNotificacoes.get(i).getId()+" e IDMENSAGEM: "+id);
            if (minhasNotificacoes.get(i).getId().equals(id))
            {
                if (minhasNotificacoes.get(i).getRecebido().equals(recebido))
                {
                    res = true;
                    Log.d(TAG, "Esse id já foi notificado: "+ id);
                    break;
                }
                else
                {
                    switch (tipo)
                    {
                        case 1:
                            try
                            {
                                db.atualizarNotificacao(new Notificacao(mensagem.getMessagem(),mensagem.getId()));
                                Log.d(TAG, "Banco Atualizado Notificacao : "+db.getQTDNotificacao());
                                cont ++;
                                enviarNotificacao(null,mensagem,null,0);
                            }catch (NullPointerException e)
                            {
                                Log.d(TAG, "Entrou no catch,atualizando noticia... ");
                            }
                        case 2:
                            try
                            {
                                db.atualizarNotificacao(new Notificacao(noticia.getTitulo(),noticia.getId()));
                                Log.d(TAG, "Banco Atualizado Notificacao : "+db.getQTDNotificacao());
                                cont ++;
                                enviarNotificacao(null,null,noticia,1);
                            }catch (NullPointerException e)
                            {
                                Log.d(TAG, "Entrou no catch,atualizando mensagem... ");
                            }
                    }
                }
                Log.d(TAG, "esse id já foi notificado --> "+ id);
            }
        }
        if (res == false)
        {
            switch (tipo)
            {
                case 0:
                    Log.d(TAG, "Banco Notificacao Antes: "+db.getQTDNotificacao());
                    cont ++;
                    enviarNotificacao(null,mensagem,null,0);
                    db.inserirNotificacao(new Notificacao(mensagem.getMessagem(),mensagem.getId()));
                    Log.d(TAG, "Banco Notificacao Depois: "+db.getQTDNotificacao());
                case 2:
                    Log.d(TAG, "Banco Notificacao Antes: "+db.getQTDNotificacao());
                    cont ++;
                    enviarNotificacao(null,null,noticia,1);
                    db.inserirNotificacao(new Notificacao(noticia.getTitulo(),noticia.getId()));
                    Log.d(TAG, "Banco Notificacao Depois: "+db.getQTDNotificacao());

            }
        }
    }

    // Enviar uma notificação
    private void enviarNotificacao(String notificacao,Mensagem mensagem,Noticia noticia,int tipo)
    {
        switch (tipo)
        {
            case 0:
                Log.i(TAG, "enviarNotificação Mensagem: " + mensagem.getId());
// Intenção de iniciar a atividade principal
                Intent chat = new Intent(getApplicationContext(), Chat.class);
                Bundle dados = new Bundle();
                dados.putString("meu_id", meuUsuario.get(0).getId());
                dados.putString("id_user", mensagem.getId());
                dados.putString("meu_nick",meuUsuario.get(0).getNickname());
                dados.putString("nick_amigo", mensagem.getUsername());
                dados.putString("iconeA", mensagem.getRecebido());
                dados.putString("mIcone", meuAvatar.get(0).getAvatar());
                chat.putExtras(dados);
                //chat.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//CRIANDO INTENÇÃO
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,chat,0);
// CRIANDO NOTIFICAÇÃO
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this,CHANEL_CHAT)
                        .setSmallIcon(R.mipmap.ic_logo)
                        .setAutoCancel(true)
                        .setContentTitle(mensagem.getUsername())
                        .setContentText(mensagem.getMessagem())
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent);
// Criando e enviando notificação
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
                int random = (int) System.currentTimeMillis();

//CRIANDO CANAL DE NOTIFICAÇÃO
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                {
                    NotificationChannel canalChat = new NotificationChannel(CHANEL_CHAT,"Chat",NotificationManager.IMPORTANCE_HIGH);
                    canalChat.setDescription("Noticias do seu game");
                    canalChat.setLightColor(R.color.logo1);
                    notificationManager.createNotificationChannel(canalChat);
                    builder.setChannelId(CHANEL_CHAT);
                }
                notificationManager.notify(cont, builder.build());
                break;
            case 1:
                Log.i(TAG, "enviarNotificação Noticia: " + noticia.getId());
// Intenção de iniciar a atividade principal
                Intent conteudoNoticia = new Intent(getApplicationContext(), ConteudoNoticia.class);
                Bundle dadosNoticia = new Bundle();
                dadosNoticia.putString("data", noticia.getData());
                dadosNoticia.putString("ementa",noticia.getEmenta() );
                dadosNoticia.putString("image",noticia.getImage());
                dadosNoticia.putString("titulo",noticia.getTitulo());
                dadosNoticia.putDouble("likes", noticia.getLikes());
                dadosNoticia.putString("id", noticia.getId());
                conteudoNoticia.putExtras(dadosNoticia);
                //chat.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//CRIANDO INTENÇÃO
                PendingIntent intencaoPendente = PendingIntent.getActivity(getApplicationContext(),0,conteudoNoticia,0);
// CRIANDO NOTIFICAÇÃO
                NotificationCompat.Builder builderNoticia = new NotificationCompat.Builder(this,CHANEL_CHAT)
                        .setContentTitle(noticia.getTitulo())
                        .setSmallIcon(R.mipmap.ic_logo)
                        .setAutoCancel(true)
                        .setContentText(noticia.getEmenta())
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentIntent(intencaoPendente);
// Criando e enviando notificação
                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getApplicationContext());

//CRIANDO CANAL DE NOTIFICAÇÃO
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                {
                    NotificationChannel canalNoticias = new NotificationChannel(CHANEL_NOTICIAS,"Noticias",NotificationManager.IMPORTANCE_DEFAULT);
                    canalNoticias.setDescription("Receba notificações das novas conversas");
                    canalNoticias.setLightColor(R.color.logo2);
                    managerCompat.createNotificationChannel(canalNoticias);
                    builderNoticia.setChannelId(CHANEL_NOTICIAS);
                }
                managerCompat.notify(cont, builderNoticia.build());
                break;

            case 2:
                //personalizar os botões de ok
                Log.i(TAG, "enviarNotificação Notificacao: " + notificacao);

                ref.child("notificacao").child(meuUsuario.get(0).getId()).removeValue();
                break;
        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void criarCanais()
    {
        NotificationChannel canalNotificacao = new NotificationChannel(CHANEL_NOTIFICACAO,"Notificacao",NotificationManager.IMPORTANCE_HIGH);
        canalNotificacao.setDescription("Receba notificações dos seus amigos");
        canalNotificacao.setLightColor(R.color.logo3);


    }
}
