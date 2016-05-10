package com.luisfelipegomezc.sqlitepractica5;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase db;
    Button bAgregar, bActualizar, bEliminar, bBuscar, bDatos, bVenta;
    EditText eIdPeluche, eNombre, eCantidad, eValor;
    TextView resultado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bAgregar = (Button) findViewById(R.id.bAgregar);
        bActualizar = (Button) findViewById(R.id.bActualizar);
        bEliminar = (Button) findViewById(R.id.bEliminar);
        bBuscar = (Button) findViewById(R.id.bBuscar);
        bDatos = (Button) findViewById(R.id.bDatos);
        bVenta = (Button) findViewById(R.id.bVenta);

        eIdPeluche = (EditText) findViewById(R.id.eIdPeluche);
        eNombre = (EditText) findViewById(R.id.eNombre);
        eCantidad = (EditText) findViewById(R.id.eCantidad);
        eValor = (EditText) findViewById(R.id.eValor);
        resultado = (TextView) findViewById(R.id.eResultado);

        final InventarioSQLiteHelper inventario = new InventarioSQLiteHelper(this);
        db = inventario.getWritableDatabase();

        ver_tabla();

        bAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nombre = eNombre.getText().toString();
                String cantidad = eCantidad.getText().toString();
                String valor = eValor.getText().toString();

                ContentValues nuevoRegistro = new ContentValues();
                nuevoRegistro.put("nombre", nombre);
                nuevoRegistro.put("cantidad", cantidad);
                nuevoRegistro.put("valor", valor);
                db.insert("Inventario", null, nuevoRegistro);
                ver_tabla();

            }
        });

        bBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id_peluche = eIdPeluche.getText().toString();
                String nombre = eNombre.getText().toString();
                String valor = eValor.getText().toString();
                String cantidad = eCantidad.getText().toString();

                String[] campos = new String[] {"id_peluche", "nombre", "valor", "cantidad"};
                String[] args = new String[]{nombre};

                Cursor c = db.query("Inventario", campos, "nombre=?", args, null, null, null);
                if(c.moveToFirst()) {
                    resultado.setText("");
                    do{
                        String id_pelu = c.getString(0);
                        String nomb = c.getString(1);
                        int val = c.getInt(2);
                        int cant = c.getInt(3);
                        resultado.append(" "+id_pelu+" - "+nomb+" - "+val+" - "+cant+"\n");


                    }while (c.moveToNext());
                }

            }
        });

       bEliminar.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String id_peluche = eIdPeluche.getText().toString();
               db.delete("Inventario", "id_peluche=" + id_peluche, null);
               ver_tabla();


           }
       });

        bActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id_peluche = eIdPeluche.getText().toString();
                String nombre = eNombre.getText().toString();
                String cantidad = eCantidad.getText().toString();
                String valor = eValor.getText().toString();

                ContentValues nuevoRegistro = new ContentValues();
                nuevoRegistro.put("nombre",nombre);
                nuevoRegistro.put("cantidad",cantidad);
                nuevoRegistro.put("valor",valor);

                db.update("Inventario", nuevoRegistro, "id_peluche=" + id_peluche, null);
                ver_tabla();
            }
        });

         bDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imprimir();

            }
        });

        bVenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                venta();

            }
        });

    }

    protected void venta(){
        Intent intent = new Intent(this, Venta.class);
        startActivity(intent);
    }

    protected void imprimir(){
        Intent intent = new Intent(this, Inventario.class);
        startActivity(intent);
    }

    protected void ver_tabla(){
        Cursor c = db.rawQuery("SELECT id_peluche, nombre, cantidad, valor FROM Inventario", null);
        resultado.setText("");

        if (c.moveToFirst()){
            do{
                String id_peluche = c.getString(0);
                String nombre = c.getString(1);
                int cantidad = c.getInt(2);
                int valor = c.getInt(3);
                resultado.append("   "+id_peluche+" - "+nombre+" - "+cantidad+" - "+valor+"\n");

            }while (c.moveToNext());

        }
    }
}
