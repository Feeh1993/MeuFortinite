package com.example.meufortinite.MODEL;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User
{
    private int icone;
    private String nick;
    private String tipo;
    private ArrayList<String> amigos;


    public User()
    {

    }

    public User(int icone, String nick, String tipo, ArrayList<String> amigos)
    {
        this.icone = icone;
        this.nick = nick;
        this.tipo = tipo;
        this.amigos = amigos;
    }

    public int getIcone() {
        return icone;
    }

    public void setIcone(int icone) {
        this.icone = icone;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Exclude
    public Map<String, Object> mapearUsuario()
    {
        HashMap<String, Object> result = new HashMap<>();
        result.put("nick",nick);
        result.put("icone",icone);
        result.put("tipo",tipo);
        result.put("amigos",amigos);
        return result;
    }

    public ArrayList<String> getAmigos() {
        return amigos;
    }

    public void setAmigos(ArrayList<String> amigos) {
        this.amigos = amigos;
    }
}
