package com.example.meufortinite.VIEW.ADAPTER;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meufortinite.DAO.LOCAL.DatabaseHelper;
import com.example.meufortinite.HELPER.SCUtils;
import com.example.meufortinite.MODEL.GERAL.Avatar;
import com.example.meufortinite.MODEL.GERAL.Mensagem;
import com.example.meufortinite.MODEL.GERAL.Usuario;
import com.example.meufortinite.MODEL.INTERFACE.BaseRecyclerAdapter;
import com.example.meufortinite.R;
import java.util.ArrayList;

public class AdaptadorChat extends RecyclerView.Adapter<AdaptadorChat.MyViewHolder>
{

  private final String meuUser;
  private ArrayList<Mensagem> data;
  private Context mContext;

  private static final int RIGHT_MSG = 0;
  private static final int LEFT_MSG = 1;

  private long DURATION = 500;
  private boolean on_atach = true;


  public AdaptadorChat(Context context, ArrayList<Mensagem> data,String meuUser)
  {
    this.data = data;
    this.mContext = context;
    this.meuUser = meuUser;
  }

  @Override
  public int getItemViewType(int position)
  {
    Mensagem mensagem = data.get(position);
    String userAndIcon[] = mensagem.getUsername().split(":");
    if(meuUser.equals(userAndIcon[0]))
    {
      return LEFT_MSG;
    }
    else
    {
      return RIGHT_MSG;
    }
  }

  @Override
  public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
  {
    View view ;
    if (i == RIGHT_MSG)
    {
      view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adaptador_chat_dir, null);
      return new MyViewHolder(view);
    }else
          {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adaptador_chat_esq, null);
            return new MyViewHolder(view);
          }
  }
  @Override
  public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView)
  {
    recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
    {
      @Override
      public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState)
      {
        super.onScrollStateChanged(recyclerView, newState);
        on_atach = false;
      }
    });
    super.onAttachedToRecyclerView(recyclerView);
  }
  private void iniciarAnimacao(View itemView, int position)
  {
    if (!on_atach)
    {
      position = -1;
    }
    boolean naoEstaNoPrimeiroItem = position == -1;
    position++;
    itemView.setAlpha(0.f);
    AnimatorSet animatorSet = new AnimatorSet();
    ObjectAnimator animator = ObjectAnimator.ofFloat(itemView,"alpha",0.f,0.5f,1.0f);
    animator.setStartDelay(naoEstaNoPrimeiroItem ? DURATION / 2 : (position * DURATION / 3));
    animator.setDuration(500);
    animatorSet.play(animator);
    animator.start();
  }


  @Override public void onBindViewHolder(MyViewHolder myViewHolder, int i)
  {
    Mensagem mensagem = data.get(i);


    String userAndIcon[] = mensagem.getUsername().split(":");
    String formatted_date = SCUtils.formatted_date(mensagem.getData());
    Log.d("ADPT_CHAT","meuUSer: "+meuUser+"\n userMSG: "+userAndIcon[0] );
    if(meuUser.equals(userAndIcon[0]))
    {
      if (mensagem.recebido())
      {
        //myViewHolder.nickname.setText(userAndIcon[0]);
       // myViewHolder.imgAvtarE.setImageResource(Avatar.identificarAvatar(Integer.parseInt(userAndIcon[1])));
        myViewHolder.txt.setText(mensagem.getMessagem());
      }else
      {
        // myViewHolder.nickname.setText(userAndIcon[0]);
        //myViewHolder.imgAvtarE.setImageResource(Avatar.identificarAvatar(Integer.parseInt(userAndIcon[1])));
        myViewHolder.txt.setText(mensagem.getMessagem());
      }
    }
    else
    {
      if (mensagem.recebido())
      {
        //myViewHolder.nickname.setText(userAndIcon[0]);
        //myViewHolder.imgAvtarD.setImageResource(Avatar.identificarAvatar(Integer.parseInt(userAndIcon[1])));
        myViewHolder.txt.setText(mensagem.getMessagem());
      }else
      {
        // myViewHolder.nickname.setText(userAndIcon[0]);
        //myViewHolder.imgAvtarD.setImageResource(Avatar.identificarAvatar(Integer.parseInt(userAndIcon[1])));
        myViewHolder.txt.setText(mensagem.getMessagem());
      }
    }


  }


  @Override public int getItemCount() {
    return (null != data ? data.size() : 0);
  }

  public class MyViewHolder extends RecyclerView.ViewHolder {
    protected TextView txt;
    protected FrameLayout frml;

    public MyViewHolder(View view)
    {
      super(view);
      this.txt = (TextView) view.findViewById(R.id.txtMSG);
      this.frml = (FrameLayout) view.findViewById(R.id.frml);
    }
  }
}