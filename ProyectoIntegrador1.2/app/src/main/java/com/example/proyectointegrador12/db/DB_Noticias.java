package com.example.proyectointegrador12.db;

public class DB_Noticias {
    private String Contenido;
    private String Fecha;
    private String Id_Noticia;
    private String Id_Usr;
    private String Imagen;
    private String Tipo_Noticia;
    private String Titulo;

    public DB_Noticias() {}

    public DB_Noticias(String contenido, String fecha, String id_Noticia,
                       String id_Usr, String imagen, String tipo_Noticia,
                       String titulo) {
        Contenido = contenido;
        Fecha = fecha;
        Id_Noticia = id_Noticia;
        Id_Usr = id_Usr;
        Imagen = imagen;
        Tipo_Noticia = tipo_Noticia;
        Titulo = titulo;
    }

    public String getContenido() { return Contenido; }

    public void setContenido(String contenido) { Contenido = contenido; }

    public String getFecha() { return Fecha; }

    public void setFecha(String fecha) { Fecha = fecha; }

    public String getId_Noticia() { return Id_Noticia; }

    public void setId_Noticia(String id_Noticia) { Id_Noticia = id_Noticia; }

    public String getId_Usr() { return Id_Usr; }

    public void setId_Usr(String id_Usr) { Id_Usr = id_Usr; }

    public String getImagen() { return Imagen; }

    public void setImagen(String imagen) { Imagen = imagen; }

    public String getTipo_Noticia() { return Tipo_Noticia; }

    public void setTipo_Noticia(String tipo_Noticia) { Tipo_Noticia = tipo_Noticia; }

    public String getTitulo() { return Titulo; }

    public void setTitulo(String titulo) { Titulo = titulo; }
}
