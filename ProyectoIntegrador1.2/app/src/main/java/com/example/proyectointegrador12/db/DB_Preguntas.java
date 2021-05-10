package com.example.proyectointegrador12.db;

public class DB_Preguntas {

    String Id_Pregunta;
    String Pregunta;
    String Respuesta;

    public DB_Preguntas() {}

    public DB_Preguntas(String id_Pregunta, String pregunta, String respuesta) {
        Id_Pregunta = id_Pregunta;
        Pregunta = pregunta;
        Respuesta = respuesta;
    }

    public String getId_Pregunta() {
        return Id_Pregunta;
    }

    public void setId_Pregunta(String id_Pregunta) {
        Id_Pregunta = id_Pregunta;
    }

    public String getPregunta() {
        return Pregunta;
    }

    public void setPregunta(String pregunta) {
        Pregunta = pregunta;
    }

    public String getRespuesta() {
        return Respuesta;
    }

    public void setRespuesta(String respuesta) {
        Respuesta = respuesta;
    }
}
