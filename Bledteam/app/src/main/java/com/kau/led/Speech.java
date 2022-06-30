package com.kau.led;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class Speech extends AppCompatActivity implements TextToSpeech.OnInitListener { //OnInitListener는 인터페이스니까 사용하기 위해 implement 함
    private Button btTts;                                                           //인터페이스는 추상 클래스 이고 함수 구현해서 사용해야 함 : implement
    private EditText edTts;
    private TextToSpeech myTts;
    private Button btVoice;
    private TextView txVoice;
    private final int myVoiceCode = 1234;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech);
        btTts = findViewById(R.id.btTts);
        btVoice = findViewById(R.id.btVoice);
        edTts = findViewById(R.id.edTts);
        txVoice = findViewById(R.id.txVoice);
        btTts.setOnClickListener(new View.OnClickListener() { //Event Listener
            @Override
            public void onClick(View v) {
                final String msg = edTts.getText().toString(); //리턴값이 스트링이이 아니기 떄문에 스트링으로 변경
                speakTextThread(msg);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        speakText(msg);

                    }
                }).start();
            }
         });
        btVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVoiceIntent();
            }
        });

        myTts = new TextToSpeech(this,this); // Speech.class 상속 2개 받았으니까 this지만 상속은 다른 extends AppCompatActivity implements TextToSpeech.OnInitListener
    }
    protected  void startVoiceIntent(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.KOREAN);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Recognizing...");
        startActivityForResult(intent,myVoiceCode);

    }
    protected void speakText(String msg) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            myTts.speak(msg, TextToSpeech.QUEUE_FLUSH, null, null); //말하는 중간에 다른 동작이 가능토록 함
            while (myTts.isSpeaking()) {
                try {
                    Thread.sleep(100);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    protected void speakTextThread(final String msg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                speakText(msg);

            }
        }).start();
    }
    protected String getRecogStr(Intent data) {
       ArrayList<String> result =  data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        return result.get(0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == myVoiceCode && resultCode == RESULT_OK && data != null){
            String msg = getRecogStr(data);
            txVoice.setText(msg);
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            myTts.setLanguage(Locale.KOREAN);
            myTts.setPitch(1.0f);
            myTts.setSpeechRate(1.0f);
        }
    }



}