package com.example.meufortinite.VIEW.FRAGMENT;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.meufortinite.DAO.LOCAL.DatabaseHelper;
import com.example.meufortinite.DAO.REMOTO.ConfiguracaoFirebase;
import com.example.meufortinite.MODEL.GERAL.Amigo;
import com.example.meufortinite.MODEL.GERAL.Avatar;
import com.example.meufortinite.MODEL.GERAL.Mensagem;
import com.example.meufortinite.MODEL.GERAL.Usuario;
import com.example.meufortinite.MODEL.INTERFACE.CustomConversa;
import com.example.meufortinite.R;
import com.example.meufortinite.VIEW.ADAPTER.AdaptadorChat;
import com.example.meufortinite.VIEW.ADAPTER.AdaptadorConversa;
import com.example.meufortinite.VIEW.DIALOG.NovaMensagem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Conversas extends Fragment
{
    private DatabaseReference ref = ConfiguracaoFirebase.getFirebase();
    private RecyclerView recChat;
    private FloatingActionButton fabNmsg;
    private Button btnAmigos;
    private ViewPager viewPager;
    private DatabaseHelper db;
    private ArrayList<Usuario> meuUsuario = new ArrayList<>();
    private ArrayList<Avatar> meuAvatar = new ArrayList<>();
    private ArrayList<Mensagem> listConversa = new ArrayList<>();
    private AdaptadorConversa adapter;


    public Conversas()
    {
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("CONVERSAS_","onStart");
        recuperarDadosLocais();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("CONVERSAS_","onStop");
    }

    @Override
    public void onPause()
    {
        super.onPause();
        Log.d("CONVERSAS_","onPause");
    }

    @Override
    public void onResume()
    {
        super.onResume();
        ref.child("novaMensagem").child(meuUsuario.get(0).getId()).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                try
                {
                    if (dataSnapshot.getValue().toString() != null)
                    {
                        Toast.makeText(getContext(),"Alteração",Toast.LENGTH_LONG).show();
                        viewPager.setCurrentItem(1);
                        recuperandoAmigo(dataSnapshot.getValue().toString());
                        //REMOVE VALOR DO BANCO PARA QUE OUTRA NOVA MENSAGEM ENTRE
                        ref.child("novaMensagem").child(meuUsuario.get(0).getId()).removeValue();
                    }

                }catch (NullPointerException e)
                {

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void recuperandoAmigo(String nick)
    {
        ref.child("usuarios").child(nick).addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                try
                {
                    Amigo amigo = dataSnapshot.getValue(Amigo.class);
                    iniciarNovaMensagem(amigo);
                    Log.d("CONVERSAS_","DADOS AMIGO RECEBIDO \n Nick: "+amigo.nick);
                }catch (NullPointerException e)
                {
                    Toast.makeText(getContext(),"Usuário não encontrado \n tente novamente!",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

// CRIAR CHAMADA DE NOVA MENSAGEM VINDA DA LISTA AMIGOS
    private void iniciarNovaMensagem(Amigo amigo)
    {
        FragmentManager fm = getChildFragmentManager();
        NovaMensagem novaMensagem = NovaMensagem.novaMensagem("Conversar com "+amigo.getNick(),
                amigo.getId(),
                meuUsuario.get(0).getId(),
                meuUsuario.get(0).getNickname(),
                amigo.getNick(),
                meuAvatar.get(0).getAvatar(),
                String.valueOf(amigo.getIcone()));
        novaMensagem.show(fm,"fragment_alert");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        Log.d("CONVERSAS_","onViewCreated");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_conversa, container, false);
        fazerCast(view);
        recuperarDadosLocais();
        recuperarBanco();
        return view;
    }
// IMPLEMENTAR BANCO REMOTO :<
    private void recuperarBanco()
    {
        ref.child("chat").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                try
                {
                    if (dataSnapshot.getValue().toString() != null)
                    {

                    }

                }catch (NullPointerException e)
                {

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void recuperarDadosLocais()
    {
        db = new DatabaseHelper(getContext());
        meuUsuario.clear();
        meuAvatar.clear();
        listConversa.clear();
        meuUsuario.addAll(db.recuperarUsuarios());
        meuAvatar.addAll(db.recuperarAvatar());
        listConversa.addAll(db.recuperaConversas());
        try
        {
            Log.d("CONVERSAS_","AVATAR: "+listConversa.get(0).getRecebido());
            Log.d("CONVERSAS_","USUARIO: "+meuUsuario.get(0).getNickname());
            Log.d("CONVERSAS_","QTD CONVERSAS: "+listConversa.size());
            Log.d("CONVERSAS_","DADOS CONVERSA 1 : "+listConversa.get(0).getUsername());

        }catch (IndexOutOfBoundsException e)
        {
            Log.d("CONVERSAS_","PRIMEIRO ACESSO");
        }
        recChat.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AdaptadorConversa(getContext(), listConversa,meuUsuario.get(0).getId(), new CustomConversa() {
            @Override
            public void onItemClick(View itemView, int position, Mensagem conversa)
            {
                //FALTA IMPLEMENTAR CLIQUE

            }
        });
        recChat.setAdapter(adapter);

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
//criar algo para abrir lista amigos
    private void abrirListaAmigos()
    {
        FragmentManager fm = getChildFragmentManager();
        //NovaMensagem novaMensagem = NovaMensagem.novaMensagem();
        //novaMensagem.show(fm,"fragment_alert");

    }


}
