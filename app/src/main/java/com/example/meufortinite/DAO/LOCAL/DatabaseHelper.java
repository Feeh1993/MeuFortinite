package com.example.meufortinite.DAO.LOCAL;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.example.meufortinite.MODEL.GERAL.Amigo;
import com.example.meufortinite.MODEL.GERAL.Avatar;
import com.example.meufortinite.MODEL.GERAL.Mensagem;
import com.example.meufortinite.MODEL.GERAL.Usuario;
import com.example.meufortinite.VIEW.FRAGMENT.Conversas;

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

    //DADOS TABELA CONVERSA
    public static final String COLUMN_ULT_MSG =  "ultmsg";
    public static final String COLUMN_RECEBIDO =  "recebido";
    public static  final String TABLE_NAME_CONVERSA = "conversa";

    //DADOS TABELA USUARIO
    public static final String TABLE_NAME_USER = "usuario";
    public static final String COLUMN_NICKNAME = "nickname";

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

    //CRIANDO TABELA CONVERSA
    public static final String CREATE_TABLECONVERSA =
            "CREATE TABLE " + TABLE_NAME_CONVERSA + "("
                    + COLUMN_ID + " TEXT PRIMARY KEY,"
                    + COLUMN_ULT_MSG + " TEXT,"
                    + COLUMN_CRIADO + " TEXT,"
                    + COLUMN_RECEBIDO + " TEXT,"
                    + COLUMN_NICKNAME + " TEXT"
                    + ")";

    // CRIANDO TABELA USUARIO
    public static final String CREATE_TABLEUSER =
            "CREATE TABLE " + TABLE_NAME_USER + "("
                    + COLUMN_ID + " TEXT PRIMARY KEY,"
                    + COLUMN_NICKNAME + " TEXT,"
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
        db.execSQL(CREATE_TABLECONVERSA);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_AVATAR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_AMIGOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_CONVERSA);

        // Create tables again
        onCreate(db);
    }


    public long inserirAmigo(Amigo amigo)
    {
        // ABRIR MODO DE LEITURA DO BANCO
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, amigo.id);
        values.put(COLUMN_AMIGOS, amigo.nick);
        values.put(COLUMN_ICONE, amigo.icone);

        //INSERIR LINHA
        long id = db.insert(TABLE_NAME_AMIGOS, null, values);

        // FECHAR CONEXAO
        db.close();

        // RETORNA ID INSERIDO
        return id;
    }

    public long inserirConversa(Mensagem conversa)
    {
        // ABRIR MODO DE LEITURA DO BANCO
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, conversa.getId());
        values.put(COLUMN_NICKNAME, conversa.getUsername());
        values.put(COLUMN_RECEBIDO, conversa.getRecebido());
        values.put(COLUMN_CRIADO,this.getDateTime());
        values.put(COLUMN_ULT_MSG, conversa.getMessagem());


        //INSERIR LINHA
        long id = db.insert(TABLE_NAME_CONVERSA, null, values);

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
        values.put(COLUMN_ID, usuario.getId());
        values.put(COLUMN_NICKNAME, usuario.getNickname());

        // insert row
        long id = db.insert(TABLE_NAME_USER, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public Amigo getAmigo(long id)
    {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_AMIGOS,
                new String[]{COLUMN_AMIGOS},
                COLUMN_AMIGOS + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();
        Amigo amigo = new Amigo
                (cursor.getInt(cursor.getColumnIndex(COLUMN_ICONE)),
                cursor.getString(cursor.getColumnIndex(COLUMN_AMIGOS)),
                null,cursor.getString(cursor.getColumnIndex(COLUMN_ID)),null);

        // close the db connection
        cursor.close();

        return amigo;
    }

    public Mensagem getConversa(long id)
    {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_CONVERSA,
                new String[]{COLUMN_ULT_MSG},
                COLUMN_ULT_MSG + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();
        Mensagem conversa = new Mensagem(cursor.getString(cursor.getColumnIndex(COLUMN_ID)),
                                         cursor.getString(cursor.getColumnIndex(COLUMN_ULT_MSG)),
                                         cursor.getString(cursor.getColumnIndex(COLUMN_CRIADO)),
                                         cursor.getString(cursor.getColumnIndex(COLUMN_RECEBIDO)),
                                         cursor.getString(cursor.getColumnIndex(COLUMN_NICKNAME))
                );

        // close the db connection
        cursor.close();

        return conversa;
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
                cursor.getString(cursor.getColumnIndex(COLUMN_CRIADO)),
                cursor.getString(cursor.getColumnIndex(COLUMN_NICKNAME)));

        // close the db connection
        cursor.close();

        return usuario;
    }

    public List<Amigo> recuperaAmigos()
    {
        List<Amigo> amigos = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " +TABLE_NAME_AMIGOS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                Amigo amigo = new Amigo();
                amigo.setId(cursor.getString(cursor.getColumnIndex(COLUMN_ID)));
                amigo.setIcone(cursor.getInt(cursor.getColumnIndex(COLUMN_ICONE)));
                amigo.setNick(cursor.getString(cursor.getColumnIndex(COLUMN_AMIGOS)));

                amigos.add(amigo);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return amigos;
    }

    public List<Mensagem> recuperaConversas()
    {
        List<Mensagem> conversas= new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " +TABLE_NAME_CONVERSA;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                Mensagem conversa = new Mensagem();
                conversa.setId(cursor.getString(cursor.getColumnIndex(COLUMN_ID)));
                conversa.setMessagem(cursor.getString(cursor.getColumnIndex(COLUMN_ULT_MSG)));
                conversa.setData(cursor.getString(cursor.getColumnIndex(COLUMN_CRIADO)));
                conversa.setRecebido(cursor.getString(cursor.getColumnIndex(COLUMN_RECEBIDO)));
                conversa.setUsername(cursor.getString(cursor.getColumnIndex(COLUMN_NICKNAME)));

                conversas.add(conversa);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return conversas;
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
                usuario.setCriado(cursor.getString(cursor.getColumnIndex(COLUMN_CRIADO)));
                usuario.setNickname(cursor.getString(cursor.getColumnIndex(COLUMN_NICKNAME)));

                usuarios.add(usuario);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return usuarios;
    }

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

    public int getQTDConversas()
    {
        String countQuery = "SELECT  * FROM " + TABLE_NAME_CONVERSA;
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

    public int atualizarAmigo(Amigo amigo)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_AMIGOS, amigo.nick);
        values.put(COLUMN_ICONE, amigo.icone);
        values.put(COLUMN_ID, amigo.id);

        return db.update(TABLE_NAME_AMIGOS, values, COLUMN_ID+ " = ?",
                new String[]{String.valueOf(amigo.getId())});
    }

    public int atualizarConversa(Mensagem conversa)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, conversa.getId());
        values.put(COLUMN_ULT_MSG, conversa.getMessagem());
        values.put(COLUMN_CRIADO,this.getDateTime());
        values.put(COLUMN_RECEBIDO, conversa.getRecebido());
        values.put(COLUMN_NICKNAME,conversa.getUsername());

        return db.update(TABLE_NAME_CONVERSA, values, COLUMN_ID+ " = ?",
                new String[]{String.valueOf(conversa.getId())});
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
        values.put(COLUMN_ID, usuario.getId());
        values.put(COLUMN_NICKNAME, usuario.getNickname());
        values.put(COLUMN_CRIADO,this.getDateTime());

        return db.update(TABLE_NAME_USER, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(usuario.getId())});
    }

    public void deletarAmigo(Mensagem conversa, String ID)
    {
        if (ID == "")
        {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_NAME_CONVERSA, COLUMN_ID+ " = ?",
                    new String[]{String.valueOf(conversa.getId())});
            db.close();
        }
        else
        {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_NAME_CONVERSA, COLUMN_ID+ " = ?",
                    new String[]{ID});
            db.close();
        }
    }

    public void deletarAmigo(Amigo amigo, String ID)
    {
        if (ID == "")
        {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_NAME_AMIGOS, COLUMN_ID+ " = ?",
                    new String[]{String.valueOf(amigo.getId())});
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
    public void deletarConversa(Mensagem conversa, String ID)
    {
        if (ID == "")
        {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_NAME_CONVERSA, COLUMN_ID+ " = ?",
                    new String[]{String.valueOf(conversa.getId())});
            db.close();
        }
        else
        {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_NAME_CONVERSA, COLUMN_ID+ " = ?",
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