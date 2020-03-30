package com.example.meufortinite.MODEL.GERAL.CHAT;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

@Retention(SOURCE)
@IntDef({MessageType.TEXT, MessageType.IMAGE, MessageType.VOICE})
public @interface MessageType {
    int TEXT = 0;
    int IMAGE = 1;
    int VOICE = 2;
}
