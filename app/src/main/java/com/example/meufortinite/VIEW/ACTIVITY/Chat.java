package com.example.meufortinite.VIEW.ACTIVITY;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.meufortinite.DAO.LOCAL.DatabaseHelper;
import com.example.meufortinite.DAO.REMOTO.ConfiguracaoFirebase;
import com.example.meufortinite.HELPER.SCUtils;
import com.example.meufortinite.MODEL.GERAL.Avatar;
import com.example.meufortinite.MODEL.GERAL.Mensagem;
import com.example.meufortinite.R;
import com.example.meufortinite.VIEW.ADAPTER.AdaptadorChat;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Chat extends AppCompatActivity
{
    private String meuId,idUser;
    private DatabaseReference ref = ConfiguracaoFirebase.getFirebase();
    private ChildEventListener msgmListener;

    //Views UI
    public static final int ANTI_FLOOD_SECONDS = 3; //simple anti-flood
    private boolean IS_ADMIN = false; //set this to true for the admin app.
    private boolean PROFANITY_FILTER_ACTIVE = true;
    private RecyclerView recChat;
    private Chat mContext;
    private AdaptadorChat adapter;
    private ImageButton imgButtonSend;
    private EditText edtMensagem;
    private ArrayList<Mensagem> mensagemArrayList = new ArrayList<>();
    private ArrayList<Mensagem> listaBancoLocal = new ArrayList<>();
    private ProgressBar progressBar;
    private long last_message_timestamp = 0;
    private String caminho = "";
    private String meuNick,nickAmigo;
    private Toolbar toolbar;

    String mensagem;
    private String iconeA,mIcone;
    private DatabaseHelper db;

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        db = new DatabaseHelper(getApplicationContext());
        fazerCast();
        Bundle bundle = getIntent().getExtras();
        meuId = bundle.getString("meu_id");
        idUser = bundle.getString("id_user");
        meuNick = bundle.getString("meu_nick");
        nickAmigo = bundle.getString("nick_amigo");
        iconeA = bundle.getString("iconeA");
        mIcone = bundle.getString("mIcone");
        Log.d("CHAT_","CHAT INICIADO IDUSER: "+idUser);
        Log.d("CHAT_","CHAT INICIADO MEUID: "+meuId);

        //UNE O USUARIO COM O AMIGO PARA SE CRIAR O CAMINHO DO CHAT
        String possibil1 = meuId+"||"+idUser;
        String possibil2 = idUser+"||"+meuId;
        localizarExistencia(possibil1,possibil2);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(Avatar.identificarAvatar(Integer.parseInt(iconeA)));
        toolbar.setTitle(nickAmigo);

        progressBar.setVisibility(View.VISIBLE);
        recChat.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdaptadorChat(mContext, mensagemArrayList,meuNick);
        recChat.setAdapter(adapter);

        imgButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                enviarMsg(edtMensagem.getText().toString().trim(), false);
            }
        });

        edtMensagem.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_SEND)) {
                    imgButtonSend.performClick();
                }
                return false;
            }
        });
    }


    private void fazerCast()
    {
        mContext = Chat.this;
        recChat = (RecyclerView) findViewById(R.id.main_recycler_view);
        imgButtonSend = (ImageButton) findViewById(R.id.imageButton_send);
        edtMensagem = (EditText) findViewById(R.id.editText_message);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    private void localizarExistencia(final String possibil1, final String possibil2)
    {
        ref.child("chat").child(possibil1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try
                {
                    if (dataSnapshot.getValue().toString() != null)
                    {
                        caminho = possibil1;
                        progressBar.setVisibility(View.VISIBLE);
                        recuperarMensagensRede(possibil1);
                    }
                }catch (NullPointerException e)
                {

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
        ref.child("chat").child(possibil2).addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try
                {
                    if (dataSnapshot.getValue().toString() != null)
                    {
                        caminho = possibil2;
                        progressBar.setVisibility(View.VISIBLE);
                        recuperarMensagensRede(possibil2);
                    }
                }catch (NullPointerException e)
                {

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }
    //Recuperar Banco firebase com mensagens
    private void recuperarMensagensRede(String caminho)
    {
        msgmListener = new ChildEventListener()
        {
            @Override public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                progressBar.setVisibility(View.GONE);
                Mensagem new_mensagem = dataSnapshot.getValue(Mensagem.class);
                mensagemArrayList.add(new_mensagem);
                adapter.notifyDataSetChanged();
                recChat.scrollToPosition(adapter.getItemCount() - 1);
            }

            @Override public void onChildChanged(DataSnapshot dataSnapshot, String s)
            {

            }

            @Override public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d("REMOVED", dataSnapshot.getValue(Mensagem.class).toString());
                mensagemArrayList.remove(dataSnapshot.getValue(Mensagem.class));
                adapter.notifyDataSetChanged();
            }

            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s)
            {

            }

            @Override public void onCancelled(DatabaseError databaseError)
            {

            }
        };
        ref.child("chat").child(caminho).limitToLast(50).addChildEventListener(msgmListener);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_chat,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.mn_back:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void enviarMsg(String new_message, boolean isNotification)
    {
        edtMensagem.setText("");
        if (new_message.isEmpty())
        {
            return;
        }
        //simple anti-flood protection
        if ((System.currentTimeMillis() / 1000L - last_message_timestamp) < ANTI_FLOOD_SECONDS) {
            SCUtils.showErrorSnackBar(mContext, findViewById(android.R.id.content), "Texto muito grande,limite se a menos de 1000 caracteres").show();
            return;
        }
        TarefaMensagem tarefaMensagem = new TarefaMensagem();
        tarefaMensagem.execute(new_message);
    }

    private class TarefaMensagem extends AsyncTask<String, Void , Void>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(String... strings)
        {
            mensagem = strings[0];
            Mensagem msgRemota = new Mensagem(idUser,mensagem, DatabaseHelper.getDateTime(), "0",meuNick+":"+mIcone);
            Mensagem msgLocal = new Mensagem(idUser,mensagem, DatabaseHelper.getDateTime(),iconeA,meuNick);
            String key = ref.child("chat").child(meuId).child(idUser).push().getKey();
            if (caminho != "")
            {
                ref.child("chat").child(caminho).child(key).setValue(msgRemota);
                //IMPLEMENTAR OS BANCOS LOCAIS
                verificarEsalvar(msgLocal);
            }
            else
            {
                //UNE O USUARIO COM O AMIGO PARA SE CRIAR O CAMINHO DO CHAT
                caminho = meuId+"||"+idUser;
                ref.child("chat").child(caminho).child(key).setValue(msgRemota);
                localizarExistencia(caminho,"");
                verificarEsalvar(msgLocal);
            }

            last_message_timestamp = System.currentTimeMillis() / 1000L;
            return null;
        }
        private  void verificarEsalvar(Mensagem mensagem)
        {
            listaBancoLocal.addAll(db.recuperaConversas());
            for (int i = 0; i < listaBancoLocal.size(); i++)
            {
                if (listaBancoLocal.get(i).getId().equals(mensagem.getId()))
                {
                    Log.d("CHAT_","Verificar = true");
                    db.deletarConversa(mensagem,"");
                }
            }
            Log.d("CHAT_","AVATAR RECEBIDO: "+mensagem.getRecebido() );
            db.inserirConversa(mensagem);
            ref.child("conversas").child(caminho).setValue(mensagem);
        }
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        ref.removeEventListener(msgmListener);
    }
}