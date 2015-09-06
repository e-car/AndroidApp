package com.example.u310_adm.socketclient;

import android.widget.TextView;

/**53
 * Created by U310-Adm on 2015/07/04.
 */

/*値を渡すクラス*/

public class CarInformation {

    int wordCount;  //setDataされた文字が何文字目か数える
    int wordLimit = 35;
    String[] dataArray = new String[wordLimit]; //入力文字を入れておく配列
    double voltage1;     //電圧
    double ampere1;
    double temperature1;
    TextView tvDebug;

    /*コンストラクタ*/
    public CarInformation(TextView txtDebug){
        this.tvDebug = txtDebug;
    }

    void setData(String data){
        data = data.replaceAll("\n", "");
        data = data.replaceAll("\r\n", "");
        if(data == "s"){
            wordCount = 0;
        }
        wordCount++;
        if(wordCount > wordLimit-1){     //配列を超えないように
            wordCount = wordLimit-1;
        }
        dataArray[wordCount] = data;    //WordCount番目に入力文字を入れる
        tvDebug.setText(String.valueOf(wordCount));
    }

    /*96V電圧取得********/
    String getVoltage(){
        if(wordCount < 5){
            return "未取得1";
        }
        voltage1 = Integer.parseInt(dataArray[2]) * 10 + Integer.parseInt(dataArray[3]) + Integer.parseInt(dataArray[5])*0.1;
        return String.valueOf(voltage1);
    }
    /*96V電流取得******/
    String  getAmpere(){
        if(wordCount < 10){
            return "未取得1";
        }
        ampere1 = Integer.parseInt(dataArray[7]) * 10 + Integer.parseInt(dataArray[8]) + Integer.parseInt(dataArray[10])*0.1;
        return String.valueOf(ampere1);
    }
    /*モーター温度取得*****/
    String getTemperature(){
        if(wordCount < 15){
            return "未取得1";
        }
        temperature1 = Integer.parseInt(dataArray[12]) * 10 + Integer.parseInt(dataArray[13]) + Integer.parseInt(dataArray[15])*0.1;
        return String.valueOf(temperature1);
    }
}
