package com.example.parcial4;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText agregaAlista;
    ListView lv;
    ArrayList<String> array_list;
    ArrayAdapter adaptador;
    String holder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv=findViewById(R.id.lvLista);
        agregaAlista=findViewById(R.id.edtAgregar);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String items = array_list.get(position).toString();
                Intent intent = new Intent (MainActivity.this, Edit_Delete.class);

                intent.putExtra("ClickValue", items);
                startActivityForResult(intent, 1);
            }
        });

        Mostrar_Lista();
    }
    
    @Override
    protected void onActivityResult(int codActivity, int codResultado, @Nullable Intent datos) {
        super.onActivityResult(codActivity, codResultado, datos);

        String producto;
        if (codActivity == 1)
        {
            producto = datos.getStringExtra("Producto");
            lv.setTag(producto);
        }

    }

    public void Mostrar_Lista() {

        Base obj=new Base(this, "Productos",null,1);
        SQLiteDatabase objDb=obj.getWritableDatabase();
        array_list = new ArrayList<String>();
        Cursor cursor =  objDb.rawQuery( "select * from Compras", null );
        cursor.moveToFirst();
        while(cursor.isAfterLast() == false) {
            array_list.add(cursor.getString(cursor.getColumnIndex("Id"))+"  -"+(cursor.getString(cursor.getColumnIndex("Nombre"))));
            //array_list.add(cursor.getString(cursor.getColumnIndex("Nombre")));
            cursor.moveToNext();
        }
        adaptador = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, array_list);


        lv.setAdapter(adaptador);
        objDb.close();
    }

    public void Agregar(View v)
    {
        Base obj=new Base(this, "Productos",null,1);
        SQLiteDatabase objDb=obj.getWritableDatabase();

        String nuevoProducto= agregaAlista.getText().toString();
        String sintaxisSql = "insert into Compras(Id,Nombre)values("+null+",'"+ nuevoProducto + "')";

        if(nuevoProducto.equals(""))
        {
            agregaAlista.setError("Debe agregar un producto a la lista de compras");
        }else {
            objDb.execSQL(sintaxisSql);
            Toast.makeText(this, "Se ha agregado un nuevo producto a la lista de compras", Toast.LENGTH_LONG).show();
            agregaAlista.setText("");
            agregaAlista.requestFocus();
            Mostrar_Lista();
        }
        objDb.close();
    }

    public void Borrar(){
        Base obj=new Base(this, "Productos",null,1);
        SQLiteDatabase objDb=obj.getWritableDatabase();
        Cursor cursor=objDb.rawQuery("SELECT * FROM Compras", null);
        if(cursor.moveToNext()) {
            objDb.delete("Compras", null, null);
            array_list.clear();
            //Notifica que los datos se han modificado,cualquier Vista que refleje el conjunto
            // de datos debe actualizarse.
            adaptador.notifyDataSetChanged();
            objDb.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = 'Compras'");
            Toast.makeText(this, "Se han eliminado todos los registros",Toast.LENGTH_LONG).show();
            objDb.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mi_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog;
        switch (id){
            case R.id.borrarLista:
                builder.setTitle("Borrar lista");
                builder.setMessage("¿Está seguro que desea eliminar definitivamente la lista?");
                builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Borrar();
                    }
                });
                builder.setNegativeButton("NO", null);
                dialog = builder.create();
                dialog.show();
                break;
            case R.id.about:
                builder.setTitle("Acerca de:");
                builder.setMessage("Hecho por: Alvarado Henríquez, Sofía Michelle");
                builder.setPositiveButton("Aceptar", null);
                dialog = builder.create();
                dialog.show();
                break;
            default:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}