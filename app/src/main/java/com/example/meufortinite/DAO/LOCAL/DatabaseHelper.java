package com.example.meufortinite.DAO.LOCAL;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


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
    public void onCreate(SQLiteDatabase db)
    {
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

    public long inserirUser(Usuario usuario)
    {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Usuario.COLUMN_ID, usuario.id);
        values.put(Usuario.COLUMN_DEZPRIMEIROS, usuario.dezpri);
        values.put(Usuario.COLUMN_TRESPRIMEIROS, usuario.trespri);
        values.put(Usuario.COLUMN_VINTECINCOPRIMEIROS, usuario.vintecincopri);
        values.put(Usuario.COLUMN_KD, usuario.kd);
        values.put(Usuario.COLUMN_KILL, usuario.kill);
        values.put(Usuario.COLUMN_VITORIAS, usuario.vitorias);
        values.put(Usuario.COLUMN_SCORE, usuario.score);


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
                cursor.getString(cursor.getColumnIndex(Usuario.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Usuario.COLUMN_SCORE)),
                cursor.getString(cursor.getColumnIndex(Usuario.COLUMN_KD)),
                cursor.getString(cursor.getColumnIndex(Usuario.COLUMN_KILL)),
                cursor.getString(cursor.getColumnIndex(Usuario.COLUMN_VINTECINCOPRIMEIROS)),
                cursor.getString(cursor.getColumnIndex(Usuario.COLUMN_DEZPRIMEIROS)),
                cursor.getString(cursor.getColumnIndex(Usuario.COLUMN_TRESPRIMEIROS)),
                cursor.getString(cursor.getColumnIndex(Usuario.COLUMN_VITORIAS)));

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
                usuario.setScore(cursor.getString(cursor.getColumnIndex(Usuario.COLUMN_SCORE)));
                usuario.setKd(cursor.getString(cursor.getColumnIndex(Usuario.COLUMN_KD)));
                usuario.setKill(cursor.getString(cursor.getColumnIndex(Usuario.COLUMN_KILL)));
                usuario.setVintecincopri(cursor.getString(cursor.getColumnIndex(Usuario.COLUMN_VINTECINCOPRIMEIROS)));
                usuario.setDezpri(cursor.getString(cursor.getColumnIndex(Usuario.COLUMN_DEZPRIMEIROS)));
                usuario.setTrespri(cursor.getString(cursor.getColumnIndex(Usuario.COLUMN_TRESPRIMEIROS)));
                usuario.setVitorias(cursor.getString(cursor.getColumnIndex(Usuario.COLUMN_VITORIAS)));

                usuarios.add(usuario);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return usuarios;
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
        values.put(Usuario.COLUMN_DEZPRIMEIROS, usuario.dezpri);
        values.put(Usuario.COLUMN_TRESPRIMEIROS, usuario.trespri);
        values.put(Usuario.COLUMN_VINTECINCOPRIMEIROS, usuario.vintecincopri);
        values.put(Usuario.COLUMN_KD, usuario.kd);
        values.put(Usuario.COLUMN_KILL, usuario.kill);
        values.put(Usuario.COLUMN_SCORE, usuario.score);
        values.put(Usuario.COLUMN_VITORIAS, usuario.vitorias);
        return db.update(Usuario.TABLE_NAME, values, Usuario.COLUMN_ID + " = ?",
                new String[]{String.valueOf(usuario.getId())});
    }

    public void deletarUser(Usuario usuario, String ID)
    {
        if (ID == "")
        {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(Usuario.TABLE_NAME, Usuario.COLUMN_ID + " = ?",
                    new String[]{String.valueOf(usuario.getId())});
            db.close();
        }
        else
        {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(Usuario.TABLE_NAME, Usuario.COLUMN_ID + " = ?",
                    new String[]{ID});
            db.close();
        }

    }
}