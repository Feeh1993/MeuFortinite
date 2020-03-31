package com.example.meufortinite.DAO.LOCAL;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.example.meufortinite.MODEL.GERAL.Amigo;
import com.example.meufortinite.MODEL.GERAL.Avatar;
import com.example.meufortinite.MODEL.GERAL.Conversa;
import com.example.meufortinite.MODEL.GERAL.Usuario;

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
    public static  final String TABLE_NAME_CONVERSA = "conversa";

    //DADOS TABELA USUARIO
    public static final String TABLE_NAME_USER = "usuario";
    public static final String COLUMN_VITORIAS = "vitorias";
    public static final String COLUMN_TRESPRIMEIROS = "trespri";
    public static final String COLUMN_DEZPRIMEIROS = "dezpri";
    public static final String COLUMN_VINTECINCOPRIMEIROS = "vintecincopri";
    public static final String COLUMN_KD = "kd";
    public static final String COLUMN_KILL = "kill";
    public static final String COLUMN_SCORE = "score";
    public static final String COLUMN_NICKNAME = "nickname";

    //DADOS TABELA AMIGOS
    public static final String TABLE_NAME_AMIGOS = "amigos";
    public static final String COLUMN_AMIGOS =  "nickname";
    public static final String COLUMN_ICONE = "icone";
    public static final String COLUMN_RANK = "rank";


    //CRIANDO TABELA AMIGOS
    public static final String CREATE_TABLEAMIGOS =
            "CREATE TABLE " + TABLE_NAME_AMIGOS + "("
                    + COLUMN_ID + " TEXT PRIMARY KEY,"
                    + COLUMN_AMIGOS + " TEXT,"
                    + COLUMN_ICONE + " INTEGER,"
                    + COLUMN_RANK + " TEXT"
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
                    + COLUMN_NICKNAME + " TEXT,"
                    + COLUMN_CRIADO + " TEXT"
                    + ")";

    // CRIANDO TABELA USUARIO
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
        values.put(COLUMN_RANK, amigo.rank);


        //INSERIR LINHA
        long id = db.insert(TABLE_NAME_AMIGOS, null, values);

        // FECHAR CONEXAO
        db.close();

        // RETORNA ID INSERIDO
        return id;
    }

    public long inserirConversa(Conversa conversa)
    {
        // ABRIR MODO DE LEITURA DO BANCO
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, conversa.id);
        values.put(COLUMN_AMIGOS, conversa.nickname);
        values.put(COLUMN_CRIADO,this.getDateTime());
        values.put(COLUMN_ULT_MSG, conversa.mensagem);


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
        values.put(COLUMN_NICKNAME, usuario.nickname);


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
                null,cursor.getString(cursor.getColumnIndex(COLUMN_ID)),null,cursor.getString(cursor.getColumnIndex(COLUMN_RANK)));

        // close the db connection
        cursor.close();

        return amigo;
    }

    public Conversa getConversa(long id)
    {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_CONVERSA,
                new String[]{COLUMN_ULT_MSG},
                COLUMN_ULT_MSG + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();
        Conversa conversa = new Conversa(cursor.getString(cursor.getColumnIndex(COLUMN_ULT_MSG)),
                cursor.getString(cursor.getColumnIndex(COLUMN_CRIADO)),
                cursor.getString(cursor.getColumnIndex(COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(COLUMN_NICKNAME)));

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
                cursor.getString(cursor.getColumnIndex(COLUMN_SCORE)),
                cursor.getString(cursor.getColumnIndex(COLUMN_KD)),
                cursor.getString(cursor.getColumnIndex(COLUMN_KILL)),
                cursor.getString(cursor.getColumnIndex(COLUMN_VINTECINCOPRIMEIROS)),
                cursor.getString(cursor.getColumnIndex(COLUMN_DEZPRIMEIROS)),
                cursor.getString(cursor.getColumnIndex(COLUMN_TRESPRIMEIROS)),
                cursor.getString(cursor.getColumnIndex(COLUMN_VITORIAS)),
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
                amigo.setRank(cursor.getString(cursor.getColumnIndex(COLUMN_RANK)));
                amigo.setNick(cursor.getString(cursor.getColumnIndex(COLUMN_AMIGOS)));

                amigos.add(amigo);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return amigos;
    }

    public List<Conversa> recuperaConversas()
    {
        List<Conversa> conversas= new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " +TABLE_NAME_CONVERSA;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do
            {
                Conversa conversa = new Conversa();
                conversa.setId(cursor.getString(cursor.getColumnIndex(COLUMN_ID)));
                conversa.setCriado(cursor.getString(cursor.getColumnIndex(COLUMN_CRIADO)));
                conversa.setMensagem(cursor.getString(cursor.getColumnIndex(COLUMN_ULT_MSG)));
                conversa.setNickname(cursor.getString(cursor.getColumnIndex(COLUMN_NICKNAME)));

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
                usuario.setScore(cursor.getString(cursor.getColumnIndex(COLUMN_SCORE)));
                usuario.setKd(cursor.getString(cursor.getColumnIndex(COLUMN_KD)));
                usuario.setKill(cursor.getString(cursor.getColumnIndex(COLUMN_KILL)));
                usuario.setVintecincopri(cursor.getString(cursor.getColumnIndex(COLUMN_VINTECINCOPRIMEIROS)));
                usuario.setDezpri(cursor.getString(cursor.getColumnIndex(COLUMN_DEZPRIMEIROS)));
                usuario.setTrespri(cursor.getString(cursor.getColumnIndex(COLUMN_TRESPRIMEIROS)));
                usuario.setVitorias(cursor.getString(cursor.getColumnIndex(COLUMN_VITORIAS)));
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
        values.put(COLUMN_RANK, amigo.rank);

        return db.update(TABLE_NAME_AMIGOS, values, COLUMN_ID+ " = ?",
                new String[]{String.valueOf(amigo.getId())});
    }

    public int atualizarConversa(Conversa conversa)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NICKNAME, conversa.nickname);
        values.put(COLUMN_ULT_MSG, conversa.mensagem);
        values.put(COLUMN_ID, conversa.id);
        values.put(COLUMN_CRIADO,this.getDateTime());

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
        values.put(COLUMN_ID, usuario.id);
        values.put(COLUMN_DEZPRIMEIROS, usuario.dezpri);
        values.put(COLUMN_TRESPRIMEIROS, usuario.trespri);
        values.put(COLUMN_VINTECINCOPRIMEIROS, usuario.vintecincopri);
        values.put(COLUMN_KD, usuario.kd);
        values.put(COLUMN_KILL, usuario.kill);
        values.put(COLUMN_SCORE, usuario.score);
        values.put(COLUMN_VITORIAS, usuario.vitorias);
        values.put(COLUMN_NICKNAME, usuario.nickname);
        values.put(COLUMN_CRIADO,this.getDateTime());

        return db.update(TABLE_NAME_USER, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(usuario.getId())});
    }

    public void deletarAmigo(Conversa conversa, String ID)
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