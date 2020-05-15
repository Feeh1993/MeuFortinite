package com.example.meufortinite.MODEL.GERAL;

public class Notificacao
{
    private String recebido;
    private String id;

    public Notificacao(String recebido, String id) {
        this.recebido = recebido;
        this.id = id;
    }

    public Notificacao() {

    }

    public String getRecebido() {
        return recebido;
    }

    public void setRecebido(String recebido) {
        this.recebido = recebido;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
