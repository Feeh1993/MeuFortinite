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
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meufortinite.DAO.REMOTO.ConfiguracaoFirebase;
import com.example.meufortinite.MODEL.INTERFACE.CustomClick;
import com.example.meufortinite.MODEL.User;
import com.example.meufortinite.R;
import com.example.meufortinite.VIEW.ADAPTER.AdaptadorAmigos;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Amigos extends Fragment
{

    private EditText srchBuscar;
    private TextView  txtUsuario;
    private DatabaseReference ref = ConfiguracaoFirebase.getFirebase();
    private ArrayList<User> listaUsuarios = new ArrayList<>();
    private RecyclerView recUsuarios;
    private FirebaseAuth user = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private boolean resultado = true;
    private ProgressBar prgUser;
    private User meuUser;
    private AdaptadorAmigos rvAdapter ;
    private LinearLayoutManager linearLayoutManager;


    public Amigos()
    {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_amigos, container, false);
        fazerCast(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        iniciarRecycler();
    }

    private void fazerCast(View view)
    {
        srchBuscar = view.findViewById(R.id.edtBuscar_busca);
        txtUsuario = view.findViewById(R.id.txtUsuarios_Busca);
        recUsuarios = view.findViewById(R.id.recBuscaUsuarios_Busca);
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
                            ref.child("nick").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot dados : dataSnapshot.getChildren()) {
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
                        listaUsuarios.add(usuario);
                        rvAdapter.notifyDataSetChanged();
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
        listaUsuarios.clear();
        rvAdapter.notifyDataSetChanged();
    }
    private void iniciarRecycler()
    {
        recUsuarios.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recUsuarios.setLayoutManager(linearLayoutManager);
        rvAdapter = new AdaptadorAmigos(getContext(), listaUsuarios, new CustomClick()
        {
            @Override
            public void onItemClick(View itemView, int position, Button button,
                                    User meuUsuario,User usuario)
            {
                //Toast.makeText(getApplicationContext(), "Amigo adicionado: Posição da Lista"+listaUsuarios.get(position).getNome(), Toast.LENGTH_LONG).show();
                Drawable imgClicked  = getActivity().getResources().getDrawable(R.drawable.ic_amigos);
                Drawable imgOutClicked  = getActivity().getResources().getDrawable(R.drawable.ic_amigos_check);
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
                        ref.child("usuarios").child(user.getUid()).child("amigos").setValue(meuUsuario.getAmigos());
                    } else {
                        Log.d("AMIGOs", "meuUsuario.getAmigos().contains != null ELSE  | Nick usuario " + usuario.getNick() + " E Nick meuUser: " + meuUsuario.getNick());
                        button.setCompoundDrawablesWithIntrinsicBounds(null, null, null, imgClicked);
                        button.setText("Seguindo");
                        meuUsuario.getAmigos().add(usuario.getNick());
                        ref.child("usuarios").child(user.getUid()).child("amigos").setValue(meuUsuario.getAmigos());
                    }

                } else {
                    Log.d("AMIGOs", "meuUsuario.getAmigos() != null ELSE | Nick usuario " + usuario.getNick() + " E Nick meuUser: " + meuUsuario.getNick());
                    button.setCompoundDrawablesWithIntrinsicBounds(null, null, null, imgClicked);
                    button.setText("Seguindo");
                    ArrayList<String> staticList = new ArrayList<>();
                    staticList.add(usuario.getNick());
                    ref.child("usuarios").child(user.getUid()).child("amigos").setValue(staticList);
                }

            }

            @Override
            public void onViewClick(View view, User usuario)
            {
                Toast.makeText(getContext(),"Clicou no usuário: "+ usuario.getNick(),Toast.LENGTH_LONG).show();
            }
        });
        recUsuarios.setAdapter(rvAdapter);
    }

}
