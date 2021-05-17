package com.example.parcial4;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Edit_Delete extends AppCompatActivity {

    EditText edtProducto;
    Button btnEditar;
    String Producto;
    Button btnEliminar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__delete);

        edtProducto= findViewById(R.id.edtProducto);
        btnEditar = findViewById(R.id.btnEditar);
        btnEliminar = findViewById(R.id.btnEliminar);

        Bundle datos = getIntent().getExtras();

        Producto = datos.getString("ClickValue");
        edtProducto.setText(Producto);

    }



    public void editar(View v)
    {

        MainActivity MA = new MainActivity();

        Base obj=new Base(this, "Productos",null,1);
        SQLiteDatabase objDb=obj.getWritableDatabase();

        //ContentValues value = new ContentValues();
        String actualProducto= edtProducto.getText().toString();

        ContentValues val = new ContentValues();

        val.put("Nombre", edtProducto.getText().toString());

        Intent retornar = new Intent();

        if(!edtProducto.getText().toString().isEmpty())
        {
            objDb.update("Compras", val, "Id"+"=?", new String[]{actualProducto});
            Toast.makeText(this, "Registro actualizado con éxito", Toast.LENGTH_SHORT).show();
            edtProducto.requestFocus();
            retornar.putExtra("Producto", actualProducto);
            setResult(RESULT_OK, retornar);
        }
        MA.Mostrar_Lista();
        MA.adaptador.notifyDataSetChanged();
        objDb.close();
    }

    public void eliminar(View v)
    {
        Base obj= new Base(getApplicationContext(), "Productos", null, 1);
        MainActivity MA = new MainActivity();
        SQLiteDatabase baseEstado=obj.getWritableDatabase();
        //Dui a eliminar
        String producto =edtProducto.getText().toString();
        if(!edtProducto.getText().toString().isEmpty()) {
            baseEstado.delete("Compras", "Nombre" + "=?", new String[]{producto});

            edtProducto.requestFocus();
            baseEstado.close();
            Toast.makeText(this, "Registro borrado con éxito",
                    Toast.LENGTH_SHORT).show();
            MA.Mostrar_Lista();
        }
        else{
            Toast.makeText(this, "No hay nada que eliminar",
                    Toast.LENGTH_SHORT).show();
        }

    }

}