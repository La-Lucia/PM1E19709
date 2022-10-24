package com.example.pm2e103090197;

import static android.app.PendingIntent.getActivity;
import static android.widget.AbsListView.CHOICE_MODE_MULTIPLE_MODAL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pm2e103090197.config.SQLiteConexion;
import com.example.pm2e103090197.config.tablas.Personas;
import com.example.pm2e103090197.config.tablas.Transacciones;

import java.util.ArrayList;

public class ActivityListaContactos extends AppCompatActivity {

    Button Regresar;
    SQLiteConexion conexion;
    private static final int ACTION_CALL = 1;
    ListView listperson;
    ArrayList<Personas> lista;
    ArrayList<String> listaconcatenada;
    String t, n;
    int c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_contactos);

        Regresar = (Button) findViewById(R.id.AC_BtnRegresar);
        conexion = new SQLiteConexion(this, Transacciones.NameDatabase, null, 1);

        listperson = (ListView) findViewById(R.id.AC_ListPerson);

        GetListPerson();
        ArrayAdapter adp = new ArrayAdapter(this, android.R.layout.simple_list_item_1,listaconcatenada );
        listperson.setAdapter(adp);

        listperson.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {

                t = lista.get(i).getTelefono();
                n = lista.get(i).getNombre();
                c = lista.get(i).getId();

                Toast.makeText(getApplicationContext(), t, Toast.LENGTH_SHORT).show();
                listperson.getSelectedView();

                Intent intent = new Intent(getApplicationContext(), ActivityContacto.class);

                intent.putExtra("nombre", n);
                intent.putExtra("telefono", t);
                intent.putExtra("codigo", c);

                startActivity(intent);

            }
        });

        Regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

            }
        });



    }

    private void GetListPerson()
    {
        SQLiteDatabase db = conexion.getReadableDatabase(); // Base de datos en modo de lectura
        Personas listpersonas = null;

        lista = new ArrayList<Personas>();  // Lista de Objetos del tipo personas

        Cursor cursor = db.rawQuery(Transacciones.GetPersonas,null);

        while(cursor.moveToNext())
        {
            listpersonas = new Personas();
            listpersonas.setId(cursor.getInt(0));
            listpersonas.setPais(cursor.getString(1));
            listpersonas.setNombre(cursor.getString(2));
            listpersonas.setTelefono(cursor.getString(3));
            listpersonas.setNota(cursor.getString(4));

            lista.add(listpersonas);
        }

        cursor.close();

        LLenarLista();

    }

    private void LLenarLista()
    {
        listaconcatenada = new ArrayList<String>();

        for(int i =0;  i < lista.size(); i++)
        {
            listaconcatenada.add(lista.get(i).getId() + " | " +
                    lista.get(i).getNombre() + " | " +
                    lista.get(i).getTelefono());
        }
    }
}