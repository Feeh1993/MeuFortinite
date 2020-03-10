package com.example.meufortinite.VIEW.FRAGMENT;


import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meufortinite.DAO.LOCAL.DatabaseHelper;
import com.example.meufortinite.DAO.REMOTO.ConfiguracaoFirebase;
import com.example.meufortinite.MODEL.INTERFACE.CustomClick;
import com.example.meufortinite.MODEL.User;
import com.example.meufortinite.R;
import com.example.meufortinite.VIEW.ADAPTER.AdaptadorAmigos;
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
    private ArrayList<User> listaBusca = new ArrayList<>();
    private RecyclerView recBusca,recAmigos;
    private boolean resultado = true;
    private ProgressBar prgUser;
    private User meuUser;
    private AdaptadorAmigos adapterBusca,adaptadorAmigos ;
    private DatabaseHelper db;
    private ArrayList<User> listAmigos = new ArrayList<>();

    public Amigos()
    {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_amigos, container, false);
        fazerCast(view);
        db = new DatabaseHelper(getContext());
        return view;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        limparRecycler();
        txtBusca.setVisibility(View.GONE);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (db.getQTDAmigos() > 0)
        {
            listAmigos.clear();
            listAmigos.addAll(db.recuperaAmigos());
        }
        iniciarRecycler();
        iniciRecAmigos();
        adaptadorAmigos = new AdaptadorAmigos(getContext(), listAmigos, new CustomClick()
        {
            @Override
            public void onItemClick(View itemView, int position, Button button, User meuUsario, User usuario)
            {
                //Toast.makeText(getApplicationContext(), "Amigo adicionado: Posição da Lista"+listaUsuarios.get(position).getNome(), Toast.LENGTH_LONG).show();
                Drawable imgClicked  = getActivity().getResources().getDrawable(R.drawable.ic_amigos_check);
                Drawable imgOutClicked  = getActivity().getResources().getDrawable(R.drawable.ic_amigos);
//Tratando da lista de amigos
                Log.d("Busca", "REC CLICADO : " + usuario.getNick());
                if (meuUsario.getAmigos() != null)
                {
                    if (meuUsario.getAmigos().contains(usuario.getNick()))
                    {
                        Log.d("AMIGOs", "meuUsuario.getAmigos().contains != null IF  | Nick usuario " + usuario.getNick() + " E Nick meuUser: " + meuUsario.getNick());
                        button.setCompoundDrawablesWithIntrinsicBounds(null, null, null,imgOutClicked);
                        button.setText("Seguir");
                        meuUsario.getAmigos().remove(usuario.getNick());
                        //atualizando banco firebase
                        ref.child("usuarios").child(usuario.getId()).child("amigos").setValue(meuUsario.getAmigos());
                        //removendo do banco local
                        Log.i("AMIGOs","()deletando amigo,Antes: "+ db.getQTDAmigos()+" amigos");
                        db.deletarAmigo(usuario,"");
                        Log.i("AMIGOs","atualizando banco,Depois: "+db.getQTDAmigos()+" amigos");
                    } else
                    {
                        Log.d("AMIGOs", "meuUsuario.getAmigos().contains != null ELSE  | Nick usuario " + usuario.getNick() + " E Nick meuUser: " + meuUsario.getNick());
                        button.setCompoundDrawablesWithIntrinsicBounds(null, null, null, imgClicked);
                        button.setText("Seguindo");
                        //inserindo novo amigo no banco local
                        Log.i("AMIGOs","(APOS_SALVAR)atualizando amigo,Antes: "+ db.getQTDAmigos()+" amigos");
                        db.inserirAmigo(usuario);
                        Log.i("AMIGOs","(APOS_SALVAR)atualizando banco,Depois: "+db.getQTDAmigos()+" amigos");
                        meuUsario.getAmigos().add(usuario.getNick());
                        ref.child("usuarios").child(usuario.getId()).child("amigos").setValue(meuUsario.getAmigos());
                    }

                } else
                {
                    Log.d("AMIGOs", "meuUsuario.getAmigos() != null ELSE | Nick usuario " + usuario.getNick() + " E Nick meuUser: " + meuUsario.getNick());
                    button.setCompoundDrawablesWithIntrinsicBounds(null, null, null, imgClicked);
                    button.setText("Seguindo");
                    ArrayList<String> staticList = new ArrayList<>();
                    staticList.add(usuario.getNick());
                    meuUsario.setAmigos(staticList);
                    ref.child("usuarios").child(usuario.getId()).child("amigos").setValue(staticList);

                    //adicionando no banco local
                    Log.i("AMIGOs","()deletando amigo,Antes: "+ db.getQTDAmigos()+" amigos");
                    db.inserirAmigo(usuario);
                    Log.i("AMIGOs","atualizando banco,Depois: "+db.getQTDAmigos()+" amigos");

                }

            }

            @Override
            public void onViewClick(View view, User usuario)
            {
                Toast.makeText(getContext(),"Clicou no usuário: "+ usuario.getNick(),Toast.LENGTH_LONG).show();
            }
        });
        recAmigos.setAdapter(adaptadorAmigos);
    }

    private void fazerCast(View view)
    {
        srchBuscar = view.findViewById(R.id.edtBuscar_busca);
        txtUsuario = view.findViewById(R.id.txtUsuarios_Busca);
        txtBusca = view.findViewById(R.id.txtResBusca);
        recBusca = view.findViewById(R.id.recBuscaUsuarios_Busca);
        recAmigos = view.findViewById(R.id.recAmigos);
        prgUser = view.findViewById(R.id.prgbUser_Busca);
        srchBuscar.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    limparRecycler();
                    iniciarRecycler();
                    prgUser.setVisibility(View.GONE);
                    txtUsuario.setVisibility(View.GONE);
                    final String query = srchBuscar.getText().toString();
                    if (query.length() != 1)
                    {
                        recAmigos.setVisibility(View.GONE);
                            ref.child("nick").addListenerForSingleValueEvent(new ValueEventListener()
                            {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                                {
                                    for (DataSnapshot dados : dataSnapshot.getChildren())
                                    {
                                        Log.d("Busca", "Resultado: " + query);
                                        String nicks = dados.getKey();
                                        if (nicks.contains(query))
                                        {
                                            Log.d("Busca", "Contém: " + dados.getValue().toString());
                                            prgUser.setVisibility(View.VISIBLE);
                                            txtUsuario.setVisibility(View.VISIBLE);
                                            popularLista(dados.getValue().toString());
                                            break;
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

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
                        User usuario = dataSnapshot.getValue(User.class);
                        Log.d("Busca", usuario.getNick());
                        prgUser.setVisibility(View.GONE);
                        txtUsuario.setVisibility(View.VISIBLE);
                        txtUsuario.setText("usuarios");
                        listaBusca.add(usuario);
                        adapterBusca.notifyDataSetChanged();
                        recAmigos.setVisibility(View.VISIBLE);
                        txtBusca.setVisibility(View.VISIBLE);
                        /*
                        if (!usuario.getNick().contains(meuUser.getNick()))
                        {
                            prgUser.setVisibility(View.GONE);
                            txtUsuario.setVisibility(View.VISIBLE);
                            txtUsuario.setText("usuarios");
                            listaUsuarios.add(usuario);
                            rvAdapter.notifyDataSetChanged();
                        }
                        else
                        {
                            prgUser.setVisibility(View.GONE);
                            txtUsuario.setVisibility(View.VISIBLE);
                            txtUsuario.setText("Voce é burro?Pra que pesquisar voce mesmo seu porta!");
                        }
                         */

                        //rvAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError)
                    {

                    }
                });
    }

    private void limparRecycler()
    {
        listaBusca.clear();
        listAmigos.clear();
        adaptadorAmigos.notifyDataSetChanged();
        adapterBusca.notifyDataSetChanged();
    }
    private void iniciRecAmigos()
    {
        //recycler Amigos
        LinearLayoutManager lnlMAmigos = new LinearLayoutManager(getContext());
        lnlMAmigos.setOrientation(LinearLayoutManager.VERTICAL);
        recAmigos.setLayoutManager(lnlMAmigos);

    }
        private void iniciarRecycler()
    {
        //recycler busca
        recBusca.setHasFixedSize(true);
        LinearLayoutManager lnlMBusca = new LinearLayoutManager(getContext());
        lnlMBusca.setOrientation(LinearLayoutManager.VERTICAL);
        recBusca.setLayoutManager(lnlMBusca);
        adapterBusca = new AdaptadorAmigos(getContext(), listaBusca, new CustomClick()
        {
            @Override
            public void onItemClick(View itemView, int position, Button button,
                                    User meuUsuario,User usuario)
            {
                //Toast.makeText(getApplicationContext(), "Amigo adicionado: Posição da Lista"+listaUsuarios.get(position).getNome(), Toast.LENGTH_LONG).show();
                Drawable imgClicked  = getActivity().getResources().getDrawable(R.drawable.ic_amigos_check);
                Drawable imgOutClicked  = getActivity().getResources().getDrawable(R.drawable.ic_amigos);
//Tratando da lista de amigos
                Log.d("Busca", "REC CLICADO : " + usuario.getNick());
                if (meuUsuario.getAmigos() != null)
                {
                    if (meuUsuario.getAmigos().contains(usuario.getNick()))
                    {
                        Log.d("AMIGOs", "meuUsuario.getAmigos().contains != null IF  | Nick usuario " + usuario.getNick() + " E Nick meuUser: " + meuUsuario.getNick());
                        button.setCompoundDrawablesWithIntrinsicBounds(null, null, null,imgOutClicked);
                        button.setText("Seguir");
                        meuUsuario.getAmigos().remove(usuario.getNick());
                        //atualizando banco firebase
                        ref.child("usuarios").child(usuario.getId()).child("amigos").setValue(meuUsuario.getAmigos());
                        //removendo do banco local
                        Log.d("AMIGOs","Chegou aqui");
                        Log.i("AMIGOs","()deletando amigo,Antes: "+ db.getQTDAmigos()+" amigos");
                        db.deletarAmigo(usuario,"");
                        Log.i("AMIGOs","atualizando banco,Depois: "+db.getQTDAmigos()+" amigos");
                    } else
                        {
                            Log.d("AMIGOs", "meuUsuario.getAmigos().contains != null ELSE  | Nick usuario " + usuario.getNick() + " E Nick meuUser: " + meuUsuario.getNick());
                            button.setCompoundDrawablesWithIntrinsicBounds(null, null, null, imgClicked);
                            button.setText("Seguindo");
                            //inserindo novo amigo no banco local
                                Log.i("AMIGOs","(APOS_SALVAR)atualizando amigo,Antes: "+ db.getQTDAmigos()+" amigos");
                                db.inserirAmigo(usuario);
                                Log.i("AMIGOs","(APOS_SALVAR)atualizando banco,Depois: "+db.getQTDAmigos()+" amigos");
                            meuUsuario.getAmigos().add(usuario.getNick());
                            ref.child("usuarios").child(usuario.getId()).child("amigos").setValue(meuUsuario.getAmigos());
                        }

                } else
                    {
                        Log.d("AMIGOs", "meuUsuario.getAmigos() != null ELSE | Nick usuario " + usuario.getNick() + " E Nick meuUser: " + meuUsuario.getNick());
                        button.setCompoundDrawablesWithIntrinsicBounds(null, null, null, imgClicked);
                        button.setText("Seguindo");
                        ArrayList<String> staticList = new ArrayList<>();
                        staticList.add(usuario.getNick());
                        meuUsuario.setAmigos(staticList);
                        ref.child("usuarios").child(usuario.getId()).child("amigos").setValue(staticList);

                        //adicionando no banco local
                        Log.i("AMIGOs","(APOS_SALVAR)atualizando amigo,Antes: "+ db.getQTDAmigos()+" amigos");
                        db.inserirAmigo(usuario);
                        Log.i("AMIGOs","(APOS_SALVAR)atualizando banco,Depois: "+db.getQTDAmigos()+" amigos");

                    }

            }

            @Override
            public void onViewClick(View view, User usuario)
            {
                Toast.makeText(getContext(),"Clicou no usuário: "+ usuario.getNick(),Toast.LENGTH_LONG).show();
            }
        });
        recBusca.setAdapter(adapterBusca);
    }
}
