package com.example.ladm_u3_e1_sqliteopenhelper

import android.content.ContentValues
import android.database.sqlite.SQLiteException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.ladm_u3_e1_sqliteopenhelper.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var db=DataBase(this,"ejemplo1",null,1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btInsertar.setOnClickListener {
            try{
                var datos=ContentValues()
                val tablaPersonas = db.writableDatabase

                datos.put("NOMBRE",binding.etNombre.text.toString())
                datos.put("DOMICILIO",binding.etDomicilio.text.toString())
                datos.put("TELEFONO",binding.etTelefono.text.toString())

                val resultado = tablaPersonas.insert("PERSONAS","ID",datos)
                if(resultado==1L){
                    AlertDialog.Builder(this).setTitle("ERROR!").setMessage("NO SE PUDO INSERTAR").show()
                }else{
                    AlertDialog.Builder(this).setTitle("ERROR!").setMessage("INSERTADO CORRECTAMENTE").show()
                }

            }catch(err:SQLiteException){

            }finally {
                db.close()
            }
        }
    }
}