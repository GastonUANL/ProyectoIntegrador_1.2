package com.example.proyectointegrador12.db;

public class DB_Admins {
    private String Activo;
    private String Contrasena;
    private String Correo;
    private String Id_Usr;
    private String Imagen;
    private String NombreUsuario;
    private String Tipo_Usr;

    public DB_Admins() {}

    public DB_Admins(String activo, String contrasena, String correo,
                     String id_Usr, String imagen, String nombreUsuario,
                     String tipo_Usr) {
        Activo = activo;
        Contrasena = contrasena;
        Correo = correo;
        Id_Usr = id_Usr;
        Imagen = imagen;
        NombreUsuario = nombreUsuario;
        Tipo_Usr = tipo_Usr;
    }

    public String getActivo() {
        return Activo;
    }

    public void setActivo(String activo) {
        Activo = activo;
    }

    public String getContrasena() {
        return Contrasena;
    }

    public void setContrasena(String contrasena) {
        Contrasena = contrasena;
    }

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String correo) {
        Correo = correo;
    }

    public String getId_Usr() {
        return Id_Usr;
    }

    public void setId_Usr(String id_Usr) {
        Id_Usr = id_Usr;
    }

    public String getImagen() {
        return Imagen;
    }

    public void setImagen(String imagen) {
        Imagen = imagen;
    }

    public String getNombreUsuario() {
        return NombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        NombreUsuario = nombreUsuario;
    }

    public String getTipo_Usr() {
        return Tipo_Usr;
    }

    public void setTipo_Usr(String tipo_Usr) {
        Tipo_Usr = tipo_Usr;
    }
}
