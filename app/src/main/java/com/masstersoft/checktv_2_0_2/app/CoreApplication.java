package com.masstersoft.checktv_2_0_2.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.math.BigDecimal;

/**
 * Created by WismutPC on 16.03.15.
 */
public class CoreApplication {

    private final String SETTINGS_NAME="Check_TV_Settings";

    private Context appContext;

    //=============== Настройки ===================
    private boolean isVibration = true;
    private int countAfterDot = 2;
    private boolean isManualVat = true;
    private boolean isSaveLastValue = true;

    //=========== Поля для расчета НДС ============
    private Double dSumm;
    private Double dVATPercent;
    private Double dSumPVat;
    private Double dVat;
    private Double dSumMVat;
    private Double dVatInSumm;

    //=========== Методы для сохранения/загрузки настроек, для расчетов ===============
    public void loadSettings() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(appContext);//.getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE);
        dVATPercent=18d;
        dSumm = 1000d;
    }

    public void saveSettings() {
    }

    public void setSum(Double d) {
        this.dSumm = d;
    }

    public void setVATPercent(Double v) {
        this.dVATPercent = v;
    }

    public void Calculate() {
        BigDecimal decimal;

        dSumMVat = (dSumm * 100) / (100 + dVATPercent);

        decimal = new BigDecimal(dSumMVat);
        decimal = decimal.setScale(countAfterDot, BigDecimal.ROUND_HALF_EVEN);
        dSumMVat = decimal.doubleValue();

        dVatInSumm = (dSumm * dVATPercent) / (100 + dVATPercent);
        decimal = new BigDecimal(dVatInSumm);
        decimal = decimal.setScale(countAfterDot, BigDecimal.ROUND_HALF_EVEN);
        dVatInSumm = decimal.doubleValue();

        dSumPVat = dSumm * (100 + dVATPercent) / 100;
        decimal = new BigDecimal(dSumPVat);
        decimal = decimal.setScale(countAfterDot, BigDecimal.ROUND_HALF_EVEN);
        dSumPVat = decimal.doubleValue();

        dVat = (dSumm * dVATPercent) / 100;
        decimal = new BigDecimal(dVat);
        decimal = decimal.setScale(countAfterDot, BigDecimal.ROUND_HALF_EVEN);
        dVat = decimal.doubleValue();
    }

    public CoreApplication(Context context) {
        this.appContext = context;
        loadSettings();
        //Calculate();
    }

    public Double getSUM(){return dSumm;}
    public Double getVAT_inSUM(){return dVatInSumm;}
    public Double getSUM_mVat(){return dSumMVat;}
    public Double getSUM_pVat(){return dSumPVat;}
    public Double getVAT(){return dVat;}
}
