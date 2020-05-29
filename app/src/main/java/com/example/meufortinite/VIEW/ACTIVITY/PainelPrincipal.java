package com.example.meufortinite.VIEW.ACTIVITY;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.meufortinite.DAO.LOCAL.DatabaseHelper;
import com.example.meufortinite.DAO.REMOTO.ConfiguracaoFirebase;
import com.example.meufortinite.MODEL.GERAL.Usuario;
import com.example.meufortinite.R;
import com.example.meufortinite.SERVICE.NotificacaoService;
import com.example.meufortinite.VIEW.ADAPTER.PPPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PainelPrincipal extends AppCompatActivity
{

    private ArrayList<Usuario> usuarios = new ArrayList<>();
    private DatabaseReference ref = ConfiguracaoFirebase.getFirebase();
    private TabLayout tabLayout;
    private String TAG = "PainelPrincipal_";
    private int[] imageResId = 
            {
            R.drawable.ic_amigos,
            R.drawable.ic_chat,
            R.drawable.ic_search,
            R.drawable.ic_noticia,
            R.drawable.ic_market,
            R.drawable.ic_settings
    };
    private int[] imageResIdChecked = {
            R.drawable.ic_amigos_check,
            R.drawable.ic_chat_click,
            R.drawable.ic_search_clicked,
            R.drawable.ic_noticia_click,
            R.drawable.ic_market_click,
            R.drawable.ic_settings_check
    };
    private int tabsInBack = 0;
    private ViewPager viewPager;
    private Usuario meuUser ;
    private FirebaseAuth user = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private DatabaseHelper db;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_painel_principal);
        fazerCast();
        db = new DatabaseHelper(getApplicationContext());
        usuarios.clear();
        usuarios.addAll(db.recuperarUsuarios());

        PPPagerAdapter painelPrincipalFragmentAdapter = new PPPagerAdapter(getApplicationContext(),getSupportFragmentManager());
        viewPager.setAdapter(painelPrincipalFragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);
        inserirTab(1,tabLayout,0);
        tabLayout.setOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(viewPager)
                {

                    @Override
                    public void onTabSelected(TabLayout.Tab tab)
                    {
                        super.onTabSelected(tab);
                        inserirTab(2, tabLayout, tab.getPosition());
                        tabsInBack = 1;
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab)
                    {
                        super.onTabUnselected(tab);
                        inserirTab(3,tabLayout,tab.getPosition());
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        super.onTabReselected(tab);
                    }
                }
        );
        viewPager.setCurrentItem(2);
    }

    private void fazerCast()
    {
        viewPager = findViewById(R.id.vp_painel);
        tabLayout = findViewById(R.id.tab_principal);
    }

    private void inserirTab(int tipo,TabLayout tabLayout,int position)
    {
        switch (tipo)
        {
            case 1:
                for (int i = 0; i < tabLayout.getTabCount(); i++)
                {
                    tabLayout.getTabAt(i).setIcon(imageResId[i]);
                }
                break;
            case 2:
                tabLayout.getTabAt(position).setIcon(imageResIdChecked[position]);
                break;
            case 3:
                tabLayout.getTabAt(position).setIcon(imageResId[position]);
                break;

        }

    }
    @Override
    public void onBackPressed()
    {
        if (tabsInBack == 1 )
        {
            viewPager.setCurrentItem(2);
            tabsInBack = 0;
        }
        else
        {
            new SweetAlertDialog(PainelPrincipal.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Aviso")
                    .setContentText("Deseja realmente sair?")
                    .setConfirmText("Sim")
                    .setCancelText("Cancelar")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
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

    }

    @Override
    protected void onStop() 
    {
        super.onStop();
        ref.child("usuarios").child(usuarios.get(0).getId()).child("tipo").setValue("offline");
    }
}
