package com.example.meufortinite.VIEW.ADAPTER;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.meufortinite.VIEW.FRAGMENT.Amigos;
import com.example.meufortinite.VIEW.FRAGMENT.Estatisticas;
import com.example.meufortinite.VIEW.FRAGMENT.Loja;
import com.example.meufortinite.VIEW.FRAGMENT.Settings;

public class PPPagerAdapter extends FragmentPagerAdapter
{

    private final Context mContext;

    public PPPagerAdapter(Context context, FragmentManager fm)
    {
        super(fm);
        mContext = context;
    }
    @Override
    public Fragment getItem(int position)
    {
        switch (position)
        {
            case 0:
                return new Estatisticas();
            case 1:
                return new Amigos();
            case 2:
                return new Loja();
            case 3:
                return new Settings();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {

        return null;
    }
}




