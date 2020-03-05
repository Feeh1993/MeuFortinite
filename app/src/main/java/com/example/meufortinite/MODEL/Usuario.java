package com.example.meufortinite.MODEL;

import android.text.TextUtils;

public class Usuario
{
    public String id;
    public String score;
    public String kd;
    public String kill;
    public String vintecincopri;
    public String dezpri;
    public String trespri;
    public String vitorias;
    public String criado;

    public Usuario(String id, String score, String kd, String kill, String vintecincopri, String dezpri, String trespri, String vitorias, String criado) {
        this.id = id;
        this.score = score;
        this.kd = kd;
        this.kill = kill;
        this.vintecincopri = vintecincopri;
        this.dezpri = dezpri;
        this.trespri = trespri;
        this.vitorias = vitorias;
        this.criado = criado;
    }

    public Usuario() {
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

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getKd() {
        return kd;
    }

    public void setKd(String kd) {
        this.kd = kd;
    }

    public String getKill() {
        return kill;
    }

    public void setKill(String kill) {
        this.kill = kill;
    }

    public String getVintecincopri() {
        return vintecincopri;
    }

    public void setVintecincopri(String vintecincopri) {
        this.vintecincopri = vintecincopri;
    }

    public String getDezpri() {
        return dezpri;
    }

    public void setDezpri(String dezpri) {
        this.dezpri = dezpri;
    }

    public String getTrespri() {
        return trespri;
    }

    public void setTrespri(String trespri) {
        this.trespri = trespri;
    }

    public String getVitorias() {
        return vitorias;
    }

    public void setVitorias(String vitorias) {
        this.vitorias = vitorias;
    }

}
