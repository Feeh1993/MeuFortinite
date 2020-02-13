package com.example.meufortinite;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Login extends AppCompatActivity {


    private String accountId;
    private ArrayList<Stats> lifeTimeStat;
    private Button check;
    private ImageButton btnXBX,btnPSN,btnPC;
    private int XBX = 0 ,PSN = 0,PC = 0;

    private String platform = "";
    private EditText username;
    public static final String EXTRA_ACCOUNT = "account";
    public static final String EXTRA_STATS = "stats";

    public  MediaPlayer mp;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fazerCast();

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(platform == "" || username.getText().toString().equals(""))
                {
                    Toast.makeText(Login.this, "Digite o username e selecione uma das plataformas", Toast.LENGTH_SHORT).show();
                    username.setError("Digite o username!");
                }
                else{
                    getAccountData(platform, username.getText().toString());
                }
            }
        });
        btnPSN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (PSN == 1)
                {
                    PSN = 0;
                    btnPSN.setBackgroundResource(R.drawable.bordas_vazias);
                }
                else
                {
                    platform = "psn";
                    btnPSN.setBackgroundResource(R.drawable.bordas_verdes);
                    btnPC.setBackgroundResource(R.drawable.bordas_vazias);
                    btnXBX.setBackgroundResource(R.drawable.bordas_vazias);
                    PSN = 1;
                    XBX = 0;
                    PC = 0;

                }
            }
        });
        btnXBX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (XBX == 1)
                {
                    XBX = 0;
                    btnXBX.setBackgroundResource(R.drawable.bordas_vazias);
                }
                else
                {
                    platform = "xb1";
                    btnXBX.setBackgroundResource(R.drawable.bordas_verdes);
                    btnPC.setBackgroundResource(R.drawable.bordas_vazias);
                    btnPSN.setBackgroundResource(R.drawable.bordas_vazias);
                    XBX = 1;
                    PSN = 0;
                    PC = 0;

                }
            }
        });
        btnPC.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                if (PC == 1)
                {
                    PC = 0;
                    btnPC.setBackgroundResource(R.drawable.bordas_vazias);
                }
                else
                {
                    platform = "pc";
                    btnPC.setBackgroundResource(R.drawable.bordas_verdes);
                    btnXBX.setBackgroundResource(R.drawable.bordas_vazias);
                    btnPSN.setBackgroundResource(R.drawable.bordas_vazias);
                    PC = 1;
                    PSN = 0;
                    XBX = 0;

                }
            }
        });


    }

    private void getAccountData(String p, String u)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.fortnitetracker.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FornightService service =
                retrofit.create(FornightService.class);

        Call<ItemResponse> itemResponseCall =
                service.searchByPlatfrom(p, u);

        itemResponseCall.enqueue(new Callback<ItemResponse>() {
            @Override
            public void onResponse(Call<ItemResponse> call,
                                   Response<ItemResponse> response) {


                if(response.body() == (null))
                {
                    Toast.makeText(Login.this, "Usuário Não encontrado!\n tente novamente", Toast.LENGTH_SHORT).show();
                }
                else if(response.body().getAccountId() == (null))
                {
                    Toast.makeText(Login.this, "Your Username didn't seem to work, maybe you forgot a capital.", Toast.LENGTH_SHORT).show();
                }
                else{

                    accountId = response.body().getAccountId();
                    lifeTimeStat = response.body().getLifeTimeStats();

                    Intent intent = new Intent(Login.this, InfoConta.class);

                    ArrayList<Stats> stats = new ArrayList<>();

                    intent.putExtra(EXTRA_ACCOUNT, accountId);
                    intent.putParcelableArrayListExtra(EXTRA_STATS, lifeTimeStat);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<ItemResponse> call,
                                  Throwable t) {
                Log.d("ENQUEUE", "onFailure: " + t.getMessage());
            }
        });

    }


    private void fazerCast()
    {

        username = findViewById(R.id.edtUserLogin);
        check = findViewById(R.id.btnBuscarLogin);
        btnPC = findViewById(R.id.imbtnPC);
        btnXBX = findViewById(R.id.imbtnXBX);
        btnPSN = findViewById(R.id.imbtnPSN);
        mp = MediaPlayer.create(Login.this,R.raw.forti_theme);
        mp.start();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (mp != null)
                {
                    mp.release();;
                    mp = null;
                }
            }
        });

    }

    private void searchItems() {
    }
}