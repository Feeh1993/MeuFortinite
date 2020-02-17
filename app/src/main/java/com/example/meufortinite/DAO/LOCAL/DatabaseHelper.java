package com.example.meufortinite.DAO.LOCAL;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.example.meufortinite.MODEL.Usuario;

import java.util.ArrayList;
import java.util.List;



public class DatabaseHelper extends SQLiteOpenHelper
{

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "meuForti_db";


    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create notes table
        db.execSQL(Usuario.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Usuario.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public long inserirBook(Usuario usuario)
    {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Usuario.COLUMN_ID, usuario.id);

        // insert row
        long id = db.insert(Usuario.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public Usuario getUsuario(long id)
    {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Usuario.TABLE_NAME,
                new String[]{Usuario.COLUMN_ID},
                Usuario.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();
        Usuario usuario = new Usuario(
                cursor.getString(cursor.getColumnIndex(Usuario.COLUMN_ID)));

        // close the db connection
        cursor.close();

        return usuario;
    }

    public List<Usuario> recuperarUsuarios()
    {
        List<Usuario> usuarios = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Usuario.TABLE_NAME ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do
            {
                Usuario usuario = new Usuario();
                usuario.setId(cursor.getString(cursor.getColumnIndex(Usuario.COLUMN_ID)));

                usuarios.add(usuario);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return usuarios;
    }
    public Usuario recuperarUsuario()
    {
        Usuario usuario = new Usuario();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Usuario.TABLE_NAME ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do
            {
                usuario.setId(cursor.getString(cursor.getColumnIndex(Usuario.COLUMN_ID)));
            } while (cursor.moveToNext());
        }
        db.close();

        // return notes list
        return usuario;
    }

    public int getQTDUsuarios()
    {
        String countQuery = "SELECT  * FROM " + Usuario.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }

    public int atualizarUsuario(Usuario usuario)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Usuario.COLUMN_ID, usuario.id);
        // updating row
        return db.update(Usuario.TABLE_NAME, values, Usuario.COLUMN_ID + " = ?",
                new String[]{String.valueOf(usuario.getId())});
    }

    public void deletarUser(Usuario usuario)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Usuario.TABLE_NAME, Usuario.COLUMN_ID + " = ?",
                new String[]{String.valueOf(usuario.getId())});
        db.close();
    }
}