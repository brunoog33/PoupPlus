package com.example.brunocarvalho.poupplus.Interfaces;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.brunocarvalho.poupplus.Controle.Lancamento;
import com.example.brunocarvalho.poupplus.DAO.DatabaseHelper;
import com.example.brunocarvalho.poupplus.R;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Calendar;

public class LancamentoUI extends AppCompatActivity {

    private EditText edtValor;
    private Button btnData;
    private Spinner spnOperacao;
    private ImageButton imgSalvar;
    private int ano;
    private int mes;
    private int dia;
    private int salvou = 0;

    private Lancamento lancamento;
    private DatabaseHelper helper;

    Calendar cal = Calendar.getInstance();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lancamento);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        loadControls();
    }

    private void loadControls() {
        lancamento = new Lancamento();
        helper = new DatabaseHelper(this);
        edtValor = (EditText) findViewById(R.id.edtValorLancamento);
        btnData = (Button) findViewById(R.id.btnData);
        spnOperacao = (Spinner) findViewById(R.id.spnOperacao);
        imgSalvar = (ImageButton) findViewById(R.id.img_salvar);
        dia = cal.get(Calendar.DAY_OF_MONTH);
        mes = cal.get(Calendar.MONTH) + 1;
        ano = cal.get(Calendar.YEAR);

        btnData.setText(dia + "/" + mes + "/" + ano);
        lancamento.setDataTransacao(cal);


        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, R.layout.support_simple_spinner_dropdown_item);
        adapter.add("Selecione");
        adapter.add("Débito");
        adapter.add("Depósito");
        adapter.add("Juros");
        spnOperacao.setAdapter(adapter);
        lancamento.setOperacao(spnOperacao.getSelectedItemPosition());

        imgSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preencheDemaisCampos();
                if (lancamento.salvar(helper)) {
                    mensagem("Estamos guardando sua movimentação!");
                    salvou = 1;
                    onBackPressed();
                } else {
                    mensagem("Desculpe-nos, não conseguimos salvar!");
                }

            }
        });

    }

    private void dialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Deseja descartar a movimentação?");
        builder.setPositiveButton("Descartar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                salvou = 1;
                onBackPressed();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create();
        builder.show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (R.id.btnData == id) {
            return new DatePickerDialog(this, listener, ano, mes - 1, dia);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            cal.set(year, monthOfYear, dayOfMonth);
            lancamento.setDataTransacao(cal);
            btnData.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
        }
    };

    public void selecionarData(View view) {
        showDialog(view.getId());
    }

    private void preencheDemaisCampos() {
        lancamento.setValor(Double.parseDouble(edtValor.getText().toString()));
        lancamento.setOperacao(spnOperacao.getSelectedItemPosition());
    }

    private void mensagem(final String mensagem) {
        Toast toast = Toast.makeText(this, mensagem, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onBackPressed() {
        if (salvou == 0) {
           dialog();
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        helper.close();
        super.onDestroy();
    }
}
