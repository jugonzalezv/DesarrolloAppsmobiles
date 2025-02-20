package com.example.reto8finalfinal


import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLiteHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "EmpresasDB"
        private const val TABLE_NAME = "empresas"

        // Columnas de la tabla
        private const val COLUMN_ID = "id"
        private const val COLUMN_NOMBRE = "nombre"
        private const val COLUMN_URL = "url"
        private const val COLUMN_TELEFONO = "telefono"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_PRODUCTOS = "productos"
        private const val COLUMN_CLASIFICACION = "clasificacion"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE $TABLE_NAME ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COLUMN_NOMBRE TEXT, "
                + "$COLUMN_URL TEXT, "
                + "$COLUMN_TELEFONO TEXT, "
                + "$COLUMN_EMAIL TEXT, "
                + "$COLUMN_PRODUCTOS TEXT, "
                + "$COLUMN_CLASIFICACION TEXT)")
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // Métodos CRUD
    fun agregarEmpresa(nombre: String, url: String, telefono: String, email: String, productos: String, clasificacion: String): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NOMBRE, nombre)
            put(COLUMN_URL, url)
            put(COLUMN_TELEFONO, telefono)
            put(COLUMN_EMAIL, email)
            put(COLUMN_PRODUCTOS, productos)
            put(COLUMN_CLASIFICACION, clasificacion)
        }
        val id = db.insert(TABLE_NAME, null, values)
        db.close()
        return id
    }

    fun obtenerEmpresas(): List<Map<String, String>> {
        val listaEmpresas = mutableListOf<Map<String, String>>()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val empresa = mapOf(
                "id" to cursor.getString(0),
                "nombre" to cursor.getString(1),
                "url" to cursor.getString(2),
                "telefono" to cursor.getString(3),
                "email" to cursor.getString(4),
                "productos" to cursor.getString(5),
                "clasificacion" to cursor.getString(6)
            )
            listaEmpresas.add(empresa)
        }
        cursor.close()
        db.close()
        return listaEmpresas
    }

    fun eliminarEmpresa(id: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$COLUMN_ID=?", arrayOf(id.toString()))
        db.close()
    }
}
