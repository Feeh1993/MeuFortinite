package com.example.meufortinite.DAO.LOCAL;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.example.meufortinite.MODEL.Avatar;
import com.example.meufortinite.MODEL.User;
import com.example.meufortinite.MODEL.Usuario;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class DatabaseHelper extends SQLiteOpenHelper
{

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "meuForti_db";

    //dados banco relacional
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CRIADO = "criado";

    //DADOS TABELA AVATAR
    public static final String COLUMN_AVATAR =  "icone";
    public static  final String TABLE_NAME_AVATAR = "avatar";

    //DADOS TABELA USUARIO
    public static final String TABLE_NAME_USER = "usuario";
    public static final String COLUMN_VITORIAS = "vitorias";
    public static final String COLUMN_TRESPRIMEIROS = "trespri";
    public static final String COLUMN_DEZPRIMEIROS = "dezpri";
    public static final String COLUMN_VINTECINCOPRIMEIROS = "vintecincopri";
    public static final String COLUMN_KD = "kd";
    public static final String COLUMN_KILL = "kill";
    public static final String COLUMN_SCORE = "score";

    //DADOS TABELA AMIGOS
    public static final String TABLE_NAME_AMIGOS = "amigos";
    public static final String COLUMN_AMIGOS =  "nickname";
    public static final String COLUMN_ICONE = "icone";


    //CRIANDO TABELA AMIGOS
    public static final String CREATE_TABLEAMIGOS =
            "CREATE TABLE " + TABLE_NAME_AMIGOS + "("
                    + COLUMN_ID + " TEXT PRIMARY KEY,"
                    + COLUMN_AMIGOS + " TEXT,"
                    + COLUMN_ICONE + " INTEGER"
                    + ")";

    //CRIANDO TABELA AVATAR
    public static final String CREATE_TABLEAVATAR =
            "CREATE TABLE " + TABLE_NAME_AVATAR + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY,"
                    + COLUMN_AVATAR + " TEXT,"
                    + COLUMN_CRIADO + " DATETIME"
                    + ")";

    // Create table SQL query
    public static final String CREATE_TABLEUSER =
            "CREATE TABLE " + TABLE_NAME_USER + "("
                    + COLUMN_ID + " TEXT PRIMARY KEY,"
                    + COLUMN_DEZPRIMEIROS + " TEXT,"
                    + COLUMN_TRESPRIMEIROS + " TEXT,"
                    + COLUMN_VINTECINCOPRIMEIROS + " TEXT,"
                    + COLUMN_KD + " TEXT,"
                    + COLUMN_KILL + " TEXT,"
                    + COLUMN_SCORE + " TEXT,"
                    + COLUMN_VITORIAS + " TEXT,"
                    + COLUMN_CRIADO + " DATETIME"
                    + ")";


    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // create notes table
        db.execSQL(CREATE_TABLEUSER);
        db.execSQL(CREATE_TABLEAVATAR);
        db.execSQL(CREATE_TABLEAMIGOS);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_AVATAR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_AMIGOS);

        // Create tables again
        onCreate(db);
    }
    public long inserirAmigo(User user)
    {
        // ABRIR MODO DE LEITURA DO BANCO
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, user.id);
        values.put(COLUMN_AMIGOS, user.nick);
        values.put(COLUMN_ICONE, user.icone);


        //INSERIR LINHA
        long id = db.insert(TABLE_NAME_AMIGOS, null, values);

        // FECHAR CONEXAO
        db.close();

        // RETORNA ID INSERIDO
        return id;
    }
    public long inserirAvatar(Avatar avatar)
    {
        // ABRIR MODO DE LEITURA DO BANCO
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_AVATAR, avatar.avatar);
        values.put(COLUMN_CRIADO, this.getDateTime());

        //INSERIR LINHA
        long id = db.insert(TABLE_NAME_AVATAR, null, values);

        // FECHAR CONEXAO
        db.close();

        // RETORNA ID INSERIDO
        return id;
    }

    public long inserirUser(Usuario usuario)
    {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(COLUMN_ID, usuario.id);
        values.put(COLUMN_DEZPRIMEIROS, usuario.dezpri);
        values.put(COLUMN_TRESPRIMEIROS, usuario.trespri);
        values.put(COLUMN_VINTECINCOPRIMEIROS, usuario.vintecincopri);
        values.put(COLUMN_KD, usuario.kd);
        values.put(COLUMN_KILL, usuario.kill);
        values.put(COLUMN_VITORIAS, usuario.vitorias);
        values.put(COLUMN_SCORE, usuario.score);


        // insert row
        long id = db.insert(TABLE_NAME_USER, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public User getAmigo(long id)
    {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_AMIGOS,
                new String[]{COLUMN_AMIGOS},
                COLUMN_AMIGOS + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();
        User user = new User
                (cursor.getInt(cursor.getColumnIndex(COLUMN_ICONE)),
                cursor.getString(cursor.getColumnIndex(COLUMN_AMIGOS)),
                null,cursor.getString(cursor.getColumnIndex(COLUMN_ID)),null);

        // close the db connection
        cursor.close();

        return user;
    }

    public Avatar getAvatar(long id)
    {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_AVATAR,
                new String[]{COLUMN_AVATAR},
                COLUMN_AVATAR + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();
             Avatar avatar = new Avatar(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                                        cursor.getString(cursor.getColumnIndex(COLUMN_AVATAR)),
                                        cursor.getString(cursor.getColumnIndex(COLUMN_CRIADO)));

        // close the db connection
        cursor.close();

        return avatar;
    }
    public Usuario getUsuario(long id)
    {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_USER,
                new String[]{COLUMN_ID},
                COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();
        Usuario usuario = new Usuario(
                cursor.getString(cursor.getColumnIndex(COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(COLUMN_SCORE)),
                cursor.getString(cursor.getColumnIndex(COLUMN_KD)),
                cursor.getString(cursor.getColumnIndex(COLUMN_KILL)),
                cursor.getString(cursor.getColumnIndex(COLUMN_VINTECINCOPRIMEIROS)),
                cursor.getString(cursor.getColumnIndex(COLUMN_DEZPRIMEIROS)),
                cursor.getString(cursor.getColumnIndex(COLUMN_TRESPRIMEIROS)),
                cursor.getString(cursor.getColumnIndex(COLUMN_VITORIAS)),
                cursor.getString(cursor.getColumnIndex(COLUMN_CRIADO)));

        // close the db connection
        cursor.close();

        return usuario;
    }

    public List<User> recuperaAmigos()
    {
        List<User> amigos = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " +TABLE_NAME_AMIGOS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                User user = new User();
                user.setId(cursor.getString(cursor.getColumnIndex(COLUMN_ID)));
                user.setIcone(cursor.getInt(cursor.getColumnIndex(COLUMN_ICONE)));
                user.setNick(cursor.getString(cursor.getColumnIndex(COLUMN_AMIGOS)));

                amigos.add(user);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return amigos;
    }
    public List<Avatar> recuperarAvatar()
    {
        List<Avatar> avatars = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " +TABLE_NAME_AVATAR;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                Avatar avatar = new Avatar();
                avatar.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                avatar.setAvatar(cursor.getString(cursor.getColumnIndex(COLUMN_AVATAR)));
                avatar.setCriado(cursor.getString(cursor.getColumnIndex(COLUMN_CRIADO)));

                avatars.add(avatar);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return avatars;
    }
    public List<Usuario> recuperarUsuarios()
    {
        List<Usuario> usuarios = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME_USER ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do
            {
                Usuario usuario = new Usuario();
                usuario.setId(cursor.getString(cursor.getColumnIndex(COLUMN_ID)));
                usuario.setScore(cursor.getString(cursor.getColumnIndex(COLUMN_SCORE)));
                usuario.setKd(cursor.getString(cursor.getColumnIndex(COLUMN_KD)));
                usuario.setKill(cursor.getString(cursor.getColumnIndex(COLUMN_KILL)));
                usuario.setVintecincopri(cursor.getString(cursor.getColumnIndex(COLUMN_VINTECINCOPRIMEIROS)));
                usuario.setDezpri(cursor.getString(cursor.getColumnIndex(COLUMN_DEZPRIMEIROS)));
                usuario.setTrespri(cursor.getString(cursor.getColumnIndex(COLUMN_TRESPRIMEIROS)));
                usuario.setVitorias(cursor.getString(cursor.getColumnIndex(COLUMN_VITORIAS)));
                usuario.setCriado(cursor.getString(cursor.getColumnIndex(COLUMN_CRIADO)));

                usuarios.add(usuario);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return usuarios;
    }

//
    public int getQTDAmigos()
    {
        String countQuery = "SELECT  * FROM " + TABLE_NAME_AMIGOS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();
        // return count
        return count;
    }
    public int getQTDAvatares()
    {
        String countQuery = "SELECT  * FROM " + TABLE_NAME_AVATAR;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
    public int getQTDUsuarios()
    {
        String countQuery = "SELECT  * FROM " + TABLE_NAME_USER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }



    public int atualizarAmigo(User user)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_AMIGOS, user.nick);
        values.put(COLUMN_ICONE, user.icone);
        values.put(COLUMN_ID, user.id);

        return db.update(TABLE_NAME_AMIGOS, values, COLUMN_ID+ " = ?",
                new String[]{String.valueOf(user.getId())});
    }
    public int atualizarAvatar(Avatar avatar)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_AVATAR, avatar.avatar);
        values.put(COLUMN_CRIADO, avatar.criado);

        return db.update(TABLE_NAME_AVATAR, values, COLUMN_ID+ " = ?",
                new String[]{String.valueOf(avatar.getId())});
    }
    public int atualizarUsuario(Usuario usuario)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, usuario.id);
        values.put(COLUMN_DEZPRIMEIROS, usuario.dezpri);
        values.put(COLUMN_TRESPRIMEIROS, usuario.trespri);
        values.put(COLUMN_VINTECINCOPRIMEIROS, usuario.vintecincopri);
        values.put(COLUMN_KD, usuario.kd);
        values.put(COLUMN_KILL, usuario.kill);
        values.put(COLUMN_SCORE, usuario.score);
        values.put(COLUMN_VITORIAS, usuario.vitorias);
        values.put(COLUMN_CRIADO,this.getDateTime());

        return db.update(TABLE_NAME_USER, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(usuario.getId())});
    }

    public void deletarAmigo(User user,String ID)
    {
        if (ID == "")
        {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_NAME_AMIGOS, COLUMN_ID+ " = ?",
                    new String[]{String.valueOf(user.getId())});
            db.close();
        }
        else
        {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_NAME_AMIGOS, COLUMN_ID+ " = ?",
                    new String[]{ID});
            db.close();
        }
    }
    public void deletarAvatar(Avatar avatar, String ID)
    {
        if (ID == "")
        {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_NAME_AVATAR, COLUMN_AVATAR+ " = ?",
                    new String[]{String.valueOf(avatar.getAvatar())});
            db.close();
        }
        else
        {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_NAME_AVATAR, COLUMN_AVATAR+ " = ?",
                    new String[]{ID});
            db.close();
        }
    }
    public void deletarUser(Usuario usuario, String ID)
    {
        if (ID == "")
        {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_NAME_USER, COLUMN_ID + " = ?",
                    new String[]{String.valueOf(usuario.getId())});
            db.close();
        }
        else
        {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_NAME_USER, COLUMN_ID + " = ?",
                    new String[]{ID});
            db.close();
        }

    }
    public static String getDateTime()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}