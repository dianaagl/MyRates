package com.dianaagl.myrates;

import android.app.Application;

/**
 * Created by Диана on 01.05.2017.
 */
public class SampleApplication extends Application{
    private CurrenciesStorage storage = new CurrenciesStorage();

    public CurrenciesStorage getStorage() {
        return storage;
    }
}

