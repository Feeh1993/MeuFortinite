package com.example.meufortinite.VIEW;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.meufortinite.DAO.LOCAL.DatabaseHelper;
import com.example.meufortinite.DAO.REMOTO.ConfiguracaoFirebase;
import com.example.meufortinite.MODEL.GERAL.Avatar;
import com.example.meufortinite.R;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class SelecionarAvatar extends AppCompatActivity
{
    private Button btnLendario,btnEpico,btnMitico;
    private ArrayList<ImageButton> avatares = new ArrayList<>();
    private ImageButton imgAV1T,imgAV2T,imgAV3T,imgAV4T,imgAV5T,imgAV6T,imgAV7T,imgAV8T;
    private ImageButton imgAV9T,imgAV10T,imgAV11T,imgAV12T,imgAV13T,imgAV14T,imgAV15T,imgAV16T;
    private ImageButton imgAV17T,imgAV18T,imgAV19T,imgAV20T,imgAV21T,imgAV22T,imgAV23T,imgAV24T;
    private ImageButton imgAV25T,imgAV26T,imgAV27T,imgAV28T,imgAV29T,imgAV30T,imgAV31T,imgAV32T;
    private int cont = 0;
    private DatabaseHelper db;
    private int id = 1;

    private DatabaseReference ref = ConfiguracaoFirebase.getFirebase();
    private String idUser = "";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecao_avatar);
        db = new DatabaseHelper(getApplicationContext());
        Bundle bundle = getIntent().getExtras();
        idUser = bundle.getString("id_user");
        if (db.getQTDAvatares() > 0)
        {
            id = bundle.getInt("id_avt");
            Log.d("SELECIONARAVATAR","id: "+id+"\n e idUser: "+idUser);
        }
        fazerCast();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void fazerCast()
    {
        btnEpico = findViewById(R.id.btnEpicPro);
        btnMitico = findViewById(R.id.btnMiticoPro);
        btnLendario = findViewById(R.id.btnLendarioPro);
        imgAV1T = findViewById(R.id.imgAV1T);
        imgAV2T = findViewById(R.id.imgAV2T);
        imgAV3T = findViewById(R.id.imgAV3T);
        imgAV4T = findViewById(R.id.imgAV4T);
        imgAV5T = findViewById(R.id.imgAV5T);
        imgAV6T = findViewById(R.id.imgAV6T);
        imgAV7T = findViewById(R.id.imgAV7T);
        imgAV8T = findViewById(R.id.imgAV8T);
        imgAV9T = findViewById(R.id.imgAV9T);
        imgAV10T = findViewById(R.id.imgAV10T);
        imgAV11T = findViewById(R.id.imgAV11T);
        imgAV12T = findViewById(R.id.imgAV12T);
        imgAV13T = findViewById(R.id.imgAV13T);
        imgAV14T = findViewById(R.id.imgAV14T);
        imgAV15T = findViewById(R.id.imgAV15T);
        imgAV16T = findViewById(R.id.imgAV16T);
        imgAV17T = findViewById(R.id.imgAV17T);
        imgAV18T = findViewById(R.id.imgAV18T);
        imgAV19T = findViewById(R.id.imgAV19T);
        imgAV20T = findViewById(R.id.imgAV20T);
        imgAV21T = findViewById(R.id.imgAV21T);
        imgAV22T = findViewById(R.id.imgAV22T);
        imgAV23T = findViewById(R.id.imgAV23T);
        imgAV24T = findViewById(R.id.imgAV24T);
        imgAV25T = findViewById(R.id.imgAV25T);
        imgAV26T = findViewById(R.id.imgAV26T);
        imgAV27T = findViewById(R.id.imgAV27T);
        imgAV28T = findViewById(R.id.imgAV28T);
        imgAV29T = findViewById(R.id.imgAV29T);
        imgAV30T = findViewById(R.id.imgAV30T);
        imgAV31T = findViewById(R.id.imgAV31T);
        imgAV32T = findViewById(R.id.imgAV32T);
        // dismiss(); para encerrar dialog
        //
        avatares.add(imgAV1T);
        avatares.add(imgAV2T);
        avatares.add(imgAV3T);
        avatares.add(imgAV4T);
        avatares.add(imgAV5T);
        avatares.add(imgAV6T);
        avatares.add(imgAV7T);
        avatares.add(imgAV8T);

        avatares.add(imgAV9T);
        avatares.add(imgAV10T);
        avatares.add(imgAV11T);
        avatares.add(imgAV12T);
        avatares.add(imgAV13T);
        avatares.add(imgAV14T);
        avatares.add(imgAV15T);
        avatares.add(imgAV16T);

        avatares.add(imgAV17T);
        avatares.add(imgAV18T);
        avatares.add(imgAV19T);
        avatares.add(imgAV20T);
        avatares.add(imgAV21T);
        avatares.add(imgAV22T);
        avatares.add(imgAV23T);
        avatares.add(imgAV24T);

        avatares.add(imgAV25T);
        avatares.add(imgAV26T);
        avatares.add(imgAV27T);
        avatares.add(imgAV28T);
        avatares.add(imgAV29T);
        avatares.add(imgAV30T);
        avatares.add(imgAV31T);
        avatares.add(imgAV32T);

       // iniciarLibPag();


        btnMitico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(SelecionarAvatar.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                        .setTitleText("Pacote Mitico")
                        .setCustomImage(R.drawable.ic_premium)
                        .setContentText("Deseja comprar este pacote?")
                        .setConfirmText("Sim")
                        .setCancelText("Não")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                finish();
                                //ADICIONAR FUNÇÃO DE PAGAMENTO
                                configurarPagamento(0);
                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog)
                            {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();
            }
        });
        btnEpico.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                new SweetAlertDialog(SelecionarAvatar.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                        .setTitleText("Pacote Epico")
                        .setCustomImage(R.drawable.ic_premium)
                        .setContentText("Deseja comprar este pacote?")
                        .setConfirmText("Sim")
                        .setCancelText("Não")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                finish();
                                //ADICIONAR FUNÇÃO DE PAGAMENTO
                                configurarPagamento(1);
                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog)
                            {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();
            }
        });
        btnLendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(SelecionarAvatar.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                        .setTitleText("Pacote Lendário")
                        .setCustomImage(R.drawable.ic_premium)
                        .setContentText("Deseja comprar este pacote?")
                        .setConfirmText("Sim")
                        .setCancelText("Não")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                finish();
                                //ADICIONAR FUNÇÃO DE PAGAMENTO
                                configurarPagamento(2);
                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog)
                            {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();
            }
        });

        for (int i = 0; i < avatares.size(); i++)
        {
            final int x = i;
            avatares.get(i).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    // transformar string em drawable
                    final int tag = Integer.parseInt(avatares.get(x).getTag().toString());
                    int drawable = Avatar.identificarAvatar(tag);

                    new SweetAlertDialog(SelecionarAvatar.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                            .setTitleText("Avatar")
                            .setCustomImage(drawable)
                            .setContentText("Deseja obter este avatar?")
                            .setConfirmText("Sim")
                            .setCancelText("Não")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener()
                            {
                                @Override
                                public void onClick(SweetAlertDialog sDialog)
                                {
                                    if (db.getQTDAvatares() > 0)
                                    {
                                        Log.d("SELECION_", "Banco atualizado Entrou no if: REPeTICAO: "+x+" \ne \n avatar: "+tag);
                                        db.atualizarAvatar(new Avatar(id,String.valueOf(tag),DatabaseHelper.getDateTime()));
                                        ref.child("usuarios").child(idUser).child("icone").setValue(tag);
                                    }
                                    else
                                    {
                                        Log.d("SELECION_", "Banco atualizado Entrou no else: REPeTICAO: "+x+" \ne \n avatar: "+tag);
                                        db.inserirAvatar(new Avatar(id,String.valueOf(tag),DatabaseHelper.getDateTime()));
                                        ref.child("usuarios").child(idUser).child("icone").setValue(tag);
                                    }

                                    Log.d("SELECION_", "Banco atualizado: " + db.getQTDAvatares()+" avatares salvos");
                                    finish();
                                }
                            })
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                }
            });
        }
    }

    private void configurarPagamento(int n)
    {
        String produtoID = "";
        String descricao = "";
        double preco = 0.0;
        switch (n)
        {
            case 0:
                produtoID = "001";
                descricao = "Pacote Mitico";
                preco = 3.99;
                break;
            case 1:
                produtoID = "002";
                descricao = "Pacote Epico";
                preco = 1.99;
                break;
            case 2:
                produtoID = "003";
                descricao = "Pacote Lendario";
                preco = 2.99;
                break;
        }
        Toast.makeText(getApplicationContext(),"Adiquirindo pacote: "+descricao+" por apenas R$ "+preco,Toast.LENGTH_LONG).show();
    }



}
