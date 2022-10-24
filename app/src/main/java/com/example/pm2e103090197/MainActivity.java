package com.example.pm2e103090197;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pm2e103090197.config.SQLiteConexion;
import com.example.pm2e103090197.config.tablas.Transacciones;

public class MainActivity extends AppCompatActivity {

    static final int peticion_captura_imagen = 100;
    static final int peticion_acceso_cam = 201;
    Button BtnSalvar, BtnContactos, BtnImg;
    EditText Nombre, Telefono, Nota;
    Spinner SpinPaises;
    String Pais="", codigo = "";
    ImageView img;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BtnContactos = (Button) findViewById(R.id.MA_BtnContactos);
        BtnSalvar = (Button) findViewById(R.id.MA_BtnSalvar);
        Nombre = (EditText) findViewById(R.id.MA_TxtNombre);
        Telefono = (EditText) findViewById(R.id.MA_TxtPhone);
        Nota = (EditText) findViewById(R.id.MA_TxtNota);
        SpinPaises = (Spinner) findViewById(R.id.MA_SpinPaises);
        img = (ImageView) findViewById(R.id.MA_Img) ;
        BtnImg = (Button) findViewById(R.id.MA_BtnImg);

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.combo_paises, android.R.layout.simple_spinner_item);

        SpinPaises.setAdapter(adapter);

        SpinPaises.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                //Toast.makeText(parent.getContext(),"Seleccionado: "+parent.getItemAtPosition(i).toString(), Toast.LENGTH_LONG).show();

                String p = parent.getItemAtPosition(i).toString();
                if (p.equals("(504) Honduras")){
                    codigo = "+504 ";
                    Pais = "Honduras";
                }else if (p.equals("(506) Costa Rica")){
                    codigo = "+506 ";
                    Pais = "Costa Rica";
                }else if (p.equals("(502) Guatemala")){
                    codigo = "+502 ";
                    Pais = "Guatemala";
                }else if (p.equals("(503) El Salvador")){
                    codigo = "+503 ";
                    Pais = "El Salvador";
                }else if (p.equals("(507) Panama")){
                    codigo = "+507 ";
                    Pais = "Panama";
                }else if (p.equals("(505) Nicaragua")){
                    codigo = "+505 ";
                    Pais = "Nicaragua";
                }else if (p.equals("(501) El Belice")){
                    codigo = "+501 ";
                    Pais = "Belice";
                }else if (p.equals("Seleccione un país")){
                    Pais = "";
                    codigo = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        BtnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validar().equals("si")){
                    AgregarPersona();
                }

            }
        });

        BtnContactos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityListaContactos.class);
                startActivity(intent);
            }
        });

        BtnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    permisos();

            }
        });

    }

    private String validar(){

        String v = "no";

        if (Pais.equals("")){
            Toast.makeText(getApplicationContext(), "ALERTA: Debe seleccionar un país ", Toast.LENGTH_SHORT).show();

        }else  if (codigo.equals("")){
            Toast.makeText(getApplicationContext(), "ALERTA: Debe seleccionar un país ", Toast.LENGTH_SHORT).show();

        } else if (Nombre.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "ALERTA: Debe escribir un nombre ", Toast.LENGTH_SHORT).show();

        } else if (Telefono.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "ALERTA: Debe escribir un telefono", Toast.LENGTH_SHORT).show();

        }else if (Nota.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "ALERTA: Debe escribir una nota ", Toast.LENGTH_SHORT).show();

        }else{

            v = "si";

        }

        return v;

    }

    private void AgregarPersona() {

        SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.NameDatabase, null, 1);
        SQLiteDatabase db = conexion.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(Transacciones.pais, Pais);
        valores.put(Transacciones.nombre, Nombre.getText().toString());
        valores.put(Transacciones.telefono, codigo+Telefono.getText().toString());
        valores.put(Transacciones.nota, Nota.getText().toString());

        Long resultado  = db.insert(Transacciones.TbPersonas, Transacciones.id, valores);

        Toast.makeText(getApplicationContext(), "Contacto Guardado " + resultado.toString()
                , Toast.LENGTH_SHORT).show();

        db.close();

        Limpiar();

    }

    private void Limpiar() {

        Nombre.setText("");
        Telefono.setText("");
        Nota.setText("");
        img.setImageResource(R.drawable.ic_baseline_add_photo_alternate_24);

    }

    private void permisos() {
        // Validar si el permiso esta otorgado o no para tomar fotos
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            // Otorgar el permiso si no se tiene el mismo
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, peticion_acceso_cam);
        }
        else
        {
            tomarfoto();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == peticion_acceso_cam)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                tomarfoto();
            }
        }
    }

    private void tomarfoto()
    {
        Intent intentfoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(intentfoto.resolveActivity(getPackageManager())!= null)
        {
            startActivityForResult(intentfoto, peticion_captura_imagen);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == peticion_captura_imagen)
        {
            Bundle extras = data.getExtras();
            Bitmap imagen = (Bitmap) extras.get("data");
            img.setImageBitmap(imagen);
        }
    }


}