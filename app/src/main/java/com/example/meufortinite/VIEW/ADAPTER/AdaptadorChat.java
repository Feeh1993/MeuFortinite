package com.example.meufortinite.VIEW.ADAPTER;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.meufortinite.HELPER.SCUtils;
import com.example.meufortinite.MODEL.GERAL.Mensagem;
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
    String formatted_date = SCUtils.formatted_date(mensagem.getData());
    if (mensagem.recebido())
    {
      myViewHolder.textView_message.setText(Html.fromHtml("<small><i><font color=\"#FFBB33\">" + " " + mensagem.getMessagem() + "</font></i></small>"));
    }else {
      myViewHolder.textView_message.setText(
          Html.fromHtml("<font color=\"#403835\">&#x3C;" + mensagem.getUsername() + "&#x3E;</font>" + " " + mensagem.getMessagem() + " <font color=\"#999999\">" + formatted_date + "</font>"));
    }
  }

  @Override public int getItemCount() {
    return (null != data ? data.size() : 0);
  }

  public class MyViewHolder extends RecyclerView.ViewHolder {
    protected TextView textView_message;

    public MyViewHolder(View view)
    {
      super(view);
      this.textView_message = (TextView) view.findViewById(R.id.textView_message);
    }
  }
}