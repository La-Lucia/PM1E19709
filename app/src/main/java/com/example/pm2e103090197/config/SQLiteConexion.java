package com.example.pm2e103090197.config;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.pm2e103090197.config.tablas.Transacciones;


public class SQLiteConexion extends SQLiteOpenHelper{

    //Constructor de clase para la base de datos
    public SQLiteConexion(Context context,
                          String dbname,
                          SQLiteDatabase.CursorFactory factory,
                          int version)
    {
        super(context, dbname, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // Creacion de las tablas de la db
        db.execSQL(Transacciones.CTPersonas); // Creando la tabla de Personas
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1)
    {
        // eliminamos la data y las tablas de la aplicacion
        db.execSQL(Transacciones.DropTPersona);
        onCreate(db);
    }

}
