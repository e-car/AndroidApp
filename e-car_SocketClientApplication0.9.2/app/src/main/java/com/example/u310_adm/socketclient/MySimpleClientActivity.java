package com.example.u310_adm.socketclient;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import static android.view.View.OnClickListener;


public class MySimpleClientActivity extends Activity implements OnCheckedChangeListener, OnClickListener
{
    /*** ウィジェットオブジェクト作成 ***/
    private EditText etxtServerIP;
    private EditText etxtServerPort;
    private ToggleButton tglbtnConnect;
    private EditText etxtSendStr;
    private Button btnSend;
    private TextView txtDebug;
    private TextView txtVoltage;
    private TextView txtAmpere;
    private TextView txtTemperature;
    private  CarInformation carInfo;    //デバッグ用に数字を入れてるから必要なだけ

    /*ソケット通信*/
    String ServerIP = "192.168.111.111";
    String ServerPort = "3000";
    String[] splitIP;

    /*** クライアントタスクオブジェクト作成 ***/
    private MyClientAsyncTask clientTask;
    private MyDisplayAsyncTask displayTask;
    private SocketControl socketCtrl;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_simple_client);

        /*** ウィジェットインスタンス作成 ***/
        etxtServerIP = (EditText)findViewById(R.id.etxtServerIP);
        etxtServerPort = (EditText)findViewById(R.id.etxtServerPort);
        tglbtnConnect = (ToggleButton)findViewById(R.id.tglbtnConnect);
        etxtSendStr = (EditText)findViewById(R.id.etxtSendStr);
        btnSend = (Button)findViewById(R.id.btnSend);
        txtDebug = (TextView)findViewById(R.id.debugView);
        txtVoltage = (TextView)findViewById(R.id.voltage);
        txtAmpere = (TextView)findViewById(R.id.ampere);
        txtTemperature = (TextView)findViewById(R.id.temperature);
        carInfo = new CarInformation(txtDebug);
        socketCtrl = new SocketControl();
        displayTask = new MyDisplayAsyncTask(this, carInfo, socketCtrl, txtVoltage, txtTemperature, txtAmpere);

        /*** 各種リスナー登録 ***/
        tglbtnConnect.setOnCheckedChangeListener(this);
        btnSend.setOnClickListener(this);

        displayTask.executeOnExecutor(displayTask.THREAD_POOL_EXECUTOR, "start");   //ディスプレイの更新開始

        carInfo.setData("s");
        carInfo.setData("1");
        carInfo.setData("2");
        carInfo.setData(".");
        carInfo.setData("4");
        carInfo.setData(",");
        carInfo.setData("2");
        carInfo.setData("5");
        carInfo.setData(".");
        carInfo.setData("2");
        carInfo.setData(",");
        carInfo.setData("5");
        carInfo.setData("7");
        carInfo.setData(".");
        carInfo.setData("1");
        carInfo.setData("end");

//        txtVoltage.setText(String.valueOf(carInfo.getVoltage()));


    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_simple_client, menu);
//        return true;
//    }

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

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
    {
        if((buttonView == tglbtnConnect) & (isChecked)){

            ServerIP = etxtServerIP.getText().toString();   //入力されたサーバのIPアドレス取得
            ServerPort = etxtServerPort.getText().toString(); //入力されたサーバのPortを取得

            //エディットテキストの入力チェック
            if(ServerIP.equals("") | ServerPort.equals("")){        //テキストが空の時
                Toast.makeText(this, "Please Input Server Information", Toast.LENGTH_SHORT).show();
                tglbtnConnect.setChecked(false);
                return;
            }

            //入力がIPアドレスの形になっているかチェックする
            splitIP = ServerIP.split("\\.");    //カンマで分割
            if(splitIP.length < 4){            //IPアドレスの形式なら4つ以上の配列になるはず
                Toast.makeText(this, "Can't use the Server IP", Toast.LENGTH_SHORT).show();
                tglbtnConnect.setChecked(false);
                return;
            }

            //Socket通信起動
            socketCtrl.startSocket(this, ServerIP, ServerPort,tglbtnConnect, carInfo);
//            clientTask = new MyClientAsyncTask(this, ServerIP, Integer.parseInt(ServerPort),tglbtnConnect, carInfo);   //クライアントインスタンス作成
//            clientTask.executeOnExecutor(clientTask.THREAD_POOL_EXECUTOR, "start");    //クライアントスレッドスタート
        }
        else if((buttonView == tglbtnConnect) & (!isChecked)){
            socketCtrl.socketLogout();
        }
    }

    @Override
    public void onClick(View v)
    {
        if(v == btnSend){
            socketCtrl.socketSend(etxtSendStr.getText().toString());
        }

    }
}
