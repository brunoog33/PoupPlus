package com.example.brunocarvalho.poupplus.Controle;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.example.brunocarvalho.poupplus.DAO.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * Created by BrunoCarvalho on 31/07/2016.
 */
public class Lancamento {

    private Calendar dataTransacao;
    private int operacao;
    private double valor;

    public Calendar getDataTransacao() {
        return dataTransacao;
    }

    public void setDataTransacao(Calendar dataTransacao) {

        this.dataTransacao = dataTransacao;
    }

    public int getOperacao() {
        return operacao;
    }

    public void setOperacao(int operacao) {
        this.operacao = operacao;
    }

    public double getValor() {
        return valor;
    }

    public String getDataFormatada(final int tipo){
        final StringBuilder dataFormatada = new StringBuilder();
        final int mes = getDataTransacao().get(Calendar.MONTH)+1;
        final int dia = getDataTransacao().get(Calendar.DAY_OF_MONTH);
        if(tipo > 0){//Exibição
            dataFormatada.append((dia < 10 ? "0" + dia : dia));
            dataFormatada.append("/" + (mes < 10 ? "0" + mes : mes));
            dataFormatada.append("/" + getDataTransacao().get(Calendar.YEAR));
        }
        else {
            dataFormatada.append(getDataTransacao().get(Calendar.YEAR));
            dataFormatada.append("-" + (mes < 10 ? "0" + mes : mes));
            dataFormatada.append("-" + (dia < 10 ? "0" + dia : dia));
        }
        return dataFormatada.toString();

        /*final Calendar cal = Calendar.getInstance();
		try {
			final SimpleDateFormat curFormFat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			cal.setTime(getDataTransacao());
		}
		catch (final Exception e) {
			Sys.addLog(e);
		}
		return (cal);*/
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public boolean salvar(DatabaseHelper helper){
        SQLiteDatabase db = helper.getWritableDatabase();
        double valor = getValor();

        ContentValues values = new ContentValues();
        values.put("VALOR", getOperacao() == 1 ? - valor : valor);
        values.put("OPERACAO", getOperacao());
        values.put("DATAOPERACAO", getDataFormatada(0));
        values.put("CONTA", "SANTANDER");

        long resultado = db.insert(helper.TRANSACOES, null, values);

        return resultado != -1 ? true : false;
    }

}
