package com.example.meufortinite.MODEL.GERAL;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User
{
    public int icone;
    public String nick;
    public String tipo;
    public String id;
    public String rank;
    public ArrayList<String> amigos;


    public User()
    {

    }

    public User(int icone, String nick, String tipo, String id, ArrayList<String> amigos,String rank)
    {
        this.icone = icone;
        this.nick = nick;
        this.tipo = tipo;
        this.id = id;
        this.amigos = amigos;
        this.rank = rank;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
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
        result.put("id",id);
        result.put("rank",rank);

        return result;
    }

    public ArrayList<String> getAmigos() {
        return amigos;
    }

    public void setAmigos(ArrayList<String> amigos) {
        this.amigos = amigos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
