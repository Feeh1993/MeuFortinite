package com.example.meufortinite.VIEW.FRAGMENT;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.meufortinite.DAO.REMOTO.ConfiguracaoFirebase;
import com.example.meufortinite.R;
import com.google.firebase.database.DatabaseReference;

public class Chat extends Fragment
{
    private DatabaseReference ref = ConfiguracaoFirebase.getFirebase();

    public Chat()
    {
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("CHAT_","onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("CHAT_","onStop");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("CHAT_","onPause");
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        Log.d("CHAT_","onViewCreated");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        return view;
    }

}
