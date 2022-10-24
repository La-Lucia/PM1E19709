package com.example.pm2e103090197;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pm2e103090197.config.SQLiteConexion;
import com.example.pm2e103090197.config.tablas.Transacciones;

public class ActivityContacto extends AppCompatActivity {


    int codigo;
    TextView nombre, telefono;
    Button BtnLlamar, BtnCompartir, BtnEliminar, BtnModificar, BtnRegresar;
    String nomConst;

    private static final int ACTION_CALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacto);

        nombre = (TextView) findViewById(R.id.C_TxtNombre) ;
        telefono = (TextView) findViewById(R.id.C_TxtTelefono);
        BtnLlamar = (Button) findViewById(R.id.C_BtnLlamar);
        BtnCompartir = (Button) findViewById(R.id.C_BtnCompartir);
        BtnModificar = (Button) findViewById(R.id.C_BtnEditar);
        BtnEliminar = (Button) findViewById(R.id.C_BtnEliminar);
        BtnRegresar = (Button) findViewById(R.id.C_BtnRegresar);

        Bundle resultado = getIntent().getExtras();
        nombre.setText(resultado.getString("nombre"));
        telefono.setText(resultado.getString("telefono"));
        codigo = resultado.getInt("codigo");
        nomConst = resultado.getString("nombre");

        BtnLlamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alerta = new AlertDialog.Builder(ActivityContacto.this);
                alerta.setMessage("Desea llamar a "+ nombre.getText().toString() +"?")
                        .setCancelable(false).
                        setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Llamar(telefono.getText().toString());
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                AlertDialog titulo = alerta.create();
                titulo.setTitle("Confirmacion");
                titulo.show();


            }
        });

        BtnCompartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, "Nombre : " + nombre.getText().toString()
                        + ", Telefono : " + telefono.getText().toString());
                intent.setType("text/plain");

                if (intent.resolveActivity(getPackageManager()) != null){

                    startActivity(intent);
                }

            }
        });

        BtnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alerta = new AlertDialog.Builder(ActivityContacto.this);
                alerta.setMessage("Desea Eliminar este contacto?")
                                .setCancelable(false).
                        setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Eliminar();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                AlertDialog titulo = alerta.create();
                titulo.setTitle("Confirmacion");
                titulo.show();



            }

        });

        BtnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alerta = new AlertDialog.Builder(ActivityContacto.this);
                alerta.setMessage("Desea guardar los cambios?")
                        .setCancelable(false).
                        setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Editar();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                AlertDialog titulo = alerta.create();
                titulo.setTitle("Confirmacion");
                titulo.show();



            }
        });

        BtnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), ActivityListaContactos.class);
                startActivity(intent);

            }
        });



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ACTION_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Llamar(telefono.getText().toString());
            } else {
                Toast.makeText(this, "Permiso Denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void Llamar(String p) {

        if (ContextCompat.checkSelfPermission(ActivityContacto.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ActivityContacto.this, new String[] {Manifest.permission.CALL_PHONE}, ACTION_CALL);
        }else{
            String llamar = "tel:"+p;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(llamar)));
        }


    }

    private void Editar() {

        SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.NameDatabase, null, 1);
        SQLiteDatabase db = conexion.getWritableDatabase();

        String n = nombre.getText().toString();
        String t = telefono.getText().toString();


        ContentValues valores = new ContentValues();
        valores.put(Transacciones.nombre, n);
        valores.put(Transacciones.telefono, t);

        try {
            db.update(Transacciones.TbPersonas, valores, "nombre = '"+ nomConst+"'", null);
            Toast.makeText(getApplicationContext(),"Contacto Actualizado Correctamente", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), ActivityListaContactos.class);
            startActivity(intent);
        } catch (Exception ex) {
            ex.toString();
        } finally {
            db.close();
        }

    }

    public void Eliminar() {


        SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.NameDatabase, null, 1);
        SQLiteDatabase db = conexion.getWritableDatabase();


        try {
            db.delete(Transacciones.TbPersonas, "nombre = '" + nomConst + "'", null);
            Toast.makeText(getApplicationContext(),"Contacto Borrado Correctamente", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), ActivityListaContactos.class);
            startActivity(intent);

        } catch (Exception ex) {
            ex.toString();
        } finally {
            db.close();
        }



    }

}