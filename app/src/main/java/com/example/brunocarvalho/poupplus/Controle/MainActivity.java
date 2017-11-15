package com.example.brunocarvalho.poupplus.Controle;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.brunocarvalho.poupplus.DAO.DatabaseHelper;
import com.example.brunocarvalho.poupplus.Interfaces.LancamentoUI;
import com.example.brunocarvalho.poupplus.Interfaces.UltimasTransacoesUI;
import com.example.brunocarvalho.poupplus.R;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper _helper;
    private TextView       _tvSaldoAtual;
    private TextView       _tvAcumulado;
    private TextView       _tvUltimoSaldo;
    private TextView       _tvUltimoDebito;
    private TextView       _tvJuro;
    private CardView       _cdUltimasTransacoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _helper = new DatabaseHelper(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _helper.close();
                startActivity(new Intent(MainActivity.this, LancamentoUI.class));
            }
        });

        _cdUltimasTransacoes = (CardView) findViewById(R.id.card_view_ultimas_trans);
        _cdUltimasTransacoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _helper.close();
                startActivity(new Intent(MainActivity.this, UltimasTransacoesUI.class));
            }
        });

        chargeResult();

    }

    @Override
    protected void onResume() {
        atualizarTela();
        super.onResume();
    }

    private void chargeResult() {
        _tvSaldoAtual = (TextView)findViewById(R.id.main_saldo_atual_result);
        _tvAcumulado  = (TextView)findViewById(R.id.main_acumulado_result);
        _tvUltimoSaldo = (TextView)findViewById(R.id.main_ultimo_saldo_data);
        _tvUltimoDebito = (TextView)findViewById(R.id.main_ultimo_debito_data);
        _tvJuro = (TextView)findViewById(R.id.main_ultimo_juro_data);
        _tvSaldoAtual.setText(getSaldoAtual());
        _tvAcumulado.setText(getAcumulado());
        _tvUltimoSaldo.setText(getUltimasTransacoes(2));
        _tvJuro.setText(getUltimasTransacoes(3));
    }

    private void atualizarTela(){
        _tvSaldoAtual.setText(getSaldoAtual());
        _tvAcumulado.setText(getAcumulado());
        _tvUltimoSaldo.setText(getUltimasTransacoes(2));
        _tvUltimoDebito.setText(getUltimasTransacoes(1));
        _tvUltimoDebito.setText(getUltimasTransacoes(1));
        _tvJuro.setText(getUltimasTransacoes(3));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            resetarValores();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void resetarValores() {
        SQLiteDatabase db = _helper.getWritableDatabase();
        db.execSQL("DELETE FROM TRANSACOES");
        atualizarTela();
    }

    private String getSaldoAtual() {
        SQLiteDatabase db = _helper.getReadableDatabase();
        String valor = "R$ 0.00";

        final StringBuilder sbQuery = new StringBuilder();
        sbQuery.append(" SELECT ROUND(TOTAL(VALOR),2) AS TOTAL");
        sbQuery.append(" FROM TRANSACOES");
        sbQuery.append(" WHERE DATE(DATAOPERACAO) >= (SELECT DATE(DATAOPERACAO) FROM TRANSACOES WHERE OPERACAO = '0' ORDER BY DATAOPERACAO DESC LIMIT 1)");
        Cursor cr = db.rawQuery(sbQuery.toString(),null);

        if(cr.moveToFirst()){
            valor = "R$" + cr.getDouble(0);
        }
        cr.close();

        return valor;
    }

    private String getAcumulado() {
        SQLiteDatabase db = _helper.getReadableDatabase();
        Calendar cal = Calendar.getInstance();
        String valor = "R$ 0.00";
        String dataAnterior = "";
        String dataAtual = "";

        if(cal.get(Calendar.DAY_OF_MONTH) > 19){
            dataAnterior = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH)+1) + "-20";
            dataAtual    = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH)+2) + "-20";
        }
        else{
            if( cal.get(Calendar.MONTH) == 0){
                dataAnterior = (cal.get(Calendar.YEAR)-1) + "-12" + "-20";
            }
            else {
                dataAnterior = cal.get(Calendar.YEAR) + "-0" + cal.get(Calendar.MONTH) + "-20";
            }
            dataAtual    = cal.get(Calendar.YEAR) + "-0" + (cal.get(Calendar.MONTH)+1) + "-20";
        }


        final StringBuilder sbQuery = new StringBuilder();

        sbQuery.append(" SELECT ROUND(TOTAL(VALOR),2) AS TOTALACUMULADO ");
        sbQuery.append(" FROM TRANSACOES                       ");
        sbQuery.append(" WHERE DATE(DATAOPERACAO) BETWEEN DATE('"+ dataAnterior +"') AND DATE('"+ dataAtual +"') ");
        sbQuery.append("   AND OPERACAO IN ('1','2','3')       ");
        Cursor cr = db.rawQuery(sbQuery.toString(),null);

        if(cr.moveToFirst()){
            valor = "R$" + cr.getDouble(0);
        }
        cr.close();

        return valor;
    }

    private String getUltimasTransacoes(final int transacao) {
        SQLiteDatabase db = _helper.getReadableDatabase();
        String valor = "R$ 0.00";
        String data  = "";
        String dia   = "";
        String mes   = "";
        String ano   = "";

        final StringBuilder sbQuery = new StringBuilder();
        sbQuery.append("SELECT VALOR,              ");
        sbQuery.append("       DATE(DATAOPERACAO)  ");
        sbQuery.append("FROM TRANSACOES            ");
        sbQuery.append("WHERE OPERACAO = " + "'" + transacao + "'" );
        sbQuery.append("ORDER BY DATAOPERACAO DESC ");
        sbQuery.append("LIMIT 1 ");
        Cursor cr = db.rawQuery(sbQuery.toString(),null);

        if(cr.moveToFirst()){
            valor = "R$" + (cr.getDouble(0) < 0 ? cr.getDouble(0) * -1 : cr.getDouble(0));
            data  = cr.getString(1);
        }
        cr.close();

        if(data.length() > 5){
            dia  = data.substring(8,10);
            mes  = data.substring(5,7);
            ano  = data.substring(0,4);
            data = (dia+"/"+mes+"/"+ano);
        }

        return valor + "     " + data ;
    }
}
