package com.example.proyectointegrador12.db;

public class DB_Comentarios {
    private String Contenido;
    private String Fecha;
    private String Id_Comentario;
    private String Id_Noticia;
    private String Id_Usr;

    public DB_Comentarios(){}

    public DB_Comentarios(String contenido, String fecha, String id_Comentario, String id_Noticia, String id_Usr) {
        Contenido = contenido;
        Fecha = fecha;
        Id_Comentario = id_Comentario;
        Id_Noticia = id_Noticia;
        Id_Usr = id_Usr;
    }

    public String getContenido() {
        return Contenido;
    }

    public void setContenido(String contenido) {
        Contenido = contenido;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getId_Comentario() {
        return Id_Comentario;
    }

    public void setId_Comentario(String id_Comentario) {
        Id_Comentario = id_Comentario;
    }

    public String getId_Noticia() {
        return Id_Noticia;
    }

    public void setId_Noticia(String id_Noticia) {
        Id_Noticia = id_Noticia;
    }

    public String getId_Usr() {
        return Id_Usr;
    }

    public void setId_Usr(String id_Usr) {
        Id_Usr = id_Usr;
    }
}
