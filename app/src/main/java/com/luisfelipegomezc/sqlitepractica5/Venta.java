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
import android.widget.Toast;

public class Venta extends AppCompatActivity {

    SQLiteDatabase db;
    int cantidades = 0, stock = 0;
    Double total = Double.valueOf(0), Vent_Total = Double.valueOf(0);
    Button bRegresar, bVer, bCalcular, bAceptar;
    EditText eIdPeluche, eNombre, eCantidad, eTotal;
    TextView tValor, tTotal, tVentaTotal;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venta);



        Toast toast = Toast.makeText(getApplicationContext(), "Favor iniciar proceso con el nombre del peluche", Toast.LENGTH_LONG);
        toast.show();

        final InventarioSQLiteHelper inventario = new InventarioSQLiteHelper(this);
        db = inventario.getWritableDatabase();

        eIdPeluche = (EditText)findViewById(R.id.eIdPeluche);
        eNombre = (EditText)findViewById(R.id.eNombre);
        eCantidad = (EditText)findViewById(R.id.eCantidad);

        tTotal = (TextView) findViewById(R.id.tTotal);
        tValor = (TextView) findViewById(R.id.tValor);
        tVentaTotal = (TextView) findViewById(R.id.tTotalVenta);

        bRegresar = (Button) findViewById(R.id.bRegresar);
        bVer = (Button) findViewById(R.id.bVer);
        bCalcular = (Button) findViewById(R.id.bCalcular);
        bAceptar = (Button) findViewById(R.id.bAceptar);



        bVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id_peluche = eIdPeluche.getText().toString();
                String nombre = eNombre.getText().toString();
                String valor = tValor.getText().toString();
                String cantidad = eCantidad.getText().toString();

                String[] campos = new String[] {"id_peluche", "nombre", "valor", "cantidad"};
                String[] args = new String[]{nombre};

                Cursor c = db.query("Inventario", campos, "nombre=?", args, null, null, null);
                if(c.moveToFirst()) {
                    eIdPeluche.setText(String.valueOf(""));

                    tValor.setText(String.valueOf(""));
                    eCantidad.setText(String.valueOf(""));
                    tTotal.setText(String.valueOf(""));

                    do{
                        String id_pelu = c.getString(0);
                        String nomb = c.getString(1);
                        String val = c.getString(2);
                        String cant = c.getString(3);
                        eIdPeluche.append(id_pelu);
                        eCantidad.append(cant);

                        tValor.append(val);
                        Toast toast = Toast.makeText(getApplicationContext(), "En bodega = "+cant+ " Unidades", Toast.LENGTH_LONG);
                        toast.show();
                    }while (c.moveToNext());
                }else{
                    eIdPeluche.setText(String.valueOf(""));

                    tValor.setText(String.valueOf(""));
                    eCantidad.setText(String.valueOf("No existe el peluche"));
                }
            }
        });

        bRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regresar();
            }
        });

        bCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((eCantidad.getText().toString().equals("")) || (tValor.getText().toString().equals("")) || (eIdPeluche.getText().toString().equals(""))) {
                    tValor.setText("Error");
                    Toast toast = Toast.makeText(getApplicationContext(), "Error al cargar datos", Toast.LENGTH_LONG);
                    toast.show();

                }else{
                    total = Double.parseDouble(eCantidad.getText().toString()) *  Double.parseDouble(tValor.getText().toString());
                    tTotal.setText(String.valueOf(total));
                    cantidades = Integer.parseInt(eCantidad.getText().toString());
                    //cantidad();

                }
            }
        });

        bAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((tTotal.getText().toString().equals("")) ) {
                    //tTotal.setText("Error");
                    Toast toast = Toast.makeText(getApplicationContext(), "No se realizo la venta", Toast.LENGTH_LONG);
                    toast.show();
                }else{

                    String id_peluche = eIdPeluche.getText().toString();
                    String nombre = eNombre.getText().toString();
                    String valor = tValor.getText().toString();
                    String cantidad = eCantidad.getText().toString();

                    String[] campos = new String[] {"id_peluche", "nombre", "valor", "cantidad"};
                    String[] args = new String[]{nombre};

                    Cursor c = db.query("Inventario", campos, "nombre=?", args, null, null, null);
                    if(c.moveToFirst()) {

                        do{
                            String id_pelu = c.getString(0);
                            String nomb = c.getString(1);
                            int val = c.getInt(2);
                            int cant = c.getInt(3);

                            stock = cant - cantidades;
                        }while (c.moveToNext());


                    if(stock >= 0){
                        String cadena = Integer.toString(stock);
                        ContentValues nuevoRegistro = new ContentValues();
                        nuevoRegistro.put("nombre",nombre);
                        nuevoRegistro.put("cantidad", cadena);
                        nuevoRegistro.put("valor", valor);

                        db.update("Inventario", nuevoRegistro, "id_peluche=" + id_peluche, null);
                        Vent_Total = Vent_Total + Double.parseDouble(tValor.getText().toString());
                        eIdPeluche.setText(String.valueOf(""));
                        eNombre.setText(String.valueOf(""));
                        tValor.setText(String.valueOf(""));
                        eCantidad.setText(String.valueOf(""));
                        tTotal.setText(String.valueOf(""));
                        Toast toast = Toast.makeText(getApplicationContext(), "En bodega = "+stock+ " Unidades", Toast.LENGTH_LONG);
                        toast.show();

                        if(stock<6){
                            toast = Toast.makeText(getApplicationContext(), "Pocas existencias", Toast.LENGTH_LONG);
                            toast.show();
                        }
                        stock = 0;
                        cantidades = 0;

                    }else{
                        Toast toast = Toast.makeText(getApplicationContext(), "No Hay suficientes Unidades", Toast.LENGTH_LONG);
                        toast.show();
                    }
                    }

                }
            }
        });
    }

    protected void regresar(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    protected void cantidad(){
        //cantidades = Integer.parseInt(eCantidad.getText().toString());
    }
}
