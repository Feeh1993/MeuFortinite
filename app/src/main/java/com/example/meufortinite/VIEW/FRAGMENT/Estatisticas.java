package com.example.meufortinite.VIEW.FRAGMENT;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meufortinite.DAO.API.FornightService;
import com.example.meufortinite.DAO.LOCAL.DatabaseHelper;
import com.example.meufortinite.DAO.REMOTO.ConfiguracaoFirebase;
import com.example.meufortinite.MODEL.Stats;
import com.example.meufortinite.MODEL.Store;
import com.example.meufortinite.MODEL.Usuario;
import com.example.meufortinite.R;
import com.example.meufortinite.VIEW.Login;
import com.example.meufortinite.VIEW.StoreActivity;
import com.google.firebase.database.DatabaseReference;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Estatisticas extends Fragment
{
    private List<Stats> stats;
    private DatabaseReference ref = ConfiguracaoFirebase.getFirebase();

    private ImageButton btnCopiar,btnShare;
    private Button btnLoja,btnLogout;
    private TextView txtVitorias,txtScore,txtKill,txtKD,txt25Prim,txt10Pri,txt3Pri,txtId;
    private DatabaseHelper db;
    private ArrayList<Usuario> usuarios = new ArrayList<>();

    public String vitorias = "",trespri = "",dezpri = "",vintecincopri = "",id = "",kd = "",kill = "",score = "";

    public Estatisticas()
    {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_estatisticas, container, false);
        db = new DatabaseHelper(getContext());

        fazerCast(view);
        if (db.getQTDUsuarios() > 0)
        {
            usuarios.clear();
            usuarios.addAll(db.recuperarUsuarios());
            Log.d("INFOCONTA_","USUARIO BANCO LOCAL ADICIONADO ");
            id = usuarios.get(0).getId();
            vitorias = usuarios.get(0).getVitorias();
            trespri = usuarios.get(0).getTrespri();
            dezpri = usuarios.get(0).getDezpri();
            vintecincopri = usuarios.get(0).getVintecincopri();
            kd = usuarios.get(0).getKd();
            kill = usuarios.get(0).getKill();
            score =  usuarios.get(0).getScore();
            txtVitorias.setText("Voce Tem " + vitorias + " vitorias");
            txt3Pri.setText("no top 3: " + trespri + " vezes");
            txt10Pri.setText(" no top 10: " + dezpri + "vezes");
            txt25Prim.setText( "Ficou entre os 25 primeiros: " + vintecincopri + " vezes");
            txtId.setText("seu ID da conta é: " + id);
            txtKD.setText("seu K/d é de " + kd + " ");
            txtKill.setText( "Você tem " + kill + " kills");
            txtScore.setText( "Seu score é " + score + " ");
        }

    return view;
    }

    private void fazerCast(View view)
    {
        btnLogout = view.findViewById(R.id.btnLogout);
        btnLoja = view.findViewById(R.id.btnLojaInfo);
        btnCopiar = view.findViewById(R.id.btnCopiarIdInfo);
        btnShare = view.findViewById(R.id.btnShareIdInfo);
        txtVitorias = view.findViewById(R.id.txtVitoriasInfo);
        txtScore = view.findViewById(R.id.txtScoreInfo);
        txtKill = view.findViewById(R.id.txtKillInfo);
        txtKD = view.findViewById(R.id.txtKDInfo);
        txt25Prim = view.findViewById(R.id.txt25PriInfo);
        txt10Pri = view.findViewById(R.id.txt10PriInfo);
        txt3Pri = view.findViewById(R.id.txt3PriInfo);
        txtId = view.findViewById(R.id.txtIDInfo);
        btnLoja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoja();
            }
        });
        btnCopiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                txtId.setTextColor(getResources().getColor(R.color.verde));
                btnCopiar.setImageResource(R.drawable.ic_success);
               // ClipboardManager clipboard = (ClipboardManager) ContextCompat.getSystemService(Context.CLIPBOARD_SERVICE);
                //ClipData clipData = ClipData.newPlainText("Texto Copiado",id);
               // clipboard.setPrimaryClip(clipData);
                Toast.makeText(getContext(),"Id copiado para area de transferencia!",Toast.LENGTH_LONG).show();
            }
        });
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"Compartilhando id!",Toast.LENGTH_LONG).show();
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(getContext(),"ID desconectado :)",Toast.LENGTH_LONG).show();
                ref.child(id).child("estado").setValue("deslogado");
                startActivity(new Intent(getContext(), Login.class));
            }
        });

    }

    private void showLoja()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.fortnitetracker.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofit.create(FornightService.class).getStore().enqueue(new Callback<List<Store>>() {
            @Override
            public void onResponse(Call<List<Store>> call, Response<List<Store>> response) {
                Intent storeIntent = new Intent(getContext(), StoreActivity.class);
                ArrayList<Store> stores = new ArrayList<>();
                stores.addAll(response.body());
                storeIntent.putParcelableArrayListExtra("stores", stores);
                startActivity(storeIntent);
            }

            @Override
            public void onFailure(Call<List<Store>> call, Throwable t) {
                Log.d("InfoConta", t.getMessage());
            }
        });
    }


}
