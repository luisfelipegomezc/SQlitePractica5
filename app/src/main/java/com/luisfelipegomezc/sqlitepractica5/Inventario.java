package com.luisfelipegomezc.sqlitepractica5;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Inventario extends AppCompatActivity {

    SQLiteDatabase db;
    TextView table;
    Button bRegresar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventario);

        //Bundle extras = getIntent().getExtras();
        table = (TextView) findViewById(R.id.eResultado);
        bRegresar = (Button) findViewById(R.id.bRegresar);

        final InventarioSQLiteHelper inventario = new InventarioSQLiteHelper(this);
        db = inventario.getWritableDatabase();
        imprimir_tabla();

        bRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regresar();

            }
        });
    }


    protected void regresar(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    protected void imprimir_tabla(){
        Cursor c = db.rawQuery("SELECT id_peluche, nombre, cantidad, valor FROM Inventario", null);
        table.setText("");

        if (c.moveToFirst()){
            do{
                String id_peluche = c.getString(0);
                String nombre = c.getString(1);
                int cantidad = c.getInt(2);
                int valor = c.getInt(3);
                table.append("   "+id_peluche+" - "+nombre+" - "+cantidad+" - "+valor+"\n");

            }while (c.moveToNext());

        }

    }
}
