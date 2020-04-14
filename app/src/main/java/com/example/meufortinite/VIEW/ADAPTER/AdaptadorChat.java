package com.example.meufortinite.VIEW.ADAPTER;

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


  public AdaptadorChat(Context context, ArrayList<Mensagem> data,String meuUser)
  {
    this.data = data;
    this.mContext = context;
    this.meuUser = meuUser;
  }

  @Override public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
  {
    View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adaptador_chat, null);
    MyViewHolder viewHolder = new MyViewHolder(view);
    return viewHolder;
  }

  @Override public void onBindViewHolder(MyViewHolder myViewHolder, int i)
  {
    Mensagem mensagem = data.get(i);


    String userAndIcon[] = mensagem.getUsername().split(":");
    String formatted_date = SCUtils.formatted_date(mensagem.getData());
    Log.d("ADPT_CHAT","meuUSer: "+meuUser+"\n userMSG: "+userAndIcon[0] );
    if(meuUser.equals(userAndIcon[0]))
    {
      myViewHolder.lnlD.setVisibility(View.GONE);
      myViewHolder.lnlE.setVisibility(View.VISIBLE);
      myViewHolder.frmlE.setBackgroundResource(R.drawable.bordas_selecionada);
      if (mensagem.recebido())
      {
        //myViewHolder.nickname.setText(userAndIcon[0]);
        myViewHolder.imgAvtarE.setImageResource(Avatar.identificarAvatar(Integer.parseInt(userAndIcon[1])));
        myViewHolder.textView_messageE.setText(mensagem.getMessagem());
      }else
      {
        // myViewHolder.nickname.setText(userAndIcon[0]);
        myViewHolder.imgAvtarE.setImageResource(Avatar.identificarAvatar(Integer.parseInt(userAndIcon[1])));
        myViewHolder.textView_messageE.setText(mensagem.getMessagem());
      }
    }
    else
    {
      myViewHolder.lnlE.setVisibility(View.GONE);
      myViewHolder.lnlD.setVisibility(View.VISIBLE);

      myViewHolder.frmlD.setBackgroundResource(R.drawable.bordas_comum);
      if (mensagem.recebido())
      {
        //myViewHolder.nickname.setText(userAndIcon[0]);
        myViewHolder.imgAvtarD.setImageResource(Avatar.identificarAvatar(Integer.parseInt(userAndIcon[1])));
        myViewHolder.textView_messageD.setText(mensagem.getMessagem());
      }else
      {
        // myViewHolder.nickname.setText(userAndIcon[0]);
        myViewHolder.imgAvtarD.setImageResource(Avatar.identificarAvatar(Integer.parseInt(userAndIcon[1])));
        myViewHolder.textView_messageD.setText(mensagem.getMessagem());
      }
    }


  }

  @Override public int getItemCount() {
    return (null != data ? data.size() : 0);
  }

  public class MyViewHolder extends RecyclerView.ViewHolder {
    protected TextView textView_messageD,textView_messageE;
    protected ImageView imgAvtarD,imgAvtarE;
    protected FrameLayout frmlE,frmlD;
    protected LinearLayout lnlD,lnlE;

    public MyViewHolder(View view)
    {
      super(view);
      this.textView_messageD = (TextView) view.findViewById(R.id.textView_messageD);
      this.frmlD= (FrameLayout) view.findViewById(R.id.frmlD);
      this.imgAvtarD = (ImageView) view.findViewById(R.id.imgAVTD);
      this.textView_messageE = (TextView) view.findViewById(R.id.textView_messageE);
      this.frmlE = (FrameLayout) view.findViewById(R.id.frmlE);
      this.imgAvtarE = (ImageView) view.findViewById(R.id.imgAVTE);
      this.lnlD = (LinearLayout) view.findViewById(R.id.lnlD);
      this.lnlE = (LinearLayout) view.findViewById(R.id.lnlE);
    }
  }
}