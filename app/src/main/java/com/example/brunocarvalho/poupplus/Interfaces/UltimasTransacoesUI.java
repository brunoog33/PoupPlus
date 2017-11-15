package com.example.brunocarvalho.poupplus.Interfaces;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.brunocarvalho.poupplus.Controle.UltimasTransacoes;
import com.example.brunocarvalho.poupplus.R;

/**
 * Created by BrunoCarvalho on 26/11/2016.
 */
public class UltimasTransacoesUI extends AppCompatActivity{

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private UltimasTransacoes ultimasTransacoes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ultimas_transacoes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_movimentacoes);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        //mAdapter = new MyAdapter(myDataset);
        //mRecyclerView.setAdapter(mAdapter);
    }
}
