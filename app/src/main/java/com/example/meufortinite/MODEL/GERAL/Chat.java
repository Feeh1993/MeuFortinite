package com.example.meufortinite.MODEL.GERAL;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Chat
{
    private String mensagem;
    private String data;

    public Chat(String mensagem, String data) {
        this.mensagem = mensagem;
        this.data = data;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Exclude
    public Map<String, Object> mapearChat()
    {
        HashMap<String, Object> result = new HashMap<>();
        result.put("data",data);
        result.put("mensagem",mensagem);
        return result;
    }
}
