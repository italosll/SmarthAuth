package italo.com.smartauth.BandoDeDados

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import italo.com.smartauth.Modelo.LoginModelo

val DATABASE_NAME ="smartauthdb"
val TABLE_NAME ="login"
val COL_MATRICULA ="matricula"
val COL_SENHA ="senha"
val COL_ID ="id"


class DataBaseHandler (val context: Context) : SQLiteOpenHelper(context, DATABASE_NAME,null,1) {

    override fun onCreate(db: SQLiteDatabase) {

        val createTable = ("CREATE TABLE " + TABLE_NAME + "("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_MATRICULA + " VARCHAR (16), "
                + COL_SENHA + " VARCHAR (255)" + ")")
                Log.e("sql ->>",createTable)
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}

    fun insert(loginModelo: LoginModelo){
        val db = this.writableDatabase
        var contentValues = ContentValues()
        contentValues.put(COL_MATRICULA,loginModelo.matricula)
        contentValues.put(COL_SENHA,loginModelo.senha)
        var result = db.insert(TABLE_NAME,null,contentValues)
        if (result==-1.toLong()) Toast.makeText(context,"falha",Toast.LENGTH_SHORT).show()
        else Toast.makeText(context,"ok",Toast.LENGTH_SHORT).show()
    }

    fun read() :MutableList<LoginModelo>{

        var  list:MutableList<LoginModelo> = ArrayList()
        val db = this.readableDatabase
        val query = "SELECT * FROM " + TABLE_NAME
        val result = db.rawQuery(query,null)
        if (result.moveToFirst()){
            do {
                var login = LoginModelo()
                login.id = result.getString(result.getColumnIndex(COL_ID)).toInt()
                login.matricula = result.getString(result.getColumnIndex(COL_MATRICULA))
                login.senha = result.getString(result.getColumnIndex(COL_SENHA))
                list.add(login)
            }while (result.moveToNext())
        }
        result.close()
        db.close()
        return list
    }

    fun update(loginModelo: LoginModelo) {

        val db = this.writableDatabase
        var contentValues = ContentValues()
        contentValues.put(COL_MATRICULA,loginModelo.matricula)
        contentValues.put(COL_SENHA,loginModelo.senha)
        db.update(
            TABLE_NAME,
            contentValues,
            null,
            null)
        db.close()
    }

    fun delete(){
        val db = this.writableDatabase
        db.delete(TABLE_NAME, COL_ID, null)
    }


}