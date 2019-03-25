package com.loftschool.loftcoin.data.prefs;

import com.loftschool.loftcoin.utils.Fiat;

public interface Prefs {
    boolean isFirstLaunch();

    void setFirstLaunch(boolean firstLaunch);

    Fiat getFiatCurrency();

    void setFiatCurrency(Fiat fiat);

}
