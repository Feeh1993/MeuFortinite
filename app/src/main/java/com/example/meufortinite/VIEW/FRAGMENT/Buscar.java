package com.example.meufortinite.VIEW.FRAGMENT;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.example.meufortinite.R;
import com.tomer.fadingtextview.FadingTextView;

public class Buscar extends Fragment
{
    private RotateAnimation rotateAnimation;
    private int cont = 0;
    private FadingTextView txtBuscar,txtInfo,txtResumo;
    private LinearLayout lnlTopo,lnlFundo;
    private Switch swtchMeuHS,swtchSeuHS,swtchDuo,swtchSQD;
    ImageButton imgbtnPlaneta;
    private FrameLayout frmlTopo;

    public Buscar()
    {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_buscar, container, false);
        fazerCast(view);
        imgbtnPlaneta.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (cont == 0)
                {
                    cont = 1;
                    lnlTopo.setVisibility(View.GONE);
                    imgbtnPlaneta.startAnimation(rotateAnimation);
                    txtBuscar.setVisibility(View.VISIBLE);
                    frmlTopo.setVisibility(View.VISIBLE);
                    lnlFundo.setVisibility(View.VISIBLE);
                    Log.d("BUSCAR_","RESUMO: \n SWITCH MHS: "+swtchMeuHS.getShowText()+
                            "\nSWITCH SHS: "+swtchSeuHS.getShowText()+
                            "\nSWITCH DUO: "+swtchDuo.getShowText()+
                            "\nSWITCH SQD: "+swtchSQD.getShowText()
                          );
                }
                else
                {
                    cont = 0;
                    lnlTopo.setVisibility(View.VISIBLE);
                    imgbtnPlaneta.clearAnimation();
                    txtBuscar.setVisibility(View.GONE);
                    frmlTopo.setVisibility(View.GONE);
                    //lnlFundo.setVisibility(View.GONE);
                }
            }
        });
     return view;
    }

    public void fazerCast(View view)
    {
        frmlTopo = (FrameLayout) view.findViewById(R.id.frmlTXTRESUMO_buscar);
        lnlTopo = (LinearLayout) view.findViewById(R.id.lnlTopo_buscar);
        lnlFundo = (LinearLayout) view.findViewById(R.id.lnlFundo_buscar);

        swtchDuo = (Switch) view.findViewById(R.id.swtchDuo_buscar);
        swtchSQD = (Switch) view.findViewById(R.id.swtchSquad_buscar);
        swtchMeuHS = (Switch) view.findViewById(R.id.swtchMeuHeadSet_buscar);
        swtchSeuHS = (Switch) view.findViewById(R.id.swtchSeuHeadSet_buscar);
        txtBuscar = (FadingTextView) view.findViewById(R.id.fdngtxtBuscando_buscar);
        txtResumo = (FadingTextView) view.findViewById(R.id.fdngtxtResumo_buscar);
        txtInfo = (FadingTextView) view.findViewById(R.id.fdngtxtTopo_buscar);
        rotateAnimation = new RotateAnimation(360,0, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        rotateAnimation.setRepeatMode(Animation.RESTART);
        rotateAnimation.setDuration(5000);
        imgbtnPlaneta = (ImageButton)view.findViewById(R.id.imgbSearch_BUSCAR);
    }

}
