package com.masstersoft.checktv_2_0_2.app;

import android.os.Vibrator;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    private FragmentManager manager;
    private FragmentTransaction transaction;
    private fragment_HEAD frHead;
    private fragment_KEYBOARD frKeyboard;
    private fragment_RESULT frResult;
    private Vibrator vibrator;
    private AdapterForKeyboard adapter;
    private TextView tvSum;
    private CoreApplication coreApp;

    private TextView tvdSumm, tvdVAT, tvdSumPVat, tvdSumMVat, tvdVatInSumm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manager = getSupportFragmentManager();

        frHead = new fragment_HEAD();
        frKeyboard = new fragment_KEYBOARD();
        frResult = new fragment_RESULT();
        vibrator = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
        adapter = new AdapterForKeyboard();

        tvSum = (TextView)findViewById(R.id.tvOUTSumm);
        coreApp = new CoreApplication(getApplicationContext());

        tvdSumm = (TextView)findViewById(R.id.tvdSumm);
        tvdVAT = (TextView) findViewById(R.id.tvdVat);
        tvdSumPVat = (TextView) findViewById(R.id.tvdSumPVat);
        tvdSumMVat =(TextView) findViewById(R.id.tvdSumMVat);
        tvdVatInSumm = (TextView)findViewById(R.id.tvdVatInSumm);

    }

    private void ShowHideKeyRes(View v) {
        transaction = manager.beginTransaction();

        vibrator.vibrate(10);

        switch (v.getId()) {
            case R.id.btnCheck:
                // Нажал кнопку РАСЧЕТ
                if (manager.findFragmentByTag(fragment_KEYBOARD.TAG) != null) {
                    transaction.remove(frKeyboard);
                }
                if (manager.findFragmentByTag(fragment_RESULT.TAG) == null) {
                    transaction.add(R.id.container, frResult, fragment_RESULT.TAG);
                }
                //coreApp.setSum(Double.parseDouble(tvSum.getText().toString()));
                //coreApp.setSum(1000d);
                coreApp.Calculate();
                FillResult();
                break;
            case R.id.btnClear:
                // Нажал кнопку ОЧИСТИТЬ
                if (manager.findFragmentByTag(fragment_RESULT.TAG) != null) {
                    transaction.remove(frResult);
                }
                if (manager.findFragmentByTag(fragment_KEYBOARD.TAG) == null) {
                    transaction.add(R.id.container, frKeyboard, fragment_KEYBOARD.TAG);
                }
                break;
        }
        transaction.commit();
    }

    private void FillResult() {
        tvdSumm.setText(coreApp.getSUM().toString());
        tvdVAT.setText(coreApp.getVAT().toString());
        tvdSumMVat.setText(coreApp.getSUM_mVat().toString());
        tvdSumPVat.setText(coreApp.getSUM_pVat().toString());
        tvdVatInSumm.setText(coreApp.getVAT_inSUM().toString());
    }

    public void ShowMenuVAT(View v) {
        if (v.getId() == R.id.btnVAT) {
            /*dialogVATCountry dlg = new dialogVATCountry();
            FragmentManager fm = getSupportFragmentManager();
            dlg.show(fm, "dialog_vat");*/
            dialogVATManual dlg = new dialogVATManual();
            FragmentManager fm = getSupportFragmentManager();
            dlg.show(fm,"dialog_vat");
        }
    }

    public void onClick(View v) {
        KeyNames.Keys key=getPressedKey(v);
        ShowHideKeyRes(v);
        ShowMenuVAT(v);
        adapter.OnKeyPressed(key);
        tvSum.setText(adapter.getEvaluation());
        if (key == KeyNames.Keys.EQUAL) {
            tvSum.setText(String.valueOf(adapter.Result()));
        }
    }

    private KeyNames.Keys getPressedKey(View v) {
        KeyNames.Keys prKey = KeyNames.Keys.NONE;
        switch (v.getId()) {
            case R.id.btnOne:
                prKey = KeyNames.Keys.ONE;
                break;
            case R.id.btnTwo:
                prKey = KeyNames.Keys.TWO;
                break;
            case R.id.btnThree:
                prKey = KeyNames.Keys.THREE;
                break;
            case R.id.btnFour:
                prKey = KeyNames.Keys.FOUR;
                break;
            case R.id.btnFive:
                prKey = KeyNames.Keys.FIVE;
                break;
            case R.id.btnSix:
                prKey = KeyNames.Keys.SIX;
                break;
            case R.id.btnSeven:
                prKey = KeyNames.Keys.SEVEN;
                break;
            case R.id.btnEight:
                prKey = KeyNames.Keys.EIGHT;
                break;
            case R.id.btnNine:
                prKey = KeyNames.Keys.NINE;
                break;
            case R.id.btnZero:
                prKey = KeyNames.Keys.ZERO;
                break;
            case R.id.btnDot:
                prKey = KeyNames.Keys.DOT;
                break;
            case R.id.btnEquals:
                prKey = KeyNames.Keys.EQUAL;
                break;
            case R.id.btnPlus:
                prKey = KeyNames.Keys.PLUS;
                break;
            case R.id.btnMinus:
                prKey = KeyNames.Keys.MINUS;
                break;
            case R.id.btnMultily:
                prKey = KeyNames.Keys.MULTIPLY;
                break;
            case R.id.btnDivide:
                prKey = KeyNames.Keys.DIVISION;
                break;
            case R.id.btnClear:
                prKey=KeyNames.Keys.CLEAR;
                break;
        }
        return prKey;
    }

    public void onClickKeyBoard(View v) {
        KeyNames.Keys key=getPressedKey(v);
        System.out.println(key);
        adapter.OnKeyPressed(key);

        tvSum.setText(adapter.getEvaluation());
        if (key == KeyNames.Keys.EQUAL) {
            tvSum.setText(String.valueOf(adapter.Result()));
        }

        vibrator.vibrate(40);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
