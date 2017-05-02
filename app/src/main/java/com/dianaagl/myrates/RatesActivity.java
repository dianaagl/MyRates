package com.dianaagl.myrates;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.dianaagl.myrates.networking.Currency;

public class RatesActivity extends Activity {
    private ListView myList;
    private MyAdapter adapter;
    private CurrenciesStorage mStorage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rates);
        myList = (ListView) findViewById(R.id.ListView);
        mStorage = ((SampleApplication) getApplication()).getStorage();



        adapter = new MyAdapter(mStorage);
        myList.setAdapter(adapter);
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                launchCalculatorActivity(position);


            }
        });
    }
    private void launchApp(){

    }
    private void launchCalculatorActivity(int position){
        Intent viewIntent = new Intent(this,CalculatorAcitivity.class);
        String code = mStorage.getLoadedList().getCurrencies().get(position).getCharCode();
        viewIntent.putExtra("code",code);
        viewIntent.putExtra("course",mStorage.getLoadedList().getCurrencies().get(position).getValue());
        startActivity(viewIntent);

    }


    @Override
    protected void onDestroy() {
        adapter.Cancel();
        super.onDestroy();
    }
}

