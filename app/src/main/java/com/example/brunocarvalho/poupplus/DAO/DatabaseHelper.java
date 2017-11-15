package com.example.brunocarvalho.poupplus.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by BrunoCarvalho on 19/11/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    //Informações do banco de dados
    private static final int    VERSAO      = 1;
    private static final String BANCO_DADOS = "POUPPLUS";
    //Nomes das tabelas
    public static final String TRANSACOES = "TRANSACOES";



    public DatabaseHelper(Context context) {
        super(context, BANCO_DADOS, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        tableTransacoes(db);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void tableTransacoes(final SQLiteDatabase db) {

        final StringBuilder sbQuery = new StringBuilder();
        sbQuery.append(" CREATE TABLE TRANSACOES ");
        sbQuery.append(" (_id INTEGER PRIMARY KEY, ");
        sbQuery.append(" VALOR NUMERIC, ");
        sbQuery.append(" OPERACAO INTEGER, ");
        sbQuery.append(" DATAOPERACAO DATETIME, ");
        sbQuery.append(" CONTA VARCHAR(50)); ");
        db.execSQL(sbQuery.toString());
    }
}
