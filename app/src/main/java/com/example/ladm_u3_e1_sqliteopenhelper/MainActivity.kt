package com.example.ladm_u3_e1_sqliteopenhelper

import android.R
import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.ladm_u3_e1_sqliteopenhelper.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var db = DataBase(this, "ejemplo1", null, 1)
    var listaID = ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mostrar()

        binding.btInsertar.setOnClickListener {
            try {
                var datos = ContentValues()
                val tablaPersonas = db.writableDatabase

                datos.put("NOMBRE", binding.etNombre.text.toString())
                datos.put("DOMICILIO", binding.etDomicilio.text.toString())
                datos.put("TELEFONO", binding.etTelefono.text.toString())

                val resultado = tablaPersonas.insert("PERSONAS", "ID", datos)
                if (resultado == -1L) {
                    AlertDialog.Builder(this).setTitle("ERROR!").setMessage("NO SE PUDO INSERTAR")
                        .show()
                } else {
                    mostrar()
                    binding.etNombre.setText("")
                    binding.etDomicilio.setText("")
                    binding.etTelefono.setText("")
                    Toast.makeText(this, "Insertado Correctamente", Toast.LENGTH_SHORT).show()
                }

            } catch (err: SQLiteException) {
                AlertDialog.Builder(this).setTitle("ERROR!").setMessage(err.message).show()
            } finally {
                db.close()
            }
        }
    }

    fun mostrar() {
        val dataBase = DataBase(this, "ejemplo1", null, 1)
        var arreglo = ArrayList<String>()

        listaID.clear()
        try {
            val tablaPersonas = dataBase.readableDatabase
            var cursor = tablaPersonas.query(
                "PERSONAS",
                arrayOf("NOMBRE", "ID"/*,"DOMICILIO","TELEFONO"*/),
                null,
                null,
                null,
                null,
                null
            )
            //Vector resultado similiar a un arrayList, en este caso contendra un registro PERSONAS (nombre(0), domicilio(1),telefono(2))
            // var cursor=tablaPersona.query("PERSONA", arrayOf("*"),null,null,null,null,null)  -> ID,NOMBRE,DOMICILIO,TELEFONO

            if (cursor.moveToFirst()) {
                do {
                    arreglo.add(cursor.getString(0))
                    listaID.add(cursor.getInt(1).toString()) // (0) nombre, (1)ID
                } while (cursor.moveToNext()) //mientras haya datos continuar
            } else {
                arreglo.add("NO HAY RESULTADOS")
            }

        } catch (err: SQLiteException) {
            AlertDialog.Builder(this).setTitle("ERROR!").setMessage(err.message).show()
        } finally {
            dataBase.close()
        }
        binding.lvLista.adapter = ArrayAdapter<String>(this, R.layout.simple_list_item_1, arreglo)

        binding.lvLista.setOnItemClickListener { adapterView, view, i, l ->
            val idRecuperado = listaID.get(i)
            val datosPersona = mostrarUnaPersona(idRecuperado)

            AlertDialog.Builder(this)
                .setTitle("Informacion")
                .setMessage("Datos completos del Contacto ${datosPersona}")
                .setPositiveButton("Aceptar") { d, i -> }
                .setNeutralButton(  "Eliminar")  { d, i -> confirmarEliminar(idRecuperado)}
                .setNegativeButton("Actualizar"){d,i->
                    var ventana2= Intent(this,MainActivity2::class.java)
                    ventana2.putExtra("idActualizar",idRecuperado)
                    startActivity(ventana2)
                }
                .show()
        }

    }

    fun confirmarEliminar(idEliminar:String) {
        AlertDialog.Builder(this)
            .setTitle("Confirmar Eliminacion")
            .setMessage("Esta seguro que desea Eliminar a ${idEliminar}?")
            .setPositiveButton("SI"){d,i-> eliminar(idEliminar)}
            .setNegativeButton("NO"){d,i->}
            .show()
    }

    fun eliminar(idEliminar:String){
        val dataBase = DataBase(this,"ejemplo1",null,1)
        try{
            val tablaPersonas=dataBase.writableDatabase
            val resultado = tablaPersonas.delete("PERSONA","ID=?",arrayOf(idEliminar))
            if(resultado!=0){
                Toast.makeText(this,"Se elimino correctamente",Toast.LENGTH_SHORT).show()
                mostrar()
            }else{
                AlertDialog.Builder(this)
                    .setTitle("Atencion")
                    .setMessage("No se pudo Eliminar el ID ${idEliminar}")
                    .setPositiveButton("OK"){d,i->}
                    .show()
            }
        }catch(err:SQLiteException){
            AlertDialog.Builder(this)
                .setMessage(err.message)
                .show()
        }finally {
            dataBase.close()
        }
    }

    fun mostrarUnaPersona(idBuscado:String):String{
        val dataBase=DataBase(this,"ejemplo1",null,1)
        var resultado =""
        try{
            val tablaPersona = dataBase.readableDatabase                    //WHERE ?
            var cursor=tablaPersona.query("PERSONAS",arrayOf("*"),"ID=?", arrayOf(idBuscado),null,null,null)
            if(cursor.moveToFirst()){
                // (0)ID   (1)NOMBRE    (2)DOMICILIO    (3)TELEFONO
                resultado="\n\nID: "+cursor.getInt(0).toString()+ "\nNOMBRE: "+cursor.getString(1)+"\nDOMICILIO: "+cursor.getString(2)+"\nTELEFONO: "+cursor.getString(3)
            }else{
                resultado="NO SE ENCONTRARON DATOS DE LA CONSULTA"
            }
        }catch(err:SQLiteException){
            AlertDialog.Builder(this)
                .setMessage(err.message)
                .show()
        }finally {
            dataBase.close()
        }
        return resultado
    }
}
