package com.example.meufortinite.VIEW.FRAGMENT;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;

import com.example.meufortinite.R;
import com.tomer.fadingtextview.FadingTextView;

public class Buscar extends Fragment
{
    private RotateAnimation rotateAnimation;
    private int cont = 0;
    private FadingTextView fadingTextView;

    public Buscar()
    {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_buscar, container, false);
        fadingTextView = (FadingTextView) view.findViewById(R.id.fadingText);
        rotateAnimation = new RotateAnimation(360,0, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        rotateAnimation.setRepeatMode(Animation.RESTART);
        rotateAnimation.setDuration(5000);
        final ImageButton iv = (ImageButton)view.findViewById(R.id.imgbSearch_BUSCAR);
        iv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (cont == 0)
                {
                    cont = 1;
                    iv.startAnimation(rotateAnimation);
                    fadingTextView.setVisibility(View.VISIBLE);
                }
                else
                {
                    cont = 0;
                    iv.clearAnimation();
                }
            }
        });
     return view;
    }

}
