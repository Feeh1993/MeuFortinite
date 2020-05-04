package com.example.meufortinite.VIEW.FRAGMENT;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.meufortinite.DAO.LOCAL.DatabaseHelper;
import com.example.meufortinite.DAO.REMOTO.ConfiguracaoFirebase;
import com.example.meufortinite.MODEL.GERAL.Amigo;
import com.example.meufortinite.MODEL.GERAL.Usuario;
import com.example.meufortinite.MODEL.INTERFACE.BaseRecyclerAdapter;
import com.example.meufortinite.MODEL.INTERFACE.CustomBusca;
import com.example.meufortinite.MODEL.INTERFACE.CustomMsgeNtfc;
import com.example.meufortinite.R;
import com.example.meufortinite.VIEW.ADAPTER.AdaptadorBusca;
import com.example.meufortinite.VIEW.ADAPTER.AdaptadorChat;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.tomer.fadingtextview.FadingTextView;

import java.util.ArrayList;

import static com.makeramen.roundedimageview.RoundedDrawable.TAG;

public class Buscar extends Fragment
{
    private FadingTextView txtBuscar,txtInfo;
    private LinearLayout lnlTopo,lnlFundo;
    private Switch swtchMeuHS,swtchSeuHS,swtchDuo,swtchSQD;
    private ImageButton img;
    private LottieAnimationView animationView;
    private DatabaseReference ref = ConfiguracaoFirebase.getFirebase();
    private RecyclerView recbusca;
    private DatabaseHelper db;

    private String TAG = "BUSCAR_";
    private ArrayList<Amigo> listUsers = new ArrayList<>(),meuUser = new ArrayList<>();
    private AdaptadorBusca adapter;

    public Buscar()
    {
    }

    @Override
    public void onPause()
    {
        super.onPause();
        animationView.cancelAnimation();
        img.setVisibility(View.VISIBLE);
        animationView.setVisibility(View.GONE);

        lnlTopo.setVisibility(View.VISIBLE);
        txtInfo.setVisibility(View.GONE);
        txtInfo.clearAnimation();
        txtBuscar.setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_buscar, container, false);
        db = new DatabaseHelper(getContext());
        meuUser.addAll(db.recuperaAmigos());
        fazerCast(view);
        recbusca.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AdaptadorBusca(getContext(), listUsers, meuUser.get(0).getId(), new CustomBusca() {
            @Override
            public void onCopiar(ImageButton button, int position, Amigo usuario) {
                // inserir funcao copiar id
                ClipboardManager clipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("text",usuario.getNick());
                Toast.makeText(getContext(),"Copiado com sucesso",Toast.LENGTH_LONG).show();
                button.setImageResource(R.drawable.ic_success);
            }

            @Override
            public void onMessagemClick(ImageButton button, int position, Amigo usuario)
            {
                ref.child("novaMensagem").child(meuUser.get(0).getId()).setValue(usuario.getId());
            }
        });
        recbusca.setAdapter(adapter);
        img.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (swtchDuo.isChecked() || swtchSQD.isChecked())
                {
                    animationView.playAnimation();
                    animationView.setVisibility(View.VISIBLE);
                    img.setVisibility(View.GONE);

                    lnlTopo.setVisibility(View.GONE);
                    txtBuscar.setVisibility(View.VISIBLE);
                    lnlFundo.setVisibility(View.VISIBLE);
                    txtInfo.setVisibility(View.VISIBLE);
                    Log.d("BUSCAR_","RESUMO: \n SWITCH MHS: "+swtchMeuHS.getShowText()+
                            "\nSWITCH SHS: "+swtchSeuHS.getShowText()+
                            "\nSWITCH DUO: "+swtchDuo.getShowText()+
                            "\nSWITCH SQD: "+swtchSQD.getShowText()
                    );
                    verificarSwitchs(meuUser.get(0).getId());
                }
                else
                {
                    Toast.makeText(getContext(),"Voce precisa selecionar pelo menos ESQUADRÕES OU DUPLAS",Toast.LENGTH_LONG).show();
                }
            }
        });
        animationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                animationView.cancelAnimation();
                img.setVisibility(View.VISIBLE);
                animationView.setVisibility(View.GONE);

                lnlTopo.setVisibility(View.VISIBLE);
                txtInfo.setVisibility(View.GONE);
                txtInfo.clearAnimation();
                txtBuscar.setVisibility(View.GONE);
            }
        });



     return view;
    }

    private void verificarSwitchs(String meuId)
    {
        String mHs = "semhead",hsA = "semhead",tipoSquad = "";
        int qtd = 0;

        if (swtchSeuHS.isChecked())
        {
            Log.d(TAG, "verificarSwitchs:swtchSeuHS TRUE");
            hsA = "comhead";
        }
        if (swtchMeuHS.isChecked())
        {
            Log.d(TAG, "verificarSwitchs:swtchMeuHS TRUE");
            mHs = "comhead";
        }
        if (swtchSQD.isChecked())
        {
            Log.d(TAG, "verificarSwitchs:swtchSQD TRUE");
            qtd = 4;
            tipoSquad = "squad";
        }
        if (swtchDuo.isChecked())
        {
            qtd = 2;
            Log.d(TAG, "verificarSwitchs:swtchDuo TRUE");
            tipoSquad = "dupla";
        }
            listUsers.clear();
            iniciarBusca(mHs,hsA,tipoSquad,meuId,qtd);
    }

    private void iniciarBusca(String mHeadset,String headsetA,String tipoSquad,String meuid, int qtd)
    {
        ref.child("pareamento").child(mHeadset).child(tipoSquad).child(meuid).setValue(meuUser.get(0));
        ref.child("pareamento").child(headsetA).child(tipoSquad).limitToLast(qtd).addChildEventListener(new ChildEventListener()
        {
            @Override public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                Amigo amigo = dataSnapshot.getValue(Amigo.class);
                listUsers.add(amigo);
                adapter.notifyDataSetChanged();
                recbusca.scrollToPosition(adapter.getItemCount() - 1);
            }

            @Override public void onChildChanged(DataSnapshot dataSnapshot, String s)
            {

            }

            @Override public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG,"Removido "+ dataSnapshot.getValue(Amigo.class).toString());
                listUsers.remove(dataSnapshot.getValue(Amigo.class));
                adapter.notifyDataSetChanged();
            }

            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s)
            {

            }

            @Override public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }

    public void fazerCast(View view)
    {
        lnlTopo = (LinearLayout) view.findViewById(R.id.lnlTopo_buscar);
        lnlFundo = (LinearLayout) view.findViewById(R.id.lnlFundo_buscar);
        swtchDuo = (Switch) view.findViewById(R.id.swtchDuo_buscar);
        swtchSQD = (Switch) view.findViewById(R.id.swtchSquad_buscar);
        swtchMeuHS = (Switch) view.findViewById(R.id.swtchMeuHeadSet_buscar);
        swtchSeuHS = (Switch) view.findViewById(R.id.swtchSeuHeadSet_buscar);
        txtBuscar = (FadingTextView) view.findViewById(R.id.fdngtxtBuscando_buscar);
        txtInfo = (FadingTextView) view.findViewById(R.id.fdngtxtTopo_buscar);
        img = (ImageButton)view.findViewById(R.id.imgbSearch_BUSCAR);
        animationView = (LottieAnimationView) view.findViewById(R.id.animation_view);
        recbusca = (RecyclerView)view.findViewById(R.id.recBusca);
    }

}
