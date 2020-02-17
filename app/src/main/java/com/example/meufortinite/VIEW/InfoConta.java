package com.example.meufortinite.VIEW;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.meufortinite.DAO.API.FornightService;
import com.example.meufortinite.MODEL.Stats;
import com.example.meufortinite.MODEL.Store;
import com.example.meufortinite.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class InfoConta extends AppCompatActivity
{

    private String acc;
    private List<Stats> stats;
    private String wins;

    private ImageButton btnCopiar,btnShare;
    private Button btnLoja;
    private TextView txtVitorias,txtScore,txtKill,txtKD,txt25Prim,txt10Pri,txt3Pri,txtId;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_conta);
        fazerCast();

        Intent accountIntent = getIntent();
        acc = accountIntent.getStringExtra("account");
        Intent statIntent = getIntent();
        stats = statIntent.getParcelableArrayListExtra("stats");

        wins = stats.get(8).getValue();

        txtVitorias.setText("Voce Tem " + wins + " vitorias");
        txt3Pri.setText("no top 3: " + stats.get(1).getValue() + " vezes");
        txt10Pri.setText(" no top 10: " + stats.get(3).getValue() + "vezes");
        txt25Prim.setText( "Ficou entre os 25 primeiros: " + stats.get(5).getValue() + " vezes");
        txtId.setText("seu ID da conta é: " + acc);
        txtKD.setText("seu K/d é de " + stats.get(11).getValue() + " ");
        txtKill.setText( "Você tem " + stats.get(10).getValue() + " kills");
        txtScore.setText( "Seu score é " + stats.get(6).getValue() + " ");
    }

    private void fazerCast()
    {
        btnLoja = findViewById(R.id.btnLojaInfo);
        btnCopiar = findViewById(R.id.btnCopiarIdInfo);
        btnShare = findViewById(R.id.btnShareIdInfo);
        txtVitorias = findViewById(R.id.txtVitoriasInfo);
        txtScore = findViewById(R.id.txtScoreInfo);
        txtKill = findViewById(R.id.txtKillInfo);
        txtKD = findViewById(R.id.txtKDInfo);
        txt25Prim = findViewById(R.id.txt25PriInfo);
        txt10Pri = findViewById(R.id.txt10PriInfo);
        txt3Pri = findViewById(R.id.txt3PriInfo);
        txtId = findViewById(R.id.txtIDInfo);
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
                btnCopiar.setBackgroundResource(R.drawable.ic_success);
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Texto Copiado",acc);
                clipboard.setPrimaryClip(clipData);
                Toast.makeText(getApplicationContext(),"Id copiado para area de transferencia!",Toast.LENGTH_LONG).show();
            }
        });
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Compartilhando id!",Toast.LENGTH_LONG).show();
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
                Intent storeIntent = new Intent(InfoConta.this, StoreActivity.class);
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
