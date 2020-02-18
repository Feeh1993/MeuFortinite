package com.example.meufortinite.MODEL;

import android.text.TextUtils;

public class Usuario {
    public String id;
    public String score;
    public String kd;
    public String kill;
    public String vintecincopri;
    public String dezpri;
    public String trespri;
    public String vitorias;

    public static final String TABLE_NAME = "usuario";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_VITORIAS = "vitorias";
    public static final String COLUMN_TRESPRIMEIROS = "trespri";
    public static final String COLUMN_DEZPRIMEIROS = "dezpri";
    public static final String COLUMN_VINTECINCOPRIMEIROS = "vintecincopri";
    public static final String COLUMN_KD = "kd";
    public static final String COLUMN_KILL = "kill";
    public static final String COLUMN_SCORE = "score";

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " TEXT PRIMARY KEY,"
                    + COLUMN_DEZPRIMEIROS + " TEXT,"
                    + COLUMN_TRESPRIMEIROS + " TEXT,"
                    + COLUMN_VINTECINCOPRIMEIROS + " TEXT,"
                    + COLUMN_KD + " TEXT,"
                    + COLUMN_KILL + " TEXT,"
                    + COLUMN_SCORE + " TEXT,"
                    + COLUMN_VITORIAS + " TEXT"
                    + ")";

    public Usuario(String id, String score, String kd, String kill, String vintecincopri,
                   String dezpri, String trespri, String vitorias)
    {
        this.id = id;
        this.score = score;
        this.kd = kd;
        this.kill = kill;
        this.vintecincopri = vintecincopri;
        this.dezpri = dezpri;
        this.trespri = trespri;
        this.vitorias = vitorias;
    }

    public Usuario() {
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

    public static String getCreateTable() {
        return CREATE_TABLE;
    }

}
