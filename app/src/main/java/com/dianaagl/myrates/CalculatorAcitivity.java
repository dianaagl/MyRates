package com.dianaagl.myrates;

import android.app.Activity;
import android.os.Bundle;
import android.util.StringBuilderPrinter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.dianaagl.myrates.networking.Currency;
import org.w3c.dom.Text;

/**
 * Created by user8 on 29.04.2017.
 */

public class CalculatorAcitivity extends Activity {

    private TextView summ;
    private TextView inputVal;
    private TextView name_of_val;
    private Button calc;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator);
        final String code = getIntent().getStringExtra("code");
        final double course = getIntent().getDoubleExtra("course",0);
        summ = (TextView) findViewById(R.id.summ);
        name_of_val = (TextView) findViewById(R.id.name_of_val);
        inputVal = (TextView) findViewById(R.id.input_num);
        calc = (Button) findViewById(R.id.calc);
        name_of_val.setText(code);

        calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    double val = Integer.parseInt(inputVal.getText().toString());
                    summ.setText(String.valueOf(val* course));


                }
                catch (NumberFormatException e){

                }
            }
        });


    }

}
