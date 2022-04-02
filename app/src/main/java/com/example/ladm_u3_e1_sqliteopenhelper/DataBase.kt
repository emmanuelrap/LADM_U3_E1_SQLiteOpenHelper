package com.example.ladm_u3_e1_sqliteopenhelper
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBase(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {

    override fun onCreate(db: SQLiteDatabase) {
        //Se invoca tras la instalacion de la app y construye la estructura de la base de datos(tabla y relaciones)
        db.execSQL("CREATE TABLE PERSONAS (ID INTEGER PRIMARY KEY AUTOINCREMENT, NOMBRE VARCHAR (1000), " +
                                            "DOMICILIO VARCHAR (1000), TELEFONO VARCHAR(50))")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
      //se invoca tras un cambio de version de la base de datos, en la version se usan numeros naturales, al haber diferencia
      //se dispara esta parte del codigo

    }
}