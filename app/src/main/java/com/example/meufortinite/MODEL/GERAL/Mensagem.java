package com.example.meufortinite.MODEL.GERAL;

import com.example.meufortinite.HELPER.RandomColor;
import com.example.meufortinite.R;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Mensagem
{

  private String messagem;
  private long data;
  private boolean recebido;
  private String username;

  public Mensagem() {

  }

  public Mensagem(String messagem, long data, boolean recebido, String username)
  {
    this.messagem = messagem;
    this.data = data;
    this.recebido = recebido;
    this.username = username;
  }
  public boolean isRecebido() {
    return recebido;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public boolean recebido() {
    return recebido;
  }

  public void setRecebido(boolean recebido) {
    recebido = recebido;
  }

  public String getMessagem() {
    return messagem;
  }

  public void setMessagem(String messagem) {
    this.messagem = messagem;
  }

  public long getData() {
    return data;
  }

  public void setData(long data) {
    this.data = data;
  }

  private static int corRandom()
  {
    RandomColor randomColor = new RandomColor();
    int cor = randomColor.randomColor();
    return cor;
  }

}
