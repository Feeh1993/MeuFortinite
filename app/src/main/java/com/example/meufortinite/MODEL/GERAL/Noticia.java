package com.example.meufortinite.MODEL.GERAL;

public class Noticia
{
    private String image;
    private String titulo;
    private String data;
    private String likes;

    public Noticia(String image, String titulo,String data,String likes)
    {
        this.image = image;
        this.data = data;
        this.titulo = titulo;
        this.likes = likes;
    }

    public Noticia() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }
}
