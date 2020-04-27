package com.example.meufortinite.VIEW.ACTIVITY;
import com.example.meufortinite.R;
import com.stephentuso.welcome.BasicPage;
import com.stephentuso.welcome.TitlePage;
import com.stephentuso.welcome.WelcomeActivity;
import com.stephentuso.welcome.WelcomeConfiguration;

public class TelaBoasVindas extends WelcomeActivity
{

    @Override
    protected WelcomeConfiguration configuration()
    {
        return new WelcomeConfiguration.Builder(this)
                .defaultBackgroundColor(R.color.colorAccent)
                .page(new TitlePage(R.drawable.ic_playstation, "Olá Seja bem vindo ao Meu Battle Royale"))
                .page(new BasicPage(R.drawable.ic_score, "Estatisticas", "Aqui voce poderá suas estatisticas"))
                .page(new BasicPage(R.drawable.ic_copiar, "Noticias", "Noticias sobre o seu Battle Royale Favorito!"))
                .build();
    }

}

