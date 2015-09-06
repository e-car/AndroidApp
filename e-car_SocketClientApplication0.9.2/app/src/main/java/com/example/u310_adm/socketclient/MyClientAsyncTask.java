package com.example.u310_adm.socketclient;

import android.content.Context;
import android.os.AsyncTask;
import android.os.CancellationSignal.OnCancelListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class MyClientAsyncTask extends AsyncTask<String, String, Long> implements OnCancelListener
{
    private Context context;    //Activityのコンテキスト
    String ServerIP;             //サーバIPアドレス
    int ServerPort;             //サーバのポート
    ToggleButton tglbtnConnect; //接続用のトグルボタンオブジェクト
    String receive;
    int count;

    Socket server;               //サーバソケット
    InputStream in;
    OutputStream out;
    BufferedReader br;
    PrintWriter pw;

    String strSend;
    CarInformation carInfo;

    public boolean isServer = false;   //サーバ接続チェックフラグ
    boolean isLogout = false;
    boolean isSendData = false;

    long time;
    private static final int DELAY = 33; //スレッド周期



    /*** コンストラクタ ***/
    public MyClientAsyncTask(Context context, String ServerIP, int ServerPort,ToggleButton tglbtnConnect, CarInformation carInfo)
    {
        this.context = context;
        this.ServerIP = ServerIP;
        this.ServerPort = ServerPort;
        this.tglbtnConnect = tglbtnConnect;
        this.carInfo = carInfo;
    }


    /*** doInBackgraundメソッドの前にUIスレッド上で実行される ***/
    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
        Toast.makeText(context, "Connecting to Server...", Toast.LENGTH_SHORT).show();
    }



    @Override
    protected Long doInBackground(String... params)
    {
        try {
            //サーバへのソケット作成
            server = new Socket(ServerIP, ServerPort);

            isServer = true;                              //サーバ接続成功　フラグtrue

            in = server.getInputStream();       //入力ストリーム取得
            out = server.getOutputStream();     //出力ストリーム取得
            br = new BufferedReader(new InputStreamReader(in));
            pw = new PrintWriter(out);

            publishProgress("connected");

            while(true) {
                time = System.nanoTime();

                if(isSendData) {
                    pw.println(strSend);    //エディットテキストの文字列を送信
                    pw.flush();              //pwのフラッシュ

                    isSendData = false;

                    if(strSend.equals("logout")){
                        publishProgress("logout");
                        break;
                    }
                    if(strSend.equals("get")){
                        count = 0;
                        while(receive == "end" || isSendData == false){
                            receive = br.readLine();          //サーバからの返信待ち
                            publishProgress("receive", receive);     //画面に返信文字列を表示
                            count ++;
                        }
                    }
                }

                try {
                    Thread.sleep(33);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
            publishProgress("error");
            isServer = false;
        }
        catch (IOException e) {     //サーバ接続エラー処理
            //e.printStackTrace();
            isServer = false;       //サーバ接続失敗　フラグfalse
        }
        finally{                    //try catchのあと必ず実行される
            if(server != null) {
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }


    /*** doInBackgraundメソッドの後にUIスレッド上で実行される ***/
    @Override
    protected void onPostExecute(Long aLong)
    {
        super.onPostExecute(aLong);

        if(!isServer){  //サーバ接続エラー時処理
            Toast.makeText(context, "Failed connecting server", Toast.LENGTH_SHORT).show();
            tglbtnConnect.setChecked(false);

            return;
        }
        else{
            return;
        }
    }

    @Override
    protected void onProgressUpdate(String... str)
    {
        super.onProgressUpdate(str);

        switch(str[0]){
            case "connected":
                Toast.makeText(context, "Connected Server", Toast.LENGTH_SHORT).show();
                break;
            case "receive":
                carInfo.setData(str[1]);
                break;
            case "logout":
                Toast.makeText(context, "Logout Server", Toast.LENGTH_SHORT).show();
                tglbtnConnect.setChecked(false);
                break;
            default:
                Toast.makeText(context, "???", Toast.LENGTH_SHORT);
                break;
        }
    }

    public void sendData(String str)
    {
        isSendData = true;
        strSend = str;

        return;
    }

    @Override
    public void onCancel()
    {
        isSendData = true;
        strSend = "logout";
        return;
    }
}
