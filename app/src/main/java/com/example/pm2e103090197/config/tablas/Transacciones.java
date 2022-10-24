package com.example.pm2e103090197.config.tablas;

public class Transacciones {

    //Nombre de la base de datos
    public static final String NameDatabase = "PM01DB";

    /* Creacion de las tablas de la BD */
    public static final String TbPersonas = "personas";

    /* Campos de la tabla personas */
    public static final String id = "id";
    public static final String pais = "pais";
    public static final String nombre = "nombre";
    public static final String telefono = "telefono";
    public static final String nota = "nota";

    // DDL
    public static final String CTPersonas = "CREATE TABLE personas (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            " pais TEXT, nombre TEXT, telefono TEXT, nota TEXT)";

    public static final String GetPersonas = "SELECT * FROm " + Transacciones.TbPersonas;

    public static final String DropTPersona = "DROP TABLE IF EXISTS personas";

}
