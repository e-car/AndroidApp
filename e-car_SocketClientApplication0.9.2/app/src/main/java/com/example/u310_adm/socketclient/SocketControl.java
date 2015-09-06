package com.example.u310_adm.socketclient;

import android.content.Context;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * Created by U310-Adm on 2015/08/29.
 */
public class SocketControl {

    MyClientAsyncTask clientAsyncTask;

    /*コンストラクタ*/
    public SocketControl(){
    }

    void startSocket(Context Main, String ServerIP, String ServerPort, ToggleButton tglbtnConnect, CarInformation carInfo){
        clientAsyncTask = new MyClientAsyncTask(Main, ServerIP, Integer.parseInt(ServerPort),tglbtnConnect, carInfo);
        clientAsyncTask.executeOnExecutor(clientAsyncTask.THREAD_POOL_EXECUTOR, "start");
    }

    void socketLogout(){
        clientAsyncTask.onCancel();
    }

    void socketSend(String str){
        if(clientAsyncTask != null){
            clientAsyncTask.sendData(str);
        }
    }

}
