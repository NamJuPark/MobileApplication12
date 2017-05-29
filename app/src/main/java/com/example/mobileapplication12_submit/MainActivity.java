package com.example.mobileapplication12_submit;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TextView tvCount;
    EditText etSec;
    int [] drink = {0,1,2,3,4};
    String [] name = {"헛개수","포카리","콜라","쿠우","맑은티엔"};
    ImageView imgDrink;
    int index = 0, time = 0;
    ChangeSec changeSec;
    ChangeImg changeImg;
    boolean checkRunning = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        tvCount = (TextView)findViewById(R.id.tvCount);
        etSec = (EditText)findViewById(R.id.editText);
        imgDrink = (ImageView)findViewById(R.id.imageView);
    }

    public void setDrink(int i) {
        switch (i){
            case 0:
                imgDrink.setImageResource(R.drawable.drink1);
                break;
            case 1:
                imgDrink.setImageResource(R.drawable.drink2);
                break;
            case 2:
                imgDrink.setImageResource(R.drawable.drink3);
                break;
            case 3:
                imgDrink.setImageResource(R.drawable.drink4);
                break;
            case 4:
                imgDrink.setImageResource(R.drawable.drink5);
                break;
        }
    }

    public class ChangeImg extends AsyncTask<Integer,Integer,Void>{
        @Override
        protected void onPreExecute() {//첫 시작
            int i = 0;
            super.onPreExecute();
            setDrink(i);
        }
        @Override
        protected Void doInBackground(Integer... integers) {
            while(!isCancelled()){
                try {
                    Thread.sleep(integers[0]);
                    if(index >= 5)
                        index = 0;
                    publishProgress(++index);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        @Override
        protected void onProgressUpdate(Integer... values) {//doIn에서 호출 하면 들어가는 UI조정자
            super.onProgressUpdate(values);
            setDrink(values[0]);
        }
        @Override
        protected void onCancelled() {//바로 취소했을때
            super.onCancelled();
            setDrink(index);
        }
    }



    public class ChangeSec extends AsyncTask<Integer,Integer,Void>{
        @Override
        protected void onPreExecute() {//첫 시작
            super.onPreExecute();
            tvCount.setVisibility(View.VISIBLE);
            tvCount.setText("시작부터 0초");
        }
        @Override
        protected Void doInBackground(Integer... integers) {
            while(!isCancelled()){
                try {
                    Thread.sleep(1000);
                    publishProgress(++time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        @Override
        protected void onProgressUpdate(Integer... values) {//doIn에서 호출 하면 들어가는 UI조정자
            super.onProgressUpdate(values);
            tvCount.setText("시작부터 "+ values[0] +"초");
        }
        @Override
        protected void onCancelled() {//바로 취소했을때
            super.onCancelled();
            tvCount.setVisibility(View.VISIBLE);
            tvCount.setText(name[index]+"선택 "+"("+ time +")");
        }
    }
    public void onClick(View view){
        if(view.getId() == R.id.button){
            if(!changeImg.isCancelled()) changeImg.cancel(true);
            if(!changeSec.isCancelled())changeSec.cancel(true);
            checkRunning = false;
            imgDrink.setImageResource(R.drawable.start);
            tvCount.setText("시작부터 0초");
            tvCount.setVisibility(View.INVISIBLE);
            etSec.setText(null);
        }
        else if(view.getId() == R.id.imageView){
            if(etSec.getText().toString().equals("")){
                Toast.makeText(getApplication(),"입력",Toast.LENGTH_SHORT).show();
            }
            if(checkRunning){//쓰레드가 돌아가고 있을 때
                changeImg.cancel(true);
                changeSec.cancel(true);
                checkRunning = false;
            }
            else{
                int sec = Integer.parseInt(etSec.getText().toString());
                changeImg = new ChangeImg();
                changeSec = new ChangeSec();
                changeImg.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, sec * 1000);
                changeSec.execute(0);
                checkRunning = true;
            }
        }
    }
}
