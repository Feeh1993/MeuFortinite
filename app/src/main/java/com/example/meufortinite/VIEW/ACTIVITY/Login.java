package com.example.meufortinite.VIEW.ACTIVITY;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.meufortinite.DAO.LOCAL.DatabaseHelper;
import com.example.meufortinite.DAO.REMOTO.ConfiguracaoFirebase;
import com.example.meufortinite.DAO.REMOTO.PermissionsUtils;
import com.example.meufortinite.MODEL.GERAL.Amigo;
import com.example.meufortinite.MODEL.GERAL.Usuario;
import com.example.meufortinite.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.stephentuso.welcome.WelcomeHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class Login extends AppCompatActivity
{
    private DatabaseReference ref = ConfiguracaoFirebase.getFirebase();
    private FirebaseAuth mAuth = ConfiguracaoFirebase.getFirebaseAutenticacao();

    //admin
    private FrameLayout frmlPostAdm;
    private EditText edtSenhaAdm;
    private Button btnLogarAdm;
    public ProgressBar prgLoginAdm;

    //pro
    private Button btnPro;

   //login
    private EditText edtUsername,edtPassword,edtEmail;
    private Button btnCadastrar,btnConectar;
    private ImageButton btnShowPassword;
    public ProgressBar prgConectar;

    WelcomeHelper welcomeScreen;

    String[] permissoes = new String[]
            {
                    Manifest.permission.INTERNET,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.ACCESS_WIFI_STATE
            };


    private DatabaseHelper db;
    private ArrayList<Usuario> usuarios = new ArrayList<>();

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
        btnLogarAdm.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {
                if (!edtSenhaAdm.getText().toString().isEmpty())
                {
                    prgLoginAdm.setVisibility(View.VISIBLE);
                    btnLogarAdm.setVisibility(View.GONE);
                    ref.child("admin").addListenerForSingleValueEvent(new ValueEventListener()
                    {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                        {
                            Log.d("LOGIN_ACVTY","Senha= "+edtSenhaAdm.getText().toString()+"\n Senha Server: "+dataSnapshot.getValue().toString());
                            if (edtSenhaAdm.getText().toString().equals(dataSnapshot.getValue().toString()))
                            {
                                prgLoginAdm.setVisibility(View.GONE);
                                Snackbar.make(v, "Seja bem vindo Administrador :) ", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                                iniciarAnimacao();
                                startActivity(new Intent(getApplicationContext(), AreaAdministrativa.class));
                            }
                            else
                            {
                                prgLoginAdm.setVisibility(View.GONE);
                                btnLogarAdm.setVisibility(View.VISIBLE);
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
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtUsername.getVisibility() == View.GONE)
                {
                    edtUsername.setVisibility(View.VISIBLE);
                }
                else
                {
                    if(edtPassword.getText().toString().equals("") || edtUsername.getText().toString().equals("")||edtEmail.getText().toString().equals(""))
                    {
                        Snackbar.make(getCurrentFocus(),"Digite o username,senha e email e tente novamente", BaseTransientBottomBar.LENGTH_LONG).show();
                    }
                    else
                    {
                        prgConectar.setVisibility(View.VISIBLE);
                        prgConectar.setElevation(2);
                        closeKeyboard(view);
                        salvarUsuario(edtPassword.getText().toString(),edtUsername.getText().toString(),edtEmail.getText().toString());
                    }
                }
            }
        });
        btnConectar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (edtUsername.getText().toString().equals("a1d9m9i3n"))
                {
                    btnPro.setVisibility(View.GONE);
                    iniciarAnimacao();
                }
                else
                    {
                        if(edtPassword.getText().toString().equals("") ||edtEmail.getText().toString().equals(""))
                        {
                            Snackbar.make(getCurrentFocus(),"Digite senha,email e tente novamente", BaseTransientBottomBar.LENGTH_LONG).show();
                        }
                        else
                            {
                                prgConectar.setVisibility(View.VISIBLE);
                                prgConectar.setElevation(2);
                                closeKeyboard(v);
                                conectar(edtPassword.getText().toString(),edtEmail.getText().toString());
                            }
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

    private void conectar(String password, String email)
    {
        mAuth.signInWithEmailAndPassword(
                email,
                password
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if (task.isSuccessful())
                {
                    recuperarBancoRemoto();
                } else{
                    prgConectar.setVisibility(View.GONE);
                    new SweetAlertDialog(Login.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Erro de Cadastro!")
                            .setContentText("Verifique os dados e tente novamente\n Ou digite seu username e cadastre-se!")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener()
                            {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog)
                                {
                                    edtUsername.setVisibility(View.VISIBLE);
                                    sweetAlertDialog.dismiss();
                                    //startActivity(new Intent(getApplicationContext(), PainelPrincipal.class));
                                }
                            })
                            .show();
                }
            }
        });
    }

    private void recuperarBancoRemoto()
    {
        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ref.child("usuarios").child(id).addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                Amigo user = dataSnapshot.getValue(Amigo.class);
                db.inserirUser(new Usuario(user.getId(),DatabaseHelper.getDateTime(),user.getNick()));
                db.inserirAmigo(user);
                prgConectar.setVisibility(View.GONE);
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

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void salvarUsuario(String password, final String nickname, String email)
    {
        mAuth.createUserWithEmailAndPassword(
                email,
                password
        ).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    //salvando usuário logado pela primeira vez
                    Usuario user = new Usuario(id,DatabaseHelper.getDateTime(),nickname);
                    db.inserirUser(user);
                    salvar(user);

                } else
                {
                    String erro = "";
                    try
                    {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e)
                    {
                        erro = "Escolha uma senha que contenha, letras e números.";
                        prgConectar.setVisibility(View.GONE);
                    } catch (FirebaseAuthInvalidCredentialsException e)
                    {
                        erro = "Email indicado não é válido.";
                        prgConectar.setVisibility(View.GONE);
                    } catch (FirebaseAuthUserCollisionException e)
                    {
                        erro = "Já existe uma conta com esse e-mail.";
                        prgConectar.setVisibility(View.GONE);
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    Toast.makeText(Login.this, "Erro ao cadastrar usuário: " + erro, Toast.LENGTH_LONG).show();
                    //  progressBarCC.setVisibility(View.INVISIBLE);
                }
            }
        });
    }


    private void fazerCast()
    {
        prgConectar = findViewById(R.id.prgbBuscaLogin);
        edtEmail = findViewById(R.id.edtEmailLogin);
        edtUsername = findViewById(R.id.edtUserLogin);
        edtPassword = findViewById(R.id.edtSenhaLog);
        btnConectar = findViewById(R.id.btnLogin);
        btnPro = findViewById(R.id.btnPro);
        btnCadastrar = findViewById(R.id.btnCadLogin);
        btnShowPassword = findViewById(R.id.imgPassLog);


        //adm
        frmlPostAdm = findViewById(R.id.frml_adm);
        edtSenhaAdm = findViewById(R.id.edtSenhaAdmLogin);
        btnLogarAdm = findViewById(R.id.btnAdmLogin);
        prgLoginAdm = findViewById(R.id.prgbLoginAdm);
    }
    private void iniciarAnimacao()
    {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.anim_fadein);
        anim.reset();
        if (frmlPostAdm != null && frmlPostAdm.getVisibility() == View.GONE)
        {
            frmlPostAdm.clearAnimation();
            frmlPostAdm.startAnimation(anim);
            frmlPostAdm.setVisibility(View.VISIBLE);
        }
        else if (frmlPostAdm != null && frmlPostAdm.getVisibility() == View.VISIBLE)
        {
            frmlPostAdm.clearAnimation();
            anim = AnimationUtils.loadAnimation(this,R.anim.anim_fadeout);
            frmlPostAdm.startAnimation(anim);
            frmlPostAdm.setVisibility(View.GONE);
        }
        if (edtSenhaAdm != null)
        {
            edtSenhaAdm.clearAnimation();
            btnLogarAdm.clearAnimation();
            edtSenhaAdm.startAnimation(anim);
            btnLogarAdm.startAnimation(anim);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
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

    private void salvar(Usuario usuario)
    {
        String nickname = usuario.getNickname().replace(" ","");

        ref.child("nick").child(nickname).setValue(usuario.getId());

        Amigo amigo = new Amigo(0,nickname,"logado", usuario.getId(), null);
        // salvar no banco local como amigo para recuperar futuramente; usuario lista0
        db.inserirAmigo(amigo);

        Map<String, Object> userMap = amigo.mapearUsuario();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/usuarios/"+amigo.getId(), userMap);
        ref.updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                prgConectar.setVisibility(View.GONE);
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
    private void closeKeyboard(View view)
    {
        final InputMethodManager imm = (InputMethodManager) getApplication().getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
