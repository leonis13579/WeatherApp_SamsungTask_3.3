package ru.realityfamily.weatherapp_samsungtask_33.Services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class WeatherService extends Service {
    public WeatherService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        WeatherAsyncTask weatherAsyncTask = new WeatherAsyncTask();
        weatherAsyncTask.execute();

        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class WeatherAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {

            String result;
            try {
                //загружаем данные
                URL url = new URL("http://icomms.ru/inf/meteo.php?tid=35");

                // ”оборачиваем” для удобства чтения
                Scanner in = new Scanner((InputStream) url.getContent());

                // читаем и добавляем имя JSON массива
                result = "{\"weather\":" + in.nextLine() + "}";
            } catch (Exception e) {
                result = e.toString();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            Intent i = new Intent("WeatherService"); // интент для отправки ответа
            i.putExtra("INFO", s); // добавляем в интент данные
            sendBroadcast(i); // рассылаем
        }
    }
}