package com.loftschool.loftcoin.work;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import com.loftschool.loftcoin.App;
import com.loftschool.loftcoin.R;
import com.loftschool.loftcoin.data.api.Api;
import com.loftschool.loftcoin.data.db.DataBase;
import com.loftschool.loftcoin.data.db.model.CoinEntity;
import com.loftschool.loftcoin.data.db.model.CoinEntityMapper;
import com.loftschool.loftcoin.data.db.model.CoinEntityMapperImpl;
import com.loftschool.loftcoin.data.db.model.QouteEntity;
import com.loftschool.loftcoin.data.prefs.Prefs;
import com.loftschool.loftcoin.screens.main.MainActivity;
import com.loftschool.loftcoin.utils.CurrencyFormatter;
import com.loftschool.loftcoin.utils.Fiat;

import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class SyncRateWorker extends Worker {

    public static final String EXTRA_SYMBOL = "symbol";

    private static final String NOTIFICATION_CHANNEL_RATE_CHANGED = "RATE_CHANGED";
    private static final int NOTIFICATION_ID_RATE_CHANGED = 10;

    public SyncRateWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        App app = (App) getApplicationContext();
        Api api = app.getApi();

        CoinEntityMapper mapper = new CoinEntityMapperImpl();


        Disposable disposable = api.rates(Api.CONVERT)
                .map(rateResponse -> mapper.map(rateResponse.data))
                .subscribe(
                        this::handleCoins,
                        this::handleError
                );

        return Result.success();
    }

    private void handleError(Throwable error) {

    }

    private void handleCoins(List<CoinEntity> newCoins) {
        App app = (App) getApplicationContext();
        Prefs prefs = app.getPrefs();
        DataBase dataBase = app.getDataBase();
        CurrencyFormatter formatter = new CurrencyFormatter();

        String symbol = getInputData().getString(EXTRA_SYMBOL);


        dataBase.open();

        Fiat fiat = Fiat.USD;

        CoinEntity oldCoin = dataBase.getCoin(symbol);
        CoinEntity newCoin = finCoin(newCoins, symbol);

        if (oldCoin != null && newCoin != null) {
            QouteEntity oldQuote = oldCoin.getQuote(fiat);
            QouteEntity newQuote = newCoin.getQuote(fiat);

            if (newQuote.price != (oldQuote.price + 1)) {

                Random random = new Random();

                double priceDiff = newQuote.price - (oldQuote.price + random.nextInt(100));

                String price = formatter.format(Math.abs(priceDiff), false);
                String priceDiffString;

                if (priceDiff > 0) {
                    priceDiffString = "+ " + price + " " + fiat.symbol;
                } else {
                    priceDiffString = "- " + price + " " + fiat.symbol;
                }

                showRateChangedNotification(newCoin, priceDiffString);
            }
        }

        dataBase.saveCoins(newCoins);

        dataBase.close();

    }


    private CoinEntity finCoin(List<CoinEntity> newCoins, String symbol) {
        for (CoinEntity coin : newCoins) {
            if (coin.symbol.equals(symbol)) {
                return coin;
            }
        }
        return null;
    }

    private void showRateChangedNotification(CoinEntity newCoin, String priceDiff) {
        Timber.d("showRateChangedNotification coin = %s, priceDiff = %s", newCoin.name, priceDiff);

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);


        Notification notification = new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_CHANNEL_RATE_CHANGED)
                .setSmallIcon(R.drawable.ic_notiffication)
                .setContentTitle(newCoin.name)
                .setContentText(getApplicationContext().getString(R.string.notification_rate_changed_body, priceDiff))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setLights(Color.RED, 200, 100)
                .build();


        NotificationManager notificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_RATE_CHANGED,
                    getApplicationContext().getString(R.string.notification_channel_rate_changed),
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            notificationManager.createNotificationChannel(channel);
        }


        notificationManager.notify(newCoin.id, notification);

    }

}
