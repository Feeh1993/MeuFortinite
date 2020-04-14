package com.example.meufortinite.VIEW.ADAPTER;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
  private ArrayList<Mensagem> data;
  private Context mContext;


  public AdaptadorChat(Context context, ArrayList<Mensagem> data)
  {
    this.data = data;
    this.mContext = context;
  }

  @Override public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
  {
    View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adaptador_chat, null);
    MyViewHolder viewHolder = new MyViewHolder(view);
    return viewHolder;
  }

  @Override public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
    Mensagem mensagem = data.get(i);
    String userAndIcon[] = mensagem.getUsername().split(":");
    String formatted_date = SCUtils.formatted_date(mensagem.getData());

    if (mensagem.recebido())
    {
      //myViewHolder.nickname.setText(userAndIcon[0]);
      myViewHolder.imgAvtar.setImageResource(Avatar.identificarAvatar(Integer.parseInt(userAndIcon[1])));
      myViewHolder.textView_message.setText(mensagem.getMessagem());
    }else
      {
       // myViewHolder.nickname.setText(userAndIcon[0]);
        myViewHolder.imgAvtar.setImageResource(Avatar.identificarAvatar(Integer.parseInt(userAndIcon[1])));
        myViewHolder.textView_message.setText(mensagem.getMessagem());
    }
  }

  @Override public int getItemCount() {
    return (null != data ? data.size() : 0);
  }

  public class MyViewHolder extends RecyclerView.ViewHolder {
    protected TextView textView_message;
    protected ImageView imgAvtar;

    public MyViewHolder(View view)
    {
      super(view);
      this.textView_message = (TextView) view.findViewById(R.id.textView_message);
     // this.nickname = (TextView) view.findViewById(R.id.nickname);
      this.imgAvtar = (ImageView) view.findViewById(R.id.imgAVT);
    }
  }
}