package com.example.meufortinite.VIEW.FRAGMENT;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meufortinite.DAO.LOCAL.DatabaseHelper;
import com.example.meufortinite.DAO.REMOTO.ConfiguracaoFirebase;
import com.example.meufortinite.MODEL.GERAL.Amigo;
import com.example.meufortinite.MODEL.GERAL.Avatar;
import com.example.meufortinite.MODEL.GERAL.Notificacao;
import com.example.meufortinite.MODEL.GERAL.Usuario;
import com.example.meufortinite.MODEL.INTERFACE.CustomClick;
import com.example.meufortinite.MODEL.INTERFACE.CustomMsgeNtfc;
import com.example.meufortinite.R;
import com.example.meufortinite.VIEW.ADAPTER.AdaptadorAmigos;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Amigos extends Fragment
{

    private EditText srchBuscar;
    private TextView  txtUsuario,txtBusca;
    private DatabaseReference ref = ConfiguracaoFirebase.getFirebase();
    private RecyclerView recBusca,recAmigos;
    private ProgressBar prgUser;

    private AdaptadorAmigos adapterBusca,adaptadorAmigos ;
    private DatabaseHelper db;

    private ArrayList<Amigo> listAmigos = new ArrayList<>();
    private ArrayList<Amigo> listaBusca = new ArrayList<>();

    private ArrayList<Usuario> meuUsuario = new ArrayList<>();
    private ArrayList<Avatar> meuAvatar = new ArrayList<>();
    private ViewPager viewPager;

    public Amigos()
    {
    }

    @Override
    public void onStart()
    {
        super.onStart();
       // recuperarBancoRemoto();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_amigos, container, false);
        fazerCast(view);
        Log.d("AMIGOS_","CRIANDO VIEW");
        return view;
    }


    private void recuperarMeusDados()
    {
        db = new DatabaseHelper(getContext());
        meuUsuario.addAll(db.recuperarUsuarios());
        meuAvatar.addAll(db.recuperarAvatar());
        listAmigos.addAll(db.recuperaAmigos());
        if (listAmigos.size() == 1)
        {
            txtUsuario.setVisibility(View.VISIBLE);
            txtUsuario.setText("Voce não possui nenhum amigo adicionado! \n Busque por novos amigos...");
        }
        Log.d("Amigos","LISTA AMIGOS SIZE "+listAmigos.size()+" \n Banco local TAM: "+ db.getQTDAmigos());
    }

    private void recuperarBF(String id)
    {
        ref.child("usuarios").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                Amigo meuUser = dataSnapshot.getValue(Amigo.class);
                    // implementar banco remoto que não esta atualizando
                try
                {
                    if (meuUser.getAmigos().size() > 0)
                    {
                        for (int i = 0; i < meuUser.getAmigos().size(); i++)
                        {
                            buscarNicks(meuUser.getAmigos().get(i));
                        }
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

    private void buscarNicks(String id)
    {
        ref.child("usuarios").child(id).addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                    Log.d("AMIGOS_","CHAVE: "+dataSnapshot.getValue());
                    Amigo meuAmigo = dataSnapshot.getValue(Amigo.class);
                    Log.d("AMIGOS_","AMIGO: "+meuAmigo.getNick());
                boolean res = false;
                for (int i = 0; i < listAmigos.size(); i++)
                {
                    if (listAmigos.get(i).getNick().contains(meuAmigo.getNick()))
                    {
                        res = true;
                        if (listAmigos.get(i).getIcone() != meuAmigo.getIcone())
                        {
                            db.atualizarAmigo(meuAmigo);
                            listAmigos.get(i).setIcone(meuAmigo.getIcone());
                            listAmigos.get(i).setAmigos(meuAmigo.getAmigos());
                            txtUsuario.setVisibility(View.GONE);
                            adaptadorAmigos.notifyDataSetChanged();
                        }
                    }
                }
                if (res == false)
                {
                    db.inserirAmigo(meuAmigo);
                    listAmigos.add(meuAmigo);
                    txtUsuario.setVisibility(View.GONE);
                    adaptadorAmigos.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("AMIGOS_","VIEW CRIADA");
    }

    @Override
    public void onPause()
    {
        super.onPause();
        Log.d("AMIGOS_","PAUSADO");
        limparRecycler(1);
        limparRecycler(0);
        txtBusca.setVisibility(View.GONE);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        Log.d("AMIGOS_","RESUMO");
        try
        {
            recuperarMeusDados();
        }catch (NullPointerException e)
        {
            if (listAmigos.size() == 1)
            {
                txtUsuario.setVisibility(View.VISIBLE);
                txtUsuario.setText("Voce não possui nenhum amigo adicionado! \n Busque por novos amigos...");
            }
        }
        iniciRecAmigos();
        recuperarBF(listAmigos.get(0).getId());
        iniciarRecBusca();
        recAmigos.setAdapter(adaptadorAmigos);
    }

    private void fazerCast(final View view)
    {
        Log.d("AMIGOS_","FAzendo CASt");
        viewPager = (ViewPager) getActivity().findViewById(R.id.vp_painel);
        srchBuscar = view.findViewById(R.id.edtBuscar_busca);
        txtUsuario = view.findViewById(R.id.txtUsuarios_Busca);
        txtBusca = view.findViewById(R.id.txtResBusca);
        recBusca = view.findViewById(R.id.recBuscaUsuarios_Busca);
        recAmigos = view.findViewById(R.id.recAmigos);
        prgUser = view.findViewById(R.id.prgbUser_Busca);

        //listAmigos.add(new Amigo())

        srchBuscar.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    Log.d("AMIGOS_","Dentro do srch");
                    limparRecycler(0);
                    iniciarRecBusca();
                    prgUser.setVisibility(View.GONE);
                    final String query = srchBuscar.getText().toString();
                    if (meuUsuario.get(0).getNickname().equals(query))
                    {
                        Toast.makeText(getContext(),"Voce digitou seu nick???\n Digite um nick que não seja o seu !!!!",Toast.LENGTH_LONG).show();
                    }
                    else
                        {
                            if (query.length() != 1)
                            {
                                if (query.isEmpty())
                                {
                                    srchBuscar.clearFocus();
                                }
                                else
                                {
                                    ref.child("nick").addListenerForSingleValueEvent(new ValueEventListener()
                                    {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                                        {
                                            boolean resultado = false;
                                            for (DataSnapshot dados : dataSnapshot.getChildren())
                                            {
                                                Log.d("AMIGOS_", "Resultado: " + query);
                                                String nicks = dados.getKey();
                                                if (nicks.equals(query))
                                                {
                                                    resultado = true;
                                                    prgUser.setVisibility(View.VISIBLE);
                                                    srchBuscar.clearFocus();
                                                    txtBusca.setVisibility(View.VISIBLE);
                                                    Log.d("AMIGOS_", "Contém: " + dados.getValue().toString());
                                                    prgUser.setVisibility(View.VISIBLE);
                                                    popularLista(dados.getValue().toString());
                                                }
                                            }
                                            if (resultado == false)
                                            {
                                                srchBuscar.clearFocus();
                                                txtBusca.setVisibility(View.VISIBLE);
                                                txtBusca.setText("A busca não obteve sucesso...");
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError)
                                        {

                                        }
                                    });
                                }
                            }
                        }

                }
                return false;
            }
        });

    }
    private void popularLista(final String key)
    {
                ref.child("usuarios").child(key).addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                            Amigo usuario = dataSnapshot.getValue(Amigo.class);
                                    Log.d("AMIGOS_", usuario.getNick());
                                    prgUser.setVisibility(View.GONE);
                                    txtUsuario.setVisibility(View.VISIBLE);
                                    txtUsuario.setText("usuarios");
                                    listaBusca.add(usuario);
                                    adapterBusca.notifyDataSetChanged();
                                    recAmigos.setVisibility(View.VISIBLE);
                                    txtBusca.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError)
                    {

                    }
                });
    }

    private void limparRecycler(int t)
    {
        switch (t)
        {
            case 0:
                listaBusca.clear();
                adapterBusca.notifyDataSetChanged();
                break;

            case 1:
                listAmigos.clear();
                adaptadorAmigos.notifyDataSetChanged();
                break;
        }
    }
    private void iniciRecAmigos()
    {
        // adicionar modificações user

        //recycler Amigo
        LinearLayoutManager lnlMAmigos = new LinearLayoutManager(getContext());
        lnlMAmigos.setOrientation(LinearLayoutManager.VERTICAL);
        recAmigos.setLayoutManager(lnlMAmigos);
        adaptadorAmigos = new AdaptadorAmigos(getContext(), listAmigos,meuUsuario.get(0).getId(),0, new CustomClick()
        {
            @Override
            public void onItemClick(View itemView, int position, Button button, Amigo meuUsario, Amigo usuario)
            {
                //Toast.makeText(getApplicationContext(), "Amigo adicionado: Posição da Lista"+listaUsuarios.get(position).getNome(), Toast.LENGTH_LONG).show();
                Drawable imgClicked  = getActivity().getResources().getDrawable(R.drawable.ic_amigos_check);
                Drawable imgOutClicked  = getActivity().getResources().getDrawable(R.drawable.ic_amigos);
//Tratando da lista de amigos
                Log.d("AMIGOS_", "REC CLICADO : " + usuario.getNick());
                if (meuUsario.getAmigos() != null)
                {
                    if (meuUsario.getAmigos().contains(usuario.getId()))
                    {
                        Log.d("AMIGOs", "meuUsuario.getAmigos().contains != null IF  | Nick usuario " + usuario.getNick() + " E Nick meuAmigo: " + meuUsario.getNick());
                        button.setCompoundDrawablesWithIntrinsicBounds(null, null, null,imgOutClicked);
                        button.setText("Seguir");
                        meuUsario.getAmigos().remove(usuario.getId());
                        //atualizando banco firebase
                        ref.child("usuarios").child(meuUsario.getId()).child("amigos").setValue(meuUsario.getAmigos());
                        //removendo do banco local
                        Log.i("AMIGOs","()deletando amigo,Antes: "+ db.getQTDAmigos()+" amigos");
                        db.deletarAmigo(usuario,"");
                        listAmigos.remove(usuario);
                        // ATUALIZAR LISTA ATUAL
                        listAmigos.clear();
                        listAmigos.addAll(db.recuperaAmigos());
                        adaptadorAmigos.notifyDataSetChanged();
                        Log.i("AMIGOs","atualizando banco,Depois: "+db.getQTDAmigos()+" amigos");
                    } else
                    {
                        Log.d("AMIGOs", "meuUsuario.getAmigos().contains != null ELSE  | Nick usuario " + usuario.getNick() + " E Nick meuAmigo: " + meuUsario.getNick());
                        button.setCompoundDrawablesWithIntrinsicBounds(null, null, null, imgClicked);
                        button.setText("Seguindo");
                        //inserindo novo amigo no banco local
                        Log.i("AMIGOs","(APOS_SALVAR)atualizando amigo,Antes: "+ db.getQTDAmigos()+" amigos");
                        db.inserirAmigo(usuario);
                        listAmigos.add(usuario);
                        adaptadorAmigos.notifyDataSetChanged();
                        Log.i("AMIGOs","(APOS_SALVAR)atualizando banco,Depois: "+db.getQTDAmigos()+" amigos");
                        meuUsario.getAmigos().add(usuario.getId());
                        ref.child("usuarios").child(meuUsario.getId()).child("amigos").setValue(meuUsario.getAmigos());
                    }

                } else
                {
                    Log.d("AMIGOs", "meuUsuario.getAmigos() != null ELSE | Nick usuario " + usuario.getNick() + " E Nick meuAmigo: " + meuUsario.getNick());
                    button.setCompoundDrawablesWithIntrinsicBounds(null, null, null, imgClicked);
                    button.setText("Seguindo");
                    ArrayList<String> staticList = new ArrayList<>();
                    staticList.add(usuario.getId());
                    meuUsario.setAmigos(staticList);
                    ref.child("usuarios").child(meuUsario.getId()).child("amigos").setValue(staticList);

                    //adicionando no banco local
                    Log.i("AMIGOs","()deletando amigo,Antes: "+ db.getQTDAmigos()+" amigos");
                    db.inserirAmigo(usuario);
                    listAmigos.add(usuario);
                    adaptadorAmigos.notifyDataSetChanged();
                    Log.i("AMIGOs","atualizando banco,Depois: "+db.getQTDAmigos()+" amigos");

                }

            }

        },new CustomMsgeNtfc()
        {
            @Override
            public void onNotificacaoClick(ImageButton button, int position, Amigo usuario)
            {
                chamarNotifificacao(usuario);
            }

            @SuppressLint("ResourceType")
            @Override
            public void onMessagemClick(ImageButton button, int position, Amigo usuario)
            {
                ref.child("novaMensagem").child(meuUsuario.get(0).getId()).setValue(usuario.getId());
                //viewPager.setCurrentItem(2);
            }
        });

    }
        private void iniciarRecBusca()
    {
        //recycler busca
        recBusca.setHasFixedSize(true);
        LinearLayoutManager lnlMBusca = new LinearLayoutManager(getContext());
        lnlMBusca.setOrientation(LinearLayoutManager.VERTICAL);
        recBusca.setLayoutManager(lnlMBusca);
        adapterBusca = new AdaptadorAmigos(getContext(), listaBusca,meuUsuario.get(0).getId(), 1, new CustomClick() {
            @Override
            public void onItemClick(View itemView, int position, Button button,
                                    Amigo meuUsuario, Amigo usuario) {
                //Toast.makeText(getApplicationContext(), "Amigo adicionado: Posição da Lista"+listaUsuarios.get(position).getNome(), Toast.LENGTH_LONG).show();
                Drawable imgClicked = getActivity().getResources().getDrawable(R.drawable.ic_amigos_check);
                Drawable imgOutClicked = getActivity().getResources().getDrawable(R.drawable.ic_amigos);
//Tratando da lista de amigos
                Log.d("Busca", "REC CLICADO : " + usuario.getNick());
                if (meuUsuario.getAmigos() != null)
                {
                    if (meuUsuario.getAmigos().contains(usuario.getId()))
                    {
                        Log.d("AMIGOs", "meuUsuario.getAmigos().contains != null IF  | Nick usuario " + usuario.getNick() + " E Nick meuAmigo: " + meuUsuario.getNick());
                        button.setCompoundDrawablesWithIntrinsicBounds(null, null, null, imgOutClicked);
                        button.setText("Seguir");
                        meuUsuario.getAmigos().remove(usuario.getId());
                        //atualizando banco firebase
                        ref.child("usuarios").child(meuUsuario.getId()).child("amigos").setValue(meuUsuario.getAmigos());
                        //removendo do banco local
                        Log.d("AMIGOs", "Chegou aqui");
                        Log.i("AMIGOs", "()deletando amigo,Antes: " + db.getQTDAmigos() + " amigos");
                        db.deletarAmigo(usuario, "");
                        listAmigos.clear();
                        listAmigos.addAll(db.recuperaAmigos());
                        adaptadorAmigos.notifyDataSetChanged();
                        Log.i("AMIGOs", "atualizando banco,Depois: " + db.getQTDAmigos() + " amigos");
                    } else {
                        Log.d("AMIGOs", "meuUsuario.getAmigos().contains != null ELSE  | Nick usuario " + usuario.getNick() + " E Nick meuAmigo: " + meuUsuario.getNick());
                        button.setCompoundDrawablesWithIntrinsicBounds(null, null, null, imgClicked);
                        button.setText("Seguindo");
                        //inserindo novo amigo no banco local
                        Log.i("AMIGOs", "(APOS_SALVAR)atualizando amigo,Antes: " + db.getQTDAmigos() + " amigos");
                        db.inserirAmigo(usuario);
                        Log.i("AMIGOs", "(APOS_SALVAR)atualizando banco,Depois: " + db.getQTDAmigos() + " amigos");
                        meuUsuario.getAmigos().add(usuario.getId());
                        ref.child("usuarios").child(meuUsuario.getId()).child("amigos").setValue(meuUsuario.getAmigos());
                        if (listAmigos.contains(usuario))
                        {
                            listAmigos.remove(usuario);
                        }
                        listAmigos.add(usuario);
                        adaptadorAmigos.notifyDataSetChanged();
                        listaBusca.clear();
                        adapterBusca.notifyDataSetChanged();
                        txtBusca.setVisibility(View.GONE);
                        srchBuscar.clearFocus();
                    }

                } else
                    {
                        Log.d("AMIGOs", "meuUsuario.getAmigos() != null ELSE | Nick usuario " + usuario.getNick() + " E Nick meuAmigo: " + meuUsuario.getNick());
                        button.setCompoundDrawablesWithIntrinsicBounds(null, null, null, imgClicked);
                        button.setText("Seguindo");
                        ArrayList<String> staticList = new ArrayList<>();
                        staticList.add(usuario.getId());
                        meuUsuario.setAmigos(staticList);
                        ref.child("usuarios").child(meuUsuario.getId()).child("amigos").setValue(staticList);

                        //adicionando no banco local
                        Log.i("AMIGOs", "(APOS_SALVAR)atualizando amigo,Antes: " + db.getQTDAmigos() + " amigos");
                        db.inserirAmigo(usuario);
                        if (listAmigos.contains(usuario))
                        {
                            listAmigos.remove(usuario);
                        }
                        listAmigos.add(usuario);
                        adaptadorAmigos.notifyDataSetChanged();

                        listaBusca.clear();
                        adapterBusca.notifyDataSetChanged();
                        txtBusca.setVisibility(View.GONE);
                        srchBuscar.clearFocus();
                        Log.i("AMIGOs", "(APOS_SALVAR)atualizando banco,Depois: " + db.getQTDAmigos() + " amigos");

                }

            }


        }, new CustomMsgeNtfc()
        {
            @Override
            public void onNotificacaoClick(ImageButton button, int position, Amigo usuario)
            {
                //ref.child("notificacao").child(usuario.getId()).setValue(new Notificacao(":",usuario.getId()));
                chamarNotifificacao(usuario);
            }

            @SuppressLint("ResourceType")
            @Override
            public void onMessagemClick(ImageButton button, int position, Amigo usuario)
            {
                ref.child("novaMensagem").child(meuUsuario.get(0).getId()).setValue(usuario.getId());
            }
        });
        recBusca.setAdapter(adapterBusca);
    }
    private void chamarNotifificacao(Amigo usuario)
    {
        //tipo+":"+idA+":"+mId+":"+icone+":"+nick+":"id
        ref.child("alerta").child(usuario.getId()).setValue(new Notificacao(
                "0"+"###"+meuUsuario.get(0).getId()+"###"+usuario.getId()+ "###"+meuAvatar.get(0).getAvatar()+"###"+meuUsuario.get(0).getNickname(),
                meuUsuario.get(0).getId(),DatabaseHelper.getDateTime())).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Snackbar.make(getView(),"Notificação encaminhada com sucesso!\n Aguarde resposta...", Snackbar.LENGTH_LONG).setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
            }
        });
    }



}
