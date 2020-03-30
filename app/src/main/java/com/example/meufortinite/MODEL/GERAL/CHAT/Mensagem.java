package com.example.meufortinite.MODEL.GERAL.CHAT;

import androidx.annotation.Nullable;

import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;
import com.stfalcon.chatkit.commons.models.MessageContentType;

import java.util.Date;

public class Mensagem implements IMessage, MessageContentType, MessageContentType.Image {

    private String id;
    private String body;
    private IUser remetente;
    private Date criadoEm;
    @MessageType
    private int type;

    public Mensagem(String id, String body, IUser sender, Date createdAt) {
        this(id, body, sender, createdAt, MessageType.TEXT);
    }

    public Mensagem(String id, String body, IUser remetente, Date criadoEm, @MessageType int type)
    {
        this.id = id;
        this.body = body;
        this.remetente = remetente;
        this.criadoEm = criadoEm;
        this.type = type;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getText() {
        return body;
    }

    @Override
    @Nullable
    public IUser getUser() {
        return remetente;
    }

    @Override
    public Date getCreatedAt() {
        return criadoEm;
    }

    public int getType() {
        return type;
    }

    @Nullable
    @Override
    public String getImageUrl() {
        return this.type == MessageType.IMAGE ? body : null;
    }

    public String getVoiceUrl() {
        return this.type == MessageType.VOICE ? body : null;
    }
}
