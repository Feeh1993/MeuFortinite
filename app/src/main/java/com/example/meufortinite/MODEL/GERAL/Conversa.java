package com.example.meufortinite.MODEL.GERAL;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Conversa
{
    public String mensagem;
    public String criado;
    public String id;
    public String nickname;

    public Conversa()
    {
    }
    public Conversa(String mensagem, String criado, String id, String nickname)
    {
        this.mensagem = mensagem;
        this.criado = criado;
        this.id = id;
        this.nickname = nickname;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getCriado() {
        return criado;
    }

    public void setCriado(String criado) {
        this.criado = criado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Exclude
    public Map<String, Object> mapearChat()
    {
        HashMap<String, Object> result = new HashMap<>();
        result.put("criado",criado);
        result.put("mensagem",mensagem);
        result.put("id",id);
        result.put("nickname",nickname);
        return result;
    }
}
