package com.example.meufortinite;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.meufortinite.DBFirebase.ConfiguracaoFirebase;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.aviran.cookiebar2.CookieBar;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Login extends AppCompatActivity
{
    private DatabaseReference ref = ConfiguracaoFirebase.getFirebase();
    private FirebaseAuth user = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private FrameLayout frmlPost;
    private EditText edtSenha;
    private Button btnLogar;

    private String accountId;
    private ArrayList<Stats> lifeTimeStat;
    private Button check;
    private ImageButton btnXBX,btnPSN,btnPC;
    private int XBX = 0 ,PSN = 0,PC = 0;

    private String platform = "";
    private EditText username;
    public static final String EXTRA_ACCOUNT = "account";
    public static final String EXTRA_STATS = "stats";

    public ProgressBar prgLogin,prgBuscar;


    public  MediaPlayer mp;

    @Override
    protected void onStart()
    {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fazerCast();
        btnLogar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {
                if (!edtSenha.getText().toString().isEmpty())
                {
                    prgLogin.setVisibility(View.VISIBLE);
                    btnLogar.setVisibility(View.GONE);
                    ref.child("admin").addListenerForSingleValueEvent(new ValueEventListener()
                    {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                        {
                            Log.d("Login","Senha= "+edtSenha.getText().toString()+"\n Senha Server: "+dataSnapshot.getValue().toString());
                            if (edtSenha.getText().toString().equals(dataSnapshot.getValue().toString()))
                            {
                                prgLogin.setVisibility(View.GONE);
                                Snackbar.make(v, "Seja bem vindo Administrador :) ", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                                iniciarAnimacao();
                                startActivity(new Intent(getApplicationContext(),AreaAdministrativa.class));
                            }
                            else
                            {
                                prgLogin.setVisibility(View.GONE);
                                btnLogar.setVisibility(View.VISIBLE);
                                Snackbar.make(v, "Erro!Tente novamente", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
        check.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (username.getText().toString().equals("a1d9m9i3n"))
                {
                    iniciarAnimacao();
                }
                else
                    {
                        if(platform == "" || username.getText().toString().equals(""))
                        {
                            Toast.makeText(Login.this, "Digite o username e selecione uma das plataformas", Toast.LENGTH_SHORT).show();
                            username.setError("Digite o username!");
                        }
                        else{
                            prgBuscar.setVisibility(View.VISIBLE);
                            prgBuscar.setElevation(2);
                            getAccountData(platform, username.getText().toString());
                        }
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
                    prgBuscar.setVisibility(View.GONE);
                }
                else if(response.body().getAccountId() == (null))
                {
                    Toast.makeText(Login.this, "Your Username didn't seem to work, maybe you forgot a capital.", Toast.LENGTH_SHORT).show();
                    prgBuscar.setVisibility(View.GONE);
                }
                else{

                    accountId = response.body().getAccountId();
                    lifeTimeStat = response.body().getLifeTimeStats();

                    Intent intent = new Intent(Login.this, InfoConta.class);

                    ArrayList<Stats> stats = new ArrayList<>();

                    intent.putExtra(EXTRA_ACCOUNT, accountId);
                    intent.putParcelableArrayListExtra(EXTRA_STATS, lifeTimeStat);
                    prgBuscar.setVisibility(View.GONE);
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
        prgBuscar = findViewById(R.id.prgbBuscaLogin);
        prgLogin = findViewById(R.id.prgbFRMLogin);
        username = findViewById(R.id.edtUserLogin);
        check = findViewById(R.id.btnBuscarLogin);
        btnPC = findViewById(R.id.imbtnPC);
        btnXBX = findViewById(R.id.imbtnXBX);
        btnPSN = findViewById(R.id.imbtnPSN);
        frmlPost = findViewById(R.id.frml_post);
        edtSenha = findViewById(R.id.edtSenhaAdmLogin);
        btnLogar = findViewById(R.id.btnConectarLogin);
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
    private void iniciarAnimacao()
    {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.anim_fadein);
        anim.reset();
        if (frmlPost != null && frmlPost.getVisibility() == View.GONE)
        {
            frmlPost.clearAnimation();
            frmlPost.startAnimation(anim);
            frmlPost.setVisibility(View.VISIBLE);
        }
        else if (frmlPost != null && frmlPost.getVisibility() == View.VISIBLE)
        {
            frmlPost.clearAnimation();
            anim = AnimationUtils.loadAnimation(this,R.anim.anim_fadeout);
            frmlPost.startAnimation(anim);
            frmlPost.setVisibility(View.GONE);
        }
        if (edtSenha != null)
        {
            edtSenha.clearAnimation();
            btnLogar.clearAnimation();
            edtSenha.startAnimation(anim);
            btnLogar.startAnimation(anim);
        }
        int SPLASH_DISPLAY_LENGTH = 1000;
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {

            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    private void searchItems() {
    }
}