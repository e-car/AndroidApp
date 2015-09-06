package com.example.u310_adm.socketclient;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.CancellationSignal.OnCancelListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


/**
 * Created by U310-Adm on 2015/08/22.
 */

public class MyDisplayAsyncTask extends AsyncTask<String, String, Long> implements OnCancelListener {

    private Context mainContext;
    TextView tvVoltage;
    TextView tvTemperature;
    TextView tvAmpere;
    ToggleButton tglbtnConnect;
    /*** クライアントタスクオブジェクト作成 ***/
    private CarInformation carInfo;
    private MyClientAsyncTask clientAsyncTask;
    private SocketControl socketCtrl;

    /**
     * コンストラクタ **
     */
    public MyDisplayAsyncTask(Context context, CarInformation carInfo, SocketControl socketCtrl,
                              TextView tvVoltage, TextView tvTemperature, TextView tvAmpere) {
        this.mainContext = context;
        this.tvAmpere = tvAmpere;
        this.tvTemperature = tvTemperature;
        this.tvVoltage = tvVoltage;
        this.carInfo = carInfo;
        this.socketCtrl = socketCtrl;
//        this.clientAsyncTask = clientAsyncTask;
//        this.tglbtnConnect = tglbtnConnect;
    }

    /**
     * doInBackgraundメソッドの前にUIスレッド上で実行される **
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Toast.makeText(mainContext, "Display更新開始", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Long doInBackground(String... params) {
        while (true) {       //繰り返し
            /*ディスプレイ表示更新*/
            publishProgress("DisplayRefresh");
            try{
                Thread.sleep(500);
            }
            catch (InterruptedException e){
            }
            /*"get"を送信して情報を取得*/
            publishProgress("GetData");
            /*情報取得が終わるまで待つ,更新時間*/
            try{
                Thread.sleep(3500);//
            }
            catch (InterruptedException e){
            }
        }
    }

    /**
     * doInBackgraundメソッドの後にUIスレッド上で実行される **
     */
    @Override
    protected void onPostExecute(Long aLong) {
        super.onPostExecute(aLong);
    }

    @Override
    protected void onProgressUpdate(String... str) {
        super.onProgressUpdate(str);

        switch (str[0]) {
            case "DisplayRefresh":
                tvVoltage.setText(carInfo.getVoltage());
                tvAmpere.setText(String.valueOf(carInfo.getAmpere()));
                tvTemperature.setText(String.valueOf(carInfo.getTemperature()));
                break;
            case "GetData":
                socketCtrl.socketSend("get");
                break;
        }
    }

    @Override
    public void onCancel()
    {
    }
}