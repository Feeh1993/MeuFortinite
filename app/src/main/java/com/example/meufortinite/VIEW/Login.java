package com.example.meufortinite.VIEW;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.meufortinite.DAO.API.FornightService;
import com.example.meufortinite.DAO.LOCAL.DatabaseHelper;
import com.example.meufortinite.DAO.REMOTO.ConfiguracaoFirebase;
import com.example.meufortinite.DAO.REMOTO.PermissionsUtils;
import com.example.meufortinite.MODEL.ItemResponse;
import com.example.meufortinite.MODEL.User;
import com.example.meufortinite.MODEL.Usuario;
import com.example.meufortinite.R;
import com.example.meufortinite.MODEL.Stats;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.stephentuso.welcome.WelcomeHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
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
    private Button btnLogar,btnPro;

    private String accountId;
    private ArrayList<Stats> lifeTimeStat;
    private Button check;
    private ImageButton btnXBX,btnPSN,btnPC;
    private int XBX = 0 ,PSN = 0,PC = 0;

    private String platform = "";
    private EditText username;
    public static final String EXTRA_ACCOUNT = "account";
    public static final String EXTRA_STATS = "stats";
    WelcomeHelper welcomeScreen;

    String[] permissoes = new String[]
            {
                    Manifest.permission.INTERNET,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.ACCESS_WIFI_STATE
            };
    public ProgressBar prgLogin,prgBuscar;


    public  MediaPlayer mp;
    private DatabaseHelper db;
    private ArrayList<Usuario> usuarios = new ArrayList<>();
    private String name = "";

    @Override
    protected void onStart()
    {
        super.onStart();
        db = new DatabaseHelper(getApplicationContext());
    }
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        welcomeScreen.onSaveInstanceState(outState);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        recuperarBancoLocal();
        welcomeScreen = new WelcomeHelper(this, TelaBoasVindas.class);
        welcomeScreen.show(savedInstanceState);
        PermissionsUtils.ActivePermissions(this,permissoes,1);
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
                            Log.d("LOGIN_ACVTY","Senha= "+edtSenha.getText().toString()+"\n Senha Server: "+dataSnapshot.getValue().toString());
                            if (edtSenha.getText().toString().equals(dataSnapshot.getValue().toString()))
                            {
                                prgLogin.setVisibility(View.GONE);
                                Snackbar.make(v, "Seja bem vindo Administrador :) ", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                                iniciarAnimacao();
                                startActivity(new Intent(getApplicationContext(), AreaAdministrativa.class));
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
        btnPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Virando Pró",Toast.LENGTH_LONG).show();
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

                    name = response.body().getEpicUserHandle();
                    accountId = response.body().getAccountId();
                    lifeTimeStat = response.body().getLifeTimeStats();

                    String[] score = lifeTimeStat.get(6).getValue().split(",");

                    //salvando usuário logado pela primeira vez
                    Usuario user = new Usuario(accountId,score[0]+"."+score[1],lifeTimeStat.get(11).getValue(),
                            lifeTimeStat.get(10).getValue(),lifeTimeStat.get(5).getValue(),
                            lifeTimeStat.get(3).getValue(),lifeTimeStat.get(1).getValue(),
                            lifeTimeStat.get(8).getValue(),DatabaseHelper.getDateTime());
                    Log.d("LOGIN_ACVTY", "DADOS USER: " +
                            "\n ID: " + user.getId()+
                            "\n KILL: "+ user.getKill()+
                            "\n KD: " + user.getKd()+
                            "\n SCORE: "+ user.getScore()+
                            "\n 3PRI: " + user.getTrespri()+
                            "\n 10PRI: "+ user.getDezpri()+
                            "\n 25PRI: " + user.getVintecincopri()+
                            "\n Vitoria: "+ user.getVitorias()+
                            "\n HORAS: "+user.getCriado()+
                            "\n"+ lifeTimeStat.get(0).getKey()+":" + lifeTimeStat.get(0).getValue()+
                            "\n"+ lifeTimeStat.get(2).getKey()+":" + lifeTimeStat.get(2).getValue()+
                            "\n"+ lifeTimeStat.get(4).getKey()+":" + lifeTimeStat.get(4).getValue()+
                            "\n"+ lifeTimeStat.get(7).getKey()+":" + lifeTimeStat.get(7).getValue()+
                            "\n"+ lifeTimeStat.get(9).getKey()+":" + lifeTimeStat.get(9).getValue()
                          );
                    //salvando dados de acesso localmente
                    db.inserirUser(user);

                    Log.d("LOGIN_ACVTY", "DADOS USER: \n ID: " + user.getId()+
                            "\n KILL: "+ user.getKill());
                    Log.d("LOGIN_ACVTY", "Banco atualizado: " + db.getQTDUsuarios()+" usuarios salvos");

                    //SALVANDO USUARIO NO BANCO FIREBASE
                    salvar(user.id,name);
                    ref.child("nick").child(name).setValue(user.id);
                }
            }

            @Override
            public void onFailure(Call<ItemResponse> call,
                                  Throwable t) {
                Log.d("LOGIN_ACVTY", "onFailure: " + t.getMessage());
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
        btnPro = findViewById(R.id.btnPro);

        frmlPost = findViewById(R.id.frml_post);
        edtSenha = findViewById(R.id.edtSenhaAdmLogin);
        btnLogar = findViewById(R.id.btnConectarLogin);

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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int result : grantResults)
        {
            if (result == PackageManager.PERMISSION_DENIED)
            {
                ativeasPermissoes();
                return;
            }
        }
    }
    private void ativeasPermissoes()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões negadas");
        builder.setMessage("Para utilizar este app, é necessário aceitar as permissões");
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    //VERIFICA SE O BANCO LOCAL NÃO ESTÁ VAZIO
    private void recuperarBancoLocal()
    {
        try
        {
            if (db.getQTDUsuarios() > 0 )
            {
                usuarios.addAll( db.recuperarUsuarios());
                Log.d("LOGIN_ACVTY",usuarios.get(0).getId());
            }
        }
        catch (NullPointerException e)
        {
            usuarios = null;
            Log.d("LOGIN_ACVTY","VAZIO ESSA PORRA");
        }
    }

    private void salvar(final String id,String nick)
    {

        User usuario = new User(0,nick,"logado", id, null);
        Map<String, Object> userMap = usuario.mapearUsuario();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/usuarios/"+id, userMap);
        ref.updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                prgBuscar.setVisibility(View.GONE);
                new SweetAlertDialog(Login.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Cadastrado!")
                        .setContentText("Seja bem vindo a nossa plataforma!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener()
                        {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog)
                            {
                                startActivity(new Intent(getApplicationContext(), PainelPrincipal.class));
                            }
                        })
                        .show();
            }
        });

    }

}