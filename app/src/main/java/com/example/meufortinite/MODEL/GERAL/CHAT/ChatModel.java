package com.example.meufortinite.MODEL.GERAL.CHAT;

import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;

import java.io.Serializable;

public class ChatModel implements Serializable
{
    private String id;
    private IUser remetente;
    private IMessage ultimaMsg;
    private int qtdNaoLido;

    public ChatModel(String id, IUser remetente, IMessage ultimaMsg, int qtdNaoLido)
    {
        this.id = id;
        this.remetente = remetente;
        this.ultimaMsg = ultimaMsg;
        this.qtdNaoLido = qtdNaoLido;
    }

    public String getId() {
        return id;
    }

    public IUser getBuddy() {
        return remetente;
    }

    public IMessage getLastMessage() {
        return ultimaMsg;
    }

    public int getUnreadCount() {
        return qtdNaoLido;
    }
}
